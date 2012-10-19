package com.example.workers;

import android.util.Log;

import com.example.utils.CommandLineUtil;

public class PingWorker implements Runnable
{
	//private Condition start_throughput;
	//private Lock start_lock;
	public static CommandLineUtil cmdUtil;
	public static String pingOutput;
	private static final String SERVER_ADDRESS = "google.com";
	//private static final long PING_TEST_TIME = 120000; 
	//private static final long START_THROUGHPUT_TIME = 60000;
	TestWorker Boss;
	
	public PingWorker(TestWorker Boss)
	{
		this.Boss = Boss;
	}
	
	public void run()
	{
		String ipDst 	= SERVER_ADDRESS;
		String cmd 		= "ping";
		String options 	= "-c "+900 + " -i " + 0.2;
		String output 	= "";
		//long start_time;
		//long current_time;
		cmdUtil = new CommandLineUtil();
		
		Boss.ping_lock.lock();
		while(!Boss.start_p)
		{
			try {
				Boss.start_ping.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.e("Throughput worker", "Interrupted");
			}
		}
		Boss.ping_lock.unlock();
		
		//start_time = System.currentTimeMillis();
		//current_time = System.currentTimeMillis();
		
		output = cmdUtil.runCommand(cmd, ipDst, options);
		pingOutput += (output+"\n");
			/*try {
				Thread.sleep(90);
			} catch (InterruptedException e) {
				Log.e("Thread interrupted", output);
				//e.printStackTrace();
			}*/
			//current_time = System.currentTimeMillis();
		
		
		
		Boss.output = pingOutput;
		Log.d("Ping", "Exiting");
	}
	
}