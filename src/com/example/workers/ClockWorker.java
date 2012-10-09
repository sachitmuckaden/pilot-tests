package com.example.workers;

import android.util.Log;


public 
class 
ClockWorker 
implements Runnable
{
	TestWorker Boss;

	
	ClockWorker(TestWorker Boss)
	{
		this.Boss = Boss;
	}
	
	
	public
	void
	run()
	{
		try
		{
			Boss.ping_lock.lock();
			Boss.start_p=true;
			Boss.start_ping.signal();
			Boss.ping_lock.unlock();
			Thread.sleep(60000);
			Boss.data_lock.lock();
			Boss.start_d=true;
			Boss.start_throughput.signal();
			Boss.data_lock.unlock();
			Thread.sleep(50000);
			Log.d("Clock", "Sending interrupt");
			Boss.Throughput.interrupt();
	
		}
		catch(InterruptedException e)
		{
			return;
		}

	}
}