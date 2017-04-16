package org.roundrobin.socket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.json.JSONObject;

public class JSONEncoder implements Encoder.Text<JSONObject>{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode(JSONObject arg0) throws EncodeException {
		return arg0.toString();
	}
	
}
