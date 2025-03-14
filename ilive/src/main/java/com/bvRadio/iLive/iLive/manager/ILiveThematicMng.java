package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveThematic;

/**
 * 专题管理   事务层
 * @author YanXL
 *
 */
public interface ILiveThematicMng {
	/**
	 * 专题分页数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
	 * @param isDelete 是否删除    
	 * @return
	 */
	public Pagination selectILiveThematicPage(Integer pageNo, Integer pageSize, boolean isDelete);
	/**
	 * 添件专题
	 * @param iLiveThematic
	 */
	public void addILiveThematic(ILiveThematic iLiveThematic);
	/**
	 * 删除数据
	 * @param thematicId 专题ID
	 * @param b
	 */
	public void deleteILiveThematicByIsDelete(Long thematicId, boolean b);
	/**
	 * 根据主键ID获取数据
	 * @param thematicId 主键ID
	 * @return
	 */
	public ILiveThematic selectIliveThematicById(Long thematicId);
	/**
	 * 修改数据
	 * @param iLiveThematic
	 */
	public void updateILiveThematic(ILiveThematic iLiveThematic);
	
	public List<ILiveThematic> getAllList();
	
	/**
	 * 根据企业id获取专题列表
	 * @param enterpriseId
	 * @return
	 */
	public List<ILiveThematic> getListByEnterpriseId(Integer enterpriseId);
	
	/**
	 * 根据企业id获取专题列表和数量
	 * @param enterpriseId
	 * @return
	 */
	public List<ILiveThematic> getListByEnterpriseIdAndSize(Integer enterpriseId,Integer num);

}
