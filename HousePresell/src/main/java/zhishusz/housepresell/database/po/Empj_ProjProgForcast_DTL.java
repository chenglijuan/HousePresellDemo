package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 工程进度巡查-子
 * 
 * @author xsz
 * @since 2020-9-1 15:19:25
 */
@ITypeAnnotation(remark = "工程进度巡查-子")
public class Empj_ProjProgForcast_DTL implements Serializable {

    private static final long serialVersionUID = 8745075375442620918L;

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
    private Empj_ProjProgForcast_AF afEntity;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "是否达到正负零(-1：默认空 0：否 1：是)")
    private String hasAchieve;

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
    @IFieldAnnotation(remark = "地上层数")
    private Double floorUpNumber;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前进度节点")
    private String nowNodeName;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联当前进度节点")
    private Tgpj_BldLimitAmountVer_AFDtl nowNode;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "建设进度类型（1-主体结构 2-外立面装饰 3-室内装修）")
    private String buildProgressType;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前建设进度")
    private String buildProgress;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "备注")
    private String remark;

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
    @IFieldAnnotation(remark = "图片处理状态 1-已处理 ")
    private String handleState;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "网站推送状态")
    private String webPushState;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "网站审核状态")
    private String webHandelState;
    
    
    @Getter @Setter @IFieldAnnotation(remark="附件信息")
    private List<Sm_AttachmentCfg_Copy> smAttachmentCfgList;

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

}
