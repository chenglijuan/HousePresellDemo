package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：
 * 根据楼幢主键加载楼幢相关
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_SpecialFundBuildingInfoDetailService
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	@Autowired
	private Empj_BuildingAccountSupervisedDao empj_buildingAccountSupervisedDao;

	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();

		model.setTheState(S_TheState.Normal);
		// 查询楼幢相关信息
		Long empj_BuildingInfoId = model.getTableId();

		if (null == empj_BuildingInfoId || empj_BuildingInfoId < 0)
		{
			return MyBackInfo.fail(properties, "请选择施工编号");
		}

		Empj_BuildingInfo empj_BuildingInfo = (Empj_BuildingInfo) empj_BuildingInfoDao.findById(empj_BuildingInfoId);
		if (empj_BuildingInfo == null || S_TheState.Deleted.equals(empj_BuildingInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "该楼幢信息已失效，请刷新后重试");
		}

		// 查询楼幢账户相关信息
		Tgpj_BuildingAccountForm aModel = new Tgpj_BuildingAccountForm();
		aModel.setBuilding(empj_BuildingInfo);
		aModel.setTheState(S_TheState.Normal);
		Tgpj_BuildingAccount tgpj_BuildingAccount = tgpj_BuildingAccountDao
				.findOneByQuery_T(tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), aModel));

		if (null == tgpj_BuildingAccount)
		{
			return MyBackInfo.fail(properties, "未查询到选择楼幢的账户信息");
		}

		/*
		 * xsz by time 2018-11-30 09:12:53
		 * 对应的楼幢没有维护监管账号和备案价格，不可以做拨付，限制客户操作，如果没有维护监管账户，则提示信息“请维护监管账户！”；
		 * 如果没有维护备案价格，则提示信息“请维护物价备案价格！”。
		 */
		/*Empj_BuildingAccountSupervisedForm accountSupervisedForm = new Empj_BuildingAccountSupervisedForm();
		accountSupervisedForm.setTheState(S_TheState.Normal);
		accountSupervisedForm.setBuildingInfoId(empj_BuildingInfo.getTableId());
		Integer count = empj_buildingAccountSupervisedDao.findByPage_Size(empj_buildingAccountSupervisedDao
				.getQuery_Size(empj_buildingAccountSupervisedDao.getBasicHQL(), accountSupervisedForm));
		if (count == 0)
		{
			return MyBackInfo.fail(properties, "请维护监管账户！");
		}*/

		if (null == tgpj_BuildingAccount.getRecordAvgPriceOfBuilding()
				|| tgpj_BuildingAccount.getRecordAvgPriceOfBuilding() <= 0)
		{
			return MyBackInfo.fail(properties, "请维护物价备案价格！");
		}

		properties.put("empj_BuildingInfo", empj_BuildingInfo);
		properties.put("tgpj_BuildingAccount", tgpj_BuildingAccount);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
