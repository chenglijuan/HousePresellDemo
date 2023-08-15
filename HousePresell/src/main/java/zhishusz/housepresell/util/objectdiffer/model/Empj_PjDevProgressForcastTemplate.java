package zhishusz.housepresell.util.objectdiffer.model;

import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.util.IFieldAnnotation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Hank on 2018-10-19.
 * Company: zhishusz
 */

public class Empj_PjDevProgressForcastTemplate
{
    @Getter @Setter
    private Empj_PjDevProgressForcast pjDevProgressForcast;
//    @Getter @Setter @IFieldAnnotation(remark = "开发企业名称")
//    private String developCompanyName;
//    @Getter @Setter @IFieldAnnotation(remark = "项目名称")
//    private String projectName;
//    @Getter @Setter @IFieldAnnotation(remark = "楼幢编号")
//    private String buildingECode;
//    @Getter @Setter @IFieldAnnotation(remark = "施工编号")
//    private String eCodeFromConstruction;
//    @Getter @Setter @IFieldAnnotation(remark = "公安编号")
//    private String eCodeFromPublicSecurity;
//    @Getter @Setter @IFieldAnnotation(remark = "楼幢坐落")
//    private String position;
//    @Getter @Setter @IFieldAnnotation(remark = "地上层数")
//    private Double upfloorNumber;
    @Getter @Setter @IFieldAnnotation(remark = "巡查日期")
    private String patrolTimeStampString;
    @Getter @Setter @IFieldAnnotation(remark = "备注")
    private String remark;

//    @Getter @Setter @IFieldAnnotation(remark = "当前进度节点")
//    private String currentFigureProgress;
    @Getter @Setter @IFieldAnnotation(remark = "进度详情列表")
    private List<HashMap> forcastDtlList;


//    public Empj_PjDevProgressForcast getPjDevProgressForcast() {
//        return pjDevProgressForcast;
//    }
//
//    public void setPjDevProgressForcast(Empj_PjDevProgressForcast pjDevProgressForcast)
//    {
//        MyDouble muDouble = MyDouble.getInstance();
//
//        Emmp_CompanyInfo developCompany = pjDevProgressForcast.getDevelopCompany();
//        if (developCompany != null)
//        {
//            this.setDevelopCompanyName(developCompany.getTheName());
//        }
//        Empj_ProjectInfo projectInfo = pjDevProgressForcast.getProject();
//        if (projectInfo != null)
//        {
//            this.setProjectName(projectInfo.getTheName());
//        }
//        Empj_BuildingInfo building = pjDevProgressForcast.getBuilding();
//        if (building != null)
//        {
//            this.setBuildingECode(building.geteCode());
//            this.setPosition(building.getPosition());
//            this.setECodeFromConstruction(building.geteCodeFromConstruction());
//            this.setECodeFromPublicSecurity(building.geteCodeFromPublicSecurity());
//            this.setUpfloorNumber(muDouble.getShort(building.getUpfloorNumber(), 0));
//        }
//        this.setPatrolTimestamp(MyDatetime.getInstance().dateToSimpleString(pjDevProgressForcast.getPatrolTimestamp()));
//
//        Integer currentFigureProgress = 100;
//        if (pjDevProgressForcast.getCurrentFigureProgress() != null)
//        {
//            currentFigureProgress = pjDevProgressForcast.getCurrentFigureProgress().intValue();
//        }
//        String currentFigureProgressStr = null;
//        switch (currentFigureProgress){
//            case 100:
//                currentFigureProgressStr = "正负零前";
//                break;
//            case 80:
//                currentFigureProgressStr = "正负零";
//                break;
//            case 60:
//                currentFigureProgressStr = "主体结构达到1/2";
//                break;
//            case 40:
//                currentFigureProgressStr = "主体结构封顶";
//                break;
//            case 35:
//            case 20:
//                currentFigureProgressStr = "外立面装饰工程完成 ";
//                break;
//            case 5:
//                currentFigureProgressStr = "室内装修工程完成";
//                break;
//            default:
//                currentFigureProgressStr = "完成交付使用备案";
//        }
//        this.setCurrentFigureProgress(currentFigureProgressStr);
//
//        this.pjDevProgressForcast = pjDevProgressForcast;
//    }
//
//    public void createSpecialLogFieldWithDtlList(List<Empj_PjDevProgressForcastDtl> detailList)
//    {
//        if (detailList == null || detailList.size() < 1)
//        {
//            this.setEmpj_pjDevProgressForcastDtlListString("无进度详情列表");
//        }
//        else
//        {
//            List<String> allDescriptionStringList = new ArrayList<String>();
//            for (Empj_PjDevProgressForcastDtl itemPjDevProgressForcastDtl : detailList)
//            {
//                List<String> descriptionStringList = new ArrayList<String>();
//                Integer predictedFigureProgress = 100;
//                if (itemPjDevProgressForcastDtl.getPredictedFigureProgress() != null)
//                {
//                    predictedFigureProgress = itemPjDevProgressForcastDtl.getPredictedFigureProgress().intValue();
//                }
//                String predictedFigureProgressStr = "";
//                switch (predictedFigureProgress)
//                {
//                    case 100:
//                        predictedFigureProgressStr = "正负零前";
//                        break;
//                    case 80:
//                        predictedFigureProgressStr = "正负零";
//                        break;
//                    case 60:
//                        predictedFigureProgressStr = "主体结构达到1/2";
//                        break;
//                    case 40:
//                        predictedFigureProgressStr = "主体结构封顶";
//                        break;
//                    case 35:
//                    case 20:
//                        predictedFigureProgressStr = "外立面装饰工程完成 ";
//                        break;
//                    case 5:
//                        predictedFigureProgressStr = "室内装修工程完成";
//                        break;
//                    default:
//                        predictedFigureProgressStr = "完成交付使用备案";
//                }
//                descriptionStringList.add(predictedFigureProgressStr);
//                descriptionStringList.add(MyDatetime.getInstance().dateToSimpleString(itemPjDevProgressForcastDtl.getPredictedFinishDatetime()));
//                descriptionStringList.add(MyDatetime.getInstance().dateToSimpleString(itemPjDevProgressForcastDtl.getOgPredictedFinishDatetime()));
//                if (itemPjDevProgressForcastDtl.getProgressJudgement() == 1)
//                {
//                    descriptionStringList.add("滞后");
//                }
//                else
//                {
//                    descriptionStringList.add("正常");
//                }
//                descriptionStringList.add(itemPjDevProgressForcastDtl.getCauseDescriptionForDelay());
//
////                Sm_User userUpdate = itemPjDevProgressForcastDtl.getUserUpdate();
////                if (userUpdate == null)
////                {
////                    userUpdate =  itemPjDevProgressForcastDtl.getUserStart();
////                }
////                if (userUpdate != null)
////                {
////                    descriptionStringList.add(MyString.getInstance().parse(userUpdate.getTableId()));
////                    descriptionStringList.add(userUpdate.getTheName());
////                }
////                Long operationTimeStamp = itemPjDevProgressForcastDtl.getLastUpdateTimeStamp();
////                if (operationTimeStamp == null || operationTimeStamp < 1)
////                {
////                    operationTimeStamp = itemPjDevProgressForcastDtl.getCreateTimeStamp();
////                }
////                descriptionStringList.add(MyDatetime.getInstance().dateToSimpleString(operationTimeStamp));
//
//                String descriptionString = StringUtils.join(descriptionStringList, "、");
//                allDescriptionStringList.add(descriptionString);
//            }
//            String allDescriptionString = StringUtils.join(allDescriptionStringList, ";\n");
//            this.setEmpj_pjDevProgressForcastDtlListString(allDescriptionString);
//        }
//    }

    public List getNeedFieldList()
    {
        return Arrays.asList("patrolTimeStampString", "remark", "forcastDtlList");
//        return Arrays.asList("developCompanyName", "projectName", "buildingECode", "eCodeFromConstruction",
//                "eCodeFromPublicSecurity", "position", "upfloorNumber", "pjDevProgressForcast/patrolPerson",
//                "patrolTimestamp", "currentFigureProgress", "pjDevProgressForcast/remark",
//                "empj_pjDevProgressForcastDtlListString");
    }
}
