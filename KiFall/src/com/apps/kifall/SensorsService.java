package com.apps.kifall;




import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


public class SensorsService extends Service {
	
	 //Remote service that collects data from Sensors and runs the fall detection algorithm
	 private final IBinder myBinder = new MyLocalBinder(); //background task
	 private Handler handler; //timer write on file every 200ms
	
	 //Notification when running on background
	 private NotificationManager NotificMngr;
	 private boolean serviceRunning;
	 
	//Sensors
	SensorsManager mySensorListener;
	
	//Algorithm
	DetectFallsAlgorithm myAlgorithm;
	
	public class MyLocalBinder extends Binder{
		SensorsService getService() {
			return SensorsService.this;
		}
	}
	
	@Override
    public IBinder onBind(Intent intent) {
  		return myBinder;
  	 }
	
	@Override 
	public void onCreate(){

	     NotificMngr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);        
			// Display a notification about us starting.  
			//We put an icon in the status bar.        
			showNotification();    
			
		//Start Sensors Listening
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mySensorListener = new SensorsManager(sensorManager);
		mySensorListener.startListening();
		
    	myAlgorithm=new DetectFallsAlgorithm(mySensorListener);

		
		//Start running the detectctFallsionFalls algorithm
		handler = new Handler();
		handler.postDelayed(runnable, 1000); //wait 1 seconds when start button pressed
	}
     
	
	@Override
     public void onStart(Intent intent, int startId) {
         handleCommand(intent);
     }
	
     
     @Override
     public int onStartCommand(Intent intent, int flags, int startId) {
    
    	 receiveParams(intent); //update the location and orientation of the phone
    	 				//in case settings changed
			 
 		 serviceRunning=true;

         handleCommand(intent);
         
		// We want this service to continue running until it is explicitly
         // stopped, so return sticky.
         return START_STICKY; 
     }
     
     
     public void receiveParams(Intent intent){
    	 //Receive the intent and set position and orientation of the phone 
    	 Bundle extras = intent.getExtras(); 
	      String phoneLocation = extras.getString("PHONEPOSITION");
		  String phoneOrientation = extras.getString("PHONEORIENTATION");
			 Log.d("TAG", "PhonePosition: "+phoneLocation+ " phoneOrientation: "+phoneOrientation); 
 		 myAlgorithm.setPhoneLocation(phoneLocation);
 		 myAlgorithm.setPhoneOrientation(phoneOrientation);
     }
     
     public void onDestroy() {
    	 serviceRunning=false;
    	 clearNotification();
     }
	
     void handleCommand(Intent intent){
    	 
     }
	 
     /** * Show a notification while this service is running. */
 	private void showNotification() {
 		// In this sample, we'll use the same text for the ticker and the
 		// expanded notification
 		CharSequence text = getText(R.string.service_running);
 		// Set the icon, scrolling text and timestamp
 		Notification notification = new Notification(R.drawable.ic_launcher,
 				text, System.currentTimeMillis());
 		// The PendingIntent to launch our activity if the user selects this
 		// notification
 		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
 				new Intent(this, MainActivity.class), 0);
 		// Set the info for the views that show in the notification panel.
 		notification.setLatestEventInfo(this,
 				getText(R.string.service_running), text, contentIntent);
 		// Send the notification.
 		// We use a layout id because it is a unique number. We use it later to
 		// cancel.
 		NotificMngr.notify(R.string.service_running, notification);
 	}
 	
 	
 	
 	private void clearNotification() {
 		NotificMngr.cancel(R.string.service_running);
 	}
 
 //Runs the algorithm every 100ms. 
 private Runnable runnable = new Runnable() {
	   @Override
	   public void run() {
		   if(serviceRunning){
			  // Log.d("TAG","RuN");
			   
			   //Send a Broadcast Alert when a fall is detected
			   if( myAlgorithm.runAlgorithm()) {
				 Intent intentFD = new Intent("FALL_DETECTED");
				 intentFD.putExtra("FALL DETECTED","fall detected");
				 sendBroadcast(intentFD);
			   }
				 
			   handler.postDelayed(this, 100); //100ms, fs=100MHz
		   }
		   }
		   
	 
	};

}
