package com.icss.mvp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icss.mvp.Constants;
import com.icss.mvp.dao.IDtsTaskDao;
import com.icss.mvp.dao.IProjectParameterValueDao;
import com.icss.mvp.entity.DtsInfo;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.util.DateUtils;

@Service
public class DtsTaskService {
	private final static Logger LOG = Logger.getLogger(DtsTaskService.class);
	
	@Autowired
	private IDtsTaskDao dtsTaskDao;
	@Resource
	private IProjectParameterValueDao parameterValueDao;

	public Map<String, Object> getDtsSeverity(String projNo) {
		return dtsTaskDao.queryServerity(projNo);
	}

	public List<Map<String, Object>> queryDIs(String no, String date) {
		List<Map<String, Object>> hashMaps = dtsTaskDao.queryDts(no,date);
		String today = DateUtils.getToday();
		int len;
		if(date.equals("00000000")) {
			date = DateUtils.getSystemFewDay(today, -30);
			len = 30;
		}else {
			date = DateUtils.getSystemFewDay(date, 1);
			len = DateUtils.calculationTimeDifference(today,date);
		}
		List<Map<String, Object>> reList = new ArrayList<>();
		for(int i=0;i<len;i++){
			Map<String, Object> reMap = new HashMap<>();
			reMap.put("critNum", 0.0);
			reMap.put("majNum", 0.0);
			reMap.put("minNum", 0.0);
			reMap.put("sugNum", 0.0);
			for (Map<String, Object> hashMap : hashMaps) {
				String curentStatus = (String) hashMap.get("curentStatus");
				String severity = (String) hashMap.get("severity");
				String createdTime = (String) hashMap.get("createdTime");
				String lastUpdateTime = (String) hashMap.get("lastUpdateTime");
				if(Constants.CANCEL.equals(curentStatus)||Constants.CLOSE.equals(curentStatus)) {
					if(DateUtils.comparisonDateSize(createdTime,date)&&DateUtils.comparisonDateSize(date,lastUpdateTime)) {
						addNum(severity,reMap);
					}
				}else {
					if(DateUtils.comparisonDateSize(createdTime,date)) {
						addNum(severity,reMap);
					}
				}
			}
			Double dtsLeaveDI = 10*(Double)reMap.get("critNum")+
					3*(Double)reMap.get("majNum")+
					1*(Double)reMap.get("minNum")+
					0.1*(Double)reMap.get("sugNum");
			reMap.put("dtsLeaveDI", dtsLeaveDI);
			reMap.put("date", date);
			reList.add(reMap);
			date = DateUtils.getSystemFewDay(date,1);
		}
		return reList;
	}

	
	
	private void addNum(String severity, Map<String, Object> reMap) {
		if(Constants.CRITIAL.equals(severity)) {
			reMap.put("critNum", (Double)reMap.get("critNum")+1);
		}
		if(Constants.MAJOR.equals(severity)) {
			reMap.put("majNum", (Double)reMap.get("majNum")+1);
		}
		if(Constants.MINOR.equals(severity)) {
			reMap.put("minNum", (Double)reMap.get("minNum")+1);
		}
		if(Constants.SUGGESTION.equals(severity)) {
			reMap.put("sugNum", (Double)reMap.get("sugNum")+1);
		}
	}

	public List<Map<String, Object>> getDtsList(String projNo) {
		return parameterValueDao.queryDtsDiList(projNo,Constants.LEAVE_DI);
	}

	public List<Map<String, Object>> queryDINums(List<Map<String, Object>> maps, String projNo) {
		String date="00000000";
		if(maps!=null) {
			for (Map<String, Object> map : maps) {
				String newdate = (String) map.get("date");
				if(DateUtils.comparisonDateSize(date,newdate)) {
					date=newdate;
				}
			}
		}
		List<Map<String, Object>> ret = queryDIs(projNo,date);
		for (Map<String, Object> map : ret) {
			Date day =null;
			try {
				day = DateUtils.format.parse(String.valueOf(map.get("date")));
			} catch (ParseException e) {
				LOG.error("DtsLeaveDINumCollector myFormatter.parse fail!" , e);
			}
			ParameterValueNew paraValue = new ParameterValueNew();
			paraValue.setNo(projNo);
			paraValue.setParameterId(Integer.parseInt(Constants.LEAVE_DI));
			paraValue.setMonth(day);
			paraValue.setValue((Double)map.get("dtsLeaveDI"));
			parameterValueDao.insertParameterValueNew(paraValue);
		}
		for (Map<String, Object> map : maps) {
			ret.add(map);
		}
		return ret;
	}

	public List<HashMap<String, Object>> getDtsSeverityByVersion(String projNo) {
		return dtsTaskDao.queryServerityByVersion(projNo);
	}

	public List<HashMap<String, Object>> getDtsSeverityByEven(DtsInfo dtsInfo) {
		Map<String, Object> map=new HashMap<>();
		if(!setValue(map,"curentStatus",dtsInfo.getCurentStatus())) {
			return new ArrayList<>();
		}
		if(!setValue(map,"workflowStatus",dtsInfo.getWorkflowStatus())) {
			return new ArrayList<>();
		}
		map.put("projNo", dtsInfo.getProjNo());
		map.put("dimensionality", dtsInfo.getDimensionality());
		return dtsTaskDao.queryServerityByEven(map);
	}
	private boolean setValue(Map<String, Object> map,String key,String value) {
		List<String> valueList = new ArrayList<>();
		if(value!=null && !"".equals(value)) {
			String[] values = value.split(",");
			for (String value1 : values) {
				valueList.add(value1);
			}
		}
		map.put(key, valueList);
		if(valueList.size()==0) {
			return false;
		}
		return true;
	}
}
