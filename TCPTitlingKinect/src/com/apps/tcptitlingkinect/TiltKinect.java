package com.apps.tcptitlingkinect;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TiltKinect extends Activity {
	private boolean connected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tiltkinect);
		connected=false;
		//Button buton_connect = (Button) findViewById(R.id.ConnectionButton);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tiltkinect, menu);
		return true;
	}
	
	/** Called when the user touches the Up button */
	public void Up(View view) {
		Log.d("TAG", "Up");
		InternetTask task = new InternetTask('U');
		task.execute();
	}
	
	/** Called when the user touches the Up button */
	public void Down(View view) {
		Log.d("TAG", "Down");
		InternetTask task = new InternetTask('D');
		task.execute();
	}	

}
