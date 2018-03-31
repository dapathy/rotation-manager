package dapathy.com.rotationmanager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

public class Utility {
	private static final String ENABLED = "enabled";

	private static HeadsetStateReceiver receiver;

	public static boolean isEnabled(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(ENABLED, false);
	}

	public static void registerHeadsetReceiver(Context context) {
		if (!hasSettingPermissions(context)) return;

		setEnabled(context, true);
		IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);

		if (receiver == null)
			receiver = new HeadsetStateReceiver();
		context.registerReceiver( receiver, receiverFilter );
	}

	public static void unregisterHeadsetReceiver(Context context) {
		setEnabled(context, false);
		if (receiver == null) return;

		context.unregisterReceiver(receiver);
	}

	public static boolean hasSettingPermissions(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			return Settings.System.canWrite(context);

		return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
	}

	private static void setEnabled(Context context, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(ENABLED, value);
		editor.apply();
	}
}
