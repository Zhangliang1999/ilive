package com.jwzt.DB.soms.live.liveChannel;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SavePlanInfoJson {

	private int plan_id;
	private String channel_id;
	private String channelName;
	private int time_long;
	private String plan_name;
	private String start_time;
	private String end_time;
	private long start_time_long;
	private long end_time_long;
	private String cycle;
	private String cycle_ids;
	private String if_refresh;
	private int startUpdated=0;
	private int endUpdated=0;
	private String status;
	
	
	public long getStart_time_long() {
		return start_time_long;
	}
	public void setStart_time_long(long start_time_long) {
		this.start_time_long = start_time_long;
	}
	public long getEnd_time_long() {
		return end_time_long;
	}
	public void setEnd_time_long(long end_time_long) {
		this.end_time_long = end_time_long;
	}
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
	public int getPlan_id() {
		return plan_id;
	}
	public void setPlan_id(int plan_id) {
		this.plan_id = plan_id;
	}
	
	public String getChannel_id() {
		return channel_id;
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
	public String getPlan_name() {
		return plan_name;
	}
	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
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
	public String getIf_refresh() {
		return if_refresh;
	}
	public void setIf_refresh(String if_refresh) {
		this.if_refresh = if_refresh;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
