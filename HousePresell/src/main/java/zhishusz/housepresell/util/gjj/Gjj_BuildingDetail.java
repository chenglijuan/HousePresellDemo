package zhishusz.housepresell.util.gjj;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * @Author: chenglijuan
 * @Data: 2021/6/4  15:28
 * @Decription:
 * @Modified:
 */
@ITypeAnnotation(remark="公积金对接")
public class Gjj_BuildingDetail {

    @Getter @Setter @IFieldAnnotation(remark="楼幢ID（托管）")
    private Long tableId;

    @Getter @Setter @IFieldAnnotation(remark="楼幢ID（托管）")
    private Long buildingId;

    @Getter @Setter @IFieldAnnotation(remark="开发企业名称")
    private String companyName;

    @Getter @Setter @IFieldAnnotation(remark="项目名称")
    private String projectName;

    @Getter @Setter @IFieldAnnotation(remark="预售证编号")
    private String ecodeofpresell;

    @Getter @Setter @IFieldAnnotation(remark="施工编号")
    private String eCodeFromConstruction;

    @Getter @Setter @IFieldAnnotation(remark="公安编号")
    private String eCodeFromPublicSecurity;

    @Getter @Setter @IFieldAnnotation(remark="附件")
    private List<String> smAttachmentCfgList;

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEcodeofpresell() {
        return ecodeofpresell;
    }

    public void setEcodeofpresell(String ecodeofpresell) {
        this.ecodeofpresell = ecodeofpresell;
    }

    public String geteCodeFromConstruction() {
        return eCodeFromConstruction;
    }

    public void seteCodeFromConstruction(String eCodeFromConstruction) {
        this.eCodeFromConstruction = eCodeFromConstruction;
    }

    public String geteCodeFromPublicSecurity() {
        return eCodeFromPublicSecurity;
    }

    public List<String> getSmAttachmentCfgList() {
        return smAttachmentCfgList;
    }

    public void setSmAttachmentCfgList(List<String> smAttachmentCfgList) {
        this.smAttachmentCfgList = smAttachmentCfgList;
    }

    public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity) {
        this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
    }


}
