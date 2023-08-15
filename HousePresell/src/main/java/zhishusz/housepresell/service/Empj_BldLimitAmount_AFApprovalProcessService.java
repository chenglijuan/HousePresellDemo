package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccountLog;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.BuildingAccountLogUtil;

/*
 * Service提交操作:提交 受限额度变更
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_BldLimitAmount_AFApprovalProcessService
{
	// 受限额度变更
	private static final String BUSI_CODE = "03030101";
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private ExportPdfByWordService exportPdfByWordService;//生成PDF
	@Autowired
	private Sm_AttachmentDao attacmentDao;
	@Autowired
	private Sm_AttachmentCfgDao attacmentcfgDao;
	
	@Autowired
    private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;//受限额度变更
	@Autowired
    private BuildingAccountLogUtil buildingAccountLogUtil;
	@Autowired
    private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_bldLimitAmountVer_afDtlDao;
	@Autowired
    private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;//用款申请子表
	@Autowired
    private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;//用款申请主表

	public Properties execute(Empj_BldLimitAmount_AFForm model)
	{
		Properties properties = new MyProperties();
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		String buttonType = model.getButtonType(); // 1： 保存按钮 2：提交按钮

		if (null == buttonType || buttonType.trim().isEmpty())
		{
			buttonType = "2";
		}

		String busiCode = model.getBusiCode();
		Long tableId = model.getTableId();

		model.setButtonType(buttonType);

		if (busiCode == null || busiCode.length() == 0)
		{
			busiCode = BUSI_CODE;
		}

		if (tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择有效的单据");
		}

		Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = empj_BldLimitAmount_AFDao.findById(tableId);
		if(null == empj_BldLimitAmount_AF)
		{
			return MyBackInfo.fail(properties, "未查询到有效的单据");
		}
		
		/*
		 * xsz by time 2018-11-13 15:29:54
		 * 根据审批状态判断是否提交
		 */
		if (S_ApprovalState.Examining.equals(empj_BldLimitAmount_AF.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该单据已在审核中，不可重复提交");
		}
		else if (S_ApprovalState.Completed.equals(empj_BldLimitAmount_AF.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该单据已审批完成，不可重复提交");
		}

		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		/*
		 * 如果未查询到审批流程，则直接审批通过
		 */
		// if (properties.getProperty("info").equals("noApproval"))
		if ("noApproval".equals(properties.getProperty("info")))
		{
			/*buildingAccountLogUtil.calculateWithoutApproval(empj_BldLimitAmount_AF.getBuilding(), tableId, BUSI_CODE, tgpj_BuildingAccountLog -> {
                setChanges(versionAfDtl, tgpj_BuildingAccountLog);
            });*/
            //备案人，备案日期，备案状态，审批状态
            empj_BldLimitAmount_AF.setApprovalState(S_ApprovalState.Completed);
            empj_BldLimitAmount_AF.setBusiState(S_BusiState.HaveRecord);
            empj_BldLimitAmount_AF.setUserRecord(model.getUser());
            empj_BldLimitAmount_AF.setRecordTimeStamp(System.currentTimeMillis());
            empj_BldLimitAmount_AFDao.save(empj_BldLimitAmount_AF);
		}
		else if ("fail".equals(properties.getProperty(S_NormalFlag.result)))
		{
			// 判断当前登录用户是否有权限发起审批
			return properties;
		}
		else
		{
			
			/**
			 * xsz by time 2019-7-15 09:47:10
			 * 提交前校验楼幢是否在做拨付
			 */
			Empj_BuildingInfo building = empj_BldLimitAmount_AF.getBuilding();
			
        	/*
        	 * xsz by time 2019-4-11 16:17:07
        	 * 正式提交前校验签章
        	 */
        	String isSign = model.getIsSign();
        	if(null == isSign)
        	{
        		isSign = "0";
        	}

        	if(!"1".equals(isSign)&&"2".equals(buttonType))
        	{
        		/*
                 * xsz by time 2019-1-19 10:44:26
                 * 审批操作结束后，生成发对应的pdf并检查是否有签章权限
                 */
                /*
    			 * 并将生成PDF后上传值OSS路径返回给前端
    			 * 
    			 * 参数：
    			 * sourceBusiCode：业务编码
    			 * sourceId：单据ID
    			 */
    			Sm_User user = model.getUser();
    			//登录人是否具有签章
    			if(null!=user.getIsSignature()&&"1".equals(user.getIsSignature()))
    			{
    				
    				ExportPdfForm pdfModel = new ExportPdfForm();
    				pdfModel.setSourceBusiCode(BUSI_CODE);
    				pdfModel.setSourceId(String.valueOf(tableId));
    				Properties executeProperties = exportPdfByWordService.execute(pdfModel);
    				String pdfUrl = (String) executeProperties.get("pdfUrl");
    				
    				Map<String, String> signatureMap = new HashMap<>();
    				
    				signatureMap.put("signaturePath", pdfUrl);
    				//TODO 此配置后期做成配置
    				signatureMap.put("signatureKeyword", "开发企业：");
    				signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());
    				
    				properties.put("signatureMap", signatureMap);
    				properties.put(S_NormalFlag.result, S_NormalFlag.success);
    		        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
    				
    				return properties;
    				
    			}
        	}
        	
            Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
                    .get("sm_approvalProcess_cfg");
            
            
            Tgpj_BldLimitAmountVer_AFDtl versionAfDtl = empj_BldLimitAmount_AF.getExpectFigureProgress();
            if (versionAfDtl == null) {
                return MyBackInfo.fail(properties, "变更额度不存在");
            }
            
            
    		Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
    		
            if (buildingAccount != null && model.getButtonType().equals(S_ButtonType.Submit)) {
                buildingAccountLogUtil.changeAndCaculate(building,tableId,BUSI_CODE,tgpj_BuildingAccountLog -> {
                    setChanges(versionAfDtl, tgpj_BuildingAccountLog);
                });
            }

            //审批操作
            sm_approvalProcessService.execute(empj_BldLimitAmount_AF, model, sm_approvalProcess_cfg);
        
		}
		
		empj_BldLimitAmount_AF.setUserUpdate(model.getUser());
	    empj_BldLimitAmount_AF.setLastUpdateTimeStamp(System.currentTimeMillis());
		empj_BldLimitAmount_AFDao.save(empj_BldLimitAmount_AF);

		return properties;
	}
	
	/**
	 * 是否存在PDF
	 * 
	 * @param sourceBusiCode
	 *            业务编码
	 * @param sourceId
	 *            业务数据Id
	 * @return
	 */
	Sm_Attachment isSaveAttachment(String sourceBusiCode, String sourceId)
	{
		//受限额度变更打印编码
		String attacmentcfg = "240107";
		
		Sm_AttachmentCfg sm_AttachmentCfg = isSaveAttachmentCfg(attacmentcfg);
		
		if (null == sm_AttachmentCfg)
		{
			return null;
		}
		
		Sm_AttachmentForm form = new Sm_AttachmentForm();
		form.setSourceId(sourceId);
		form.setBusiType(sourceBusiCode);
		form.setSourceType(sm_AttachmentCfg.geteCode());
		form.setTheState(S_TheState.Normal);

		Sm_Attachment attachment = attacmentDao
				.findOneByQuery_T(attacmentDao.getQuery(attacmentDao.getBasicHQL(), form));

		if (null == attachment)
		{
			return null;
		}
		return attachment;
	}
	
	/**
	 * 是否进行档案配置
	 * 
	 * @param attacmentcfg
	 *            档案类型编码
	 * @return
	 */
	Sm_AttachmentCfg isSaveAttachmentCfg(String attacmentcfg)
	{
		// 根据业务编号查询配置文件
		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setTheState(S_TheState.Normal);
		form.setBusiType(attacmentcfg);

		Sm_AttachmentCfg sm_AttachmentCfg = attacmentcfgDao
				.findOneByQuery_T(attacmentcfgDao.getQuery(attacmentcfgDao.getBasicHQL(), form));

		if (null == sm_AttachmentCfg)
		{
			return null;
		}
		return sm_AttachmentCfg;
	}
	
	private void setChanges(Tgpj_BldLimitAmountVer_AFDtl versionAfDtl, Tgpj_BuildingAccountLog tgpj_buildingAccountLog) {
        tgpj_buildingAccountLog.setCurrentFigureProgress(versionAfDtl.getStageName());
        tgpj_buildingAccountLog.setCurrentLimitedRatio(versionAfDtl.getLimitedAmount());
        tgpj_buildingAccountLog.setBldLimitAmountVerDtl(versionAfDtl);
    }
}
