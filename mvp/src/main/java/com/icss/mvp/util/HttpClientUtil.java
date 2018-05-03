package com.icss.mvp.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.StringUtil;


public class HttpClientUtil {
	
	private static final String DTS_URL = "dts.huawei.com";
	private static final String DOMAIN = "CHINA";
	private static final int PORT = 80;
	private static final String WORK_STATION = "myworkstation";
//	private static final String SCHEMA_NAME = "ntlm";
	private static final String HTTP = "http";
	private static final String GET_URL="http://dts.huawei.com/net/dts/DTS/DTSList.aspx?type=AllWork&IsHistory=0&dtsbizbusinesslist_allworkGroupDataGet=1";
	private static final String ALL_WORK_GROUP="&dtsbizbusinesslist_allworkGroupIndex=";
	private static final String ALL_WORK_FILTER_VALUA="&dtsbizbusinesslist_allworkFilterValue";
	private static final String GET_FILE_URL="http://dts.huawei.com/net/dts/DTS/DTSList.aspx?type=AllWork&IsHistory=0&dtsbizbusinesslist_allworkExcelExport=1&dtsbizbusinesslist_allworkExportType=1&dtsbizbusinesslist_allworkExportField=1";
	private static final String GET_NO_URL_PD="http://dts.huawei.com/net/dts/DTS/DTSList.aspx?type=AllWork&IsHistory=0&dtsbizbusinesslist_allworkGroupDataGet=1&dtsbizbusinesslist_allworkGroupIndex=2&Lang=EN";
	//	private static final String GET_FILE_URL = "/iisstart.png"; 

	private static HttpClientUtil instance = null;

	private HttpClientUtil() {
	}

	public static HttpClientUtil getInstance() {
		if (instance == null) {
			instance = new HttpClientUtil();
		}
		return instance;
	}

	public String createDTSFile(String userName,String password,String projectNo,String version){
		if(StringUtils.isBlank(version)) {
			return "";
		}
		String fileName = "";
		
		DefaultHttpClient httpClient =new DefaultHttpClient();
		NTCredentials creds =new NTCredentials(userName,password,WORK_STATION,DOMAIN);
		httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
		CloseableHttpResponse response =null;
		try {
			HttpHost target = new HttpHost(DTS_URL, PORT, HTTP);
			// 保证相同的容内来用于执行逻辑相关的请求
			HttpContext localContext = new BasicHttpContext();
			try {
				String noUrl=getNoUrl(httpClient,target,localContext,version);
				if(!StringUtils.isBlank(noUrl)) {
					HttpGet httpGet=new HttpGet(GET_FILE_URL+noUrl);
					response=httpClient.execute(target,httpGet,localContext);
					if(response.getStatusLine().getStatusCode()==200) {
						HttpEntity entity =response.getEntity();
						if(entity!=null) {
							InputStream is=entity.getContent();
							Date currentTime =new Date();
							SimpleDateFormat formatter =new SimpleDateFormat("yyyyMMddHHmmss");
							String dateString =formatter.format(currentTime);
							String tmpName=projectNo+"_"+dateString+".xml";
							String filePath ="D:\\MVP_FILE\\DTS";
							fileName = createFile(filePath,tmpName);
							if(!fileName.equals(null)&& !fileName.isEmpty()) {
								File file =new File(fileName);
								FileOutputStream fileout =new FileOutputStream(file);
								BufferedOutputStream bos =new BufferedOutputStream(fileout);
								byte[] buffer =new byte[1024];
								int ch=0;
								while((ch=is.read(buffer))!=-1) {
									for (int j = 0; j <= buffer.length-1; j++) {
										if(buffer[j]=="&".getBytes()[0]) {
											buffer[j]=" ".getBytes()[0];
										}
									}
									bos.write(buffer, 0, ch);
								}
								bos.flush();
								fileout.flush();
								fileout.close();
								is.close();
								bos.close();
							}
						}
					}
				}
//				EntityUtils.consume(response.getEntity()); 
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.close();
		}
		return fileName;
	}
	public static String createFile(String dirPath,String fileName){
		try {
			File dir = new File(dirPath);
			if (!dirPath.endsWith(File.separator)) {// 结尾是否以"/"结束
				dirPath = dirPath + File.separator;
			}
			if (!dir.exists()){
				dir.mkdirs();
			}
			String filePathName = dirPath + fileName;
			File file = new File(filePathName);
			if (!file.exists()){
				file.createNewFile();
			}
			return filePathName;
		} catch (Exception e) {// 捕获异常
			e.printStackTrace();
			return "";
		}
	}
	
	private static String getNoUrl(DefaultHttpClient httpClient,HttpHost target,HttpContext localContext,String version)throws ClientProtocolException,IOException {
		String noUrl="";
		if(version.indexOf("/")>=0) {
			String[] versionArray =version.split("/");
			if(versionArray!=null&& versionArray.length>0&& versionArray.length<=8) {
				int pathCnt =versionArray.length;
				for (int i = 0; i <=pathCnt-1; i++) {
					noUrl =noUrl +getURL(httpClient,target,localContext,i,versionArray[i],noUrl);
				}
			}
		}
			
		return noUrl;
	}
	
	private static String getURL(DefaultHttpClient httpClient,HttpHost target,HttpContext localContext,int i,String version,String parentUrl)throws ClientProtocolException,IOException {
		String url="";
		String no ="";
		String getUrl=GET_URL+ALL_WORK_GROUP+String.valueOf(i+2)+parentUrl;
		HttpGet httpGet =new HttpGet(getUrl);
		CloseableHttpResponse response =httpClient.execute(target,httpGet,localContext);
		if(response.getStatusLine().getStatusCode()==200) {
			String responseContent =EntityUtils.toString(response.getEntity(),"UTF-8");
			if(responseContent.indexOf("||")>=0) {
				for (String json:responseContent.split("\\|\\|")) {
					String[] str =json.split(":");
					if(str!=null && str.length>1) {
						if(str[1].equals(version)) {
							no=str[0];
							break;
							}
						}
					}
				}else {
					String json =responseContent;
					String[] str =json.split(":");
					if(str!=null&& str.length>1) {
						if(str[1].equals(version)) {
							no=str[0];
						}
					}
				}
			}
			if(!StringUtils.isBlank(no)) {
			url=ALL_WORK_FILTER_VALUA+String.valueOf(i+2)+"="+no.replace("|", "%7C");
			}
		return url;
		}
	}
