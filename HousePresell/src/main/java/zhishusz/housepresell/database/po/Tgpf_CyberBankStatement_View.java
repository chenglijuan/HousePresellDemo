package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：网银数据上传-Excel导出    
 * 工作人员通过后台上传
 * */
@ITypeAnnotation(remark="网银数据上传-Excel导出表")
public class Tgpf_CyberBankStatement_View implements Serializable
{
	private static final long serialVersionUID = 2779106381043009191L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

	@Getter @Setter @IFieldAnnotation(remark="托管账号", columnName="accountOfBankAccountEscrowed")
	private String theAccountOfBankAccountEscrowed;

	@Getter @Setter @IFieldAnnotation(remark="开户行")
	private String theNameOfBankBranch;

	@Getter @Setter @IFieldAnnotation(remark="文件上传日期")
	private String uploadTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="文件上传状态")
	private Integer fileUploadState;//【状态：0-未上传、1-已提交】
	
	@Getter @Setter @IFieldAnnotation(remark="记账日期")
	private String billTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="总笔数")
	private Integer transactionCount;//交易总笔数
	
	@Getter @Setter @IFieldAnnotation(remark="总金额")
	private Double transactionAmount;//交易总金额
	
	@Getter @Setter @IFieldAnnotation(remark="对账银行")
	private String theNameOfBank;

}
