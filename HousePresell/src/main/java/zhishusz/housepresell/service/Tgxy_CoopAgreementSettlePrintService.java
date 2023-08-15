package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service打印：托管合作协议 输出PDF打印
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_CoopAgreementSettlePrintService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;// 基础参数

	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{
		Properties properties = new MyProperties();
		/*
		 * 打印风险函
		 */

		String strNewFileName;
		try
		{
			Long tgxy_CoopAgreementSettleId = model.getTableId();
			Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = (Tgxy_CoopAgreementSettle)tgxy_CoopAgreementSettleDao.findById(tgxy_CoopAgreementSettleId);
			if(tgxy_CoopAgreementSettle == null || S_TheState.Deleted.equals(tgxy_CoopAgreementSettle.getTheState()))
			{
				return MyBackInfo.fail(properties, "该记录不存在，请联系管理员！");
			}

			/*
			 * 打印
			 */
			// 文本
			HashMap<String, String> paramsHashmap = new HashMap<>();

			// 代理公司名称
			String companyName = tgxy_CoopAgreementSettle.getCompanyName() == null ? "" : tgxy_CoopAgreementSettle.getAgentCompany().getTheName();
			paramsHashmap.put("gsmc", null == companyName ? "": companyName);

			// 结算日期
			String applySettlementDate = tgxy_CoopAgreementSettle.getApplySettlementDate();
			paramsHashmap.put("jsrq", null == applySettlementDate ? "": applySettlementDate);

			// 三方协议签署总量
			Integer totalNum = tgxy_CoopAgreementSettle.getProtocolNumbers();
			paramsHashmap.put("qszl", null == totalNum ? "": totalNum.toString());

			// 三方协议有效数量
			Integer totalEffectiveNum = tgxy_CoopAgreementSettle.getProtocolNumbers();
			paramsHashmap.put("yxsl", null == totalEffectiveNum ? "": totalEffectiveNum.toString());

			// 三方协议整改次数
//			Sm_CityRegionInfo cityRegion = tgxy_CoopAgreementSettle.getCityRegion();
//			paramsHashmap.put("zgcs", null == cityRegion.getTheName() ? "": cityRegion.getTheName());
			paramsHashmap.put("zgcs", "0");


			// 代理公司负责人
			Sm_User user = tgxy_CoopAgreementSettle.getUserStart();
			paramsHashmap.put("fzr", null == user ? "": user.getTheName());
			
			// 代理公司负责人
			paramsHashmap.put("lxdh", null == user ? "": (null == user.getPhoneNumber() ? "": user.getPhoneNumber()));
			

			// 代理公司意见
			String agencyCompanySuggesstion = "情况属实，数量正确。";
			paramsHashmap.put("dlgsyj", null == agencyCompanySuggesstion ? "": agencyCompanySuggesstion);
			
			// 代理公司
//			String basicSituation = tgxy_CoopAgreementSettle.getBasicSituation();
			paramsHashmap.put("dlgsmc", null == companyName ? "": companyName);
			
			// 代理公司日期
//			String basicSituation = tgxy_CoopAgreementSettle.getBasicSituation();
			paramsHashmap.put("dlgsrq", null == applySettlementDate ? "": applySettlementDate);

			// 项目部意见
			String xmbyj = "情况属实，同意结算。";
			paramsHashmap.put("xmbyj", null == xmbyj ? "": xmbyj);
			
			// 经办人
//			String riskAssessment = tgxy_CoopAgreementSettle.getRiskAssessment();
//			paramsHashmap.put("jbr", null == riskAssessment ? "": riskAssessment);
//			
//			// 经办日期
//			String riskAssessment = tgxy_CoopAgreementSettle.getRiskAssessment();
//			paramsHashmap.put("jbrq", null == riskAssessment ? "": riskAssessment);
			
			// 项目部负责人意见
			String xmbfzryj = "同意!";
			paramsHashmap.put("xmbfzryj", null == xmbfzryj ? "": xmbfzryj);

//			// 项目部负责人
//			String riskAssessment = tgxy_CoopAgreementSettle.getRiskAssessment();
//			paramsHashmap.put("xmbfzr", null == riskAssessment ? "": riskAssessment);
//			
//			// 项目部负责日期
//			String riskAssessment = tgxy_CoopAgreementSettle.getRiskAssessment();
//			paramsHashmap.put("xmbfzrrq", null == riskAssessment ? "": riskAssessment);
			
			
			
			// 创建导出路径
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String localPath = directoryUtil.getProjectRoot();// 项目路径

			String saveDirectory = localPath + "print/";// 文件在服务器文件系统中的完整路径

			if (saveDirectory.contains("%20"))
			{
				saveDirectory = saveDirectory.replace("%20", " ");
			}

			directoryUtil.mkdir(saveDirectory);

			strNewFileName = MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss")
					+ ".pdf";

			String saveFilePath = saveDirectory + strNewFileName;

			Sm_BaseParameterForm baseModel = new Sm_BaseParameterForm();
			baseModel.setTheState(S_TheState.Normal);
			baseModel.setParametertype("50");
			baseModel.setTheValue("500206");

			Sm_BaseParameter parameter = sm_BaseParameterDao
					.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseModel));

			// 工具安装路径 500101
			baseModel.setTheState(S_TheState.Normal);
			baseModel.setParametertype("50");
			baseModel.setTheValue("500101");

			Sm_BaseParameter parameter1 = sm_BaseParameterDao
					.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseModel));

			if (null == parameter1 || null == parameter1.getTheName() || parameter1.getTheName().trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "未查找到配置的工具信息");
			}

			String replace2 = saveDirectory.replace("/", "\\\\");

//			WordAdmin print = new WordAdmin(replace2 + parameter.getTheName().trim(), parameter1.getTheName().trim());

			String replace = saveFilePath.replace("/", "\\\\");
//			print.admin(replace, paramsHashmap, null, null, null, paramsHashmap, null);

			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			properties.put("url", "print/" + strNewFileName);

		}
		catch (Exception e)
		{

			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_BusiError);
			properties.put("logger", e.getMessage());

		}

		return properties;
	}
}
