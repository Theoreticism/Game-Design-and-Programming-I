package germanwhist.player;

import germanwhist.card.Card;

import java.util.List;
import java.util.Random;

public class ComputerPlayer {

	Random random;
	
	/**
	 * Takes the bot's hand, the current suit and rank of the card and the trump
	 * suit to make a valid play. Must be of same suit if possible 
	 * 
	 * @param hand The bot's hand.
	 * @param suit Suit of card played.
	 * @param rank Rank of card played.
	 * @param trumpSuit Trump suit of current game.
	 * @return Valid play for current trick.
	 */
	public int makePlay(List<Card> hand, int suit, int rank, int trumpSuit) {
		int play = 0;
		/*
		for (int i = 0; i < hand.size(); i++) {
			int tempId = hand.get(i).getId();
			// TODO: rank to be used to determine tactical play
			// TODO: trump suit to be used to determine tactical play
			
			int tempSuit = hand.get(i).getSuit();
			if (suit == tempSuit || trumpSuit == tempSuit) {
				play = tempId;
			}
		}
		*/
		// Currently just plays a set card from its hand
		random = new Random();
		int tempRan = random.nextInt(hand.size());
		play = hand.get(tempRan).getId();
		return play;
	}
	
}
