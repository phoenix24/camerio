package org.fictious.camerio;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Main extends Activity {
	private Preview preview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		preview = new Preview(this);
		setContentView(preview);
	}
}