package com.bvRadio.iLive.iLive.action.front.phone;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.DocumentPicture;
import com.bvRadio.iLive.iLive.entity.ILiveFileDoc;
import com.bvRadio.iLive.iLive.manager.DocumentManagerMng;
import com.bvRadio.iLive.iLive.manager.DocumentPictureMng;
import com.bvRadio.iLive.iLive.manager.ILiveFileDocMng;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value="document")
public class DocumentManagerController {

	@Autowired
	private DocumentManagerMng documentManagerMng;
	
	@Autowired
	private DocumentPictureMng documentPictureMng;
	@Autowired
	private ILiveFileDocMng fileDocMng;
	
	/**
	 * 根据企业id获取所有文档
	 * @param enterpriseId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="getByEnterpriseId.jspx")
	public void getDoc(Integer enterpriseId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			List<DocumentManager> list = documentManagerMng.getListByEnterpriseId(enterpriseId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取列表成功");
			resultJson.put("data",JSONArray.fromObject(list));
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取列表失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	/**
	 * 根据文档id获取所有图片
	 * @param docId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="getPicByDocId.jspx")
	public void getPicByDocId(Long docId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			List<DocumentPicture> list = documentPictureMng.getListByDocId(docId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取列表成功");
			resultJson.put("data",JSONArray.fromObject(list));
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取列表失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	
	/**
	 * 根据文档id文档信息及文档包含的图片
	 * @param docId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="getByd.jspx")
	public void getById(Long docId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			DocumentManager doc = documentManagerMng.getById(docId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			resultJson.put("data",JSONObject.fromObject(doc));
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
			resultJson.put("data", new JSONObject());
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
	/**
	 * 根据文件id获取文档信息 文档包含的图片
	 * @param docId
	 * @param response
	 * @param request
	 */
	@RequestMapping(value="getByfileId.jspx")
	public void getByfileId(Long fileId,HttpServletResponse response,HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			List<ILiveFileDoc> fileDoc =fileDocMng.getListById(fileId);
			resultJson.put("code", SUCCESS_STATUS);
			resultJson.put("message", "获取信息成功");
			resultJson.put("data",JSONArray.fromObject(fileDoc));
		} catch (Exception e) {
			e.printStackTrace();
			resultJson.put("code", ERROR_STATUS);
			resultJson.put("message", "获取信息失败");
		}
		ResponseUtils.renderJson(request, response, resultJson.toString());
	}
}
