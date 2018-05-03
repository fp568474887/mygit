package com.icss.mvp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.dao.ICodeMasterInfoDao;
import com.icss.mvp.dao.IOrgnizeInfoDao;
import com.icss.mvp.dao.IParameterInfo;
import com.icss.mvp.dao.IProjectKeyrolesDao;
import com.icss.mvp.dao.IProjectListDao;
import com.icss.mvp.dao.IProjectParameter;
import com.icss.mvp.entity.CodeMasterInfo;
import com.icss.mvp.entity.OrganizeInfo;
import com.icss.mvp.entity.Page;
import com.icss.mvp.entity.ParameterInfo;
import com.icss.mvp.entity.ProjectClock;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectKeyroles;
import com.icss.mvp.entity.ProjectManager;
import com.icss.mvp.entity.ProjectMember;
import com.icss.mvp.entity.ProjectParameter;

@Service("projectListService")
@Transactional
public class ProjectListService {
	@Resource
	private IProjectListDao projectListDao;
	@Resource
	private IProjectParameter projParam;
	@Resource
	private IOrgnizeInfoDao orgInfoDao;
	@Resource
	private IProjectKeyrolesDao projectKeyrolesDao;
	@Resource
	private IProjectParameter projectParameter;
	@Resource
	private IParameterInfo parameterInfo;
	@Resource
	private ICodeMasterInfoDao codeMasterInfo;

//	private final static String[] HEADERS = { "项目名称", "项目编号", "事业部", "交付部","产品",
//			"地域", "项目经理", "项目类型", "项目开始日期", "项目结束日期", "项目导入日期", "状态" };
	private final static String[] HEADERS = { "项目名称", "团队名称", "地域", "华为产品线","子产品线",
			"PDU/SPDT", "业务线", "事业部", "交付部", "运营编码", "外部PO号", "外部PO名","项目经理",
			"项目经理工号", "项目QA", "项目QA工号", "是否上网项目", "华为PM/接口人", "外包代表", "当前月项目总人力",
			"运营商务模式", "项目类型", "合作类型", "是否离岸", "业务分类", "业务类型", "项目简介",
			"项目计划开始时间","项目计划结束时间", "项目状态", "现团队类型", "推进后团队类型", "计划完成转化月份",
			"团队现商务模式","团队推进后商务模式","商务模式计划完成转化月份","不能FP的原因","离岸人数","华为在场支撑人数",
			"不能离场关键原因"};
	private final static Logger LOG = Logger
			.getLogger(ProjectListService.class);

	public Map<String, Object> getList(ProjectInfo proj, Page page) {
		List<ProjectInfo> projList = projectListDao.getList(proj,
				page.getSort(), page.getOrder());
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", projList);
		result.put("total", projList.size());
		return result;
	}

	public byte[] exportExcel(ProjectInfo proj) {
		List<ProjectInfo> projList = projectListDao.getList(proj,
				"", "");
		proj = new ProjectInfo();
		InputStream is = this.getClass().getResourceAsStream(
				"/project-info-template.xlsx");
		Workbook wb;;
		try {
			wb = new XSSFWorkbook(is);
		} catch (IOException e) {
			LOG.error("read file failed!", e);
			return null;
		}
		Sheet sheet = wb.getSheetAt(0);
		Row row;
		Cell cell;
//		CellStyle cellStyle = wb.createCellStyle();
//		CreationHelper creationHelper = wb.getCreationHelper();
//		cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(
//				"yyyy-MM-dd"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		for (int i = 0; i < HEADERS.length; i++) {
//			cell = row.createCell(i);
//			cell.setCellValue(HEADERS[i]);
//		}
		for (int i = 0; i < projList.size(); i++) {
			proj = projList.get(i);
			row = sheet.createRow(i + 1);
			cell = row.createCell(0);
			cell.setCellValue(proj.getName());
			cell = row.createCell(1);
			cell.setCellValue("");
			cell = row.createCell(2);
			cell.setCellValue(proj.getArea());
			cell = row.createCell(3);
			cell.setCellValue(proj.getHwpdu());
			cell = row.createCell(4);
			cell.setCellValue(proj.getHwzpdu());
			cell = row.createCell(5);
			cell.setCellValue(proj.getPduSpdt());
			cell = row.createCell(6);
			cell.setCellValue(proj.getBu());
			cell = row.createCell(7);
			cell.setCellValue(proj.getPdu());
			cell = row.createCell(8);
			cell.setCellValue(proj.getDu());
			cell = row.createCell(9);
			cell.setCellValue(proj.getNo());
			cell = row.createCell(10);
			cell.setCellValue("");
			cell = row.createCell(11);
			cell.setCellValue("");
			String pm = proj.getPm();
			if(null != pm && !"".equals(pm)) {
				String[] pms = pm.split(" ");
				if(pms.length >= 2) {
					cell = row.createCell(12);
					cell.setCellValue(pms[0]);
					cell = row.createCell(13);
					cell.setCellValue(pms[1]);
				}else {
					cell = row.createCell(12);
					cell.setCellValue("");
					cell = row.createCell(13);
					cell.setCellValue("");
				}
			}else {
				cell = row.createCell(12);
				cell.setCellValue("");
				cell = row.createCell(13);
				cell.setCellValue("");
			}
			cell = row.createCell(14);
			cell.setCellValue("");
			cell = row.createCell(15);
			cell.setCellValue("");
			cell = row.createCell(16);
			cell.setCellValue("");
			cell = row.createCell(17);
			cell.setCellValue("");
			cell = row.createCell(18);
			cell.setCellValue("");
			cell = row.createCell(19);
			cell.setCellValue("");
			cell = row.createCell(20);
			cell.setCellValue(proj.getType());
			cell = row.createCell(21);
			cell.setCellValue(proj.getProjectType());
			cell = row.createCell(22);
			cell.setCellValue(proj.getCoopType());
			cell = row.createCell(23);
			cell.setCellValue(proj.getIsOffshore());
			cell = row.createCell(24);
			cell.setCellValue("");
			cell = row.createCell(25);
			cell.setCellValue("");
			cell = row.createCell(26);
			cell.setCellValue(proj.getProjectSynopsis());
			cell = row.createCell(27);
			Date date;
			if (null != proj.getStartDate()) {
				date = proj.getStartDate();
				cell.setCellValue(df.format(date));
			} else {
				cell.setCellValue("");
			}
			cell = row.createCell(28);
			if (null != proj.getEndDate()) {
				date = proj.getStartDate();
				cell.setCellValue(df.format(date));
			} else {
				cell.setCellValue("");
			}
			cell = row.createCell(29);
			cell.setCellValue(proj.getProjectState());
			cell = row.createCell(30);
			cell.setCellValue("");
			cell = row.createCell(31);
			cell.setCellValue("");
			cell = row.createCell(32);
			cell.setCellValue("");
			cell = row.createCell(33);
			cell.setCellValue(proj.getType());
			cell = row.createCell(34);
			cell.setCellValue(proj.getType());
			cell = row.createCell(35);
			cell.setCellValue("");
			cell = row.createCell(36);
			cell.setCellValue("");
			cell = row.createCell(37);
			cell.setCellValue("");
			cell = row.createCell(38);
			cell.setCellValue("");
			cell = row.createCell(39);
			cell.setCellValue("");
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			wb.write(os);
		} catch (IOException e) {
			LOG.error("export excel failed!", e);
		}
		return os.toByteArray();
	}

	public ProjectInfo getProjInfo(String buName, String projNo) {
		return projectListDao.getProjInfo(buName, projNo);
	}

	public Map<String, Object> getProjectSelectInfo() {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("businessUnit", projectListDao.getBusinessUnit());
		result.put("deliveryUnit", projectListDao.getDeliveryUnit());
		result.put("countArea", projectListDao.getCountArea());
		result.put("projectType", projectListDao.getProjectType());
		result.put("projectManager", projectListDao.getProjectManager());
		return result;
	}

	public Map<String,String> insertInfo(ProjectDetailInfo proj, String param,
			ProjectManager projManger, Date imptDate) {
		Map<String,String> result=new HashMap<String,String>();
		String message;
		if (param.equals("add")) {
			int projInfoRow = projectListDao.insertInfo(proj);
			int projParamRow = insertProjParam(proj, imptDate);
			int memberRow = projectListDao.insertMember(projManger);
			if (projInfoRow > 0 && projParamRow > 0 && memberRow > 0) {
				message = "保存成功";
			} else {
				message = "保存失败";
			}

		} else {
			int projInfoRow = projectListDao.updateInfo(proj);
			int memberRow = projectListDao.updateMemberInfo(projManger);
			if (projInfoRow > 0 && memberRow > 0) {
				message = "保存成功";
			} else {
				message = "保存失败";
			}
		}
		result.put("param",param);
		result.put("message",message);
		return result;

	}

	public Map<String, Object> queryName(String projNo) {
		ProjectManager proManager = null;
		try {
			proManager = projectListDao.isExitMember(projNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", proManager);
		return result;
	}

	public int insertProjParam(ProjectDetailInfo proj, Date imptDate) {
		List<ParameterInfo> paramInfos = parameterInfo.queryParameterInfo();
		List<Object> listInfo = new ArrayList<Object>();
		for (ParameterInfo paramInfo : paramInfos) {
			Map<String, Object> mapInfo = new HashMap<String, Object>();
			mapInfo.put("no", proj.getNo());
			mapInfo.put("id", paramInfo.getId());
			mapInfo.put("unit", paramInfo.getUnit());
			mapInfo.put("source_value", paramInfo.getSource_value());
			mapInfo.put("parameter", paramInfo.getParameter());
			mapInfo.put("isDisplay", 1);
			mapInfo.put("create_date", imptDate);
			mapInfo.put("creator", "admin");
			mapInfo.put("update_date", imptDate);
			mapInfo.put("update_user", "admin");
			listInfo.add(mapInfo);
		}
		return projectParameter.insertParamInfo(listInfo);
	}

	public Map<String, Object> importProjects(MultipartFile file) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<ProjectDetailInfo> projInfoLists = new ArrayList<ProjectDetailInfo>();
		List<ProjectManager> projMemberInfo = new ArrayList<ProjectManager>();
		InputStream is;
		Workbook workbook=null;
		String err="";
		String sess="";
		  try {
			 is = file.getInputStream();
			 workbook = new XSSFWorkbook(is);
			} catch (IOException e) {
				LOG.error("read file failed!", e);
				result.put("STATUS", "FAILED");
				result.put("MESSAGE", "读取文件失败！");
				return result;
			}
			Sheet sheet = workbook.getSheetAt(0);
			int rowSize = sheet.getLastRowNum();
			  Row row = null;
			  if(rowSize<1){
				  result.put("STATUS", "FAILED");
				  result.put("MESSAGE", "没有要导入的数据！");
				  return result; 
			  }
				for (int i = 1; i <= rowSize; i++) {
					row = sheet.getRow(i);
					ProjectDetailInfo projInfo = new ProjectDetailInfo();
					String cell01Value =getCellFormatValue(row.getCell(0));
					String no =getCellFormatValue(row.getCell(9));
					if(cell01Value==null || no==null){
						LOG.error("项目编号不能为空");
						continue;
					}
				
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					projInfo.setName(cell01Value);
					projInfo.setNo(no);
					projInfo.setProjectType(isProjectType(getCellFormatValue(row.getCell(21))));
					projInfo.setProjectSynopsis(getCellFormatValue(row.getCell(26)));
					projInfo.setProjectState(getCellFormatValue(row.getCell(29)));
					projInfo.setCoopType(getCellFormatValue(row.getCell(22)));
					projInfo.setIsOffshore(getCellFormatValue(row.getCell(23)));
					projInfo.setType(isPayType(getCellFormatValue(row.getCell(20))));
					projInfo.setHwpdu(getCellFormatValue(row.getCell(3)));
					projInfo.setHwzpdu(getCellFormatValue(row.getCell(4)));
					projInfo.setPduSpdt(getCellFormatValue(row.getCell(5)));
					projInfo.setBu(getCellFormatValue(row.getCell(6)));
					projInfo.setPdu(getCellFormatValue(row.getCell(7)));
					projInfo.setDu(isPayDepart(getCellFormatValue(row.getCell(8))));
					projInfo.setArea(isArea(getCellFormatValue(row.getCell(2))));
					projInfo.setStartDate(CovertDate(row.getCell(27)));
					projInfo.setEndDate(CovertDate(row.getCell(28)));
					String date = df.format(new Date());
					Date imptDate=null;
					try {
						imptDate = df.parse(date);
					} catch (ParseException e) {
						LOG.error("时间转换异常",e);
					}
					projInfo.setImportDate(imptDate);
					projInfo.setImportUser("admin");
					projectListDao.replaceInfo(projInfo);
//					if (NameAndNo(projInfo.getName(),projInfo.getNo())==null
//			            &&("不存在项目").equals(judgeProjInfo(projInfo.getNo()))
//					     ){  
						try {
							insertProjParam(projInfo,imptDate);
						} catch (Exception e) {
							LOG.error(projInfo.getName()+"导入项目已存在",e);
						}
//						projInfoLists.add(projInfo);
						ProjectManager projMember = new ProjectManager();
						projMember.setNo(no);
						String pmName = getCellFormatValue(row.getCell(12))+" "+getCellFormatValue(row.getCell(13));;
						String pmNo = getCellFormatValue(row.getCell(13));;
						projMember.setName(pmName);
						projMember.setPosition("PM");
						if(pmNo==null){
						projMember.setAccount("pm");	
						}else{
						 projMember.setAccount(pmName);
						}
						projMember.setStartDate(projInfo.getStartDate());
						projMember.setEndDate(projInfo.getEndDate());
						projMember.setImportDate(imptDate);
//							projMemberInfo.add(projMember);
						projectListDao.replaceMember(projMember);
						LOG.info(projInfo.getName()+"导入成功");
						sess += projInfo.getName()+"\n";
//					}else {
//						LOG.error(projInfo.getName()+"导入项目已存在");
//						err += projInfo.getName()+"\n";
//					}
				}
//			int projInfoRow = 0;
//			if (projInfoLists != null && projInfoLists.size() != 0) {
//				projInfoRow = projectListDao.insertProjInfos(projInfoLists);
//			}
//			if (projMemberInfo != null && projMemberInfo.size() != 0) {
//				int projMemberRow = projectListDao
//						.insertProjMembers(projMemberInfo);
//			}
			result.put("STATUS", "SUCCESS");
			result.put("err", err);
			result.put("sess", sess);
		   return result;
	}
	
	public Map<String, Object> importPromember(MultipartFile file) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<ProjectMember> projMember = new ArrayList<ProjectMember>();
		InputStream is;
		Workbook workbook=null;
		String err="";
		String sess="";
		  try {
			 is = file.getInputStream();
			 workbook = new XSSFWorkbook(is);
			} catch (IOException e) {
				LOG.error("read file failed!", e);
				result.put("STATUS", "FAILED");
				result.put("MESSAGE", "读取文件失败！");
				return result;
			}
			Sheet sheet = workbook.getSheetAt(0);
			int rowSize = sheet.getLastRowNum();
			  Row row = null;
			  if(rowSize<1){
				  result.put("STATUS", "FAILED");
				  result.put("MESSAGE", "没有要导入的数据！");
				  return result; 
			  }
				for (int i = 1; i <= rowSize; i++) {
					row = sheet.getRow(i);
//					ProjectDetailInfo projInfo = new ProjectDetailInfo();
					ProjectMember promember = new ProjectMember();
					String cell01Value =getCellFormatValue(row.getCell(0));
					String no =getCellFormatValue(row.getCell(2));
					if(cell01Value==null || no==null || rowSize==0){
						result.put("STATUS", "FAILED");
						result.put("MESSAGE", "没有要导入的数据！");
					    return result;
					}
				
					promember.setName(cell01Value);
					promember.setNo(no);
					promember.setPo(getCellFormatValue(row.getCell(1)));
					promember.setCompany(getCellFormatValue(row.getCell(3)));
					promember.setType(getCellFormatValue(row.getCell(4)));
					promember.setProline(getCellFormatValue(row.getCell(5)));
					promember.setSubproline(getCellFormatValue(row.getCell(6)));
					promember.setPdu(getCellFormatValue(row.getCell(7)));
					promember.setArea(getCellFormatValue(row.getCell(9)));
					promember.setMode(getCellFormatValue(row.getCell(10)));
					promember.setStatus(getCellFormatValue(row.getCell(11)));
					promember.setAuthor(getCellFormatValue(row.getCell(12)));
					promember.setRole(getCellFormatValue(row.getCell(13)));
					promember.setSkill(getCellFormatValue(row.getCell(14)));
					promember.setTeam(getCellFormatValue(row.getCell(15)));
					promember.setUpdatetime(CovertDate(row.getCell(16)));
					if ("已存在项目".equals(judgeProjInfo(promember.getNo())) 
							&& promember.getAuthor() != null) { 
						if ("人员信息不存在".equals(Exitmember(promember.getNo(), promember.getAuthor()))) {
							
							projectListDao.insertmemberInfo(promember);
						}else {
							LOG.info("该项目人员信息已存在,请勿重复导入！");
						}			
					}else {
						LOG.info("查无此项目,请确认！");
					}		
				}
			result.put("STATUS", "SUCCESS");
			result.put("err", err);
			result.put("sess", sess);
		   return result;
	}
	
	public Map<String, Object> importClock(MultipartFile file) {
		Map<String, Object> result = new HashMap<String, Object>();
		InputStream is;
		Workbook workbook=null;
		String err="";
		String sess="";
		  try {
			 is = file.getInputStream();
			 workbook = new XSSFWorkbook(is);
			} catch (IOException e) {
				LOG.error("read file failed!", e);
				result.put("STATUS", "FAILED");
				result.put("MESSAGE", "读取文件失败！");
				return result;
			}
			Sheet sheet = workbook.getSheetAt(0);
			int rowSize = sheet.getLastRowNum();
			  Row row = null;
			  if(rowSize<1){
				  result.put("STATUS", "FAILED");
				  result.put("MESSAGE", "没有要导入的数据！");
				  return result; 
			  }
				for (int i = 1; i <= rowSize; i++) {
					row = sheet.getRow(i);
//					ProjectDetailInfo projInfo = new ProjectDetailInfo();
//					ProjectMember promember = new ProjectMember();
					ProjectClock proclock = new ProjectClock();
					String author =getCellFormatValue(row.getCell(5));
					String idnumber =getCellFormatValue(row.getCell(10));
					if(author==null ||idnumber==null || rowSize==0){
						result.put("STATUS", "FAILED");
						result.put("MESSAGE", "没有要导入的数据！");
					    return result;
					}
				
					proclock.setCompany(getCellFormatValue(row.getCell(0)));
					proclock.setArea(getCellFormatValue(row.getCell(1)));
					proclock.setProline(getCellFormatValue(row.getCell(2)));
					proclock.setSubproline(getCellFormatValue(row.getCell(3)));
					proclock.setPdu(getCellFormatValue(row.getCell(4)));
					proclock.setAuthor(author);
					proclock.setName(getCellFormatValue(row.getCell(6)));
					proclock.setWorktime(CovertDate(row.getCell(7)));
					proclock.setOfftime(CovertDate(row.getCell(8)));
					proclock.setType(getCellFormatValue(row.getCell(9)));
					proclock.setIdnumber(idnumber);
					proclock.setPo(getCellFormatValue(row.getCell(11)));
					projectListDao.inserClockInfo(proclock);
					
				}
			result.put("STATUS", "SUCCESS");
			result.put("err", err);
			result.put("sess", sess);
		   return result;
	}
	public String NameAndNo(String name,String no){
		 String message=null;
		  if(name==null || no==null){
			 message="不能为空";
		 }
		  return message;
	}
	
	
	public String getBU(String bu) {
		List<OrganizeInfo> orgInfos = orgInfoDao.getBU(bu);
		if (orgInfos.size() == 0) {
			   bu=null;
		}
		return bu;
	}
	
	public String getPDU(String bu,String pdu) {
		List<OrganizeInfo> orgInfos = orgInfoDao.getBU(pdu);
	    if (orgInfos.size() == 0) {
		      pdu=null;
			  return pdu;
		 }
		 if(bu==null){
		   return pdu;
		}
	    List<String> list = new ArrayList<String>();	
		List<OrganizeInfo> orgInfo = orgInfoDao.getBU(bu);
		for (OrganizeInfo infos : orgInfos) {
			infos.setLevel(0);
			infos.setNodeName(bu);
			int nodeId = orgInfoDao.getNodeId(infos);
			List<OrganizeInfo> orgs = orgInfoDao.getPDU(nodeId);
			for (OrganizeInfo org : orgs) {
				list.add(org.getNodeName());
			}
		}
		if (!list.contains(pdu)) {
			pdu=null;
		 }
		return pdu;
	}
	
	public String judgeProjInfo(String no) {
		String projMessage=null;
		ProjectDetailInfo proj=projectListDao.isExit(no);
		if(proj!=null){
		  projMessage="已存在项目";
		}else{
		  projMessage="不存在项目";
		}
		return  projMessage;
	}
	
	public String judgeMemberInfo(String no) {
		String MemberMessage=null;
		ProjectManager projM=projectListDao.isExitMember(no);
		if(projM!=null){
			MemberMessage="项目信息存在";
		}else{
			MemberMessage="不存在项目信息";
		}
		return  MemberMessage;
	}
	
	public String Exitmember(String no,String author) {
		String Message= null;
		Map<String, String>member = new HashMap<String, String>();
		member.put("no", no);
		member.put("author",author );
		ProjectMember projm = projectListDao.queryMember(member);
		if (projm !=null) {
			Message="人员信息已存在";
		}else {
			Message= "人员信息不存在";
		}
		return Message;
	}

	private String getCellFormatValue(Cell cell) {
		String strCell =null;
		if (cell == null) {
			return strCell;
		}
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		default:
			strCell =null;
			break;
		}
		return strCell;
	}

	public Date CovertDate(Cell cell) {
		Date dates = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		if (null == cell) {
			return dates;
		}
		try {
			Date cellDate = df.parse(cell.getStringCellValue());
			if (null == cellDate) {
				return dates;
			}
			dates = cellDate;
		} catch (ParseException e) {
			dates=null;
		}catch(IllegalStateException e){
			dates=null;
		}

		return dates;
	}

	public Double StringToDouble(String str) {
		try{
		  return Double.parseDouble(str);
		}catch(NullPointerException e){
			return null;
		}catch(Exception e){
			return null;
		}
		
	}
		
	
	public String isProjectType(String projectType){
		CodeMasterInfo codeInfo=new CodeMasterInfo();
		List<String> list=new ArrayList<String>();
		if(projectType==null){
			projectType=null;
		}
		codeInfo.setCodekey("ProjectType");
		List<CodeMasterInfo> codeMaster=codeMasterInfo.getList(codeInfo);
		for(CodeMasterInfo code:codeMaster){
			list.add(code.getName());
		}
		if(!list.contains(projectType)){
			projectType=null;
		}
	  return projectType;
	}
	
   public String isPayType(String payType){
	   CodeMasterInfo codeInfo=new CodeMasterInfo();
		List<String> list=new ArrayList<String>();
		if(payType==null){
			payType=null;
		}
		codeInfo.setCodekey("PayType");
		List<CodeMasterInfo> codeMaster=codeMasterInfo.getList(codeInfo);
		for(CodeMasterInfo code:codeMaster){
			list.add(code.getName());
		}
		if(!list.contains(payType)){
			payType=null;
		}
		return payType;
	}
   
   public String isPayDepart(String payDepart){
	   CodeMasterInfo codeInfo=new CodeMasterInfo();
		List<String> list=new ArrayList<String>();
		if(payDepart==null){
			payDepart=null;
		}
		codeInfo.setCodekey("DeliveryDepartment");
		List<CodeMasterInfo> codeMaster=codeMasterInfo.getList(codeInfo);
		for(CodeMasterInfo code:codeMaster){
			list.add(code.getName());
		}
		if(!list.contains(payDepart)){
			payDepart=null;
		}
		return payDepart;
	}
   
   public String isArea(String area){
	   CodeMasterInfo codeInfo=new CodeMasterInfo();
		List<String> list=new ArrayList<String>();
		if(area==null){
			area=null;
		}
		codeInfo.setCodekey("area");
		List<CodeMasterInfo> codeMaster=codeMasterInfo.getList(codeInfo);
		for(CodeMasterInfo code:codeMaster){
			list.add(code.getName());
		}
		if(!list.contains(area)){
			area=null;
		}
		return area;
	}

	public Map<String, Object> importKeyRoles(MultipartFile file) {
		Map<String, Object> result = new HashMap<String, Object>();
		InputStream is;
		Workbook workbook=null;
		String err="";
		String sess="";
		try {
			is = file.getInputStream();
			workbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			LOG.error("read file failed!", e);
			result.put("STATUS", "FAILED");
			result.put("MESSAGE", "读取文件失败！");
			return result;
		}
		Sheet sheet = workbook.getSheetAt(0);
		int rowSize = sheet.getLastRowNum();
		Row row = null;
		if(rowSize<1){
			result.put("STATUS", "FAILED");
			result.put("MESSAGE", "没有要导入的数据！");
			return result; 
		}
		projectKeyrolesDao.batchDelete();
		List<ProjectKeyroles> projectKeyroles = new ArrayList<>();
		for (int i = 1; i <= rowSize; i++) {
			row = sheet.getRow(i);
			ProjectKeyroles keyroles = new ProjectKeyroles();
			String name =getCellFormatValue(row.getCell(1));
			String zrAccount =getCellFormatValue(row.getCell(0));
			String no =getCellFormatValue(row.getCell(13));
			if(name==null ||zrAccount==null||no==null){
				LOG.error("用户名和项目编号不能为空");
				continue;
			}
			keyroles.setNo(no);
			keyroles.setName(name);
			keyroles.setZrAccount(zrAccount.substring(1));
			keyroles.setPosition(getCellFormatValue(row.getCell(19)));
			keyroles.setReplyResults(getCellFormatValue(row.getCell(21)));
			keyroles.setProCompetence(getCellFormatValue(row.getCell(22)));
			keyroles.setStatus(getCellFormatValue(row.getCell(26)));
			projectKeyroles.add(keyroles);
		}
		projectKeyrolesDao.insertInfos(projectKeyroles);
		result.put("STATUS", "SUCCESS");
		result.put("err", err);
		result.put("sess", sess);
		return result;
	}

	public Map<String, Object> importRDPM(MultipartFile file) {
		Map<String, Object> result = new HashMap<String, Object>();
		InputStream is;
		Workbook workbook=null;
		String err="";
		String sess="";
		try {
			is = file.getInputStream();
			workbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			LOG.error("read file failed!", e);
			result.put("STATUS", "FAILED");
			result.put("MESSAGE", "读取文件失败！");
			return result;
		}
		Sheet sheet = workbook.getSheetAt(0);
		int rowSize = sheet.getLastRowNum();
		Row row = null;
		if(rowSize<2){
			result.put("STATUS", "FAILED");
			result.put("MESSAGE", "没有要导入的数据！");
			return result; 
		}
		List<ProjectKeyroles> projectKeyroles = new ArrayList<>();
		for (int i = 2; i <= rowSize; i++) {
			row = sheet.getRow(i);
			ProjectKeyroles keyroles = new ProjectKeyroles();
			String zrAccount =getCellFormatValue(row.getCell(1));
			String examType =getCellFormatValue(row.getCell(9));
			String rdpmExam =getCellFormatValue(row.getCell(11));
			if(examType==null ||zrAccount==null||rdpmExam==null){
				LOG.error("用户名和项目编号不能为空");
				continue;
			}
			zrAccount = zrAccount.substring(1);
			List<ProjectKeyroles> list = projectKeyrolesDao.queryProjectKeyrolesZrAccount(zrAccount);
			for (ProjectKeyroles projectKey : list) {
				if("通过".equals(projectKey.getRdpmExam())){
					continue;
				}
				if(examType.equals("PMP")||examType.equals("RDPM")) {
					if("通过".equals(rdpmExam)) {
						projectKey.setRdpmExam(rdpmExam);
						projectKeyrolesDao.updateProjectKeyroles(projectKey);
					}
				}
			}
		}
		result.put("STATUS", "SUCCESS");
		result.put("err", err);
		result.put("sess", sess);
		return result;
	}
}