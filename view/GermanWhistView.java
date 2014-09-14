package germanwhist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Christopher Lee
 */
public class GermanWhistView extends View {
	
	private Paint redPaint;
	private int circleX;
	private int circleY;
	private float radius;

	public GermanWhistView(Context context) {
		super(context);
		redPaint = new Paint();
		redPaint.setAntiAlias(true);
		redPaint.setColor(Color.RED);
		circleX = 100;
		circleY = 100;
		radius = 30;
	}
	
	/**
	 * Draws the textures and shapes needed for the UI.
	 * 
	 * @param canvas The canvas on which the background will be drawn.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(circleX, circleY, radius, redPaint);
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
			break;
			
		case MotionEvent.ACTION_MOVE:
			break;
			
		case MotionEvent.ACTION_UP:
			circleX = X;
			circleY = Y;
			break;
		}
		invalidate();
		return true;
	}

}
