package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.external.service.Tgxy_TripleAgreementDeleteInterfaceService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个撤回：三方协议
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_TripleAgreementWithdrawService
{
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Tgxy_TripleAgreementDeleteInterfaceService deleteInterfaceService;

	public Properties execute(Tgxy_TripleAgreementForm model)
	{
		
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		if(null == user)
		{
			return MyBackInfo.fail(properties, "登录信息已失效，请重新登录！");
		}

		Long tgxy_TripleAgreementId = model.getTableId();
		if(null == tgxy_TripleAgreementId || tgxy_TripleAgreementId < 0)
		{
			return MyBackInfo.fail(properties, "请选择需要撤回的协议信息");
		}
		Tgxy_TripleAgreement tgxy_TripleAgreement = (Tgxy_TripleAgreement)tgxy_TripleAgreementDao.findById(tgxy_TripleAgreementId);
		if(tgxy_TripleAgreement == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的协议信息，请刷新后重试");
		}
		
		//撤回后恢复为待提交，未备案状态
		tgxy_TripleAgreement.setApprovalState(S_ApprovalState.WaitSubmit);
		tgxy_TripleAgreement.setUserUpdate(user);
		tgxy_TripleAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
		
		/**
		 * xsz by time 2019-7-8 17:17:43
		 * 与档案系统接口对接
		 * ====================start==================
		 */
		
		Properties execute = deleteInterfaceService.execute(tgxy_TripleAgreement, "1", model);
		if(execute.isEmpty()|| S_NormalFlag.fail.equals(execute.get(S_NormalFlag.result)))
		{
			return MyBackInfo.fail(properties, "与档案系统对接失败，请稍后重试！");
		}
		
		/**
		 * xsz by time 2019-7-8 17:17:43
		 * 与档案系统接口对接
		 * ====================end==================
		 */

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
