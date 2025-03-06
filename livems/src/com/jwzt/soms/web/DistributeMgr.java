package com.jwzt.soms.web;
import java.util.Vector;
import com.jwzt.common.SomsConfigInfo;

public class DistributeMgr
{
	 /**
     * 根据文件的后缀判断所用协议(增加对预览文件的判断)
     * 如果是多端口的随机
     * @param sFileType
     * @return
     */
    public static PlayInfo getMediaType(String sMainFileType, String sPreviewFileType, PlayInfo playerInfo)
    {
        //规范主文件类型的格式: ;wmv;asf;wma;
    	sMainFileType = ";" + sMainFileType.toLowerCase() + ";";
        sMainFileType = sMainFileType.replaceAll(" ","");

        //规范预览文件类型的格式: ;wmv;asf;wma;
        sPreviewFileType = ";" + sPreviewFileType.toLowerCase() + ";";
        sPreviewFileType = sPreviewFileType.replaceAll(" ","");

    	//从配置文件获取媒体类型所对应的网络协议, 端口号等信息
        String sMediaTypeDesc = SomsConfigInfo.get("media_type");
    	sMediaTypeDesc = sMediaTypeDesc.replaceAll("\\n","");
    	sMediaTypeDesc = sMediaTypeDesc.replaceAll("\\t","");
    	sMediaTypeDesc = sMediaTypeDesc.replaceAll(" ","");


    	String sProtocol = "";				//主文件使用协议
    	String sPort = "";					//主文件使用端口
    	String sFileType = "";				//主文件协议支持的文件类型
    	String sPlayerType = "";			//主文件使用的播放器类型
    	String sPreviewProtocol = "";		//预览文件使用协议
    	String sPreviewPort = "";			//预览文件使用端口
    	String sPreFileType = "";			//预览文件协议支持的文件类型
    	String sPreviewPlayerType = "";		//预览文件使用的播放器类型

    	boolean sFileReady = false;			//主文件信息准备就绪
    	boolean sPreFileReady = false;		//分段文件信息准备就绪



    	//获取(protocol)列表
    	Vector tagValueList = getMyTagValue("protocol",sMediaTypeDesc);
    	if(tagValueList != null && tagValueList.size()>0)
    	{
    	    //解析每个(protocol)
    	    for(int i=0; i<tagValueList.size(); i++)
    	    {
    	        String tagValue = (String)tagValueList.get(i);

    	        //在相应的(protocol)中解析(fileType)
    	        Vector fileTypeTagList = getMyTagValue("fileType",tagValue);
	            String fileTypeTagValue = ";" + (String)fileTypeTagList.get(0) + ";";
	            fileTypeTagValue = fileTypeTagValue.toLowerCase();

	            //判断该文件的后缀是否在该(protocol)中，如果是(default)则选择默认的配置
	            if(!sFileReady && (fileTypeTagValue.indexOf(sMainFileType) != -1 || fileTypeTagValue.equals(";default;")))
	            {
	                //获取该(protocol)的协议，如：rtsp
	                Vector nameTagList = getMyTagValue("name",tagValue);
	                sProtocol = ((String)nameTagList.get(0)).toLowerCase();


	                //获取该(protocol)使用的播放器类型，如：media player
		            Vector mediaTypeTagList = getMyTagValue("mediaType",tagValue);
		            sPlayerType = ((String)mediaTypeTagList.get(0)).toLowerCase();


	                //获取该(protocol)所用的端口，如果有多个端口则随机一个，如：5540
		            Vector portTagList = getMyTagValue("port",tagValue);
		            sPort = ((String)portTagList.get(0)).toLowerCase();
		            if(sPort.length() > 0)
		            {
		                String[] sPorts = sPort.split(";");
		                int nRandom = (int)(Math.random()*sPorts.length);
		                sPort = sPorts[nRandom];
		            }

	                //获取该(protocol)所适用的文件后缀串，如：wmv;wma;asf;mp3
		            sFileType = ((String)fileTypeTagList.get(0)).toLowerCase();

		            sFileReady = true;
	            }

	            //判断该文件的后缀是否在该(protocol)中，如果是(default)则选择默认的配置
	            if(!sPreFileReady && (fileTypeTagValue.indexOf(sPreviewFileType) != -1 || fileTypeTagValue.equals(";default;")))
	            {
	                //获取该(protocol)的协议，如：rtsp
	                Vector nameTagList = getMyTagValue("name",tagValue);
	                sPreviewProtocol = ((String)nameTagList.get(0)).toLowerCase();


	                //获取该(protocol)使用的播放器类型，如：media player
		            Vector mediaTypeTagList = getMyTagValue("mediaType",tagValue);
		            sPreviewPlayerType = ((String)mediaTypeTagList.get(0)).toLowerCase();


	                //获取该(protocol)所用的端口，如果有多个端口则随机一个，如：5540
		            Vector portTagList = getMyTagValue("port",tagValue);
		            sPreviewPort = ((String)portTagList.get(0)).toLowerCase();
		            if(sPort.length() > 0)
		            {
		                String[] sPorts = sPort.split(";");
		                int nRandom = (int)(Math.random()*sPorts.length);
		                sPort = sPorts[nRandom];
		            }

	                //获取该(protocol)所适用的文件后缀串，如：wmv;wma;asf;mp3
		            sPreFileType = ((String)fileTypeTagList.get(0)).toLowerCase();

		            sPreFileReady = true;
	            }
    	    }
    	}

    	playerInfo.sProtocol = sProtocol;
    	playerInfo.sPort = sPort;
    	playerInfo.sFileType = sFileType;
    	playerInfo.sPlayerType = sPlayerType;
    	playerInfo.sPreviewProtocol = sPreviewProtocol;
    	playerInfo.sPreviewPort = sPreviewPort;
    	playerInfo.sPreviewFileType = sPreFileType;
    	playerInfo.sPreviewPlayerType = sPreviewPlayerType;

        return playerInfo;
    }
    /**
     * 解析自定义标签(protocol)(protocol)
     *
     * @param sTag
     * @param sStr
     * @return
     */
    public static Vector getMyTagValue(String sTag, String sStr)
    {
        Vector tagValueList = new Vector();
        String sTempStr = sStr;

        String sStartTag = "(" + sTag + ")";
        String sEndTag = "(/" + sTag + ")";

        while (sTempStr.indexOf(sStartTag) != -1)
        {
            String sValue = "";
            sValue = sTempStr.substring(sTempStr.indexOf(sStartTag)+sStartTag.length(), sTempStr.indexOf(sEndTag));
            sTempStr = sTempStr.substring(sTempStr.indexOf(sEndTag)+sEndTag.length(), sTempStr.length());
            tagValueList.add(sValue);
        }

        return tagValueList;
    }
    
}
