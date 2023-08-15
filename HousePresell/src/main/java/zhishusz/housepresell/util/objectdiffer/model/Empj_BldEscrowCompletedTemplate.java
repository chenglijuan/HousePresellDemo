package zhishusz.housepresell.util.objectdiffer.model;

import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.util.IFieldAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyString;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Hank on 2018-10-19.
 * Company: zhishusz
 */

public class Empj_BldEscrowCompletedTemplate
{
    private Empj_BldEscrowCompleted bldEscrowCompleted;
    @Getter @Setter @IFieldAnnotation(remark = "开发企业名称")
    private String developCompanyName;
    @Getter @Setter @IFieldAnnotation(remark = "项目名称")
    private String projectName;
//    @Getter @Setter @IFieldAnnotation(remark = "项目托管面积")
//    private Double allBldEscrowSpace;
    @Getter @Setter @IFieldAnnotation(remark = "本次终止托管面积")
    private Double currentBldEscrowSpace;
    @Getter @Setter @IFieldAnnotation(remark = "托管终止楼幢列表")
    private String empj_BldEscrowCompleted_DtlListString;

    public void createSpecialLogFieldWithDtlList(List<Empj_BldEscrowCompleted_Dtl> empj_BldEscrowCompleted_DtlList)
    {
        if (empj_BldEscrowCompleted_DtlList == null || empj_BldEscrowCompleted_DtlList.size() < 1)
        {
//            this.setAllBldEscrowSpace(0.0);
            this.setCurrentBldEscrowSpace(0.0);
            this.setEmpj_BldEscrowCompleted_DtlListString("无托管终止楼幢列表");
        }
        else
        {
//            this.setAllBldEscrowSpace(0.0);
            this.setCurrentBldEscrowSpace(0.0);
            List<String> allDescriptionStringList = new ArrayList<String>();
            for (Empj_BldEscrowCompleted_Dtl itemBldEscrowCompleted_Dtl : empj_BldEscrowCompleted_DtlList)
            {
                List<String> descriptionStringList = new ArrayList<String>();
                descriptionStringList.add(itemBldEscrowCompleted_Dtl.geteCode());
                descriptionStringList.add(itemBldEscrowCompleted_Dtl.geteCodeFromConstruction());
                descriptionStringList.add(itemBldEscrowCompleted_Dtl.geteCodeFromPublicSecurity());

                Empj_BuildingInfo building = itemBldEscrowCompleted_Dtl.getBuilding();
                if (building != null)
                {
                    MyDouble muDouble = MyDouble.getInstance();
                    MyString muString = MyString.getInstance();
                    this.setCurrentBldEscrowSpace(this.getCurrentBldEscrowSpace()+muDouble.getShort(building.getEscrowArea(), 2));

                    descriptionStringList.add(building.getEscrowStandard());
                    descriptionStringList.add(muString.parse(building.getUpfloorNumber()));
                    descriptionStringList.add(muString.parse(muDouble.getShort(building.getBuildingArea(), 2)));
                    descriptionStringList.add(muString.parse(muDouble.getShort(building.getEscrowArea(), 2)));
                    descriptionStringList.add(building.getDeliveryType());
                    Tgpj_BuildingAccount buildingAccount = building.getBuildingAccount();
                    if (buildingAccount != null)
                    {
                        descriptionStringList.add(muString.parse(muDouble.getShort(buildingAccount.getCurrentEscrowFund(), 2)));
                        descriptionStringList.add(muString.parse(muDouble.getShort(buildingAccount.getAllocableAmount(), 2)));
                        descriptionStringList.add(buildingAccount.getCurrentFigureProgress());
                        descriptionStringList.add(muString.parse(muDouble.getShort(buildingAccount.getEffectiveLimitedAmount(), 2)));
                    }
                    descriptionStringList.add(building.getDeliveryType());
                }
                String descriptionString = StringUtils.join(descriptionStringList, "、");
                allDescriptionStringList.add(descriptionString);
            }
            String allDescriptionString = StringUtils.join(allDescriptionStringList, ";\n");
            this.setEmpj_BldEscrowCompleted_DtlListString(allDescriptionString);
        }
    }

    public List getNeedFieldList()
    {
        return Arrays.asList("bldEscrowCompleted/eCodeFromDRAD" ,"developCompanyName", "projectName",
                "allBldEscrowSpace", "currentBldEscrowSpace", "empj_BldEscrowCompleted_DtlListString");
    }

    public Empj_BldEscrowCompleted getBldEscrowCompleted() {
        return bldEscrowCompleted;
    }

    public void setBldEscrowCompleted(Empj_BldEscrowCompleted bldEscrowCompleted) {
        Emmp_CompanyInfo developCompany = bldEscrowCompleted.getDevelopCompany();
        if (developCompany != null)
        {
            this.setDevelopCompanyName(developCompany.getTheName());
        }
        Empj_ProjectInfo projectInfo = bldEscrowCompleted.getProject();
        if (projectInfo != null)
        {
            this.setProjectName(projectInfo.getTheName());
        }

        this.bldEscrowCompleted = bldEscrowCompleted;
    }

}
