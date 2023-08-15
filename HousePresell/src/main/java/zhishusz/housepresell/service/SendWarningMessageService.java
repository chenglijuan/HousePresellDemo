package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Sm_MessageTemplate_CfgForm;
import zhishusz.housepresell.controller.form.Sm_Permission_RoleUserForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAvgPriceForm;
import zhishusz.housepresell.controller.form.extra.Tg_EarlyWarningFrom;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_PresellDocumentInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDao;
import zhishusz.housepresell.database.dao.Sm_CommonMessageDtlDao;
import zhishusz.housepresell.database.dao.Sm_MessageTemplate_CfgDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RangeAuthorizationDao;
import zhishusz.housepresell.database.dao.Sm_Permission_RoleUserDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAvgPriceDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_DepositAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_PresellDocumentInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_CommonMessageDtl;
import zhishusz.housepresell.database.po.Sm_MessageTemplate_Cfg;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Sm_Permission_RoleUser;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.Tgpj_BuildingAvgPrice;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_DepositAgreement;
import zhishusz.housepresell.database.po.state.S_IsReaderState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service 预警消息推送
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class SendWarningMessageService
{
	
	private final String BUSICODE="06110101";// 协定存款协议
	@Autowired
	private Sm_PushletService sm_PushletService;
	@Autowired
	private Sm_CommonMessageDao sm_CommonMessageDao;
	@Autowired
	private Sm_CommonMessageDtlDao sm_CommonMessageDtlDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Sm_MessageTemplate_CfgDao sm_MessageTemplate_CfgDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_Permission_RangeAuthorizationDao sm_Permission_RangeAuthorizationdao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_companyInfoDao;
	@Autowired
	private Sm_Permission_RoleUserDao sm_Permission_RoleUserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Empj_BuildingAccountSupervisedDao empj_BuildingAccountSupervisedDao;
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
	@Autowired
	private Empj_PresellDocumentInfoDao empj_PresellDocumentInfoDao;
	@Autowired
	private Tgpj_BuildingAvgPriceDao tgpj_BuildingAvgPriceDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	@Autowired
	private Tgxy_DepositAgreementDao tgxy_DepositAgreementDao;
	
	

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_EarlyWarningFrom model)
	{
		Tgxy_ContractInfo tgxy_ContractInfo=new Tgxy_ContractInfo();
		Empj_BuildingInfo buildingInfo=new Empj_BuildingInfo();
		
		Properties properties = new MyProperties();
		// 获取推送信息主表Id
		Long tableId = model.getTableId();

		// 获取其他消息主键
		String otherId = model.getOtherId();
		
		// 查询消息主表
		Sm_CommonMessage sm_CommonMessage = sm_CommonMessageDao.findById(tableId);

		if (null == sm_CommonMessage)
		{
			return MyBackInfo.fail(properties, "没有查询到有效的数据，请联系管理人员进行维护！");
		}

		// 查询消息模板
		String busiCode = sm_CommonMessage.getBusiCode();// 单据业务编号
		String orgDataId = sm_CommonMessage.getOrgDataId();// 关联数据Id
		String theData = sm_CommonMessage.getTheData();

		Sm_MessageTemplate_CfgForm form = new Sm_MessageTemplate_CfgForm();
		form.setTheState(S_TheState.Normal);
		form.setBusiCode(busiCode);
		form.seteCode(theData);
		
		Sm_MessageTemplate_Cfg messageTemplate_Cfg = sm_MessageTemplate_CfgDao
				.findOneByQuery_T(sm_MessageTemplate_CfgDao.getQuery(sm_MessageTemplate_CfgDao.getBasicHQL(), form));// 查询消息模板
		if (null == messageTemplate_Cfg)
		{
			return MyBackInfo.fail(properties, "未查询到有效的消息模板，请核对是否配置消息模板。");
		}
		else
		{
			// 获取消息模板
			Map<String, String> messageModel = getMessageModel(sm_CommonMessage, messageTemplate_Cfg);

			String templateFina1 = messageModel.get("templateFina1");

			String templateFina2 = messageModel.get("templateFina2");

			List<Sm_Permission_Role> sm_permission_roleList = messageTemplate_Cfg.getSm_permission_roleList();// 获取角色

			Emmp_CompanyInfo emmp_CompanyInfo = new Emmp_CompanyInfo();

			Sm_CityRegionInfo cityRegion = new Sm_CityRegionInfo();

			List<Long> userList = new ArrayList<Long>();

				switch (busiCode)
				{
				
				case "06110101": // 协定存款协议预警
					
					Tgxy_DepositAgreement depositAgreement = tgxy_DepositAgreementDao.findById(new Long(orgDataId));
					if(null == depositAgreement)
					{
						return MyBackInfo.fail(properties, "未查询到有效的单据信息。");
					}
					
					//查询接收人(财务复核)104825
//					userList = getRecipient(busiCode, theData, companyInfo.getTableId(), null);
					Sm_Permission_RoleUserForm roleUserModle = new Sm_Permission_RoleUserForm();
					roleUserModle.setTheState(S_TheState.Normal);
					roleUserModle.setSm_Permission_RoleId(104825L);
					List<Sm_Permission_RoleUser> roleUserList = new ArrayList<Sm_Permission_RoleUser>();
					roleUserList = sm_Permission_RoleUserDao.findByPage(sm_Permission_RoleUserDao.getQuery(sm_Permission_RoleUserDao.getBasicHQL(), roleUserModle));
					if(null != roleUserList && roleUserList.size()>0)
					{
						for (Sm_Permission_RoleUser sm_Permission_RoleUser : roleUserList)
						{
							userList.add(sm_Permission_RoleUser.getSm_User().getTableId());
						}
					}
					else
					{
						userList = new ArrayList<>();
					}
					
					break;
				
				case "220101": // 开发企业预警
					Emmp_CompanyInfo companyInfo = emmp_companyInfoDao.findById(new Long(orgDataId));

					if (null == companyInfo)
					{
						return MyBackInfo.fail(properties, "未查询到有效的开发企业信息。");
					}
					else
					{
						emmp_CompanyInfo = companyInfo;
					}
					
					//查询接收人
					userList = getRecipient(busiCode, theData, companyInfo.getTableId(), null);

					break;

				case "220102":// 项目变更

					Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findById(new Long(orgDataId));

					if (null == empj_ProjectInfo)
					{
						return MyBackInfo.fail(properties, "未查询到有效的项目信息。");
					}					

					if (null == empj_ProjectInfo.getCityRegion())
					{
						return MyBackInfo.fail(properties, "未查询到关联区域信息，请核对。");
					}

					cityRegion = empj_ProjectInfo.getCityRegion();
					
					//查询接收人
					userList = getRecipient(busiCode, theData, empj_ProjectInfo.getDevelopCompany().getTableId(), cityRegion.getTableId());

					break;

				case "220103":// 楼幢变更预警
					
					buildingInfo = empj_BuildingInfoDao.findById(new Long(orgDataId));// 查询楼幢

					if (null == buildingInfo)
					{
						return MyBackInfo.fail(properties, "未查询到有效的楼幢信息。");
					}

					if (null == buildingInfo.getProject())
					{
						return MyBackInfo.fail(properties, "该楼幢没有关联项目，请核对。");
					}

					if (null == buildingInfo.getProject().getCityRegion())
					{
						return MyBackInfo.fail(properties, "未查询到关联区域信息，请核对。");
					}

					cityRegion = buildingInfo.getProject().getCityRegion();
					
					// 获取开发企业
					emmp_CompanyInfo = buildingInfo.getDevelopCompany();
					
					//查询接收人
					userList = getRecipient(busiCode, theData, emmp_CompanyInfo.getTableId(), cityRegion.getTableId());

					break;

				case "220104": // 房屋状态预警 （预售系统）

					Empj_HouseInfo houseInfo = empj_HouseInfoDao.findById(new Long(orgDataId));// 查询房屋信息

					if (null == houseInfo)
					{
						return MyBackInfo.fail(properties, "未查询到有效的房屋信息。");
					}

					if (null == houseInfo.getBuilding())
					{
						return MyBackInfo.fail(properties, "未查询到关联楼幢信息，请核对。");
					}

					if (null == houseInfo.getBuilding().getProject())
					{
						return MyBackInfo.fail(properties, "未查询到关联项目信息，请核对。");
					}

					if (null == houseInfo.getBuilding().getProject().getCityRegion())
					{
						return MyBackInfo.fail(properties, "未查询到关联区域信息，请核对。");
					}

					
					// 查询区域负责人
					cityRegion = houseInfo.getBuilding().getProject().getCityRegion();

					//查询接收人
					userList = getRecipient(busiCode, theData, houseInfo.getBuilding().getDevelopCompany().getTableId(), cityRegion.getTableId());

					break;

				case "220105": // 合同信息预警 （预售系统）

					tgxy_ContractInfo = tgxy_ContractInfoDao.findById(new Long(orgDataId));

					if (null == tgxy_ContractInfo)
					{
						return MyBackInfo.fail(properties, "未查询到有效的合同信息。");
					}

					if (null == tgxy_ContractInfo.getCompany())
					{
						return MyBackInfo.fail(properties, "未查询到关联企业信息，请核对。");
					}

					if (null == tgxy_ContractInfo.getBuildingInfo())
					{
						return MyBackInfo.fail(properties, "未查询到关联楼幢信息，请核对。");
					}

					emmp_CompanyInfo = tgxy_ContractInfo.getCompany();// 查询开发企业
					

			        if (tgxy_ContractInfo.getBuildingInfo().getProject() == null)
			        {
			          return MyBackInfo.fail(properties, "未查询到关联项目信息，请核对。");
			        }

			        if (tgxy_ContractInfo.getBuildingInfo().getProject().getCityRegion() == null)
			        {
			          return MyBackInfo.fail(properties, "未查询到关联区域信息，请核对。");
			        }
			        
			        cityRegion = tgxy_ContractInfo.getBuildingInfo().getProject().getCityRegion();

					//查询接收人
			        userList = getRecipient(busiCode, theData, tgxy_ContractInfo.getBuildingInfo().getDevelopCompany().getTableId(), cityRegion.getTableId());
					
					break;
				case "220106":// 楼幢备案价格单价变更预警

					Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(new Long(orgDataId));

					if (null == empj_BuildingInfo)
					{
						return MyBackInfo.fail(properties, "未查询到关联楼幢信息，请核对。");
					}

					Tgpj_BuildingAvgPriceForm avgForm = new Tgpj_BuildingAvgPriceForm();
					avgForm.setTheState(S_TheState.Normal);
					avgForm.setBuildingInfo(empj_BuildingInfo);

					Tgpj_BuildingAvgPrice tgpj_BuildingAvgPrice = tgpj_BuildingAvgPriceDao.findOneByQuery_T(
							tgpj_BuildingAvgPriceDao.getQuery(tgpj_BuildingAvgPriceDao.getBasicHQL(), avgForm));

					if (null == tgpj_BuildingAvgPrice)
					{
						return MyBackInfo.fail(properties, "未查询到有效的楼幢备案价格信息。");
					}

					if (null == tgpj_BuildingAvgPrice.getBuildingInfo())
					{
						return MyBackInfo.fail(properties, "未查询到关联楼幢信息，请核对。");
					}

					if (null == tgpj_BuildingAvgPrice.getBuildingInfo().getProject())
					{
						return MyBackInfo.fail(properties, "未查询到关联项目信息，请核对。");
					}

					if (null == tgpj_BuildingAvgPrice.getBuildingInfo().getProject().getCityRegion())
					{
						return MyBackInfo.fail(properties, "未查询到关联区域信息，请核对。");
					}

					 emmp_CompanyInfo = tgpj_BuildingAvgPrice.getBuildingInfo().getDevelopCompany();
					// 获取开发企业

					// 查询区域负责人
					cityRegion = tgpj_BuildingAvgPrice.getBuildingInfo().getProject().getCityRegion();
					//查询接收人
					userList = getRecipient(busiCode, theData, emmp_CompanyInfo.getTableId(), cityRegion.getTableId());

					break;

				case "220107":// 监管账户预警
					
					Empj_BuildingAccountSupervised empj_BuildingAccountSupervised=empj_BuildingAccountSupervisedDao.findById(new Long(orgDataId));
					
					Tgpj_BankAccountSupervised tgpj_BankAccountSupervised =empj_BuildingAccountSupervised.getBankAccountSupervised();

					if (null == tgpj_BankAccountSupervised)
					{
						return MyBackInfo.fail(properties, "未查询到有效的监管账户信息。");
					}

					if (null == tgpj_BankAccountSupervised.getDevelopCompany())
					{
						return MyBackInfo.fail(properties, "未查询到关联开发企业，请核对。");
					}

					emmp_CompanyInfo = tgpj_BankAccountSupervised.getDevelopCompany();// 获取开发企业
					//查询接收人
					userList = getRecipient(busiCode, theData, emmp_CompanyInfo.getTableId(), null);
					
					break;
				case "220108":

					// 获取业务种类
					String busiKind = sm_CommonMessage.getBusiKind();

					tgxy_ContractInfo = tgxy_ContractInfoDao.findById(new Long(orgDataId));

					if (null == tgxy_ContractInfo)
					{
						return MyBackInfo.fail(properties, "未查询到有效的合同信息。");
					}

					if (null == tgxy_ContractInfo.getCompany())
					{
						return MyBackInfo.fail(properties, "未查询到关联企业信息，请核对。");
					}

					if (null == tgxy_ContractInfo.getBuildingInfo())
					{
						return MyBackInfo.fail(properties, "未查询到关联楼幢信息，请核对。");
					}

					emmp_CompanyInfo = tgxy_ContractInfo.getCompany();// 查询开发企业

					buildingInfo = tgxy_ContractInfo.getBuildingInfo();

					if (null == buildingInfo.getProject())
					{
						return MyBackInfo.fail(properties, "未查询到关联项目信息，请核对。");
					}

					if (null == buildingInfo.getProject().getCityRegion())
					{
						return MyBackInfo.fail(properties, "未查询到关联区域信息，请核对。");
					}

					cityRegion = buildingInfo.getProject().getCityRegion();
					
					//查询接收人
					userList = getRecipient(busiCode, theData, emmp_CompanyInfo.getTableId(), cityRegion.getTableId());
				
					break;

				case "220109":// 预售状态变更预警 （预售系统）
					Empj_PresellDocumentInfo empj_PresellDocumentInfo = empj_PresellDocumentInfoDao
							.findById(new Long(orgDataId));

					if (null == empj_PresellDocumentInfo)
					{
						return MyBackInfo.fail(properties, "未查询到有效的预售证信息。");
					}


					if (null == empj_PresellDocumentInfo.getProject())
					{
						return MyBackInfo.fail(properties, "未查询到关联项目信息，请核对。");
					}

					if (null == empj_PresellDocumentInfo.getProject().getCityRegion())
					{
						return MyBackInfo.fail(properties, "未查询到关联区域信息，请核对。");
					}				
					
					//查询接收人
					userList = getRecipient(busiCode, theData, empj_PresellDocumentInfo.getProject().getDevelopCompany().getTableId(), cityRegion.getTableId());

					break;

				default:
					break;
				}

			sendCommanyMesg(userList, sm_CommonMessage, templateFina1, templateFina2);
			

			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		     return properties;
		}
	}
	
	/**
	 * 推送消息
	 * 
	 * @param userList
	 *            用户列表
	 * @param sm_CommonMessage
	 *            消息主题
	 * @param templateFina1
	 *            主题
	 * @param templateFina2
	 *            内容
	 */
	// 业务编码
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;

	private void sendCommanyMesg(List<Long> userList, Sm_CommonMessage sm_CommonMessage, String templateFina1,
			String templateFina2)
	{
		// 发送消息
		for (Long userId : userList)
		{
			Sm_User user = sm_UserDao.findById(userId);

			// 更新主表信息
			sm_CommonMessage.setTheTitle(templateFina1);
			sm_CommonMessage.setTheContent(templateFina2);
			sm_CommonMessageDao.save(sm_CommonMessage);

			// 保存消息子表
			Sm_CommonMessageDtl sm_CommonMessageDtl = new Sm_CommonMessageDtl();
			sm_CommonMessageDtl.seteCode(sm_BusinessCodeGetService.execute(sm_CommonMessage.getBusiCode()));
			sm_CommonMessageDtl.setUserStart(user);
			sm_CommonMessageDtl.setCreateTimeStamp(System.currentTimeMillis());

			sm_CommonMessageDtl.setBusiState("0");
			sm_CommonMessageDtl.setTheState(S_TheState.Normal);
			sm_CommonMessageDtl.setIsReader(S_IsReaderState.UnReadMesg);
			sm_CommonMessageDtl.setSendTimeStamp(MyDatetime.getInstance().dateToString(System.currentTimeMillis()));
			sm_CommonMessageDtl.setMessage(sm_CommonMessage);
			sm_CommonMessageDtl.setMessageType(sm_CommonMessage.getMessageType());
			sm_CommonMessageDtl.setReceiver(user);

			sm_CommonMessageDtlDao.save(sm_CommonMessageDtl);

			sm_PushletService.execute(templateFina1, templateFina2, userId);
		}
	}

	/**
	 * 查询消息接收人
	 * 
	 * @param userList
	 *            用户列表
	 * @param sm_Permission_Role
	 *            用户角色
	 * @return
	 */
	private List<Long> getRecipient(String busicode, String theData, Long companyId, Long cityRegionId)
	  {
	    String sql = "select *   from sm_user u  "
	    		+ " where exists (select null  from sm_permission_roleuser c"
	    		+ " inner join rel_messagetemplate_role a  "
	    		+ " on c.sm_permission_role = a.sm_permission_role  "
	    		+ " inner join SM_MESSAGETEMPLATE_CFG b "
	    		+ " on a.sm_messagetemplate_cfg = b.tableid "
	    		+ " where b.busicode = '" + busicode + "' " 
	    		+ " and ('" + theData + "' = 'null' or '" + theData + "'=b.ecode)" 
	    		+ " and u.tableid = c.sm_user " 
	    		+ " and ((u.thetype = 1 and (" + cityRegionId + " is null or (c.sm_permission_role = 21 and exists " 
	    		+ " (select null from sm_permission_range r  where r.userinfo = c.sm_user " 
	    		+ " and r.cityregioninfo = " + cityRegionId + ") or " 
	    		+ " c.sm_permission_role <> 21))) or " 
	    		+ " (thetype = 2 and instr(u.company, " + companyId + ") > 0)))";

	    List<Sm_User> userList = new ArrayList<Sm_User>();
	    userList = this.sessionFactory.getCurrentSession().createNativeQuery(sql, Sm_User.class)
	      .getResultList();
	    List userIdList = new ArrayList();
	    if ((userList != null) && (userList.size() > 0))
	    {
	      for (Sm_User sm_user:userList)
	      {
	        userIdList.add(sm_user.getTableId());
	      }
	    }
	    return userIdList;
	  }

	/**
	 * 获取消息模板
	 * 
	 * @param sm_CommonMessage
	 *            消息主体
	 * @param messageTemplate_Cfg
	 *            消息模板配置
	 * @return
	 */
	private Map<String, String> getMessageModel(Sm_CommonMessage sm_CommonMessage,
			Sm_MessageTemplate_Cfg messageTemplate_Cfg)
	{
		Map<String, Object> keyValMap = BeanUtil.beanToMap(sm_CommonMessage);

		String theTitle = messageTemplate_Cfg.getTheTitle();// 主题
		String content = messageTemplate_Cfg.getTheContent();// 内容

		String regex = "\\{([\\w]+)\\}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(theTitle);
		Matcher m2 = p.matcher(content);

		List<String> keyArrList = new ArrayList<String>();
		List<String> valArrList = new ArrayList<String>();
		while (m.find())
		{
			String keyStr = m.group().replaceAll(regex, "$1");

			if (keyStr == null)
				continue;
			if (!keyArrList.contains(keyStr))
			{
				keyArrList.add(keyStr);
			}

			String value = (String) keyValMap.get(keyStr);
			valArrList.add(value);
		}

		// 用户自定义的模板
		for (String keyStr_Temp : keyArrList)
		{
			theTitle = theTitle.replaceAll("\\{" + keyStr_Temp + "\\}", "{}");
		}

		// 消息模板内容
		List<String> keyArrList2 = new ArrayList<String>();
		List<String> valArrList2 = new ArrayList<String>();
		while (m2.find())
		{
			String keyStr2 = m2.group().replaceAll(regex, "$1");

			if (keyStr2 == null)
				continue;
			if (!keyArrList2.contains(keyStr2))
			{
				keyArrList2.add(keyStr2);
			}

			String value = (String) keyValMap.get(keyStr2);
			valArrList2.add(value);
		}

		// 用户自定义的模板
		for (String keyStr_Temp : keyArrList2)
		{
			content = content.replaceAll("\\{" + keyStr_Temp + "\\}", "{}");
		}

		Map<String, String> messageModel = new HashMap<String, String>();

		String template1 = theTitle;
		String templateFina1 = StrUtil.format(template1, valArrList.toArray(new String[valArrList.size()]));
		String template2 = content;
		String templateFina2 = StrUtil.format(template2, valArrList2.toArray(new String[valArrList2.size()]));

		messageModel.put("templateFina1", templateFina1);
		messageModel.put("templateFina2", templateFina2);

		return messageModel;
	}
}
