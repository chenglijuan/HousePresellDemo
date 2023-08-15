package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_DepositAgreement;

/*
 * Dao数据库操作：协定存款协议
 * Company：ZhiShuSZ
 */
@Repository
public class Tgxy_DepositAgreementDao extends BaseDao<Tgxy_DepositAgreement>
{
	public String getBasicHQL()
	{
		return "from Tgxy_DepositAgreement where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ " <#if theNameOfBank??> and theNameOfBank=:theNameOfBank</#if>"
				+ " <#if bankOfDeposit??> and bankOfDeposit=:bankOfDeposit</#if>"
				+ " <#if escrowAccount??> and escrowAccount=:escrowAccount</#if>"
				+ " <#if depositRate??> and depositRate=:depositRate</#if>"
				+ " <#if orgAmount??> and orgAmount=:orgAmount</#if>" + " <#if signDate??> and signDate=:signDate</#if>"
				+ " <#if timeLimit??> and timeLimit=:timeLimit</#if>"
				+ " <#if beginExpirationDate??> and beginExpirationDate=:beginExpirationDate</#if>"
				+ " <#if endExpirationDate??> and endExpirationDate=:endExpirationDate</#if>"
				+ " <#if remark??> and remark=:remark</#if>"
				+ " <#if escrowAccountId??> and escrowAccount.tableId=:escrowAccountId</#if>";
	}

	/*
	 * xsz by time 2018-8-22 14:13:21
	 * 1.修改查询条件 使得根据关键字（keyword）即可查询
	 * （协定存款协议编号、银行名称、开户行名称、托管账号、协定存款利率、起始金额、期限）
	 * 2.添加排序规则（创建时间降序）
	 * 
	 * 调用service:
	 * Tgxy_DepositAgreementListService
	 * 
	 * @version 2.0
	 * 
	 * @return
	 */
	public String getBasicHQL2()
	{
		return "from Tgxy_DepositAgreement where 1=1" + " <#if theState??> and theState=:theState</#if>"
		// + " <#if keyword??> and ( eCode like :keyword or theNameOfBank like
		// :keyword or bankOfDeposit like :keyword or escrowAccount like
		// :keyword or depositRate like :keyword or orgAmount like :keyword or
		// timeLimit like :keyword) </#if>"
		// + " <#if signDate??> and signDate=:signDate</#if>" + " order by
		// createTimeStamp desc";

		/*
		 * 1.签订日期
		 * 2.协定存款协议、银行名称、开户行
		 * 3.签订日期↓、到期日期↓、生效日期↓、银行名称
		 */
				+ " <#if keyword??> and ( eCode like :keyword or theNameOfBank like :keyword or theNameOfDepositBank like :keyword ) </#if>"
				+ " <#if signDate??> and signDate=:signDate</#if>"
				+ " order by signDate desc,endExpirationDate desc,beginExpirationDate desc,theNameOfBank";

	}
}
