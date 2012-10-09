package com.example.workers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import android.util.Log;

public class TestWorker
{
	public final Lock ping_lock;
	public final Lock data_lock;
	//public final Lock clock_lock;
	public Condition start_throughput;
	public Condition start_ping;
	//public Condition start_clock;
	public boolean start_p;
	public boolean start_d;
	//public boolean clock_start;
	public Thread Ping, Throughput, Clock;
	
	public String output;
	public double rate;
	
	public TestWorker()
	{
		ping_lock = new ReentrantLock();
		data_lock = new ReentrantLock();
		//clock_lock = new ReentrantLock();
		start_throughput = data_lock.newCondition();
		start_ping = ping_lock.newCondition();
		//start_clock = clock_lock.newCondition();
		start_p = false;
		start_d = false;
		//clock_start = false;
		
	}
	
	public String createThreads()
	{
		try{
			
			PingWorker PingTest = new PingWorker(this);
			Ping = new Thread(PingTest);
			Ping.start();
			ThroughputWorker ThroughputTest = new ThroughputWorker(this);
			Throughput = new Thread(ThroughputTest);
			Throughput.start();
			ClockWorker ClockSync = new ClockWorker(this);
			Clock = new Thread(ClockSync);
			Clock.start();
			Clock.join();
			Throughput.join();
			Ping.join();
			Log.d("Test Worker", "Rate " + rate);
			output = output + "\nThroughput: " + rate; 
			return output;
		}
		catch (InterruptedException e) {
		// TODO Auto-generated catch block
			Log.e("Joins interrupted", null);
			return "Error";
		}
	}
	
}