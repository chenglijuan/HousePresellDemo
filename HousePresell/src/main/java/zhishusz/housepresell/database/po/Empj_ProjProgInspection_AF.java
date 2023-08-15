package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 项目进度巡查-主
 * 
 * @author xsz
 * @since 2020-9-1 15:19:25
 */
@ITypeAnnotation(remark = "项目进度巡查-主")
public class Empj_ProjProgInspection_AF implements Serializable, IApprovable {

    /**
     *
     */
    private static final long serialVersionUID = -5996021640642640952L;

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
    @IFieldAnnotation(remark = "申请时间")
    private Long applyDate;

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

    /* (non-Javadoc)
     * @see zhishusz.housepresell.database.po.internal.IApprovable#getSourceType()
     */
    @Override
    public String getSourceType() {
        // TODO Auto-generated method stub
        return getClass().getName();
    }

    /* (non-Javadoc)
     * @see zhishusz.housepresell.database.po.internal.IApprovable#getSourceId()
     */
    @Override
    public Long getSourceId() {
        // TODO Auto-generated method stub
        return tableId;
    }

    /* (non-Javadoc)
     * @see zhishusz.housepresell.database.po.internal.IApprovable#getEcodeOfBusiness()
     */
    @Override
    public String getEcodeOfBusiness() {
        // TODO Auto-generated method stub
        return eCode;
    }

    /* (non-Javadoc)
     * @see zhishusz.housepresell.database.po.internal.IApprovable#getPeddingApprovalkey()
     */
    @Override
    public List<String> getPeddingApprovalkey() {
        // TODO Auto-generated method stub
        List<String> peddingApprovalkey = new ArrayList<>();
        peddingApprovalkey.add("busiState");
        peddingApprovalkey.add("theNameOfProject");
        return peddingApprovalkey;
    }

    /* (non-Javadoc)
     * @see zhishusz.housepresell.database.po.internal.IApprovable#updatePeddingApprovalDataAfterSuccess()
     */
    @Override
    public Boolean updatePeddingApprovalDataAfterSuccess() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see zhishusz.housepresell.database.po.internal.IApprovable#updatePeddingApprovalDataAfterFail()
     */
    @Override
    public Boolean updatePeddingApprovalDataAfterFail() {
        // TODO Auto-generated method stub
        return null;
    }

}
