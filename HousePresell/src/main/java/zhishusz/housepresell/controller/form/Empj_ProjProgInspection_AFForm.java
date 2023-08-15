package zhishusz.housepresell.controller.form;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 项目进度巡查-主
 * 
 * @author xsz
 * @since 2020-9-1 15:19:25
 */
@ITypeAnnotation(remark = "项目进度巡查-主")
public class Empj_ProjProgInspection_AFForm extends NormalActionForm {


    /**
     *
     */
    private static final long serialVersionUID = -7932976119302219540L;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "表ID", isPrimarykey = true)
    private Long tableId;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "单据号")
    private String code;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "所属区域")
    private String areaName;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联区域")
    private Sm_CityRegionInfo areaInfo;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "项目名称")
    private String projectName;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联项目")
    private Empj_ProjectInfo project;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "施工编号")
    private String buildCode;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联楼幢信息")
    private Empj_BuildingInfo buildInfo;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "交付类型")
    private String deliveryType;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前建设进度")
    private String buildProgress;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前节点名称")
    private String nowNodeName;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前节点信息")
    private Tgpj_BldLimitAmountVer_AFDtl nowNode;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "数据来源")
    private String dataSources;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "更新日期")
    private Long updateDateTime;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "状态 S_TheState 初始为Normal")
    private Integer theState;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "业务状态")
    private String busiState;

    @IFieldAnnotation(remark = "编号")
    private String eCode;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "创建人")
    private Sm_User userStart;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "创建时间")
    private Long createTimeStamp;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "修改人")
    private Sm_User userUpdate;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "最后修改日期")
    private Long lastUpdateTimeStamp;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "备案人")
    private Sm_User userRecord;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "备案日期")
    private Long recordTimeStamp;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "版本号")
    private Integer versionNo;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "流程状态 待提交/审核中/已完结")
    private String approvalState;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "区域")
    private Long areaId;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "项目")
    private Long projectId;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "申请时间")
    private Long applyDate;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "子表信息")
    private List<Empj_ProjProgInspection_DTLForm> empj_ProjProgInspection_DTL;

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

}
