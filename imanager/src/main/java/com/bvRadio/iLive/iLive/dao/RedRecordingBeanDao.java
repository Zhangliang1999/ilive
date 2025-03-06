package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.RedRecordingBean;

/**
 * 红包 管理 数据
 * @author YanXL
 *
 */
public interface RedRecordingBeanDao {
	/**
	 * 分页红包数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据
	 * @return
	 * @throws Exception
	 */
	public Pagination selectRedRecordingByPage(Integer pageNo,Integer pageSize) throws Exception;
	/**
	 * 添加红包 记录
	 * @param redRecordingBean 数据
	 * @throws Exception
	 */
	public void insertRedRecording(RedRecordingBean redRecordingBean) throws Exception;
	/**
	 * 根据用户分页红包数据
	 * @param pageNo 页码
	 * @param pageSize 每页数据
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public Pagination selectRedRecordingByPageUserId(int pageNo, int pageSize,
			Integer userId) throws Exception;
	/**
	 * 根据用户ID和红包类型获取数据
	 * @param redType 红包类型
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public List<RedRecordingBean> selectRedRecordingBeans(Integer redType,Integer userId)throws Exception;
	/**
	 * 根据主键ID获得信息
	 * @param redId
	 * @return
	 * @throws Exception
	 */
	public RedRecordingBean getRedRecordingBeanByID(Integer redId) throws Exception;
	/**
	 * 根据主体ID获得信息
	 * @param parentsId 主体
	 * @return
	 * @throws Exception
	 */
	public List<RedRecordingBean> getRedRecordingBeanByParentsID(Integer parentsId) throws Exception;
	/**
	 * 修改
	 * @param redRecordingBean
	 * @throws Exception
	 */
	public void updateRedRecordingBean(RedRecordingBean redRecordingBean) throws Exception;
}
