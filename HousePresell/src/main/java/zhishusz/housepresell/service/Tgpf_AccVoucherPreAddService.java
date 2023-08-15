package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_AccVoucherForm;
import zhishusz.housepresell.controller.form.Tgpf_DayEndBalancingForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_AccVoucherDao;
import zhishusz.housepresell.database.dao.Tgpf_DayEndBalancingDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_AccVoucher;
import zhishusz.housepresell.database.po.Tgpf_DayEndBalancing;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：推送给财务系统-凭证
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_AccVoucherPreAddService
{
	@Autowired
	private Tgpf_AccVoucherDao tgpf_AccVoucherDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Tgpf_DayEndBalancingDao tgpf_DayEndBalancingDao; // 日终结算
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Properties execute(Tgpf_AccVoucherForm model)
	{
		Properties properties = new MyProperties();
		
		String billTimeStap = model.getBillTimeStamp();

		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			billTimeStap = myDatetime.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis()));;
			model.setBillTimeStamp(billTimeStap);
		}

		model.setTheState(S_TheState.Normal);
		
		Sm_User userStart = model.getUser(); // admin

		Tgpf_DayEndBalancingForm tgpf_DayEndBalancingForm = new Tgpf_DayEndBalancingForm();
		tgpf_DayEndBalancingForm.setTheState(S_TheState.Normal);
		tgpf_DayEndBalancingForm.setBillTimeStamp(billTimeStap);
		tgpf_DayEndBalancingForm.setJudgeState(0);
		
		Integer dayEndBalancingCount = tgpf_DayEndBalancingDao.findByPage_Size(tgpf_DayEndBalancingDao.getQuery_Size(tgpf_DayEndBalancingDao.getBasicHQL(), tgpf_DayEndBalancingForm));

		List<Tgpf_DayEndBalancing> tgpf_DayEndBalancingList;
		if( dayEndBalancingCount >0)
		{
			tgpf_DayEndBalancingList = tgpf_DayEndBalancingDao.findByPage(tgpf_DayEndBalancingDao.getQuery(tgpf_DayEndBalancingDao.getBasicHQL(), tgpf_DayEndBalancingForm));
			
			for(Tgpf_DayEndBalancing tgpf_DayEndBalancing : tgpf_DayEndBalancingList)
			{
				if( tgpf_DayEndBalancing.getTotalCount() > 0)
				{
					String theAccount = tgpf_DayEndBalancing.getEscrowedAccount();// 托管账户
					int totalCount = tgpf_DayEndBalancing.getTotalCount(); // 总笔数
					Double totalAmount = tgpf_DayEndBalancing.getTotalAmount(); // 总金额
								
					String bankName = tgpf_DayEndBalancing.getBankName(); //银行名称
					Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = tgpf_DayEndBalancing.getTgxy_BankAccountEscrowed();
					Emmp_CompanyInfo emmp_CompanyInfo = tgxy_BankAccountEscrowed.getCompany();
					Empj_ProjectInfo empj_ProjectInfo = tgxy_BankAccountEscrowed.getProject();
					Emmp_BankInfo emmp_BankInfo = tgxy_BankAccountEscrowed.getBank();
					
					// 查询凭证表，是否存在凭证
					// 查询条件：1.记账日期 2.托管账号 3.状态：正常
					Tgpf_AccVoucherForm tgpf_AccVoucherForm = new Tgpf_AccVoucherForm();
					tgpf_AccVoucherForm.setBillTimeStamp(billTimeStap);
					tgpf_AccVoucherForm.setTheAccountOfBankAccountEscrowed(theAccount);
					
					Integer accVoucherCount = tgpf_AccVoucherDao.findByPage_Size(tgpf_AccVoucherDao.getQuery_Size(tgpf_AccVoucherDao.getBasicHQL(), tgpf_AccVoucherForm));

					// 如果存在则为修改，不存在则为新增
					List<Tgpf_AccVoucher> tgpf_AccVoucherList;
					Tgpf_AccVoucher tgpf_AccVoucher = new Tgpf_AccVoucher();
					if(accVoucherCount > 0)
					{
						tgpf_AccVoucherList = tgpf_AccVoucherDao.findByPage(tgpf_AccVoucherDao.getQuery(tgpf_AccVoucherDao.getBasicHQL(), tgpf_AccVoucherForm));
						tgpf_AccVoucher = tgpf_AccVoucherList.get(0);
											
					}else{
						
						tgpf_AccVoucher.setBusiState("0");
//						tgpf_AccVoucher.seteCode(eCode);
						tgpf_AccVoucher.setUserStart(userStart);
						tgpf_AccVoucher.setCreateTimeStamp(System.currentTimeMillis());
						tgpf_AccVoucher.setUserUpdate(userStart);
						tgpf_AccVoucher.setLastUpdateTimeStamp(System.currentTimeMillis());
//						tgpf_AccVoucher.setUserRecord(userRecord);
//						tgpf_AccVoucher.setRecordTimeStamp(recordTimeStamp);
						tgpf_AccVoucher.setBillTimeStamp(billTimeStap);
						tgpf_AccVoucher.setTheType("入账");	//业务类型 :入账、拨付
						tgpf_AccVoucher.setBankAccountEscrowed(tgxy_BankAccountEscrowed);
						tgpf_AccVoucher.setTheAccountOfBankAccountEscrowed(theAccount);
//						tgpf_AccVoucher.setContentJson(contentJson); // 凭证内容
//						tgpf_AccVoucher.setPayoutTimeStamp(payoutTimeStamp);//资金拨付日期
						try
						{
							if(null == emmp_CompanyInfo || null == emmp_CompanyInfo.getTheName())
							{
								tgpf_AccVoucher.setCompany(null);
								tgpf_AccVoucher.setTheNameOfCompany("");
							}
							else
							{
								tgpf_AccVoucher.setCompany(emmp_CompanyInfo);
								tgpf_AccVoucher.setTheNameOfCompany(emmp_CompanyInfo.getTheName());
							}
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							System.out.println(tgxy_BankAccountEscrowed.getTableId());
							e.printStackTrace();
							
						}
										
						if(null == empj_ProjectInfo || empj_ProjectInfo.getTheName() == null)
						{
							tgpf_AccVoucher.setTheNameOfProject(null);
							tgpf_AccVoucher.setProject(null);
						}
						else
						{
							tgpf_AccVoucher.setProject(empj_ProjectInfo);
							tgpf_AccVoucher.setTheNameOfProject(empj_ProjectInfo.getTheName());
						}					
						tgpf_AccVoucher.setBank(emmp_BankInfo);
						tgpf_AccVoucher.setTheNameOfBank(bankName);
//						tgpf_AccVoucher.setPayoutAmount(payoutAmount);
						
					}
					
					tgpf_AccVoucher.setTradeCount(totalCount);	// 总笔数
					tgpf_AccVoucher.setTotalTradeAmount(totalAmount); // 总金额
					
					if(tgpf_DayEndBalancing.getSettlementState() == 1)
					{
						tgpf_AccVoucher.setDayEndBalancingState(1);
					}
					else if( tgpf_DayEndBalancing.getSettlementState() == 2)
					{
						tgpf_AccVoucher.setDayEndBalancingState(2);
					}
					else
					{
						tgpf_AccVoucher.setDayEndBalancingState(0);
					}
					
					tgpf_AccVoucher.setTheState(S_TheState.Normal);		
					
					tgpf_AccVoucherDao.save(tgpf_AccVoucher);
				}
				else
				{
					continue;
				}							
			}
			
		}
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
