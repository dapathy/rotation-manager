package dapathy.com.rotationmanager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
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
		int overlay = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		WindowManager.LayoutParams orientationLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, overlay, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

		orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
		WindowManager windowManager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
		windowManager.addView(orientationChanger, orientationLayout);
		orientationChanger.setVisibility(View.VISIBLE);
	}

	public void revert(Context context) {
		if (orientationChanger == null) return;
		WindowManager windowManager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
		orientationChanger.setVisibility(View.INVISIBLE);
		windowManager.removeView(orientationChanger);
		orientationChanger = null;
	}
}
