package com.bvRadio.iLive.iLive.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.ILiveRollingAdvertising;

public class SerializeUtil {
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
          e.printStackTrace();
		}
		return null;
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {

		}
		return null;
	}

	public static ILiveMessage getObject(String id) {
		
		byte[] person = JedisUtils.getByte(("msg:" + id).getBytes());
		return (ILiveMessage) SerializeUtil.unserialize(person);
	}
	public static ILiveLiveRoom getObjectMeet(Integer id) {
		byte[] person = JedisUtils.getByte(("meetRoomInfo:" + id).getBytes());
		
		return (ILiveLiveRoom) SerializeUtil.unserialize(person);
	}
	public static ILiveLiveRoom getObjectRoom(Integer id) {
		byte[] person = JedisUtils.getByte(("roomInfo:" + id).getBytes());
		return (ILiveLiveRoom) SerializeUtil.unserialize(person);
	}
	public static ILiveManager getObjectManager(String id) {
		byte[] person = JedisUtils.getByte(("managerInfo:" + id).getBytes());
		return (ILiveManager) SerializeUtil.unserialize(person);
	}
	public static ILiveEnterprise getObjectEnterprise(Integer id) {
		byte[] person = JedisUtils.getByte(("enterpriseInfo:" + id).getBytes());
		return (ILiveEnterprise) SerializeUtil.unserialize(person);
	}
	public static ILiveRollingAdvertising getObjectRollingAdvertising(Integer id) {
		byte[] person = JedisUtils.getByte(("iLiveRolling:" + id).getBytes());
		return (ILiveRollingAdvertising) SerializeUtil.unserialize(person);
	}
}
