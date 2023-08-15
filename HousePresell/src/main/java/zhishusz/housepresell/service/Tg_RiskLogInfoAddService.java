package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskRatingForm;
import zhishusz.housepresell.controller.form.Tg_RiskLogInfoForm;
import zhishusz.housepresell.database.dao.Tg_RiskLogInfoDao;
import zhishusz.housepresell.database.po.Tg_RiskLogInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.database.dao.Tg_PjRiskRatingDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;

/*
 * Service添加操作：风险日志管理
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tg_RiskLogInfoAddService
{
	// 项目风险日志
	private static final String BUSI_CODE = "21020303";// 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	@Autowired
	private Tg_RiskLogInfoDao tg_RiskLogInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Tg_PjRiskRatingDao tg_PjRiskRatingDao;// 项目风险评级
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// 编码
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;// 附件配置
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;// 附件

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RiskLogInfoForm model)
	{
		Properties properties = new MyProperties();

		// 保存对象
		Tg_RiskLogInfo tg_RiskLogInfo = new Tg_RiskLogInfo();

		/*
		 * xsz by time 2018-10-16 10:46:40
		 * 新增风险日志：
		 * 前段字段：
		 * projectId：项目主键
		 * pjRiskRatingId：风险评级主键
		 * riskLog：风险日志（必输）
		 * logDate：日志日期（必输）
		 * 
		 * 默认字段：
		 * 操作人信息
		 * theState：正常态 0
		 * eCode：自动编码
		 * 
		 * 
		 * 
		 */

		// 操作人
		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "请先登录");
		}

		// 设置操作人及操作时间
		tg_RiskLogInfo.setUserStart(user);
		tg_RiskLogInfo.setUserUpdate(user);
		tg_RiskLogInfo.setCreateTimeStamp(System.currentTimeMillis());
		tg_RiskLogInfo.setLastUpdateTimeStamp(System.currentTimeMillis());

		// 风险日志
		String riskLog2 = model.getRiskLog();
		if (null == riskLog2 || riskLog2.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请输入风险日志信息");
		}

		// 设置风险日志
		tg_RiskLogInfo.setRiskLog(riskLog2);

		// 日志日期
		String logDate = model.getLogDate();
		if (null == logDate || logDate.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "请选择日志日期");
		}

		// 设置日志日期
		tg_RiskLogInfo.setLogDate(logDate);

		// 项目信息
		Long projectId2 = model.getProjectId();
		if (null == projectId2 || projectId2 < 1)
		{
			return MyBackInfo.fail(properties, "请选择有效的项目信息");
		}

		/*
		 * xsz by time 保存时校验项目评级等级
		 * 
		 */
		Tg_PjRiskRatingForm tModel = new Tg_PjRiskRatingForm();
		tModel.setProjectId(projectId2);
		List<Tg_PjRiskRating> tg_PjRiskRatingList;
		tg_PjRiskRatingList = tg_PjRiskRatingDao
				.findByPage(tg_PjRiskRatingDao.getQuery(tg_PjRiskRatingDao.getBasicDescoperateDateHQL(), tModel));
		if (null == tg_PjRiskRatingList || tg_PjRiskRatingList.size() == 0)
		{

			return MyBackInfo.fail(properties, "该项目还未进行项目风险评级");

		}
		else
		{
			/*
			 * xsz by time 2018-12-13 09:50:52
			 * 对评级为低（2）的，提示：
			 * 根据项目自动带出项目风险评级，只有值为“高”需要输入，值为“中”或“低”，则系统报错提示“该项目风险评级为XX，请确认！”
			 */

			String level = tg_PjRiskRatingList.get(0).getTheLevel();

			switch (level)
			{
			case "0":
				// 高

				break;
			case "1":
				// 中
				return MyBackInfo.fail(properties, "该项目风险评级为'中'，请确认！");

			case "2":
				// 低
				return MyBackInfo.fail(properties, "该项目风险评级为'低'，请确认！");

			default:
				// 其他
				return MyBackInfo.fail(properties, "该项目风险评级为不在正常范围内，请确认！");

			}

		}

		/*
		 * 查询项目详情
		 * 1.获取开发企业信息
		 * 2.获取区域信息
		 */
		Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(projectId2);
		if (null == projectInfo || 0 != projectInfo.getTheState())
		{
			return MyBackInfo.fail(properties, "选择的项目信息已失效，请刷新后重试");
		}

		Emmp_CompanyInfo company = projectInfo.getDevelopCompany();
		if (null == company)
		{
			return MyBackInfo.fail(properties, "选择的项目所属开发企业已失效，请刷新后重试");
		}

		Sm_CityRegionInfo cityRegionInfo = projectInfo.getCityRegion();
		if (null == cityRegionInfo)
		{
			return MyBackInfo.fail(properties, "选择的项目所属区域信息已失效，请刷新后重试");
		}

		// 设置项目信息
		tg_RiskLogInfo.setProject(projectInfo);
		tg_RiskLogInfo.setTheNameOfProject(projectInfo.getTheName());
		tg_RiskLogInfo.setCityRegion(cityRegionInfo);
		tg_RiskLogInfo.setTheNameOfCityRegion(cityRegionInfo.getTheName());
		tg_RiskLogInfo.setDevelopCompany(company);
		tg_RiskLogInfo.seteCodeOfDevelopCompany(company.geteCode());

		/*
		 * 查询项目评级信息
		 * 
		 */
		Long pjRiskRatingId = model.getPjRiskRatingId();
		if (null == pjRiskRatingId || pjRiskRatingId < 1)
		{
			return MyBackInfo.fail(properties, "项目评级为空");
		}

		Tg_PjRiskRating tg_PjRiskRating = tg_PjRiskRatingDao.findById(pjRiskRatingId);
		if (null == tg_PjRiskRating)
		{
			return MyBackInfo.fail(properties, "项目评级信息已失效，请刷新后重试");
		}

		tg_RiskLogInfo.setPjRiskRating(tg_PjRiskRating);
		tg_RiskLogInfo.seteCodeOfPjRiskRating(tg_PjRiskRating.geteCode());
		tg_RiskLogInfo.setRiskRating(tg_PjRiskRating.getTheLevel());

		// eCode
		tg_RiskLogInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tg_RiskLogInfo.setTheState(S_TheState.Normal);

		/*
		 * xsz by time 2018-11-21 14:47:06
		 * 判断附件是否必须上传
		 */
		// 判断是否有必传
		Sm_AttachmentCfgForm sm_AttachmentCfgForm = new Sm_AttachmentCfgForm();
		sm_AttachmentCfgForm.setBusiType(BUSI_CODE);
		sm_AttachmentCfgForm.setTheState(S_TheState.Normal);
		List<Sm_AttachmentCfg> sm_AttachmentCfgList = smAttachmentCfgDao
				.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), sm_AttachmentCfgForm));

		// 先判断是否有附件传递
		List<Sm_Attachment> attachmentList;
		if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
		{
			attachmentList = JSON.parseArray(model.getSmAttachmentList().toString(), Sm_Attachment.class);
		}
		else
		{
			attachmentList = new ArrayList<Sm_Attachment>();
		}

		if (null != sm_AttachmentCfgList && sm_AttachmentCfgList.size() > 0)
		{

			for (Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList)
			{
				// 根据业务判断是否有必传的附件配置
				if (sm_AttachmentCfg.getIsNeeded())
				{
					Boolean isExistAttachment = false;

					if (attachmentList.size() > 0)
					{

						for (Sm_Attachment sm_Attachment : attachmentList)
						{
							if (sm_AttachmentCfg.geteCode().equals(sm_Attachment.getSourceType()))
							{
								isExistAttachment = true;
								break;
							}
						}

					}

					if (!isExistAttachment)
					{
						return MyBackInfo.fail(properties, sm_AttachmentCfg.getTheName() + "未上传,此附件为必须上传附件");
					}

				}
			}
		}

		Serializable tableId = tg_RiskLogInfoDao.save(tg_RiskLogInfo);

		/*
		 * xsz by time 2018-10-16 11:28:47
		 * 附件信息
		 */
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
		{
			smAttachmentList = model.getSmAttachmentList().toString();

			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);

				sm_Attachment.setSourceId(tableId.toString());// 关联Id
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_Attachment.setUserStart(user);// 创建人
				sm_Attachment.setUserUpdate(user);// 操作人
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		properties.put("tableId", new Long(tableId.toString()));

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
