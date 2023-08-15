package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * @Author: chenglijuan
 * @Data: 2022/1/21  15:51
 * @Decription:
 * @Modified:
 */
@Getter
@Setter
@ITypeAnnotation(remark = "特殊拨付")
public class Tgpf_SpecialFundAppropriated_AFExcelVO {

    /**
     * 序号
     */
    private Integer ordinal;
    /**
     * 用款申请单号
     */
    private String ecode;
    /**
     * 用款申请日期
     */
    private String applydate;
    /**
     * 开发企业名称
     */
    private String theNameOfDevelopCompany;
    /**
     * 项目名称
     */
    private String theNameOfProject;
    /**
     * 关联楼栋
     */
    private String eCodeFromConstruction;
    /**
     * 审批状态
     */
    private String approvalstate;
    /**
     * 支付状态
     */
    private String applystate;
    /**
     * 申请金额
     */
    private Double totalapplyamount;
    /**
     * 收款方名称
     */
	private String theNameOfBankAccount;
    /**
     * 划款银行
     */
    /**
     * 付款日期
     */
    private String afPayoutDate;

}
