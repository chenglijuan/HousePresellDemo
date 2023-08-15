package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：三方协议打印
 * Company：ZhiShuSZ
 * 
 * 打印完成将协议状态置为：1-已打印未上传（打印三方协议）
 */
@Service
@Transactional
public class Tgxy_TripleAgreementPrintService
{
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	public Properties execute(Tgxy_TripleAgreementForm model)
	{
		Properties properties = new MyProperties();

		String strNewFileName;
		try
		{
			Long tgxy_TripleAgreementId = model.getTableId();
			Tgxy_TripleAgreement tgxy_TripleAgreement = (Tgxy_TripleAgreement) tgxy_TripleAgreementDao
					.findById(tgxy_TripleAgreementId);
			if (tgxy_TripleAgreement == null || S_TheState.Deleted.equals(tgxy_TripleAgreement.getTheState()))
			{
				return MyBackInfo.fail(properties, "该信息已失效，请刷新后重试");
			}

			/*
			 * 获取合同备案号
			 * 根据合同备案号查询合同信息
			 */
			// Tgxy_ContractInfo tgxy_ContractInfo = new Tgxy_ContractInfo();
			// String codeOfContractRecord =
			// tgxy_TripleAgreement.geteCodeOfContractRecord();
			// List<Tgxy_BuyerInfo> tgxy_BuyerInfoList = new
			// ArrayList<Tgxy_BuyerInfo>();
			// if (null != codeOfContractRecord &&
			// !codeOfContractRecord.trim().isEmpty())
			// {
			// Tgxy_ContractInfoForm conForm = new Tgxy_ContractInfoForm();
			// conForm.seteCodeOfContractRecord(codeOfContractRecord);
			// Object conQuery = tgxy_ContractInfoDao
			// .findOneByQuery(tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(),
			// conForm));
			//
			// if (null != conQuery)
			// {
			// tgxy_ContractInfo = (Tgxy_ContractInfo) conQuery;
			// }
			//
			// /*
			// * 根据合同备案号查询买受人信息
			// *
			// */
			// Tgxy_BuyerInfoForm buyForm = new Tgxy_BuyerInfoForm();
			// buyForm.seteCodeOfContract(codeOfContractRecord);
			//
			// tgxy_BuyerInfoList = tgxy_BuyerInfoDao
			// .findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(),
			// buyForm));
			//
			// }

			// 构建map，用于打印输出
			HashMap<String, String> signMao = new HashMap<>();
//			HashMap<Integer, String> picMap = new HashMap<>();
			HashMap<String, String> headMap = new HashMap<>();

			/*
			 * 商品房买卖合同编号：
			 * 协议编号：
			 * 出卖人：
			 * 买受人：
			 * 项目名称：
			 * 施工编号：
			 * 公安编号：
			 * 合同建筑面积：
			 * 合同总金额：
			 * 首付款：
			 * 【幢】 【座】  【单元】 【室】
			 */
			// 合同编号
			String htbh = tgxy_TripleAgreement.geteCodeOfContractRecord();
			headMap.put("htbh", htbh);
			// 三方协议号
			String sfxyh = tgxy_TripleAgreement.geteCodeOfTripleAgreement();
			headMap.put("sfxyh", sfxyh);
			// 出卖人
			String cmr = tgxy_TripleAgreement.getSellerName();
			signMao.put("cmr", cmr);
			// 买受人
			String msr = tgxy_TripleAgreement.getBuyerName();
			signMao.put("msr", msr);
			// 项目名称
			String xmmc = tgxy_TripleAgreement.getTheNameOfProject();
			signMao.put("xmmc", xmmc);
			// 施工编号
			String sgbh = tgxy_TripleAgreement.geteCodeFromConstruction();
			signMao.put("sgbh", sgbh);

			// 合同建筑面积
			Double jzmj = tgxy_TripleAgreement.getBuildingArea();
			signMao.put("jzmj", String.valueOf(null==jzmj?0.00:jzmj));
			// 合同总金额
			Double htje = tgxy_TripleAgreement.getContractAmount();
			signMao.put("htje", String.valueOf(null==htje?0.00:htje));
			// 首付款
			Double sfk = tgxy_TripleAgreement.getFirstPayment();
			signMao.put("sfk", String.valueOf(null==sfk?0.00:sfk));

			// 房屋信息
			Empj_HouseInfo houseInfo = tgxy_TripleAgreement.getHouse();
			// 坐落
			String fwzl = "";
			// 公安编号
			String gabh = "";
			if (null != houseInfo)
			{
				fwzl = houseInfo.getPosition();
				signMao.put("fwzl", fwzl);
				gabh = null==houseInfo.geteCodeFromPublicSecurity()?"":houseInfo.geteCodeFromPublicSecurity();
				signMao.put("gabh", gabh);
			}
			else
			{
				signMao.put("fwzl", "");
				signMao.put("gabh", "");
			}
			
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

			// 查询模板名称 三方协议 500205
			Sm_BaseParameterForm baseModel = new Sm_BaseParameterForm();
			baseModel.setTheState(S_TheState.Normal);
			baseModel.setParametertype("50");
			baseModel.setTheValue("500205");

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
//			print.admin(replace, signMao, null, null, null, headMap, null);

			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			properties.put("url", "print/" + strNewFileName);
			
			//如果打印时的状态是：0-未打印，则修改状态
			if("0".equals(tgxy_TripleAgreement.getTheStateOfTripleAgreement())){
				
				//将三方协议状态置为:1-已打印未上传
				tgxy_TripleAgreement.setTheStateOfTripleAgreement("1");
				
			}
			
			tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
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
