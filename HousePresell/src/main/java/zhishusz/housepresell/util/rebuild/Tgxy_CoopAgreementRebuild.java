package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgxy_CoopAgreement;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：合作协议
 * Company：ZhiShuSZ
 */
@Service
public class Tgxy_CoopAgreementRebuild extends RebuilderBase<Tgxy_CoopAgreement>
{
	@Override
	public Properties getSimpleInfo(Tgxy_CoopAgreement object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-16 14:14:37
		 * 合作协议列表显示字段
		 * tableId、合作协议编号、银行名称、开户行、签署日期、操作人、操作日期
		 */
		properties.put("tableId", object.getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("theNameOfDepositBank", object.getTheNameOfDepositBank());
		properties.put("signDate", object.getSignDate());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());
		// 处理时间格式yyyy-MM-dd HH:mm:ss
		properties.put("lastUpdateTimeStamp",
				MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));

		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_CoopAgreement object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-20 09:32:08
		 * 设置合作协议基本信息展示字段
		 */
		
		properties.put("tableId", object.getTableId());
		//合作协议编号
		properties.put("eCode", object.geteCode());
		
		//创建人及修改人信息
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("userUpdateId", object.getUserUpdate().getTableId());
		
		//创建时间及修改时间 时间格式处理（yyyy-MM-dd HH:mm:ss） 
		properties.put("createTimeStamp", MyDatetime.getInstance().dateToString2(object.getCreateTimeStamp()));
		properties.put("lastUpdateTimeStamp", MyDatetime.getInstance().dateToString2(object.getLastUpdateTimeStamp()));
		
		//银行信息
		properties.put("bankId", object.getBank().getTableId());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		
		//开户行信息,提取之前判空
		if(null!=object.getBankOfDeposit()){
			properties.put("bankOfDepositId", object.getBankOfDeposit().getTableId());
			properties.put("theNameOfDepositBank", object.getTheNameOfDepositBank());
		}
		
		//签署日期
		properties.put("signDate", object.getSignDate());

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_CoopAgreement> tgxy_CoopAgreementList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgxy_CoopAgreementList != null)
		{
			for (Tgxy_CoopAgreement object : tgxy_CoopAgreementList)
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

				list.add(properties);
			}
		}
		return list;
	}
}
