package dapathy.com.rotationmanager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class Utility {

	public static final String ENABLED = "enabled";

	public static boolean isEnabled(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(ENABLED, false);
	}

	public static void tryStartService(Context context) {
		if (!hasSettingPermissions(context)) return;

		Intent service = new Intent(context, ReceiverManagerService.class);
		context.startService(service);
	}

	public static void tryStopService(Context context) {
		try {
			Intent service = new Intent(context, ReceiverManagerService.class);
			context.stopService(service);
		}
		catch (Exception ignored) {

		}
	}

	public static boolean hasSettingPermissions(Context context) {
		boolean hasPermission;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			hasPermission = Settings.System.canWrite(context);
		else
			hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;

		if (!hasPermission) Log.d("UTILITY", "No permission");

		return hasPermission;
	}
}
