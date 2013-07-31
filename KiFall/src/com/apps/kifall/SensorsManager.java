package com.apps.kifall;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorsManager implements SensorEventListener{
	  private float[] mAccValues;
	  private float[] mOrientValues;
	  
	  private SensorManager mSensorManager; 
	  private Sensor mAccelerometer, mRotationVector;
	  
	  
	  public SensorsManager(SensorManager sensorManager){
		  mAccValues = new float[3];
		  mOrientValues = new float[3];
		  mSensorManager = sensorManager;
	  }
	  
	  public float[] getAcceleration(){
		  return mAccValues;
	  }
	  
	  public float[] getOrientation(){
		  return mOrientValues;
	  }
	  
	  public void startListening(){
		  	  //Get instance from sensors
			  mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		      mRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);	
		      //Register sensors
	          mSensorManager.registerListener(this, mAccelerometer, mSensorManager.SENSOR_DELAY_NORMAL);
		      mSensorManager.registerListener(this, mRotationVector, mSensorManager.SENSOR_DELAY_NORMAL);
		}
		
		public void stopListening() {
			//Unregistener sensors when data is not collected in order to save battery
			mSensorManager.unregisterListener(this);
		}

		
	  
	//SENSORS
		@Override
		public void onAccuracyChanged(Sensor sensor, int Accuracy) {
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			Sensor sensor = event.sensor; 
				//Accelerometer values
				if (sensor.getType()==Sensor.TYPE_ACCELEROMETER)
						mAccValues = event.values.clone();
				
				//Rotation vector uses the compass and gyroscope to get the orientation. 
				if(sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
					float[] rotationMatrix = new float[16];
					SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
					SensorManager.getOrientation(rotationMatrix, mOrientValues);	
					for(int i=0; i<3; i++)
						mOrientValues[i]=(float) Math.toDegrees(mOrientValues[i])+180.0f;//orientation from 0 to 360degrees
					
			  	}
				
		}
		
		
}
