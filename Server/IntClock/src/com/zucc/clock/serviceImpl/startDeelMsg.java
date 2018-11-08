package com.zucc.clock.serviceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.zucc.clock.model.clock;

public class startDeelMsg implements Runnable{

	   private Thread t;
	   private String threadName;
	   
	   
	   startDeelMsg( String name) {
	      threadName = name;
	      System.out.println("Creating " +  threadName );
	   }
	   
	   @Override
	public void run() {
	      System.out.println("Running " +  threadName );
	      try {
	    	  	//��ʼ��client����
	    	     
	        	//����ServerSocket����
	    		ServerSocket ss = new ServerSocket(8887);
	    		//��ȡ�ͻ��˶���
	    		while (true) {
					System.out.println("new a new client!!!!!!!!!!!!!look here1");
					Socket s = ss.accept();
					System.out.println("new a new client!!!!!!!!!!!!!look here2");
					new Thread() {
						 @Override
						public void run() {
							 	try {
									clock.clientServer.add(new ClientServer(s));
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} //���ڵ��������ֻnew��һ̨�豸

						 }
					}.start();
					System.out.println("new a new client!!!!!!!!!!!!!look here3");

					Thread.sleep(500);
				}
	    		/*		
	    		//��ȡ�ͻ���IP
	    		String ip = s.getInetAddress().getHostAddress();
	    		System.out.println(ip +"..connected..");
	    		new Thread(new getMSG1(s)).start();
	    		new Thread(new sendMSG1(s)).start();
	    		while(true){
	    			if(clock.S_CLOSE){
	    				break;
	    			}
	    			t.sleep(10);
	    		}
	    		System.out.println(ip +"�ر�..connected..");
	    		clock.S_CLOSE=false;
	    		s.close();
	    		ss.close();
	    		new Thread(new startDeelMsg("thread_1")).start();*/
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	   
	   
	   public void start () {
		   
	      System.out.println("Starting " +  threadName );
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	    
	   }

}
