package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_AF;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
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
public class Empj_BldLimitAmount_AFPrintService
{
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;// 基础参数

	public Properties execute(Empj_BldLimitAmount_AFForm model)
	{
		Properties properties = new MyProperties();
		/*
		 * 打印风险函
		 */

		String strNewFileName;
		try
		{
			Long empj_BldLimitAmount_AFId = model.getTableId();
			Empj_BldLimitAmount_AF empj_BldLimitAmount_AF = (Empj_BldLimitAmount_AF)empj_BldLimitAmount_AFDao.findById(empj_BldLimitAmount_AFId);
			if(empj_BldLimitAmount_AF==null){
				return MyBackInfo.fail(properties, "'Empj_BldLimitAmount_AF(Id:" + empj_BldLimitAmount_AFId + ")'不存在");
			}
			/*
			 * 打印
			 */
			// 文本
			HashMap<String, String> paramsHashmap = new HashMap<>();

			// 节点名称
			String jdmc = empj_BldLimitAmount_AF.getCurrentFigureProgress();
			paramsHashmap.put("jdmc", null == jdmc ? "": jdmc);

			// 业务编号
			String ywbh = empj_BldLimitAmount_AF.geteCode();
			paramsHashmap.put("ywbh", null == ywbh ? "": ywbh);

			// 项目名称
			Empj_ProjectInfo project = empj_BldLimitAmount_AF.getProject();
			paramsHashmap.put("xmmc", null == project.getTheName() ? "": project.getTheName());

			// 开发企业
			Emmp_CompanyInfo developCompany = empj_BldLimitAmount_AF.getDevelopCompany();
			paramsHashmap.put("kfqy", null == developCompany.getTheName() ? "": developCompany.getTheName());

			// 楼幢号
			String eCodeOfBuilding = empj_BldLimitAmount_AF.geteCodeOfBuilding();
			paramsHashmap.put("lzh", null == eCodeOfBuilding ? "": eCodeOfBuilding);

			Empj_BuildingInfo building = empj_BldLimitAmount_AF.getBuilding();
			
			// 地上总层数
			Double dszcs = 0.0;
			// 托管面积
			Double tgmj = 0.0;
			// 房屋类型
			String fwlx = "";
			// 物价备案均价
			Double bajj = 0.0;
			// 托管标准
			String tgbz = "";
			
			if( null != building)
			{
				dszcs = building.getUpfloorNumber();
				
				tgmj = building.getEscrowArea();
				
				String deliveryType = building.getDeliveryType();
				if(null != deliveryType && "1".equals(deliveryType))
				{
					fwlx = "毛坯房";
				}
				else if(null != deliveryType && "2".equals(deliveryType))
				{
					fwlx = "成品房";
				}
				
				Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
				
				if( null != buildingAccount)
				{
					bajj = buildingAccount.getRecordAvgPriceOfBuilding();
				}
				
				tgbz = building.getEscrowStandard();
			}

			// 地上总层数 
			paramsHashmap.put("dszcs", null == dszcs ? "": dszcs.toString());

			// 托管面积
			paramsHashmap.put("tgmj", null == tgmj ? "": tgmj.toString());

			// 房屋类型
			paramsHashmap.put("fwlx", null == fwlx ? "": fwlx);

			// 物价备案均价
			paramsHashmap.put("bajj", null == bajj ? "": bajj.toString());
			
			// 托管标准
			paramsHashmap.put("tgbz", null == tgbz ? "": tgbz);
			
			
			// 初始受限额度
			Double cssxed = empj_BldLimitAmount_AF.getOrgLimitedAmount();
			paramsHashmap.put("cssxed", null == cssxed ? "": cssxed.toString());
			
			// 当前形象进度
			String dqxxjd = empj_BldLimitAmount_AF.getCurrentFigureProgress();
			paramsHashmap.put("dqxxjd", null == dqxxjd ? "": dqxxjd);
			
			// 当前受限比例
			Double dqsxbl = empj_BldLimitAmount_AF.getCurrentLimitedRatio();
			paramsHashmap.put("dqsxbl", null == dqsxbl ? "": dqsxbl.toString()+"%");
			
			// 当前受限额度
			Double dqsxed = empj_BldLimitAmount_AF.getEffectiveLimitedAmount();
			paramsHashmap.put("dqsxed", null == dqsxed ? "": dqsxed.toString());
			
			// 拟变更形象进度
			String nbgxxjd = "";			
			
			Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress = empj_BldLimitAmount_AF.getExpectFigureProgress();
			if(null != expectFigureProgress)
			{
				nbgxxjd = expectFigureProgress.getStageName();
				
			}
			
			// 拟变更形象进度
			paramsHashmap.put("nbgxxjd", null == nbgxxjd ? "": nbgxxjd);
			
			// 拟变更受限比例
			Double nbgsxbl = empj_BldLimitAmount_AF.getExpectLimitedRatio();
			paramsHashmap.put("nbgsxbl", null == dqsxbl ? "": nbgsxbl.toString());
		
			// 拟调整受限额度
			Double nbgsxed = empj_BldLimitAmount_AF.getExpectLimitedAmount();
			paramsHashmap.put("nbgsxed", null == nbgsxed ? "": nbgsxed.toString());
			
			// 开发企业联系人
//			String lxr = empj_BldLimitAmount_AF.getRiskAssessment();
//			paramsHashmap.put("dqsxbl", null == dqsxbl ? "": dqsxbl);
			
			// 联系人电话
//			String lxdh = empj_BldLimitAmount_AF.getRiskAssessment();
//			paramsHashmap.put("dqsxbl", null == dqsxbl ? "": dqsxbl);		
			
			// 项目部负责人意见
			String xmbfzryj = "同意！";
			paramsHashmap.put("xmbfzryj", null == xmbfzryj ? "": xmbfzryj);

			
			

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

			// 查询模板名称 受限额度 500207
			Sm_BaseParameterForm baseModel = new Sm_BaseParameterForm();
			baseModel.setTheState(S_TheState.Normal);
			baseModel.setParametertype("50");
			baseModel.setTheValue("500207");

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
