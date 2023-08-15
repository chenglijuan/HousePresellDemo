package zhishusz.housepresell.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Service提交操作:提交 托管合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_EscrowAgreementApprovalProcessService
{
	// 贷款托管合作协议
	private static final String BUSI_CODE = "06110201";
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// eCode生成规则
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;// 楼幢账户
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDao tgpj_BldLimitAmountVer_AFDao;// 版本管理-受限节点设置(主表)
	@Autowired
	private Tgpj_BldLimitAmountVer_AFDtlDao tgpj_BldLimitAmountVer_AFDtlDao;// 受限额度设置(子表)
	@Autowired
	private Tgpj_EscrowStandardVerMngDao tgpj_EscrowStandardVerMngDao;// 托管标准
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;// 楼幢拓展表
	@Autowired
	private ExportPdfByWordService exportPdfByWordService;//生成PDF
	@Autowired
	private Sm_AttachmentDao attacmentDao;
	@Autowired
	private Sm_AttachmentCfgDao attacmentcfgDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_EscrowAgreementForm model)
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
		Long userStartId = model.getUserId(); // 登录用户id TODO

		model.setButtonType(buttonType);

		if (busiCode == null || busiCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'业务编码'不能为空");
		}

		if (tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择有效的合作协议");
		}

		Tgxy_EscrowAgreement escrowAgreement = tgxy_EscrowAgreementDao.findById(tableId);

		if (null == escrowAgreement)
		{
			return MyBackInfo.fail(properties, "未查询到有效的合作协议");
		}

		/*
		 * xsz by time 2018-11-13 15:29:54
		 * 根据审批状态判断是否提交
		 */
		if (S_ApprovalState.Examining.equals(escrowAgreement.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
		}
		else if (S_ApprovalState.Completed.equals(escrowAgreement.getApprovalState()))
		{
			return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
		}

		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		/*
		 * 如果未查询到审批流程，则直接审批通过
		 */
		// if (properties.getProperty("info").equals("noApproval"))
		if ("noApproval".equals(properties.getProperty("info")))
		{
			// 提交审批后状态置为提交态（7-已备案）
			escrowAgreement.setBusinessProcessState("7");
			// 审批流程状态
			escrowAgreement.setApprovalState(S_ApprovalState.Completed);
			// 获取签署合作协议的楼幢信息
			List<Empj_BuildingInfo> buildingInfoList;
			buildingInfoList = escrowAgreement.getBuildingInfoList();

			if (null == buildingInfoList || buildingInfoList.size() < 1)
			{
				// 未查询到提示
				// return MyBackInfo.fail(properties, "未查询到签署合作协议的楼幢信息");
			}
			else
			{
				for (Empj_BuildingInfo po : buildingInfoList)
				{
					po.setBusiState(S_BusiState.HaveRecord);// 楼幢状态置为备案状态
					empj_BuildingInfoDao.save(po);

					// 查询楼幢账户表
					Tgpj_BuildingAccountForm model1 = new Tgpj_BuildingAccountForm();
					model1.setTheState(S_TheState.Normal);
					model1.setBuilding(po);
					Tgpj_BuildingAccount tgpj_BuildingAccount;
					tgpj_BuildingAccount = (Tgpj_BuildingAccount) tgpj_BuildingAccountDao.findOneByQuery(
							tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), model1));
					if (null == tgpj_BuildingAccount)
					{
						return MyBackInfo.fail(properties, "未查询到相关楼幢账户信息");
					}

					// 楼幢交付类型
					String theType = po.getDeliveryType();
					if (theType == null)
					{
						return MyBackInfo.fail(properties, "楼幢类型未选择，操作失败");
					}

					// 根据交付类型查询受限额度版本信息
					Tgpj_BldLimitAmountVer_AF nowLimitAmountVer = tgpj_BldLimitAmountVer_AFDao
							.getNowLimitAmountVer(theType);
					if (nowLimitAmountVer == null)
					{
						return MyBackInfo.fail(properties, "目前没有对应的受限额度版本信息");
					}

					// 根据受限额度版本查询节点信息，找到节点为100%的，并关联保存
					Long limitAmountVerId = nowLimitAmountVer.getTableId();
					Tgpj_BldLimitAmountVer_AFDtlForm tgpj_bldLimitAmountVer_afDtlForm = new Tgpj_BldLimitAmountVer_AFDtlForm();
					tgpj_bldLimitAmountVer_afDtlForm.setBldLimitAmountVerMngId(limitAmountVerId);
					tgpj_bldLimitAmountVer_afDtlForm.setOrderBy("limitedAmount asc");
					tgpj_bldLimitAmountVer_afDtlForm.setTheState(S_TheState.Normal);
					List<Tgpj_BldLimitAmountVer_AFDtl> list = tgpj_BldLimitAmountVer_AFDtlDao.findByPage(
							tgpj_BldLimitAmountVer_AFDtlDao.getQuery(tgpj_BldLimitAmountVer_AFDtlDao.getBasicHQL(),
									tgpj_bldLimitAmountVer_afDtlForm));

					if (null == list || list.size() <= 0)
					{
						return MyBackInfo.fail(properties, "目前没有对应的受限额度版本节点信息");
					}
					else
					{
						boolean flag = true;

						for (Tgpj_BldLimitAmountVer_AFDtl orginDtl : list)
						{
							// 如果找到有对应的100%，则将值存入关联
							if (orginDtl.getLimitedAmount() == 100.00)
							{

								// 关联ecode
								// tgpj_BuildingAccount.setCurrentFigureProgress(orginDtl.geteCode());
								/*
								 * xsz by time 2018-11-12 11:13:09
								 * 关联节点对象
								 */
								tgpj_BuildingAccount.setBldLimitAmountVerDtl(orginDtl);
								tgpj_BuildingAccount.setCurrentFigureProgress(orginDtl.getStageName());
								tgpj_BuildingAccount.setCurrentLimitedRatio(100.00);
								flag = false;
								break;

							}
						}

						// 没有找到对应的100%的加点，返回提示信息
						if (flag)
						{
							return MyBackInfo.fail(properties, "没有找到初始受限额度节点信息");
						}

					}

					/*
					 * xsz by time 2018-11-11 10:28:57
					 * 根据楼幢的交付类型查找形象进度，再存储形象进度为100%的ecode关联
					 * ====================end============================
					 */

					/*
					 * 计算初始受限额度
					 * 楼幢住宅备案均价*托管面积*50%==>(托管标准)（目前政策）
					 */

					/*
					 * xsz by time 2018-11-11 10:59:58
					 * 
					 * 计算初始受限额度
					 * 根据托管标准类型分开计算
					 * 托管面积：从楼幢主表中获取
					 * 
					 * ====================start=========================
					 */

					// 托管面积
					Double escrowArea = 0.00;
					escrowArea = po.getEscrowArea();
					// 楼幢住宅备案均价
					Double priceOfBuilding = 0.00;
					priceOfBuilding = tgpj_BuildingAccount.getRecordAvgPriceOfBuilding();

					Tgpj_EscrowStandardVerMng escrowStandardVerMng = po.getEscrowStandardVerMng();
					// 托管标准类型 (枚举选择:1-标准金额 2-标准比例)
					if (S_EscrowStandardType.StandardAmount.equals(escrowStandardVerMng.getTheType()))
					{
						if (null == escrowStandardVerMng.getAmount())
						{
							tgpj_BuildingAccount.setOrgLimitedAmount(0.00);
						}
						else
						{

							// 初始受限额度 = 托管面积 * 标准金额(如果其中有一个值为0，则保存为0)
							if (escrowArea <= 0.00 || escrowStandardVerMng.getAmount() <= 0.00)
								tgpj_BuildingAccount.setOrgLimitedAmount(0.00);

							Double orgLimitedAmount = MyDouble.getInstance().doubleMultiplyDouble(escrowArea,
									escrowStandardVerMng.getAmount());
							tgpj_BuildingAccount.setOrgLimitedAmount(orgLimitedAmount);

						}
					}
					else if (S_EscrowStandardType.StandardPercentage.equals(escrowStandardVerMng.getTheType()))
					{
						// 初始受限额度 = 托管面积 * 楼幢备案均价 * 受限比例
						if (null == escrowStandardVerMng.getPercentage())
						{
							tgpj_BuildingAccount.setOrgLimitedAmount(0.00);
						}
						else
						{
							// 初始受限额度 = 托管面积 * 楼幢备案均价 * 受限比例(如果有一个值为0，则直接为0)
							if (priceOfBuilding <= 0 || escrowArea <= 0 || escrowStandardVerMng.getPercentage() <= 0)
								tgpj_BuildingAccount.setOrgLimitedAmount(0.00);
							/*
							 * 此计算作为后期优化放开
							 * Double double1 =
							 * MyDouble.getInstance().doubleMultiplyDouble(
							 * priceOfBuilding,
							 * escrowArea);
							 * Double double2 =
							 * MyDouble.getInstance().doubleMultiplyDouble(
							 * double1,
							 * escrowStandardVerMng.getPercentage() / 100);
							 * 
							 */
							Double double2 = escrowArea * escrowStandardVerMng.getPercentage() * priceOfBuilding / 100;

							tgpj_BuildingAccount.setOrgLimitedAmount(double2);

						}

					}

					/*
					 * xsz by time 2018-11-11 10:59:58
					 * 
					 * 计算初始受限额度
					 * 根据托管标准类型分开计算
					 * 托管面积：从楼幢主表中获取
					 * 
					 * ====================end=========================
					 */

					// 修改版本号
					Long versionNo = tgpj_BuildingAccount.getVersionNo();
					if (null == versionNo || versionNo == 0)
					{
						tgpj_BuildingAccount.setVersionNo(1L);
					}
					else
					{
						tgpj_BuildingAccount.setVersionNo(versionNo + 1);
					}

					// 保存
					tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);

					// 更新楼幢的托管状态(已托管)
					Empj_BuildingExtendInfo extendInfo = po.getExtendInfo();

					if (null != extendInfo)
					{
						extendInfo.setEscrowState(S_EscrowState.HasEscrowState);

						empj_BuildingExtendInfoDao.save(extendInfo);
					}

				}
			}

			// 处理完成之后返回成功信息
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			
			
		}
		else if ("fail".equals(properties.getProperty(S_NormalFlag.result)))
		{
			// 判断当前登录用户是否有权限发起审批
			return properties;
		}
		else
		{

			// 提交前校验是否可以提交
			// 获取签署合作协议的楼幢信息
			List<Empj_BuildingInfo> buildingInfoList;
			buildingInfoList = escrowAgreement.getBuildingInfoList();
			if (null == buildingInfoList || buildingInfoList.size() == 0)
			{
				return MyBackInfo.fail(properties, "该信息未选择需要托管的楼幢");
			}
			
			for (Empj_BuildingInfo empj_BuildingInfo : buildingInfoList)
			{
				// 楼幢交付类型
				String theType = empj_BuildingInfo.getDeliveryType();
				if (theType == null)
				{
					return MyBackInfo.fail(properties, "楼幢类型未选择，操作失败");
				}

				// 根据交付类型查询受限额度版本信息
				Tgpj_BldLimitAmountVer_AF nowLimitAmountVer = tgpj_BldLimitAmountVer_AFDao
						.getNowLimitAmountVer(theType);
				if (nowLimitAmountVer == null)
				{
					return MyBackInfo.fail(properties, "目前没有对应的受限额度版本信息");
				}
			}
			
			
			/*
			 * xsz by time 2019-4-11 14:17:45
			 * 提交之前判断是否过滤签章
			 * isSign == 1  过滤签章请求
			 */
			String isSign = model.getIsSign();
			if(null == isSign)
			{
				isSign = "0";
			}
			if(!"1".equals(isSign))
			{
				/*
				 * xsz by time 提交结束后调用生成PDF方法
				 * 并将生成PDF后上传值OSS路径返回给前端
				 * 
				 * 参数：
				 * sourceBusiCode：业务编码
				 * sourceId：单据ID
				 * 
				 * xsz by time 2019-3-11 19:28:10
				 * 2.0
				 * 每次点击提交时，重新生成新的协议pdf
				 * 
				 * xsz by time 2019-4-11 14:20:10
				 * 3.0
				 * 在正式提交之前先进行签章
				 * 
				 */
				Sm_User user = model.getUser();
				if(null!=user.getIsSignature()&&"1".equals(user.getIsSignature()))
				{
					// 查询是否已经存在PDF附件
					Sm_Attachment attachment = isSaveAttachment(busiCode, String.valueOf(tableId));
					if (null != attachment)
					{
						//如果存在附件，置为删除态重新生成
						attachment.setTheState(S_TheState.Deleted);
//						attacmentDao.save(attachment);
						attacmentDao.delete(attachment);
					}
					
					ExportPdfForm pdfModel = new ExportPdfForm();
					pdfModel.setSourceBusiCode(busiCode);
					pdfModel.setSourceId(String.valueOf(tableId));
					Properties executeProperties = exportPdfByWordService.execute(pdfModel);
					String pdfUrl = (String) executeProperties.get("pdfUrl");
					
					Map<String, String> signatureMap = new HashMap<>();
					
					signatureMap.put("signaturePath", pdfUrl);
					//TODO 此配置后期做成配置
					signatureMap.put("signatureKeyword", "甲方（盖章）");
					signatureMap.put("ukeyNumber", model.getUser().getUkeyNumber());
					
					properties.put("signatureMap", signatureMap);
					
					return properties;
				}
			}

			// 判断当前登录用户是否有权限发起审批
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
					.get("sm_approvalProcess_cfg");

			// 审批操作
			properties = sm_approvalProcessService.execute(escrowAgreement, model, sm_approvalProcess_cfg);

			// 审批流程状态
			escrowAgreement.setApprovalState(S_ApprovalState.Examining);
			// 提交审批后状态置为提交态（2-已申请（提交））
			escrowAgreement.setBusinessProcessState("2");
			
			tgxy_EscrowAgreementDao.buildingPREsale(tableId);
			
		}

		tgxy_EscrowAgreementDao.save(escrowAgreement);

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
		//合作协议打印编码
		String attacmentcfg = "240104";
		
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
