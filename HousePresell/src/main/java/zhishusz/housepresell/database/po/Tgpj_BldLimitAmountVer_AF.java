package zhishusz.housepresell.database.po;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.internal.IVersion;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：版本管理-受限节点设置
 * */
@ITypeAnnotation(remark="版本管理-受限节点设置")
public class Tgpj_BldLimitAmountVer_AF implements Serializable,IVersion,IApprovable
{
	private static final long serialVersionUID = -2257950935412244106L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;

	@Getter @Setter @IFieldAnnotation(remark="流程状态")
	private String approvalState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
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
	
	@Getter @Setter @IFieldAnnotation(remark="版本号")
	private String theVerion;
	
	@Getter @Setter @IFieldAnnotation(remark="交付类型")
	private String theType;//【交付类型：毛坯房、成品房】
	
	@Getter @Setter @IFieldAnnotation(remark="受限额度数据-JSON格式")
	private String limitedAmountInfoJSON;
	
	@Getter @Setter @IFieldAnnotation(remark="启用日期")
	private Long beginExpirationDate;
	
	@Getter @Setter @IFieldAnnotation(remark="停用日期")
	private Long endExpirationDate;

	@Getter @Setter @IFieldAnnotation(remark="是否启用")
	private Integer isUsing;//0：启用，1：不启用

	@Override
	public String getTheVersion()
	{
		return theVerion;
	}
	@Override
	public void setTheVersion(String theVerion)
	{
		this.theVerion = theVerion;
	}

	@Override
	public String getSourceType() {
		return getClass().getName();
	}

	@Override
	public Long getSourceId() {
		return tableId;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		List<String> peddingApprovalkey = new ArrayList<String>();

		peddingApprovalkey.add("theName");
		peddingApprovalkey.add("theVerion");
		peddingApprovalkey.add("theType");
		peddingApprovalkey.add("beginExpirationDate");
		peddingApprovalkey.add("isUsing");
		peddingApprovalkey.add("generalAttachmentList");

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

	public List getNeedFieldList(){
		return Arrays.asList("theName");
	}

	@Override
	public String toString() {
		return "Tgpj_BldLimitAmountVer_AF{" +
				"tableId=" + tableId +
				", theState=" + theState +
				", busiState='" + busiState + '\'' +
				", eCode='" + eCode + '\'' +
				", userStart=" + userStart +
				", createTimeStamp=" + createTimeStamp +
				", userUpdate=" + userUpdate +
				", lastUpdateTimeStamp=" + lastUpdateTimeStamp +
				", userRecord=" + userRecord +
				", recordTimeStamp=" + recordTimeStamp +
				", theName='" + theName + '\'' +
				", theVerion='" + theVerion + '\'' +
				", theType='" + theType + '\'' +
				", limitedAmountInfoJSON='" + limitedAmountInfoJSON + '\'' +
				", beginExpirationDate=" + beginExpirationDate +
				", endExpirationDate=" + endExpirationDate +
				", isUsing=" + isUsing +
				'}';
	}
}
