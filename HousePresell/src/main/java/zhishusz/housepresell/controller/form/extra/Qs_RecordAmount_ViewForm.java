package zhishusz.housepresell.controller.form.extra;

import zhishusz.housepresell.controller.form.NormalActionForm;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：入账金额核对表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Qs_RecordAmount_ViewForm extends NormalActionForm
{

	private static final long serialVersionUID = -4618122406165981835L;

	@Getter @Setter @IFieldAnnotation(remark="表ID")
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="流水号")
	private String serialNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="银行名称")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户")
	private String theAccountOfEscrow;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户名称")
	private String theNameOfEscrow;
	
	@Getter @Setter @IFieldAnnotation(remark="网银入账金额")
	private Double silverAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="网银入账笔数")
	private Integer silverNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="托管系统入账金额")
	private Double escrowAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="托管系统入账笔数")
	private Integer escrowNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="比对差额")
	private Double compareDifference;
	
	@Getter @Setter @IFieldAnnotation(remark="差异备注")
	private String differenceNote;
	
	@Getter @Setter @IFieldAnnotation(remark="入账日期")
	private String recordDate;
	
	@Getter @Setter @IFieldAnnotation(remark="入账起始日期")
	private String recordDateStart;
	
	@Getter @Setter @IFieldAnnotation(remark="入账结束日期")
	private String recordDateEnd;
	
	@Getter
    @Setter
    @IFieldAnnotation(remark = "预测完成日期")
    private String FORECASTCOMPLETEDATE;
	
	@Getter
    @Setter
    @IFieldAnnotation(remark = "预测完成日期起始")
    private String billTimeStamp;
	
	@Getter
    @Setter
    @IFieldAnnotation(remark = "预测完成日期结束")
    private String endBillTimeStamp;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "企业ID")
    private Long COMMPANYID;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "项目ID")
    private Long PROJECTID;
	
}
