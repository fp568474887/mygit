package com.icss.mvp.dao;

import java.util.List;
import java.util.Map;

import com.icss.mvp.entity.SysHwdept;

/**
 * 华为部门表对应的MyBatis SQL XML配置文件的映射接口
 * @author gaoyao
 * @time 2018-4-24 19:08:41
*/
public interface SysHwdeptDao {
	
	/**
	 * 
	 * <p>Title: getSysHwdeptByPId</p>  
	 * <p>Description: 通过父id查找下面的子节点信息</p>  
	 * @param pId
	 * @author gaoyao
	 * @return
	 */
	public List<SysHwdept> getSysHwdeptByPId(String pId);
	
    /**
	 * 在数据库中新插入一条指定的华为部门记录
	 * @author gaoyao
     * @time 2018-4-24 19:08:41
	 */
	public Integer insertSysHwdept(SysHwdept sysHwdept);
	
	/**
	 * 在数据库中修改此条华为部门记录
	 * @author gaoyao
     * @time 2018-4-24 19:08:41
	 */
	public Integer updateSysHwdept(SysHwdept sysHwdept);
	
	/**
	 * 在数据库中通过主键deptId查出指定的华为部门记录
	 * @author gaoyao
     * @time 2018-4-24 19:08:41
	 */
	public SysHwdept getSysHwdeptByDeptId(String deptId);
	
	
	/**
	 * 在数据库中通过主键deptId删除指定华为部门记录
	 * @author gaoyao
     * @time 2018-4-24 19:08:41
	 */
	public Integer deleteSysHwdeptByDeptId(String deptId);
	
	/**
	 * 在数据库中通过主键deptId批量删除指定华为部门记录
	 * @author gaoyao
     * @time 2018-4-24 19:08:41
	 */
	public Integer deleteSysHwdeptByDeptIdList(List<String> list);
	
	/**
	 * 在数据库中通过Map中的分页参数（startNo,pageSize）
	 * 和其他条件参数得到指定条数的华为部门记录
	 * @author gaoyao
     * @time 2018-4-24 19:08:41
	 */
	public List<SysHwdept> getSysHwdeptForPage(Map<String,Object> map);
	
    /**
     * 在数据库中通过Map中条件参数得到指定华为部门记录的总条数
	 * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
	public Integer getSysHwdeptCount(Map<String,Object> map);
	
	/**
     * 在数据库中查出所有华为部门记录
     * @author gaoyao
     * @time 2018-4-24 19:08:41
     */
	public List<SysHwdept> getAllSysHwdept();
}
