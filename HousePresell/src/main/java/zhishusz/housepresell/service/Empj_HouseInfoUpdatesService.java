package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_HouseInfoUpdatesService
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;;
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	
	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Long tableId=model.getTableId();
		Long userUpdateId= model.getUserUpdateId();
		String roomId=model.getRoomId();//室号
		String position = model.getPosition();//房屋坐落
		Double floor = model.getFloor();//所在楼层
		Double forecastArea = model.getForecastArea();//建筑面积		
		Double shareConsArea = model.getShareConsArea();//分摊面积
		Double innerconsArea = model.getInnerconsArea();//套内建筑面积（㎡）
		String purpose = model.getPurpose();//房屋用途
		Double recordPrice = model.getRecordPrice();//物价备案价格
		//对日期进行处理	(将String类型转换成long类型)
		Long lastTimeStampSyncRecordPriceToPresellSystem = MyDatetime.getInstance().stringToLong(model.getP1().toString());//物价备案时间
		String remark = model.getRemark();//备注
		
		if(roomId == null || roomId.length() == 0)
		{
			return MyBackInfo.fail(properties, "室号不能为空");
		}
		if(position == null || position.length() == 0)
		{
			return MyBackInfo.fail(properties, "房屋坐落不能为空");
		}
		if(floor == null || floor < 1)
		{
			return MyBackInfo.fail(properties, "所在楼层不能为空");
		}
		if(forecastArea == null || forecastArea < 1)
		{
			return MyBackInfo.fail(properties, "建筑面积（预测）不能为空");
		}
		if(shareConsArea == null || shareConsArea < 1)
		{
			return MyBackInfo.fail(properties, "分摊面积（预测）不能为空");
		}
		if(innerconsArea == null || innerconsArea < 1)
		{
			return MyBackInfo.fail(properties, "套内面积（预测）不能为空");
		}
		if(purpose == null || purpose.length() == 0)
		{
			return MyBackInfo.fail(properties, "房屋用途不能为空");
		}
		if(recordPrice == null || recordPrice < 1)
		{
			return MyBackInfo.fail(properties, "物价备案价格不能为空");
		}
		if(lastTimeStampSyncRecordPriceToPresellSystem == null || lastTimeStampSyncRecordPriceToPresellSystem < 1)
		{
			return MyBackInfo.fail(properties, "物价备案时间不能为空");
		}
		if(remark == null || remark.length() == 0)
		{
			return MyBackInfo.fail(properties, "备注不能为空");
		}
		//判断室号是否唯一			
		Empj_HouseInfoForm empj_HouseInfoForm = new Empj_HouseInfoForm();
		empj_HouseInfoForm.setRoomId(model.getRoomId());
		empj_HouseInfoForm.setTheState(S_TheState.Normal);
		// 这个单元的下的室号是唯一的
		Long empj_UnitInfoId = model.getUnitInfoId();// 关联单元-Id
		// 根据单元的id获取对应单元的信息
		Empj_UnitInfo empj_UnitInfo = (Empj_UnitInfo) empj_UnitInfoDao.findById(empj_UnitInfoId);
		empj_HouseInfoForm.setUnitInfo(empj_UnitInfo);		
		Integer totalCount = empj_HouseInfoDao
				.findByPage_Size(empj_HouseInfoDao.getQuery_Size(empj_HouseInfoDao.getHouseInfoByUnitHQL(), empj_HouseInfoForm));
		if (totalCount > 1) {
			return MyBackInfo.fail(properties, "室号已存在");
		}		
		//根据tableid查询已有的信息
		Empj_HouseInfo  empj_HouseInfo=	empj_HouseInfoDao.findById(tableId);
		if(empj_HouseInfo!=null){
			empj_HouseInfo.setRoomId(roomId);
			empj_HouseInfo.setPosition(position);
			empj_HouseInfo.setFloor(floor);
			empj_HouseInfo.setForecastArea(forecastArea);
			empj_HouseInfo.setShareConsArea(shareConsArea);
			empj_HouseInfo.setInnerconsArea(innerconsArea);
			empj_HouseInfo.setPurpose(purpose);
			empj_HouseInfo.setRecordPrice(recordPrice);
			empj_HouseInfo.setLastTimeStampSyncRecordPriceToPresellSystem(lastTimeStampSyncRecordPriceToPresellSystem);
			empj_HouseInfo.setRemark(remark);
			//设置修改人
			Sm_User userUpdate =model.getUser() ;
			if(userUpdate == null || S_TheState.Deleted.equals(userUpdate.getTheState()))
			{
				return MyBackInfo.fail(properties, "用户未登录，请先登录！");
			}
			empj_HouseInfo.setUserUpdate(userUpdate);
			//设置修改时间
			empj_HouseInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
		}
		empj_HouseInfoDao.save(empj_HouseInfo);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
