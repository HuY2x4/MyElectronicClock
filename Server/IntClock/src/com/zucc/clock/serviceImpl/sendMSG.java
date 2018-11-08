package com.zucc.clock.serviceImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.zucc.clock.model.clock;

public class sendMSG implements Runnable{

	private Thread t;
   private String threadName;
   private Socket s=null;
   private String clientID="";
   private String data="";
  
   
   sendMSG( Socket s,String clientID) {
      this.s = s;
      this.clientID=clientID;
      
   }
   
   @Override
   public void run() {
    
      try {
    		//��ͻ��˷��ͷ�����Ϣ
    		OutputStream out = s.getOutputStream();
    		while(true){
    			if((boolean) clock.map.get(clientID).get("S_CLOSE")){//
    				break;
    			}
    			if(!((String)(clock.map.get(clientID).get("sendMSG_cache"))).equals("")){
    				data=(String) clock.map.get(clientID).get("sendMSG_cache");
    				System.out.println("��Ҫ���͸�"+clientID+"������:"+data);
    				out.write(data.getBytes());
    				clock.map.get(clientID).put("sendMSG_cache", "");//��շ��͵Ļ����
    				
    			}
    			t.sleep(10);
    		}
    		clock.map.get(clientID).put("S_CLOSE", true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
   
   public void start () {
     
    
   }

}

