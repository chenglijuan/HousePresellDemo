package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDtlDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service更新操作：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementSettleSubmitService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private ExportPdfByWordService exportPdfByWordService;//生成PDF
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{
		Properties properties = new MyProperties();	

		model.setButtonType("2"); //1： 保存按钮  2：提交按钮
		String busiCode = "06110304";
		Long tableId = model.getTableId();
		Long userStartId = model.getUserId();
		Sm_User user = model.getUser();

		if(busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'业务编码'不能为空");
		}

		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "未查询到有效的三方协议计量结算信息！");
		}

		Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = (Tgxy_CoopAgreementSettle)tgxy_CoopAgreementSettleDao.findById(tableId);
		if(tgxy_CoopAgreementSettle == null)
		{
			return MyBackInfo.fail(properties, "该记录不存在！");
		}
		
		if (S_ApprovalState.Examining.equals(tgxy_CoopAgreementSettle.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
		}
		else if (S_ApprovalState.Completed.equals(tgxy_CoopAgreementSettle.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
		}
		
//		tgxy_CoopAgreementSettle.setSettlementState(1);
		
		
		
		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		
		
		if (properties.getProperty("info").equals("noApproval"))
		{
			tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);	
			
			tgxy_CoopAgreementSettle.setApprovalState(S_ApprovalState.Completed);
			tgxy_CoopAgreementSettle.setTheState(S_TheState.Normal);
			tgxy_CoopAgreementSettle.setBusiState(S_BusiState.HaveRecord);
			tgxy_CoopAgreementSettle.setUserUpdate(user);
			tgxy_CoopAgreementSettle.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgxy_CoopAgreementSettle.setUserRecord(user);
			tgxy_CoopAgreementSettle.setRecordTimeStamp(System.currentTimeMillis());
			tgxy_CoopAgreementSettle.setSignTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));
			tgxy_CoopAgreementSettle.setSettlementState(2);
		
			tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);	
			
			// 查询子表
			Tgxy_CoopAgreementSettleDtlForm tgxy_CoopAgreementSettleDtlForm = new Tgxy_CoopAgreementSettleDtlForm();
			
			Integer totalCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));
			
			// 子表
			List<Tgxy_CoopAgreementSettleDtl> tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();		
			if(totalCount > 0)
			{
				tgxy_CoopAgreementSettleDtlList = tgxy_CoopAgreementSettleDtlDao.findByPage(tgxy_CoopAgreementSettleDtlDao.getQuery(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));	
				for(Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl : tgxy_CoopAgreementSettleDtlList)
				{
					
					Empj_HouseInfo empj_HouseInfo = tgxy_CoopAgreementSettleDtl.getHouseInfo();
	
					empj_HouseInfo.setUserRecord(user);
					empj_HouseInfo.setUserUpdate(user);
					empj_HouseInfo.setRecordTimeStamp(System.currentTimeMillis());
					empj_HouseInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
					empj_HouseInfo.setSettlementStateOfTripleAgreement(1);
					
					empj_HouseInfoDao.save(empj_HouseInfo);
				}		
			}
			else
			{
				tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
			}

		}
		else
		{
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
					.get("sm_approvalProcess_cfg");	
			
			if( null == sm_approvalProcess_cfg)
			{
				return MyBackInfo.fail(properties, "没有审批权限");
			}
			
			tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);	
			
			tgxy_CoopAgreementSettle.setUserUpdate(user);
			tgxy_CoopAgreementSettle.setLastUpdateTimeStamp(System.currentTimeMillis());
			tgxy_CoopAgreementSettle.setSettlementState(1);
			tgxy_CoopAgreementSettle.setApprovalState(S_ApprovalState.Examining);
			tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);	
			
		
			//审批操作
			sm_approvalProcessService.execute(tgxy_CoopAgreementSettle, model, sm_approvalProcess_cfg);
			
			/*
			 * xsz by time 提交结束后调用生成PDF方法
			 * 并将生成PDF后上传值OSS路径返回给前端
			 * 
			 * 参数：
			 * sourceBusiCode：业务编码
			 * sourceId：单据ID
			 */
			if(null!=user.getIsSignature()&&"1".equals(user.getIsSignature()))
			{
				
				ExportPdfForm pdfModel = new ExportPdfForm();
				pdfModel.setSourceBusiCode(busiCode);
				pdfModel.setSourceId(String.valueOf(tableId));
				Properties executeProperties = exportPdfByWordService.execute(pdfModel);
				String pdfUrl = (String) executeProperties.get("pdfUrl");
				
				Map<String, String> signatureMap = new HashMap<>();
				
				signatureMap.put("signaturePath", pdfUrl);
				//TODO 此配置后期做成配置
				signatureMap.put("signatureKeyword", "代理公司：");
				signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());
				
				properties.put("signatureMap", signatureMap);
				
			}
			
		}
		
		//审批操作
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
