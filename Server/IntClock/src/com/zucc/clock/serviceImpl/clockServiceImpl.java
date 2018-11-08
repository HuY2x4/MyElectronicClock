package com.zucc.clock.serviceImpl;

import org.springframework.stereotype.Service;

import com.zucc.clock.model.clock;
import com.zucc.clock.service.clockService;

@Service
public class clockServiceImpl implements clockService {
	
	
	@Override
	public boolean sendMSG(String time,String clientID) {
		// TODO Auto-generated method stub
		clock.map.get(clientID).put("sendMSG_cache",time);
		
		return true;
	}


	@Override
	public void startDeelMSG() {
		// TODO Auto-generated method stub
		new Thread(new startDeelMsg("mainThread")).start();
	}

}
