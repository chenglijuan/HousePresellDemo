package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
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
public class Tgxy_EscrowAgreementPrintService
{
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;

	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;// 基础参数

	public Properties execute(Tgxy_EscrowAgreementForm model)
	{
		Properties properties = new MyProperties();
		/*
		 * 打印合作协议:
		 * 协议编号（页眉）
		 * 开发企业（甲方）
		 * 托管机构（乙方）
		 * 
		 * 预售项目名称
		 * 施工编号
		 * 公安编号
		 * 
		 * 其他预定事项
		 * 
		 * 争议解决方式：
		 * （1）向常州仲裁委员会申请仲裁。
		 * （2）向有管辖权的人民法院起诉。
		 */

		String strNewFileName;
		try
		{
			Long tgxy_EscrowAgreementId = model.getTableId();
			if (null == tgxy_EscrowAgreementId || tgxy_EscrowAgreementId < 1)
			{
				return MyBackInfo.fail(properties, "请选择有效的合作协议");
			}

			Tgxy_EscrowAgreement tgxy_EscrowAgreement = (Tgxy_EscrowAgreement) tgxy_EscrowAgreementDao
					.findById(tgxy_EscrowAgreementId);
			if (tgxy_EscrowAgreement == null)
			{
				return MyBackInfo.fail(properties, "该协议信息已失效，请刷新后重试");
			}

			/*
			 * 打印
			 */
			// 文本
			HashMap<String, String> paramsHashmap = new HashMap<>();
			HashMap<String, String> headMap = new HashMap<>();

			// 协议编号
			String eCodeOfAgreement = tgxy_EscrowAgreement.geteCodeOfAgreement();
			if (null == eCodeOfAgreement || eCodeOfAgreement.trim().isEmpty())
			{
				// 如果协议编号为空，直接使用eCode
				headMap.put("xybh", tgxy_EscrowAgreement.geteCode());
			}
			else
			{
				headMap.put("xybh", eCodeOfAgreement);
			}

			// 开发企业（甲方）
			Emmp_CompanyInfo company = tgxy_EscrowAgreement.getDevelopCompany();
			if (null == company || company.getTheName().trim().isEmpty())
			{
				paramsHashmap.put("kfqy", null == tgxy_EscrowAgreement.getTheNameOfDevelopCompany() ? ""
						: tgxy_EscrowAgreement.getTheNameOfDevelopCompany());
			}
			else
			{
				paramsHashmap.put("kfqy", company.getTheName());
			}

			// 托管机构（乙方）
			String escrowCompany = tgxy_EscrowAgreement.getEscrowCompany();
			paramsHashmap.put("tgjg", null == escrowCompany ? "" : escrowCompany);

			// 预售项目名称
			Empj_ProjectInfo project = tgxy_EscrowAgreement.getProject();
			if (null == project || project.getTheName().trim().isEmpty())
			{
				paramsHashmap.put("xmmc", "");
			}
			else
			{
				paramsHashmap.put("xmmc", project.getTheName());
			}

			// 施工编号
			String buildingInfoCodeList = tgxy_EscrowAgreement.getBuildingInfoCodeList();
			paramsHashmap.put("sgbh", buildingInfoCodeList);

			// 公安编号
			List<Empj_BuildingInfo> infoList = tgxy_EscrowAgreement.getBuildingInfoList();
			if (null == infoList || infoList.size() < 1)
			{
				paramsHashmap.put("gabh", "");
			}
			else
			{
				String bh = "";
				for (int i = 0; i < infoList.size(); i++)
				{
					if (i > 0)
					{
						bh += ";" + infoList.get(i).geteCodeFromPublicSecurity();
					}
					else
					{
						bh += infoList.get(i).geteCodeFromPublicSecurity();
					}

					// 从楼幢中提取预售项目名称
					if (null != infoList.get(i).getTheNameFromPresellSystem()
							&& !infoList.get(i).getTheNameFromPresellSystem().trim().isEmpty())
					{
						paramsHashmap.put("xmmc", infoList.get(i).getTheNameFromPresellSystem());
					}

				}

				paramsHashmap.put("gabh", (null == bh || bh.contains("null")) ? "" : bh);

			}

			// 其他预定事项
			String otherAgreedMatters = tgxy_EscrowAgreement.getOtherAgreedMatters();
			paramsHashmap.put("qtydsx", null == otherAgreedMatters ? "" : otherAgreedMatters);

			// 争议解决方式
			String disputeResolution = tgxy_EscrowAgreement.getDisputeResolution();
			if (null == disputeResolution || disputeResolution.trim().isEmpty())
			{
				paramsHashmap.put("zyjjfs", "1");
			}
			else
			{
				paramsHashmap.put("zyjjfs", disputeResolution);
			}

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

			// 查询模板名称 合作协议 500204
			Sm_BaseParameterForm baseModel = new Sm_BaseParameterForm();
			baseModel.setTheState(S_TheState.Normal);
			baseModel.setParametertype("50");
			baseModel.setTheValue("500204");

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
//			print.admin(replace, paramsHashmap, null, null, null, headMap, null);

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
