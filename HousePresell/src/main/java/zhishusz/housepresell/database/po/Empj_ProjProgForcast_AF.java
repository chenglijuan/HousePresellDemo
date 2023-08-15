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
 * 工程进度巡查-主
 * 
 * @author xsz
 * @since 2020-9-1 15:19:25
 */
@ITypeAnnotation(remark = "工程进度巡查-主")
public class Empj_ProjProgForcast_AF implements Serializable, IApprovable {

    private static final long serialVersionUID = -730979148793070326L;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "表ID", isPrimarykey = true)
    private Long tableId;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "巡查单号")
    private String code;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "提交日期")
    private String submitDate;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "巡查时间")
    private String forcastTime;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "巡查人")
    private String forcastPeople;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "巡查机构")
    private String companyName;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联巡查机构")
    private Emmp_CompanyInfo company;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "所属区域")
    private String areaName;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联区域")
    private Sm_CityRegionInfo area;

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
    @IFieldAnnotation(remark = "总幢数")
    private Integer buildCount;

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
    @IFieldAnnotation(remark = "申请时间")
    private Long applyDate;
    
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
