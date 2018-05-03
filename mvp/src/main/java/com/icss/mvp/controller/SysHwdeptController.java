package com.icss.mvp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icss.mvp.entity.SysHwdept;
import com.icss.mvp.service.SysHwdeptService;

@RestController
@RequestMapping("/sysHwdept")
public class SysHwdeptController {
	@Resource
	private HttpServletRequest request;

	@Resource
	private SysHwdeptService sysHwdeptService;
	
	/**
	 * <p>Title: getSysHwdeptByPId</p>  
	 * <p>Description: 通过父id查找下面的子节点信息</p>  
	 * @param pId
	 * @author gaoyao
	 * @return
	 */ 
	@RequestMapping("/getSysHwdeptByPId")
	@ResponseBody
	public Map<String, Object> getSysHwdeptByPId(String pId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<SysHwdept> list = sysHwdeptService.getSysHwdeptByPId(pId);
			map.put("data", list);
			map.put("msg", "返回成功");
			map.put("status", "0");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "返回失败");
			map.put("status", "1");
		}
		return map;
	}
}
