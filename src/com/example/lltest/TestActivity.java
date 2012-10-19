package com.example.lltest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.example.workers.TestWorker;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

public class TestActivity extends Activity
{
	TextView output_text;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        output_text = (TextView) findViewById(R.id.textView1);
        TestWorker Test = new TestWorker();
        String pingOutput = Test.createThreads();
        int rc = writeOutputToFile(pingOutput);
        if(rc<0)
        {
        	Log.e("Error writing to file", ""+rc);
        }
        pingOutput = null;
        output_text.setText("COMPLETED...."+ rc);
	}
	@SuppressLint("WorldReadableFiles")
	private int writeOutputToFile(String pingOutput)
	{
		String FILENAME = "pingoutput.txt";
		File path = Environment.getExternalStorageDirectory();
		File file = new File(path, FILENAME);
		//try {
			//fos = openFileOutput(FILENAME, Context.MODE_WORLD_READABLE);
		OutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				fos.write(pingOutput.getBytes());
				fos.close();
			} catch (IOException e) {
				
				return -1;
			}
			
		return 0;
		/*} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return -2;*/
	}
	
	
}