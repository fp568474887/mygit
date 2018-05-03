package com.icss.mvp.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.TblArea;
import com.icss.mvp.service.TblAreaService;

@Controller
@RequestMapping("/tblArea")
public class TblAreaController
{
	@Resource
	private TblAreaService tblAreaService;
	
	/**
	 * <p>Title: getAllTblArea</p>  
	 * <p>Description: 获取所有的地域</p>  
	 * @author gaoyao
	 * @return
	 */ 
	@RequestMapping("/getAllTblArea")
	@ResponseBody
	public Map<String, Object> getAllTblArea() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<TblArea> list = tblAreaService.getAllTblArea();
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
