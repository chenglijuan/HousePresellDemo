package zhishusz.housepresell.service.extra;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.controller.form.extra.Tg_EarlyWarningFrom;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_contractDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_personofcontractDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.Tb_b_contract;
import zhishusz.housepresell.database.po.extra.Tb_b_personofcontract;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 户室预警消息1推送触发service
 * 触发条件：已批准预售->查封
 * 接收者：
 * 1、项目所属区域负责人
 * 2、法务负责人
 * 消息内容：
 * 可售房源“完整户坐落XX”变更为查封状态！
 * 
 * @ClassName: Tg_HouseEarlyForOneService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月16日 下午6:27:00
 * @version V1.0
 * 
 * 
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_HouseEarlyForOneService
{
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;// 消息主体
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;// 消息关联
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;// 户室
	@Autowired
	private Sm_UserDao sm_UserDao;// 用户

	public Properties execute(Tg_EarlyWarningFrom model)
	{
		Properties properties = new MyProperties();

		/*
		 * 1.根据传递的消息主体ID查询触发器新增的预警信息
		 * 2.根据预警信息主体查询预警对象（业务）主体--触发者
		 * 3.根据预警对象（业务）主体，查询相关负责人--接收者
		 * ==》需要推送预警消息的人员--接收者
		 * 4.保存关联信息
		 */

		// 获取预警消息ID
		Long tableId = model.getTableId();

		// 根据消息ID查询预警消息主体
		Sm_CommonMessage message = sm_CommonMessageDao.findById(tableId);
		if (null == message)
		{
			return MyBackInfo.fail(properties, "根据消息ID:" + tableId + "，未获取到详细信息");
		}

		// 获取相关预警信息
		// String busiCode = message.getBusiCode();//业务编号
		// String orgDataId = message.getOrgDataId();//关联业务ID
		String orgDataCode = message.getOrgDataCode();// 关联业务编号

		/*
		 * 根据业务编号查询触发者详细信息
		 * tableId
		 * eCode
		 * 再查询接收者信息
		 */
		Empj_HouseInfoForm houseModel = new Empj_HouseInfoForm();
		houseModel.seteCode(orgDataCode);
		Empj_HouseInfo houseInfo = (Empj_HouseInfo) empj_HouseInfoDao
				.findOneByQuery(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getBasicHQL(), houseModel));

		if (null == houseInfo)
		{
			return MyBackInfo.fail(properties, "根据户室编号：" + orgDataCode + "，未查询到相关户室信息");
		}

		// 项目负责人
		String projectLeader = houseInfo.getBuilding().getProject().getProjectLeader();

		Sm_UserForm userForm = new Sm_UserForm();
		userForm.setTheName(projectLeader);
		Sm_User sm_User = (Sm_User) sm_UserDao.findOneByQuery(sm_UserDao.getQuery(sm_UserDao.getBasicHQL(), userForm));
		if (null == sm_User)
		{
			return MyBackInfo.fail(properties, "负责人名称："+projectLeader+"，未查询到相关信息");
		}
		
		//保存预警信息与接收者的关联关系
		Sm_CommonMessageDtl commonMessage = new Sm_CommonMessageDtl();
		commonMessage.setReceiver(sm_User);
		commonMessage.setMessage(message);
		
		sm_CommonMessageDtlDao.save(commonMessage);
		
		/*
		 * 根据接收者数量，循环进行消息推送
		 * ++暂无，后期利用消息推送公共类进行业务处理++
		 */

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
