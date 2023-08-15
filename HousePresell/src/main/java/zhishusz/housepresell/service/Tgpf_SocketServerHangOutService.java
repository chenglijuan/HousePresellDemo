package zhishusz.housepresell.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zhishusz.housepresell.controller.form.Emmp_BankBranchForm;
import zhishusz.housepresell.controller.form.Emmp_BankInfoForm;
import zhishusz.housepresell.controller.form.Empj_PresellDocumentInfoForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.controller.form.Tgpf_BankUploadDataDetailForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.controller.form.Tgpf_SerialNumberForm;
import zhishusz.housepresell.controller.form.Tgpf_SocketMsgForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.controller.form.Tgxy_BuyerInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_ContractInfoForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Empj_PresellDocumentInfoDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.dao.Tgpf_BankUploadDataDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_SerialNumberDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.dao.Tgxy_BuyerInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_ContractInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PresellDocumentInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgpf_BankUploadDataDetail;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.Tgpf_SerialNumber;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_IDType;
import zhishusz.housepresell.database.po.state.S_SocketErrCode;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.SocketUtil;

/**
 * Service更新操作：被动接口服务分发
 * 
 * @author wuyu
 * @since 2018年8月10日17:08:00
 */
@Service
@Transactional
public class Tgpf_SocketServerHangOutService
{
	
	private static final String SEPARATE = "|";
	private static final String PADDING = StringUtils.SPACE;
	
	private static final String Deposit_BUSI_CODE = "06120102";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	private static final String BankUpload_BUSI_CODE = "200202";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;	//三方协议
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;				//接口报文表
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;			//基础数据-城市区域表
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao; //托管账户
	@Autowired
	private Tgpf_BankUploadDataDetailDao tgpf_BankUploadDataDetailDao; //银行对账单数据
	@Autowired
	private Empj_PresellDocumentInfoDao empj_PresellDocumentInfoDao;// 预售许可证表
	@Autowired
	private Tgxy_ContractInfoDao tgxy_ContractInfoDao;// 预售合同表
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;// 银行网点表
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;// 银行信息
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao;
	@Autowired
	private Tgpf_SerialNumberDao tgpf_SerialNumberDao;// 流水号
	@Autowired
	private Tgxy_BuyerInfoDao tgxy_BuyerInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	MyDatetime dateTime = MyDatetime.getInstance();
	MyDouble myDouble = MyDouble.getInstance();

	public String sendMessage(Object message)
	{
	
		String receiveMsg = (String) message;
		//返回报文
		String returnMsg = "";
		// 生成中心平台流水号
		String eCodeFromCenterPlatform = "";
		// 总包长|区位代码|交易代码|银行编码|......|
		try{
			String[] recMsg = SocketUtil.msgAnalysis(receiveMsg);
			
			if(recMsg.length < 3)
			{
				String[] respMsg = new String[4];
				respMsg[0] = "00"; // 区位码
				respMsg[1] = "1001"; // 交易码
				respMsg[2] = "9999"; // 返回码
				respMsg[3] = "报文信息错误，域长错误！"; // 区域代码不存在
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
			
			eCodeFromCenterPlatform = getECodeFromCenterPlatform();
			
			// 报文入表
			tgpf_SocketMsgAddFun(receiveMsg, returnMsg, eCodeFromCenterPlatform);
			
			// 检测 包长和 MD5		
			String[] checkData = new String[3];
			checkData[0] = recMsg[0];
			checkData[1] = recMsg[2];
			checkData[2] = recMsg[recMsg.length-2];
			String checkMsg = SocketUtil.checkPackage((String)message, checkData);
			if(!"true".equals(checkMsg))
			{
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = String.valueOf((Integer.parseInt(recMsg[2])+100)); // 交易码
				respMsg[2] = checkMsg; // 返回码
				respMsg[3] = S_SocketErrCode.getMsg(checkMsg); // 区域代码不存在
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
			
			// 校验区域代码
			if(!checkLocationCode(recMsg[2]))
			{
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = String.valueOf((Integer.parseInt(recMsg[2])+100)); // 交易码
				respMsg[2] = "0002"; // 返回码
				respMsg[3] = S_SocketErrCode.getMsg("0002"); // 区域代码不存在
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
			

			
			// 根据交易代码分发服务
			switch(recMsg[2])
			{
				case "1001": // 三方协议信息查询[1001]
					returnMsg = TriAgreementManager(receiveMsg, eCodeFromCenterPlatform);
					break;
				case "1002": // 缴款记账[1002]
					returnMsg = AdvPaymentManager(receiveMsg, eCodeFromCenterPlatform);
					break;
				case "1008": // 缴款记账记录修改[1008]
					returnMsg = AdvPayMsgUpdateManager(receiveMsg, eCodeFromCenterPlatform);
					break;
				case "1092": // 缴款冲正[1092]
					returnMsg = AdvPayReverseManager(receiveMsg, eCodeFromCenterPlatform);
					break;
				case "4001": // 交易明细日终对账单上传[4001]
					returnMsg = BsnBillUploadManager(receiveMsg, eCodeFromCenterPlatform);
					break;
				case "6001": // 预售许可证信息查询[6001]
					returnMsg = PrsLicenseQuery(receiveMsg, eCodeFromCenterPlatform);
					break;
				case "6002": // 预售合同查询[6002]
					returnMsg = PrsContractQuery(receiveMsg, eCodeFromCenterPlatform);
					break;
				default: // 返回错误报文
					String[] respMsg = new String[4];
					respMsg[0] = recMsg[1]; // 区位码
					respMsg[1] = String.valueOf((Integer.parseInt(recMsg[2])+100)); // 交易码
					respMsg[2] = "0003"; // 返回码
					respMsg[3] = "交易代码不存在"; // 区域代码不存在
					returnMsg = SocketUtil.getFailureMsg(respMsg);
					break;
			}
			
		}	
		catch (Exception e)
		{

			String[] recMsg = SocketUtil.msgAnalysis(receiveMsg);
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = String.valueOf((Integer.parseInt(recMsg[2])+100)); // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "数据库数据异常！"; // 区域代码不存在
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			System.out.println("reback errormessage1 ：" + e.getMessage());
		}
		finally
		{
			// 报文入表
			try
			{
				tgpf_SocketMsgAddFun(receiveMsg, returnMsg, eCodeFromCenterPlatform);
			}
			catch (Exception e)
			{
				String[] recMsg = SocketUtil.msgAnalysis(receiveMsg);
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = String.valueOf((Integer.parseInt(recMsg[2])+100)); // 交易码
				respMsg[2] = "9999"; // 返回码
				respMsg[3] = "数据库数据异常！"; // 区域代码不存在
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				// TODO Auto-generated catch block
				System.out.println("reback errormessage2 ：" + e.getMessage());
			}
		}
		
		return returnMsg;		
	}
	
	/**
	 * 三方协议信息查询应答[1101]
	 * @author 苏州智数
	 * @param 报文信息
	 * @return 返回的报文信息
	 */
	private String TriAgreementManager(String message,String eCodeFromCenterPlatform)
	{
		// 总包长|区位代码|交易代码|银行编码|三方协议号|操作时间|银行网点号|银行柜员号|结算渠道|MD5验证|
		String[] recMsg = SocketUtil.msgAnalysis(message);
		// 返回报文
		String returnMsg = "";
		
		Emmp_BankBranchForm bankBranchModel = new Emmp_BankBranchForm();
		bankBranchModel.seteCode(StringUtils.isBlank(recMsg[6])?"":recMsg[6]);
		bankBranchModel.setTheState(S_TheState.Normal);
		List<Emmp_BankBranch> bankBranchList = emmp_BankBranchDao.findByPage(emmp_BankBranchDao.getQuery(emmp_BankBranchDao.getBasicHQL(), bankBranchModel));
		Emmp_BankBranch emmp_BankBranch = bankBranchList.get(0);
		
		Tgxy_BankAccountEscrowedForm bankAccountModel = new Tgxy_BankAccountEscrowedForm();
		bankAccountModel.setBankBranchId(emmp_BankBranch.getTableId());
		bankAccountModel.setBankBranch(emmp_BankBranch);
		bankAccountModel.setTheState(S_TheState.Normal);
		List<Tgxy_BankAccountEscrowed> bankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountModel));

		// 查询条件：1.三方协议 2.未删除
		Tgxy_TripleAgreementForm agreementForm = new Tgxy_TripleAgreementForm();
		agreementForm.seteCodeOfTripleAgreement(recMsg[4]); // 三方协议号
		agreementForm.setTheState(S_TheState.Normal); // 状态为正常

		// 查询三方协议信息
		Integer totalCount = tgxy_TripleAgreementDao.findByPage_Size(
				tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
		Tgxy_TripleAgreement tripleAgreement = new Tgxy_TripleAgreement();

		// 校验三方协议是否存在
		if (totalCount > 0)
		{
			List<Tgxy_TripleAgreement> tripleAgreements = tgxy_TripleAgreementDao
					.findByPage(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
			tripleAgreement = tripleAgreements.get(0);
		}
		else
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1101"; // 交易码
			respMsg[2] = "0014"; // 返回码
			respMsg[3] = S_SocketErrCode.getMsg("0014"); // 托管项目交易合同备案号/三方协议无效
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		if( null == tripleAgreement.getBusiState() || S_BusiState.NoRecord.equals(tripleAgreement.getBusiState()) )
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1101"; // 交易码
			respMsg[2] = "0014"; // 返回码
			respMsg[3] = "三方协议未备案！"; // 托管项目交易合同备案号/三方协议无效
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		if( null == tripleAgreement.getTheStateOfTripleAgreementEffect() || "2".equals(tripleAgreement.getTheStateOfTripleAgreementEffect()) || "3".equals(tripleAgreement.getTheStateOfTripleAgreementEffect()) )
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1101"; // 交易码
			respMsg[2] = "0014"; // 返回码
			respMsg[3] = "三方协议已失效！"; // 托管项目交易合同备案号/三方协议无效
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		

		String buyerName = tripleAgreement.getBuyerName().replace("，",",");
		
		// 查询条件：1.三方协议 2.未删除 3.未红冲 4.资金性质为贷款
		Tgpf_DepositDetailForm depositDetailAmount = new Tgpf_DepositDetailForm();
		depositDetailAmount.setTripleAgreement(tripleAgreement);// 三方协议主键
		depositDetailAmount.setTheState(S_TheState.Normal); // 状态为正常
		depositDetailAmount.setTheStateFromReverse(0);// 未红冲
//		depositDetailAmount.setJudgeStatement("(1,2,3)"); // 1.商业贷款 2.公积金贷款
															// 3.公转商贷款
		// 查询总贷款金额
		String queryCondition = " nvl(sum(loanAmountFromBank),0) ";

		Double loanAmount = (Double) tgpf_DepositDetailDao.findOneByQuery(tgpf_DepositDetailDao
				.getSpecialQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailAmount, queryCondition));
	
		
		Double loanTotalAmount = 0.0;

		if( null != tripleAgreement.getLoanAmount())
		{
			loanTotalAmount = tripleAgreement.getLoanAmount();
		}		

		Double contractAmount = tripleAgreement.getContractAmount();// 合同金额
//		Double totalAmountOfHouse = (tripleAgreement.getTotalAmountOfHouse() == null) ? 0
//				: tripleAgreement.getTotalAmountOfHouse();// 户入账金额
		// 合同成交金额
		String contractAmountToString = myDouble
				.doubleToString(myDouble.doubleMultiplyDouble(contractAmount, new Double(100)), 0);
		
		// 协议应交款金额
		String payAmount = "0";
		if( loanTotalAmount > loanAmount )
		{
			payAmount  = myDouble.doubleToString(myDouble.doubleMultiplyDouble(myDouble.doubleSubtractDouble(loanTotalAmount, loanAmount), new Double(100)),
					0);
		}

		// 查询条件：1.三方协议 2.未删除
		Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
		depositDetailForm.setTripleAgreement(tripleAgreement);// 三方协议主键
		depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常

		// 查询资金归集明细表
		Integer detailCount = tgpf_DepositDetailDao.findByPage_Size(
				tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
		List<Tgpf_DepositDetail> tgpf_DepositDetailList;
		String eCodeFromPaymentMax = "0"; // 当不存在明细是，交款序号默认为0
		String theNameOfBankAccountEscrowed = "";// 托管账户名称
		String theAccountOfBankAccountEscrowed = "";// 托管账户
		if (detailCount > 0)
		{
			tgpf_DepositDetailList = tgpf_DepositDetailDao
					.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
			// 查询最大的缴款序号
			String queryIndexCondition = " nvl(max(eCodeFromPayment),0) ";
			eCodeFromPaymentMax = (String) tgpf_DepositDetailDao.findOneByQuery(tgpf_DepositDetailDao
					.getSpecialQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm, queryIndexCondition));
			Tgpf_DepositDetail tgpf_DepositDetail = tgpf_DepositDetailList.get(0);
			theNameOfBankAccountEscrowed = tgpf_DepositDetail.getTheNameOfBankAccountEscrowed();
			theAccountOfBankAccountEscrowed = tgpf_DepositDetail.getTheAccountOfBankAccountEscrowed();
		}

		String[] respMsg = new String[13];
		respMsg[0] = recMsg[1]; // 区位码
		respMsg[1] = "1101"; // 交易码
		respMsg[2] = "0000"; // 返回码
		respMsg[3] = "正确"; // 备注
		respMsg[4] = tripleAgreement.geteCodeOfContractRecord(); // 预购合同编号
		respMsg[5] = tripleAgreement.getSellerName(); // 预售人名称
		respMsg[6] = buyerName; // 预购人名称
		respMsg[7] = recMsg[4]; // 三方协议编号
		respMsg[8] = String.valueOf((Integer.parseInt(eCodeFromPaymentMax) + 1)); // 交款序号
		respMsg[9] = myDouble.doubleToString(myDouble.doubleMultiplyDouble(loanAmount, new Double(100)), 0); // 本三方协议下对应的累计已缴款金额
		respMsg[10] = contractAmountToString; // 合同成交价格
		/*respMsg[11] = "随享存对公数据19"; // 托管账户名称
		respMsg[12] = "646760148"; // 托管账号
*/		
		respMsg[11] = theNameOfBankAccountEscrowed; // 托管账户名称
        respMsg[12] = theAccountOfBankAccountEscrowed; // 托管账号
		if(null != bankAccountEscrowedList && bankAccountEscrowedList.size() > 0){
		    respMsg[11] = bankAccountEscrowedList.get(0).getTheName(); // 托管账户名称
	        respMsg[12] = bankAccountEscrowedList.get(0).getTheAccount(); // 托管账号
		}
		

		returnMsg = SocketUtil.getSuccessMsg_1101(respMsg);

		return returnMsg;
	}
	
	/**
	 * 缴款记账应答[1102]
	 * @author 苏州智数
	 * @param 报文信息
	 * @return 返回的报文信息
	 */
	private String AdvPaymentManager(String message,String eCodeFromCenterPlatform)
	{
		// 总包长|区位代码|交易代码|银行编码|三方协议编号|缴款序号|预售合同编号|缴款金额|手续费|入账金额|资金性质|银行代码|托管帐号名称|托管帐号|结算方式|缴款人名称|证件号|帐号|票据号码|记账日期时间|银行核心记账流水号|银行平台流水号|银行网点号|银行柜员号|结算渠道|缴存方式|MD5验证|
		String[] recMsg = SocketUtil.msgAnalysis(message);
		// 返回信息
		String returnMsg = "";

		// 校验是否重复请求（判断流水号-银行平台流水号）
		if (!checkMsgSerialno(recMsg[21]))
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1102"; // 交易码
			respMsg[2] = "0005"; // 返回码
			respMsg[3] = S_SocketErrCode.getMsg("0005"); // 银行平台流水号重复
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

		// 查询条件：1.三方协议 2.未删除
		Tgxy_TripleAgreementForm agreementForm = new Tgxy_TripleAgreementForm();
		agreementForm.seteCodeOfTripleAgreement(recMsg[4]); // 三方协议号
		agreementForm.setTheState(S_TheState.Normal); // 状态为正常

		// 查询三方协议信息
		Integer totalCount = tgxy_TripleAgreementDao.findByPage_Size(
				tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
		Tgxy_TripleAgreement tripleAgreement = new Tgxy_TripleAgreement();

		// 校验三方协议是否存在
		if (totalCount > 0)
		{
			List<Tgxy_TripleAgreement> tripleAgreements = tgxy_TripleAgreementDao
					.findByPage(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
			tripleAgreement = tripleAgreements.get(0);
		}
		else
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1102"; // 交易码
			respMsg[2] = "0014"; // 返回码
			respMsg[3] = S_SocketErrCode.getMsg("0014"); // 托管项目交易合同备案号/三方协议无效
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

		// 查询条件：1.三方协议 2.未删除
		Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
		depositDetailForm.setTripleAgreement(tripleAgreement);// 三方协议主键
		depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常

		// 查询资金归集明细表
		Integer detailCount = tgpf_DepositDetailDao.findByPage_Size(
				tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
		List<Tgpf_DepositDetail> tgpf_DepositDetailList;
		String eCodeFromPaymentMax = "0"; // 当不存在明细是，交款序号默认为0
//		String theNameOfBankAccountEscrowed = "";// 托管账户名称
//		String theAccountOfBankAccountEscrowed = "";// 托管账户
		if (detailCount > 0)
		{
			tgpf_DepositDetailList = tgpf_DepositDetailDao
					.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
			// 查询最大的缴款序号
			String queryCondition = " nvl(max(eCodeFromPayment),0) ";
			eCodeFromPaymentMax = (String) tgpf_DepositDetailDao.findOneByQuery(tgpf_DepositDetailDao
					.getSpecialQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm, queryCondition));
//			Tgpf_DepositDetail tgpf_DepositDetail = tgpf_DepositDetailList.get(0);
//			theNameOfBankAccountEscrowed = tgpf_DepositDetail.getTheNameOfBankAccountEscrowed();
//			theAccountOfBankAccountEscrowed = tgpf_DepositDetail.getTheAccountOfBankAccountEscrowed();
//
//			// 校核报文中的托管账户和名称是否正确
//			if (!theNameOfBankAccountEscrowed.equals(recMsg[12]) || !theAccountOfBankAccountEscrowed.equals(recMsg[13]))
//			{
//				String[] respMsg = new String[4];
//				respMsg[0] = recMsg[1]; // 区位码
//				respMsg[1] = "1102"; // 交易码
//				respMsg[2] = "0006"; // 返回码
//				respMsg[3] = "请确认托管账户和托管账户名称是否正确（与上一次缴款的托管账户不一致）"; // 托管账户和托管账户名称错误
//				returnMsg = SocketUtil.getFailureMsg(respMsg);
//				return returnMsg;
//			}
//
//			// 通过托管账户和账户名称进行查询，表中是否存在
//			// 查询条件：1.三方协议 2.未删除 3.托管账户名称 4.托管账户
//			depositDetailForm.setTheNameOfBankAccountEscrowed(theNameOfBankAccountEscrowed);
//			depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccountOfBankAccountEscrowed);
//			Integer accountNum = tgpf_DepositDetailDao.findByPage_Size(
//					tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
//			if (accountNum != detailCount)
//			{
//				String[] respMsg = new String[4];
//				respMsg[0] = recMsg[1]; // 区位码
//				respMsg[1] = "1102"; // 交易码
//				respMsg[2] = "0006"; // 返回码
//				respMsg[3] = "请确认托管账户和托管账户名称是否正确（与上一次缴款的托管账户不一致）"; // 托管账户和托管账户名称在系统中未录入
//				returnMsg = SocketUtil.getFailureMsg(respMsg);
//				return returnMsg;
//			}
		}

		// 托管账户表
		// 查询条件：1.托管账户名称 2.托管账户 3.未删除
		Tgxy_BankAccountEscrowedForm bankAccountEscrowedForm = new Tgxy_BankAccountEscrowedForm();
//		bankAccountEscrowedForm.setTheName(recMsg[12]);// 托管账户名称
		bankAccountEscrowedForm.setTheAccount(recMsg[13]);// 托管账户
		bankAccountEscrowedForm.setTheState(S_TheState.Normal); // 状态为正常
		Integer accountCount = tgxy_BankAccountEscrowedDao.findByPage_Size(tgxy_BankAccountEscrowedDao
				.getQuery_Size(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));

		// 校核托管账户是否存在
		List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList;
		Tgxy_BankAccountEscrowed bankAccountEscrowed = new Tgxy_BankAccountEscrowed();
		Emmp_BankInfo bankInfo = new Emmp_BankInfo();
		if (accountCount > 0)
		{
			tgxy_BankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao
					.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));
			bankAccountEscrowed = tgxy_BankAccountEscrowedList.get(0);

			// 校核贷款银行和托管账户银行是否为同一家
			bankInfo = bankAccountEscrowed.getBank();
			if (Integer.parseInt(recMsg[3]) != Integer.parseInt(bankInfo.getBankNo()))
			{
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = "1102"; // 交易码
				respMsg[2] = "0016"; // 返回码
				respMsg[3] = S_SocketErrCode.getMsg("0016"); // 存在他行贷款
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
		}
		else
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1102"; // 交易码
			respMsg[2] = "0006"; // 返回码
			respMsg[3] = "请确认托管账户和托管账户名称是否正确（与中心端不符）"; // 缴款序号已经存在
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

		// 校验缴款序号是否正确
		if (!recMsg[5].equals(String.valueOf((Integer.parseInt(eCodeFromPaymentMax) + 1))))
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1102"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "请核对缴款序号是否正确！"; // 缴款序号已经存在
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}	

		// 查询条件：1.三方协议 2.未删除 3.未红冲 4.资金性质为贷款
		Tgpf_DepositDetailForm depositDetailAmount = new Tgpf_DepositDetailForm();
		depositDetailAmount.setTripleAgreement(tripleAgreement);// 三方协议主键
		depositDetailAmount.setTheState(S_TheState.Normal); // 状态为正常
		depositDetailAmount.setTheStateFromReverse(0);// 未红冲
		depositDetailAmount.setJudgeStatement("(1,2,3)"); // 1.商业贷款 2.公积金贷款
															// 3.公转商贷款
		// 查询总贷款金额
		String queryCondition = " nvl(sum(loanAmountFromBank),0) ";

		Double loanAmount = (Double) tgpf_DepositDetailDao.findOneByQuery(tgpf_DepositDetailDao
				.getSpecialQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailAmount, queryCondition));

		Double contractAmount = tripleAgreement.getContractAmount();// 合同金额
//		Double totalAmountOfHouse = (tripleAgreement.getTotalAmountOfHouse() == null) ? 0
//				: tripleAgreement.getTotalAmountOfHouse();// 户入账总金额

		// 当前缴款金额
		Double currentAmount = myDouble.getShort(myDouble.div(new Double(recMsg[9]), new Double(100),4), 2);
		// 缴款后的入账总金额
		Double currentTotalAmount = myDouble.doubleAddDouble(currentAmount, loanAmount);

		System.out.println("loanAmount="+loanAmount);
		System.out.println("contractAmount="+contractAmount);
		System.out.println("currentAmount="+currentAmount);
		System.out.println("currentTotalAmount="+currentTotalAmount);


		// 校验是否已经业务对账
		// 如果已经存在贷款，判断是否已经业务对账
		if( loanAmount > 0 )
		{
			// 如果全部业务对账，就不允许对账
			// 对账列表
			// 查询条件 ：1.托管账户 2.托管账号 3.日期 4.状态为正常
			Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
			tgpf_BalanceOfAccountForm.setTgxy_BankAccountEscrowed(bankAccountEscrowed);
			tgpf_BalanceOfAccountForm.setBillTimeStamp(recMsg[19].substring(0, 10));
			tgpf_BalanceOfAccountForm.setTheState(S_TheState.Normal);

			Integer balanceCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao
					.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
			
			List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
			Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = new Tgpf_BalanceOfAccount();
			// 有记录，为更新
			if (balanceCount > 0) {
				tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao
						.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
				tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(0);
				
				if( null != tgpf_BalanceOfAccount.getCenterTotalCount() && tgpf_BalanceOfAccount.getCenterTotalCount() > 0 
						&& null != tgpf_BalanceOfAccount.getReconciliationState() && tgpf_BalanceOfAccount.getReconciliationState() == 1)
				{
					String[] respMsg = new String[4];
					respMsg[0] = recMsg[1]; // 区位码
					respMsg[1] = "1102"; // 交易码
					respMsg[2] = "9999"; // 返回码
					respMsg[3] = "托管账户已进行业务对账，如需继续缴款，请联系中心先进行撤销对账！"; // 银行平台流水号重复
					returnMsg = SocketUtil.getFailureMsg(respMsg);
					return returnMsg;
				}			
			}
			
		}

		// 判断是否超额 合同金额 >= 户入账总金额 + 当前缴款金额
		if (contractAmount < currentTotalAmount)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1102"; // 交易码
			respMsg[2] = "0004"; // 返回码
			respMsg[3] = S_SocketErrCode.getMsg("0004"); // 金额超限
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

		// 判断资金性质 非自有资金时
//		if (!recMsg[10].equals("0"))
//		{
//			// 实际贷款总金额
//			Double loanTotalAmount = myDouble.doubleAddDouble(loanAmount, currentAmount);
//			System.out.println("loanTotalAmount="+loanTotalAmount);
//			// 判断是否超额 实际贷款总金额<=合同金额的80%
//			if (loanTotalAmount > myDouble.doubleMultiplyDouble(contractAmount, new Double(0.80)))
//			{
//				String[] respMsg = new String[4];
//				respMsg[0] = recMsg[1]; // 区位码
//				respMsg[1] = "1102"; // 交易码
//				respMsg[2] = "0017"; // 返回码
//				respMsg[3] = S_SocketErrCode.getMsg("0017"); // 贷款金额超限
//				returnMsg = SocketUtil.getFailureMsg(respMsg);
//				return returnMsg;
//			}
//		}
		
		// 查询银行网点
		// 查询条件：1.银行网点号 2.状态：正常
		Emmp_BankBranchForm emmp_BankBranchForm = new Emmp_BankBranchForm();
		emmp_BankBranchForm.seteCode(recMsg[22]);
		emmp_BankBranchForm.setTheState(S_TheState.Normal); // 状态为正常
		Integer branchCount = emmp_BankBranchDao
				.findByPage_Size(emmp_BankBranchDao.getQuery_Size(emmp_BankBranchDao.getBasicHQL(), emmp_BankBranchForm));
		
		List<Emmp_BankBranch> emmp_BankBranchList;
		Emmp_BankBranch emmp_BankBranch= new Emmp_BankBranch();
		if (branchCount > 0)
		{
			emmp_BankBranchList = emmp_BankBranchDao.findByPage(emmp_BankBranchDao.getQuery(emmp_BankBranchDao.getBasicHQL(), emmp_BankBranchForm));
			emmp_BankBranch = emmp_BankBranchList.get(0);
		}
		else
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1102"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "网点信息有误"; // 贷款金额超限
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
//		tripleAgreement.setTgxy_BankAccountEscrowed(bankAccountEscrowed);
//		tgxy_TripleAgreementDao.save(tripleAgreement);
		
		Sm_User userStart = (Sm_User)sm_UserDao.findById(1l); // admin
		
		Tgpf_DepositDetail tgpf_DepositDetail = new Tgpf_DepositDetail();
		tgpf_DepositDetail.setTheState(S_TheState.Normal); // 状态为正常
		// tgpf_DepositDetail.setBusiState(busiState); //业务状态
		 tgpf_DepositDetail.seteCode(sm_BusinessCodeGetService.execute(Deposit_BUSI_CODE)); //编号
		tgpf_DepositDetail.setUserStart(userStart); //创建人
		tgpf_DepositDetail.setCreateTimeStamp(System.currentTimeMillis()); //创建时间
		tgpf_DepositDetail.setUserUpdate(userStart);// 修改人
		tgpf_DepositDetail.setLastUpdateTimeStamp(System.currentTimeMillis());//最后修改时间
		// tgpf_DepositDetail.setUserRecord(userRecord); //备案人
		// tgpf_DepositDetail.setRecordTimeStamp(recordTimeStamp); //备案日期
		tgpf_DepositDetail.seteCodeFromPayment(recMsg[5]); // 缴款序号
		tgpf_DepositDetail.setFundProperty(Integer.parseInt(recMsg[10]));// 资金性质
		tgpf_DepositDetail.setBankAccountEscrowed(bankAccountEscrowed); //托管账户信息
		tgpf_DepositDetail.setBankBranch(bankAccountEscrowed.getBankBranch()); // 托管账户开户行
		tgpf_DepositDetail.setTheNameOfBankAccountEscrowed(recMsg[12]);// 托管账户名称
		tgpf_DepositDetail.setTheAccountOfBankAccountEscrowed(recMsg[13]);// 托管账号
		tgpf_DepositDetail.setTheNameOfCreditor(recMsg[15]); // 贷款人
		tgpf_DepositDetail.setIdType(S_IDType.ResidentIdentityCard); //证件类型 S_IDType
		tgpf_DepositDetail.setIdNumber(recMsg[16]); // 证件号码
		tgpf_DepositDetail.setBankAccountForLoan(recMsg[17]); // 用于接收贷款的银行账号
		tgpf_DepositDetail.setLoanAmountFromBank(currentAmount);// 银行放款金额（元）
		tgpf_DepositDetail.setBillTimeStamp(recMsg[19].substring(0, 10)); // 记账日期
		tgpf_DepositDetail.seteCodeFromBankCore(recMsg[20]); // 银行核心流水号
		tgpf_DepositDetail.seteCodeFromBankPlatform(recMsg[21]); // 银行平台流水号tgpf_depositdetail
		// tgpf_DepositDetail.setRemarkFromDepositBill(remarkFromDepositBill);//缴款记账备注
		tgpf_DepositDetail.setTheNameOfBankBranchFromDepositBill(emmp_BankBranch);//缴费银行网点
		tgpf_DepositDetail.seteCodeFromBankWorker(recMsg[23]); // 网点柜员号
		tgpf_DepositDetail.setDepositState(1); // 缴款状态
//		tgpf_DepositDetail.setDayEndBalancing(new Tgpf_DayEndBalancing()); //关联日终结算
		tgpf_DepositDetail.setDepositDatetime(recMsg[19].substring(0, 10)); // 缴款记账日期
		// tgpf_DepositDetail.setReconciliationTimeStampFromBusiness(reconciliationTimeStampFromBusiness);//业务对账日期
		tgpf_DepositDetail.setReconciliationStateFromBusiness(0); // 业务对账状态
		// tgpf_DepositDetail.setReconciliationTimeStampFromCyberBank(reconciliationTimeStampFromCyberBank);//网银对账日期
		tgpf_DepositDetail.setReconciliationStateFromCyberBank(0); // 网银对账状态
		tgpf_DepositDetail.setHasVoucher(false); // 是否已生成凭证
		// tgpf_DepositDetail.setTimestampFromReverse(timestampFromReverse);//红冲-日期
		tgpf_DepositDetail.setTheStateFromReverse(0); // 红冲-状态
		// tgpf_DepositDetail.seteCodeFromReverse(eCodeFromReverse); //红冲-平台流水号
		tgpf_DepositDetail.setTripleAgreement(tripleAgreement);//三方协议
		tgpf_DepositDetailDao.save(tgpf_DepositDetail);

		String[] respMsg = new String[6];
		respMsg[0] = recMsg[1]; // 区位码
		respMsg[1] = "1102"; // 交易码
		respMsg[2] = "0000"; // 返回码
		respMsg[3] = "正确"; // 备注
		respMsg[4] = eCodeFromCenterPlatform; // 管理系统流水号
		respMsg[5] = recMsg[21]; // 银行端平台流水号

		returnMsg = SocketUtil.getSuccessMsg_1102(respMsg);

		return returnMsg;
	}
	
	
	/**
	 * 缴款记账信息修改[1108]
	 * @author 苏州智数
	 * @param 报文信息
	 * @return 返回的报文信息
	 */
	private String AdvPayMsgUpdateManager(String message,String eCodeFromCenterPlatform)
	{
		// 总包长|区位代码|交易代码|银行编码|三方协议编号|缴款序号|预售合同编号|缴款金额|手续费|入账金额|资金性质|银行代码|托管帐号名称|托管帐号|结算方式|缴款人名称|证件号|帐号|票据号码|记账日期时间|银行核心记账流水号|银行平台流水号|银行网点号|银行柜员号|结算渠道|缴存方式|MD5验证|
		String[] recMsg = SocketUtil.msgAnalysis(message);
		// 返回信息
		String returnMsg = "";
		
			
		
		// 查询条件：1.三方协议 2.未删除
		Tgxy_TripleAgreementForm agreementForm = new Tgxy_TripleAgreementForm();
		agreementForm.seteCodeOfTripleAgreement(recMsg[4]); // 三方协议号
		agreementForm.setTheState(S_TheState.Normal); // 状态为正常

		// 查询三方协议信息
		Integer totalCount = tgxy_TripleAgreementDao.findByPage_Size(
				tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
		Tgxy_TripleAgreement tripleAgreement = new Tgxy_TripleAgreement();

		// 校验三方协议是否存在
		if (totalCount > 0)
		{
			List<Tgxy_TripleAgreement> tripleAgreements = tgxy_TripleAgreementDao
					.findByPage(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
			tripleAgreement = tripleAgreements.get(0);
		}
		else
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1108"; // 交易码
			respMsg[2] = "0014"; // 返回码
			respMsg[3] = S_SocketErrCode.getMsg("0014"); // 托管项目交易合同备案号/三方协议无效
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

		// 查询条件：1.三方协议 2.未删除
		Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
		depositDetailForm.setTripleAgreement(tripleAgreement);// 三方协议主键
		depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常

		// 查询资金归集明细表
		Integer detailCount = tgpf_DepositDetailDao.findByPage_Size(
				tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
		List<Tgpf_DepositDetail> tgpf_DepositDetailList;
		String eCodeFromPaymentMax = "0"; // 当不存在明细是，交款序号默认为0
		String theNameOfBankAccountEscrowed = "";// 托管账户名称
		String theAccountOfBankAccountEscrowed = "";// 托管账户
		if (detailCount > 0)
		{
			tgpf_DepositDetailList = tgpf_DepositDetailDao
					.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
			// 查询最大的缴款序号
			String queryCondition = " nvl(max(eCodeFromPayment),0) ";
			eCodeFromPaymentMax = (String) tgpf_DepositDetailDao.findOneByQuery(tgpf_DepositDetailDao
					.getSpecialQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm, queryCondition));
//			Tgpf_DepositDetail tgpf_DepositDetail = tgpf_DepositDetailList.get(0);
//			theNameOfBankAccountEscrowed = tgpf_DepositDetail.getTheNameOfBankAccountEscrowed();
//			theAccountOfBankAccountEscrowed = tgpf_DepositDetail.getTheAccountOfBankAccountEscrowed();
//
//			// 校核报文中的托管账户和名称是否正确
//			if (!theNameOfBankAccountEscrowed.equals(recMsg[12]) || !theAccountOfBankAccountEscrowed.equals(recMsg[13]))
//			{
//				String[] respMsg = new String[4];
//				respMsg[0] = recMsg[1]; // 区位码
//				respMsg[1] = "1108"; // 交易码
//				respMsg[2] = "0006"; // 返回码
//				respMsg[3] = "请确认托管账户和托管账户名称是否正确（与上一次缴款的托管账户不一致）"; // 托管账户和托管账户名称错误
//				returnMsg = SocketUtil.getFailureMsg(respMsg);
//				return returnMsg;
//			}
//
//			// 通过托管账户和账户名称进行查询，表中是否存在
//			// 查询条件：1.三方协议 2.未删除 3.托管账户名称 4.托管账户
//			depositDetailForm.setTheNameOfBankAccountEscrowed(theNameOfBankAccountEscrowed);
//			depositDetailForm.setTheAccountOfBankAccountEscrowed(theAccountOfBankAccountEscrowed);
//			Integer accountNum = tgpf_DepositDetailDao.findByPage_Size(
//					tgpf_DepositDetailDao.getQuery_Size(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));
//			if (accountNum != detailCount)
//			{
//				String[] respMsg = new String[4];
//				respMsg[0] = recMsg[1]; // 区位码
//				respMsg[1] = "1108"; // 交易码
//				respMsg[2] = "0006"; // 返回码
//				respMsg[3] = "请确认托管账户和托管账户名称是否正确（与上一次缴款的托管账户不一致）"; // 托管账户和托管账户名称在系统中未录入
//				returnMsg = SocketUtil.getFailureMsg(respMsg);
//				return returnMsg;
//			}
		}

		// 托管账户表
		// 查询条件：1.托管账户名称 2.托管账户 3.未删除
		Tgxy_BankAccountEscrowedForm bankAccountEscrowedForm = new Tgxy_BankAccountEscrowedForm();
//		bankAccountEscrowedForm.setTheName(recMsg[12]);// 托管账户名称
		bankAccountEscrowedForm.setTheAccount(recMsg[13]);// 托管账户
		bankAccountEscrowedForm.setTheState(S_TheState.Normal); // 状态为正常
		Integer accountCount = tgxy_BankAccountEscrowedDao.findByPage_Size(tgxy_BankAccountEscrowedDao
				.getQuery_Size(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));

		// 校核托管账户是否存在
		List<Tgxy_BankAccountEscrowed> tgxy_BankAccountEscrowedList;
		Tgxy_BankAccountEscrowed bankAccountEscrowed = new Tgxy_BankAccountEscrowed();
		Emmp_BankInfo bankInfo = new Emmp_BankInfo();
		if (accountCount > 0)
		{
			tgxy_BankAccountEscrowedList = tgxy_BankAccountEscrowedDao.findByPage(tgxy_BankAccountEscrowedDao
					.getQuery(tgxy_BankAccountEscrowedDao.getBasicHQL(), bankAccountEscrowedForm));
			bankAccountEscrowed = tgxy_BankAccountEscrowedList.get(0);

			// 校核贷款银行和托管账户银行是否为同一家
			bankInfo = bankAccountEscrowed.getBank();
			if (Integer.parseInt(recMsg[3]) != Integer.parseInt(bankInfo.getBankNo()))
			{
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = "1108"; // 交易码
				respMsg[2] = "0016"; // 返回码
				respMsg[3] = S_SocketErrCode.getMsg("0016"); // 存在他行贷款
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
		}
		else
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1108"; // 交易码
			respMsg[2] = "0006"; // 返回码
			respMsg[3] = "请确认托管账户和托管账户名称是否正确"; // 缴款序号已经存在
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

//		// 校验缴款序号是否正确
//		if (!recMsg[5].equals(String.valueOf((Integer.parseInt(eCodeFromPaymentMax) + 1))))
//		{
//			String[] respMsg = new String[4];
//			respMsg[0] = recMsg[1]; // 区位码
//			respMsg[1] = "1108"; // 交易码
//			respMsg[2] = "9999"; // 返回码
//			respMsg[3] = "请核对缴款序号是否正确！"; // 缴款序号已经存在
//			returnMsg = SocketUtil.getFailureMsg(respMsg);
//			return returnMsg;
//		}

		// 校验是否已经业务对账
		// 如果已经存在贷款，判断是否已经业务对账

			// 如果全部业务对账，就不允许对账
			// 对账列表
			// 查询条件 ：1.托管账户 2.托管账号 3.日期 4.状态为正常
		Tgpf_BalanceOfAccountForm tgpf_BalanceOfAccountForm = new Tgpf_BalanceOfAccountForm();
		tgpf_BalanceOfAccountForm.setTgxy_BankAccountEscrowed(bankAccountEscrowed);
		tgpf_BalanceOfAccountForm.setBillTimeStamp(recMsg[19].substring(0, 10));
		tgpf_BalanceOfAccountForm.setTheState(S_TheState.Normal);

		Integer balanceCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao
					.getQuery_Size(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
			
		List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
		Tgpf_BalanceOfAccount tgpf_BalanceOfAccount = new Tgpf_BalanceOfAccount();
			// 有记录，为更新
		if (balanceCount > 0) {
			tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao
						.getQuery(tgpf_BalanceOfAccountDao.getBasicHQL(), tgpf_BalanceOfAccountForm));
			tgpf_BalanceOfAccount = tgpf_BalanceOfAccountList.get(0);
				
			if( null != tgpf_BalanceOfAccount.getCenterTotalCount() && tgpf_BalanceOfAccount.getCenterTotalCount() > 0 
						&& null != tgpf_BalanceOfAccount.getReconciliationState() && tgpf_BalanceOfAccount.getReconciliationState() == 1)
			{
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = "1108"; // 交易码
				respMsg[2] = "9999"; // 返回码
				respMsg[3] = "托管账户已进行业务对账，如需修改，请联系中心先进行撤销对账！"; // 银行平台流水号重复
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}			
		}
			
	
		

		// 查询银行网点
		// 查询条件：1.银行网点号 2.状态：正常
		Emmp_BankBranchForm emmp_BankBranchForm = new Emmp_BankBranchForm();
		emmp_BankBranchForm.seteCode(recMsg[22]);
		emmp_BankBranchForm.setTheState(S_TheState.Normal); // 状态为正常
		Integer branchCount = emmp_BankBranchDao.findByPage_Size(
				emmp_BankBranchDao.getQuery_Size(emmp_BankBranchDao.getBasicHQL(), emmp_BankBranchForm));

		List<Emmp_BankBranch> emmp_BankBranchList;
		Emmp_BankBranch emmp_BankBranch = new Emmp_BankBranch();
		if (branchCount > 0)
		{
			emmp_BankBranchList = emmp_BankBranchDao
					.findByPage(emmp_BankBranchDao.getQuery(emmp_BankBranchDao.getBasicHQL(), emmp_BankBranchForm));
			emmp_BankBranch = emmp_BankBranchList.get(0);
		}
		else
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1108"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "网点信息有误"; // 贷款金额超限
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}



		Sm_User userStart = (Sm_User) sm_UserDao.findById(1l); // admin

		
		// 查询资金归集明细表
		// 查询条件：1.银行平台流水号 2.状态未删除
		Tgpf_DepositDetailForm detailForm = new Tgpf_DepositDetailForm();
		detailForm.seteCodeFromBankPlatform(recMsg[21]); // 原银行中心平台流水号
		detailForm.setTheState(S_TheState.Normal); // 状态为正常
		List<Tgpf_DepositDetail> depositDetailList = tgpf_DepositDetailDao
				.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), detailForm));

		// 校核是否存在需要红冲的记录
		if (depositDetailList == null || depositDetailList.size() == 0)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1108"; // 交易码
			respMsg[2] = "0012"; // 返回码
			respMsg[3] = "不存在需要修改的缴款记录，请核对原银行记账流水号";
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

		Tgpf_DepositDetail tgpf_DepositDetail = depositDetailList.get(0);
	
		// 判断是否已经业务对账
		
		if(tgpf_DepositDetail.getReconciliationStateFromBusiness() == 1 || tgpf_DepositDetail.getReconciliationStateFromCyberBank() == 1)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1108"; // 交易码
			respMsg[2] = "0012"; // 返回码
			respMsg[3] = "已经进行对账操作，不允许修改";
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		// 查询条件：1.三方协议 2.未删除 3.未红冲 4.资金性质为贷款
		Tgpf_DepositDetailForm depositDetailAmount = new Tgpf_DepositDetailForm();
		depositDetailAmount.setTripleAgreement(tripleAgreement);// 三方协议主键
		depositDetailAmount.setTheState(S_TheState.Normal); // 状态为正常
		depositDetailAmount.setTheStateFromReverse(0);// 未红冲
		depositDetailAmount.setJudgeStatement("(1,2,3)"); // 1.商业贷款 2.公积金贷款
															// 3.公转商贷款
		// 查询总贷款金额
		String queryCondition = " nvl(sum(loanAmountFromBank),0) ";

		Double loanAmount = (Double) tgpf_DepositDetailDao.findOneByQuery(tgpf_DepositDetailDao
				.getSpecialQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailAmount, queryCondition));

		Double contractAmount = tripleAgreement.getContractAmount();// 合同金额
		// Double totalAmountOfHouse = (tripleAgreement.getTotalAmountOfHouse()
		// == null) ? 0
		// : tripleAgreement.getTotalAmountOfHouse();// 户入账总金额

		// 当前缴款金额
		Double currentAmount = myDouble.getShort(myDouble.div(new Double(recMsg[9]), new Double(100), 4), 2);
		// 缴款后的入账总金额
		Double currentTotalAmount = myDouble.doubleSubtractDouble(myDouble.doubleAddDouble(currentAmount, loanAmount), tgpf_DepositDetail.getLoanAmountFromBank());

		// 判断是否超额 合同金额 >= 户入账总金额 + 当前缴款金额

		if (contractAmount < currentTotalAmount)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1108"; // 交易码
			respMsg[2] = "0004"; // 返回码
			respMsg[3] = S_SocketErrCode.getMsg("0004"); // 金额超限
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

		// 判断资金性质 非自有资金时
		if (!recMsg[10].equals("0"))
		{
			// 实际贷款总金额
			Double loanTotalAmount = myDouble.doubleSubtractDouble(myDouble.doubleAddDouble(loanAmount, currentAmount),tgpf_DepositDetail.getLoanAmountFromBank());
			// 判断是否超额 实际贷款总金额<=合同金额的80%
			if (loanTotalAmount > myDouble.doubleMultiplyDouble(contractAmount, new Double(0.80)))
			{
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = "1108"; // 交易码
				respMsg[2] = "0017"; // 返回码
				respMsg[3] = S_SocketErrCode.getMsg("0017"); // 贷款金额超限
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
		}

		
//		tripleAgreement.setTgxy_BankAccountEscrowed(bankAccountEscrowed);
//		tgxy_TripleAgreementDao.save(tripleAgreement);
		
		
		tgpf_DepositDetail.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改时间
//		tgpf_DepositDetail.seteCodeFromPayment(recMsg[5]); // 缴款序号
		tgpf_DepositDetail.setFundProperty(Integer.parseInt(recMsg[10]));// 资金性质
		tgpf_DepositDetail.setTheNameOfBankAccountEscrowed(recMsg[12]);// 托管账户名称
		tgpf_DepositDetail.setTheAccountOfBankAccountEscrowed(recMsg[13]);// 托管账号
		tgpf_DepositDetail.setTheNameOfCreditor(recMsg[15]); // 贷款人
		tgpf_DepositDetail.setIdNumber(recMsg[16]); // 证件号码
		tgpf_DepositDetail.setBankAccountForLoan(recMsg[17]); // 用于接收贷款的银行账号
		tgpf_DepositDetail.setLoanAmountFromBank(currentAmount);// 银行放款金额（元）
		tgpf_DepositDetail.setBillTimeStamp(recMsg[19].substring(0, 10)); // 记账日期
		tgpf_DepositDetail.seteCodeFromBankCore(recMsg[20]); // 银行核心流水号
		tgpf_DepositDetail.seteCodeFromBankPlatform(recMsg[21]); // 银行平台流水号
		tgpf_DepositDetail.seteCodeFromBankWorker(recMsg[23]); // 网点柜员号
		tgpf_DepositDetail.setDepositDatetime(recMsg[19].substring(0, 10)); // 缴款记账日期
		tgpf_DepositDetailDao.save(tgpf_DepositDetail);
		

		String[] respMsg = new String[6];
		respMsg[0] = recMsg[1]; // 区位码
		respMsg[1] = "1108"; // 交易码
		respMsg[2] = "0000"; // 返回码
		respMsg[3] = "正确"; // 备注
		respMsg[4] = eCodeFromCenterPlatform; // 管理系统流水号
		respMsg[5] = recMsg[21]; // 银行端平台流水号

		returnMsg = SocketUtil.getSuccessMsg_1102(respMsg);

		return returnMsg;

	}

	/**
	 * 缴款冲正应答[1192]
	 * @author 苏州智数
	 * @param 报文信息
	 * @return 返回的报文信息
	 */
	private String AdvPayReverseManager(String message,String eCodeFromCenterPlatform)
	{
		// 总包长|区位代码|交易代码|银行编码|三方协议编号|交款序号|交款金额|冲正时间|原银行记账流水号|原管理系统流水号|银行冲正流水号|银行网点号|银行柜员号|结算渠道|MD5验证|
		String[] recMsg = SocketUtil.msgAnalysis(message);
		// 返回报文
		String returnMsg = "";
		// 校验是否重复请求（判断流水号-银行平台流水号）
		if (!checkMsgSerialno(recMsg[10]))
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1192"; // 交易码
			respMsg[2] = "0005"; // 返回码
			respMsg[3] = S_SocketErrCode.getMsg("0005"); // 银行平台流水号重复
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

		// 查询资金归集明细表
		// 查询条件：1.银行平台流水号 2.状态未删除
		Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
		depositDetailForm.seteCodeFromBankPlatform(recMsg[8]); // 原银行中心平台流水号
		depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
		List<Tgpf_DepositDetail> tgpf_DepositDetailList = tgpf_DepositDetailDao
				.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));

		// 校核是否存在需要红冲的记录
		if (tgpf_DepositDetailList == null || tgpf_DepositDetailList.size() == 0)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1192"; // 交易码
			respMsg[2] = "0012"; // 返回码
			respMsg[3] = "不存在需要红冲的记录，请核对原银行记账流水号"; // 银行平台流水号重复
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		Sm_User userStart = (Sm_User)sm_UserDao.findById(1l); // admin

		Tgpf_DepositDetail tgpf_DepositDetail = tgpf_DepositDetailList.get(0);
		
		if(null != tgpf_DepositDetail.getTheStateFromReverse() && tgpf_DepositDetail.getTheStateFromReverse() == 1)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1192"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "该记录已红冲，请勿重复发起！"; // 银行平台流水号重复
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		if(null != tgpf_DepositDetail.getReconciliationStateFromBusiness() && tgpf_DepositDetail.getReconciliationStateFromBusiness() == 1)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1192"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "该记录已进行业务对账，如需红冲，请联系中心先进行撤销！"; // 银行平台流水号重复
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		if(null != tgpf_DepositDetail.getReconciliationStateFromCyberBank() && tgpf_DepositDetail.getReconciliationStateFromCyberBank() == 1)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "1192"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "该记录已进行网银对账，无法红冲，请联系中心先进行撤销！"; // 银行平台流水号重复
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}

		Date reverseTime = dateTime.parse(recMsg[7]);
		tgpf_DepositDetail.setUserUpdate(userStart);// 修改人
		tgpf_DepositDetail.setLastUpdateTimeStamp(System.currentTimeMillis());//最后修改时间
		tgpf_DepositDetail.setDepositState(2); // 缴款状态
		tgpf_DepositDetail.setTimestampFromReverse(recMsg[7]); // 红冲-日期
		tgpf_DepositDetail.setTheStateFromReverse(1); // 红冲-状态
		tgpf_DepositDetail.seteCodeFromReverse(recMsg[10]); // 红冲-平台流水号
		
		

		// 查询三方协议
		Tgxy_TripleAgreement tgxy_TripleAgreement = tgpf_DepositDetail.getTripleAgreement();
//		Double totalAmountOfHouse = tgxy_TripleAgreement.getTotalAmountOfHouse();// 户入账总金额
//		Double reverseAmount = Double.parseDouble(recMsg[6]); // 红冲金额
//		tgxy_TripleAgreement
//				.setTotalAmountOfHouse(MyDouble.getInstance().doubleSubtractDouble(totalAmountOfHouse, reverseAmount));
//		// 保存三方协议
//		tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);
		tgpf_DepositDetail.setTripleAgreement(tgxy_TripleAgreement);
		tgpf_DepositDetailDao.save(tgpf_DepositDetail);

		String[] respMsg = new String[6];
		respMsg[0] = recMsg[1]; // 区位码
		respMsg[1] = "1192"; // 交易码
		respMsg[2] = "0000"; // 返回码
		respMsg[3] = "正确"; // 备注
		respMsg[4] = eCodeFromCenterPlatform; // 管理系统流水号
		respMsg[5] = recMsg[10]; // 银行端平台流水号

		returnMsg = SocketUtil.getSuccessMsg_1192(respMsg);

		return returnMsg;
	}
	
	/**
	 * 交易明细日终对账单上传应答[4101]
	 * @author 苏州智数
	 * @param 报文信息
	 * @return 返回的报文信息
	 */
	private String BsnBillUploadManager(String message,String eCodeFromCenterPlatform)
	{
		// 总包长|区位代码|交易代码|银行编码|日期|交易总笔数|交易总金额|文件名|结算渠道|MD5验证|
		String[] recMsg =  SocketUtil.msgAnalysis(message);
		
		String returnMsg = "";
		
		// 校验报文中是否存在空值
		for(int i=0;i<recMsg.length;i++){
			if(recMsg[i]==null){
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = "4101"; // 交易码
				respMsg[2] = "9999"; // 返回码
				respMsg[3] = "报文中存在空值，请确认后重试！"; // 存在空值
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
		}
		
		String fileName = recMsg[7];// 报文中的文件名
		String tradeNum = recMsg[5];// 交易总笔数
		String strdate = recMsg[4].replace("-", "");// 交易日期
		String fileCheckName = "dzd" + recMsg[3] + "MX" + strdate.trim();// 本地拼的文件名
		String handleName = fileName.substring(0, fileName.length() - 6);	// 报文中处理后的文件名
		
		// 校验文件名是否正确
		if(!fileCheckName.equals(handleName))
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "4101"; // 交易码
			respMsg[2] = "0019"; // 返回码
			respMsg[3] = S_SocketErrCode.getMsg("0019"); // 文件名错误
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		// 查询银行信息
		// 查询条件：1. 银行编号 2.状态：正常
		Emmp_BankInfoForm emmp_BankInfoForm = new Emmp_BankInfoForm();
		emmp_BankInfoForm.setBankNo(recMsg[3]);
		emmp_BankInfoForm.setTheState(S_TheState.Normal);// 状态：正常
		
		Integer totalCount = emmp_BankInfoDao.findByPage_Size(emmp_BankInfoDao.getQuery_Size(emmp_BankInfoDao.getBasicHQL(), emmp_BankInfoForm));
		
		List<Emmp_BankInfo> emmp_BankInfoList;
		Emmp_BankInfo emmp_BankInfo = new Emmp_BankInfo();
		if(totalCount > 0)
		{
			emmp_BankInfoList = emmp_BankInfoDao.findByPage(emmp_BankInfoDao.getQuery(emmp_BankInfoDao.getBasicHQL(), emmp_BankInfoForm));
			emmp_BankInfo = emmp_BankInfoList.get(0);
		}
		
//		fileName = "D:/MX/"+fileName;

		try
		{
			// 读取文件，保存入表
			String checkMsg = readfile(fileName, tradeNum , emmp_BankInfo);
			if(checkMsg.startsWith("FTP") || checkMsg.startsWith("存在记录"))
			{
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = String.valueOf((Integer.parseInt(recMsg[2])+100)); // 交易码
				respMsg[2] = "9999"; // 返回码
				respMsg[3] = checkMsg;// 文件对账失败
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
			else if(!"true".equals(checkMsg))
			{
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = String.valueOf((Integer.parseInt(recMsg[2])+100)); // 交易码
				respMsg[2] = checkMsg; // 返回码
				respMsg[3] = S_SocketErrCode.getMsg(checkMsg);// 文件对账失败
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
			else
			{
				String[] respMsg = new String[19];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = "4101"; // 交易码
				respMsg[2] = "0000"; // 返回码
				respMsg[3] = "正确"; // 备注
				respMsg[4] = recMsg[7]; // 文件名
				returnMsg = SocketUtil.getSuccessMsg_4101(respMsg);
			}
		}
		catch (IOException e)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "4101"; // 交易码
			respMsg[2] = "0022"; // 返回码
			respMsg[3] = S_SocketErrCode.getMsg("0022"); // 失败
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		return returnMsg;
	}
	
	/**
	 * 预售许可证信息查询应答[6101]
	 * @author 苏州智数
	 * @param 报文信息
	 * @return 返回的报文信息
	 */
	private String PrsLicenseQuery(String message,String eCodeFromCenterPlatform)
	{
		// 总包长|区位代码|交易代码|银行编码|预售许可证编号|操作时间|银行网点号|银行柜员号|MD5验证|
		String[] recMsg =  SocketUtil.msgAnalysis(message);
		// 返回报文信息
		String returnMsg = "";
		
		// 查询预售许可证
		Empj_PresellDocumentInfoForm presellDocumentInfoForm = new Empj_PresellDocumentInfoForm();
		presellDocumentInfoForm.seteCode(recMsg[4]); // 原银行中心平台流水号
		presellDocumentInfoForm.setTheState(S_TheState.Normal); // 状态为正常
		
		// 判断是否存在预售许可证
		Integer presellDocumentCount = empj_PresellDocumentInfoDao.findByPage_Size(empj_PresellDocumentInfoDao.getQuery_Size(empj_PresellDocumentInfoDao.getBasicHQL(), presellDocumentInfoForm));
		List<Empj_PresellDocumentInfo> empj_PresellDocumentInfoList;
		Empj_PresellDocumentInfo  empj_PresellDocumentInfo = new Empj_PresellDocumentInfo();
		if(presellDocumentCount > 0)
		{
			empj_PresellDocumentInfoList = empj_PresellDocumentInfoDao.findByPage(empj_PresellDocumentInfoDao.getQuery(empj_PresellDocumentInfoDao.getBasicHQL(), presellDocumentInfoForm));
			empj_PresellDocumentInfo = empj_PresellDocumentInfoList.get(0);
		}
		else
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "6101"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "未查询到预售许可证！"; // 未查询到预售许可证 
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
				
		String queryTime = recMsg[5];//查询时间
		// 开发企业
		Emmp_CompanyInfo companyInfo = empj_PresellDocumentInfo.getDevelopCompany();
		// 项目
		Empj_ProjectInfo project = empj_PresellDocumentInfo.getProject();
		
		String[] respMsg = new String[19];
		respMsg[0] = recMsg[1]; // 区位码
		respMsg[1] = "6101"; // 交易码
		respMsg[2] = "0000"; // 返回码
		respMsg[3] = "正确"; // 备注
//		respMsg[4] = "0"; // 预售许可证状态
		respMsg[4] = null == empj_PresellDocumentInfo.getSaleState()?"0":empj_PresellDocumentInfo.getSaleState(); // 预售许可证状态
//		respMsg[5] = "0"; // 托管状态
		respMsg[5] = null == empj_PresellDocumentInfo.getEscorwState()?"0":empj_PresellDocumentInfo.getEscorwState(); // 托管状态
		respMsg[6] = recMsg[4]; // 预售许可证编号
		respMsg[7] = companyInfo.getTheName(); // 开发商名称
		respMsg[8] = project.getTheName(); // 项目名称
		respMsg[9] = empj_PresellDocumentInfo.getAddressOfProject(); // 项目地址
		respMsg[10] = null == empj_PresellDocumentInfo.getSaleRange()?"":empj_PresellDocumentInfo.getSaleRange(); // 预售范围
//		respMsg[11] = "";   //empj_PresellDocumentInfo.getCreateTimeStamp(); // 发证时间
		respMsg[11] = empj_PresellDocumentInfo.getCertificationDate();  // 发证时间
//		respMsg[12] = myDouble.doubleToString(project.getLandArea(), 2); // 预售总面积
		respMsg[12] = null == empj_PresellDocumentInfo.getSaleAreaCounts()?"0.00":empj_PresellDocumentInfo.getSaleAreaCounts();// 预售总面积
//		respMsg[13] = ""; // 预售总户数
		respMsg[13] = null == empj_PresellDocumentInfo.getSaleCounts()?"0":empj_PresellDocumentInfo.getSaleCounts(); // 预售总户数
		respMsg[14] = ""; // 备注一
		respMsg[15] = ""; // 备注二
		respMsg[16] = ""; // 备注三
		respMsg[17] = ""; // 备注四
		respMsg[18] = recMsg[5]; // 查询时间
		
		returnMsg = SocketUtil.getSuccessMsg_6101(respMsg);
		
		return returnMsg;	
	}
	
	/**
	 * 预售合同查询应答[6102]
	 * @author 苏州智数
	 * @param 报文信息
	 * @return 返回的报文信息
	 */
	private String PrsContractQuery(String message,String eCodeFromCenterPlatform)
	{
		// 总包长|区位代码|交易代码|银行编码|预售合同编号|三方协议编号|操作时间|银行网点号|银行柜员号|MD5验证|
		// "0180|0 |6002|03|446464                        |123456          |2018-08-22 10:03:49 |0301                          |1                             |94a60b986329e80b1f35a2df47fc6df0|
		String[] recMsg = SocketUtil.msgAnalysis(message);
		// 返回报文信息
		String returnMsg = "";
		//预售合同编号
		String eCodeOfContract = recMsg[4];
		//三方协议号
		String eCodeOfTripleAgreement = recMsg[5];
		
		Tgxy_ContractInfo tgxy_ContractInfo = new Tgxy_ContractInfo();
		
		Tgxy_TripleAgreement tripleAgreement = new Tgxy_TripleAgreement();
		
		if( null != eCodeOfContract && !"".equals(eCodeOfContract))
		{
			tgxy_ContractInfo = getContractInfo(eCodeOfContract);
			
			tripleAgreement = getTripleAgreementByConstract(eCodeOfContract);
		}
		else if( null != eCodeOfTripleAgreement && !"".equals(eCodeOfTripleAgreement))
		{
			tripleAgreement = getTripleAgreement(eCodeOfTripleAgreement);
			
			if( null == tripleAgreement)
			{
				String[] respMsg = new String[4];
				respMsg[0] = recMsg[1]; // 区位码
				respMsg[1] = "6102"; // 交易码
				respMsg[2] = "9999"; // 返回码
				respMsg[3] = "未查询到三方协议！"; // 托管项目交易合同备案号/三方协议无效
				returnMsg = SocketUtil.getFailureMsg(respMsg);
				return returnMsg;
			}
			
			eCodeOfContract = tripleAgreement.geteCodeOfContractRecord();
			
			if(null != eCodeOfContract && !"".equals(eCodeOfContract.trim()))
			{
				tgxy_ContractInfo = getContractInfo(eCodeOfContract);
			}
		}
		else
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "6102"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "请输入预售合同编号或三方协议编号！"; // 未查询到预售许可证 
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		if( null == tgxy_ContractInfo )
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "6102"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "未查询到预售合同！"; // 未查询到预售许可证 
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		if( null == tripleAgreement)
		{
			String[] respMsg = new String[4];
			respMsg[0] = recMsg[1]; // 区位码
			respMsg[1] = "6102"; // 交易码
			respMsg[2] = "9999"; // 返回码
			respMsg[3] = "未查询到三方协议！"; // 托管项目交易合同备案号/三方协议无效
			returnMsg = SocketUtil.getFailureMsg(respMsg);
			return returnMsg;
		}
		
		eCodeOfTripleAgreement = tripleAgreement.geteCodeOfTripleAgreement();		
		String companyName = "";
		String companyCode = "";
		// 预售人(开发企业)
		if(null != tripleAgreement.getProject() )
		{
			if( null != tripleAgreement.getProject().getDevelopCompany())
			{
				Emmp_CompanyInfo emmp_CompanyInfo = tripleAgreement.getProject().getDevelopCompany();
				if( null!= emmp_CompanyInfo)
				{
					companyName = emmp_CompanyInfo.getTheName();
					companyCode = emmp_CompanyInfo.geteCode();
				}
			}
		}
				
		// 合同备案号
		String eCodeOfContractRecord = eCodeOfContract;
				
		String buyerName = "";
		String buyerCode = "";
		// 查找买受人信息
		// 根据合同备案号查询买受人信息
		Tgxy_BuyerInfoForm tgxy_BuyerInfoForm = new Tgxy_BuyerInfoForm();

		tgxy_BuyerInfoForm.seteCodeOfContract(eCodeOfContractRecord);

		List<Tgxy_BuyerInfo> buyerInfos = tgxy_BuyerInfoDao
				.findByPage(tgxy_BuyerInfoDao.getQuery(tgxy_BuyerInfoDao.getBasicHQL(), tgxy_BuyerInfoForm));

		// 买受人名称
		StringBuffer theNameOfBuyer = new StringBuffer();
		// 买受人证件号码
		StringBuffer certificateNumberOfBuyer = new StringBuffer();
		
		if(null == buyerInfos || buyerInfos.size() < 0)
		{
			theNameOfBuyer.append(" ");
			certificateNumberOfBuyer.append(" ");
		}
		else
		{
			for (int i = 0; i < buyerInfos.size(); i++)
			{

				theNameOfBuyer.append(buyerInfos.get(i).getBuyerName());
				certificateNumberOfBuyer.append(buyerInfos.get(i).geteCodeOfcertificate());

				if (i != buyerInfos.size() - 1)
				{
					theNameOfBuyer.append(",");
					certificateNumberOfBuyer.append(",");
				}
			}
		}
	
		buyerName = theNameOfBuyer.toString();
		buyerCode = certificateNumberOfBuyer.toString();
		
		// 已入账总金额
		String totalAmountOfHouse = "0";
		// 托管金额
		String contractAmount = "0";
		if(tripleAgreement.getTotalAmountOfHouse()!=null) //		if((a>=-0.0001)&&a<=0.00001)) 
		{
			totalAmountOfHouse = myDouble.doubleToString(myDouble.doubleMultiplyDouble(tripleAgreement.getTotalAmountOfHouse(), new Double(100)), 0);
			contractAmount = myDouble.doubleToString(myDouble.doubleMultiplyDouble(tripleAgreement.getContractAmount(), new Double(100)), 0);
		}
		
		Empj_BuildingInfo buildingInfo = tripleAgreement.getBuildingInfo();
		
		Double loanAmount = 0.0;
		if( null != tgxy_ContractInfo.getLoanAmount())
		{
			loanAmount = tgxy_ContractInfo.getLoanAmount();
		}
		
		Double buildingArea = 0.0;
		if( null != tgxy_ContractInfo.getBuildingArea())
		{
			buildingArea = tgxy_ContractInfo.getBuildingArea();
		}
		
		Double contractSumPrice = 0.0;
		if( null != tgxy_ContractInfo.getContractSumPrice())
		{
			contractSumPrice = tgxy_ContractInfo.getContractSumPrice();
		}

		String[] respMsg = new String[24];
		respMsg[0] = recMsg[1]; // 区位码
		respMsg[1] = "6102"; // 交易码
		respMsg[2] = "0000"; // 返回码
		respMsg[3] = "正确"; // 备注
		respMsg[4] = eCodeOfContract; // 预售合同编号
		respMsg[5] = tgxy_ContractInfo.getBusiState(); // 合同状态
		respMsg[6] = tgxy_ContractInfo.getPosition(); // 房屋坐落
		respMsg[7] = myDouble.doubleToString(buildingArea,2); // 建筑面积
		respMsg[8] = myDouble.doubleToString(myDouble.doubleMultiplyDouble(contractSumPrice, new Double(100)), 0); // 合同金额
		respMsg[9] = myDouble.doubleToString(myDouble.doubleMultiplyDouble(loanAmount, new Double(100)), 0); // 贷款金额
		respMsg[10] = buildingInfo.getDecorationType(); // 是否装修
		respMsg[11] = companyName.replace("，",","); // 预售人名称
		respMsg[12] = companyCode.replace("，",","); // 预售人证号
		respMsg[13] = buyerName.replace("，",","); // 承购人名称
		respMsg[14] = buyerCode.replace("，",","); // 承购人证件号
		respMsg[15] = "0"; // 是否托管
		respMsg[16] = eCodeOfTripleAgreement; // 三方协议号
		respMsg[17] = tripleAgreement.getEscrowCompany(); // 托管机构名称
		respMsg[18] = "0"; // 托管状态
		respMsg[19] = contractAmount; // 托管金额
		respMsg[20] = totalAmountOfHouse; // 已缴款金额
		respMsg[21] = ""; // 备注一
		respMsg[22] = ""; // 备注二
		respMsg[23] = recMsg[6]; // 查询时间
		
		returnMsg = SocketUtil.getSuccessMsg_6102(respMsg);
	
		return returnMsg;
	}
	
	private Tgxy_TripleAgreement getTripleAgreement(String eCodeOfTripleAgreement)
	{
		// 查询条件：1.三方协议 2.未删除
		Tgxy_TripleAgreementForm agreementForm = new Tgxy_TripleAgreementForm();
		agreementForm.seteCodeOfTripleAgreement(eCodeOfTripleAgreement); // 三方协议号
		agreementForm.setTheState(S_TheState.Normal); // 状态为正常

		// 查询三方协议信息
		Integer totalCount = tgxy_TripleAgreementDao.findByPage_Size(
				tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
		Tgxy_TripleAgreement tripleAgreement = new Tgxy_TripleAgreement();

		// 校验三方协议是否存在
		if (totalCount > 0)
		{
			List<Tgxy_TripleAgreement> tripleAgreements = tgxy_TripleAgreementDao
					.findByPage(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
			tripleAgreement = tripleAgreements.get(0);
		}
		else
		{
			tripleAgreement = null;
		}
	
		return tripleAgreement;	
	}
	
	private Tgxy_TripleAgreement getTripleAgreementByConstract(String eCodeOfContract)
	{
		// 查询条件：1.三方协议 2.未删除
		Tgxy_TripleAgreementForm agreementForm = new Tgxy_TripleAgreementForm();
		agreementForm.seteCodeOfContractRecord(eCodeOfContract); // 三方协议号
		agreementForm.setTheState(S_TheState.Normal); // 状态为正常

		// 查询三方协议信息
		Integer totalCount = tgxy_TripleAgreementDao.findByPage_Size(
				tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
		Tgxy_TripleAgreement tripleAgreement = new Tgxy_TripleAgreement();

		// 校验三方协议是否存在
		if (totalCount > 0)
		{
			List<Tgxy_TripleAgreement> tripleAgreements = tgxy_TripleAgreementDao
					.findByPage(tgxy_TripleAgreementDao.getQuery(tgxy_TripleAgreementDao.getBasicHQL(), agreementForm));
			tripleAgreement = tripleAgreements.get(0);
		}
		else
		{
			tripleAgreement = null;
		}
	
		return tripleAgreement;	
	}
	
	
	private Tgxy_ContractInfo getContractInfo(String eCodeOfContractInfo)
	{
		// 查询预售合同
		// 查询条件：1.预售合同编号 2.状态：正常
		Tgxy_ContractInfoForm tgxy_ContractInfoForm = new Tgxy_ContractInfoForm();
		tgxy_ContractInfoForm.seteCodeOfContractRecord(eCodeOfContractInfo);// 预售合同编号
		tgxy_ContractInfoForm.setTheState(S_TheState.Normal); // 状态为正常
		
		Integer contractCount = tgxy_ContractInfoDao.findByPage_Size(tgxy_ContractInfoDao.getQuery_Size(tgxy_ContractInfoDao.getBasicHQL(), tgxy_ContractInfoForm));
		
		// 校验是否存在预售合同
		List<Tgxy_ContractInfo> tgxy_ContractInfoList;
		Tgxy_ContractInfo tgxy_ContractInfo = new Tgxy_ContractInfo();
		if(contractCount > 0)
		{
			tgxy_ContractInfoList = tgxy_ContractInfoDao.findByPage(tgxy_ContractInfoDao.getQuery(tgxy_ContractInfoDao.getBasicHQL(), tgxy_ContractInfoForm));
			tgxy_ContractInfo = tgxy_ContractInfoList.get(0);			
		}
		else
		{
			tgxy_ContractInfo = null;
		}
	
		return tgxy_ContractInfo;	
	}
	
	/**
	 * 校核区域代码
	 * @author 苏州智数
	 * @param code 区域代码
	 * @return 错误信息
	 */
	private boolean checkLocationCode(String locationCode)
	{
		Sm_CityRegionInfoForm cityRegionInfoForm = new Sm_CityRegionInfoForm();

		cityRegionInfoForm.seteCode(locationCode);// 区域代码
		cityRegionInfoForm.setTheState(S_TheState.Normal); // 状态为正常

		Integer totalCount = sm_CityRegionInfoDao.findByPage_Size(sm_CityRegionInfoDao.getQuery_Size(sm_CityRegionInfoDao.getBasicHQL(), cityRegionInfoForm));
		
		if (totalCount > 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * 校核流水号是否重复（银行平台流水号）
	 * @author 苏州智数
	 * @param code 区域代码
	 * @return 错误信息
	 */
	private boolean checkMsgSerialno(String msgSerialno)
	{
		Tgpf_SocketMsgForm socketMsgForm = new Tgpf_SocketMsgForm();

		socketMsgForm.setBankPlatformSerialNo(msgSerialno); // 银行平台流水号
		socketMsgForm.setTheState(S_TheState.Normal); // 状态为正常

		Integer totalCount = 1;
		totalCount = tgpf_SocketMsgDao
				.findByPage_Size(tgpf_SocketMsgDao.getQuery_Size(tgpf_SocketMsgDao.getBasicHQL(), socketMsgForm));

		if (totalCount > 1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * 保存报文方法
	 * @author 苏州智数
	 * @param reciveMsg 接受报文
	 * @param returnMsg 返回报文
	 * @param msgSerialno 中心平台流水号
	 */
	private void tgpf_SocketMsgAddFun(String reciveMsg , String returnMsg , String msgSerialno )
	{
		// 总包长|区位代码|交易代码|返回码|备注|....|
		String[] recMsg = SocketUtil.msgAnalysis(reciveMsg);
		String[] retMsg = new String[5];
		
		SocketUtil soc = SocketUtil.getInstance();
		
		String data = StringUtils.substring(reciveMsg.trim(), 5, reciveMsg.trim().length() - 33);

		Sm_User userStart = (Sm_User)sm_UserDao.findById(1l); // admin
		
		Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();

		// 公共字段
		tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
		// tgpf_SocketMsg.setBusiState(busiState);// 业务状态
		// tgpf_SocketMsg.seteCode(eCode);// 编号
		tgpf_SocketMsg.setUserStart(userStart);// 创建人
		tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
		tgpf_SocketMsg.setUserUpdate(userStart);
		tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
		// tgpf_SocketMsg.setUserRecord(userRecord);// 备案人
		// tgpf_SocketMsg.setRecordTimeStamp(recordTimeStamp);// 备案日期
		tgpf_SocketMsg.setMsgStatus(1);// 发送状态
		tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
		tgpf_SocketMsg.setMsgSerialno(msgSerialno);// 报文流水号-中心平台流水号
		tgpf_SocketMsg.setBankCode(recMsg[3]);// 银行编码

		// 根据交易代码保存特有的参数
		if (recMsg[2].equals("1001"))
		{
			tgpf_SocketMsg.seteCodeOfTripleAgreement(recMsg[4]);// 三方协议号
		}
		else if (recMsg[2].equals("1002") || recMsg[2].equals("1008"))
		{
			tgpf_SocketMsg.seteCodeOfTripleAgreement(recMsg[4]);// 三方协议号
			tgpf_SocketMsg.seteCodeOfContractRecord(recMsg[6]);// 预售合同编号
			tgpf_SocketMsg.setBankPlatformSerialNo(recMsg[21]);
		}
		else if (recMsg[2].equals("1092"))
		{
			tgpf_SocketMsg.seteCodeOfTripleAgreement(recMsg[4]);// 三方协议号
			tgpf_SocketMsg.setBankPlatformSerialNo(recMsg[10]);
		}
		else if (recMsg[2].equals("4001"))
		{

		}
		else if (recMsg[2].equals("6001"))
		{
			tgpf_SocketMsg.seteCodeOfPermitRecord(recMsg[4]);// 预售许可证编号
		}
		else if (recMsg[2].equals("6002"))
		{
			tgpf_SocketMsg.seteCodeOfTripleAgreement(recMsg[5]);// 三方协议号
			tgpf_SocketMsg.seteCodeOfContractRecord(recMsg[4]);// 预售合同编号
		}

		// 接受报文和返回报文
		if ("".equals(returnMsg))
		{
			tgpf_SocketMsg.setMsgLength(recMsg[0]);// 总包长
			tgpf_SocketMsg.setLocationCode(recMsg[1]);// 区位代码
			tgpf_SocketMsg.setMsgBusinessCode(recMsg[2]);// 交易代码
			tgpf_SocketMsg.setMsgDirection("B");// 报文方向
			tgpf_SocketMsg.setMd5Check(soc.md5(data));// MD5验证
			tgpf_SocketMsg.setMsgContent(reciveMsg);// 报文内容
		}
		else
		{
			// 解析返回报文
			retMsg = SocketUtil.msgAnalysis(returnMsg);

			tgpf_SocketMsg.setMsgLength(retMsg[0]);// 总包长
			tgpf_SocketMsg.setLocationCode(retMsg[1]);// 区位代码
			tgpf_SocketMsg.setMsgBusinessCode(retMsg[2]);// 交易代码
			tgpf_SocketMsg.setReturnCode(retMsg[3]);// 返回码
			tgpf_SocketMsg.setRemark(retMsg[4]);// 备注
			tgpf_SocketMsg.setMsgDirection("C");// 报文方向

			if ("0000".equals(retMsg[3]))
			{
				tgpf_SocketMsg.setMd5Check(retMsg[retMsg.length - 1]);// MD5验证
			}

			tgpf_SocketMsg.setMsgContent(returnMsg);// 报文内容

		}

		tgpf_SocketMsgDao.save(tgpf_SocketMsg);
	}
	
	/**
	 * 读取文件且数据库插入数据：
	 * 		读取文件--->查看该条记录的状态--->更新记录
	 * @param docpath 文件路径
	 * @param dealsumnum 交易笔数
	 * @return 成功返回"true";失败则返回错误代码
	 * @throws IOException
	 */
	@Transactional
	private String readfile(String docpath , String dealsumnum , Emmp_BankInfo emmp_BankInfo) throws IOException
	{
		Sm_User userStart = (Sm_User)sm_UserDao.findById(1l); // admin
		String isreadfile="true";
		int count=0;
		String saveDirectory = null;
		String ftpIp = "";
		int ftpPort = 21;
		String ftpName = "";
		String ftpPassword = "";
		
		try
		{
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = "MX";//文件在项目中的相对路径
			String localPath = directoryUtil.getProjectRoot();//项目路径
			
			saveDirectory = localPath + relativeDir;//文件在服务器文件系统中的完整路径
			
			saveDirectory = saveDirectory.replace("%20"," ");
			directoryUtil.mkdir(saveDirectory);
			
			int reply;
			FTPClient ftp = new FTPClient();
			
			ftpIp = getParameter("FTP_IP");
			ftpPort = Integer.parseInt(getParameter("FTP_PORT"));
			ftpName = getParameter("FTP_USER");
			ftpPassword = getParameter("FTP_PASSWORD");

			ftp.connect(ftpIp,ftpPort);// 连接FTP服务器 // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器 
			boolean flag = ftp.login(ftpName, ftpPassword);// 登录 
			
			if( !flag )
			{
				return isreadfile = "FTP登陆错误！";
			}

			reply = ftp.getReplyCode(); 
			if (!FTPReply.isPositiveCompletion(reply)) { 
				ftp.disconnect(); 
				System.out.println("FTP已连接");
			}

			boolean flag1 = ftp.changeWorkingDirectory("/");    // MX/
			
			System.out.println(flag1);

			FTPFile[] fs = ftp.listFiles();  
			
        	Emmp_BankBranch branch = new Emmp_BankBranch();
        	String billTimeStamp = null;
        	Map<String,String> keyMap = new HashMap<String,String>();
            
            for(FTPFile ff:fs){
            	

            	
                if(ff.getName().equals(docpath)){  
                	System.out.println("dd");
                    File localFile = new File(saveDirectory+"/"+ff.getName());    
                    OutputStream is = new FileOutputStream(localFile);     
                    ftp.retrieveFile(ff.getName(), is);  
                    is.close();
                      
                    File newLocalFile = new File(saveDirectory+"/"+ff.getName());
                    
                    // 判断是否存在文件
            		if(!newLocalFile.exists()||newLocalFile.isDirectory()){
            			return isreadfile="0022";
            		}          		
         		
            		//读取文件
            		BufferedReader br=new BufferedReader(new FileReader(newLocalFile));
            		String temp="";
            		
            		
            		while(temp!=null){
            			//数据解析
            			temp=br.readLine();
            			
            			if(temp== null){//读到null,返回   
            				break;  
            			}
            			count++;
            			temp = new String(temp.getBytes(), "utf-8");
            			
            			// 交易时间|账号|银行核心流水号|银行平台流水号|借贷方向|缴款金额|手续费|入账金额|摘要|三方协议编号|序号|平台交易流水号|结算渠道|。
            			// 2018-08-17 00:00:00|45645632132|21332132253|0320180817091622691|借|1300|0|1300|备注一|123456|20|01|1|
            			String[] accountMsg = SocketUtil.msgAnalysis(temp);
            			
            			// 初始化
            			Tgpf_BankUploadDataDetail tgpf_BankUploadDataDetail = new Tgpf_BankUploadDataDetail();
            			
            			// 查询网银对账单上传数据-如果存在记录，则更新
            			// 查询条件 1.银行平台流水号 2.状态：正常
            			Tgpf_BankUploadDataDetailForm tgpf_BankUploadDataDetailForm = new Tgpf_BankUploadDataDetailForm();
            			tgpf_BankUploadDataDetailForm.setBkpltNo(accountMsg[3]);
            			tgpf_BankUploadDataDetailForm.setTheState(S_TheState.Normal); // 状态为正常
            			Integer totalCount = tgpf_BankUploadDataDetailDao.findByPage_Size(tgpf_BankUploadDataDetailDao.getQuery_Size(tgpf_BankUploadDataDetailDao.getBasicHQL(), tgpf_BankUploadDataDetailForm));
            			
            			List<Tgpf_BankUploadDataDetail> tgpf_BankUploadDataDetailList;
            			if(totalCount > 0)
            			{
            				tgpf_BankUploadDataDetailList = tgpf_BankUploadDataDetailDao.findByPage(tgpf_BankUploadDataDetailDao.getQuery(tgpf_BankUploadDataDetailDao.getBasicHQL(), tgpf_BankUploadDataDetailForm));
            				tgpf_BankUploadDataDetail = tgpf_BankUploadDataDetailList.get(0);
            			}
            			
            			// 查询资金归集明细表
            			// 查询条件：1.银行平台流水号 2.状态未删除
            			Tgpf_DepositDetailForm depositDetailForm = new Tgpf_DepositDetailForm();
            			depositDetailForm.seteCodeFromBankPlatform(accountMsg[3]); // 原银行中心平台流水号
            			depositDetailForm.setTheState(S_TheState.Normal); // 状态为正常
            			List<Tgpf_DepositDetail> tgpf_DepositDetailList = tgpf_DepositDetailDao
            					.findByPage(tgpf_DepositDetailDao.getQuery(tgpf_DepositDetailDao.getBasicHQL(), depositDetailForm));

            			// 校核是否存在记录
            			if (tgpf_DepositDetailList == null || tgpf_DepositDetailList.size() == 0)
            			{
            				continue;
            			}

            			Tgpf_DepositDetail tgpf_DepositDetail = tgpf_DepositDetailList.get(0);
            			
            			if(null != tgpf_DepositDetail.getTheStateFromReverse() && tgpf_DepositDetail.getTheStateFromReverse() == 1)
            			{
            				return isreadfile="存在记录已红冲，请核实对账单！";
            			}
            			
//            			if(null != tgpf_DepositDetail.getReconciliationStateFromCyberBank() && tgpf_DepositDetail.getReconciliationStateFromCyberBank() == 1)
//            			{
//            				return isreadfile="存在记录已进行网银对账，请联系中心！";
//            			}
            		
            			if(null != tgpf_DepositDetail.getBankBranch())
            			{
                			branch = tgpf_DepositDetail.getBankBranch();
            			}
            			if(null != accountMsg[0] || accountMsg[0].length()>10)
            			{
            				billTimeStamp = accountMsg[0].substring(0,10);
            			}
           			
            			tgpf_BankUploadDataDetail.setTheState(S_TheState.Normal);//状态 S_TheState 初始为Normal
//            			tgpf_BankUploadDataDetail.setBusiState(busiState);//业务状态
            			tgpf_BankUploadDataDetail.seteCode(sm_BusinessCodeGetService.execute(BankUpload_BUSI_CODE));//编号
            			tgpf_BankUploadDataDetail.setUserStart(userStart);//创建人
            			tgpf_BankUploadDataDetail.setCreateTimeStamp(System.currentTimeMillis());//创建时间
            			tgpf_BankUploadDataDetail.setUserUpdate(userStart);
            			tgpf_BankUploadDataDetail.setLastUpdateTimeStamp(System.currentTimeMillis());//最后修改日期
//            			tgpf_BankUploadDataDetail.setUserRecord(userRecord);//备案人
//            			tgpf_BankUploadDataDetail.setRecordTimeStamp(recordTimeStamp);//备案日期
            			tgpf_BankUploadDataDetail.setBank(emmp_BankInfo);//银行
            			tgpf_BankUploadDataDetail.setTheNameOfBank(emmp_BankInfo.getTheName());//银行名称
            			tgpf_BankUploadDataDetail.setBankBranch(tgpf_DepositDetail.getBankBranch());//开户行
//            			tgpf_BankUploadDataDetail.setTheNameOfBankBranch(theNameOfBankBranch);//支行名称
            			tgpf_BankUploadDataDetail.setBankAccountEscrowed(tgpf_DepositDetail.getBankAccountEscrowed());//托管账号
            			tgpf_BankUploadDataDetail.setTheNameOfBankAccountEscrowed(tgpf_DepositDetail.getBankAccountEscrowed().getTheNameOfBank());//托管银行名称
            			tgpf_BankUploadDataDetail.setTheAccountBankAccountEscrowed(tgpf_DepositDetail.getTheNameOfBankAccountEscrowed());//托管账号名称
            			tgpf_BankUploadDataDetail.setTheAccountOfBankAccountEscrowed(tgpf_DepositDetail.getTheAccountOfBankAccountEscrowed());//托管账户账号
            			tgpf_BankUploadDataDetail.setTradeAmount(myDouble.getShort(myDouble.div(new Double(accountMsg[7]), new Double(100),4),2));//交易金额
            			tgpf_BankUploadDataDetail.setEnterTimeStamp(accountMsg[0].substring(0,10));//入账日期
            			tgpf_BankUploadDataDetail.setRecipientAccount(tgpf_DepositDetail.getBankAccountForLoan());//对方账号
            			tgpf_BankUploadDataDetail.setRecipientName(tgpf_DepositDetail.getTheNameOfCreditor());//对方名称
//            			tgpf_BankUploadDataDetail.setLastCfgUser(lastCfgUser);//配置人
//            			tgpf_BankUploadDataDetail.setLastCfgTimeStamp(lastCfgTimeStamp);//配置日期
            			tgpf_BankUploadDataDetail.setBkpltNo(accountMsg[3]);//银行平台流水号
            			tgpf_BankUploadDataDetail.seteCodeOfTripleAgreement(accountMsg[9]);//三方协议号22
            			tgpf_BankUploadDataDetail.setReconciliationState(0);//业务对账状态
//            			tgpf_BankUploadDataDetail.setReconciliationStamp(reconciliationStamp);//业务对账时间
//            			tgpf_BankUploadDataDetail.setRemark(remark);//备注
            			tgpf_BankUploadDataDetail.setCoreNo(accountMsg[2]);//核心流水号
//            			tgpf_BankUploadDataDetail.setReconciliationUser(reconciliationUser);//对账人
            			Serializable tableId = tgpf_BankUploadDataDetailDao.save(tgpf_BankUploadDataDetail); 
            			
        				if(keyMap.containsKey(tableId.toString()))
        				{
        					continue;
        				}
        				else
        				{
        					keyMap.put(tableId.toString(), tableId.toString());
        				}
            		}
            		
            		br.close();
            		
            		if(count==0&&!dealsumnum.equals("0")){
            			return isreadfile="0021";
            		}
            		break;
                }
            } 
            if(null != branch)
            {               	
            	// 查询网银对账单上传数据-如果存在记录，则更新
    			// 查询条件 1.银行平台流水号 2.状态：正常
    			Tgpf_BankUploadDataDetailForm tgpf_BankUploadDataDetailForm = new Tgpf_BankUploadDataDetailForm();
    			tgpf_BankUploadDataDetailForm.setBankBranch(branch);
    			tgpf_BankUploadDataDetailForm.setEnterTimeStamp(billTimeStamp);
    			tgpf_BankUploadDataDetailForm.setTheState(S_TheState.Normal); // 状态为正常
    			Integer totalCount = tgpf_BankUploadDataDetailDao.findByPage_Size(tgpf_BankUploadDataDetailDao.getQuery_Size(tgpf_BankUploadDataDetailDao.getBasicHQL(), tgpf_BankUploadDataDetailForm));
    			
    			List<Tgpf_BankUploadDataDetail> tgpf_BankUploadDataDetailList;
    			if(totalCount > 0)
    			{
    				tgpf_BankUploadDataDetailList = tgpf_BankUploadDataDetailDao.findByPage(tgpf_BankUploadDataDetailDao.getQuery(tgpf_BankUploadDataDetailDao.getBasicHQL(), tgpf_BankUploadDataDetailForm));
				
    				for(Tgpf_BankUploadDataDetail tgpf_BankUploadDataDetail : tgpf_BankUploadDataDetailList)
    				{
    					if(!keyMap.containsKey(tgpf_BankUploadDataDetail.getTableId().toString()))
    					{
                			tgpf_BankUploadDataDetail.setUserUpdate(userStart);
                			tgpf_BankUploadDataDetail.setLastUpdateTimeStamp(System.currentTimeMillis());//最后修改日期
    						tgpf_BankUploadDataDetail.setTheState(S_TheState.Deleted);
    						tgpf_BankUploadDataDetailDao.save(tgpf_BankUploadDataDetail);     						
    					}      					
    				}        				      			
    			}               	
            }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// TODO Auto-generated catch block
			isreadfile=e.getMessage();
			System.out.println("Ftp错误" + e.getMessage());
			isreadfile = "FTP错误！";
		}
	
		return isreadfile;
	}
	
	/**
	 * 获取中心平台流水号
	 * @return
	 */
	public String getECodeFromCenterPlatform()
	{
		
		String eCodeFromCenterPlatform = "0";
		// 查询流水号，若没有，则新增
		// 查询条件：1. 业务类型businessType 2.状态：正常
		Tgpf_SerialNumberForm tgpf_SerialNumberForm = new Tgpf_SerialNumberForm();
		tgpf_SerialNumberForm.setBusinessType("eCodeFromCenterPlatform");
		tgpf_SerialNumberForm.setTheState(S_TheState.Normal);
		
		Integer totalCount = tgpf_SerialNumberDao.findByPage_Size(tgpf_SerialNumberDao.getQuery_Size(tgpf_SerialNumberDao.getBasicHQL(), tgpf_SerialNumberForm));
		List<Tgpf_SerialNumber> tgpf_SerialNumberList;
		Tgpf_SerialNumber tgpf_SerialNumber = new Tgpf_SerialNumber();
		if(totalCount > 0)
		{
			tgpf_SerialNumberList = tgpf_SerialNumberDao.findByPage(tgpf_SerialNumberDao.getQuery(tgpf_SerialNumberDao.getBasicHQL(), tgpf_SerialNumberForm));
			tgpf_SerialNumber = tgpf_SerialNumberList.get(0);
			eCodeFromCenterPlatform = String.valueOf(tgpf_SerialNumber.getSerialNumber()+1);
		}
		
		// 调用一次则增加一次流水号
		tgpf_SerialNumber.setBusinessType("eCodeFromCenterPlatform");
		tgpf_SerialNumber.setTheState(S_TheState.Normal);
		tgpf_SerialNumber.setSerialNumber(Integer.parseInt(eCodeFromCenterPlatform));
		tgpf_SerialNumberDao.save(tgpf_SerialNumber);

		return eCodeFromCenterPlatform;
	}
	
	public String getParameter(String theName)
	{		
		String retParam = "";
		
		Sm_BaseParameterForm sm_BaseParameterForm = new Sm_BaseParameterForm();
		sm_BaseParameterForm.setTheState(0);
		sm_BaseParameterForm.setTheName(theName);
		
		Integer totalCount = sm_BaseParameterDao.findByPage_Size(sm_BaseParameterDao.getQuery_Size(sm_BaseParameterDao.getBasicHQL(), sm_BaseParameterForm));
	
		List<Sm_BaseParameter> sm_BaseParameterList;
		if(totalCount > 0)
		{
			sm_BaseParameterList = sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), sm_BaseParameterForm));
			retParam = sm_BaseParameterList.get(0).getTheValue();
		}
		
		return retParam;
	}
	
	
}
