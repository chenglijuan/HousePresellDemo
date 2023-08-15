package zhishusz.housepresell.controller.form;

import java.io.Serializable;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentBond;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付保函子表
 * 
 * @author xsz
 * @since 2020-5-11 15:46:43
 */
@ITypeAnnotation(remark = "支付保函子表")
public class Empj_PaymentBondChildForm extends NormalActionForm {

    private static final long serialVersionUID = -2897144323559134138L;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "表ID", isPrimarykey = true)
    private Long tableId;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联楼幢Id")
    private Long buildingId;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联楼幢")
    private Empj_BuildingInfo empj_BuildingInfo;
    @Setter
    @IFieldAnnotation(remark = "施工编号")
    private String eCodeFromConstruction;
    @Setter
    @IFieldAnnotation(remark = "公安编号")
    private String eCodeFromPublicSecurity;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "初始受限额度（元） ")
    private Double orgLimitedAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前形象进度  ")
    private String currentFigureProgress;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前受限比例（%）")
    private Double currentLimitedRatio;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前节点受限额度（元）")
    private Double nodeLimitedAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前托管余额（元）")
    private Double currentEscrowFund;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "溢出金额） ")
    private Double spilloverAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "现金额度最低控制线")
    private Double controlAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "最高可释放额度")
    private Double releaseAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "本次保函金额（元）")
    private Double paymentBondAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "实际可释放金额（元）")
    private Double actualReleaseAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "现金受限额度（元）")
    private Double cashLimitedAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "有效受限额度（元）")
    private Double effectiveLimitedAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "办理后现金受限额度（元）")
    private Double afterCashLimitedAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "办理后有效受限额度（元）")
    private Double afterEffectiveLimitedAmount;
    @Getter
    @Setter
    @IFieldAnnotation(remark = "本次可申请金额（元）")
    private Double canApplyAmount;
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
    @IFieldAnnotation(remark = "关联支付保函主键")
    private Empj_PaymentBond empj_PaymentBond;

    public String geteCodeFromConstruction() {
        return eCodeFromConstruction;
    }

    public void seteCodeFromConstruction(String eCodeFromConstruction) {
        this.eCodeFromConstruction = eCodeFromConstruction;
    }

    public String geteCodeFromPublicSecurity() {
        return eCodeFromPublicSecurity;
    }

    public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity) {
        this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
    }

    

}
