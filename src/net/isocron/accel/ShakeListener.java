

/* The following code was written by Matthew Wiggins
 * and is released under the APACHE 2.0 license
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Force "strength" changes by Juan Rodriguez (Nushio)
 */
package net.isocron.accel;

import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.content.Context;
import java.lang.UnsupportedOperationException;

public class ShakeListener implements SensorListener 
{
	private static int FORCE_THRESHOLD_HADOKEN = 1200;
	private static int FORCE_THRESHOLD_STRONG = 750;
	private static int FORCE_THRESHOLD_MEDIUM = 500;
	private static int FORCE_THRESHOLD_WEAK = 350;
	private static final int TIME_THRESHOLD = 100;
	private static final int SHAKE_TIMEOUT = 500;
	private static final int SHAKE_DURATION = 1000;

	private SensorManager mSensorMgr;
	private float mLastX=-1.0f, mLastY=-1.0f, mLastZ=-1.0f;
	private long mLastTime;
	private OnShakeListener mShakeListener;
	private Context mContext;
	private long mLastShake;
	private long mLastForce;

	public interface OnShakeListener
	{
		public void onStrongShake();
		public void onMediumShake();
		public void onWeakShake();
		public void onHadokenShake();
	}

	public ShakeListener(Context context) 
	{ 
		mContext = context;
		resume();
	}

	public void setOnShakeListener(OnShakeListener listener)
	{
		mShakeListener = listener;
	}

	public void resume() {
		mSensorMgr = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorMgr == null) {
			throw new UnsupportedOperationException("Sensors not supported");
		}
		boolean supported = mSensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME);
		if (!supported) {
			mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
			throw new UnsupportedOperationException("Accelerometer not supported");
		}
	}

	public void pause() {
		if (mSensorMgr != null) {
			mSensorMgr.unregisterListener(this, SensorManager.SENSOR_ACCELEROMETER);
			mSensorMgr = null;
		}
	}

	public void onAccuracyChanged(int sensor, int accuracy) { }

	public void onSensorChanged(int sensor, float[] values) 
	{
		if (sensor != SensorManager.SENSOR_ACCELEROMETER) return;
		long now = System.currentTimeMillis();

		if ((now - mLastTime) > TIME_THRESHOLD) {
			long diff = now - mLastTime;
			float speed = Math.abs(values[SensorManager.DATA_X] + values[SensorManager.DATA_Y] + values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
			if(speed > 200)
				Log.i("ShakeListener","Speed: "+ speed);
			if (speed > FORCE_THRESHOLD_HADOKEN) {
				if(now - mLastShake > SHAKE_DURATION) {
					mLastShake = now;
					if (mShakeListener != null) { 
						Log.i("ShakeListener","Hadoken Shake Speed: "+ speed);
						mShakeListener.onHadokenShake(); 
					}
				}
				mLastForce = now;
			}else if (speed > FORCE_THRESHOLD_STRONG) {
				if(now - mLastShake > SHAKE_DURATION) {
					mLastShake = now;
					if (mShakeListener != null) { 
						Log.i("ShakeListener","Strong Shake Speed: "+ speed);
						mShakeListener.onStrongShake(); 
					}
				}
				mLastForce = now;
			}else if (speed > FORCE_THRESHOLD_MEDIUM) { 
				if(now - mLastShake > SHAKE_DURATION) {
					mLastShake = now;
					if (mShakeListener != null) { 
						Log.i("ShakeListener","Medium Shake Speed: "+ speed);
						mShakeListener.onMediumShake(); 
					}
				}
				mLastForce = now;
			}else if (speed > FORCE_THRESHOLD_WEAK) { 
				if(now - mLastShake > SHAKE_DURATION) {
					mLastShake = now;
					if (mShakeListener != null) { 
						Log.i("ShakeListener","Weak Shake Speed: "+ speed);
						mShakeListener.onWeakShake(); 
					}
				}
				mLastForce = now;
			}

			mLastTime = now;
			mLastX = values[SensorManager.DATA_X];
			mLastY = values[SensorManager.DATA_Y];
			mLastZ = values[SensorManager.DATA_Z];
		}
	}

}

