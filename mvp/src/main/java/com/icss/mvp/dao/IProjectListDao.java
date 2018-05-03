package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.CodeMasterInfo;
import com.icss.mvp.entity.OrganizeMgmer;
import com.icss.mvp.entity.ProjectClock;
import com.icss.mvp.entity.ProjectDetailInfo;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.ProjectManager;
import com.icss.mvp.entity.ProjectMember;

public interface IProjectListDao
{
	public List<ProjectInfo> getList(@Param("proj")ProjectInfo proj, @Param("sort") String sort, @Param("order")String order);
	public List<ProjectDetailInfo> queryProjInfo(@Param("projNo")String projNo);
	public ProjectInfo getProjInfo(@Param("buName") String name, @Param("no") String no);
	public ProjectInfo getProjInfoByNo(@Param("no") String no);
	public List<OrganizeMgmer> getBusinessUnit();
	public List<OrganizeMgmer> getDeliveryUnit();
	public List<CodeMasterInfo> getCountArea();
	public List<CodeMasterInfo> getProjectType();
	public List<ProjectManager> getProjectManager();
	
	public int insertInfo(@Param("proj") ProjectDetailInfo proj);
	public ProjectDetailInfo isExit(String no);
	public int updateInfo(ProjectDetailInfo proj);

	public int insertMember(ProjectManager projManger);
	public ProjectManager isExitMember(String no);
	public int updateMemberInfo(ProjectManager projManger);
	
	public int insertProjInfos(List<ProjectDetailInfo> projInfo);
	public int insertProjMembers(List<ProjectManager> projMgs);
	
	public List<ProjectInfo> isExistVersion(@Param("version")String version);
	
	public int insertmemberInfo(@Param("projm") ProjectMember projm);
	
	public ProjectMember queryMember(@Param("map")Map<String, String> map);
	
	public String searchNameByAuthor(String author);
	
	public int inserClockInfo(@Param("projc") ProjectClock projc);
	public int replaceInfo(@Param("proj") ProjectDetailInfo projInfo);
	public int replaceMember(ProjectManager projMember);
}
