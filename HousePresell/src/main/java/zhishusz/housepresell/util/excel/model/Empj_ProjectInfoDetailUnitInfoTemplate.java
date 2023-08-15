package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDouble;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Empj_ProjectInfoDetailUnitInfoTemplate  implements IExportExcel<Empj_UnitInfo>
{
    @Getter @Setter @IFieldAnnotation(remark="单元编号")
    private String eCode;

    @Getter @Setter @IFieldAnnotation(remark="楼幢编号")
    private String buildingECode;

    @Getter @Setter @IFieldAnnotation(remark="单元名称")
    private String theName;

    @Getter @Setter @IFieldAnnotation(remark="地上楼层数")
    private Integer upfloorNumber;

    @Getter @Setter @IFieldAnnotation(remark="地上每层户数")
    private Integer upfloorHouseHoldNumber;

    @Getter @Setter @IFieldAnnotation(remark="地下楼层数")
    private Integer downfloorNumber;

    @Getter @Setter @IFieldAnnotation(remark="地下每层户数")
    private Integer downfloorHouseHoldNumber;

    @Getter @Setter @IFieldAnnotation(remark="有无电梯")
    private String iselevator;

    @Getter @Setter @IFieldAnnotation(remark="电梯数")
    private Integer elevatorNumber;

    @Getter @Setter @IFieldAnnotation(remark="有无二次供水")
    private String hasSecondaryWaterSupply;

    @Getter @Setter @IFieldAnnotation(remark="备注")
    private String remark;

    @Override
    public Map<String, String> GetExcelHead()
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("eCode", "单元编号");
        map.put("buildingECode", "楼幢编号");
        map.put("theName", "单元名称");
        map.put("upfloorNumber", "地上楼层数");
        map.put("upfloorHouseHoldNumber", "地上每层户数");
        map.put("downfloorNumber", "地下楼层数");
        map.put("downfloorHouseHoldNumber", "地下每层户数");
        map.put("iselevator", "有无电梯");
        map.put("elevatorNumber", "电梯数");
        map.put("hasSecondaryWaterSupply", "有无二次供水");
        map.put("remark", "备注");

        return map;
    }

    @Override
    public void init(Empj_UnitInfo object)
    {
        this.setECode(object.geteCode());
        Empj_BuildingInfo buildingInfo = object.getBuilding();
        if (buildingInfo != null)
        {
            this.setBuildingECode(buildingInfo.geteCode());
        }

        this.setTheName(object.getTheName());
        this.setUpfloorNumber(MyDouble.getInstance().getShort(object.getUpfloorNumber(), 0).intValue());
        this.setUpfloorHouseHoldNumber(object.getUpfloorHouseHoldNumber());
        this.setDownfloorNumber(MyDouble.getInstance().getShort(object.getDownfloorNumber(), 0).intValue());
        this.setDownfloorHouseHoldNumber(object.getDownfloorHouseHoldNumber());
        if (object.getElevatorNumber() !=null && object.getElevatorNumber()>=4)
        {
            this.setIselevator("有");
        }
        else
        {
            this.setIselevator("无");
        }
        this.setElevatorNumber(object.getElevatorNumber());
        if (object.getHasSecondaryWaterSupply() !=null && object.getHasSecondaryWaterSupply() == true)
        {
            this.setHasSecondaryWaterSupply("有");
        }
        else
        {
            this.setHasSecondaryWaterSupply("无");
        }
        this.setRemark(object.getRemark());
    }
}
