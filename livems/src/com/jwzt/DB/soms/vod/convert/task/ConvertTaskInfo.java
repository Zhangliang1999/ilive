package com.jwzt.DB.soms.vod.convert.task;

/**
 * 转码任务
 * 
 * @author 许业生 2009-11-11
 * 
 */
public class ConvertTaskInfo implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	// 任务ID
	private Integer task_id;
	// 视频ID
	private Integer file_id;
	//视频文件名称
	private String file_name;
	// 任务ID
	private Integer task_code_id;
	// 任务状态
	private Integer task_status;
	// 转码服务器id
	private Integer task_server_id;
	// 原栏目
	private String task_node_id;
	// 目标栏目
	private String task_target_node_id;
	// 任务时间
	private String task_time = "";
	// 执行任务人
	private String task_user = "";
	//转码进度
	private float progress=0;
	
	public Integer getTask_id()
	{
		return task_id;
	}
	public void setTask_id(Integer task_id)
	{
		this.task_id = task_id;
	}
	public Integer getFile_id()
	{
		return file_id;
	}
	public void setFile_id(Integer file_id)
	{
		this.file_id = file_id;
	}
	public String getFile_name()
	{
		return file_name;
	}
	public void setFile_name(String file_name)
	{
		this.file_name = file_name;
	}
	public Integer getTask_code_id()
	{
		return task_code_id;
	}
	public void setTask_code_id(Integer task_code_id)
	{
		this.task_code_id = task_code_id;
	}
	public Integer getTask_status()
	{
		return task_status;
	}
	public void setTask_status(Integer task_status)
	{
		this.task_status = task_status;
	}
	public Integer getTask_server_id()
	{
		return task_server_id;
	}
	public void setTask_server_id(Integer task_server_id)
	{
		this.task_server_id = task_server_id;
	}
	public String getTask_node_id()
	{
		return task_node_id;
	}
	public void setTask_node_id(String task_node_id)
	{
		this.task_node_id = task_node_id;
	}
	public String getTask_target_node_id()
	{
		return task_target_node_id;
	}
	public void setTask_target_node_id(String task_target_node_id)
	{
		this.task_target_node_id = task_target_node_id;
	}
	public String getTask_time()
	{
		return task_time;
	}
	public void setTask_time(String task_time)
	{
		this.task_time = task_time;
	}
	public String getTask_user()
	{
		return task_user;
	}
	public void setTask_user(String task_user)
	{
		this.task_user = task_user;
	}
	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}
	public  float getProgress() {
		return progress;
	}
	public void setProgress(float progress) {
		this.progress = progress;
	}

}
