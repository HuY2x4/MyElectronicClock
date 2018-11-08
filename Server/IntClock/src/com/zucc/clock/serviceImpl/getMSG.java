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
        	
    		//向客户端发送反馈信息
    		while(true){
	    		//通过Socket客户端输入流获取客户端数据
	    		InputStream in = s.getInputStream();
	    		byte[] buf = new byte[1024];
	    		int len = in.read(buf);
	    		String str = new String(buf, 0, len);
	    		clock.map.get(clientID).put("getMSG_cache", str);
	    		clock.map.get(clientID).put("getMSG_flag", true);
	    		System.out.println("getMSG_flag:"+String.valueOf(clock.map.get(clientID).get("getMSG_flag")));
	    		System.out.println(clientID +":"+str);
	    		if(str.equals("clientOver")){//如果客户端发过来over就断开连接
	    			break;
	    		}
	    		//向客户端发送反馈信息
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

