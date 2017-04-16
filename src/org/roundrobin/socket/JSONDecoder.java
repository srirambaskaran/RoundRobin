package org.roundrobin.socket;

import javax.websocket.DecodeException;
import javax.websocket.Decoder.Text;
import javax.websocket.EndpointConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONDecoder implements Text<JSONObject> {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject decode(String arg0) throws DecodeException {

		return new JSONObject(arg0);

	}

	@Override
	public boolean willDecode(String arg0) {
		try {
			new JSONObject(arg0);
		} catch (JSONException ex) {
			try {
				new JSONArray(arg0);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}

}
