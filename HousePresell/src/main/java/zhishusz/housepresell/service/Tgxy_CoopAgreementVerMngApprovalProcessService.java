package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：合作协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementVerMngApprovalProcessService
{
	@Autowired
	private Tgxy_CoopAgreementVerMngDao Tgxy_CoopAgreementVerMngDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;

	public Properties execute( Tgxy_CoopAgreementVerMngForm model)
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
			return MyBackInfo.fail(properties, "请选择有效的合作协议版本管理主键");
		}

		Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng = Tgxy_CoopAgreementVerMngDao.findById(tableId);

		if (null == tgxy_CoopAgreementVerMng)
		{
			return MyBackInfo.fail(properties, "未查询到有效的合作协议版本管理");
		}

		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		if (properties.getProperty("info").equals("noApproval"))
		{						
			/*
			 * 具体的业务逻辑操作:
			 * 三方协议状态置为已备案状态
			 */
			Tgxy_CoopAgreementVerMngForm Tgxy_CoopAgreementVerMngForm=new Tgxy_CoopAgreementVerMngForm();
			Tgxy_CoopAgreementVerMngForm.setTheType(tgxy_CoopAgreementVerMng.getTheType());
			List<Tgxy_CoopAgreementVerMng> tgxy_CoopAgreementVerMnglist=Tgxy_CoopAgreementVerMngDao.getQuery(Tgxy_CoopAgreementVerMngDao.getBasicH2(), Tgxy_CoopAgreementVerMngForm).getResultList();               
			if(tgxy_CoopAgreementVerMnglist.size()>=2){
				Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng1=new Tgxy_CoopAgreementVerMng();
            	tgxy_CoopAgreementVerMng1=tgxy_CoopAgreementVerMnglist.get(1);
				if(!tgxy_CoopAgreementVerMng1.getTableId().equals(tgxy_CoopAgreementVerMng.getTableId())){
					tgxy_CoopAgreementVerMng1.setDownTimeStamp(tgxy_CoopAgreementVerMng.getEnableTimeStamp()); 
                	Tgxy_CoopAgreementVerMngDao.save(tgxy_CoopAgreementVerMng1);
				}               	
            }
			tgxy_CoopAgreementVerMng.setRecordTimeStamp(System.currentTimeMillis());				
			
			//设置审核的状态
			tgxy_CoopAgreementVerMng.setBusiState(S_BusiState.HaveRecord);
			tgxy_CoopAgreementVerMng.setApprovalState(S_ApprovalState.Completed);
						
		}else{
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties.get("sm_approvalProcess_cfg");			
			// 审批操作
			sm_approvalProcessService.execute(tgxy_CoopAgreementVerMng, model, sm_approvalProcess_cfg);
			// 设置状态
			tgxy_CoopAgreementVerMng.setApprovalState(S_ApprovalState.Examining);
		}
		
		
		Tgxy_CoopAgreementVerMngDao.save(tgxy_CoopAgreementVerMng);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}
}
