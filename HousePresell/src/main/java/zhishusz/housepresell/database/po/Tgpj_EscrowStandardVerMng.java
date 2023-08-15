package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.database.po.internal.IVersion;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：版本管理-托管标准
 * */
@ITypeAnnotation(remark="版本管理-托管标准")
public class Tgpj_EscrowStandardVerMng implements Serializable, IVersion, ILogable, IApprovable
{
	private static final long serialVersionUID = -4600427514728263056L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;

	@Getter @Setter @IFieldAnnotation(remark="流程状态")
	private String approvalState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="最后修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//

	@Getter @Setter @IFieldAnnotation(remark="版本名称")
	private String theName;
	
	@IFieldAnnotation(remark="版本号")
	private String theVersion;

	@Getter @Setter @IFieldAnnotation(remark="是否启用")
	private Boolean hasEnable;
	
	@Getter @Setter @IFieldAnnotation(remark="托管标准类型S_EscrowStandardType (枚举选择:1-标准金额2-标准比例)")
	private String theType;
	
	@Getter @Setter @IFieldAnnotation(remark="托管标准")
	private String theContent;//【手工输入，当前最新的政策是：楼幢物价备案均价*40%；老政策（毛坯房）：3500元】
	
	@Getter @Setter @IFieldAnnotation(remark="金额")
	private Double amount;
	
	@Getter @Setter @IFieldAnnotation(remark="比例")
	private Double percentage;
	
	@Getter @Setter @IFieldAnnotation(remark="备选参数1")
	private String extendParameter1;
	
	@Getter @Setter @IFieldAnnotation(remark="备选参数2")
	private String extendParameter2;

	@Getter @Setter @IFieldAnnotation(remark="启用日期")
	private Long beginExpirationDate;
	
	@Getter @Setter @IFieldAnnotation(remark="停用日期")
	private Long endExpirationDate;

	@Override
	public String getSourceType()
	{
		return getClass().getName();
	}

	@Override
	public Long getSourceId()
	{
		return tableId;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	//审批流（比对字段）
	@Override
	public List<String> getPeddingApprovalkey()
	{
		List<String> peddingApprovalkey = new ArrayList<String>();
		peddingApprovalkey.add("theName");
		return peddingApprovalkey;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess()
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean updatePeddingApprovalDataAfterFail()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	@Override
	public Long getLogId() {
		return null;
	}

	@Override
	public void setLogId(Long logId) {

	}

	@Override
	public String getLogData() {
		return null;
	}

	@Override
	public void setTheVersion(String theVersion)
	{
		this.theVersion = theVersion;
	}
	@Override
	public String getTheVersion()
	{
		return this.theVersion;
	}

}
