package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：三方协议结算-子表
 * Company：ZhiShuSZ
 * */
@Service
public class Tgxy_CoopAgreementSettleDtlRebuild extends RebuilderBase<Tgxy_CoopAgreementSettleDtl>
{
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@Override
	public Properties getSimpleInfo(Tgxy_CoopAgreementSettleDtl object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgxy_CoopAgreementSettleDtl object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("userUpdate", object.getUserUpdate());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("mainTable", object.getMainTable());
		properties.put("mainTableId", object.getMainTable().getTableId());
		properties.put("eCode", object.geteCode());
		properties.put("agreementDate", object.getAgreementDate());
		properties.put("seller", object.getSeller());
		properties.put("buyer", object.getBuyer());
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("buildingInfo", object.getBuildingInfo());
		properties.put("buildingInfoId", object.getBuildingInfo().getTableId());
		properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
		properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
		properties.put("unitInfo", object.getUnitInfo());
		properties.put("unitInfoId", object.getUnitInfo().getTableId());
		properties.put("houseInfo", object.getHouseInfo());
		properties.put("houseInfoId", object.getHouseInfo().getTableId());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgxy_CoopAgreementSettleDtl> tgxy_CoopAgreementSettleDtlList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgxy_CoopAgreementSettleDtlList != null)
		{
			for(Tgxy_CoopAgreementSettleDtl object:tgxy_CoopAgreementSettleDtlList)
			{
				Properties properties = new MyProperties();
				
				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
//				properties.put("busiState", object.getBusiState());
//				properties.put("userStart", object.getUserStart());
//				properties.put("userStartId", object.getUserStart().getTableId());
//				properties.put("createTimeStamp", object.getCreateTimeStamp());
//				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
//				properties.put("userRecord", object.getUserRecord());
//				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", myDatetime.dateToString2(object.getRecordTimeStamp()));
//				properties.put("mainTable", object.getMainTable());
//				properties.put("mainTableId", object.getMainTable().getTableId());
				properties.put("agreementCode", object.geteCode());
				properties.put("agreementDate", object.getAgreementDate());
				properties.put("seller", object.getSeller());
				properties.put("buyer", object.getBuyer());
//				properties.put("project", object.getProject());
//				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
//				properties.put("buildingInfo", object.getBuildingInfo());
//				properties.put("buildingInfoId", object.getBuildingInfo().getTableId());
//				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("eCodeFromConstruction", object.geteCodeFromConstruction());
				
//				Empj_UnitInfo unit = new Empj_UnitInfo();
				if(null != object.getUnitInfo())
				{
					// 关联单元
					properties.put("unitInfoName", object.getUnitInfo().getTheName());
				}				
				
//				properties.put("unitInfoId", object.getUnitInfo().getTableId());
				if(null != object.getHouseInfo())
				{
					// 关联单元
					properties.put("houseInfoName", object.getHouseInfo().getRoomId());
				}
				
//				properties.put("houseInfoId", object.getHouseInfo().getTableId());
				
				list.add(properties);
			}
		}
		return list;
	}
}
