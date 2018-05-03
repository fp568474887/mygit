package com.icss.mvp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectKeyroles;

public interface IProjectKeyrolesDao {
	List<ProjectKeyroles> queryProjectKeyrolesNo(@Param("no") String no);
	int insertInfos(@Param("proj") List<ProjectKeyroles> proj);
	int batchDeleteByNo(@Param("no") String no);
	void batchDelete();
	List<ProjectKeyroles> queryProjectKeyrolesZrAccount(@Param("zrAccount")String zrAccount);
	int updateProjectKeyroles(@Param("proj") ProjectKeyroles projectKey);
}
