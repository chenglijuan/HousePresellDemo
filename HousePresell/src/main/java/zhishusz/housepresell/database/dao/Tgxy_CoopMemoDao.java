package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Tgxy_CoopMemo;

/*
 * Dao数据库操作：合作备忘录
 * Company：ZhiShuSZ
 */
@Repository
public class Tgxy_CoopMemoDao extends BaseDao<Tgxy_CoopMemo>
{
	public String getBasicHQL()
	{
		return "from Tgxy_CoopMemo where 1=1" + " <#if theState??> and theState=:theState</#if>"
				+ " <#if busiState??> and busiState=:busiState</#if>" + " <#if eCode??> and eCode=:eCode</#if>"
				+ " <#if createTimeStamp??> and createTimeStamp=:createTimeStamp</#if>"
				+ " <#if lastUpdateTimeStamp??> and lastUpdateTimeStamp=:lastUpdateTimeStamp</#if>"
				+ " <#if recordTimeStamp??> and recordTimeStamp=:recordTimeStamp</#if>"
				+ " <#if eCodeOfCooperationMemo??> and eCodeOfCooperationMemo=:eCodeOfCooperationMemo</#if>"
				+ " <#if theNameOfBank??> and theNameOfBank=:theNameOfBank</#if>"
				+ " <#if signDate??> and signDate=:signDate</#if>";
	}

	/*
	 * xsz by time 2018-8-13 13:27:35
	 * 1.修改查询条件 使得根据关键字（keyword）即可查询
	 * （合作备忘录编号 银行名称）
	 * 2.添加排序规则（创建时间降序）
	 * 
	 * 调用service:
	 * Tgxy_CoopMemoListService
	 * 
	 * @version 2.0
	 * 
	 * @return
	 */
	public String getBasicHQL2()
	{
		return "from Tgxy_CoopMemo where 1=1"

				+ " <#if keyword??> and ( eCode like :keyword or eCodeOfCooperationMemo like :keyword or theNameOfBank like :keyword ) </#if>"
				// + " <#if signDate??> and signDate=:signDate</#if>" + " and
				// theState = '0' order by createTimeStamp desc";

				/*
				 * 1.签署日期
				 * 2.合作备忘录编号、银行名称
				 * 3.签署日期↓、合作备忘录编号、银行名称、
				 */
				+ " <#if signDate??> and signDate=:signDate</#if>"
				+ " and theState = '0' order by signDate desc,eCode,theNameOfBank";
	}
}
