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
	
	@RequestMapping(value = "/getMSG", method = RequestMethod.POST)//�ӿͻ��˽�������
	public  @ResponseBody  String getMSG(HttpServletRequest request, HttpServletResponse response,String clientId) throws IOException, ParseException, InterruptedException{
		/***�������ݵ�˼·��
		 * ����ÿ�����ӹ����豸������״̬��state������map�,�ֱ���alive�������ߣ�����״̬����
		 * Binding����״̬�����Ǻ�ǰ�˵Ľ��տ�ͨ��clientID��ÿ���豸��ΨһID������һ��
		 * die��ȥ��״̬���������ӶϿ��󣬳������˲��Ḵ���ˣ�
		 * 
		 * ���ǰ�˵Ľ��տ�δ���豸���ͻᴫһ���յ�clientID�������������getMSG�����ͻ��ȸ�����һ��alive���豸��
		 * Ȼ������alive���豸��clientID��data����ǰ�˵Ľ��տ򣬾���󶨺��ˣ�Ȼ���豸��state����alive��ΪBinding
		 * 
		 * �󶨺�Ľ��տ�ͻ��֮ǰ����ȥ��clientID��������ֱ��ͨ�����clientID�ҵ��豸���ٰ�data������
		 * 
		 * �����ʱ���������û���յ��ӿͻ��˷��������ݣ�������ֻ�û���
		 * 
		 * */
		int flag=0;
		System.out.println("getMSG-1:�յ���clientId��"+clientId);
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
		//System.out.println("���͸�ǰ�˵����ݣ�"+getMSG_str);
		String json = JSONObject.fromObject(map_json).toString();
		return json;
		
	}
	
	
	@RequestMapping(value = "/startGetMSG", method = RequestMethod.POST)//��ʼ��������
	public  @ResponseBody  String startGetMSG(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException{
		System.out.println("startGetMSG is start");
		clockService cs = new clockServiceImpl();
		cs.startDeelMSG();;
		System.out.println("startGetMSG is done");
		return "ok";
	}
	
	
	@RequestMapping(value = "/sendMSG", method = RequestMethod.POST)//��ͻ��˷�������
	public  @ResponseBody  String sendMSG(HttpServletRequest request, HttpServletResponse response,String msg,String clientID) throws IOException, ParseException{
		/**
		 * ��Ҫ���͵����ݷŵ���Ӧ�豸�ķ��ͻ������
		 * 
		 *
		 * */
		
		clockService cs = new clockServiceImpl();
		cs.sendMSG(msg,clientID);
		System.out.println("sendMSG! from:"+clientID);
		return "ok";
	}
	
}
