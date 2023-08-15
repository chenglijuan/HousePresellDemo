package zhishusz.housepresell.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Sm_CommonMessageNoticeForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_PresellDocumentInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_IsReaderState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.JDBCConnectionUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 预警更新操作：预警消息确认更新
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_ComMesgWarningUpdateService
{
	private JDBCConnectionUtil dao = new JDBCConnectionUtil();

	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	@Autowired
	private Empj_PresellDocumentInfoDao empj_PresellDocumentInfoDao;

	public Properties execute(Sm_CommonMessageNoticeForm model)
	{
		Properties properties = new MyProperties();
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		Map<String, Object> map = new HashMap<String, Object>();

		Long[] idArr = model.getIdArr();

		Integer queryType = null;

		String sql = "";

		Sm_User user = model.getUser();

		BaseForm base = new BaseForm();

		if (idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要修改的信息");
		}

		for (Long tableId : idArr)
		{
			// 查询预警
			Sm_CommonMessageDtl sm_CommonMessageDtl = sm_CommonMessageDtlDao.findById(tableId);

			if (sm_CommonMessageDtl == null)
			{
				return MyBackInfo.fail(properties, "未查询到有效的预警信息！");
			}

			// 获取预警主表信息
			Sm_CommonMessage sm_CommonMessage = sm_CommonMessageDtl.getMessage();

			String busiCodeYJ = sm_CommonMessage.getBusiCode();

			String busiCode = "";// 单据业务编码

			String externalId = null;

			Long orgDataId = new Long(sm_CommonMessage.getOrgDataId());

			Long userStartId = model.getUserId(); // 登录用户id TODO

			IApprovable iApprovable = null;
			// 查询对应的业务对象
			switch (busiCodeYJ)
			{
			case "220101":// 开发企业变更

				busiCode = "020102";

				Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(orgDataId);

				if (null == companyInfo)
				{
					return MyBackInfo.fail(properties, "未查询到有效的开发企业数据");
				}

				externalId = companyInfo.getExternalId();// 获取关联数据Id

				if (null == externalId || externalId.trim().length() < 1)
				{
					return MyBackInfo.fail(properties, "未查询到开发企业关联数据ID");
				}

				// 查询数据
				queryType = 1;

				sql = "select * from tb_b_company t where t.companyid = ? and  t.is_formal=1 ";

				base = getFileName(externalId, sql, queryType, user, map);

				if (null == base)
				{
					return MyBackInfo.fail(properties, "未查询到有效的关联数据");
				}

				properties = excuteApproveProcess(busiCode, userStartId, companyInfo, base);

				break;

			case "220102":// 项目变更

				iApprovable = empj_ProjectInfoDao.findById(orgDataId);
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				break;

			case "220103":// 楼幢变更预警

				busiCode = "03010202";

				Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(orgDataId);

				if (null == empj_BuildingInfo)
				{
					return MyBackInfo.fail(properties, "未查询到有效的楼幢");
				}
				
				map.put("deliveryType", empj_BuildingInfo.getDeliveryType());
				
				map.put("downfloorNumber", empj_BuildingInfo.getDownfloorNumber());
				
				map.put("buildingArea", empj_BuildingInfo.getBuildingArea());

				externalId = empj_BuildingInfo.getExternalId();// 获取关联数据Id

				if (null == externalId || externalId.trim().length() < 1)
				{
					return MyBackInfo.fail(properties, "未查询到楼幢关联数据ID");
				}

				// 查询数据
				queryType = 2;

				sql = "select * from tb_b_building t where t.buildingid = ? and  t.is_formal=1 ";

				base = getFileName(externalId, sql, queryType, user, map);

				if (null == base)
				{
					return MyBackInfo.fail(properties, "未查询到有效的关联数据");
				}
				
				properties = excuteApproveProcess(busiCode, userStartId, empj_BuildingInfo, base);

				break;

			case "220104": // 房屋状态预警

				iApprovable = empj_HouseInfoDao.findById(orgDataId);
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				break;

			case "220105": // 合同信息预警

				iApprovable = tgxy_ContractInfoDao.findById(orgDataId);
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				break;

			case "220106":// 楼幢备案价格单价变更预警

				busiCode = "03010302";
				
				// 获取关联楼幢信息
				Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(orgDataId);
				
				if (null == buildingInfo)
				{
					return MyBackInfo.fail(properties, "未查询到关联的楼幢信息");
				}

				Tgpj_BuildingAvgPriceForm avgForm = new Tgpj_BuildingAvgPriceForm();
				avgForm.setTheState(S_TheState.Normal);
				avgForm.setBuildingInfo(buildingInfo);
				
				Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = tgpj_BuildingAvgPriceDao.findOneByQuery_T(tgpj_BuildingAvgPriceDao.getQuery(tgpj_BuildingAvgPriceDao.getBasicHQL(), avgForm));

				if (null == tgpj_BuildingAvgPrice)
				{
					return MyBackInfo.fail(properties, "未查询到有效的备案均价数据");
				}
				
				map.put("remark", tgpj_BuildingAvgPrice.getRemark());

				// 查询数据
				queryType = 3;

				externalId = buildingInfo.getExternalId();// 获取关联数据Id

				sql = "select * from TB_B_BUILDINGWJ t where t.buildingid = ?";

				base = getFileName(externalId, sql, queryType, user, map);

				if (null == base)
				{
					return MyBackInfo.fail(properties, "未查询到有效的关联数据");
				}

				properties = excuteApproveProcess(busiCode, userStartId, tgpj_BuildingAvgPrice, base);

				break;

			case "220107":// 监管账户预警

				iApprovable = tgpj_BankAccountSupervisedDao.findById(orgDataId);
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				break;

			case "220108":// 合同状态预警

				iApprovable = tgxy_ContractInfoDao.findById(orgDataId);
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				break;

			case "220109":// 预售状态变更预警

				iApprovable = empj_PresellDocumentInfoDao.findById(orgDataId);
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

				break;
				
			case "21020304":// 预售状态变更预警	
				
				properties.put(S_NormalFlag.result, S_NormalFlag.success);
				properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
				
				break;
			default:

				break;
			}

			/* 判断是否可以形成代办事项，如果不能直接更新单据状态。如果可以查询审批流配置，形成代办事项 */
			/*
			 * if (flag)
			 * {
			 * properties = sm_ApprovalProcessGetService.execute(busiCode,
			 * userStartId);
			 * 
			 * if (properties.getProperty("info").equals("noApproval")
			 * || properties.getProperty("result").equals("fail"))
			 * {
			 * if (properties.getProperty("info").equals("noApproval"))
			 * {
			 * return MyBackInfo.fail(properties, "生成代办流程失败");
			 * }
			 * return properties;
			 * }
			 * else
			 * {
			 * Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg =
			 * (Sm_ApprovalProcess_Cfg) properties
			 * .get("sm_approvalProcess_cfg");
			 * 
			 * model.setButtonType("2");
			 * 
			 * sm_approvalProcessService.execute(iApprovable, model,
			 * sm_approvalProcess_cfg);
			 * }
			 * 
			 * }
			 */

			// 更新数据库信息状态
			sm_CommonMessageDtl.setIsReader(S_IsReaderState.ReadMesg);// 已读
			sm_CommonMessageDtl.setBusiState("1");// 确认更新
			sm_CommonMessageDtlDao.save(sm_CommonMessageDtl);
		}

		return properties;
	}

	private MyDatetime myTime = MyDatetime.getInstance();

	BaseForm getFileName(String externalId, String sql, Integer queryType, Sm_User user, Map<String, Object> map)
	{
		Connection conn = dao.getConn();// 获取连接

		try
		{
			PreparedStatement pre = conn.prepareStatement(sql);

			pre.setString(1, externalId);

			ResultSet res = pre.executeQuery();

			if (null == res)
			{
				return null;
			}

			if (null != queryType && 1 == queryType)
			{
				Emmp_CompanyInfoForm companyForm = new Emmp_CompanyInfoForm();

				while (res.next())
				{
					
					companyForm.setUser(user);
					companyForm.setUserId(user.getTableId());

					companyForm.setTheType(S_CompanyType.Development);// 机构类型
					companyForm.setTheName(res.getString("COMPANYNAME"));// 开发企业名称
					companyForm.setUnifiedSocialCreditCode(res.getString("SOCIALCREDITCODE"));// 统一社会信用代码

					String registeredDate = res.getString("CREATETIME");

					if (null == registeredDate || registeredDate.trim().isEmpty())
					{
						companyForm.setRegisteredDate(null);// 成立日期
					}
					else
					{
						companyForm.setRegisteredDate(
								myTime.stringMinuteToLong(registeredDate.substring(0, 10) + " 00:00:00"));// 成立日期
					}

					companyForm.setAddress(res.getString("COMPANYADDRESS"));// 开发企业地址
					companyForm.setLegalPerson(res.getString("LEGALPERSON"));// 法定代表人
					companyForm.seteCodeFromPresellSystem(externalId);// 预售系统企业编号
					companyForm.setUserUpdate(user);
					companyForm.setLastUpdateTimeStamp(System.currentTimeMillis());
				}

				return companyForm;
			}

			if (null != queryType && 2 == queryType)
			{
				Empj_BuildingInfoForm buildingForm = new Empj_BuildingInfoForm();

				while (res.next())
				{
					buildingForm.setUser(user);
					buildingForm.setUserId(user.getTableId());
					
					buildingForm.setLastUpdateTimeStamp(System.currentTimeMillis());
					buildingForm.setUserUpdate(user);

					buildingForm.setExternalId(externalId);
					
					buildingForm.setBuildingArea((Double) map.get("buildingArea"));
					
					buildingForm.setDeliveryType((String) map.get("deliveryType"));
					
					buildingForm.setDownfloorNumber((Double) map.get("downfloorNumber"));
					
					buildingForm.setEscrowArea(Double.parseDouble(res.getString("AREA_TOTAL")));
					buildingForm.setUpfloorNumber(Double.parseDouble(res.getString("FLOORS_UP")));
				}

				return buildingForm;
			}
			
			if (null != queryType && 3 == queryType)
			{
				Tgpj_BuildingAvgPriceForm buildingAvgForm = new Tgpj_BuildingAvgPriceForm();

				while (res.next())
				{
					buildingAvgForm.setUser(user);
					buildingAvgForm.setUserId(user.getTableId());
					
					buildingAvgForm.setLastUpdateTimeStamp(System.currentTimeMillis());
					buildingAvgForm.setUserUpdate(user);
					
					Timestamp hiredate = res.getTimestamp("WJBASJ");
					buildingAvgForm.setAveragePriceRecordDate(hiredate.getTime());
					buildingAvgForm.setRecordAveragePrice(Double.parseDouble(res.getString("BEIANJUNJIA")));
					buildingAvgForm.setRemark((String) map.get("remark"));
				}

				return buildingAvgForm;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();

			return null;
		}
		return null;
	}

	/**
	 * 生成代办事项-审批流
	 * 
	 * @param busiCode
	 *            单据编号
	 * @param userStartId
	 *            用户编码
	 * @param iApprovable
	 *            原数据
	 * @param base
	 *            新数据
	 * @return
	 */
	Properties excuteApproveProcess(String busiCode, Long userStartId, IApprovable iApprovable, BaseForm base)
	{
		Properties properties = new Properties();

		properties = sm_ApprovalProcessGetService.execute(busiCode, userStartId);

		if (properties.getProperty("info").equals("noApproval") || properties.getProperty("result").equals("fail"))
		{
			if (properties.getProperty("info").equals("noApproval"))
			{
				return MyBackInfo.fail(properties, "生成代办流程失败");
			}
			return properties;
		}
		else
		{
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
					.get("sm_approvalProcess_cfg");

			base.setButtonType("2");

			sm_approvalProcessService.execute(iApprovable, base, sm_approvalProcess_cfg);

			return properties;
		}
	}
}
