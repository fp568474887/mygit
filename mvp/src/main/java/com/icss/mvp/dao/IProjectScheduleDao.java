package com.icss.mvp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.icss.mvp.entity.ProjectSchedule;

public interface IProjectScheduleDao {
	List<ProjectSchedule> queryProjectScheduleNo(@Param("no") String no);
	int insertInfos(@Param("proj") List<ProjectSchedule> proj);
	int batchDeleteByNo(@Param("no") String no);
}
