package com.icss.mvp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;



import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.IDtsTaskDao;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectSourceConfigDao;
import com.icss.mvp.entity.ProjectDataSourceInfo;
import com.icss.mvp.service.dts.DtsIndicatorComputer;
import com.icss.mvp.util.HttpClientUtil;
import com.icss.mvp.util.XmlSaxParserUtil;

@Service
@EnableScheduling
@PropertySource("classpath:task.properties")
public class DtsTakService
{
	@Autowired
	private IProjectSourceConfigDao sourceDao;

	@Autowired
	private IDtsTaskDao dtsTaskDao;

	private final static Logger LOG = Logger.getLogger(DtsTakService.class);

	@Value("${dts_file_path}")
	public String fPath;
	
	@Resource
	private DtsIndicatorComputer computer;

	
	@Resource
	private IProjectListDao projectListDao;
	
	@Resource
    HttpServletRequest request;
	    
   @Resource
	 HttpServletResponse response;
	
	/**
	 * 每天凌晨获取DTS数据并入库
	 */
	@Transactional
//	@Scheduled(cron = "${dtsTask_scheduled}")
	public void getDTSDatas(String no)
	{
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
    	String versionPath = "";
    	ProjectDataSourceInfo projectData = sourceDao.queryDSByNo(no);
    	if(projectData!=null && StringUtils.isNotEmpty(projectData.getVersion())) {
    		versionPath = projectData.getVersion();
    	}
		
//		List<ProjectDataSourceInfo> dtsLists = sourceDao.queryDSBySource("5");
//		for (ProjectDataSourceInfo dataSource : dtsLists)
//		{
//			String fileName = HttpClientUtil.getInstance().createDTSFile(
//					dataSource.getUser(), dataSource.getPassword(),
//					dataSource.getNo());
    		if(!StringUtils.isBlank(versionPath)) {
    			if(versionPath.indexOf("/")>=0) {
    				String[] versionArray=versionPath.split("/");
    				if(versionArray!=null &&versionArray.length>0) {
    					String fileName = HttpClientUtil.getInstance().createDTSFile(
    							username, password,
    							no,versionPath);
    					if(StringUtils.isNotEmpty(fileName)) {
    						readDtsToDBByXml(fileName,no);
    					}
    				}
    			}
    		}
			
//		}
//		computer.collectIndicators();
	}
}

	/**
	 * 读取DTS文件并入库
	 * 
	 * @param fileName
	 */
	private void readDtsToDBByXml(String fileName,String no)
	{
		try {
			XmlSaxParserUtil sax= new XmlSaxParserUtil(dtsTaskDao,no);
			InputStream input =new FileInputStream(fileName);
			List<Map<String,String>> contentsList=sax.getContents(input);
			if(contentsList.size()>0) {
				dtsTaskDao.insert(contentsList);
			}
		} catch (FileNotFoundException e) {
			LOG.error("file not found!" + fileName, e);
		} catch(IOException e) {
			LOG.error("parse excel failed!" + fileName, e);
		} catch (Exception e) {
			LOG.error("parse excel failed!" + fileName, e);
		}
	}

	/**
	 * 月度汇总DTS数据
	 * 
	 * @param month
	 */
	public void doMonthCollect(String month)
	{
		// TODO Auto-generated method stub
	}

}
