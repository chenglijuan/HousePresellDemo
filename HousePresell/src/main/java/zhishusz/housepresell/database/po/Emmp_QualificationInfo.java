package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：资质认证信息
 * */
@ITypeAnnotation(remark="资质认证信息")
public class Emmp_QualificationInfo implements Serializable
{
	private static final long serialVersionUID = -9208362054476279026L;

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
	
	@Getter @Setter @IFieldAnnotation(remark="关联机构")
	private Emmp_CompanyInfo company;
	
	@Getter @Setter @IFieldAnnotation(remark="类型")
	private String theType;

	@Getter @Setter @IFieldAnnotation(remark="等级")
	private Integer theLevel;

	@Getter @Setter @IFieldAnnotation(remark="附件")
	private Sm_Attachment attachment;
	
	@Getter @Setter @IFieldAnnotation(remark="发证日期yyyyDDmm")
	private String issuanceDate;
	
	@Getter @Setter @IFieldAnnotation(remark="有效期yyyyDDmm")
	private String expiryDate;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
