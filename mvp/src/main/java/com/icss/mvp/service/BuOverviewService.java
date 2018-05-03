package com.icss.mvp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.Constants;
import com.icss.mvp.dao.IBuOverviewDao;
import com.icss.mvp.dao.IProjectKeyrolesDao;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectScheduleDao;
import com.icss.mvp.entity.MeasureInfo;
import com.icss.mvp.entity.MonthSummary;
import com.icss.mvp.entity.OrganizeMgmer;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ParameterValueNew;
import com.icss.mvp.entity.ProjProductivity;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.ProjectManager;
import com.icss.mvp.entity.ProjectParameterValue;
import com.icss.mvp.entity.ProjectSchedule;
import com.icss.mvp.entity.ProjectSummary;
import com.icss.mvp.util.DateUtils;

/**
 * @author zw
 *
 */
/**
 * @author zw
 * 
 */
@Service("buOverviewService")
@Transactional
public class BuOverviewService {
	@Resource
	private IBuOverviewDao buOverviewDao;
	@Resource
	private IProjectKeyrolesDao projectKeyrolesDao;
	@Resource
	private IProjectScheduleDao projectScheduleDao;

	@Resource
	private IProjectListDao projectListDao;
	private final static Logger LOG = Logger.getLogger(BuOverviewService.class);
	private final static String[] HEADERS = { "项目名称", "项目经理", "地域", "华为产品线", "子产品线", "PDU/SPDT", "业务线", "事业部", "交付部", "计费类型", "项目状态" };

	public List<OrganizeMgmer> getBUs() {
		return buOverviewDao.getBuOpts();
	}

	public List<ProjectSummary> getProjSummaries(String buName) {
		return buOverviewDao.getProjectSummaries(buName);
	}

	public List<MonthSummary> getMonthSummary(String buName) {
		return buOverviewDao.getMonthSummary(buName);
	}

	public List<ProjProductivity> getProjProductity(String buName) {
		return buOverviewDao.getProjProductity(buName);
	}

	public Map<String, Object> getProjDetail(ProjectInfo proj) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> titles = new LinkedList<Map<String, String>>();
		Map<String, String> title = null;
		MeasureInfo titleInfo = null;
		for (String header : HEADERS) {
			titleInfo = new MeasureInfo();
			titleInfo.setName(header);
			title = new HashMap<String, String>();
			title.put("field", header);
			title.put("title", header);
			titles.add(title);
		}
		result.put("gridTitles", titles);
		Map<String, Object> map=setMap(proj);
		List<MeasureInfo> gridDatas = buOverviewDao.getProjDetail(map);
		List<Map<String, String>> gridDataRecords = new LinkedList<Map<String, String>>();
//		Map<String, Object> tmpMap = new HashMap<String, Object>();
		for (MeasureInfo gridData : gridDatas) {
			Map<String, String> record = new HashMap<String, String>();
//			if (StringUtils.isEmpty(gridData.getMonth())) {
				record.put("项目名称", gridData.getProjectName());
				record.put("项目经理", gridData.getPm());
				record.put("地域", gridData.getArea());
				record.put("华为产品线", gridData.getHwpdu());
				record.put("子产品线", gridData.getHwzpdu());
				record.put("PDU/SPDT", gridData.getPduSpdt());
				record.put("业务线", gridData.getBu());
				record.put("事业部", gridData.getPdu());
				record.put("交付部", gridData.getDu());
				record.put("计费类型", gridData.getType());
				record.put("项目状态", gridData.getProjectState());
				record.put("项目编码", gridData.getNo());
				gridDataRecords.add(record);
//			} else {
//				if (tmpMap.get(gridData.getMonth() + gridData.getNo()) == null) {
//					record.put(gridData.getName(), gridData.getValue());
//					record.put("地域", gridData.getArea());
//					record.put("计费类型", gridData.getType());
//					record.put("项目编码", gridData.getNo());
//					record.put("项目名称", gridData.getProjectName());
//					record.put("项目经理", gridData.getPm());
//					tmpMap.put(gridData.getMonth() + gridData.getNo(), record);
//				} else {
//					record = (Map<String, String>) tmpMap.get(gridData.getMonth() + gridData.getNo());
//					gridDataRecords.remove((Map<String, String>) tmpMap.get(gridData.getMonth() + gridData.getNo()));
//					record.put(gridData.getName(), gridData.getValue());
//				}
//				gridDataRecords.add(record);
//			}
		}
		Map<String, Object> tableRslts = new HashMap<String, Object>();
		tableRslts.put("rows", gridDataRecords);
		tableRslts.put("total", gridDataRecords.size());
		result.put("gridDatas", tableRslts);
		return result;
	}
	
	private Map<String, Object> setMap(ProjectInfo proj){
		Map<String, Object> map = new HashMap<>(); 
		if(proj.getName()!=null && !"".equals(proj.getName())) {
			map.put("name", proj.getName());
		}
		if(proj.getPm()!=null && !"".equals(proj.getPm())) {
			map.put("pm", proj.getPm());
		}
		if(proj.getArea()!=null && !"".equals(proj.getArea())) {
			List<String> list=new ArrayList<>();
			String[] areas=proj.getArea().split(",");
			for(String area:areas) {
				list.add(area);
			}
			map.put("area", list);
		}else {
			map.put("area", new HashMap<>());
		}
		if(proj.getHwpdu()!=null && !"".equals(proj.getHwpdu())) {
			List<String> list=new ArrayList<>();
			String[] hwpdus=proj.getHwpdu().split(",");
			for(String hwpdu:hwpdus) {
				list.add(hwpdu);
			}
			map.put("hwpdu", list);
		}else {
			map.put("hwpdu", new HashMap<>());
		}
		if(proj.getHwzpdu()!=null && !"".equals(proj.getHwzpdu())) {
			List<String> list=new ArrayList<>();
			String[] hwzpdus=proj.getHwzpdu().split(",");
			for(String hwzpdu:hwzpdus) {
				list.add(hwzpdu);
			}
			map.put("hwzpdu", list);
		}else {
			map.put("hwzpdu", new HashMap<>());
		}
		if(proj.getPduSpdt()!=null && !"".equals(proj.getPduSpdt())) {
			List<String> list=new ArrayList<>();
			String[] pduSpdts=proj.getPduSpdt().split(",");
			for(String pduSpdt:pduSpdts) {
				list.add(pduSpdt);
			}
			map.put("pduSpdt", list);
		}else {
			map.put("pduSpdt", new HashMap<>());
		}
		if(proj.getBu()!=null && !"".equals(proj.getBu())) {
			List<String> list=new ArrayList<>();
			String[] bus=proj.getBu().split(",");
			for(String bu:bus) {
				list.add(bu);
			}
			map.put("bu", list);
		}else {
			map.put("bu", new HashMap<>());
		}
		if(proj.getPdu()!=null && !"".equals(proj.getPdu())) {
			List<String> list=new ArrayList<>();
			String[] pdus=proj.getPdu().split(",");
			for(String pdu:pdus) {
				list.add(pdu);
			}
			map.put("pdu", list);
		}else {
			map.put("pdu", new HashMap<>());
		}
		if(proj.getDu()!=null && !"".equals(proj.getDu())) {
			List<String> list=new ArrayList<>();
			String[] dus=proj.getDu().split(",");
			for(String du:dus) {
				list.add(du);
			}
			map.put("du", list);
		}else {
			map.put("du", new HashMap<>());
		}
		if(proj.getProjectState()!=null && !"".equals(proj.getProjectState())) {
			List<String> list=new ArrayList<>();
			String[] projectStates=proj.getProjectState().split(",");
			for(String projectState:projectStates) {
				list.add(projectState);
			}
			map.put("projectState", list);
		}else {
			map.put("projectState", new HashMap<>());
		}
		return map;
	}
	
//表格加载备份
//	public Map<String, Object> getProjDetail(String buName, final Page page, ProjProductivity proj) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		List<Map<String, String>> titles = new LinkedList<Map<String, String>>();
//		Map<String, String> units = new TreeMap<String, String>();
//		Map<String, String> title = new HashMap<String, String>();
//		List<MeasureInfo> paramGridTitle = buOverviewDao.getGridTitles();
//		loadExtraTitle(paramGridTitle, titles, title, units);
//		List<MeasureInfo> gridDatas = buOverviewDao.getProjDetail(buName, page.getSort(), page.getOrder(), proj);
//		result.put("gridTitles", titles);
//		result.put("titleUnit", units);
//		List<Map<String, String>> gridDataRecords = new LinkedList<Map<String, String>>();
//		Map<String, Object> tmpMap = new HashMap<String, Object>();
//		for (MeasureInfo gridData : gridDatas) {
//			Map<String, String> record = new HashMap<String, String>();
//			if (StringUtils.isEmpty(gridData.getMonth())) {
//				for (MeasureInfo paramTitle : paramGridTitle) {
//					record.put(paramTitle.getName(), "");
//				}
//				record.put("month", "");
//				record.put("产品部", gridData.getPdu());
//				record.put("地域", gridData.getArea());
//				record.put("计费类型", gridData.getType());
//				record.put("项目编码", gridData.getNo());
//				record.put("项目名称", gridData.getProjectName());
//				record.put("项目经理", gridData.getPm());
//				gridDataRecords.add(record);
//			} else {
//				if (tmpMap.get(gridData.getMonth() + gridData.getNo()) == null) {
//					record.put(gridData.getName(), gridData.getValue());
//					record.put("month", gridData.getMonth());
//					record.put("产品部", gridData.getPdu());
//					record.put("地域", gridData.getArea());
//					record.put("计费类型", gridData.getType());
//					record.put("项目编码", gridData.getNo());
//					record.put("项目名称", gridData.getProjectName());
//					record.put("项目经理", gridData.getPm());
//					tmpMap.put(gridData.getMonth() + gridData.getNo(), record);
//				} else {
//					record = (Map<String, String>) tmpMap.get(gridData.getMonth() + gridData.getNo());
//					gridDataRecords.remove((Map<String, String>) tmpMap.get(gridData.getMonth() + gridData.getNo()));
//					record.put(gridData.getName(), gridData.getValue());
//				}
//				gridDataRecords.add(record);
//			}
//		}
//		if (StringUtils.isNotEmpty(page.getSort())) {
//			
//			// gridDataRecords.sort(new Comparator<Map<String, String>>() {
//			Collections.sort(gridDataRecords, new Comparator<Map<String, String>>() {
//				
//				@Override
//				public int compare(Map<String, String> arg0, Map<String, String> arg1) {
//					String sort = page.getSort();
//					String val0 = arg0.get(sort);
//					String val1 = arg1.get(sort);
//					String order = page.getOrder();
//					Set<String> set = new HashSet<String>(Arrays.asList(HEADERS));
//					set.add("month");
//					if (!set.contains(sort)) {
//						double dVal0 = StringUtils.isEmpty(val0) ? 0 : Double.parseDouble(val0);
//						double dVal1 = StringUtils.isEmpty(val1) ? 0 : Double.parseDouble(val1);
//						if ("asc".equals(order)) {
//							return compareDouble(dVal0, dVal1);
//						} else {
//							return compareDouble(dVal1, dVal0);
//						}
//					} else {
//						val1 = null == val1 ? "" : val1;
//						val0 = null == val0 ? "" : val0;
//						if ("asc".equals(order)) {
//							return val0.compareTo(val1);
//						} else {
//							return val1.compareTo(val0);
//						}
//					}
//					
//				}
//				
//				private int compareDouble(double dVal1, double dVal0) {
//					if (dVal1 > dVal0) {
//						return 1;
//					} else if (dVal1 < dVal0) {
//						return -1;
//					}
//					return 0;
//				}
//			});
//		}
//		Map<String, Object> tableRslts = new HashMap<String, Object>();
//		tableRslts.put("rows", gridDataRecords);
//		tableRslts.put("total", gridDataRecords.size());
//		result.put("gridDatas", tableRslts);
//		return result;
//	}
//	
//	private void loadExtraTitle(List<MeasureInfo> gridTitleUnits, List<Map<String, String>> titles,
//			Map<String, String> title, Map<String, String> units) {
//		MeasureInfo titleInfo = null;
//		
//		title.put("field", "month");
//		title.put("title", "统计月份");
//		titles.add(title);
//		for (String header : HEADERS) {
//			titleInfo = new MeasureInfo();
//			titleInfo.setName(header);
//			title = new HashMap<String, String>();
//			title.put("field", header);
//			title.put("title", header);
//			titles.add(title);
//		}
//		for (MeasureInfo gridTitleUnit : gridTitleUnits) {
//			title = new HashMap<String, String>();
//			title.put("field", gridTitleUnit.getName());
//			title.put("title", gridTitleUnit.getName());
//			titles.add(title);
//			units.put(gridTitleUnit.getName(), gridTitleUnit.getUnit());
//		}
//	}
	
	public byte[] exportExcel(ProjectInfo proj) {
		Map<String, Object> gridDatas = getProjDetail(proj);
//		Map<String, Object> gridDatas = getProjDetail(buName, new Page(), proj);
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		Row row = sheet.createRow(0);
		Cell cell;
		List<Map<String, String>> titles = (List<Map<String, String>>) gridDatas.get("gridTitles");
		for (int i = 0; i < titles.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(titles.get(i).get("title"));
		}
		Map<String, Object> tableRslts = (Map<String, Object>) gridDatas.get("gridDatas");
		List<Map<String, String>> gridDataRecords = (List<Map<String, String>>) (tableRslts.get("rows"));
		for (int i = 0; i < gridDataRecords.size(); i++) {
			row = sheet.createRow(i + 1);
			for (int j = 0; j < titles.size(); j++) {
				cell = row.createCell(j);
				cell.setCellValue(gridDataRecords.get(i).get(titles.get(j).get("field")));
			}
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			wb.write(os);
		} catch (IOException e) {
			LOG.error("export excel failed!", e);
		}
		return os.toByteArray();
	}

	public Map<String, Object> getProjIndicators(ProjProductivity proj) {
		String buName = proj.getBu();
		List<ProjProductivity> wtdsList = buOverviewDao.getProjIndicator(buName, proj.getWtdhgbtggs());
		List<ProjProductivity> lltList = buOverviewDao.getProjIndicator(buName, proj.getBbzcssbcs());
		List<ProjProductivity> ddqxsList = buOverviewDao.getProjIndicator(buName, proj.getWswts());
		List<ProjProductivity> xmdmlList = buOverviewDao.getProjIndicator(buName, proj.getDml());
		List<ProjProductivity> zxylsList = buOverviewDao.getProjIndicator(buName, proj.getSdylzxxl());
		List<ProjProductivity> ddqxxgxlList = buOverviewDao.getProjIndicator(buName, proj.getDdckwtjjl());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("wtds", wtdsList);
		result.put("llt", lltList);
		result.put("ddqxs", ddqxsList);
		result.put("xmdml", xmdmlList);
		result.put("zxyls", zxylsList);
		result.put("ddqxxgxl", ddqxxgxlList);
		return result;
	}

	public List<ProjProductivity> getSpecProjChartData(String no) {
		return buOverviewDao.getSpecProjChartData(no);
	}

	public Map<String, Map<String, Map<String, List<ProjectInfo>>>> getProjCategory() {
		List<OrganizeMgmer> bus = buOverviewDao.getBuOpts();
		Map<String, Map<String, Map<String, List<ProjectInfo>>>> result = new HashMap<String, Map<String, Map<String, List<ProjectInfo>>>>();
		for (OrganizeMgmer bu : bus) {
			List<String> pus = buOverviewDao.getPus(bu.getName());
			Map<String, Map<String, List<ProjectInfo>>> map = new HashMap<String, Map<String, List<ProjectInfo>>>();
			for (String pu : pus) {
				List<ProjectInfo> puList = buOverviewDao.getProjCategory(bu.getName(), pu);
				Map<String, List<ProjectInfo>> monMap = new HashMap<String, List<ProjectInfo>>();
				for (ProjectInfo info : puList) {
					List<ProjectInfo> monList = new LinkedList<ProjectInfo>();
					if (null == info.getMonth() || StringUtils.isEmpty(info.getMonth())) {
						info.setMonth(DateUtils.getMonth());
					}
					if (monMap.containsKey(info.getMonth())) {
						monList = monMap.get(info.getMonth());
					}
					monList.add(info);
					monMap.put(info.getMonth(), monList);
				}
				map.put(pu, monMap);

			}
			result.put(bu.getName(), map);
		}

		return result;
	}

	/**
	 * 获取展示页面各表格中的表头及数据
	 * 
	 * @param no
	 * @param bigType
	 * @param smallType
	 * @return
	 */
	public Map<String, Object> getMeasureResult(String no, String bigType, String smallType) {
		List<MeasureInfo> measureRslts = buOverviewDao.getMeasureResult(no, bigType, smallType);
		Map<String, Object> result = new HashMap<String, Object>();
		Set<String> paramNames = new TreeSet<String>();
		Set<String> monthSet = new TreeSet<String>();
		Map<String, String> units = new TreeMap<String, String>();
		List<Map<String, String>> titles = new LinkedList<Map<String, String>>();
		Map<String, String> title = new HashMap<String, String>();
		title.put("field", "month");
		title.put("title", "统计月份");
		titles.add(title);
		for (MeasureInfo measureInfo : measureRslts) {
			if (StringUtils.isNotEmpty(measureInfo.getName())) {
				paramNames.add(measureInfo.getName());
				if (!units.containsKey(measureInfo.getName())) {
					title = new HashMap<String, String>();
					title.put("field", measureInfo.getName());
					title.put("title", measureInfo.getName());
					titles.add(title);
					units.put(measureInfo.getName(), measureInfo.getUnit());
				}
			}
			if (StringUtils.isNotEmpty(measureInfo.getMonth())) {
				monthSet.add(measureInfo.getMonth());
			}
		}
		result.put("title", titles);
		result.put("units", units);
		List<Map<String, String>> monthRecords = generateDatasForGrid(measureRslts, paramNames, monthSet);
		Map<String, Object> tableRslts = new HashMap<String, Object>();
		tableRslts.put("rows", monthRecords);
		tableRslts.put("total", monthRecords.size());
		result.put("gridDatas", tableRslts);
		return result;
	}

	/**
	 * 根据数据库中查出的列表组装datagrid需要的数据格式
	 * 
	 * @param measureRslts
	 * @param paramNames
	 * @param monthSet
	 * @return
	 */
	private List<Map<String, String>> generateDatasForGrid(List<MeasureInfo> measureRslts, Set<String> paramNames,
			Set<String> monthSet) {
		List<Map<String, String>> monthRecords = new LinkedList<Map<String, String>>();
		for (String mon : monthSet) {
			Map<String, String> record = new HashMap<String, String>();
			for (String name : paramNames) {
				for (MeasureInfo measureRslt : measureRslts) {
					if (mon.equals(measureRslt.getMonth()) && name.equals(measureRslt.getName())) {
						record.put(name, measureRslt.getValue());
					}
				}
				if (null == record.get(name)) {
					record.put(name, "");
				}
			}
			record.put("month", mon);
			monthRecords.add(record);
		}
		return monthRecords;
	}

	public Map<String, Object> getProjOverveiwData(String no) {
		Map<String, Object> result = new HashMap<String, Object>();
		ProjectInfo projectInfo = projectListDao.getProjInfoByNo(no);
		result.put("no", projectInfo.getNo());
		result.put("name", projectInfo.getName());
		result.put("bu", projectInfo.getBu());
		result.put("pdu", projectInfo.getPdu());
		result.put("du", projectInfo.getDu());
		result.put("area", projectInfo.getArea());
		result.put("type", projectInfo.getType());
		result.put("startDate", projectInfo.getStartDate());
		result.put("endDate", projectInfo.getEndDate());
		result.put("projectType", projectInfo.getProjectType());
		result.put("import_date", projectInfo.getImportDate());
		result.put("pm", projectInfo.getPm());
		result.put("qa", projectInfo.getQa());
		result.put("hwpdu", projectInfo.getHwpdu());
		result.put("hwzpdu", projectInfo.getHwzpdu());
		result.put("pduSpdt", projectInfo.getPduSpdt());
		result.put("projectSynopsis", projectInfo.getProjectSynopsis());
		result.put("projectState", projectInfo.getProjectState());
		result.put("coopType", projectInfo.getCoopType());
		result.put("isOffshore", projectInfo.getIsOffshore());

		return result;
	}

	public Map<String, Object> getProjOverveiw(String no) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<MeasureInfo> gridTitleUnits = buOverviewDao.getGridTitles();
		List<MeasureInfo> gridDatas = buOverviewDao.getProjOverview(no);
		Map<String, String> units = new TreeMap<String, String>();
		List<Map<String, String>> titles = new LinkedList<Map<String, String>>();
		Map<String, String> title = new HashMap<String, String>();
		Set<String> paramNames = new TreeSet<String>();
		Set<String> monthSet = new TreeSet<String>();

		title.put("field", "month");
		title.put("title", "统计月份");
		titles.add(title);
		for (MeasureInfo gridTitleUnit : gridTitleUnits) {
			title = new HashMap<String, String>();
			title.put("field", gridTitleUnit.getName());
			title.put("title", gridTitleUnit.getName());
			titles.add(title);
			units.put(gridTitleUnit.getName(), gridTitleUnit.getUnit());
			paramNames.add(gridTitleUnit.getName());
		}
		for (MeasureInfo gridData : gridDatas) {
			if (null != gridData && StringUtils.isNotEmpty(gridData.getMonth())) {
				monthSet.add(gridData.getMonth());
			}
		}
		result.put("title", titles);
		result.put("units", units);
		List<Map<String, String>> monthRecords = generateDatasForGrid(gridDatas, paramNames, monthSet);
		Map<String, Object> tableRslts = new HashMap<String, Object>();
		tableRslts.put("rows", monthRecords);
		tableRslts.put("total", monthRecords.size());
		result.put("gridDatas", tableRslts);
		return result;
	}

	public List<ProjectKeyroles> getProjectKeyrolesNo(String no) {
		return projectKeyrolesDao.queryProjectKeyrolesNo(no);
	}

	public Map<String, Object> insertInfo(List<ProjectKeyroles> projectKeyroles) {
		Map<String, Object> res= new HashMap<>();
		if(projectKeyroles!=null && projectKeyroles.get(0)!=null) {
			projectKeyrolesDao.batchDeleteByNo(projectKeyroles.get(0).getNo());
		}
		int num = projectKeyrolesDao.insertInfos(projectKeyroles);
		if(num > 0) {
			res.put("res", "successful");
		}else {
			res.put("res", "fail");
		}
		return res;
	}

	public List<ProjectSchedule> getProjectScheduleNo(String no) {
		return projectScheduleDao.queryProjectScheduleNo(no);
	}

	public Map<String, Object> insertInfoProjectSchedule(List<ProjectSchedule> projectSchedules) {
		Map<String, Object> res= new HashMap<>();
		if(projectSchedules==null) {
			res.put("res", "fail");
			return res;
		}
		if(projectSchedules.size()>0) {
			projectScheduleDao.batchDeleteByNo(projectSchedules.get(0).getNo());
		}
		int num = projectScheduleDao.insertInfos(projectSchedules);
		if(num > 0) {
			res.put("res", "successful");
		}else {
			res.put("res", "fail");
		}
		return res;
	}

	
	public List<String> getAreas() {
		return buOverviewDao.getAreas();
	}

	public List<Map<String, Object>> getZhongruanYWX() {
		return buOverviewDao.getZhongruanYWX();
	}

	public List<Map<String, Object>> getZhongruanSYB(String ywxval) {
		Map<String, Object> map=new HashMap<>();
		if (ywxval == null || "".equals(ywxval)) {
			return new ArrayList<>();
		} else {
			List<String> valueList = new ArrayList<String>();
			String[] ywxvals = ywxval.split(",");
			for (String ywx : ywxvals) {
				valueList.add(ywx);
			}
			map.put("ywxval", valueList);
		}
		return buOverviewDao.getZhongruanSYB(map);
	};

	public List<Map<String, Object>> getZhongruanJFB(String sybval) {
		Map<String, Object> map=new HashMap<>();
		if (sybval == null || "".equals(sybval)) {
			return new ArrayList<>();
		} else {
			List<String> valueList = new ArrayList<String>();
			String[] sybvals = sybval.split(",");
			for (String syb : sybvals) {
				valueList.add(syb);
			}
			map.put("sybval", valueList);
		}
		return buOverviewDao.getZhongruanJFB(map);
	};

	public List<Map<String, Object>> getHWCPX() {
		return buOverviewDao.getHWCPX();
	}

	public List<Map<String, Object>> getZCPX(String hwcpxval) {
		Map<String, Object> map=new HashMap<>();
		if (hwcpxval == null || "".equals(hwcpxval)) {
			return new ArrayList<>();
		} else {
			List<String> valueList = new ArrayList<String>();
			String[] hwcpxvals = hwcpxval.split(",");
			for (String hwcpx : hwcpxvals) {
				valueList.add(hwcpx);
			}
			map.put("hwcpxval", valueList);
		}
		return buOverviewDao.getZCPX(map);
	};

	public List<Map<String, Object>> getPDUorSPDT(String zcpxval) {
		Map<String, Object> map=new HashMap<>();
		if (zcpxval == null || "".equals(zcpxval)) {
			return new ArrayList<>();
		} else {
			List<String> valueList = new ArrayList<String>();
			String[] zcpxvals = zcpxval.split(",");
			for (String zcpx : zcpxvals) {
				valueList.add(zcpx);
			}
			map.put("zcpxval", valueList);
		}
		return buOverviewDao.getPDUorSPDT(map);
	};
	
	public Map<String, Object> getProjectNum(){
		return buOverviewDao.getProjectNum();
	}

}
