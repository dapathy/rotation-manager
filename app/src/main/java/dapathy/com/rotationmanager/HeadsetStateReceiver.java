package dapathy.com.rotationmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.Surface;

// Headset events cannot be registered in the manifest.
public class HeadsetStateReceiver extends BroadcastReceiver {

	private static final String TAG = "HeadsetStateReceiver";

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
		// Disable accelerator rotation
		Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);

		// Reverse portrait.
		Settings.System.putInt(context.getContentResolver(), Settings.System.USER_ROTATION, Surface.ROTATION_180);
	}

	private void revert(Context context) {
		// Enable accelerator rotation
		Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);

		// Reverse portrait.
		Settings.System.putInt(context.getContentResolver(), Settings.System.USER_ROTATION, Surface.ROTATION_0);
	}
}
