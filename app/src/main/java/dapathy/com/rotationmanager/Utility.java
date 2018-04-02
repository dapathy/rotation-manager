package dapathy.com.rotationmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class Utility {

	private static final String ENABLED = "enabled";

	public static boolean isEnabled(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(ENABLED, false);
	}

	public static void tryStartService(Context context) {
		if (!hasSettingPermissions(context)) return;

		Intent service = new Intent(context, ReceiverManagerService.class);
		context.startService(service);
		setEnabled(context, true);
	}

	public static void tryStopService(Context context) {
		try {
			Intent service = new Intent(context, ReceiverManagerService.class);
			context.stopService(service);
		}
		catch (Exception ignored) {

		}
		setEnabled(context, false);
	}

	public static boolean hasSettingPermissions(Context context) {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
	}

	private static void setEnabled(Context context, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(Utility.ENABLED, value);
		editor.apply();
	}
}
