package zhishusz.housepresell.service.extra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Emmp_CompanyInfoForm;
import zhishusz.housepresell.controller.form.extra.Tb_b_companyForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.extra.Tb_b_companyDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.Tb_b_company;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_IsUsedState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.state.S_UserType;
import zhishusz.housepresell.service.Sm_ApprovalProcessGetService;
import zhishusz.housepresell.service.Sm_ApprovalProcessService;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * 中间库开发企业
 * 
 * @ClassName: Tb_b_CompanyListService
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月25日 下午7:11:30
 * @version V1.0
 *
 */
@Service
@Transactional
public class Tb_b_CompanyService
{

	private static final String BUSI_CODE = "020101";// 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	@Autowired
	private Tb_b_companyDao dao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// eCode
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService; // 发起审批流程
	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;

	public Properties execute(Tb_b_companyForm model)
	{
		Properties properties = new MyProperties();

		// 获取请求类型
		String type = model.getType();

		switch (type)
		{
		case "list":
			// 根据开发企业名称和统一社会信用码查询企业列表
			String socialcreditcode = model.getSOCIALCREDITCODE();
			String companyname = model.getCOMPANYNAME();

			// 中间库列表
			List<Tb_b_company> list = new ArrayList<Tb_b_company>();
			// 本地库列表
			List<Emmp_CompanyInfo> infoList = new ArrayList<Emmp_CompanyInfo>();

			// 查询条件判空
			// if ((null == companyname || companyname.trim().isEmpty())
			// || (null == socialcreditcode ||
			// socialcreditcode.trim().isEmpty()))
			// {
			// infoList = new ArrayList<Emmp_CompanyInfo>();
			// }
			// else
			// {

			companyname = "%" + companyname + "%";
			socialcreditcode = "%" + socialcreditcode + "%";

			// 根据条件查询中间库的开发企业信息
			list = dao.getCompanyList(companyname, socialcreditcode);

			Emmp_CompanyInfo info;
			if (null != list && list.size() > 0)
			{
				// 查询本地系统的开发企业信息（有效且是从中间库导入的信息）
				String sql = "select * from emmp_companyinfo ec where ec.thestate=0 and ec.externalid is not null";
				List<Emmp_CompanyInfo> emmp_CompanyInfoList = new ArrayList<Emmp_CompanyInfo>();
				emmp_CompanyInfoList = sessionFactory.getCurrentSession().createNativeQuery(sql, Emmp_CompanyInfo.class)
						.getResultList();

				/*
				 * 通过本地系统与中间库查询出的开发企业作比较
				 * 如果本地系统中存在，则跳过
				 * 如果本地系统中不存在，则显示到前端选择
				 * 
				 */
				if (null != emmp_CompanyInfoList && emmp_CompanyInfoList.size() > 0)
				{
					boolean isAdd = true;

					for (Tb_b_company vo : list)
					{
						for (Emmp_CompanyInfo emmp_CompanyInfo : emmp_CompanyInfoList)
						{
							// if
							// (vo.getROWGUID().equals(emmp_CompanyInfo.getExternalId()))
							if (vo.getCOMPANYID().equals(emmp_CompanyInfo.getExternalId()))
							{

								isAdd = false;
								break;

							}
							else
							{
								isAdd = true;
							}
						}

						if (isAdd)
						{
							try
							{
								info = new Emmp_CompanyInfo();
								info = init(vo);
								infoList.add(info);
							}
							catch (ParseException e)
							{

							}
						}

					}
				}
				else
				{

					for (Tb_b_company vo : list)
					{
						info = new Emmp_CompanyInfo();

						try
						{
							info = init(vo);
						}
						catch (ParseException e)
						{

						}

						infoList.add(info);
					}

				}

			}
			// }

			properties.put("companyList", infoList);

			break;

		case "detail":

			// 根据开发企业rowguid查询开发企业详情
			String rowguid = model.getROWGUID();

			if (null == rowguid || rowguid.trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "请选择开发企业");
			}

			Tb_b_company vo = new Tb_b_company();

			vo = dao.getCompanyDetail(rowguid);

			properties.put("companyDetail", vo);

			break;

		case "save":

			Sm_User user = model.getUser();
			if (null == user)
			{
				return MyBackInfo.fail(properties, "请先进行登录");
			}

			String companyList = model.getSaveCompanyList();
			if (null == companyList || companyList.trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "请选择需要导入的企业信息");
			}
			else
			{
				List<Emmp_CompanyInfo> gasList = JSON.parseArray(companyList, Emmp_CompanyInfo.class);
				Emmp_CompanyInfoForm companyForm;
				for (Emmp_CompanyInfo emmp_CompanyInfo : gasList)
				{

					// 首先判断，该公司在本系统中是否存在有效的数据
					companyForm = new Emmp_CompanyInfoForm();
					companyForm.setTheState(S_TheState.Normal);
					companyForm.setExternalId(emmp_CompanyInfo.getExternalId());
					Integer count = 0;
					count = emmp_CompanyInfoDao.findByPage_Size(
							emmp_CompanyInfoDao.getQuery_Size(emmp_CompanyInfoDao.getIsOneHQL(), companyForm));
					if (count > 0)
					{
						return MyBackInfo.fail(properties, "开发企业：" + emmp_CompanyInfo.getTheName() + "已存在，请勿重复导入！");
					}

					// 发起人校验
					// 1 如果该业务没有配置审批流程，直接保存
					// 2 如果该业务配置了审批流程 ，判断用户能否与对应模块下的审批流程的发起人角色匹配
					properties = sm_ApprovalProcessGetService.execute(BUSI_CODE, model.getUserId());
					if (!"noApproval".equals(properties.getProperty("info"))
							&& "fail".equals(properties.getProperty("result")))
					{
						return properties;
					}

					emmp_CompanyInfo.setTheState(S_TheState.Normal);
					emmp_CompanyInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
					emmp_CompanyInfo.setUserStart(user);
					emmp_CompanyInfo.setCreateTimeStamp(System.currentTimeMillis());
					emmp_CompanyInfo.setUserUpdate(user);
					emmp_CompanyInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
					// 业务状态
					emmp_CompanyInfo.setTheType(S_CompanyType.Development);// 类型
					emmp_CompanyInfo.seteCodeFromPresellSystem(emmp_CompanyInfo.getExternalId());// 预售系统企业编号
					emmp_CompanyInfo.setEstablishmentDate(emmp_CompanyInfo.getRegisteredDate());// 企业成立日期
					emmp_CompanyInfo.setProjectLeader(emmp_CompanyInfo.getLegalPerson());// 项目负责人
					// 启用状态
					emmp_CompanyInfo.setIsUsedState(S_IsUsedState.InUsing);

					// 判断是否存在审批流程
					if ("noApproval".equals(properties.getProperty("info")))
					{
						// 业务状态
						emmp_CompanyInfo.setBusiState(S_BusiState.HaveRecord);
						emmp_CompanyInfo.setApprovalState(S_ApprovalState.Completed);
						emmp_CompanyInfoDao.save(emmp_CompanyInfo);
					}
					else
					{
						Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
								.get("sm_approvalProcess_cfg");

						// 业务状态
						emmp_CompanyInfo.setBusiState(S_BusiState.NoRecord);
						emmp_CompanyInfoDao.save(emmp_CompanyInfo);

						/*
						 * xsz by time 2018-11-14 19:51:45
						 * 如果需要审批流程，则创建模拟新建开发企业的model,以便发起审批
						 * 需要注意的是参数有：
						 * buttonType：1: 保存按钮 2. 提交按钮
						 * user及userId
						 */
						Emmp_CompanyInfoForm companyModel = new Emmp_CompanyInfoForm();
						companyModel.setUser(user);
						companyModel.setUserId(user.getTableId());
						companyModel.setButtonType("1");

						companyModel.setTheType(S_CompanyType.Development);// 机构类型
						companyModel.setTheName(emmp_CompanyInfo.getTheName());
						companyModel.setUnifiedSocialCreditCode(emmp_CompanyInfo.getUnifiedSocialCreditCode());
						companyModel.setRegisteredDate(emmp_CompanyInfo.getRegisteredDate());
						companyModel.setAddress(emmp_CompanyInfo.getAddress());
						companyModel.setLegalPerson(emmp_CompanyInfo.getLegalPerson());
						companyModel.setQualificationGrade(emmp_CompanyInfo.getQualificationGrade());
						companyModel.setProjectLeader(emmp_CompanyInfo.getLegalPerson());
						companyModel.setEstablishmentDate(emmp_CompanyInfo.getRegisteredDate());// 企业成立日期
						companyModel.seteCodeFromPresellSystem(emmp_CompanyInfo.getExternalId());// 预售系统企业编号
						companyModel.seteCode(emmp_CompanyInfo.geteCode());
						companyModel.setUserStart(emmp_CompanyInfo.getUserStart());
						companyModel.setCreateTimeStamp(emmp_CompanyInfo.getCreateTimeStamp());
						companyModel.setUserUpdate(emmp_CompanyInfo.getUserUpdate());
						companyModel.setLastUpdateTimeStamp(emmp_CompanyInfo.getLastUpdateTimeStamp());

						sm_approvalProcessService.execute(emmp_CompanyInfo, companyModel, sm_approvalProcess_cfg);

					}

				}
			}

			break;

		default:

			return MyBackInfo.fail(properties, "请选择正确的请求类型");

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * 中间库VO 转化为系统库PO
	 * 
	 * @param vo
	 * @return
	 * @throws ParseException
	 */
	public Emmp_CompanyInfo init(Tb_b_company vo) throws ParseException
	{

		// 企业名称 统一社会信用代码 注册日期 公司地址 法定代表人 资质等级 是否正式

		Emmp_CompanyInfo po = new Emmp_CompanyInfo();

		po.setTheName(vo.getCOMPANYNAME());
		po.setUnifiedSocialCreditCode(vo.getSOCIALCREDITCODE());

		String createtime = vo.getCREATETIME();
		if (null == createtime || createtime.trim().isEmpty())
		{
			po.setRegisteredDate(null);// 企业成立日期
		}
		else
		{
			try
			{
				SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parse = formatDate.parse(vo.getCREATETIME().substring(0, 10) + " 00:00:00");
				long ms = parse.getTime();
				po.setRegisteredDate(ms);// 企业成立日期
			}
			catch (Exception e)
			{

			}
		}

		po.setAddress(vo.getCOMPANYADDRESS());
		po.setLegalPerson(vo.getLEGALPERSON());
		po.setQualificationGrade("100".equals(vo.getCOMPANYLEVEL()) ? "99" : vo.getCOMPANYLEVEL());
		po.setTheType(S_CompanyType.Development);
		// po.setExternalId(vo.getROWGUID());
		po.setExternalId(vo.getCOMPANYID());

		return po;
	}
}
