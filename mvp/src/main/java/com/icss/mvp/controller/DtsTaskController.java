package com.icss.mvp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icss.mvp.entity.DtsInfo;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.service.DtsTakService;
import com.icss.mvp.service.DtsTaskService;
import com.icss.mvp.service.dts.DtsLeaveDINumCollector;
import com.icss.mvp.util.HttpExecuteUtils;

@RestController
@RequestMapping("/dtsTaskController")
public class DtsTaskController {
	@Resource
	private DtsTaskService dtsTaskService;
	@Resource
	private DtsTakService dtsTakService;
	@Resource
	private DtsLeaveDINumCollector dtsLeaveDINumCollector;
	@Value("${dtsUrl}")
	private String dtsUrl;
	
	
	@RequestMapping("/DtsSeverity")
	@ResponseBody
	public Map<String, Object> getDtsSeverity(String projNo){
//		Map<String, Object> req = new HashMap<>();
//		req.put("projNo", projNo);
//		String res = HttpExecuteUtils.httpGet(dtsUrl+"dtsTaskController/DtsSeverity", req);
		Map<String, Object> req = dtsTaskService.getDtsSeverity(projNo);
		return req;
	}
	@RequestMapping("/DtsSeverityByVersion")
	@ResponseBody
	public List<HashMap<String, Object>> getDtsSeverityByVersion(String projNo){
		return dtsTaskService.getDtsSeverityByVersion(projNo);
	}
	
	@RequestMapping("/DtsSeverityByEven")
	@ResponseBody
	public List<HashMap<String, Object>> getDtsSeverityByEven(DtsInfo dtsInfo){
		return dtsTaskService.getDtsSeverityByEven(dtsInfo);
	}
	
	@RequestMapping("/DtsDiList")
	@ResponseBody
	public List<Map<String, Object>> getDtsDiList(String projNo){
		List<Map<String, Object>> map = dtsTaskService.getDtsList(projNo);
		return dtsTaskService.queryDINums(map,projNo);
	}
	
	@RequestMapping("/dtsTask")
	@ResponseBody
	public void saveDtsLogs(String no){
		dtsTakService.getDTSDatas(no);
	}
}
