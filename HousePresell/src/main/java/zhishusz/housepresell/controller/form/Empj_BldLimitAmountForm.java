package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.controller.form.extra.BldLimitAmountForm;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.util.IFieldAnnotation;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：申请表-进度节点变更 Company：ZhiShuSZ
 */
@ToString(callSuper = true)
public class Empj_BldLimitAmountForm extends NormalActionForm {
    private static final long serialVersionUID = 5141829442751659556L;
    @Getter
    @Setter
    private Long tableId;// 表ID
    @Getter
    @Setter
    private Integer theState;// 状态 S_TheState 初始为Normal
    @Getter
    @Setter
    private String busiState;// 业务状态
    @Setter
    private String eCode;// 编号
    @Getter
    @Setter
    private Sm_User userStart;// 创建人
    @Getter
    @Setter
    private Long userStartId;// 创建人-Id
    @Getter
    @Setter
    private Long createTimeStamp;// 创建时间
    @Getter
    @Setter
    private Sm_User userUpdate;// 修改人
    @Getter
    @Setter
    private Long userUpdateId;// 修改人-Id
    @Getter
    @Setter
    private Long lastUpdateTimeStamp;// 最后修改日期
    @Getter
    @Setter
    private Sm_User userRecord;// 备案人
    @Getter
    @Setter
    private Long userRecordId;// 备案人-Id
    @Getter
    @Setter
    private Long recordTimeStamp;// 备案日期
    @Getter
    @Setter
    private Emmp_CompanyInfo developCompany;// 关联开发企业
    @Getter
    @Setter
    private Long developCompanyId;// 关联开发企业-Id
    @Setter
    private String eCodeOfDevelopCompany;// 开发企业编号
    @Getter
    @Setter
    private Empj_ProjectInfo project;// 关联项目
    @Getter
    @Setter
    private Long projectId;// 关联项目-Id
    @Getter
    @Setter
    private String theNameOfProject;// 项目名称-冗余
    @Setter
    private String eCodeOfProject;// 项目编号

    @Getter
    @Setter
    @IFieldAnnotation(remark = "联系人A")
    private String contactOne;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "A联系方式")
    private String telephoneOne;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "联系人B")
    private String contactTwo;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "B联系方式")
    private String telephoneTwo;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "审批结果（0-不通过 1-通过）")
    private String approvalResult;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "审批批语")
    private String approvalInfo;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "主表申请状态")
    private String afState;

    @Getter
    @Setter
    private List<BldLimitAmountForm> dtlList;

    @Getter
    @Setter
    private List<Map<String, Object>> attachmentList;

    @Getter
    @Setter
    private String isSign;// 是否签章

    @Getter
    @Setter
    @IFieldAnnotation(remark = "总包单位")
    private String countUnit;

    // 监理机构
    @Getter
    @Setter
    private Long companyId;

    // 监理机构
    @Getter
    @Setter
    private String companyName;

    // 完成时间start
    @Getter
    @Setter
    private String completeStart;

    // 完成时间end
    @Getter
    @Setter
    private String completeEnd;
    
 // 完成时间start
    @Getter
    @Setter
    private Long completeStartLong;

    // 完成时间end
    @Getter
    @Setter
    private Long completeEndLong;
    

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

    public String geteCodeOfDevelopCompany() {
        return eCodeOfDevelopCompany;
    }

    public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
        this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
    }

    public String geteCodeOfProject() {
        return eCodeOfProject;
    }

    public void seteCodeOfProject(String eCodeOfProject) {
        this.eCodeOfProject = eCodeOfProject;
    }

}
