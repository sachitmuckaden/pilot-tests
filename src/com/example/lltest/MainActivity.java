package com.example.lltest;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	Button start_test;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_test = (Button) findViewById(R.id.start_button);
        start_test.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View v) {
                // Switching to Register screen
            	Intent i= new Intent(getApplicationContext(),TestActivity.class);
            	startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
