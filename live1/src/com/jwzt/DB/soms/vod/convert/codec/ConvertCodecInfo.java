package com.jwzt.DB.soms.vod.convert.codec;

/**
 * Soms4ConvertCodec entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ConvertCodecInfo implements java.io.Serializable
{

	// Fields

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 8365755353922444737L;
	
	/**
	 * 编码id
	 */
	private Integer codecId;
	/***
	 * 名称
	 */
	private String codecName;
	/**
	 * 目标格式
	 */
	private String codecFiletype;
	/**
	 * 视频帧率
	 */
	private String codecVideoFrameRate;
	/**
	 * 视频分辨率
	 */
	private String codecVideoWidth;
	/**
	 * 视频分辨率
	 */
	private String codecVideoHeight;
	/**
	 * 视频编码格式
	 */
	private String codecVideoFormat;
	/**
	 * 视频码流
	 */
	private String codecVideoDatarate;
	
	/**
	 * 音频编码格式
	 */
	private String codecAudioFormat;
	/**
	 * 音频码流
	 */
	private String codecAudioDatarate;
	/**
	 * 音频声道
	 */
	private String codecAudioChannel;
	/**
	 * 音频采样频率
	 */
	private String codecAudioSamplerate;
	/**
	 * 音频采样精度
	 */
	private String codecAudioBitpersample;
	
	/**
	 * 稿图设置
	 */
	private String codecPictureContent;
	/**
	 * 扩展字段1
	 */
	private String ext1;
	/**
	 * 扩展字段2
	 */
	private String ext2;
	/**
	 * 扩展字段3
	 */
	private String ext3;
	/**
	 * 扩展字段4
	 */
	private String ext4;
	/**
	 * 扩展字段5
	 */
	private String ext5;

	// Constructors

	/** default constructor */
	public ConvertCodecInfo()
	{
	}

	/** minimal constructor */
	public ConvertCodecInfo(Integer codecId, String codecFiletype)
	{
		this.codecId = codecId;
		this.codecFiletype = codecFiletype;
	}

	/** full constructor */
	public ConvertCodecInfo(Integer codecId, String codecFiletype, String codecVideoFrameRate, String codecVideoWidth, String codecVideoHeight, String codecVideoFormat, String codecVideoDatarate, String codecAudioFormat, String codecAudioDatarate, String codecAudioChannel, String codecAudioSamplerate, String codecAudioBitpersample, String codecPictureContent, String ext1, String ext2, String ext3, String ext4, String ext5)
	{
		this.codecId = codecId;
		this.codecFiletype = codecFiletype;
		this.codecVideoFrameRate = codecVideoFrameRate;
		this.codecVideoWidth = codecVideoWidth;
		this.codecVideoHeight = codecVideoHeight;
		this.codecVideoFormat = codecVideoFormat;
		this.codecVideoDatarate = codecVideoDatarate;
		this.codecAudioFormat = codecAudioFormat;
		this.codecAudioDatarate = codecAudioDatarate;
		this.codecAudioChannel = codecAudioChannel;
		this.codecAudioSamplerate = codecAudioSamplerate;
		this.codecAudioBitpersample = codecAudioBitpersample;
		this.codecPictureContent = codecPictureContent;
		this.ext1 = ext1;
		this.ext2 = ext2;
		this.ext3 = ext3;
		this.ext4 = ext4;
		this.ext5 = ext5;
	}

	// Property accessors

	public Integer getCodecId()
	{
		return this.codecId;
	}

	public void setCodecId(Integer codecId)
	{
		this.codecId = codecId;
	}

	public String getCodecFiletype()
	{
		return this.codecFiletype;
	}

	public void setCodecFiletype(String codecFiletype)
	{
		this.codecFiletype = codecFiletype;
	}

	public String getCodecVideoFrameRate()
	{
		return this.codecVideoFrameRate;
	}

	public void setCodecVideoFrameRate(String codecVideoFrameRate)
	{
		this.codecVideoFrameRate = codecVideoFrameRate;
	}

	public String getCodecVideoWidth()
	{
		return this.codecVideoWidth;
	}

	public void setCodecVideoWidth(String codecVideoWidth)
	{
		this.codecVideoWidth = codecVideoWidth;
	}

	public String getCodecVideoHeight()
	{
		return this.codecVideoHeight;
	}

	public void setCodecVideoHeight(String codecVideoHeight)
	{
		this.codecVideoHeight = codecVideoHeight;
	}

	public String getCodecVideoFormat()
	{
		return this.codecVideoFormat;
	}

	public void setCodecVideoFormat(String codecVideoFormat)
	{
		this.codecVideoFormat = codecVideoFormat;
	}

	public String getCodecVideoDatarate()
	{
		return this.codecVideoDatarate;
	}

	public void setCodecVideoDatarate(String codecVideoDatarate)
	{
		this.codecVideoDatarate = codecVideoDatarate;
	}

	public String getCodecAudioFormat()
	{
		return this.codecAudioFormat;
	}

	public void setCodecAudioFormat(String codecAudioFormat)
	{
		this.codecAudioFormat = codecAudioFormat;
	}

	public String getCodecAudioDatarate()
	{
		return this.codecAudioDatarate;
	}

	public void setCodecAudioDatarate(String codecAudioDatarate)
	{
		this.codecAudioDatarate = codecAudioDatarate;
	}

	public String getCodecAudioChannel()
	{
		return this.codecAudioChannel;
	}

	public void setCodecAudioChannel(String codecAudioChannel)
	{
		this.codecAudioChannel = codecAudioChannel;
	}

	public String getCodecAudioSamplerate()
	{
		return this.codecAudioSamplerate;
	}

	public void setCodecAudioSamplerate(String codecAudioSamplerate)
	{
		this.codecAudioSamplerate = codecAudioSamplerate;
	}

	public String getCodecAudioBitpersample()
	{
		return this.codecAudioBitpersample;
	}

	public void setCodecAudioBitpersample(String codecAudioBitpersample)
	{
		this.codecAudioBitpersample = codecAudioBitpersample;
	}

	public String getCodecPictureContent()
	{
		return this.codecPictureContent;
	}

	public void setCodecPictureContent(String codecPictureContent)
	{
		this.codecPictureContent = codecPictureContent;
	}

	public String getExt1()
	{
		return this.ext1;
	}

	public void setExt1(String ext1)
	{
		this.ext1 = ext1;
	}

	public String getExt2()
	{
		return this.ext2;
	}

	public void setExt2(String ext2)
	{
		this.ext2 = ext2;
	}

	public String getExt3()
	{
		return this.ext3;
	}

	public void setExt3(String ext3)
	{
		this.ext3 = ext3;
	}

	public String getExt4()
	{
		return this.ext4;
	}

	public void setExt4(String ext4)
	{
		this.ext4 = ext4;
	}

	public String getExt5()
	{
		return this.ext5;
	}

	public void setExt5(String ext5)
	{
		this.ext5 = ext5;
	}

	public String getCodecName()
	{
		return codecName;
	}

	public void setCodecName(String codecName)
	{
		this.codecName = codecName;
	}

}