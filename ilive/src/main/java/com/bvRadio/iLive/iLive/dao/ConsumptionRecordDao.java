package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ConsumptionRecordBean;
import com.bvRadio.iLive.iLive.entity.UserBean;

/**
 * 消费记录 数据连接层
 * @author YanXL
 *
 */
public interface ConsumptionRecordDao {
	/**
	 * 消费记录分页数据
	 * @param pageNo 页面
	 * @param pageSize 每页数据条数
	 * @param admin_id 主播ID
	 * @param user_id  用户ID
	 * @param consumption_type 
	 * @return 分页数据
	 * @throws Exception
	 */
	public Pagination selectComsumptionRecordByPage(Integer pageNo,Integer pageSize, Integer consumption_type, Integer user_id, Integer admin_id) throws Exception;
	/**
	 * 个人消费记录分页数据
	 * @param pageNo 页面
	 * @param pageSize 每页数据条数
	 * @param admin_id 主播ID
	 * @param user_id  用户ID
	 * @param consumption_type 
	 * @param userBean 登录用户
	 * @return 分页数据
	 * @throws Exception
	 */
	public Pagination selectPersonalComsumptionRecordByPage(Integer pageNo,Integer pageSize, Integer consumption_type,UserBean userBean) throws Exception;
	
	/**
	 * 添加消费记录
	 * @param consumptionRecordBean 消费信息
	 * @return
	 * @throws Exception
	 */
	public ConsumptionRecordBean insertConsumptionRecordBean(ConsumptionRecordBean consumptionRecordBean) throws Exception;
	/**
	 * 根据主键查询数据
	 * @param consumption_id 主键id
	 * @return
	 * @throws Exception
	 */
	public ConsumptionRecordBean selectConsumptionRecordBeanByID(long consumption_id) throws Exception;
	/**
	 * 根据对象删除数据
	 * @param consumptionRecordBean 删除数据
	 * @throws Exception
	 */
	public void delectConsumptionRecordBean(ConsumptionRecordBean consumptionRecordBean) throws Exception;
	/**
	 * 根据用户获取所有支出消费记录
	 * @param user_id  用户ID
	 * @return
	 * @throws Exception
	 */
	public List<ConsumptionRecordBean> selectConsumptionRecordByOut(Integer user_id) throws Exception;
}
