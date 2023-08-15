package zhishusz.housepresell.util.excel.model;

import java.util.LinkedHashMap;
import java.util.Map;

import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_HouseBusiState;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyInteger;

import lombok.Getter;
import lombok.Setter;

public class Empj_BuildingInfoDetailHouseInfoTemplate implements IExportExcel<Empj_HouseInfo>
{
	@IFieldAnnotation(remark = "预售系统户编号")
	private String eCodeFromPresellSystem;

	@IFieldAnnotation(remark = "托管系统户编号")
	private String eCodeFromEscrowSystem;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "户施工坐落")
	private String position;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "房间号")
	private String roomId;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "建筑面积")
	private Double actualArea;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "分摊面积")
	private Double shareConsArea;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "套内面积")
	private Double innerconsArea;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "单元号")
	private String unitInfoName;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "所在楼层")
	private Integer floor;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "房屋用途")
	private String purpose;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "户物价备价格")
	private Double recordPrice;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "户物价备案日期")
	private String lastTimeStampSyncRecordPriceToPresellSystem;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "房屋状态")
	private String theHouseState;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "三方协议状态")
	private String tripleAgreementState;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "操作人")
	private String userUpdateName;

	@Override
	public Map<String, String> GetExcelHead()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();

		map.put("eCodeFromPresellSystem", "预售系统户编号");
		map.put("eCodeFromEscrowSystem", "托管系统户编号");
		map.put("position", "户施工坐落");
		map.put("roomId", "房间号");
		map.put("actualArea", "建筑面积（㎡）");
		map.put("shareConsArea", "分摊面积（㎡）");
		map.put("innerconsArea", "套内面积（㎡）");
		map.put("unitInfoName", "单元号");
		map.put("floor", "所在楼层");
		map.put("purpose", "房屋用途");
		map.put("recordPrice", "户物价备价格");
		map.put("lastTimeStampSyncRecordPriceToPresellSystem", "户物价备案日期");
		map.put("theHouseState", "房屋状态");
		map.put("tripleAgreementState", "三方协议状态");
		map.put("userUpdateName", "操作人");

		return map;
	}

	public String getECodeFromPresellSystem()
	{
		return eCodeFromPresellSystem;
	}

	public void setECodeFromPresellSystem(String eCodeFromPresellSystem)
	{
		this.eCodeFromPresellSystem = eCodeFromPresellSystem;
	}

	public String getECodeFromEscrowSystem()
	{
		return eCodeFromEscrowSystem;
	}

	public void setECodeFromEscrowSystem(String eCodeFromEscrowSystem)
	{
		this.eCodeFromEscrowSystem = eCodeFromEscrowSystem;
	}

	@Override
	public void init(Empj_HouseInfo object)
	{
		this.setECodeFromPresellSystem(object.geteCodeFromPresellSystem());
		this.setECodeFromEscrowSystem(object.geteCodeFromEscrowSystem());
		this.setPosition(object.getPosition());
		this.setRoomId(object.getRoomId());
		this.setActualArea(object.getActualArea());
		this.setShareConsArea(object.getShareConsArea());
		this.setInnerconsArea(object.getInnerconsArea());
		Empj_UnitInfo unitInfo = object.getUnitInfo();
		if (unitInfo != null)
		{
			this.setUnitInfoName(unitInfo.getTheName());
		}
		this.setFloor(MyDouble.getInstance().getShort(object.getFloor(), 0).intValue());

		if (null != object.getPurpose())
		{
			/*
			 * 1 住宅
			 * 3 商业
			 * 4 办公
			 * 5 车库
			 * 9 其他
			 */
			String houseUse = "";

			switch (object.getPurpose())
			{
			case "1":
				
				houseUse = "住宅";
				
				break;

			case "3":

				houseUse = "商业";
				
				break;

			case "4":

				houseUse = "办公";
				
				break;

			case "5":

				houseUse = "车库";
				
				break;
				
			case "9":

				houseUse = "其他";
				
				break;
			}
			
			this.setPurpose(houseUse);
		}
		
		this.setRecordPrice(object.getRecordPrice());
		this.setLastTimeStampSyncRecordPriceToPresellSystem(
				MyDatetime.getInstance().dateToString(object.getLastTimeStampSyncRecordPriceToPresellSystem()));
		Integer busiState = MyInteger.getInstance().parse(object.getBusiState());
		if (busiState != null)
		{
			this.setTheHouseState(S_HouseBusiState.HouseBusiStateStrArr[busiState - 1]);
		}

		if (object.getTripleAgreement() != null)
		{
			
			String tripleAgreementState = "";

			switch (object.getTripleAgreement().getTheStateOfTripleAgreement())
			{
			case "0":
				
				tripleAgreementState = "未打印";
				
				break;

			case "1":

				tripleAgreementState = "已打印未上传";
				
				break;

			case "2":

				tripleAgreementState = "已上传";
				
				break;

			case "3":

				tripleAgreementState = "已备案";
				
				break;
				
			case "4":

				tripleAgreementState = "备案退回";
				
				break;
			}
			
			this.setTripleAgreementState(tripleAgreementState);
		}
		if (object.getUserUpdate() != null)
		{
			this.setUserUpdateName(object.getUserUpdate().getTheName());
		}
	}
}
