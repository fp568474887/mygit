package com.icss.mvp.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.tmatesoft.svn.core.SVNException;

import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectParameterValueDao;
import com.icss.mvp.dao.IProjectSourceConfigDao;
import com.icss.mvp.dao.ISvnTaskDao;
import com.icss.mvp.entity.CodeQualityInfo;
import com.icss.mvp.entity.ProjectDataSourceInfo;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.SvnLogs;
import com.icss.mvp.util.SvnkitUtils;

@Service
@EnableScheduling
//@PropertySource("classpath:task.properties")
public class SvnTaskService
{
    private static Logger logger = Logger.getLogger(SvnTaskService.class);
    
    @Autowired
    ISvnTaskDao dao;
    
    @Autowired
    private IProjectParameterValueDao paramDao;
    
    @Autowired
    private IProjectSourceConfigDao sourceDao;
    
    @Resource
	private ProjectParameterValueService projectParameterValueService;
    
    @Resource
	private IProjectListDao projectListDao;
    
    @Resource
     HttpServletRequest request;
	    
    @Resource
	 HttpServletResponse response;
    
    /**
     * 
     * <pre>
     * <b>描述：svn日志抓取任务</b>
     * <b>任务编号：</b>
     * <b>作者：张鹏飞</b> 
     * <b>创建日期： 2017年5月19日 下午5:38:24</b>
     * </pre>
     * @throws UnsupportedEncodingException 
     */
    @Transactional
//    @Scheduled(cron = "${svnTask_scheduled}")
    public int getSvnlog(String no) {
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
//            Date dayAgo = new Date(now.getTime() - 3600000 * 24);

           
//            cal.add(Calendar.MONTH, -1);
          Date lasttime = dao.searchByNo(no);
          boolean firstFlag=false; //是不是第一次采集
          if (lasttime == null) {
        	  firstFlag =true;
        	  Calendar cal = Calendar.getInstance();
        	  cal.add(Calendar.MONTH, -6);
        	  lasttime = cal.getTime();
		}
          if(projectData!=null) {
        	  	int i = 0;
            	SvnkitUtils.setupLibrary(username, password, projectData.getUrl());
                List<SvnLogs> logList = SvnkitUtils.staticticsCodeAddByTime(lasttime, now,no);
                if(logList!=null && logList.size()>0) {
                	i = dao.saveLogList(logList);
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
                return i;
            }
        }
        catch (ParseException e)
        {
            logger.error(e.getMessage());
        }
        catch (SVNException e)
        {
            logger.error(e.getMessage());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
        return 0;
    }
    
    /**
     * 
     * <pre>
     * <b>描述：svn代码新增行数月度汇总</b>
     * <b>任务编号：</b>
     * <b>作者：张鹏飞</b> 
     * <b>创建日期： 2017年5月19日 下午5:42:27</b>
     *  @param month
     * </pre>
     *  
     */
    public void doMonthCollect(String month)
    {
        List<CodeQualityInfo> list = dao.getMonthCollect(month);
        DateFormat format = new SimpleDateFormat("yyyyMMdd"); 
        String monthAll = month + "01";
        Date dateMonth = null;
        
		try {
			dateMonth = format.parse(monthAll);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (dateMonth != null){
			for(CodeQualityInfo info : list)
	        {
				double codeValue = Double.valueOf(info.getCodetotal());
				
	        	projectParameterValueService.insertParameterValue(info.getNo(), dateMonth, 129, codeValue);
	        	ProjectDetailInfo projectDetailInfo = projectListDao.isExit(info.getNo());
	        	double rateValue = 0;
	        	if (projectDetailInfo != null && projectDetailInfo.getCountOfDay() > 0 ){
	        		rateValue = codeValue/projectDetailInfo.getCountOfDay();
	        	}
	        	projectParameterValueService.insertParameterValue(info.getNo(), dateMonth, 83, rateValue);
	        }
		}
    }
    
    
    public Date searchUpdateTime(String proNo) {
    	return dao.searchByNo(proNo);
    }
    
    public void saveurl(ProjectDataSourceInfo projectDataSourceInfo) {
    	ProjectDataSourceInfo projectData = sourceDao.queryDSByNo(projectDataSourceInfo.getNo());
    	if (projectData != null) {
//    		projectData.setSource_value("100");
    		projectData.setUrl(projectDataSourceInfo.getUrl());
    		projectData.setVersion(projectDataSourceInfo.getVersion());
    		projectData.setGiturl(projectDataSourceInfo.getGiturl());
    		projectData.setIsourl(projectDataSourceInfo.getIsourl());
    		projectData.setCiurl(projectDataSourceInfo.getCiurl());
    		projectData.setUpdateUser(projectDataSourceInfo.getUser());
    		projectData.setUpdateDate(new Date());
    		sourceDao.updateurl(projectData);
		}else {
			ProjectDataSourceInfo dataSource =new ProjectDataSourceInfo();
			dataSource =projectDataSourceInfo;
			dataSource.setSource_value("100");
			dataSource.setCreator(projectDataSourceInfo.getUser());
			dataSource.setUpdateUser(projectDataSourceInfo.getUser());
			dataSource.setCreateDate(new Date());
			dataSource.setUpdateDate(new Date());
			sourceDao.inserturl(dataSource);
		}
    }
}
