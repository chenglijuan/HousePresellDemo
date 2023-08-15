package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementForm;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;

/*
 * Service添加操作：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_CyberBankStatementAddService
{
	@Autowired
	private Tgpf_CyberBankStatementDao tgpf_CyberBankStatementDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;

	// 业务编码
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;

	String busiCode = "200201";

	public Properties execute(Tgpf_CyberBankStatementForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = S_TheState.Normal;
		String busiState = "0";
		Long userStartId = model.getUserId();
		Long createTimeStamp = System.currentTimeMillis();
		Integer reconciliationState = 0;
		String orgFilePath = model.getOrgFilePath();
		String uploadTimeStamp = MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis());
		Long bankId = model.getBankId();// 银行ID
		Long theAccountOfBankAccountEscrowedId = model.getTheAccountOfBankAccountEscrowedId();// 托管账号ID
		String billTimeStamp = model.getBillTimeStamp();// 记账日期

		// 获取需要关联的详情ID
		Long[] idArr = model.getIdArr();

		if (userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if (bankId == null || bankId < 1)
		{
			return MyBackInfo.fail(properties, "'bankId'不能为空");
		}
		if (theAccountOfBankAccountEscrowedId == null || theAccountOfBankAccountEscrowedId < 1)
		{
			return MyBackInfo.fail(properties, "'theAccountOfBankAccountEscrowedId'不能为空");
		}
		if (billTimeStamp == null || billTimeStamp.length() == 0)
		{
			return MyBackInfo.fail(properties, "'billTimeStamp'不能为空");
		}
		Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
		// 查询托管银行
		Emmp_BankInfo emmp_BankInfo = emmp_BankInfoDao.findById(bankId);
		// 查询托管账户
		Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = tgxy_BankAccountEscrowedDao
				.findById(theAccountOfBankAccountEscrowedId);
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if (emmp_BankInfo == null)
		{
			return MyBackInfo.fail(properties, "'emmp_BankInfo'不能为空");
		}
		if (tgxy_BankAccountEscrowed == null)
		{
			return MyBackInfo.fail(properties, "未查询到有效的监管账户");
		}
		else
		{
			Tgpf_CyberBankStatementForm form = new Tgpf_CyberBankStatementForm();
			form.setTheState(S_TheState.Normal);
			form.setTheAccountOfBankAccountEscrowed(tgxy_BankAccountEscrowed.getTheAccount());
			form.setBillTimeStamp(billTimeStamp);
			
			Tgpf_CyberBankStatement tgpf_CyberBankStatement = tgpf_CyberBankStatementDao.findOneByQuery_T(tgpf_CyberBankStatementDao.getQuery(tgpf_CyberBankStatementDao.getBasicHQL2(), form));
			
			if(null != tgpf_CyberBankStatement){
				return MyBackInfo.fail(properties, "托管账户："+tgxy_BankAccountEscrowed.getTheAccount()+"，已上传数据。");
			}
		}
		// 查询开户行信息
		String bankBranchName = "";
		Emmp_BankBranch bankBranch = tgxy_BankAccountEscrowed.getBankBranch();
		if (null != bankBranch)
		{
			bankBranchName = bankBranch.getTheName();
		}

		Tgpf_CyberBankStatement tgpf_CyberBankStatement = new Tgpf_CyberBankStatement();
		tgpf_CyberBankStatement.setTheState(theState);
		tgpf_CyberBankStatement.setBusiState(busiState);
		tgpf_CyberBankStatement.seteCode(sm_BusinessCodeGetService.execute(busiCode));
		tgpf_CyberBankStatement.setUserStart(userStart);
		tgpf_CyberBankStatement.setCreateTimeStamp(createTimeStamp);
		tgpf_CyberBankStatement.setTheNameOfBank(emmp_BankInfo.getTheName());
		tgpf_CyberBankStatement.setTheAccountOfBankAccountEscrowed(tgxy_BankAccountEscrowed.getTheAccount());
		tgpf_CyberBankStatement.setTheNameOfBankAccountEscrowed(tgxy_BankAccountEscrowed.getTheName());
		tgpf_CyberBankStatement.setReconciliationState(reconciliationState);
		tgpf_CyberBankStatement.setOrgFilePath(orgFilePath);
		tgpf_CyberBankStatement.setTheNameOfBankBranch(bankBranchName);
		tgpf_CyberBankStatement.setUploadTimeStamp(uploadTimeStamp);
		tgpf_CyberBankStatement.setBillTimeStamp(billTimeStamp);
		tgpf_CyberBankStatement.setFileUploadState(0);
		Serializable serializable = tgpf_CyberBankStatementDao.save(tgpf_CyberBankStatement);

		if (null != idArr && idArr.length > 0)
		{
			// 关联详情
			for (Long tgpf_CyberBankStatementDelId : idArr)
			{
				Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = tgpf_CyberBankStatementDtlDao
						.findById(tgpf_CyberBankStatementDelId);
				tgpf_CyberBankStatementDtl.setMainTable(tgpf_CyberBankStatement);

				tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);
			}

			Tgpf_CyberBankStatement cyberBankStatement = tgpf_CyberBankStatementDao
					.findById(new Long(serializable.toString()));
			cyberBankStatement.setFileUploadState(1);
			tgpf_CyberBankStatementDao.save(cyberBankStatement);
		}

		properties.put("tableId", new Long(serializable.toString()));

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
