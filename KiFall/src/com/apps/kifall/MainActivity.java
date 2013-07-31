package com.apps.kifall;



import com.apps.kifall.ProvidersStatus;

import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/*KiFall: Fall Detection System Using an Android phone and the Kinect sensor
 The Android detect if a fall has taken place. 
 The detection algorithm is based on the acceleration and orientation sensor values
 If the fall is detected, the Android connects to the Kinect through a TCP server.
 The Kinect verifies if the fall has taken place. 
 The verification consists on track some joints (head, righHand and lefHand) and
 calculates the distance from the floor. 
 If the fall is verified, the Kinect takes a picture and the server sends the
 verification message and the picture to the Android. 
 The Android sends an MMS or SMS to the emergency contact containing 
 the time when the fall took place, the location, a link to Google Maps and the
 picture of the person laying down.
 */

public class MainActivity extends Activity {
	
	private SensorsService mSensorsService = null;
	private ServerConnectionService mServerCService = null;
	
	private Intent sensorsIntent; 
	
	private ProvidersStatus providersStatus;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeParams();
    }
    
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()) {
		 case R.id.action_settings:
			 startActivity(new Intent(this, PreferencesActivity.class));
	            break;
	       
		case R.id.about_title:
			// startActivity(new Intent(this, About.class));
			break;
		}
			
	        return true;
	}
    @Override
    protected void onResume() {
        super.onResume();
        initializeParams();

    }
    
    public void initializeParams() {
	    //check if GPS and Wifi are enabled
	    providersStatus= new ProvidersStatus(this); 
	     
	    //Set button text
	    updateTextButton();
	    
		//BroadcasterReceiver to receive data/messages (e.g "falldetected") 
	    //from SensorsService and ServerServices
		registerReceiver(MyReceiver, new IntentFilter("FALL_DETECTED")); 
		registerReceiver(MyReceiver, new IntentFilter("FALL_VERIFIED"));
		
	    //Create new sensors intent
	    sensorsIntent = new Intent();
	    sensorsIntent.setClass(this, SensorsService.class);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void onClickBtn (View view){
	     Log.d("TAG","Sevice running " + isSensorsServiceRunning());

		 if(!isSensorsServiceRunning()) {
				 
				 //Get values from settings
			     SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
				 String phonePosition = sharedPrefs.getString("phone_position", "-1");
				 String phoneOrientation = sharedPrefs.getString("phone_orientation", "-1");						
				 
				 Toast.makeText(this, "Fall detection started", Toast.LENGTH_SHORT).show();
			    
			    //Start collecting data from sensors and the DetectFallsAlgorithm.
				 sensorsIntent.putExtra("PHONEPOSITION", phonePosition); 
				 sensorsIntent.putExtra("PHONEORIENTATION", phoneOrientation); 
			     startService(sensorsIntent);
				
			 }
			 
			 else {	 
				 //Stop collecting data from servers and running the DetectFallsAlgorithm
				 stopService(sensorsIntent);
				 Toast.makeText(this, "Fall detection stopped", Toast.LENGTH_SHORT).show();
			 }
		 //Change button's text
		 updateTextButton();
    }
    
  
    
    
    public void updateTextButton(){
    	Button btnStart = (Button) findViewById (R.id.startbtn);
    	if(isSensorsServiceRunning())
    		btnStart.setText("STOP");
    	else
    		btnStart.setText("START");
    }
    
    
   public void connectToServer() {
   	    //Get data from settings
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		//check if kinect is enabled
	    if (sharedPrefs.getBoolean("enable_Kinect", false)){
		    //Get serverIP and serverPort from settings
		    String serverIP = sharedPrefs.getString("ip_adress", "-1");
		    int serverPort = Integer.parseInt(sharedPrefs.getString("port_number","-1"));
	
		    //Start the service that connects to the server
	    	Intent intent = new Intent(this, ServerConnectionService.class);
		    intent.putExtra("SERVERIP", serverIP); 
		    intent.putExtra("SERVERPORT", serverPort);
		    startService(intent); //start the service
	    }    
	    else 
	    	//if Kinect is not connected, we can't verify the fall with it. 
	    	//We send the emergency alert when the fall is detected on the Android (no verification with kinect)
	    	sendAlert();
	    	
   }

   //Send Emergency alert
   public void sendAlert(){
   	EmergencyAlert alert = new EmergencyAlert(this);
   	//Get the emergency contact from settings
   	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
       String phone = sharedPrefs.getString("phone_number", "-1");
   	alert.setPhone(phone);
   	alert.sendEmergencyAlert();
   }
   
   
   //Check if the sensors serivce is running
   public boolean isSensorsServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if ("com.apps.kifall.SensorsService".equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
   
   
    //Sensorservice Connection 
    private ServiceConnection mSensorsConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//This is called when the connection with the service has been stablished,
    		//giving us the service object we can use to interact with the service. 
    		//Because we have bound to a explicit service that we know is running in our process, 
    		//we can cast its IBinder to a concrete class and directly access it
    		mSensorsService = ((SensorsService.MyLocalBinder)service).getService();
    		
    		//Tell the user fall detection has started
    		//Toast.makeText(this, "Fall detection started", Toast.LENGTH_SHORT).show();
    		 			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			//This is called when the connection with the service has been unexpetedly disconnected// (process crashed)
    		mSensorsService = null;
			
		}
		
    };
    

    //ServerConnection Service
    private ServiceConnection mServerConnection = new ServiceConnection() {
    	@Override
    	public void onServiceConnected(ComponentName name, IBinder service) {
    		mServerCService = ((ServerConnectionService.LocalBinder)service).getService();

    	}

    	@Override
    	public void onServiceDisconnected(ComponentName name) {
    		// TODO Auto-generated method stub
    		mServerCService = null;
    	}

    };

    
    
    void doBindService() {
		// Establish a connection with the service. We use an explicit
		// class name because we want a specific service implementation that
		// we know will be running in our own process (and thus won't be
		// supporting component replacement by other applications).
		getApplicationContext().bindService(new Intent(this, SensorsService.class),
				mSensorsConnection, 0);
		getApplicationContext().bindService(new Intent(this, ServerConnectionService.class),
				mServerConnection, 0);
		
	}

	void doUnbindService() {
		// Detach our existing connection.
		if (mSensorsService != null) {
			getApplicationContext().unbindService(mSensorsConnection);
			getApplicationContext().unbindService(mServerConnection);

			mSensorsService = null;
			mServerConnection = null;
		}
	}

    @Override
    protected void onDestroy() {    
    	super.onDestroy();    
    	doUnbindService();
    }
    
    
    
    
    //Broadcast receiver to receive messages from the services
    private BroadcastReceiver MyReceiver= new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	String action = intent.getAction(); 	    	
	    	
	         if (action.equals("FALL_DETECTED")){
	            Log.d("TAG", "ACTION: Fall detected");
	        	//Connect to the Server when a fall is detected
	        	connectToServer();
	        } 
	         else if(action.equals("FALL_VERIFIED")){
	        	 Log.d("TAG", "Action: Fall verified");
	        	//Send an Emergency Alert when a fall is detected by the Android
	        	//And verified with the Kinect
	        	sendAlert();
	         }
	}
	    
	  
	}; 
    
}
