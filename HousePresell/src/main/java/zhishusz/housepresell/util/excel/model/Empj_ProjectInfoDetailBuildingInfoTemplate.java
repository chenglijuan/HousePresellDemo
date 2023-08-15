package zhishusz.housepresell.util.excel.model;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDouble;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class Empj_ProjectInfoDetailBuildingInfoTemplate implements IExportExcel<Empj_BuildingInfo>
{
    @Getter @Setter @IFieldAnnotation(remark="楼幢编号")
    private String eCode;

    @Getter @Setter @IFieldAnnotation(remark="施工编号")
    private String eCodeFromConstruction;

    @Getter @Setter @IFieldAnnotation(remark="公安编号")
    private String eCodeFromPublicSecurity;

    @Getter @Setter @IFieldAnnotation(remark="建筑面积")
    private Double buildingArea;

    @Getter @Setter @IFieldAnnotation(remark="托管面积")
    private Double escrowArea;

    @Getter @Setter @IFieldAnnotation(remark="托管标准")
    private String escrowStandard;

    @Getter @Setter @IFieldAnnotation(remark="房屋用途")
    private String purpose;

    @Getter @Setter @IFieldAnnotation(remark="地上层数")
    private Integer upfloorNumber;

    @Getter @Setter @IFieldAnnotation(remark="地下层数")
    private Integer downfloorNumber;

    @Getter @Setter @IFieldAnnotation(remark="预售证号")
    private String eCodeFromPresellCert;

    @Getter @Setter @IFieldAnnotation(remark="土地抵押权人")
    private String landMortgagor;

    @Getter @Setter @IFieldAnnotation(remark="土地抵押状态")
    private Integer landMortgageState;

    @Override
    public Map<String, String> GetExcelHead()
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("eCode", "楼幢编号");
        map.put("eCodeFromConstruction", "施工编号");
        map.put("eCodeFromPublicSecurity", "公安编号");
        map.put("buildingArea", "建筑面积");
        map.put("escrowArea", "托管面积");
        map.put("escrowStandard", "托管标准");
        map.put("purpose", "房屋用途");
        map.put("upfloorNumber", "地上层数");
        map.put("downfloorNumber", "地下层数");
        map.put("eCodeFromPresellCert", "预售证号");
        map.put("landMortgagor", "土地抵押权人");
        map.put("landMortgageState", "土地抵押状态");

        return map;
    }

    @Override
    public void init(Empj_BuildingInfo object)
    {
        this.setECode(object.geteCode());
        this.setECodeFromConstruction(object.geteCodeFromConstruction());
        this.setECodeFromPublicSecurity(object.geteCodeFromPublicSecurity());
        this.setBuildingArea(MyDouble.getInstance().getShort(object.getBuildingArea(), 2));
        this.setEscrowArea(MyDouble.getInstance().getShort(object.getEscrowArea(), 2));
        this.setEscrowStandard(object.getEscrowStandard());
        this.setPurpose(object.getPurpose());

        this.setUpfloorNumber(MyDouble.getInstance().getShort(object.getUpfloorNumber(), 0).intValue());
        this.setDownfloorNumber(MyDouble.getInstance().getShort(object.getDownfloorNumber(), 0).intValue());
        this.setECodeFromPresellCert(object.geteCodeFromPresellCert());
        if(object.getExtendInfo() != null)
        {
            this.setLandMortgagor(object.getExtendInfo().getLandMortgagor());
            this.setLandMortgageState(object.getExtendInfo().getLandMortgageState());
        }
    }
}
