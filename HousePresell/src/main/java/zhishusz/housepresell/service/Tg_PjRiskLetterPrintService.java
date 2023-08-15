package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;
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
public class Tg_PjRiskLetterPrintService
{
	@Autowired
	private Tg_PjRiskLetterDao tg_PjRiskLetterDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;// 基础参数

	public Properties execute(Tg_PjRiskLetterForm model)
	{
		Properties properties = new MyProperties();
		/*
		 * 打印风险函
		 */

		String strNewFileName;
		try
		{
			Long tg_PjRiskLetterId = model.getTableId();
			Tg_PjRiskLetter tg_PjRiskLetter = (Tg_PjRiskLetter) tg_PjRiskLetterDao.findById(tg_PjRiskLetterId);
			if (tg_PjRiskLetter == null || S_TheState.Deleted.equals(tg_PjRiskLetter.getTheState()))
			{
				return MyBackInfo.fail(properties, "项目风险函不存在，请核实！");
			}

			/*
			 * 打印
			 */
			// 文本
			HashMap<String, String> paramsHashmap = new HashMap<>();

			// 发布日期
			String releaseDate = tg_PjRiskLetter.getReleaseDate();
			paramsHashmap.put("fbrq", null == releaseDate ? "": releaseDate);

			// 主送单位
			String deliveryCompany = tg_PjRiskLetter.getDeliveryCompany();
			paramsHashmap.put("zsdw", null == deliveryCompany ? "": deliveryCompany);

			// 开发项目
			Empj_ProjectInfo project = tg_PjRiskLetter.getProject();
			paramsHashmap.put("kfxm", null == project.getTheName() ? "": project.getTheName());

			// 开发企业
			Emmp_CompanyInfo developCompany = tg_PjRiskLetter.getDevelopCompany();
			paramsHashmap.put("kfqy", null == developCompany.getTheName() ? "": developCompany.getTheName());

			// 所在区域
			Sm_CityRegionInfo cityRegion = tg_PjRiskLetter.getCityRegion();
			paramsHashmap.put("szqy", null == cityRegion.getTheName() ? "": cityRegion.getTheName());

			// 风险提示
			String riskNotification = tg_PjRiskLetter.getRiskNotification();
			paramsHashmap.put("fxts", null == riskNotification ? "": riskNotification);

			// 基本情况
			String basicSituation = tg_PjRiskLetter.getBasicSituation();
			paramsHashmap.put("jbqk", null == basicSituation ? "": basicSituation);

			// 风险评估
			String riskAssessment = tg_PjRiskLetter.getRiskAssessment();
			paramsHashmap.put("fxpg", null == riskAssessment ? "": riskAssessment);

			// 附件材料
			// String deliveryCompany = tg_PjRiskLetter.getDeliveryCompany();
			// paramsHashmap.put("zsdw", deliveryCompany);

			// 创建导出路径
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String localPath = directoryUtil.getProjectRoot();// 项目路径
			// D:/Workspaces/MyEclipse%202017%20CI/.metadata/.me_tcat85/webapps/HousePresell/
			String saveDirectory = localPath + "print/";// 文件在服务器文件系统中的完整路径

			if (saveDirectory.contains("%20"))
			{
				saveDirectory = saveDirectory.replace("%20", " ");
			}

			directoryUtil.mkdir(saveDirectory);

			strNewFileName = MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss")
					+ ".pdf";

			String saveFilePath = saveDirectory + strNewFileName;

			// 图片
			// HashMap<Integer, String> picHashMap = new HashMap<>();
			// picHashMap.put(3, "E:\\temp\\small.jpg");

			// 查询模板名称 合作协议 500204
			Sm_BaseParameterForm baseModel = new Sm_BaseParameterForm();
			baseModel.setTheState(S_TheState.Normal);
			baseModel.setParametertype("50");
			baseModel.setTheValue("500201");

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

			// WordAdmin print = new
			// WordAdmin("E:\\temp\\word\\DemoTemplate.docx",
			// "C:\\Program Files (x86)\\OpenOffice 4\\program\\soffice.exe");
			String replace2 = saveDirectory.replace("/", "\\\\");
			// WordAdmin print = new WordAdmin(replace2 +
			// "商品房预售资金第三方托管合作协议.docx",
			// "C:\\Program Files (x86)\\OpenOffice 4\\program\\soffice.exe");

			// WordAdmin print = new WordAdmin(replace2 +
			// parameter.getTheName().trim(),
			// "C:\\Program Files (x86)\\OpenOffice 4\\program\\soffice.exe");

//			WordAdmin print = new WordAdmin(replace2 + parameter.getTheName().trim(), parameter1.getTheName().trim());

			// print.admin("E:\\lalala.pdf",paramsHashmap, null, null,
			// picHashMap,
			// paramsHashmap, null);
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
