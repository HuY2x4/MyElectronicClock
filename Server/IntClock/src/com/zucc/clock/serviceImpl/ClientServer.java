package com.zucc.clock.serviceImpl;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.zucc.clock.model.clock;

public class ClientServer {
	Socket s;
	private Thread t;
	public ClientServer(Socket s) throws InterruptedException {
		// TODO Auto-generated constructor stub
		this.s = s;
		
		try {
			System.out.println("creat a new shebei");
			Map map = new HashMap();
			map.put("sendMSG_cache", "");
			map.put("getMSG_cache", "");
			map.put("sendMSG_flag", false);
			map.put("getMSG_flag", false);
			map.put("S_CLOSE",false);
			map.put("state","alive");
			String ip = s.getInetAddress().getHostAddress();
    		System.out.println(ip +"..connected..");
    		int random=(int)(Math.random()*900)+100;
    		String clientID=ip+Integer.toString(random);
    		System.out.println("clientID:"+clientID);
    		clock.map.put(clientID, map);
    		
    		
    		
    		new Thread(new getMSG(s,clientID)).start();
    		new Thread(new sendMSG(s,clientID)).start();
    		while(true){
    			if((boolean) clock.map.get(clientID).get("S_CLOSE")){  //好像判断不出来他是不是boolean类型
    				break;
    			}
    			t.sleep(10);
    		}
    		System.out.println(ip +"关闭..connected..");
    		clock.map.get(clientID).put("S_CLOSE", false);//重置属性，但好像没意义
    		clock.map.get(clientID).put("state", "die");
    		s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
