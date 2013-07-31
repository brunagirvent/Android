package com.apps.kifall;

import android.content.Intent;
import android.util.Log;

public class DetectFallsAlgorithm {
	
     /* Owners property, code deleted */

	
	

	public DetectFallsAlgorithm(SensorsManager sensorListener){

		//Initialize variables
		 
         /* Owners property, code deleted */
        /* 1. Data processing and feature extracture
        2. Prediction (Kalman filter) and smoothing
        3. Fall detection algorithm
         */
	     
		// chooseAxisForPhoneOrientation(phoneOrientation);
		// chooseParametersForphoneLocation(phoneLocation);

		 mySensorListener = sensorListener;  
	}
	
	public void setPhoneOrientation(String phoneOrientation){
		 chooseAxisForPhoneOrientation(phoneOrientation);
	}
	
	public void setPhoneLocation(String phoneLocation){
		chooseParametersForphoneLocation(phoneLocation);
	}
	
	///ALGORITHM
	 public Boolean runAlgorithm(){
		 
		   /* Owners property, code deleted */
     }
	 
	 public void chooseAxisForPhoneOrientation(String phoneOrientation){
			if(phoneOrientation.equals("portrait"))
				axis=1;//Oy
			if(phoneOrientation.equals("landscape"))
				axis=0; //ox
		}
		
		public void chooseParametersForphoneLocation(String phoneLocation){
			if(phoneLocation.equals("pocket")) {
				 th_U=(float) 1.4*G;
				 th_L=(float)0.6*G;
				 changeOfOrient=70;
			 }

			else if(phoneLocation.equals("waist")){
				Log.d("TAG","here");
				 th_U=(float) 1.3*G;
				 th_L=(float)0.7*G;
				 changeOfOrient=50;
			 }
			
			else if(phoneLocation.equals("chest")) {
				 th_U=(float) 1.7*G;
				 th_L=(float)0.8*G;
				 changeOfOrient=60;
			}
		}
		
		
}
