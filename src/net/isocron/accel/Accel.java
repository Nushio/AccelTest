package net.isocron.accel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

public class Accel extends Activity {

	private ShakeListener mShaker;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.i("Test", "Ready!");
		final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
//		vibe.vibrate(500);
		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
			@Override
			public void onMediumShake() {
				vibe.vibrate(80);
				Log.i("Accel","Medium Shaken!");
				vibe.vibrate(100);
		        new AlertDialog.Builder(Accel.this).setPositiveButton(android.R.string.ok, null).setMessage("Medium Shaken!").show();

			}

			@Override
			public void onStrongShake() {
				vibe.vibrate(120);
				Log.i("Accel","Strong Shaken!");
				new AlertDialog.Builder(Accel.this).setPositiveButton(android.R.string.ok, null).setMessage("Strong Shaken!").show();
			}

			@Override
			public void onWeakShake() {
				vibe.vibrate(20);
				Log.i("Accel","Weak Shaken!");				
				new AlertDialog.Builder(Accel.this).setPositiveButton(android.R.string.ok, null).setMessage("Weak Shaken!").show();
			}

			@Override
			public void onHadokenShake() {
				vibe.vibrate(150);
				Log.i("Accel","Hadoken!");				
				new AlertDialog.Builder(Accel.this).setPositiveButton(android.R.string.ok, null).setMessage("Hadoken!").show();				
			}
		});
	}

	@Override
	public void onResume()
	{
		mShaker.resume();
		super.onResume();
	}
	@Override
	public void onPause()
	{
		mShaker.pause();
		super.onPause();
	}
}
