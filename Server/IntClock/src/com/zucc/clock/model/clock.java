package com.zucc.clock.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zucc.clock.serviceImpl.ClientServer;

public class clock {

//	public static String stateMSG="";//要发送的数据
//	public static String STATE="";
//	public static boolean S_CLOSE=false;//是否断开连接
//	public static String getMSG="";//将要发给前端的数据
//	public static boolean getMSG_Flag=false;//是否接受到数据
	
	public static Map<String,Map> map = new HashMap<String,Map>();//所有数据都放在这里面，二重嵌套map
	public static ArrayList<ClientServer> clientServer= new ArrayList<ClientServer>();

}
