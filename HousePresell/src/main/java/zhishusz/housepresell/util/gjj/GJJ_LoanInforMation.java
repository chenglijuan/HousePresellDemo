package zhishusz.housepresell.util.gjj;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * @Author: wanhongbo
 * @Data: 2021/6/25  17:04
 * @Decription:
 * @Modified:
 */
@ITypeAnnotation(remark="公积金贷款申请信息")
public class GJJ_LoanInforMation implements Serializable {

    @Getter@Setter
    @IFieldAnnotation(remark="表id",isPrimarykey = true)
    private Long tableId;

    @Getter@Setter
    @IFieldAnnotation(remark="贷款申请编号")
    private String loanApplicationEcide;

    @Getter@Setter
    @IFieldAnnotation(remark="借款人姓名")
    private String buyerName;

    @Getter@Setter
    @IFieldAnnotation(remark="借款人证件类型")
    private String buyerCardType;

    @Getter@Setter
    @IFieldAnnotation(remark="借款人证件号码")
    private String buyerCardNum;

    @Getter@Setter
    @IFieldAnnotation(remark="购房地址")
    private String buyerAddress;

    @Getter@Setter
    @IFieldAnnotation(remark="商品房备案合同编号")
    private String ecodeofcontractrecord;

    @Getter@Setter
    @IFieldAnnotation(remark="是否组合贷款")
    private int theComposeStete;

    @Getter@Setter
    @IFieldAnnotation(remark="贷款银行")
    private String loanBank;

    @Getter@Setter
    @IFieldAnnotation(remark="借款金额")
    private BigDecimal loanMoney;

    @Getter@Setter
    @IFieldAnnotation(remark="状态")
    private int theState;

    @Override
    public String toString() {
        return "GJJ_LoanInforMation{" +
                "tableId='" + tableId + '\'' +
                ", loanApplicationEcide='" + loanApplicationEcide + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", buyerCardType='" + buyerCardType + '\'' +
                ", buyerCardNum='" + buyerCardNum + '\'' +
                ", buyerAddress='" + buyerAddress + '\'' +
                ", ecodeofcontractrecord='" + ecodeofcontractrecord + '\'' +
                ", theComposeStete='" + theComposeStete + '\'' +
                ", loanBank='" + loanBank + '\'' +
                ", loanMoney='" + loanMoney + '\'' +
                ", theState='" + theState + '\'' +
                '}';
    }
}
