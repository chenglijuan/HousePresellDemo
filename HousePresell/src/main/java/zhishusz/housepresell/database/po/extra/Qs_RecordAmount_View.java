package zhishusz.housepresell.database.po.extra;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计报表-入账金额核对表
 * @ClassName:  Qs_RecordAmount_View   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月5日 下午7:06:29   
 * @version V1.0 
 *
 */
@ITypeAnnotation(remark="入账金额核对表")
public class Qs_RecordAmount_View implements Serializable
{
	
	private static final long serialVersionUID = 7192134915788423838L;

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

}
