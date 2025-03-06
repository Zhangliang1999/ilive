package com.jwzt.DB.soms.live.liveChannel;

import java.sql.Timestamp;

public class PromInfoJson {
	private int prom_id;
	private String channelName;
	private String channel_id;
	private int time_long;
	private String prom_name;
	private String start_time;
	private String play_type;
	private String end_time;
	private String cycle;
	private String cycle_ids;
	private int plan_id;
	private String  remote_address;
	private int record_id;
	
	
	
	private int startUpdated=0;
	private int endUpdated=0;
	
	
	public int getStartUpdated() {
		return startUpdated;
	}
	public void setStartUpdated(int startUpdated) {
		this.startUpdated = startUpdated;
	}
	public int getEndUpdated() {
		return endUpdated;
	}
	public void setEndUpdated(int endUpdated) {
		this.endUpdated = endUpdated;
	}
	public int getProm_id() {
		return prom_id;
	}
	public void setProm_id(int prom_id) {
		this.prom_id = prom_id;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public String getPlay_type() {
		return play_type;
	}
	public void setPlay_type(String play_type) {
		this.play_type = play_type;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public int getTime_long() {
		return time_long;
	}
	public void setTime_long(int time_long) {
		this.time_long = time_long;
	}
	public String getProm_name() {
		return prom_name;
	}
	public void setProm_name(String prom_name) {
		this.prom_name = prom_name;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getCycle_ids() {
		return cycle_ids;
	}
	public void setCycle_ids(String cycle_ids) {
		this.cycle_ids = cycle_ids;
	}
	public int getPlan_id() {
		return plan_id;
	}
	public void setPlan_id(int plan_id) {
		this.plan_id = plan_id;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public long getStartTimeLong(){
		return  Timestamp.valueOf(getStart_time()).getTime();
	}
	public String getRemote_address() {
		return remote_address;
	}
	public void setRemote_address(String remote_address) {
		this.remote_address = remote_address;
	}
	public int getRecord_id() {
		return record_id;
	}
	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}
	
}
