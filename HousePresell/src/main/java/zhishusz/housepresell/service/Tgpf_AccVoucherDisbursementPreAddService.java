package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_AccVoucherForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriatedForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_AccVoucherDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriatedDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_AccVoucher;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated;
import zhishusz.housepresell.database.po.Tgpf_FundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AF;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_SpecialFundApplyState;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：推送给财务系统-凭证-拨付凭证预加载
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_AccVoucherDisbursementPreAddService
{
	@Autowired
	private Tgpf_AccVoucherDao tgpf_AccVoucherDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpf_FundAppropriatedDao tgpf_FundAppropriatedDao; // 拨付主表
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao; // 申请-用款-明细
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Properties execute(Tgpf_AccVoucherForm model)
	{
		Properties properties = new MyProperties();
		
		String billTimeStap = model.getBillTimeStamp();

		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			billTimeStap = myDatetime.dateToSimpleString(System.currentTimeMillis());
			model.setBillTimeStamp(billTimeStap);
		}

		model.setTheState(S_TheState.Normal);
		
		Sm_User userStart = model.getUser(); // admin

		// 查询资金拨付表
		// 查询条件：1.拨付时间 2.状态为：正常
		Tgpf_FundAppropriatedForm tgpf_FundAppropriatedForm = new Tgpf_FundAppropriatedForm();
		tgpf_FundAppropriatedForm.setActualPayoutDate(billTimeStap);
		tgpf_FundAppropriatedForm.setTheState(S_TheState.Normal);
		Integer fundAppropriatedCount = tgpf_FundAppropriatedDao.findByPage_Size(tgpf_FundAppropriatedDao.getQuery_Size(tgpf_FundAppropriatedDao.getBasicHQL(), tgpf_FundAppropriatedForm));

		List<Tgpf_FundAppropriated> tgpf_FundAppropriatedList;
		if(fundAppropriatedCount > 0)
		{
			tgpf_FundAppropriatedList = tgpf_FundAppropriatedDao.findByPage(tgpf_FundAppropriatedDao.getQuery(tgpf_FundAppropriatedDao.getBasicHQL(), tgpf_FundAppropriatedForm));
			
			for(Tgpf_FundAppropriated tgpf_FundAppropriated : tgpf_FundAppropriatedList)
			{
				// 开发企业
				Emmp_CompanyInfo emmp_CompanyInfo = tgpf_FundAppropriated.getDevelopCompany();
				// 托管账户
				Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = tgpf_FundAppropriated.getBankAccountEscrowed();
				// 银行
				Emmp_BankInfo emmp_BankInfo = tgxy_BankAccountEscrowed.getBank();
				// 拨付申请主表
				Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = tgpf_FundAppropriated.getFundAppropriated_AF();
				
				Empj_ProjectInfo empj_ProjectInfo = tgpf_FundAppropriated_AF.getProject();
				
				// 查询拨付凭证信息
				// 查询条件：1.拨付 2.时间 3. 开发企业 4.项目名称
				Tgpf_AccVoucherForm tgpf_AccVoucherForm = new Tgpf_AccVoucherForm();
				tgpf_AccVoucherForm.setTheType("拨付");
				tgpf_AccVoucherForm.setPayoutTimeStamp(billTimeStap);
//				tgpf_AccVoucherForm.setTgpf_FundAppropriated_AF(tgpf_FundAppropriated_AF);
				tgpf_AccVoucherForm.setContentJson(tgpf_FundAppropriated.getTableId().toString());
				tgpf_AccVoucherForm.setTheState(S_TheState.Normal);
				
				Integer accVoucherCount = tgpf_AccVoucherDao.findByPage_Size(tgpf_AccVoucherDao.getQuery_Size(tgpf_AccVoucherDao.getSpeicalHQL(), tgpf_AccVoucherForm));
				
				// 如果存在，则更新，不存在则为新增
				List<Tgpf_AccVoucher> tgpf_AccVoucherList;
				Tgpf_AccVoucher tgpf_AccVoucher = new Tgpf_AccVoucher();
				if(accVoucherCount > 0)
				{
					tgpf_AccVoucherList = tgpf_AccVoucherDao.findByPage(tgpf_AccVoucherDao.getQuery(tgpf_AccVoucherDao.getSpeicalHQL(), tgpf_AccVoucherForm));
					tgpf_AccVoucher = tgpf_AccVoucherList.get(0);
				}
				else
				{
					tgpf_AccVoucher.setBusiState("0");
//					tgpf_AccVoucher.seteCode(eCode);
					tgpf_AccVoucher.setUserStart(userStart);
					tgpf_AccVoucher.setCreateTimeStamp(System.currentTimeMillis());
					tgpf_AccVoucher.setUserUpdate(userStart);
					tgpf_AccVoucher.setLastUpdateTimeStamp(System.currentTimeMillis());
//					tgpf_AccVoucher.setUserRecord(userRecord);
//					tgpf_AccVoucher.setRecordTimeStamp(recordTimeStamp);
					tgpf_AccVoucher.setBillTimeStamp(billTimeStap);
					tgpf_AccVoucher.setTheType("拨付");	//业务类型 :入账、拨付
					tgpf_AccVoucher.setBankAccountEscrowed(tgxy_BankAccountEscrowed);
					tgpf_AccVoucher.setTheAccountOfBankAccountEscrowed(tgxy_BankAccountEscrowed.getTheAccount());
//					tgpf_AccVoucher.setContentJson(contentJson); // 凭证内容
					tgpf_AccVoucher.setPayoutTimeStamp(billTimeStap);//资金拨付日期
					tgpf_AccVoucher.setCompany(emmp_CompanyInfo);
					tgpf_AccVoucher.setTheNameOfCompany(emmp_CompanyInfo.getTheName());
					tgpf_AccVoucher.setProject(empj_ProjectInfo);
					tgpf_AccVoucher.setTheNameOfProject(empj_ProjectInfo.getTheName());
					tgpf_AccVoucher.setBank(emmp_BankInfo);
					tgpf_AccVoucher.setTheNameOfBank(emmp_BankInfo.getTheName());
//					tgpf_AccVoucher.setBuilding(empj_BuildingInfo);
					
//					tgpf_AccVoucher.setTradeCount(totalCount);	// 总笔数
//					tgpf_AccVoucher.setDayEndBalancingState(settlementState); //日终结算状态
				}
				Double currentPayoutAmount = tgpf_FundAppropriated.getCurrentPayoutAmount();

				tgpf_AccVoucher.setTgpf_FundAppropriated_AF(tgpf_FundAppropriated_AF);
				tgpf_AccVoucher.setPayoutAmount(currentPayoutAmount);
				tgpf_AccVoucher.setTotalTradeAmount(currentPayoutAmount); // 总金额
				tgpf_AccVoucher.setContentJson(tgpf_FundAppropriated.getTableId().toString());
				tgpf_AccVoucher.setTheState(S_TheState.Normal);	
				tgpf_AccVoucher.setRelatedType(0);
				tgpf_AccVoucher.setRelatedTableId(tgpf_FundAppropriated.getTableId());
				
				tgpf_AccVoucherDao.save(tgpf_AccVoucher);				
			}						
		}
		
		// 查询特殊拨付列表
		// 查询条件：1：拨付时间 2：单据状态 3.拨付状态
		Tgpf_SpecialFundAppropriated_AFDtlForm tgpf_SpecialFundAppropriated_AFDtlForm = new Tgpf_SpecialFundAppropriated_AFDtlForm();
		tgpf_SpecialFundAppropriated_AFDtlForm.setPayoutDate(billTimeStap);
		tgpf_SpecialFundAppropriated_AFDtlForm.setTheState(S_TheState.Normal);
		tgpf_SpecialFundAppropriated_AFDtlForm.setPayoutState(S_SpecialFundApplyState.Appropriated);
		
		Integer totalCount = tgpf_SpecialFundAppropriated_AFDtlDao.findByPage_Size(tgpf_SpecialFundAppropriated_AFDtlDao
				.getQuery_Size(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), tgpf_SpecialFundAppropriated_AFDtlForm));

		List<Tgpf_SpecialFundAppropriated_AFDtl> tgpf_SpecialFundAppropriated_AFDtlList;
		if (totalCount > 0)
		{
			tgpf_SpecialFundAppropriated_AFDtlList = tgpf_SpecialFundAppropriated_AFDtlDao
					.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
							.getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), tgpf_SpecialFundAppropriated_AFDtlForm));
			
			for(Tgpf_SpecialFundAppropriated_AFDtl tgpf_SpecialFundAppropriated_AFDtl : tgpf_SpecialFundAppropriated_AFDtlList)
			{
				// 查询拨付凭证信息
				// 查询条件：1.拨付 2.时间 3. 拨付类型 4.特殊拨付主键 5.单据状态
				Tgpf_AccVoucherForm tgpf_AccVoucherForm = new Tgpf_AccVoucherForm();
				tgpf_AccVoucherForm.setTheType("拨付");
				tgpf_AccVoucherForm.setPayoutTimeStamp(billTimeStap);
				tgpf_AccVoucherForm.setRelatedType(1);
				tgpf_AccVoucherForm.setRelatedTableId(tgpf_SpecialFundAppropriated_AFDtl.getTableId());
				tgpf_AccVoucherForm.setTheState(S_TheState.Normal);
				
				Integer accVoucherCount = tgpf_AccVoucherDao.findByPage_Size(tgpf_AccVoucherDao.getQuery_Size(tgpf_AccVoucherDao.getSpeicalHQL(), tgpf_AccVoucherForm));
				
				Tgxy_BankAccountEscrowed bankAccountEscrowed = tgpf_SpecialFundAppropriated_AFDtl.getBankAccountEscrowed();
				
				Emmp_BankInfo bank = bankAccountEscrowed.getBank();
				
				Tgpf_SpecialFundAppropriated_AF specialAppropriated = tgpf_SpecialFundAppropriated_AFDtl.getSpecialAppropriated();
				
				Emmp_CompanyInfo developCompany = specialAppropriated.getDevelopCompany();
				
				Empj_ProjectInfo project = specialAppropriated.getProject();
							
				// 如果存在，则更新，不存在则为新增
				List<Tgpf_AccVoucher> tgpf_AccVoucherList;
				Tgpf_AccVoucher tgpf_AccVoucher = new Tgpf_AccVoucher();
				if(accVoucherCount > 0)
				{
					tgpf_AccVoucherList = tgpf_AccVoucherDao.findByPage(tgpf_AccVoucherDao.getQuery(tgpf_AccVoucherDao.getSpeicalHQL(), tgpf_AccVoucherForm));
					tgpf_AccVoucher = tgpf_AccVoucherList.get(0);
				}
				else
				{
					tgpf_AccVoucher.setBusiState("0");
//					tgpf_AccVoucher.seteCode(eCode);
					tgpf_AccVoucher.setUserStart(userStart);
					tgpf_AccVoucher.setCreateTimeStamp(System.currentTimeMillis());
					tgpf_AccVoucher.setUserUpdate(userStart);
					tgpf_AccVoucher.setLastUpdateTimeStamp(System.currentTimeMillis());
//					tgpf_AccVoucher.setUserRecord(userRecord);
//					tgpf_AccVoucher.setRecordTimeStamp(recordTimeStamp);
					tgpf_AccVoucher.setBillTimeStamp(billTimeStap);
					tgpf_AccVoucher.setTheType("拨付");	//业务类型 :入账、拨付
					tgpf_AccVoucher.setBankAccountEscrowed(bankAccountEscrowed);
					tgpf_AccVoucher.setTheAccountOfBankAccountEscrowed(bankAccountEscrowed.getTheAccount());
//					tgpf_AccVoucher.setContentJson(contentJson); // 凭证内容
					tgpf_AccVoucher.setPayoutTimeStamp(billTimeStap);//资金拨付日期
					tgpf_AccVoucher.setCompany(developCompany);
					tgpf_AccVoucher.setTheNameOfCompany(developCompany.getTheName());
					tgpf_AccVoucher.setProject(project);
					tgpf_AccVoucher.setTheNameOfProject(project.getTheName());
					tgpf_AccVoucher.setBank(bank);
					tgpf_AccVoucher.setTheNameOfBank(bank.getTheName());
//					tgpf_AccVoucher.setBuilding(empj_BuildingInfo);
					
//					tgpf_AccVoucher.setTradeCount(totalCount);	// 总笔数
//					tgpf_AccVoucher.setDayEndBalancingState(settlementState); //日终结算状态
				}
				Double currentPayoutAmount = tgpf_SpecialFundAppropriated_AFDtl.getAppliedAmount();

				tgpf_AccVoucher.setPayoutAmount(currentPayoutAmount);
				tgpf_AccVoucher.setTotalTradeAmount(currentPayoutAmount); // 总金额
				tgpf_AccVoucher.setTheState(S_TheState.Normal);	
				tgpf_AccVoucher.setRelatedType(1);
				tgpf_AccVoucher.setRelatedTableId(tgpf_SpecialFundAppropriated_AFDtl.getTableId());
				
				tgpf_AccVoucherDao.save(tgpf_AccVoucher);	
			}
		}
		else
		{
			tgpf_SpecialFundAppropriated_AFDtlList = new ArrayList<Tgpf_SpecialFundAppropriated_AFDtl>();
		}
		
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
