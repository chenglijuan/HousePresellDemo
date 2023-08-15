package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：合作协议
 * */
@ITypeAnnotation(remark="合作协议")
public class Tgxy_CoopAgreement implements Serializable,IApprovable
{
	private static final long serialVersionUID = -1963443889169742821L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
	private String eCode;//手工输入

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
	
	@Getter @Setter @IFieldAnnotation(remark="银行")
	private Emmp_BankInfo bank;
	
	@Getter @Setter @IFieldAnnotation(remark="银行名称-冗余")
	private String theNameOfBank;
	
	@Getter @Setter @IFieldAnnotation(remark="签署日期yyyy-MM-dd")
	private String signDate;
	
	@Getter @Setter @IFieldAnnotation(remark="开户行")
	private Emmp_BankBranch bankOfDeposit;
	
	@Getter @Setter @IFieldAnnotation(remark="开户行名称-冗余")
	private String theNameOfDepositBank;

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}

	@Override
	public String getSourceType() {
		return null;
	}

	@Override
	public Long getSourceId() {
		return null;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		return null;
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
	
	
	
}
