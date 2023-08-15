package zhishusz.housepresell.util.excel.model;

import java.util.HashMap;
import java.util.Map;

import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.util.IFieldAnnotation;

import zhishusz.housepresell.util.MyDatetime;
import lombok.Getter;
import lombok.Setter;

public class Empj_PjDevProgressForcastTemplate implements IExportExcel<Empj_PjDevProgressForcast>
{
    @Getter @Setter @IFieldAnnotation(remark="工程进度预测单号")
    private String eCode;

    @Getter @Setter @IFieldAnnotation(remark="开发企业")
    private String developCompanyName;

    @Getter @Setter @IFieldAnnotation(remark="项目名称")
    private String projectName;

//    @Getter @Setter @IFieldAnnotation(remark="楼幢编号")
//    private String eCodeOfBuilding;

    @Getter @Setter @IFieldAnnotation(remark="施工编号")
    private String eCodeFromConstruction;

    @Getter @Setter @IFieldAnnotation(remark="当前进度节点")
    private String currentFigureProgress;

    @Getter @Setter @IFieldAnnotation(remark="巡查日期")
    private String patrolTimestamp;

    @Getter @Setter @IFieldAnnotation(remark="状态")
    private String busiState;

    @Override
    public Map<String, String> GetExcelHead()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("eCode", "工程进度预测单号");
        map.put("developCompanyName", "开发企业");
        map.put("projectName", "项目名称");
//        map.put("eCodeOfBuilding", "楼幢编号");
        map.put("eCodeFromConstruction", "施工编号");
        map.put("currentFigureProgress", "当前进度节点");
        map.put("patrolTimestamp", "巡查日期");
        map.put("busiState", "状态");

        return map;
    }

    @Override
    public void init(Empj_PjDevProgressForcast object)
    {
        this.setECode(object.geteCode());
        Emmp_CompanyInfo companyInfo = object.getDevelopCompany();
        if (companyInfo != null)
        {
            this.setDevelopCompanyName(companyInfo.getTheName());
        }
        Empj_ProjectInfo projectInfo = object.getProject();
        if (projectInfo != null)
        {
            this.setProjectName(projectInfo.getTheName());
        }
        Empj_BuildingInfo buildingInfo = object.getBuilding();
        if (buildingInfo != null)
        {
//            this.setECodeOfBuilding(buildingInfo.geteCode());
            this.setECodeFromConstruction(buildingInfo.geteCodeFromConstruction());
            Tgpj_BuildingAccount buildingAccount = buildingInfo.getBuildingAccount();
            if (buildingAccount != null)
            {
                this.setCurrentFigureProgress(buildingAccount.getCurrentFigureProgress());
            }
        }
        this.setPatrolTimestamp(MyDatetime.getInstance().dateToSimpleString(object.getPatrolTimestamp()));

        this.setBusiState(object.getBusiState());
    }
}
