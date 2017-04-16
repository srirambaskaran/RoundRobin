package org.roundrobin.socket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

@ServerEndpoint(value = "/UpdateValues", encoders = JSONEncoder.class, decoders = JSONDecoder.class, configurator = EndPointConfigurator.class)
public class UpdateSocket {
	private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) throws IOException, JSONException, EncodeException {
		sessions.add(session);
		updateCurrentSelected(session);
		session.getUserProperties().put("isBoss", false);
	}

	private void updateCurrentSelected(Session s) throws IOException, EncodeException {
		JSONObject json = AdminManager.getCurrentSelected();
		s.getBasicRemote().sendObject(json);
	}

	@OnClose
	public void onClose(Session session) throws IOException, JSONException, EncodeException {
		sessions.remove(session);
	}

	@OnError
	public void onError(Session session, Throwable t) {
		// Do nothing.
	}

	@OnMessage
	public void onMessage(JSONObject json) throws IOException, EncodeException {
		// Do nothing
	}

	public static void sendMessageToAll() throws IOException, EncodeException {
		JSONObject json = AdminManager.getCurrentSelected();
		for (Session s : sessions) {
			synchronized (s) {
				s.getBasicRemote().sendObject(json);
			}
		}
	}

}
