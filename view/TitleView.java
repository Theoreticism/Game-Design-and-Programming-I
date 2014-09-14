package germanwhist.view;

import germanwhist.activity.GameActivity;
import germanwhist.activity.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Christopher Lee
 */
public class TitleView extends View {
	
	private Bitmap titleGraphic;
	private Bitmap playButtonUp;
	private Bitmap playButtonDown;
	private Bitmap optionsButtonUp;
	private Bitmap optionsButtonDown;
	private int screenW;
	private int screenH;
	private boolean playButtonPressed;
	private boolean optionsButtonPressed;
	private Context myContext;
	
	public TitleView(Context context) {
		super(context);
		myContext = context;
		titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_graphic);
		playButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.play_button_up);
		playButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.play_button_down);
		optionsButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.options_button_up);
		optionsButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.options_button_down);
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
	}
	
	/**
	 * Draws the textures and shapes needed for the UI.
	 * 
	 * @param canvas The canvas on which the background will be drawn.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(titleGraphic, (screenW-titleGraphic.getWidth())/2, (int)(screenH*0.1), null);
		
		if (playButtonPressed) {
			canvas.drawBitmap(playButtonDown, (screenW-playButtonUp.getWidth())/2, (int)(screenH*0.6), null);
		} else {
			canvas.drawBitmap(playButtonUp, (screenW-playButtonUp.getWidth())/2, (int)(screenH*0.6), null);
		}
		
		if (optionsButtonPressed) {
			canvas.drawBitmap(optionsButtonDown, (screenW-optionsButtonDown.getWidth())/2, (int)(screenH*0.8), null);
		} else {
			canvas.drawBitmap(optionsButtonUp, (screenW-optionsButtonUp.getWidth())/2, (int)(screenH*0.8), null);
		}
		
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
			if (X > (screenW-playButtonUp.getWidth())/2 && X < ((screenW-playButtonUp.getWidth())/2) + 
					playButtonUp.getWidth() && Y > (int)(screenH*0.6) && Y < (int)(screenH*0.6) +
					playButtonUp.getHeight()) {
						playButtonPressed = true;
					}
			if (X > (screenW-optionsButtonUp.getWidth())/2 && X < ((screenW-optionsButtonUp.getWidth())/2) + 
			optionsButtonUp.getWidth() && Y > (int)(screenH*0.8) && Y < (int)(screenH*0.8) +
			optionsButtonUp.getHeight()) {
				optionsButtonPressed = true;
			}
			break;
			
		case MotionEvent.ACTION_MOVE:
			break;
			
		case MotionEvent.ACTION_UP:
			if (playButtonPressed) {
				//Starts the GameActivity when the PLAY button is pressed. 
				Intent gameIntent = new Intent(myContext, GameActivity.class);
				myContext.startActivity(gameIntent);
			}
			playButtonPressed = false;
			optionsButtonPressed = false;
			break;
		}
		invalidate();
		return true;
	}
	
}
