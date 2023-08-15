package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Empj_BldEscrowCompletedForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompletedDao;
import zhishusz.housepresell.database.dao.Empj_BldEscrowCompleted_DtlDao;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted;
import zhishusz.housepresell.database.po.Empj_BldEscrowCompleted_Dtl;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.project.IsNeedBackupUtil;

/*
 * Service详情：申请表-项目托管终止（审批）-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BldEscrowCompletedDetailService
{
	@Autowired
	private Empj_BldEscrowCompletedDao empj_BldEscrowCompletedDao;
	@Autowired
	private Empj_BldEscrowCompleted_DtlDao empj_BldEscrowCompleted_DtlDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao approvalProcessAFDao;
	@Autowired
	private IsNeedBackupUtil isNeedBackupUtil;
	@Autowired
	private Sm_AttachmentDao attacmentDao;
	@Autowired
	private Sm_AttachmentCfgDao attacmentcfgDao;
	@Autowired
	private ExportPdfByWordService exportPdfByWordService;
	
	@Autowired
	private CommonService commonService;

	public Properties execute(Empj_BldEscrowCompletedForm model)
	{
		Properties properties = new MyProperties();
		//优化后：前端传值主表ID，直接查询主表-->明细表
		Long empj_BldEscrowCompletedId = model.getTableId();
		if (empj_BldEscrowCompletedId == null || empj_BldEscrowCompletedId < 1)
		{
			return MyBackInfo.fail(properties, "托管终止不存在");
		}
		Empj_BldEscrowCompleted empj_BldEscrowCompleted =
				(Empj_BldEscrowCompleted)empj_BldEscrowCompletedDao.findById(empj_BldEscrowCompletedId);
		if(empj_BldEscrowCompleted == null)
		{
			return MyBackInfo.fail(properties, "托管终止不存在");
		}

		//楼幢信息
		List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList = empj_BldEscrowCompleted.getEmpj_BldEscrowCompleted_DtlList();
		if (empj_BldEscrowCompleted_DtlList == null)
		{
			empj_BldEscrowCompleted_DtlList = new ArrayList<>();
		}

		if (model.getAfId() != null)
		{
			Sm_ApprovalProcess_AF byId = approvalProcessAFDao.findById(model.getAfId());
			isNeedBackupUtil.setIsNeedBackup(properties,byId);
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_BldEscrowCompleted", empj_BldEscrowCompleted);
		properties.put("empj_BldEscrowCompleted_DtlList", empj_BldEscrowCompleted_DtlList);

		return properties;
	}
	
	
	public Properties pushExecute(Empj_BldEscrowCompletedForm model)
	{
		Properties properties = new MyProperties();
		//优化后：前端传值主表ID，直接查询主表-->明细表
		Long empj_BldEscrowCompletedId = model.getTableId();
		if (empj_BldEscrowCompletedId == null || empj_BldEscrowCompletedId < 1)
		{
			return MyBackInfo.fail(properties, "托管终止不存在");
		}
		Empj_BldEscrowCompleted empj_BldEscrowCompleted =
				(Empj_BldEscrowCompleted)empj_BldEscrowCompletedDao.findById(empj_BldEscrowCompletedId);
		if(empj_BldEscrowCompleted == null)
		{
			return MyBackInfo.fail(properties, "托管终止不存在");
		}
		
		
		String busiState = empj_BldEscrowCompleted.getBusiState();
		if(!"已备案".equals(busiState)){
			return MyBackInfo.fail(properties, "该单据还未备案！");
		}
		
		if(!"1".equals(empj_BldEscrowCompleted.getHasFormula())){
			return MyBackInfo.fail(properties, "该单据还未公示！");
		}
		
		if(StrUtil.isBlank(empj_BldEscrowCompleted.getWebSite())){
			return MyBackInfo.fail(properties, "该单据未维护公示网址！");
		}
		
		boolean pushBldEscrowCompleted = commonService.pushBldEscrowCompleted(empj_BldEscrowCompleted);
		if(pushBldEscrowCompleted){
			
			empj_BldEscrowCompleted.setHasPush("1");
			empj_BldEscrowCompletedDao.update(empj_BldEscrowCompleted);
			
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		}else{
			return MyBackInfo.fail(properties, "推送失败！");
		}

		return properties;
	}
	
	public Properties signExecute(Empj_BldEscrowCompletedForm model)
	{
		Properties properties = new MyProperties();
		//优化后：前端传值主表ID，直接查询主表-->明细表
		Long mainTableId = model.getTableId();
		
		//生成公安-施工编号对照表
    	// 查询是否已经存在PDF附件
		Sm_Attachment attachmentBak = isSaveAttachment2("240180", String.valueOf(mainTableId));
		if (null != attachmentBak)
		{
			//如果存在附件，置为删除态重新生成
			attachmentBak.setTheState(S_TheState.Deleted);
			attacmentDao.save(attachmentBak);
			
		}
		
		ExportPdfForm pdfModel = new ExportPdfForm();
		pdfModel.setSourceBusiCode("240180");
		pdfModel.setSourceId(String.valueOf(mainTableId));
		Properties executeProperties = exportPdfByWordService.execute(pdfModel);
		if(!S_NormalFlag.success.equals(executeProperties.get(S_NormalFlag.result))){
			return executeProperties;
		}
		//生成公安-施工编号对照表
		
		// 查询是否已经存在PDF附件
		Sm_Attachment attachment = isSaveAttachmentBak("240180", String.valueOf(mainTableId));
		if (null != attachment)
		{
			Map<String, String> signatureMap = new HashMap<>();
			
			signatureMap.put("signaturePath", attachment.getTheLink());
			//TODO 此配置后期做成配置
			signatureMap.put("signatureKeyword", "开发企业（");
			System.out.println(signatureMap.get("signatureKeyword"));
			signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());
			
			properties.put("signatureMap", signatureMap);
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			
		}else{
			return MyBackInfo.fail(properties, "签章失败，请稍后重试！");
		}
		
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
	Sm_Attachment isSaveAttachmentBak(String sourceBusiCode, String sourceId)
	{
		//托管终止打印编码
		String attacmentcfg = "240180";
		
		Sm_AttachmentCfg sm_AttachmentCfg = isSaveAttachmentCfgBak(attacmentcfg);
		
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
	Sm_AttachmentCfg isSaveAttachmentCfgBak(String attacmentcfg)
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
		//托管终止打印编码
		String attacmentcfg = "240108";
		
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
	 * 是否存在PDF
	 * 
	 * @param sourceBusiCode
	 *            业务编码
	 * @param sourceId
	 *            业务数据Id
	 * @return
	 */
	Sm_Attachment isSaveAttachment2(String sourceBusiCode, String sourceId)
	{
		//托管终止打印编码
		String attacmentcfg = "240180";
		
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
	
	
}
