package com.zucc.clock.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zucc.clock.model.clock;
import com.zucc.clock.service.clockService;
import com.zucc.clock.serviceImpl.clockServiceImpl;
@Controller
public class clockContr {

	
	@RequestMapping("/")
	public String shouye(){  
		
		return "Clock";
	}
	
	@RequestMapping(value = "/getMSG", method = RequestMethod.POST)//从客户端接收数据
	public  @ResponseBody  String getMSG(HttpServletRequest request, HttpServletResponse response,String clientId) throws IOException, ParseException, InterruptedException{
		/***接收数据的思路：
		 * 首先每个连接过的设备有三种状态（state，存在map里）,分别是alive（刚上线，待绑定状态），
		 * Binding（绑定状态，就是和前端的接收框通过clientID（每个设备的唯一ID）绑定在一起）
		 * die（去世状态，就是连接断开后，彻底死了不会复活了）
		 * 
		 * 如果前端的接收框未绑定设备，就会传一个空的clientID过来，现在这个getMSG函数就会先给他找一个alive的设备，
		 * 然后把这个alive的设备的clientID和data传给前端的接收框，就算绑定好了，然后设备的state就由alive变为Binding
		 * 
		 * 绑定后的接收框就会把之前传过去的clientID传过来，直接通过这个clientID找到设备，再把data传回来
		 * 
		 * 如果长时间这个函数没接收到从客户端发来的数据，这个部分还没想好
		 * 
		 * */
		int flag=0;
		System.out.println("getMSG-1:收到的clientId："+clientId);
		String clientID=clientId;
		Map map_json = new HashMap(); 
		if(clientId.equals("")){
			System.out.println("getMSG-2");
			Map<String, Map> map = clock.map; 
			System.out.println("getMSG-2-2");
			for (Map.Entry<String, Map> entry : map.entrySet()) { 
				System.out.println("getMSG-3");
			  if(entry.getValue().get("state").equals("alive")){
				  flag=1;
				  System.out.println("getMSG-4");
				  clientID=entry.getKey();
				  System.out.println("getMSG-5:"+clientID);
				  entry.getValue().put("state", "Binding");
				  System.out.println("getMSG-6");
				  break;
			  }
			}
		}
		if(flag==0&&clientId.equals("")){
			map_json.put("state", "none");
			Thread.sleep(1000);
			String json = JSONObject.fromObject(map_json).toString();
			return json;
		}
		System.out.println("getMSG-7");
		System.out.println("clientID:"+clientID);
		while(true){

			if((boolean) clock.map.get(clientID).get("getMSG_flag")){
				clock.map.get(clientID).put("getMSG_flag",false);
				System.out.println("getMSG-8");
				break;
			}
			 Thread.sleep(100);
		}
		System.out.println("getMSG-9");
		map_json.put("data", clock.map.get(clientID).get("getMSG_cache"));
		map_json.put("clientID", clientID);
		System.out.println("getMSG-10");
		map_json.put("state", "ok");
		//String getMSG_str=clock.getMSG;
		//System.out.println("发送给前端的数据："+getMSG_str);
		String json = JSONObject.fromObject(map_json).toString();
		return json;
		
	}
	
	
	@RequestMapping(value = "/startGetMSG", method = RequestMethod.POST)//开始接收数据
	public  @ResponseBody  String startGetMSG(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException{
		System.out.println("startGetMSG is start");
		clockService cs = new clockServiceImpl();
		cs.startDeelMSG();;
		System.out.println("startGetMSG is done");
		return "ok";
	}
	
	
	@RequestMapping(value = "/sendMSG", method = RequestMethod.POST)//向客户端发送数据
	public  @ResponseBody  String sendMSG(HttpServletRequest request, HttpServletResponse response,String msg,String clientID) throws IOException, ParseException{
		/**
		 * 将要发送的数据放到对应设备的发送缓冲池中
		 * 
		 *
		 * */
		
		clockService cs = new clockServiceImpl();
		cs.sendMSG(msg,clientID);
		System.out.println("sendMSG! from:"+clientID);
		return "ok";
	}
	
}
