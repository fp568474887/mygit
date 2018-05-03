package com.icss.mvp.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icss.mvp.service.GitTaskService;

@Controller
@RequestMapping("/git")
public class GitTaskController
{
	
	@Resource
	private GitTaskService gitTaskService;
	
	
	/**
	 * 采集Git数据并入库
	 * @param no
	 */
	@RequestMapping("/gitTask")
	@ResponseBody
	public void getGitLog(String no)
	{
		 gitTaskService.getGitlog(no);
		
	}
}
