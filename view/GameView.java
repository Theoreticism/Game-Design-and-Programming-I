package germanwhist.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import germanwhist.activity.GermanWhistActivity;
import germanwhist.activity.R;
import germanwhist.card.Card;
import germanwhist.player.ComputerPlayer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Christopher Lee
 */
public class GameView extends View {

	private Context myContext;
	private List<Card> deck = new ArrayList<Card>();
	private List<Card> myHand = new ArrayList<Card>();
	private List<Card> oppHand = new ArrayList<Card>();
	private List<Card> myDiscardPile = new ArrayList<Card>();
	private List<Card> botDiscardPile = new ArrayList<Card>();
	private int scaledCardW;
	private int scaledCardH;
	private int screenW;
	private int screenH;
	private int oppScore;
	private int myScore;
	private int currentSuit = 0;
	private int currentRank = 0;
	private int trumpSuit;
	private int movingCardIdx = -1;
	private int movingX;
	private int movingY;
	protected int counter = 0;
	private float scale;
	private Paint blackPaint;
	private Bitmap cardBack;
	private Bitmap cardEmpty;
	private Bitmap nextCardButton;
	private boolean myTurn;
	private boolean validPlay = true;
	private ComputerPlayer computerPlayer = new ComputerPlayer();
	
	/**
	 * GameView constructor. Scales text size to be 15 times the factor. 
	 * On displays with density 1, the font size would be 15.
	 * 
	 * @param context Context for the constructor.
	 */
	public GameView(Context context) {
		super(context);
		myContext = context;
		scale = myContext.getResources().getDisplayMetrics().density;
		blackPaint = new Paint();
		blackPaint.setAntiAlias(true);
		blackPaint.setColor(Color.BLACK);
		blackPaint.setStyle(Paint.Style.STROKE);
		blackPaint.setTextAlign(Paint.Align.LEFT);
		blackPaint.setTextSize(scale*15);
	}
	
	/**
	 * Called when the size of this view has changed.
	 * 
	 * @param w Current width of this view.
	 * @param h Current height of this view.
	 * @param oldw Old width of this view.
	 * @param oldh Old height of this view.
	 */
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		screenW = w;
		screenH = h;
		initCards();
		dealCards();
		Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.card_back);
		Bitmap tempBitmap2 = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.card_empty);
		scaledCardW = (int)(screenW/8);
		scaledCardH = (int)(scaledCardW*1.28);
		cardBack = Bitmap.createScaledBitmap(tempBitmap, scaledCardW, scaledCardH, false);
		cardEmpty = Bitmap.createScaledBitmap(tempBitmap2, scaledCardW, scaledCardH, false);
		nextCardButton = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_next); 
		trumpSuit = deck.get(0).getSuit();
		myTurn = new Random().nextBoolean();
		if (myTurn == false) {
			Toast.makeText(myContext, "Computer starts.", Toast.LENGTH_SHORT).show();
			makeBotPlay();
		} else {
			Toast.makeText(myContext, "Player starts.", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Draws the textures and shapes needed for the UI.
	 * 
	 * @param canvas The canvas on which the background will be drawn.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawText("Computer Tricks Won: " + Integer.toString(oppScore), 10, blackPaint.getTextSize() + 10, blackPaint);
		canvas.drawText("My Tricks Won: " + Integer.toString(myScore), 10, screenH - blackPaint.getTextSize() - 10, blackPaint);
		if (trumpSuit == 100) {
			canvas.drawText("Trump Suit: ", 10, screenH/2 - blackPaint.getTextSize(), blackPaint);
			canvas.drawText("Diamonds", 10, screenH/2 + blackPaint.getTextSize(), blackPaint);
		} else if (trumpSuit == 200) {
			canvas.drawText("Trump Suit: ", 10, screenH/2 - blackPaint.getTextSize(), blackPaint);
			canvas.drawText("Clubs", 10, screenH/2 + blackPaint.getTextSize(), blackPaint);
		} else if (trumpSuit == 300) {
			canvas.drawText("Trump Suit: ", 10, screenH/2 - blackPaint.getTextSize(), blackPaint);
			canvas.drawText("Hearts", 10, screenH/2 + blackPaint.getTextSize(), blackPaint);
		} else {
			canvas.drawText("Trump Suit: ", 10, screenH/2 - blackPaint.getTextSize(), blackPaint);
			canvas.drawText("Spades", 10, screenH/2 + blackPaint.getTextSize(), blackPaint);
		}
		for (int i = 0; i < myHand.size(); i++) {
			if (i < 7) {
				canvas.drawBitmap(myHand.get(i).getBitmap(), i*(scaledCardW+5), screenH-scaledCardH-blackPaint.getTextSize()-(50*scale), null);
			}
			if (myHand.get(i).getSuit() == currentSuit) {
				validPlay = true;
				break;
			}
			validPlay = false;
		}
		for (int i = 0; i < oppHand.size(); i++) {
			canvas.drawBitmap(cardBack, i*(scale*5), blackPaint.getTextSize()+(50*scale), null);
		}
		if (myDiscardPile.isEmpty() == true) {
			canvas.drawBitmap(cardEmpty, (screenW/2) + 10, (screenH/2) + 10, null);
		} else {
			canvas.drawBitmap(myDiscardPile.get(0).getBitmap(), (screenW/2) + 10, (screenH/2) + 10, null);
		}
		if (botDiscardPile.isEmpty() == true) {
			canvas.drawBitmap(cardEmpty, (screenW/2) + 10, (screenH/2) - cardBack.getHeight() - 10, null);
		} else {
			canvas.drawBitmap(botDiscardPile.get(0).getBitmap(), (screenW/2) + 10, (screenH/2) - cardBack.getHeight() - 10, null);
		}
		if (deck.isEmpty() == true) {
			canvas.drawBitmap(cardEmpty, (screenW/2) - cardBack.getWidth() - 10, (screenH/2) - (cardBack.getHeight()/2), null);
		} else {
			canvas.drawBitmap(deck.get(0).getBitmap(), (screenW/2) - cardBack.getWidth() - 10, (screenH/2) - (cardBack.getHeight()/2), null);
		}
		if (myHand.size() > 7) {
			canvas.drawBitmap(nextCardButton, 
					screenW-nextCardButton.getWidth()-(30*scale), 
					screenH-nextCardButton.getHeight()-scaledCardH-(90*scale), 
					null);
		}
		if (myHand.isEmpty() == true && oppHand.isEmpty() == true) {
			endGame();
		}
		for (int i = 0; i < myHand.size(); i++) {
			if (i == movingCardIdx) {
				canvas.drawBitmap(myHand.get(i).getBitmap(), movingX, movingY, null);
			} else {
				if (i < 7) {
					canvas.drawBitmap(myHand.get(i).getBitmap(), i*(scaledCardW+5), 
							screenH-scaledCardH-blackPaint.getTextSize()-(50*scale), null);
				}
			}
		}
		invalidate();
	} 
	
	/**
	 * Handles touch screen motion events.
	 * 
	 * @param event The motion event.
	 * @return True if the event was handled, false otherwise. 
	 */
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		int X = (int)event.getX();
		int Y = (int)event.getY();
		
		switch(eventaction) {
		
		case MotionEvent.ACTION_DOWN:
			if (myTurn) {
				for (int i = 0; i < 7; i++) {
					if (X > i*(scaledCardW+5) && X < i*(scaledCardW+5) + scaledCardW && 
							Y > screenH-scaledCardH-blackPaint.getTextSize() - (50*scale)) {
						movingCardIdx = i;
						movingX = X-(int)(30*scale);
						movingY = Y-(int)(70*scale);
					}
				}
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			movingX = X-(int)(30*scale);
			movingY = Y-(int)(30*scale);
			break;
			
		case MotionEvent.ACTION_UP:
			if (movingCardIdx > -1 && X > (screenW/2)-(100*scale) && X < (screenW/2)+(100*scale) &&
					Y > (screenH/2)-(100*scale) && Y < (screenH/2)+(100*scale)) {
				if ((myHand.get(movingCardIdx).getSuit() == currentSuit ||
						myHand.get(movingCardIdx).getSuit() == trumpSuit) && validPlay == true) {
					currentSuit = myHand.get(movingCardIdx).getSuit();
					currentRank = myHand.get(movingCardIdx).getRank();
					myDiscardPile.add(0, myHand.get(movingCardIdx));
					myHand.remove(movingCardIdx);
					counter++;
					if (counter >= 2 && trickOutcome(2) == true) {
						playerWin();
					} else if (counter >= 2 && trickOutcome(2) == false) {
						botWin();
					} else if (counter < 2) {
						myTurn = false;
						makeBotPlay();
					}
				} else if ((myHand.get(movingCardIdx).getSuit() != currentSuit ||
						myHand.get(movingCardIdx).getSuit() != trumpSuit) && validPlay == true) {
					showWrongSuitDialog();
				} else if (validPlay == false){
					currentSuit = myHand.get(movingCardIdx).getSuit();
					currentRank = myHand.get(movingCardIdx).getRank();
					myDiscardPile.add(0, myHand.get(movingCardIdx));
					myHand.remove(movingCardIdx);
					counter++;
					if (counter >= 2 && trickOutcome(2) == true) {
						playerWin();
					} else if (counter >= 2 && trickOutcome(2) == false) {
						botWin();
					} else if (counter < 2) {
						myTurn = false;
						makeBotPlay();
					}
				}
			}
			movingCardIdx = -1;
	        if (myHand.size() > 7 &&
	        	X > screenW-nextCardButton.getWidth()-(30*scale) &&
	        	Y > screenH-nextCardButton.getHeight()-scaledCardH-(90*scale) &&
	        	Y < screenH-nextCardButton.getHeight()-scaledCardH-(60*scale)) {
	        		Collections.rotate(myHand, 1);
	        }
			break;
		}
		invalidate();
		return true;
	}
	
	/**
	 * Initializes the deck with a double loop for the suit and rank.
	 * Also attaches the associated bitmap for each card.
	 */
	private void initCards() {
		for (int i = 0; i < 4; i++) {
			for (int j = 102; j < 115; j++) {
				int tempId = j + (i*100);
				Card tempCard = new Card(tempId);
				int resourceId = getResources().getIdentifier("card"+tempId, "drawable", myContext.getPackageName());
				Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(), resourceId);
				scaledCardW = (int)(screenW/8);
				scaledCardH = (int)(scaledCardW*1.28);
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap, scaledCardW, scaledCardH, false);
				tempCard.setBitmap(scaledBitmap);
				deck.add(tempCard);
			}
		}
	}

	/**
	 * Draws a card from the deck and adds it to the specified hand.
	 * 
	 * @param handToDraw List of cards to add card to.
	 */
	private void drawCard(List<Card> handToDraw) {
		if (!deck.isEmpty()) {
			handToDraw.add(0, deck.get(0));
			deck.remove(0);
		}
		//If deck is empty, move on to phase 2 of GermanWhist gameplay!
	}
	
	/**
	 * Deals a hand of thirteen cards to the player and the computer after shuffling the deck.
	 */
	private void dealCards() {
		Collections.shuffle(deck, new Random());
		for (int i = 0; i < 13; i++) {
			drawCard(myHand);
			drawCard(oppHand);
		}
	}
	
	/**
	 * Getter for the trump suit.
	 * 
	 * @return the trump suit.
	 */
	public int getTrumpSuit() {
		return trumpSuit;
	}
	
	/**
	 * Dialog for attempting to play a card of the wrong suit.
	 */
	private void showWrongSuitDialog() {
		final Dialog wrongSuitDialog = new Dialog(myContext);
		wrongSuitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		wrongSuitDialog.setContentView(R.layout.wrong_suit_dialog);
		Button okButton = (Button) wrongSuitDialog.findViewById(R.id.okButton);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				wrongSuitDialog.dismiss();
			}
		});
		wrongSuitDialog.show();
	}
	
	/**
	 * Checks the outcome of the trick.
	 * 
	 * @param player indicator determining who is the first player of the current trick
	 * @return true if player won, false if bot won.
	 */
	private boolean trickOutcome(int player) {
		if ((botDiscardPile.get(0).getSuit() == trumpSuit) && (player == 1)) {
			return false;
		} else if ((myDiscardPile.get(0).getSuit() == trumpSuit) && (player == 2)) {
			return true;
		}
		if (myDiscardPile.get(0).getSuit() != botDiscardPile.get(0).getSuit() && (player == 1)) {
			return true;
		} else if (myDiscardPile.get(0).getSuit() != botDiscardPile.get(0).getSuit() && (player == 2)) {
			return false;
		}
		if (myDiscardPile.get(0).getSuit() == botDiscardPile.get(0).getSuit() &&
				myDiscardPile.get(0).getRank() > botDiscardPile.get(0).getRank()) {
			return true;
		} else return false;
	}
	
	/**
	 * Called when player wins a trick.
	 */
	private void playerWin() {
		Toast.makeText(myContext, "You won the trick!", Toast.LENGTH_SHORT).show();
		myScore++;
		drawCard(myHand);
		drawCard(oppHand);
		myTurn = true;
		counter = 0;
		currentSuit = 0;
		currentRank = 0;
	}
	
	/**
	 * Called when computer wins a trick.
	 */
	private void botWin() {
		Toast.makeText(myContext, "Opponent won the trick!", Toast.LENGTH_SHORT).show();
		oppScore++;
		drawCard(oppHand);
		drawCard(myHand);
		myTurn = false;
		counter = 0;
		makeBotPlay();
	}
	
	/**
	 * Handles bot play during bot's turn.
	 */
	private void makeBotPlay() {
		int tempPlay = computerPlayer.makePlay(oppHand, currentSuit, currentRank, trumpSuit);
		for (int i = 0; i < oppHand.size(); i++) {
			if (tempPlay == oppHand.get(i).getId()) {
				currentSuit = oppHand.get(i).getSuit();
				currentRank = oppHand.get(i).getRank();
				switch(currentSuit) {
					case 100: 
						if (currentRank == 11) {
							Toast.makeText(myContext, "Computer played: Jack of Diamonds.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 12) {
							Toast.makeText(myContext, "Computer played: Queen of Diamonds.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 13) {
							Toast.makeText(myContext, "Computer played: King of Diamonds.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 14) {
							Toast.makeText(myContext, "Computer played: Ace of Diamonds.", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(myContext, "Computer played: "+currentRank+" of Diamonds.", Toast.LENGTH_SHORT).show();
						}
						break;
					case 200: 
						if (currentRank == 11) {
							Toast.makeText(myContext, "Computer played: Jack of Clubs.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 12) {
							Toast.makeText(myContext, "Computer played: Queen of Clubs.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 13) {
							Toast.makeText(myContext, "Computer played: King of Clubs.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 14) {
							Toast.makeText(myContext, "Computer played: Ace of Clubs.", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(myContext, "Computer played: "+currentRank+" of Clubs.", Toast.LENGTH_SHORT).show();
						}
						break;
					case 300: 
						if (currentRank == 11) {
							Toast.makeText(myContext, "Computer played: Jack of Hearts.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 12) {
							Toast.makeText(myContext, "Computer played: Queen of Hearts.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 13) {
							Toast.makeText(myContext, "Computer played: King of Hearts.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 14) {
							Toast.makeText(myContext, "Computer played: Ace of Hearts.", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(myContext, "Computer played: "+currentRank+" of Hearts.", Toast.LENGTH_SHORT).show();
						}
						break;
					case 400: 
						if (currentRank == 11) {
							Toast.makeText(myContext, "Computer played: Jack of Spades.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 12) {
							Toast.makeText(myContext, "Computer played: Queen of Spades.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 13) {
							Toast.makeText(myContext, "Computer played: King of Spades.", Toast.LENGTH_SHORT).show();
						} else if (currentRank == 14) {
							Toast.makeText(myContext, "Computer played: Ace of Spades.", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(myContext, "Computer played: "+currentRank+" of Spades.", Toast.LENGTH_SHORT).show();
						}
						break;
					default:
						System.err.println("Check makeBotPlay function");
						break;
				}
				botDiscardPile.add(0, oppHand.get(i));
				oppHand.remove(i);
			}
		}
		counter++;
		if (counter >= 2 && trickOutcome(1) == true) {
			playerWin();
		} else if (counter >= 2 && trickOutcome(1) == false) {
			botWin();
		} else if (counter < 2) {
			myTurn = true;
		}
	}
		
	/**
	 * Displays winner and final score, handles game end and offers return to main screen option
	 */
	private void endGame() {
		final Dialog endGameDialog = new Dialog(myContext);
		endGameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		endGameDialog.setContentView(R.layout.end_game_dialog);
		//Sets end game text box depending on who won the game
		TextView endGameText = (TextView) endGameDialog.findViewById(R.id.endGameText);
		if (myScore > oppScore) {
			endGameText.setText("Player won the game with " + myScore + " tricks won!");
		} else if (oppScore > myScore) {
			endGameText.setText("Computer won the game with " + oppScore + " tricks won!");
		} else {
			endGameText.setText("Game ended with a tie! Player score: " + myScore + ". Computer score: " + oppScore + ".");
		}
		Button nextButton = (Button) endGameDialog.findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			//Restart to main menu
			public void onClick(View v) {
				Intent gameIntent = new Intent(myContext, GermanWhistActivity.class);
				myContext.startActivity(gameIntent);
				endGameDialog.dismiss();
			}
		});
		endGameDialog.show();
	}
}
