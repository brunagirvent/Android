package com.apps.kifall;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;


public class ServerConnectionService  extends Service {

		public String serverIP; 
		public int serverPort;
		  
		private DataOutputStream dataOutputStream = null;
		private  DataInputStream dataInputStream = null;
		private  Socket socket;
		private  String message;
		
		private String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/KiFall";
		private String imagePath = folderPath+"/Image1.txt" ;
		  
		 

		@Override
		public IBinder onBind(Intent intent) {
		    // TODO Auto-generated method stub
		      return myBinder;
		}

		  private final IBinder myBinder = new LocalBinder();

		  public class LocalBinder extends Binder {
		        public ServerConnectionService getService() {
		            return ServerConnectionService.this;

		        }
		    }

		  @Override
		    public void onCreate() {
		        super.onCreate();
		    }

		  public void IsBoundable(){
		        //Toast.makeText(this,"I bind like butter", Toast.LENGTH_LONG).show();
		    }

		  public void sendMessage(String message){
		        //if (dataOutputStream != null && !dataOutputStream.checkError()) {
		            System.out.println("in sendMessage"+message);
		       //     dataOutputStream.println(message);
		           // dataOutputStream.flush();
		       // }
		    }

		    @Override
		    public int onStartCommand(Intent intent,int flags, int startId){
		        super.onStartCommand(intent, flags, startId);
		      
		         Bundle extras = intent.getExtras(); 
				 serverIP = extras.getString("SERVERIP");
				 serverPort = extras.getInt("SERVERPORT");
				 Log.d("TAG", "ServerIP: "+serverIP + " ServerPort: "+serverPort);
				 
		        Runnable connect = new connectSocket();
		        new Thread(connect).start();
		        return START_STICKY;
		    }


		    class connectSocket implements Runnable {

		        @Override
		        public void run() {
		        	boolean fallVerified = false;

		            try { 
		                
		                 /* Owners property, code deleted */
                        
                        /*
                          1. Android connects to the server
                          2. Kinect verifies the fall
                             If the Fall is verified: 
                          3. The server sends a verification message to teh Android
                          4. The Kinect takes a picure of the person laying down.
                          5. The server sends teh picture to the Android
                          6. The Android will send an EmergencyAlert with the Alert, 
                              location, time when the fall occurred and the picture.
                          */
		                
		    }
		    
		    public String inputStreamToString(InputStream in) throws IOException {
			    StringBuilder out = new StringBuilder();
			    char[] chars = new char[1024];
			    Reader reader = new InputStreamReader(in /*, CHARSET_TO_USE */);
			    
			    //for (int len; (len = reader.read(chars)) > 0  )
		        
		        out.append(chars, 0, reader.read(chars));
		        
			    try {
			          
			    } finally {
			        try {
			            in.close();
			        } catch (IOException ignored) {
			        }
			    
			    }
			    return out.toString();
			}


		    @Override
		    public void onDestroy() {
		        super.onDestroy();
		          try {
		              socket.close();
		          } catch (Exception e) {
		              // TODO Auto-generated catch block
		              e.printStackTrace();
		          }
		          socket = null;
		      }

			


		    }

