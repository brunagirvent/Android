package com.apps.tcpchat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class InternetTask extends AsyncTask<String, Void, String> {

	private WeakReference<TextView> mTextConectionStatus;
	DataOutputStream dataOutputStream = null;
    DataInputStream dataInputStream = null;
    Socket socket;
    String strresult="";
    String message="";
    
	public InternetTask(TextView view) {
        this.mTextConectionStatus = new WeakReference<TextView>(view);
    }
	
	@Override
    protected String doInBackground(String... params) {
	try {
		 Log.d("TAG", "new socket...");
        socket = new Socket("10.0.0.8",3200);
        Log.d("TAG", "connected");

        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("Hello Kinect!");
        Log.d("TAG", "message sent");

        
        dataInputStream = new DataInputStream(socket.getInputStream());
        Log.d("TAG", "message received");
        
        message = inputStreamToString(dataInputStream);
        Log.d("TAG", message);
        strresult="OK";
        
    } catch (UnknownHostException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (dataInputStream != null) {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

            return strresult;
      }      

      @Override
      protected void onPostExecute(String result) {
    	  mTextConectionStatus.get().append(message + " \n");
      }

      @Override
      protected void onPreExecute() {
    	  mTextConectionStatus.get().append("Connecting to Kinect... \n");
      }
      
      
      public String inputStreamToString(InputStream in) throws IOException {
  	    StringBuilder out = new StringBuilder();
  	    char[] chars = new char[1024];
  	    Reader reader = new InputStreamReader(in /*, CHARSET_TO_USE */);
  	    
  	    //for (int len; (len = reader.read(chars)) > 0  )
          
          out.append(chars, 0, reader.read(chars));
          
  	    /*
  	    try {
  	          
  	    } finally {
  	        try {
  	        	Log.d("TAG", "after");
  	            in.close();
  	        } catch (IOException ignored) {
  	        }
  	    
  	    }*/
  	    return out.toString();
  	}
}
