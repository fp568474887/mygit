package com.icss.mvp.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.IGitTaskDao;
import com.icss.mvp.dao.IProjectSourceConfigDao;
import com.icss.mvp.dao.ISvnTaskDao;
import com.icss.mvp.entity.GitLogs;
import com.icss.mvp.entity.ProjectDataSourceInfo;
import com.icss.mvp.util.GitkitUtils;

@Service
@EnableScheduling
//@PropertySource("classpath:task.properties")
public class GitTaskService
{
    private static Logger logger = Logger.getLogger(GitTaskService.class);
    
    @Autowired
    ISvnTaskDao dao;
    
    @Autowired
    IGitTaskDao gitDao;
    
    @Autowired
    private IProjectSourceConfigDao sourceDao;
    
    @Resource
	private ProjectParameterValueService projectParameterValueService;
    
    
    @Resource
    HttpServletRequest request;
	    
    @Resource
	HttpServletResponse response;
    
    /**
     * git日志抓取任务
     * 
     */
    @Transactional
//    @Scheduled(cron = "${svnTask_scheduled}")
    public void getGitlog(String no) {
    	ProjectDataSourceInfo projectData = sourceDao.queryDSByNo(no);

    	String password="";
    	String username="";
    	if(request.getCookies()!=null){
    	for(Cookie cookie:request.getCookies()){
    		if(cookie.getName().equals("username")){
        		try {
    				username=URLDecoder.decode(cookie.getValue(),"utf-8");
    			} catch (UnsupportedEncodingException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        	if(cookie.getName().equals("password")){
        		try {
    				password=URLDecoder.decode(cookie.getValue(),"utf-8");
    			} catch (UnsupportedEncodingException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
    	}
    	}

        try
        {
          Date now = new Date();
          Date lasttime = dao.searchByNo(no);
          boolean firstFlag=false; //是不是第一次采集
          if (lasttime == null) {
        	  firstFlag =true;
        	  Calendar cal = Calendar.getInstance();
        	  cal.add(Calendar.MONTH, -3);
        	  lasttime = cal.getTime();
		}
		  if(projectData!=null) {
			  username = "fp568474887";
			  password = "pr0617@@";
		      GitkitUtils.setupRepo(username,password,projectData.getGiturl());
		      List<GitLogs> gitLogs = GitkitUtils.gatherCodeByTime(lasttime, now, no);
		      if(gitLogs!=null &&gitLogs.size()>0 ) {
		      	  gitDao.saveLogList(gitLogs);
		      }
		      if(firstFlag) {
		          Map<String, Object> map = new HashMap<String, Object>();
		          map.put("NO", no);
		          map.put("lasttime", now);
		          dao.insertlasttime(map);
		      }else {
		    	  // 将此次数据采集的时间节点保存之数据库
		          Map<String, Object> map = new HashMap<String, Object>();
		          map.put("NO", no);
		          map.put("lasttime", now);
		          dao.updatelasttime( map);
		        }
		    }
	    }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }
}
