package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * @Author: clj
 * @Data: 2022/12/5  14:51
 * @Decription:
 * @Modified:
 */
@Getter
@Setter
@ITypeAnnotation(remark = "用款申请拨付导出")
public class Tgpf_FundAppropriated_AFExcelVO {

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
     * 统筹日期
     */
    private String fundOverallPlanDate;
    /**
     * 支付状态
     */
    private String applystate;
    /**
     * 申请金额
     */
    private Double totalapplyamount;

}
