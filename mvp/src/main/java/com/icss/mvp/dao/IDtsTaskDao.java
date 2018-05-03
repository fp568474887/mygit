package com.icss.mvp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface IDtsTaskDao
{
	public void insert(@Param("list") List<Map<String, String>> dtsList);

	public List<HashMap<String, Object>> query();

	public List<HashMap<String, Object>> queryDI();

	public List<HashMap<String, Object>> queryTr5DI();

	public List<HashMap<String, Object>> queryTr6DI();

        public List<HashMap<String,Object>> queryDensity();

		public HashMap<String,Object> queryServerity(@Param("no")String no);
 
		public List<Map<String, Object>> queryDts(@Param("no")String no, @Param("date") String date);

		public List<HashMap<String, Object>> queryServerityByVersion(@Param("no") String no);

		public List<HashMap<String, Object>> queryServerityByEven(Map<String, Object> map);
}
