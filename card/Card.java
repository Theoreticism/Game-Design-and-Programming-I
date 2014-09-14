package germanwhist.card;

import android.graphics.Bitmap;

/**
 * @author Christopher Lee
 */
public class Card {
	
	private int id;
	private int suit;
	private int rank;
	private Bitmap bmp;
	
	public Card(int newId) {
		id = newId;
		suit = Math.round((id/100) * 100);
		rank = id - suit;
	}
	
	/**
	 * Sets the bitmap for the specified card.
	 * 
	 * @param newBitmap Bitmap to be set for the card.
	 */
	public void setBitmap(Bitmap newBitmap) {
		bmp = newBitmap;
	}
	
	/**
	 * Gets the bitmap of the specified card.
	 * 
	 * @return Bitmap for the specified card.
	 */
	public Bitmap getBitmap() {
		return bmp;
	}
	
	/**
	 * Gets the id of the specified card.
	 * 
	 * @return Id for the specified card.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the suit of the specified card.
	 * 
	 * @return Suit for the specified card.
	 */
	public int getSuit() {
		return suit;
	}
	
	/**
	 * Gets the rank of the specified card.
	 * 
	 * @return Rank for the specified card.
	 */
	public int getRank() {
		return rank;
	}
	
}
