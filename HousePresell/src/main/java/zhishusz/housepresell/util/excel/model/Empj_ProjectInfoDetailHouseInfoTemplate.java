package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Empj_ProjectInfoDetailHouseInfoTemplate  implements IExportExcel<Empj_HouseInfo>
{
    @Getter @Setter @IFieldAnnotation(remark="预售系统户编号")
    private String eCodeFromPresellSystem;

    @Getter @Setter @IFieldAnnotation(remark="托管系统户编号")
    private String eCodeFromEscrowSystem;

    @Getter @Setter @IFieldAnnotation(remark="户施工编号")
    private String eCodeFromConstruction;

    @Getter @Setter @IFieldAnnotation(remark="户施工坐落")
    private String position;

    @Getter @Setter @IFieldAnnotation(remark="户公安编号")
    private String eCodeFromPublicSecurity;

//    @Getter @Setter @IFieldAnnotation(remark="户公安坐落")
//    private String eCodeFromPublicSecurity;

    @Getter @Setter @IFieldAnnotation(remark="建筑面积")
    private Double actualArea;

    @Getter @Setter @IFieldAnnotation(remark="分摊面积")
    private Double shareConsArea;

    @Getter @Setter @IFieldAnnotation(remark="套内面积")
    private Double innerconsArea;

    @Getter @Setter @IFieldAnnotation(remark="单元号")
    private String UnitName;

    @Getter @Setter @IFieldAnnotation(remark="所在楼层")
    private Integer floor;

    @Getter @Setter @IFieldAnnotation(remark="房屋用途")
    private String purpose;

    @Getter @Setter @IFieldAnnotation(remark="房屋状态")
    private Integer theHouseState;

    @Getter @Setter @IFieldAnnotation(remark="户物价备价格")
    private Double recordPrice;

    @Getter @Setter @IFieldAnnotation(remark="预售系统物价备案价格最后一次同步时间")
    private String lastTimeStampSyncRecordPriceToPresellSystem;

    @Getter @Setter @IFieldAnnotation(remark="户三方协议结算状态")
    private String settlementStateOfTripleAgreement;


    @Override
    public Map<String, String> GetExcelHead()
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("eCodeFromPresellSystem", "预售系统户编号");
        map.put("eCodeFromEscrowSystem", "托管系统户编号");
        map.put("eCodeFromConstruction", "户施工编号");
        map.put("position", "户施工坐落");
        map.put("eCodeFromPublicSecurity", "户公安编号");
//        map.put("eCode", "户公安坐落");
        map.put("actualArea", "建筑面积");
        map.put("shareConsArea", "分摊面积");
        map.put("innerconsArea", "套内面积");
        map.put("UnitName", "单元号");
        map.put("floor", "所在楼层");
        map.put("purpose", "房屋用途");
        map.put("theHouseState", "房屋状态");
        map.put("recordPrice", "户物价备价格");
        map.put("lastTimeStampSyncRecordPriceToPresellSystem", "预售系统物价备案价格最后一次同步时间");
        map.put("settlementStateOfTripleAgreement", "户三方协议结算状态");

        return map;
    }

    @Override
    public void init(Empj_HouseInfo object)
    {
        this.setECodeFromPresellSystem(object.geteCodeFromPresellSystem());
        this.setECodeFromEscrowSystem(object.geteCodeFromEscrowSystem());
        Empj_BuildingInfo buildingInfo = object.getBuilding();
        if (buildingInfo != null)
        {
            this.setECodeFromConstruction(buildingInfo.geteCodeFromConstruction());
            this.setECodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());
//        this.setECodeFromPublicSecurity();
//			properties.put("shareConsArea", object.getShareConsArea());							  //户公安坐落
        }
        this.setPosition(object.getPosition());
        this.setActualArea(object.getActualArea());
        this.setShareConsArea(object.getShareConsArea());
        this.setInnerconsArea(object.getInnerconsArea());
        Empj_UnitInfo unitInfo = object.getUnitInfo();
        if (unitInfo != null)
        {
            this.setUnitName(unitInfo.getTheName());
        }
        this.setFloor(MyDouble.getInstance().getShort(object.getFloor(), 0).intValue());
        this.setPurpose(object.getPurpose());
        this.setTheHouseState(object.getTheHouseState());
        this.setRecordPrice(object.getRecordPrice());
        this.setLastTimeStampSyncRecordPriceToPresellSystem(MyDatetime.getInstance().dateToString(object.getLastTimeStampSyncRecordPriceToPresellSystem()));

        if (object.getSettlementStateOfTripleAgreement() != null && object.getSettlementStateOfTripleAgreement() == 1)
        {
            this.setSettlementStateOfTripleAgreement("已结算");
        }
        else
        {
            this.setSettlementStateOfTripleAgreement("未结算");
        }
    }
}
