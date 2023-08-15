package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Service;

import zhishusz.housepresell.database.po.Tg_RetainedRightsView;
import zhishusz.housepresell.util.MyProperties;

@Service
public class Tg_RetainedRightsViewRebuild extends RebuilderBase<Tg_RetainedRightsView> {

	@Override
	public Properties getSimpleInfo(Tg_RetainedRightsView object) {

		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		return properties;
	}

	@Override
	public Properties getDetail(Tg_RetainedRightsView object) {

		return null;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tg_RetainedRightsView> Tg_RetainedRightsViewList) 
	{
		List<Properties> list = new ArrayList<Properties>();
		if (Tg_RetainedRightsViewList != null) 
		{
			for (Tg_RetainedRightsView object : Tg_RetainedRightsViewList) 
			{

				Properties properties = new MyProperties();

				properties.put("arrivalTimeStamp", object.getEnterTimeStamp());// 入账日期
				properties.put("enterTimeStamp", object.getEnterTimeStamp());// 入账日期
				properties.put("projectName", object.getProjectName());// 项目名称
				properties.put("ecodeFromConstruction", object.getEcodeFromConstruction());// 楼幢施工编号
				properties.put("ecodeOfBuildingUnit", object.getEcodeOfBuildingUnit());// 单元信息
				properties.put("ecodeFromRoom", object.getEcodeFromRoom());// 房间号
				properties.put("buyer", object.getBuyer());// 买受人名称
				properties.put("depositAmountFromloan", object.getDepositAmountFromloan());// 按揭入账金额
				properties.put("cumulativeAmountPaid", object.getCumulativeAmountPaid());// 累计支付金额
				properties.put("theAmount", object.getTheAmount());// 留存权益总金额
				properties.put("amountOfInterestNotdue", object.getAmountOfInterestNotdue());// 未到期权益金额
				properties.put("amountOfInterestdue", object.getAmountOfInterestdue());// 到期权益金额
				properties.put("amountOfThisPayment", object.getAmountOfThisPayment());// 本次支付金额
				properties.put("fromDate", object.getFromDate());// 拨付日期

				list.add(properties);
			}
		}
		return list;

	}

}
