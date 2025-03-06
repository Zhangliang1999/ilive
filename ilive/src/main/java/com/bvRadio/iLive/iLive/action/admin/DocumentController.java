package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.action.util.SubAccountCache;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.entity.ILiveFileDoc;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.manager.DocumentManagerMng;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileDocMng;
import com.bvRadio.iLive.iLive.manager.ILiveSubLevelMng;
import com.bvRadio.iLive.iLive.manager.ILiveUploadServerMng;
import com.bvRadio.iLive.iLive.util.FileUtils;
import com.bvRadio.iLive.iLive.util.HttpUtils;
import com.bvRadio.iLive.iLive.util.PictureUtils;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 文档库
 * @author Wei
 *
 */
@Controller
@RequestMapping(value="document")
public class DocumentController {
	private static final Logger log = LoggerFactory.getLogger(DocumentController.class);
	
	@Autowired
	private DocumentManagerMng documentManage;	//文档库
	
	@Autowired	
	private DocumentPictureMng documentPictureMng;	//文档图片
	
	@Autowired
	private ILiveUploadServerMng iLiveUploadServerMng;
	@Autowired
	private ILiveSubLevelMng iLiveSubLevelMng;
	@Autowired
	private ILiveFileDocMng fileDocMng;
	/**
	 * 文档库主页列表
	 * @param model
	 * @param name
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="list.do")
	public String list(Model model,String name,Integer pageNo,Integer pageSize,HttpServletRequest request) {
		UserBean user = ILiveUtils.getUser(request);
		Pagination page = new Pagination( pageNo==null?1:pageNo, 10, 0, new ArrayList<>());
		Integer level = user.getLevel();
		level = level==null?ILiveManager.USER_LEVEL_ADMIN:level;
		//查询子账户是否具有图片查看全部
		boolean per=iLiveSubLevelMng.selectIfCan(request, SubAccountCache.ENTERPRISE_FUNCTION_document);
		if(level.equals(ILiveManager.USER_LEVEL_SUB)&&!per){
			long userId = Long.parseLong(user.getUserId());
			page = documentManage.getCollaborativePage(name, pageNo==null?1:pageNo, 10, userId);
		}else{
			Integer enterpriseId = user.getEnterpriseId();
			page = documentManage.getPage(name,enterpriseId, pageNo==null?1:pageNo, 10);
		}
		Integer serverGroupId = this.selectServerGroup();
		model.addAttribute("serverGroupId", serverGroupId);
		model.addAttribute("page", page);
		model.addAttribute("totalPage", page.getTotalPage());
		model.addAttribute("name", name);
		model.addAttribute("num", page.getTotalCount());
		model.addAttribute("leftActive", "3_1");
		model.addAttribute("topActive", "2");
		
		return "document/list";
	}
	private Integer selectServerGroup() {
		return 100;
	}
	

	/**
	 * 一个文档中的所有图片
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="documentpicture")
	public String documentPicture(Model model,Long docId) {
		DocumentManager doc = documentManage.getById(docId);
		
		Pagination page = documentPictureMng.getPage(docId,1,10);
		@SuppressWarnings("unchecked")
		List<DocumentPicture> listpicu = (List<DocumentPicture>) page.getList();
		if(listpicu==null) {
			listpicu = new ArrayList<>();
		}
		model.addAttribute("doc", doc);
		model.addAttribute("listpicu", listpicu);
		model.addAttribute("page", page.getTotalPage());
		model.addAttribute("leftActive", "3_1");
		model.addAttribute("topActive", "2");
		return "document/documentpicture";
	}
	
	@ResponseBody
	@RequestMapping(value="getPicture")
	public void getPage(Integer pageNo,Long docId,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Pagination page = documentPictureMng.getPage(docId,pageNo,10);
			JSONArray jsonArray = JSONArray.fromObject(page.getList());
			result.put("status", 1);
			result.put("data", jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 保存一个文档
	 */
	@ResponseBody
	@RequestMapping(value="saveDoc",method=RequestMethod.POST)
	public void saveDoc(DocumentManager doc,String str,String str2,HttpServletResponse response,HttpServletRequest request) {
		System.out.println("开始保存文件");
		JSONObject result = new JSONObject();
		JSONObject sendjson = new JSONObject();
		UserBean user = ILiveUtils.getUser(request);
		//计算文档总的大小
		JSONArray jsonArray = JSONArray.fromObject(str2);
		Integer totalSize=0;
		for(int i=0;i<jsonArray.size();i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			totalSize=totalSize+jsonObject.getInt("size");
		}
		boolean ret=EnterpriseUtil.openEnterprise(user.getEnterpriseId(), EnterpriseUtil.ENTERPRISE_USE_TYPE_Duration,totalSize.toString() ,user.getCertStatus());
		try {
			doc.setUserId(Long.parseLong(user.getUserId()));
			doc.setUserName(user.getNickname());
			doc.setEnterpriseId(user.getEnterpriseId());
			doc.setStampTime(new Date());
			doc.setState(0);
			documentManage.saveDoc(doc,str2);
			ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
			String s =doc.getUrl();
			String string =doc.getUrl();
			string=string.substring(string.indexOf("//")+2);
			string=string.substring(string.indexOf("/"));
			string=string.substring(0,string.lastIndexOf("/"));
			string=string.replaceAll("//", "/");
			sendjson.put("fileId", doc.getId());
			sendjson.put("imgPath", string);
			sendjson.put("fileName", s.substring(s.lastIndexOf("/")+1));
			sendjson.put("filePath", doc.getUrl());
			sendjson.put("url", uploadServer.getHttpUrl());
			sendjson.put("path", uploadServer.getFtpPath());
			sendjson.put("ipAddr", uploadServer.getFtpIp());
			sendjson.put("port", uploadServer.getFtpPort());
			sendjson.put("userName", uploadServer.getFtpUsername());
			sendjson.put("pwd", uploadServer.getFtpPassword());
			String realIpAddr = ConfigUtils.get("localIP");
			System.out.println("localIP: "+realIpAddr);
			sendjson.put("callBack",realIpAddr+"/ilive/admin/document/docCallBack.do");
			System.out.println("-------------------------"+ConfigUtils.get("PPTSOMS4IP")+"/soms4/PPTTrainsServlet");
			String data= HttpUtils.sendStr(ConfigUtils.get("PPTSOMS4IP")+"/soms4/PPTTrainsServlet", "POST", sendjson.toString());
			System.out.println("-------------------------"+data);
            JSONObject json_result = JSONObject.fromObject(data);
			
			Integer status=Integer.parseInt(json_result.get("status").toString());
			String msg =json_result.get("msg").toString();
			result.put("state", status);
			result.put("msg", msg);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
			result.put("msg", "提交失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 打印图片回调
	 * @throws IOException 
	 */
	
	@RequestMapping(value="docCallBack",method=RequestMethod.POST)
	public void docCallBack(HttpServletResponse response,HttpServletRequest request) throws IOException {
		JSONObject result =new JSONObject();
		
		try {
			String string=	IOUtils.toString(request.getInputStream(), "utf-8");
			JSONObject json_result = JSONObject.fromObject(string);
			System.out.println("打印图片回调信息");
			Integer docId=Integer.parseInt(json_result.get("docId").toString());
			DocumentManager doc =documentManage.getById(docId.longValue());
			doc.setState(1);
			documentManage.update(doc);
			Integer pptCount=Integer.parseInt(json_result.get("pptCount").toString());
			String  imgUrl =json_result.get("imgUrl").toString();
			ILiveUploadServer uploadServer = iLiveUploadServerMng.getDefaultServer();
			for(int i=0;i<pptCount;i++) {
				
				DocumentPicture picture = new DocumentPicture();
				picture.setDocId(docId.longValue());
				picture.setUrl(uploadServer.getHttpUrl()+imgUrl+(i+1)+".jpg");
				picture.setName((i+1)+".jpg");
				picture.setSize(0);
				picture.setWidth(720);
				picture.setHeight(540);
				documentPictureMng.save(picture);
			}
			result.put("status", 1);
	        result.put("msg", "请求成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 0);
	        result.put("msg", "请求失败");
			
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	
	/**
	 * 删除文档
	 * @param response
	 * @param id
	 */
	@ResponseBody
	@RequestMapping(value="deleteDocument")
	public void deleteDocument(HttpServletResponse response,Long id) {
		JSONObject result =new JSONObject();
		try {
			List<ILiveFileDoc> list= fileDocMng.getListById(id);
			for (ILiveFileDoc iLiveFileDoc : list) {
				fileDocMng.delete(iLiveFileDoc.getId());
			}
			documentManage.delete(id);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 删除图片
	 * @param id
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value="delpicture.do",method=RequestMethod.POST)
	public void delPicture(Long id,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			documentPictureMng.delete(id);
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	/**
	 * 下载文档
	 */
	@ResponseBody
	@RequestMapping(value="download.do")
	public void download(HttpServletResponse response,Long id) {
		System.out.println("文档id:"+id);
		response.addHeader("Access-Control-Allow-Origin", "*");
		JSONObject result = new JSONObject();
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			DocumentManager doc = documentManage.getById(id);
			
			URL url2 = new URL(doc.getUrl());
			DataInputStream dataInputStream = new DataInputStream(url2.openStream());
			byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int length ;
			while((length = dataInputStream.read(bytes))!=-1) {
				byteArrayOutputStream.write(bytes, 0, length);
			}
			System.out.println("response开始响应");
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(doc.getName(), "utf-8"));
			response.setHeader("Content-Type","");
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(byteArrayOutputStream.toByteArray());
			outputStream.flush();
			outputStream.close();
			System.out.println("response返回完毕");
			
			result.put("status", 1);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", 2);
		}
	}
	
	@RequestMapping(value="getUrl")
	public void getUrl(HttpServletResponse response,Long id) {
		JSONObject result = new JSONObject();
		try {
			DocumentManager doc = documentManage.getById(id);
			result.put("code",0);
			result.put("url",doc.getUrl());
			result.put("name",doc.getName());
			result.put("type",doc.getType());
			System.out.println(doc.getUrl());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",1);
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
	
	@RequestMapping(value="upimgzip")
	public void upimgzip(@RequestParam MultipartFile file, String fileType, Integer width, Integer height,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Integer serverGroupId) {
		JSONObject json = new JSONObject();
		try {
			JSONArray array = new JSONArray();
			//ZIPUtil.decompression2(file.getInputStream(), "GBK", "E://test/t1");
			ZipInputStream Zin = new ZipInputStream(file.getInputStream(),Charset.forName("GBK"));
    		BufferedInputStream Bin=new BufferedInputStream(Zin);  
    		//String Parent = outPutPath; //输出路径（文件夹目录）  
    		String Parent = "E://test/t1"; //输出路径（文件夹目录）
    		File Fout=null;  
    		ZipEntry entry;  
    		try {  
    			while((entry = Zin.getNextEntry())!=null && !entry.isDirectory()){  
    				System.out.println(entry);
    				Fout=new File(Parent,entry.getName());  
    				if(!Fout.exists()){  
    					(new File(Fout.getParent())).mkdirs();  
    				}  
    				FileOutputStream out=new FileOutputStream(Fout);  
    				BufferedOutputStream Bout=new BufferedOutputStream(out);  
//    				int b;  
//    				while((b=Bin.read())!=-1){  
//    					Bout.write(b);  
//    				}
    				
    				int a;
    				byte[] bs = new byte[1024];
    				StringBuilder stringBuilder = new StringBuilder();
    				while((a=Bin.read(bs))!=-1) {
    					Bout.write(bs);
    					stringBuilder.append(new String(bs));
    				}
    				
//    				String contentType = file.getContentType();
//    				if (contentType.indexOf("image") == -1) {
//    					break;
//    				}
    				
    				String fileName = entry.getName();
    				String tempFileName = System.currentTimeMillis() + "."
    						+ fileName.substring(fileName.lastIndexOf(".") + 1);
    				String realPath = request.getSession().getServletContext().getRealPath("/temp");
    				//File tempFile = createTempFile(realPath + "/" + tempFileName, file);
    				File tempFile = Fout;
    				String filePath = FileUtils.getTimeFilePath(tempFileName);
    				String filePathPrefix = filePath.substring(0, filePath.lastIndexOf("/") + 1);
    				String thumbFilePath = null;
    				File generatorThumb = null;
    				if (width != null && height != null) {
    					generatorThumb = PictureUtils.INSTANCE.generatorThumb(tempFile, height, width);
    					thumbFilePath = filePathPrefix + generatorThumb.getName();
    				}
    				ILiveUploadServer iLiveUploadServer = iLiveUploadServerMng.getDefaultServer();
    				boolean result = false;
    				if (iLiveUploadServer != null) {
    					FileInputStream in = new FileInputStream(tempFile);
    					result = iLiveUploadServer.upload(filePath, in);
    					if (thumbFilePath != null) {
    						result = iLiveUploadServer.upload(thumbFilePath, new FileInputStream(generatorThumb));
    					}
    				}
    				if (tempFile.exists()) {
    					tempFile.delete();
    				}
    				String httpUrl = "";
    				if (result) {
    					if (thumbFilePath != null) {
    						httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath()
    								+ thumbFilePath;
    						System.out.println("1: "+httpUrl);
    					} else {
    						httpUrl = iLiveUploadServer.getHttpUrl() + iLiveUploadServer.getFtpPath() + filePath;
    						System.out.println("2: "+httpUrl);
    					}
    				}
    				
    				Bout.close();
    				out.close();  
    				System.out.println(Fout+"解压成功");      
    				
    				DocumentPicture picture = new DocumentPicture();
    				picture.setName(fileName);
    				picture.setUrl(httpUrl);
    				picture.setSize((int)entry.getSize());
    				//String[] arr = fileName.split(".");
    				//picture.setType(arr[arr.length-1]);
    				int[] arr = getwidthAndHeight(Fout);
    				picture.setWidth(arr[0]);
    				picture.setHeight(arr[1]);
    				
    				array.add(JSONObject.fromObject(picture));
    			}  
    			System.out.println(array);
    			Bin.close();  
    			Zin.close();  
    			json.put("data", array);
    			json.put("code", "1");
    		} catch (IOException e) {  
    			e.printStackTrace();  
    			json.put("code", "2");
    		}
		}catch (Exception e) {
			log.info(e.toString());
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	/**
	 * 构建临时文件
	 * 
	 * @param tempFilePath
	 * @param file
	 * @return
	 */
	private File createTempFile(String tempFilePath, MultipartFile file) {
		long start = System.currentTimeMillis();
		File tempFile = new File(tempFilePath);
		if (null != tempFile && !tempFile.exists()) {
			tempFile.mkdirs();
		}
		try {
			file.transferTo(tempFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		log.info("创建临时上传文件耗时：{} ms", end - start);
		return tempFile;
	}
	
	private static int[] getwidthAndHeight(File file) {
		int[] arr = new int[2];
		try {
			InputStream inputStream = new FileInputStream(file);
			BufferedImage bufferedImage = javax.imageio.ImageIO.read(inputStream);
			arr[0] = bufferedImage.getWidth();
			arr[1] = bufferedImage.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return arr;
	}
	
	@RequestMapping(value="batchdel.do")
	public void batchdel(HttpServletResponse response,String str) {
		JSONObject result = new JSONObject();
		try {
			List<Long> list = new ArrayList<>();
			JSONArray array = JSONArray.fromObject(str);
			for(int i=0;i<array.size();i++) {
				JSONObject json = array.getJSONObject(i);
				list.add(json.getLong("id"));
				List<ILiveFileDoc> flist= fileDocMng.getListByDocId(json.getLong("id"));
				if(flist!=null&&flist.size()!=0) {
					for (ILiveFileDoc iLiveFileDoc : flist) {
						fileDocMng.delete(iLiveFileDoc.getId());
					}
				}
				
			}
			
			documentManage.batchDel(list);
			result.put("code",0);
			result.put("msg","删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",1);
			result.put("msg","删除失败");
		}
		ResponseUtils.renderJson(response, result.toString());
	}
	
}
