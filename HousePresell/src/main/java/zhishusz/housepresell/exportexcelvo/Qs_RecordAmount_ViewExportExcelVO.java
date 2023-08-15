package zhishusz.housepresell.exportexcelvo;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计报表-入账金额核对表-导出Excel
 * @ClassName:  Qs_RecordAmount_ViewExportExcelVO   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月20日 下午2:46:50   
 * @version V1.0 
 *
 */
public class Qs_RecordAmount_ViewExportExcelVO
{
	
	@Getter @Setter
	private Integer ordinal;//序号 
	
	@Getter @Setter @IFieldAnnotation(remark="开户行名称")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户")
	private String theAccountOfEscrow;
	
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

}
