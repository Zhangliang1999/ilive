package com.bvRadio.iLive.iLive.action.admin;

import static com.bvRadio.iLive.iLive.Constants.ERROR_STATUS;
import static com.bvRadio.iLive.iLive.Constants.SUCCESS_STATUS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bvRadio.iLive.common.web.ResponseUtils;
import com.bvRadio.iLive.iLive.entity.ILiveAppointment;
import com.bvRadio.iLive.iLive.manager.ILiveAppointmentMng;

@Controller
@RequestMapping(value="/appointment")
public class ILiveAppointmentController{

	@Autowired
	private ILiveAppointmentMng iLiveAppointmentMng;
	
}
