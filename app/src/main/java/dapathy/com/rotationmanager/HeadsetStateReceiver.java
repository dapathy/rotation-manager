package dapathy.com.rotationmanager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

// Headset events cannot be registered in the manifest.
public class HeadsetStateReceiver extends BroadcastReceiver {

	private static final String TAG = "HeadsetStateReceiver";
	private LinearLayout orientationChanger;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "ReceivedEvent");

		if (intent.hasExtra("state")){
			int state = intent.getIntExtra("state", 0);

			// Unplugged
			if (state == 0){
				revert(context);

			// Plugged in.
			} else if (state == 1){
				rotateScreen(context);
			}
		}
	}

	private void rotateScreen(Context context) {
		orientationChanger = new LinearLayout(context);
		WindowManager.LayoutParams orientationLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 0, PixelFormat.RGBA_8888);

		orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
		WindowManager windowManager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
		windowManager.addView(orientationChanger, orientationLayout);
		orientationChanger.setVisibility(View.VISIBLE);
	}

	public void revert(Context context) {
		if (orientationChanger == null) return;
		WindowManager windowManager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
		windowManager.removeView(orientationChanger);
	}
}
