package com.apps.kifall;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;

public class EmergencyAlert {
	private GPSTracker gps;
	private String phoneNumber, timeFall;
	private String message;
	Context mContext;
	
	public EmergencyAlert(Context context){
		Log.d("TAG", "emergencyalert created");
		mContext = context;
		gps = new GPSTracker(mContext);
	}
	
	public void setPhone(String phone){
		phoneNumber = phone;
	}
	
	public String getPhone(){
		return phoneNumber;
	}
	
	public void sendEmergencyAlert(){
		Log.d("TAG", "send emergency alert");
		
		//get the time when the fall had occurred
		timeFall = getTime();
		
		 /*We verify that the location providers are enabled when the app starts/resumes.
	      In case the user disable the gps and network providers while the app is running, 
	      we can't get the location. We assume the emergency contact knows the location
	      since the system is designed to run in an hospital/house environment.
	      We can't show the notification to the user to enable because if there is a fall
	      the user will be laying on the floor unable to use the phone*/
		 if(gps.canGetLocation()){
	        	double latitude = gps.getLatitude();
	        	double longitude = gps.getLongitude();
	        	String gmapsurl= "http://maps.google.com.au/maps?ll="+latitude+ "," +longitude;
	    		message = "ALERT: Fall detected at "+ timeFall + " Lat: "+latitude+ " Long: " +longitude +
	    				" Gmaps: "+gmapsurl;
		 }
		 else
			 message = "ALERT: Fall detected at "+ timeFall;
		
		sendSMS();
		//sendMMS();
	}
	
	public String getTime(){
		Log.d("TAG", "gettime");

		//Time
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //HH:mm:ss.SSS
		return sdf.format(cal.getTime());		           
	}
	
	private void sendSMS()
    {        
		Log.d("TAG", "send sms");

        PendingIntent pi = PendingIntent.getActivity(mContext, 0,
        new Intent(mContext, MainActivity.class), 0);                
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);        
    }    
	
	private void sendMMS() 
	{
		String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/KiFall/Image1.jpg";
		//Uri uri = Uri.parse("file://mnt/sdcard/FallingDetectionApp/Image1.jpg");
		Intent i = new Intent(Intent.ACTION_SEND);
		i.putExtra("address",phoneNumber);
		i.putExtra("sms_body",message);
		i.putExtra(Intent.EXTRA_STREAM, imagePath);
		i.setType("image/png");
		mContext.startActivity(i);
		
		
		/*
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra("sms_body", message);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
		intent.setType("image/png"); 
		mContext.startActivity(Intent.createChooser(intent,"Send"));
		
		Intent intent = new Intent();
		Intent i = new Intent(mContext, EmergencyAlert.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.setClass(mContext, EmergencyAlert.class); //startActivity from non-activity class
	    mContext.startActivity(i);
	    startActivity(Intent.createChooser(intent,"Send"));
	    */
	}
	
	
	
}
