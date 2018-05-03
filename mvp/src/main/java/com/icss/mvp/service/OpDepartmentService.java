package com.icss.mvp.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icss.mvp.dao.OpDepartmentDao;
import com.icss.mvp.entity.OpDepartment;

@Service("opDepartmentService")
@Transactional
public class OpDepartmentService {
	
	@Resource
	private OpDepartmentDao dao;
	
	private static Logger logger = Logger.getLogger(OpDepartmentService.class);
	
	/**
	 * <p>Title: getSysHwdeptByPId</p>
	 * <p>Description: 通过父id查找下面的子节点信息</p>
	 * @author gaoyao
	 * @param pId
	 * @return
	 */
	public List<OpDepartment> getOpDepartmentByPId(String pId){
		return dao.getOpDepartmentByPId(pId);
	}
	
	
	
    /**
	 * 在数据库中新插入一条指定的中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer insertOpDepartment(OpDepartment opDepartment){
		return dao.insertOpDepartment(opDepartment);
	}
	
	/**
	 * 在数据库中修改此条中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer updateOpDepartment(OpDepartment opDepartment){
		return dao.updateOpDepartment(opDepartment);
	}
	
	/**
	 * 在数据库中通过主键id查出指定的中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public OpDepartment getOpDepartmentById(String id){
		return dao.getOpDepartmentById(id);
	}
	
	
	/**
	 * 在数据库中通过主键id删除指定中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer deleteOpDepartmentById(String id){
		return dao.deleteOpDepartmentById(id);
	}
	
	/**
	 * 在数据库中通过主键id批量删除指定中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public Integer deleteOpDepartmentByIdList(List<String> list){
		return dao.deleteOpDepartmentByIdList(list);
	}
	
	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize）
	 * 和其他条件参数得到指定条数的中软组织结构记录
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
	 */
	public List<OpDepartment> getOpDepartmentForPage(Map<String,Object> map){
		return dao.getOpDepartmentForPage(map);
	}
	
    /**
     * 在数据库中通过Map中条件参数得到指定中软组织结构记录的总条数
	 * @author gaoyao
     * @time 2018-4-25 11:06:49
     */
	public Integer getOpDepartmentCount(Map<String,Object> map){
		return dao.getOpDepartmentCount(map);
	}
	
	/**
     * 在数据库中查出所有中软组织结构记录
     * @author gaoyao
     * @time 2018-4-25 11:06:49
     */
	public List<OpDepartment> getAllOpDepartment(){
		return dao.getAllOpDepartment();
	}
}
