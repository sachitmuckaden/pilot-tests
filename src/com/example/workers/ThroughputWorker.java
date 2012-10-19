package com.example.workers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.util.Log;

public
class
ThroughputWorker implements Runnable
{
	final static String SERVER_IP_ADDRESS = "ruggles.gtnoise.net";
	final static int SERVER_PORT_NUMBER = 8888;
	private DatagramSocket client_socket;
	//private byte[] send_data = new byte[1024];
	private byte[] receive_data = new byte[1024];
	private static final String REQUEST_ACCEPTED = "Request Accepted";
	TestWorker Boss;	
	
	
	public ThroughputWorker(TestWorker Boss)
	{
		this.Boss = Boss;
	}
	
	
	private
	DatagramPacket
	getRequestPacket() 
	{
		String request = "startrequest";
		byte[] start_request_array = request.getBytes();
		DatagramPacket request_packet = new DatagramPacket(start_request_array, start_request_array.length);
		return request_packet;
	}
	
	
	private
	DatagramPacket
	getDataPacket() 
	{
		byte[] data_packet_data = new byte[1024];
		for(int i=0;i<1024;i++)
		{
			data_packet_data[i] = 122;
		}
		DatagramPacket data_packet = new DatagramPacket(data_packet_data, data_packet_data.length);
		return data_packet;
	}
	
	
	public
	void
	run()
	{
		DatagramPacket mPacket = getDataPacket();
		int count=0;
		long start_time=0, end_time = 0;
		try
		{
			
			
			client_socket = new DatagramSocket();
			
			Boss.data_lock.lock();
			while(!Boss.start_d)
			{
				try {
					Boss.start_throughput.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Log.e("Throughput worker", "Interrupted");
				}
			}
			Boss.data_lock.unlock();
			
			Log.d("Throughput worker", "Awake");
			client_socket.connect(InetAddress.getByName(SERVER_IP_ADDRESS), SERVER_PORT_NUMBER);
			Log.d("Throughput worker", "Connected");
			client_socket.send(getRequestPacket());
			receive_data = new byte[1024];
			DatagramPacket receive_packet = new DatagramPacket(receive_data, receive_data.length);
			client_socket.receive(receive_packet);
			String received = new String(receive_packet.getData(),0,receive_packet.getLength());
			
			if(received.equals(REQUEST_ACCEPTED))
			{
				Log.d("Throughput worker", "Starting to send");
				start_time = System.currentTimeMillis();
				//end_time = System.currentTimeMillis();
								
				
				
				while(true)
				{
					count++;
					//Log.d("Throughput Worker", "Entered");
					client_socket.send(mPacket);
					
					//Log.d("Throughput Worker", ""+ (end_time-start_time));
					if(Thread.interrupted())
						throw new InterruptedException();
				}
				//end_time = System.currentTimeMillis();
				//Log.d("Throughput Worker", "Data:" + count/3);
				
			}
		}
		catch(UnknownHostException e)
		{
			Log.e("Could not resolve server address", null);
		}
		catch(IOException e)
		{
			Log.e("Cannot send request packet", null);
		}
		catch(SecurityException f)
		{
			f.printStackTrace();
		}
		catch(InterruptedException e)
		{
			end_time = System.currentTimeMillis();
			Log.d("Throughput Worker","Timedf"+(end_time-start_time));
			Log.d("Throughput Worker", "Data" + count);
			double rate  = ((double)count/((double)end_time-start_time));
			Log.d("Throughput Worker", "Rate " + rate);
			Boss.rate = rate;
			
		}
		finally
		{
			if(client_socket!=null)
				client_socket.close();
		}
		
	}
}