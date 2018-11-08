package com.zucc.clock.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.zucc.clock.model.clock;

public class getMSG implements Runnable{

	private Thread t;
   private String threadName;
   private Socket s=null;
   private String clientID="";
  
   
   getMSG( Socket s,String clientID) {
      this.s = s;
      this.clientID=clientID;
   }
   
   @Override
public void run() {
    
      try {
        	
    		//��ͻ��˷��ͷ�����Ϣ
    		while(true){
	    		//ͨ��Socket�ͻ�����������ȡ�ͻ�������
	    		InputStream in = s.getInputStream();
	    		byte[] buf = new byte[1024];
	    		int len = in.read(buf);
	    		String str = new String(buf, 0, len);
	    		clock.map.get(clientID).put("getMSG_cache", str);
	    		clock.map.get(clientID).put("getMSG_flag", true);
	    		System.out.println("getMSG_flag:"+String.valueOf(clock.map.get(clientID).get("getMSG_flag")));
	    		System.out.println(clientID +":"+str);
	    		if(str.equals("clientOver")){//����ͻ��˷�����over�ͶϿ�����
	    			break;
	    		}
	    		//��ͻ��˷��ͷ�����Ϣ
	    		OutputStream out = s.getOutputStream();
	    		out.write("ok".getBytes());
	    	}
    		System.out.println("getting out");
    		clock.map.get(clientID).put("S_CLOSE", true);
    	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
   
   public void start () {
     
    
   }

}

