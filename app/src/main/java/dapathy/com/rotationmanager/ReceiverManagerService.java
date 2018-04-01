package dapathy.com.rotationmanager;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
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

		setEnabled(true);
		IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);

		if (receiver == null)
			receiver = new HeadsetStateReceiver();
		this.registerReceiver( receiver, receiverFilter );
	}

	private void unregisterReceiver() {
		setEnabled(false);
		if (receiver == null) return;

		receiver.revert(this);
		this.unregisterReceiver(receiver);
	}

	private void setEnabled(boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(Utility.ENABLED, value);
		editor.apply();
	}
}
