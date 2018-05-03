package com.icss.mvp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.entity.GerenCode;
import com.icss.mvp.entity.ProjectDataSourceInfo;
import com.icss.mvp.service.DevelopPageService;
import com.icss.mvp.service.SvnTaskService;
import com.icss.mvp.util.HttpExecuteUtils;

@Controller
@RequestMapping("/svnTask")
public class SvnTaskController
{
	@Resource
	private SvnTaskService svnTaskService;
	
	@Resource
	private DevelopPageService developPageService;
	
	/**
	 * 采集SVN数据并存入数据库
	 * @return
	 */
	@RequestMapping("/svn")
	@ResponseBody
	public int saveSvn(String no)
	{
//		Map<String, Object> request = new HashMap<String, Object>();
//		request.put("no", no);

//		String jsonString = HttpExecuteUtils.httpGet("http://127.0.0.1:8089/svnTask/svn", request);
//		System.out.println(jsonString);
		
		
		return svnTaskService.getSvnlog(no);
		
	}
	
	/**
	 * 员工个人代码量统计
	 * @param projNo
	 * @return
	 */
	@RequestMapping("/searchGeRen")
	@ResponseBody
	public List<GerenCode> searchGeRen(String projNo)
	{
//		Map<String, Object> request = new HashMap<String, Object>();
//		request.put("projNo", projNo);
//		String jsonString = HttpExecuteUtils.httpGet("http://127.0.0.1:8089/svnTask/searchGeRen", request);
//		System.out.println(jsonString+"######");
		return developPageService.developSearch(projNo);
		
	}
	
	/**
	 * 查询数据更新时间
	 * @param projNo
	 * @return
	 */
	@RequestMapping("/searchUpdateTime")
	@ResponseBody
	public Date searchUpdateTime(String projNo)
	{
//		Map<String, Object> source = new HashMap<String,Object>();
//		source.put("projNo", projNo);
////		String jsonString = HttpExecuteUtils.httpGet("http://127.0.0.1:8089/svnTask/searchUpdateTime", source);
////		System.out.println(jsonString+"***********");
		return svnTaskService.searchUpdateTime(projNo);
		
	}
	
	@RequestMapping("/saveurl")
	@ResponseBody
	public void saveurl(ProjectDataSourceInfo projectDataSourceInfo)
	{
//		Map<String, Object> source = new HashMap<String,Object>();
//		source.put("ProjectDataSourceInfo",projectDataSourceInfo );
//		String jsonString = HttpExecuteUtils.httpGet("http://127.0.0.1:8089/svnTask/saveurl", source);
//		System.out.println(jsonString+"$$$$$$$$$$$$$$$$$$$$");
		 svnTaskService.saveurl(projectDataSourceInfo);
		
	}
}
