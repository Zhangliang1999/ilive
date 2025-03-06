package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveThematic;

/**
 * 专题管理
 * @author YanXL
 *
 */
public interface ILiveThematicDao {
	/**
	 * 分页数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据条数
 	 * @param isDelete 是否删除
	 * @return
	 * @throws Exception
	 */
	public Pagination selectILiveThematicPage(Integer pageNo, Integer pageSize,
			boolean isDelete) throws Exception;
	/**
	 * 新增数据
	 * @param iLiveThematic
	 * @throws Exception
	 */
	public void addILiveThematic(ILiveThematic iLiveThematic) throws Exception;
	/**
	 * 删除数据
	 * @param thematicId 专题ID
 	 * @param isDelete 删除标示
	 * @throws Exception
	 */
	public void deleteILiveThematicByIsDelete(Long thematicId, boolean isDelete) throws Exception;
	/**
	 * 根据主键获取数据
	 * @param thematicId 主键ID
	 * @return
	 * @throws Exception
	 */
	public ILiveThematic selectIliveThematicById(Long thematicId) throws Exception;
	/**
	 * 修改数据
	 * @param iLiveThematic
	 * @throws Exception
	 */
	public void updateILiveThematic(ILiveThematic iLiveThematic) throws Exception;
	
	/**
	 * 获取所有专题列表
	 * @return
	 */
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
