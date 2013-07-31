package com.apps.tcptitlingkinect;

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

	char action;
	DataOutputStream dataOutputStream = null;
    DataInputStream dataInputStream = null;
    Socket socket;
    String strresult="";
   
    
	public InternetTask(char _action) {
        this.action = _action;
    }
	
	
	@Override
    protected String doInBackground(String... params) {
	try {
        socket = new Socket("10.0.0.8",3200); //129.10.233.141
        Log.d("TAG", "connected");
        
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeChar(action);
        Log.d("TAG", "Action:  " + action);

        dataInputStream = new DataInputStream(socket.getInputStream()); //ACK
        Log.d("TAG", "ACK");
        
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
          //mTextConectionStatus.get().append("Android:  " + "Hello Kinect! \n");
          //mTextConectionStatus.get().append("Kinect:  " + message + "\n");

      }

      @Override
      protected void onPreExecute() {
    	  //mTextConectionStatus.get().append("Connecting to Kinect \n");
      }
}
