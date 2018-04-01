package dapathy.com.rotationmanager;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class ReceiverManagerService extends Service {
	private static final String TAG = "MANAGER";

	private HeadsetStateReceiver receiver;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		registerReceiver();
		Log.d(TAG, "Service created");
	}

	@Override
	public void onDestroy()
	{
		unregisterReceiver();
		receiver = null;

		Log.d(TAG, "Service destroyed");
		super.onDestroy();
	}

	private void registerReceiver() {
		if (!Utility.hasSettingPermissions(this)) return;

		IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);

		if (receiver == null)
			receiver = new HeadsetStateReceiver();
		this.registerReceiver( receiver, receiverFilter );
	}

	private void unregisterReceiver() {
		if (receiver == null) return;

		receiver.revert(this);
		this.unregisterReceiver(receiver);
	}
}
