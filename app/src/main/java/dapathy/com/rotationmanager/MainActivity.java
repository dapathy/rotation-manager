package dapathy.com.rotationmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private static final int PERMISSION_CODE = 100;

	@Override
	protected void onStart() {
		super.onStart();

		verifyPermissions();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Button button = findViewById(R.id.toggleButton);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context = MainActivity.this;
				boolean isEnabled = Utility.isEnabled(context);

				if (isEnabled) {
					Utility.tryStopService(context);
					Toast.makeText(context, "Disabled", Toast.LENGTH_SHORT).show();
				}
				else {
					Utility.tryStartService(context);
					Toast.makeText(context, "Enabled", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void verifyPermissions() {
		Activity context = MainActivity.this;
		if (Utility.hasSettingPermissions(context)) return;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
				Uri.parse("package:" + getPackageName()));
			startActivityForResult(intent, PERMISSION_CODE);
		}
	}
}
