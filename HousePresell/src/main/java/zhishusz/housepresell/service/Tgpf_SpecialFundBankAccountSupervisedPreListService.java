package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.controller.form.Tgpj_BankAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service列表查询：根据开发企业预加载
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgpf_SpecialFundBankAccountSupervisedPreListService
{
	@Autowired
	private Empj_BuildingAccountSupervisedDao empj_buildingAccountSupervisedDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpj_BankAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();

		// 选择楼幢Id
		Long buildingId = model.getBuildingId();

		// 需要返回到前端的监管账户列表
		List<Tgpj_BankAccountSupervised> tgpj_BankAccountSupervisedList;

		List<Empj_BuildingAccountSupervised> empj_BuildingAccountSupervisedList;
		// 根据楼幢查询关联监管账户
		Empj_BuildingAccountSupervisedForm empj_buildingAccountSupervisedForm = new Empj_BuildingAccountSupervisedForm();
		empj_buildingAccountSupervisedForm.setBuildingInfoId(buildingId);
		empj_buildingAccountSupervisedForm.setTheState(S_TheState.Normal);

		empj_BuildingAccountSupervisedList = empj_buildingAccountSupervisedDao
				.findByPage(empj_buildingAccountSupervisedDao.getQuery(empj_buildingAccountSupervisedDao.getBasicHQL(),
						empj_buildingAccountSupervisedForm));

		//判断所选择的楼幢有误监管账户关联
		if (null != empj_BuildingAccountSupervisedList && empj_BuildingAccountSupervisedList.size() > 0)
		{
			tgpj_BankAccountSupervisedList = new ArrayList<Tgpj_BankAccountSupervised>();
			
			for (Empj_BuildingAccountSupervised empj_BuildingAccountSupervised : empj_BuildingAccountSupervisedList)
			{
				//将关联的监管账户加入返回的列表
				tgpj_BankAccountSupervisedList.add(empj_BuildingAccountSupervised.getBankAccountSupervised());
				
			}
		}
		else
		{
			
			tgpj_BankAccountSupervisedList = new ArrayList<Tgpj_BankAccountSupervised>();
			
		}

		properties.put("tgpj_BankAccountSupervisedList", tgpj_BankAccountSupervisedList);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
