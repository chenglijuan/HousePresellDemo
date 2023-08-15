package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

@Service
@Transactional
public class Tgxy_TripleAgreementVerMngApprovalProcessService {
	@Autowired
	private Tgxy_TripleAgreementVerMngDao Tgxy_TripleAgreementVerMngDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;

	public Properties execute(Tgxy_TripleAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();

		String buttonType = model.getButtonType(); // 1： 保存按钮 2：提交按钮

		if (null == buttonType || buttonType.trim().isEmpty())
		{
			buttonType = "2";
		}

		String busiCode = model.getBusiCode();
		Long tableId = model.getTableId();
		Long userStartId = model.getUserId(); // 登录用户id TODO
		
		model.setButtonType(buttonType);

		if (busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'业务编码'不能为空");
		}

		if (tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择主键");
		}

		Tgxy_TripleAgreementVerMng TripleAgreementVerMng = Tgxy_TripleAgreementVerMngDao.findById(tableId);

		if (null == TripleAgreementVerMng)
		{
			return MyBackInfo.fail(properties, "未查询到有效的合作协议");
		}

		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		if (properties.getProperty("info").equals("noApproval"))
		{
			Tgxy_TripleAgreementVerMngForm tgxy_TripleAgreementVerMngForm=new Tgxy_TripleAgreementVerMngForm();
			tgxy_TripleAgreementVerMngForm.setTheType(TripleAgreementVerMng.getTheType());
			List<Tgxy_TripleAgreementVerMng> tgxy_TripleAgreementVerMnglist=Tgxy_TripleAgreementVerMngDao.getQuery(Tgxy_TripleAgreementVerMngDao.getBasicH1(), tgxy_TripleAgreementVerMngForm).getResultList();
            if(tgxy_TripleAgreementVerMnglist.size()>1){
            	Tgxy_TripleAgreementVerMng tgxy_TripleAgreementVerMng=new Tgxy_TripleAgreementVerMng();
            	tgxy_TripleAgreementVerMng=tgxy_TripleAgreementVerMnglist.get(1);
            	if(!tgxy_TripleAgreementVerMng.getTableId().equals(TripleAgreementVerMng.getTableId())){
            		tgxy_TripleAgreementVerMng.setDownTimeStamp(TripleAgreementVerMng.getEnableTimeStamp());                	
                	Tgxy_TripleAgreementVerMngDao.save(tgxy_TripleAgreementVerMng);
            	}               	
            }
			TripleAgreementVerMng.setRecordTimeStamp(System.currentTimeMillis());			
			//设置审核的状态
			TripleAgreementVerMng.setBusiState(S_BusiState.HaveRecord);
			TripleAgreementVerMng.setApprovalState(S_ApprovalState.Completed);
			
		} else {

			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
					.get("sm_approvalProcess_cfg");

			// 审批操作
			sm_approvalProcessService.execute(TripleAgreementVerMng, model, sm_approvalProcess_cfg);
			// 设置状态
			TripleAgreementVerMng.setApprovalState(S_ApprovalState.Examining);
		}
		Tgxy_TripleAgreementVerMngDao.save(TripleAgreementVerMng);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}
}
