package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：申请-用款-明细
 */
@ITypeAnnotation(remark = "申请-用款-明细")
public class Tgpf_FundAppropriated_AFDtl implements Serializable {
    private static final long serialVersionUID = -5629650965562494801L;

    // ---------公共字段-Start---------//
    @Getter
    @Setter
    @IFieldAnnotation(remark = "表ID", isPrimarykey = true)
    private Long tableId;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "状态 S_TheState 初始为Normal")
    private Integer theState;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "业务状态")
    private String busiState;

    @IFieldAnnotation(remark = "编号")
    private String eCode;// eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

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
    // ---------公共字段-Start---------//

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联楼幢")
    private Empj_BuildingInfo building;

    @IFieldAnnotation(remark = "楼幢编号")
    private String eCodeOfBuilding;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "用款主表")
    private Tgpf_FundAppropriated_AF mainTable;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "关联监管账号")
    private Tgpj_BankAccountSupervised bankAccountSupervised;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "监管账号")
    private String supervisedBankAccount;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "本次划款申请金额（元）")
    private Double appliedAmount;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "拨付状态")
    private Integer payoutState;

    // 以下是冗余字段

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前可划拨金额（元）")
    private Double allocableAmount;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "托管标准（元/㎡）")
    private String escrowStandard;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "初始受限额度（元）")
    private Double orgLimitedAmount;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前形象进度")
    private String currentFigureProgress;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前受限比例（%）")
    private Double currentLimitedRatio;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前受限额度（元）")
    private Double currentLimitedAmount;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "总入账金额（元）")
    private Double totalAccountAmount;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "已申请拨付金额（元）")
    private Double appliedPayoutAmount;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前托管余额（元）")
    private Double currentEscrowFund;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "退房退款金额（元）")
    private Double refundAmount;

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
    @IFieldAnnotation(remark = "实际可释放金额/实际可替代保证额度（元）")
    private Double actualReleaseAmount;

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

    public String geteCodeOfBuilding() {
        return eCodeOfBuilding;
    }

    public void seteCodeOfBuilding(String eCodeOfBuilding) {
        this.eCodeOfBuilding = eCodeOfBuilding;
    }

}
