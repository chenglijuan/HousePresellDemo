package zhishusz.housepresell.database.po;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 项目进度巡查-子
 * 
 * @author xsz
 * @since 2020-9-1 15:19:25
 */
@ITypeAnnotation(remark = "项目进度巡查-子")
public class Empj_ProjProgInspection_DTL implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9151247053375749921L;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "表ID", isPrimarykey = true)
    private Long tableId;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "巡查单号-主")
    private String afCode;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联主表信息")
    private Empj_ProjProgInspection_AF afInfo;

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
    @IFieldAnnotation(remark = "当前建设进度")
    private String buildProgress;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "数据来源")
    private String dataSources;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测节点名称")
    private String forecastNodeName;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测节点信息")
    private Tgpj_BldLimitAmountVer_AFDtl forecastNode;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测完成日期")
    private String forecastCompleteDate;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "进度判定(1-正常 2-延期 3-滞后 4-停工)")
    private String determine;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "原因")
    private String reason;

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

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

}
