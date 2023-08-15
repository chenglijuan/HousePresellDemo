package zhishusz.housepresell.service;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_ApprovalProcess_AFForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.database.dao.Sm_ApprovalProcess_AFDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_AF;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Workflow;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_Permission_Role;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service打印：退房退款-贷款已结清 输出PDF打印
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_RefundInfoPrintService
{
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;// 基础参数
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;
	@Autowired
	private Sm_ApprovalProcess_AFDao sm_ApprovalProcess_AFDao;

	String busiCode = "06120201";// 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();

		// 获取查询条件
		Long tableId = model.getTableId();// 退房退款Id

		if (tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "未查询到有效的退房退款数据，请核对查询条件");
		}

		model.setTheState(S_TheState.Normal);

		Tgpf_RefundInfo tgpf_RefundInfo = tgpf_RefundInfoDao.findById(tableId);
		if (tgpf_RefundInfo == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的退房退款数据，请核对查询条件");
		}

		HashMap<String, String> paramsHashmap = new HashMap<String, String>();
		paramsHashmap.put("ywbh", tgpf_RefundInfo.geteCode());// 业务编号

		String companyName = null;
		if (null != tgpf_RefundInfo.getProject())
		{
			if (null != tgpf_RefundInfo.getProject().getDevelopCompany())
			{
				companyName = tgpf_RefundInfo.getProject().getDevelopCompany().getTheName();
			}
		}
		paramsHashmap.put("kfqymc", companyName == null ? "" : companyName);// 开发企业
		paramsHashmap.put("xmmc",
				tgpf_RefundInfo.getTheNameOfProject() == null ? "" : tgpf_RefundInfo.getTheNameOfProject());// 项目名称
		paramsHashmap.put("fwzl",
				tgpf_RefundInfo.getPositionOfBuilding() == null ? "" : tgpf_RefundInfo.getPositionOfBuilding());// 房屋坐落
		paramsHashmap.put("htbah",
				tgpf_RefundInfo.geteCodeOfContractRecord() == null ? "" : tgpf_RefundInfo.geteCodeOfContractRecord());// 合同备案号
		paramsHashmap.put("msr",
				tgpf_RefundInfo.getTheNameOfBuyer() == null ? "" : tgpf_RefundInfo.getTheNameOfBuyer());// 买受人名称
		paramsHashmap.put("lxdh",
				tgpf_RefundInfo.getContactPhoneOfBuyer() == null ? "" : tgpf_RefundInfo.getContactPhoneOfBuyer());// 联系电话
		paramsHashmap.put("msrzjh", tgpf_RefundInfo.getCertificateNumberOfBuyer() == null ? ""
				: tgpf_RefundInfo.getCertificateNumberOfBuyer());// 买受人证件号
		paramsHashmap.put("zjkr",
				tgpf_RefundInfo.getTheNameOfCreditor() == null ? "" : tgpf_RefundInfo.getTheNameOfCreditor());// 主借款人

		String codeOfContractRecord = tgpf_RefundInfo.geteCodeOfContractRecord();

		String loanBank = "";
		Double loanAmount = 0.0;
		if (null == codeOfContractRecord || codeOfContractRecord.length() < 1)
		{
			Tgxy_ContractInfoForm contractForm = new Tgxy_ContractInfoForm();
			contractForm.setTheState(S_TheState.Normal);
			Tgxy_ContractInfo contractInfo = tgxy_ContractInfoDao
					.findOneByQuery_T(tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(), contractForm));
			if (contractInfo != null)
			{
				loanBank = contractInfo.getLoanBank();
				loanAmount = contractInfo.getLoanAmount();
			}
		}
		paramsHashmap.put("ajdkyh", loanBank);// 按揭贷款银行
		paramsHashmap.put("ajdkje", Double.toString(loanAmount));// 按揭贷款银行
		paramsHashmap.put("tkje",
				Double.toString(tgpf_RefundInfo.getRefundAmount() == 0.00 ? 0.00 : tgpf_RefundInfo.getRefundAmount()));// 退款金额
		paramsHashmap.put("zdskf", tgpf_RefundInfo.getReceiverName() == null ? "" : tgpf_RefundInfo.getReceiverName());// 指定收款方
		paramsHashmap.put("skyh",
				tgpf_RefundInfo.getReceiverBankName() == null ? "" : tgpf_RefundInfo.getReceiverBankName());// 收款银行

		Sm_ApprovalProcess_AFForm form = new Sm_ApprovalProcess_AFForm();
		form.setTheState(S_TheState.Normal);
		form.setBusiCode(busiCode);

		Sm_ApprovalProcess_AF approvalProcess_AF = sm_ApprovalProcess_AFDao
				.findOneByQuery_T(sm_ApprovalProcess_AFDao.getQuery(sm_ApprovalProcess_AFDao.getBasicHQL(), form));

		String xmspyj = "";
		String xmqm = "";
		String xmrq = "";
		String cwspyj = "";
		String cwqm = "";
		String cwrq = "";
		String cwzjspyj = "";
		String cwzjqm = "";
		String cwzjrq = "";
		String gscnqm = "";
		String gscnzfrq = "";

		/*if (null != approvalProcess_AF)
		{
			List<Sm_ApprovalProcess_Workflow> workFlowList = approvalProcess_AF.getWorkFlowList();// 审批流程
			
			for (Sm_ApprovalProcess_Workflow sm_ApprovalProcess_Workflow : workFlowList)
			{
				Sm_Permission_Role sm_Permission_Role = sm_ApprovalProcess_Workflow.getRole();//审批角色
				
				//判断用户角色
				
			}
		}*/

		// 项目部意见
		paramsHashmap.put("xmspyj", xmspyj);// 审批意见
		paramsHashmap.put("xmqm", xmqm);// 签名
		paramsHashmap.put("xmrq", xmrq);// 日期

		// 财务部意见
		paramsHashmap.put("cwhkyh", tgpf_RefundInfo.getRefundBankName() == null ? "" : tgpf_RefundInfo.getRefundBankName());// 本次划款银行
		paramsHashmap.put("cwhkzh", tgpf_RefundInfo.getRefundBankAccount() == null ? "" : tgpf_RefundInfo.getRefundBankName());// 本次划款账户
		paramsHashmap.put("cwhksqje", Double.toString(tgpf_RefundInfo.getRefundAmount() == 0.00 ? 0.00 : tgpf_RefundInfo.getRefundAmount()));// 本次划款申请金额
		paramsHashmap.put("cwspyj", cwspyj);// 审批意见
		paramsHashmap.put("cwqm", cwqm);// 签名
		paramsHashmap.put("cwrq", cwrq);// 日期

		// 财务部总监意见
		paramsHashmap.put("cwzjspyj", cwzjspyj);// 审批意见
		paramsHashmap.put("cwzjqm", cwzjqm);// 签名
		paramsHashmap.put("cwzjrq", cwzjrq);// 日期

		// 公司出纳
		paramsHashmap.put("gscnqm", gscnqm);// 签名
		paramsHashmap.put("gscnzfrq", gscnzfrq);// 支付日期

		// 创建导出路径
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String localPath = directoryUtil.getProjectRoot();// 项目路径
		if (localPath.contains("file:/"))
		{
			localPath = localPath.replace("file:/", "");
		}

		// D:/Workspaces/MyEclipse%202017%20CI/.metadata/.me_tcat85/webapps/HousePresell/
		String saveDirectory = localPath + "print/";// 文件在服务器文件系统中的完整路径

		if (saveDirectory.contains("%20"))
		{
			saveDirectory = saveDirectory.replace("%20", " ");
		}

		directoryUtil.mkdir(saveDirectory);

		String strNewFileName = MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss")
				+ ".pdf";

		String saveFilePath = saveDirectory + strNewFileName;

		// 查询模板名称 退房退款-贷款已结清 500203
		Sm_BaseParameterForm baseModel = new Sm_BaseParameterForm();
		baseModel.setTheState(S_TheState.Normal);
		baseModel.setParametertype("50");
		baseModel.setTheValue("500203");

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

//		WordAdmin print = new WordAdmin(replace2 + parameter.getTheName().trim(), parameter1.getTheName().trim());

		String replace = saveFilePath.replace("/", "\\\\");

		// 图片
		HashMap<Integer, String> picHashMap = new HashMap<>();

		String picPath = saveDirectory + "‪‪图片1.jpg";
		picHashMap.put(1, picPath.replace("/", "\\\\"));

//		print.admin(replace, paramsHashmap, null, null, picHashMap, paramsHashmap, null);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("url", "print/" + strNewFileName);

		return properties;
	}
}
