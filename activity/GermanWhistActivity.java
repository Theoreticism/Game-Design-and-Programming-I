package germanwhist.activity;

import germanwhist.view.TitleView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Christopher Lee
 */
public class GermanWhistActivity extends Activity {
	
	/**
	 * Overrides the existing onCreate function. Called when the activity is starting.
	 * 
	 * @param savedInstanceState If the activity is being re-initialized, this Bundle contains 
	 * the most recent data supplied in. Otherwise it is null.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TitleView tView = new TitleView(this);
		tView.setKeepScreenOn(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(tView);
	}
	
}
