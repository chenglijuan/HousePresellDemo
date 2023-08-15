package zhishusz.housepresell.approvalprocess;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgpj_BldLimitAmountVer_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDao;
import zhishusz.housepresell.database.dao.Tgpj_BldLimitAmountVer_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgpj_EscrowStandardVerMngDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowStandardType;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_WorkflowBusiState;
import zhishusz.housepresell.database.po.toInterface.To_building;
import zhishusz.housepresell.external.service.Tgxy_EscrowAgreementInsertInterfaceService;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;
import zhishusz.housepresell.service.pdf.ExportPdfByWordService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.ToInterface;

/**
 * 贷款托管合作协议签署：
 * 审批过后-业务逻辑处理
 * 2018-10-11 17:02:28
 * 
 * @author ZS_XSZ
 *
 */
@Transactional
public class ApprovalProcessCallBack_06110201 implements IApprovalProcessCallback
{
	private static final Double DELIVERYTYPE1 = 4000.00;// 毛坯房
	private static final Double DELIVERYTYPE2 = 6000.00;// 成品房
	// 贷款托管合作协议
	private static final String BUSI_CODE = "06110201";
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;
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
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// eCode生成规则
	@Autowired
	private ExportPdfByWordService exportPdfByWordService;// 生成PDF
	@Autowired
	private Tgxy_EscrowAgreementInsertInterfaceService insertInterfaceService; 
	@Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;

	@SuppressWarnings("unchecked")
	@Override
	public Properties execute(Sm_ApprovalProcess_Workflow approvalProcessWorkflow, BaseForm baseForm)
	{
		Properties properties = new MyProperties();

		// 获取当前操作人（备案人）
		Sm_User user = baseForm.getUser();

		try
		{
			String workflowEcode = approvalProcessWorkflow.geteCode();
			// String workflowName = approvalProcessWorkflow.getTheName();

			// 获取正在处理的申请单
			Sm_ApprovalProcess_AF approvalProcess_AF = approvalProcessWorkflow.getApprovalProcess_AF();

			// 获取正在处理的申请单所属的流程配置
			Sm_ApprovalProcess_Cfg sm_ApprovalProcess_Cfg = approvalProcess_AF.getConfiguration();
			String approvalProcessWork = sm_ApprovalProcess_Cfg.geteCode() + "_" + workflowEcode;

			// 获取正在审批的合作协议
			Long sourceId = approvalProcess_AF.getSourceId();

			if (null == sourceId || sourceId < 1)
			{
				return MyBackInfo.fail(properties, "获取的申请单主键为空");
			}

			// 合作协议
			Tgxy_EscrowAgreement escrowAgreement;
			escrowAgreement = tgxy_EscrowAgreementDao.findById(sourceId);
			if (null == escrowAgreement)
			{
				return MyBackInfo.fail(properties, "该合作协议已处于失效状态，请刷新后重试");
			}

			// 驳回到发起人，发起人撤回
			if (S_ApprovalState.WaitSubmit.equals(approvalProcess_AF.getBusiState()))
			{
				escrowAgreement.setBusinessProcessState("1");
				escrowAgreement.setApprovalState(S_ApprovalState.WaitSubmit);

				tgxy_EscrowAgreementDao.save(escrowAgreement);

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");
			}

			// 不通过
			if (S_ApprovalState.NoPass.equals(approvalProcess_AF.getBusiState()))
			{
				escrowAgreement.setBusinessProcessState("1");
				escrowAgreement.setApprovalState(S_ApprovalState.NoPass);

				tgxy_EscrowAgreementDao.save(escrowAgreement);

				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "操作成功");
			}

			switch (approvalProcessWork)
			{
			// 审批节点名称
			case "06110201001_2":

				/*
				 * 具体的业务逻辑操作
				 */
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有进行处理的回调");

				/*
				 * xsz by time 提交结束后调用生成PDF方法
				 * 并将生成PDF后上传值OSS路径返回给前端
				 * 
				 * 参数：
				 * sourceBusiCode：业务编码
				 * sourceId：单据ID
				 */
				// ExportPdfForm pdfModel = new ExportPdfForm();
				// pdfModel.setSourceBusiCode(BUSI_CODE);
				// pdfModel.setSourceId(String.valueOf(sourceId));
				// Properties executeProperties =
				// exportPdfByWordService.execute(pdfModel);
				// String pdfUrl = (String) executeProperties.get("pdfUrl");
				//
				// Map<String, String> signatureMap = new HashMap<>();
				//
				// signatureMap.put("signaturePath", pdfUrl);
				// //TODO 此配置后期做成配置
				// signatureMap.put("signatureKeyword", "乙方（盖章）");
				// signatureMap.put("ukeyNumber", user.getUkeyNumber());
				//
				// properties.put("signatureMap", signatureMap);

				break;

			// case "06110201001_4":
			case "06110201001_ZS":

				/*
				 * 贷款托管合作协议审批完成：
				 * 进行托管的楼幢状态置为已备案状态Empj_BuildingInfo（busiState=1）
				 * 
				 * 同时更新 楼幢账户（Tgpj_BuildingAccount）中如下信息：
				 * 
				 * currentFigureProgress（当前形象进度）
				 * currentLimitedRatio（当前受限比例（%））
				 * ==>根据合作协议签约申请日期查询
				 * 
				 * escrowStandard（托管标准）
				 * ==>根据合作协议签约申请日期查询
				 * 
				 * orgLimitedAmount（初始受限额度）
				 * =楼幢住宅备案均价*托管面积*40%==>(托管标准)（目前政策）
				 * =3500*托管面积（原先毛坯房政策，不再使用）
				 */

				/*
				 * 审批通过后的具体的业务逻辑操作
				 */
				if (S_ApprovalState.Completed.equals(approvalProcess_AF.getBusiState())
						&& S_WorkflowBusiState.Completed.equals(approvalProcessWorkflow.getBusiState()))
				{
					
					/**
					 * xsz by time 2019-7-8 17:17:43
					 * 与档案系统接口对接
					 * ====================start==================
					 */
					
					Properties execute = insertInterfaceService.execute(escrowAgreement, baseForm);
					if(execute.isEmpty()|| S_NormalFlag.fail.equals(execute.get(S_NormalFlag.result)))
					{
						return MyBackInfo.fail(properties, "与档案系统对接失败，请稍后重试！");
					}
					
					/**
					 * xsz by time 2019-7-8 17:17:43
					 * 与档案系统接口对接
					 * ====================end==================
					 */
					
					// 获取签署合作协议的楼幢信息
					List<Empj_BuildingInfo> buildingInfoList;
					buildingInfoList = escrowAgreement.getBuildingInfoList();

					if (null == buildingInfoList || buildingInfoList.size() < 1)
					{
						// 未查询到提示
						// return MyBackInfo.fail(properties,
						// "未查询到签署合作协议的楼幢信息");
					}
					else
					{
						for (Empj_BuildingInfo po : buildingInfoList)
						{
							po.setBusiState(S_BusiState.HaveRecord);// 楼幢状态置为备案状态
							empj_BuildingInfoDao.save(po);

							// 查询楼幢账户表
							Tgpj_BuildingAccountForm model = new Tgpj_BuildingAccountForm();
							model.setTheState(S_TheState.Normal);
							model.setBuilding(po);
							Tgpj_BuildingAccount tgpj_BuildingAccount;
							tgpj_BuildingAccount = (Tgpj_BuildingAccount) tgpj_BuildingAccountDao.findOneByQuery(
									tgpj_BuildingAccountDao.getQuery(tgpj_BuildingAccountDao.getBasicHQL(), model));
							if (null == tgpj_BuildingAccount)
							{
								return MyBackInfo.fail(properties, "未查询到相关楼幢账户信息");
							}

							Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerDtl = tgpj_BuildingAccount.getBldLimitAmountVerDtl();
							if(null == bldLimitAmountVerDtl)
							{
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
								List<Tgpj_BldLimitAmountVer_AFDtl> list = tgpj_BldLimitAmountVer_AFDtlDao
										.findByPage(tgpj_BldLimitAmountVer_AFDtlDao.getQuery(
												tgpj_BldLimitAmountVer_AFDtlDao.getBasicHQL(),
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
											tgpj_BuildingAccount.setBldLimitAmountVer_AF(orginDtl.getBldLimitAmountVerMng());
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

								// 托管面积
								Double escrowArea = 0.00;
								escrowArea = po.getEscrowArea();
								// 楼幢住宅备案均价
								Double priceOfBuilding = 0.00;
								priceOfBuilding = tgpj_BuildingAccount.getRecordAvgPriceOfBuilding();
								if (null == priceOfBuilding)
								{
									priceOfBuilding = 0.00;
								}

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
										// 初始受限额度 = 托管面积 * 楼幢备案均价 *
										// 受限比例(如果有一个值为0，则直接为0)
										if (priceOfBuilding <= 0 || escrowArea <= 0
												|| escrowStandardVerMng.getPercentage() <= 0)
											tgpj_BuildingAccount.setOrgLimitedAmount(0.00);
										/*
										 * 此计算作为后期优化放开
										 * Double double1 =
										 * MyDouble.getInstance().
										 * doubleMultiplyDouble(
										 * priceOfBuilding,
										 * escrowArea);
										 * Double double2 =
										 * MyDouble.getInstance().
										 * doubleMultiplyDouble(
										 * double1,
										 * escrowStandardVerMng.getPercentage() /
										 * 100);
										 * 
										 */

										/*
										 * 标准比例为30%时，计算 楼幢备案均价 * 受限比例
										 * 楼幢为成品房 6000 2 deliveryType
										 * 楼幢为毛坯房 4000 1 deliveryType
										 */
										Double percentage = escrowStandardVerMng.getPercentage();// 比例
										Double doubleTage = percentage * priceOfBuilding / 100;// 楼幢备案均价
																								// *
																								// 受限比例
										Double double2 = 0.00;// 初始受限额度
										if ((percentage - 30) == 0)
										{
											theType = po.getDeliveryType();
											if ("1".equals(theType))
											{// 毛坯房
												if (DELIVERYTYPE1 - doubleTage < 0)
												{
													double2 = escrowArea * DELIVERYTYPE1;
												}
												else
												{
													double2 = escrowArea * doubleTage;
												}
											}
											else
											{// 成品房
												if (DELIVERYTYPE2 - doubleTage < 0)
												{
													double2 = escrowArea * DELIVERYTYPE2;
												}
												else
												{
													double2 = escrowArea * doubleTage;
												}
											}
										}
										else
										{
											double2 = escrowArea * percentage * priceOfBuilding / 100;
										}

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
							}
							
							// 更新楼幢的托管状态(已托管)
							Empj_BuildingExtendInfo extendInfo = po.getExtendInfo();

							if (null != extendInfo)
							{
								extendInfo.setEscrowState(S_EscrowState.HasEscrowState);

								empj_BuildingExtendInfoDao.save(extendInfo);
							}

							// 推送数据到门户网站
							/*Boolean interFaceAction = toInterFaceAction(po, po.geteCode(),
									escrowAgreement.getContractApplicationDate());*/
							Boolean interFaceAction = toInterFaceAction(po, String.valueOf(po.getTableId()),
									escrowAgreement.getContractApplicationDate());

							if (!interFaceAction)
							{
								return MyBackInfo.fail(properties, "消息推送门户网站失败！");
							}
						}
					}

					// escrowAgreement.seteCodeOfAgreement(sm_BusinessCodeGetService.execute(BUSI_CODE));
					
					// 备案时间
					escrowAgreement.setRecordTimeStamp(System.currentTimeMillis());
					// 设置备案人
					escrowAgreement.setUserRecord(user);
					// 协议状态 7-已备案
					escrowAgreement.setBusinessProcessState("7");
					// 审批流程状态
					escrowAgreement.setApprovalState(S_ApprovalState.Completed);

					tgxy_EscrowAgreementDao.save(escrowAgreement);

					properties.put(S_NormalFlag.result, S_NormalFlag.success);
					properties.put(S_NormalFlag.info, "操作成功");

					/*
					 * xsz by time 提交结束后调用生成PDF方法
					 * 并将生成PDF后上传值OSS路径返回给前端
					 * 
					 * 参数：
					 * sourceBusiCode：业务编码
					 * sourceId：单据ID
					 * 
					 * xsz by time 2019-1-21 08:54:03
					 * 首先判断提交人是否具有签章
					 */

					/*
					 * String isSignature =
					 * approvalProcess_AF.getUserStart().getIsSignature();
					 * if(null != isSignature && "1".equals(isSignature))
					 * {
					 * if(null!=user.getIsSignature()&&"1".equals(user.
					 * getIsSignature()))
					 * {
					 * 
					 * ExportPdfForm pdfModel = new ExportPdfForm();
					 * pdfModel.setSourceBusiCode(BUSI_CODE);
					 * pdfModel.setSourceId(String.valueOf(sourceId));
					 * Properties executeProperties =
					 * exportPdfByWordService.execute(pdfModel);
					 * String pdfUrl = (String) executeProperties.get("pdfUrl");
					 * 
					 * Map<String, String> signatureMap = new HashMap<>();
					 * 
					 * signatureMap.put("signaturePath", pdfUrl);
					 * //TODO 此配置后期做成配置
					 * signatureMap.put("signatureKeyword", "乙方（盖章）");
					 * signatureMap.put("ukeyNumber", user.getUkeyNumber());
					 * 
					 * properties.put("signatureMap", signatureMap);
					 * 
					 * }
					 * }
					 */

				}

				break;

			default:
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, "没有需要处理的回调");
				;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
		}

		return properties;
	}

	
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	private Log log = LogFactory.getCurrentLogFactory().createLog(ApprovalProcessCallBack_06110201.class);

	
	/**
	 * 系统推送数据到门户网站
	 * 
	 * @param model
	 * @param eCode
	 * @param qysj
	 */
	public Boolean toInterFaceAction(Empj_BuildingInfo buildingInfo, String eCode, String qysj)
	{
		
		// 查询地址
		Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
		baseParameterForm0.setTheState(S_TheState.Normal);
		baseParameterForm0.setTheValue("69004");
		baseParameterForm0.setParametertype("69");
		Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

		if (null == baseParameter0)
		{
			log.equals("未查询到配置路径！");

			return false;
		}
		
		To_building building = new To_building();
		building.setAction("add");
		building.setCate("bld");
		building.setPj_title(buildingInfo.getProject().getTheName());
//		building.setTs_pj_id(buildingInfo.getProject().geteCode());
		building.setTs_pj_id(String.valueOf(buildingInfo.getProject().getTableId()));
		building.setBld_hname(buildingInfo.geteCodeFromConstruction());
		building.setBld_hmane1(null == buildingInfo.geteCodeFromPublicSecurity()?"":buildingInfo.geteCodeFromPublicSecurity());
		building.setTs_bld_id(eCode);
		building.setBld_mj(Double.toString(null == buildingInfo.getEscrowArea() ? 0.00 : buildingInfo.getEscrowArea()));
		building.setBld_lc(Double.toString(buildingInfo.getUpfloorNumber()));

		String deliveryType = buildingInfo.getDeliveryType();
		if (null != deliveryType && deliveryType.trim().equals("1"))
		{
			building.setBld_type("0");
		}
		else if (null != deliveryType && deliveryType.trim().equals("2"))
		{
			building.setBld_type("1");
		}

		building.setBld_tgtime(MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
		building.setBld_endtime("");
//		building.setBld_jfbatime("");

		Gson gson = new Gson();

		String jsonMap = gson.toJson(building);

		System.out.println(jsonMap);
		
		// 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());

        tgpf_SocketMsg.setMsgDirection("06110201_PUSH");
        tgpf_SocketMsg.setMsgContentArchives(jsonMap);
        

		String decodeStr = Base64Encoder.encode(jsonMap);

		System.out.println(decodeStr);

		ToInterface toFace = new ToInterface();
		
		boolean interfaceUtil = toFace.interfaceUtil(decodeStr,baseParameter0.getTheName());
		
		tgpf_SocketMsg.setReturnCode(String.valueOf(interfaceUtil));
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

		return interfaceUtil;
	}

}
