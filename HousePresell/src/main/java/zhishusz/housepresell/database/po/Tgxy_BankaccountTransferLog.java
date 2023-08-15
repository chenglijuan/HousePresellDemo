package zhishusz.housepresell.database.po;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;

/**
 * @Author: chenglijuan
 * @Data: 2021/12/23  10:56
 * @Decription:
 * @Modified:
 */
@ITypeAnnotation(remark = "托管银行转账记录日志表")
public class Tgxy_BankaccountTransferLog implements Serializable {

    private static final long serialVersionUID = -1543032687719847718L;

    //---------公共字段-Start---------//
    @Getter
    @Setter
    @IFieldAnnotation(remark="表ID", isPrimarykey=true)
    private Long tableId;

    @Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
    private Integer theState;


    @Getter @Setter @IFieldAnnotation(remark="创建人")
    private Long userStart;

    @Getter @Setter @IFieldAnnotation(remark="创建时间")
    private Long createTimeStamp;

    @Getter @Setter @IFieldAnnotation(remark="转账日期")
    private String transferTime;

    @Getter @Setter @IFieldAnnotation(remark="源id（转出银行）")
    private Long fromId;

    @Getter @Setter @IFieldAnnotation(remark="转出银行账号")
    private String fromAccount;

    @Getter @Setter @IFieldAnnotation(remark="目标银行（转入银行）")
    private Long toId;

    @Getter @Setter @IFieldAnnotation(remark="转入银行账号")
    private String toAccout;

    @Getter @Setter @IFieldAnnotation(remark="交易金额")
    private Double money;

    @Override
    public String toString() {
        return "Tgxy_BankaccountTransferLog{" +
                "tableId=" + tableId +
                ", theState=" + theState +
                ", userStart=" + userStart +
                ", createTimeStamp=" + createTimeStamp +
                ", transferTime='" + transferTime + '\'' +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", money=" + money +
                '}';
    }
}
