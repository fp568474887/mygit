package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectDataSourceInfo;

public interface IProjectSourceConfigDao {
	public List<ProjectDataSourceInfo> queryProjDSInfo(@Param("projNo")String projNo,@Param("sort") String sort, @Param("order")String order);
	public int insertProjDS(ProjectDataSourceInfo projDSInfo);
	public int updateProjDS(ProjectDataSourceInfo projDSInfo);
	public int delProjDS(ProjectDataSourceInfo projDSInfo);
	public ProjectDataSourceInfo queryProjDSByKey(@Param("projNo")String projNo,@Param("source_value")String sourceValue);
	public List<ProjectDataSourceInfo> queryAllDS();
	public List<ProjectDataSourceInfo> queryDSBySource(String source);
	public ProjectDataSourceInfo queryDSByNo(@Param("no")String no);
	public void inserturl(@Param("projDSInfo")ProjectDataSourceInfo projDSInfo);
	public void updateurl(@Param("projDSInfo")ProjectDataSourceInfo projDSInfo);
}
