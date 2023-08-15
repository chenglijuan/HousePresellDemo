package zhishusz.housepresell.database.dao;

import zhishusz.housepresell.database.po.Emmp_BankInfo;

import org.springframework.stereotype.Repository;

/*
 * Dao数据库操作：金融机构(承办银行)
 * Company：ZhiShuSZ
 * */
@Repository
public class Emmp_BankInfoDao extends BaseDao<Emmp_BankInfo>
{
	public String getBasicHQL()
	{
		return "from Emmp_BankInfo where 1=1" 
				+ " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" 
				+ " <#if eCode??> and eCode=:eCode</#if>"
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
//				+ " <#if theName??> and theName=:theName</#if>"
				+ " <#if keyword??> and shortName like :keyword</#if>"
				+ " <#if shortName??> and shortName=:shortName</#if>"
				+ " <#if leader??> and leader=:leader</#if>" + " <#if address??> and address=:address</#if>"
				+ " <#if capitalCollectionModel??> and capitalCollectionModel=:capitalCollectionModel</#if>"
				+ " <#if theType??> and theType=:theType</#if>"
				+ " <#if bankNo??> and bankNo=:bankNo</#if>"
				+ " <#if postalAddress??> and postalAddress=:postalAddress</#if>"
				+ " <#if postalPort??> and postalPort=:postalPort</#if>"
				+ " <#if contactPerson??> and contactPerson=:contactPerson</#if>"
				+ " <#if contactPhone??> and contactPhone=:contactPhone</#if>"
				+ " <#if ftpDirAddress??> and ftpDirAddress=:ftpDirAddress</#if>"
				+ " <#if ftpUserName??> and ftpUserName=:ftpUserName</#if>" + " <#if ftpPwd??> and ftpPwd=:ftpPwd</#if>"
				+ " <#if financialInstitution??> and financialInstitution=:financialInstitution</#if>"
				+ " <#if theTypeOfPOS??> and theTypeOfPOS=:theTypeOfPOS</#if>"
				+ " <#if eCodeOfSubject??> and eCodeOfSubject=:eCodeOfSubject</#if>"
				+ " <#if eCodeOfProvidentFundCenter??> and eCodeOfProvidentFundCenter=:eCodeOfProvidentFundCenter</#if>"
				+ " <#if remark??> and remark=:remark</#if>"
//				+ " <#if orderBy??> order by ${orderBy}</#if>"
				+ " <#if orderBy??> order by NLSSORT(${orderBy},'NLS_SORT = SCHINESE_PINYIN_M') ${orderByType}</#if>"
				;

	}

	public String getExcelListHQL()
    {
    	return "from Emmp_BankInfo where 1=1"
    	+ " <#if idArr??> and tableId in :idArr</#if>";
    }

	/*
	 * xsz by time 2018-8-14 16:54:00
	 * 添加合作备忘录银行列表预加载
	 * 
	 * 调用service:
	 * Tgxy_CoopMemoAddPreOfBankService
	 * 
	 * @version 2.0
	 * 
	 * @return
	 */
	public String getBasicHQL2()
	{
		return "from Emmp_BankInfo ";
	}
}
