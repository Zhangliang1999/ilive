package com.bvRadio.iLive.iLive.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

public class PushJson {

	public static void pushOne(Long userId,String title,Integer type,Integer typeId) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		List<JSONObject> list = new ArrayList<>();
		JSONObject obj = new JSONObject();
		obj.put("USERID", userId);
		obj.put("TITLE", title);
		obj.put("TYPE", type);
		obj.put("ID", typeId);
		list.add(obj);
		URI uri;
		try {
			uri = new URIBuilder("http://mp.zbt.tv189.net/pushservice/push").setParameter("pushjson", list.toString()).build();
			HttpGet get = new HttpGet(uri);
			httpClient.execute(get);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void pushMany(List<?> list) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		URI uri;
		try {
			uri = new URIBuilder("http://localhost:8081/pushserviceV2/push").setParameter("pushjson", list.toString()).build();
			HttpGet get = new HttpGet(uri);
			httpClient.execute(get);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
