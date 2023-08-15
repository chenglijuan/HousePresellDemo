package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgxy_DepositAgreement;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：协定存款协议
 * Company：ZhiShuSZ
 */
@Service
public class Tgxy_DepositAgreementRebuild extends RebuilderBase<Tgxy_DepositAgreement>
{
	@Override
	public Properties getSimpleInfo(Tgxy_DepositAgreement object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-22 14:29:00
		 * 协定存款协议列表显示字段
		 */
		properties.put("tableId", object.getTableId());// 主键
		properties.put("eCode", object.geteCode());// 编号
		properties.put("theNameOfBank", object.getTheNameOfBank());// 银行名称
		properties.put("bankOfDeposit", object.getTheNameOfDepositBank());// 开户行名称
		properties.put("escrowAccount", object.getTheAccountOfEscrowAccount());// 托管账号
		properties.put("depositRate", object.getDepositRate());// 协定存款利率
		properties.put("orgAmount", object.getOrgAmount());// 起始金额
		properties.put("signDate", object.getSignDate());// 签订日期
		properties.put("timeLimit", object.getTimeLimit());// 期限
		properties.put("beginExpirationDate", object.getBeginExpirationDate());// 生效日期
		properties.put("endExpirationDate", object.getEndExpirationDate());// 到期日期
		properties.put("userUpdate", object.getUserUpdate());// 修改人信息
		// 处理时间格式yyyy-MM-dd HH:mm:ss
		properties.put("lastUpdateTimeStamp",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));// 最后修改日期
		properties.put("remark", object.getRemark());// 备注

		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_DepositAgreement object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-22 16:15:46
		 * 协定存款协议详情展示
		 */
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userUpdate", object.getUserUpdate());
		// 处理时间格式yyyy-MM-dd HH:mm:ss
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		properties.put("bank", object.getBank());
		properties.put("bankId", object.getBank().getTableId());

		properties.put("theNameOfBank", object.getTheNameOfBank());// 银行名称
		properties.put("bankOfDepositId", object.getBankOfDeposit().getTableId());// 开户行-Id
		properties.put("bankOfDeposit", object.getTheNameOfDepositBank());// 开户行名称
		properties.put("escrowAccountId", object.getEscrowAccount().getTableId());// 托管账户-Id
		properties.put("theAccountOfEscrowAccount", object.getTheAccountOfEscrowAccount());// 托管账号
		properties.put("escrowAccount", object.getEscrowAccount());// 托管账户
		properties.put("depositRate", object.getDepositRate());
		properties.put("orgAmount", object.getOrgAmount());
		properties.put("signDate", object.getSignDate());
		properties.put("timeLimit", object.getTimeLimit());
		properties.put("beginExpirationDate", object.getBeginExpirationDate());
		properties.put("endExpirationDate", object.getEndExpirationDate());
		properties.put("remark", object.getRemark());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_DepositAgreement> tgxy_DepositAgreementList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgxy_DepositAgreementList != null)
		{
			for (Tgxy_DepositAgreement object : tgxy_DepositAgreementList)
			{
				Properties properties = new MyProperties();

				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("bank", object.getBank());
				properties.put("bankId", object.getBank().getTableId());
				properties.put("theNameOfBank", object.getTheNameOfBank());// 银行名称
				properties.put("bankOfDeposit", object.getTheNameOfDepositBank());// 开户行名称
				properties.put("escrowAccount", object.getTheAccountOfEscrowAccount());// 托管账号
				properties.put("depositRate", object.getDepositRate());
				properties.put("orgAmount", object.getOrgAmount());
				properties.put("signDate", object.getSignDate());
				properties.put("timeLimit", object.getTimeLimit());
				properties.put("beginExpirationDate", object.getBeginExpirationDate());
				properties.put("endExpirationDate", object.getEndExpirationDate());
				properties.put("remark", object.getRemark());

				list.add(properties);
			}
		}
		return list;
	}
}
