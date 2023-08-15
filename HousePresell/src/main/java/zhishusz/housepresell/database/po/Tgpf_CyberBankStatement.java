package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：网银对账-后台上传的账单原始Excel数据-主表    
 * 工作人员通过后台上传
 * */
@ITypeAnnotation(remark="网银对账-后台上传的账单原始Excel数据-主表")
public class Tgpf_CyberBankStatement implements Serializable
{
	private static final long serialVersionUID = 2779106381043009192L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="对账银行")
	private String theNameOfBank;

	@Getter @Setter @IFieldAnnotation(remark="托管账号", columnName="accountOfBankAccountEscrowed")
	private String theAccountOfBankAccountEscrowed;

	@Getter @Setter @IFieldAnnotation(remark="托管账号名称")
	private String theNameOfBankAccountEscrowed;

	@Getter @Setter @IFieldAnnotation(remark="开户行")
	private String theNameOfBankBranch;

	@Getter @Setter @IFieldAnnotation(remark="网银对账状态")
	private Integer reconciliationState;//【状态：0-未对账、1-已对账】

	@Getter @Setter @IFieldAnnotation(remark="对账人")
	private String reconciliationUser;//【对账反写】

	@Getter @Setter @IFieldAnnotation(remark="原始数据文件路径")
	private String orgFilePath;
	
	@Getter @Setter @IFieldAnnotation(remark="文件上传日期")
	private String uploadTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="文件上传状态")
	private Integer fileUploadState;//【状态：0-未上传、1-已提交】
	
	@Getter @Setter @IFieldAnnotation(remark="记账日期")
	private String billTimeStamp;
	
	@Getter @Setter 
	private Integer transactionCount;//交易总笔数
	
	@Getter @Setter 
	private Double transactionAmount;//交易总金额

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
