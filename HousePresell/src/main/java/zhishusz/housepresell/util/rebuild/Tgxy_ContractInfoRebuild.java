package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgxy_ContractInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：预售系统买卖合同
 * Company：ZhiShuSZ
 */
@Service
public class Tgxy_ContractInfoRebuild extends RebuilderBase<Tgxy_ContractInfo>
{
	@Override
	public Properties getSimpleInfo(Tgxy_ContractInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_ContractInfo object)
	{
		if (object == null)
			return null;
		Properties properties = new MyProperties();
		// MyDatetime.getInstance().dateToSimpleString(object.getLastUpdateTimeStamp())
		properties.put("buildingArea", object.getBuildingArea());// 建筑面积
		try {
			properties.put("contractSignDate",
					null == object.getContractSignDate() ? "" : object.getContractSignDate().substring(0, 10));// 合同签订日期
		} catch (Exception e) {
			properties.put("contractSignDate","");// 合同签订日期
		}
		properties.put("contractSumPrice", null == object.getContractSumPrice() ? "0.00" : String.valueOf(object.getContractSumPrice()));// 合同总价
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());// 施工编号
		properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());// 公安编号
		properties.put("syncDate", MyDatetime.getInstance().dateToString2(object.getSyncDate()));// 同步时间
		properties.put("syncPerson", object.getSyncPerson());// 同步人

		String paymentMethod = object.getPaymentMethod();
		switch (paymentMethod)
		{
		case "1":
			properties.put("paymentMethod", "一次性付款");// 付款方式
			break;

		case "2":
			properties.put("paymentMethod", "分期付款");// 付款方式
			break;

		case "3":
			properties.put("paymentMethod", "贷款方式付款");// 付款方式
			break;

		case "4":
			properties.put("paymentMethod", "其他方式");// 付款方式
			break;

		default:
			properties.put("paymentMethod", "其他方式");// 付款方式
			break;
		}

//		properties.put("paymentMethod", object.getPaymentMethod());// 付款方式
		properties.put("position", object.getPosition());// 座落
		properties.put("roomIdOfHouseInfo", object.getRoomIdOfHouseInfo());// 户室号
		properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());// 备案系统楼幢号
		properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());// 合同备案号
		properties.put("eCode", object.geteCode());// 合同编号
		properties.put("theNameFormCompany", object.getTheNameFormCompany());// 开发企业名称
		properties.put("theNameOfProject", object.getTheNameOfProject());// 项目名称

		properties.put("loanBank", object.getLoanBank());// 贷款银行
		properties.put("firstPaymentAmount", null == object.getFirstPaymentAmount() ? "0.00": String.valueOf(object.getFirstPaymentAmount()));// 首付款金额
		properties.put("loanAmount", null == object.getLoanAmount() ? "0.00" : String.valueOf(object.getLoanAmount()));// 贷款金额
		properties.put("payDate", object.getPayDate());// 交付日期
		properties.put("contractRecordDate",
				null == object.getContractRecordDate() ? "" : object.getContractRecordDate().substring(0, 10));// 合同备案日期
		properties.put("busiState", object.getBusiState());

		properties.put("bano", null == object.getExternalId() ? "" : object.getExternalId());// 备案系统合同编号

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_ContractInfo> tgxy_ContractInfoList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if (tgxy_ContractInfoList != null)
		{
			for (Tgxy_ContractInfo object : tgxy_ContractInfoList)
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
				properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
				properties.put("company", object.getCompany());
				properties.put("companyId", object.getCompany().getTableId());
				properties.put("theNameFormCompany", object.getTheNameFormCompany());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				properties.put("houseInfo", object.getHouseInfo());
				properties.put("houseInfoId", object.getHouseInfo().getTableId());
				properties.put("eCodeOfHouseInfo", object.geteCodeOfHouseInfo());
				properties.put("roomIdOfHouseInfo", object.getRoomIdOfHouseInfo());
				properties.put("contractSumPrice", object.getContractSumPrice());
				properties.put("buildingArea", object.getBuildingArea());
				properties.put("position", object.getPosition());
				properties.put("contractSignDate", object.getContractSignDate());
				properties.put("paymentMethod", object.getPaymentMethod());
				properties.put("loanBank", object.getLoanBank());
				properties.put("firstPaymentAmount", object.getFirstPaymentAmount());
				properties.put("loanAmount", object.getLoanAmount());
				properties.put("escrowState", object.getEscrowState());
				properties.put("payDate", object.getPayDate());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("eCodeFromPublicSecurity", object.geteCodeFromPublicSecurity());
				properties.put("contractRecordDate", object.getContractRecordDate());
				properties.put("syncPerson", object.getSyncPerson());
				properties.put("syncDate", object.getSyncDate());

				list.add(properties);
			}
		}
		return list;
	}
}
