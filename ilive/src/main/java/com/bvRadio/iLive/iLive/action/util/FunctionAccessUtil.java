package com.bvRadio.iLive.iLive.action.util;

import javax.servlet.http.HttpServletRequest;

import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.web.ILiveUtils;

public class FunctionAccessUtil {
	
  public static boolean IfCanAccess(String function,HttpServletRequest request) {
	 boolean ret=false;
	 UserBean userBean = ILiveUtils.getUser(request);
	 String functionCode=userBean.getFunctionCode();
	  if(!"-1".equals(functionCode)||functionCode.indexOf(function)!=-1) {
		 ret=true;
	 }else{
		 ret=false;
	 } 
	 return ret;
  }
}
