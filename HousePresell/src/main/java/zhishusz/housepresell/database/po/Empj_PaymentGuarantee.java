package zhishusz.housepresell.database.po;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付保证申请详情
 * @author wang
 *create by 2018/09/21
 */
@ITypeAnnotation(remark="支付保证申请详情")
public class Empj_PaymentGuarantee implements Serializable,IApprovable
{

	private static final long serialVersionUID = 1L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	@Getter @Setter @IFieldAnnotation(remark="申请日期")
	private String applyDate;//申请日期
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业 ")
	private String companyName;//开发企业 
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//项目名称
	
	@Getter @Setter @IFieldAnnotation(remark="关联开发企业 ")
	private Emmp_CompanyInfo company;//开发企业 
	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	private Empj_ProjectInfo project;//项目名称
	
	@IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;//施工编号
	
	@Getter @Setter @IFieldAnnotation(remark="支付保证单号")
	private String guaranteeNo;//支付保证单号
	
	@Getter @Setter @IFieldAnnotation(remark="支付保证撤销单号")
	private String revokeNo;//支付保证撤销单号
	
	@Getter @Setter @IFieldAnnotation(remark="支付保证出具单位")	
	private String guaranteeCompany;//支付保证出具单位("机构信息表")
	
	@Getter @Setter @IFieldAnnotation(remark="//支付保证类型 (1-银行支付保证 ,2- 支付保险 ,3- 支付担保 )")
	private String guaranteeType;//支付保证类型(1-银行支付保证 ,2- 支付保险 ,3- 支付担保 )
	
	@Getter @Setter @IFieldAnnotation(remark="项目建设工程已实际支付金额 ")
	private Double alreadyActualAmount;// 项目建设工程已实际支付金额 
	
	@Getter @Setter @IFieldAnnotation(remark="项目建设工程待支付承保金额 ")
	private Double actualAmount; //项目建设工程待支付承保金额 
	
	@Getter @Setter @IFieldAnnotation(remark="已落实支付保证金额")
	private Double guaranteedAmount; //已落实支付保证金额
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark; //备注
	
	@Getter @Setter @IFieldAnnotation(remark="支付保证撤销备注")
	private String remark2; //备注
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="版本号")
	private Integer versionNo;//版本号
	
	@Getter @Setter @IFieldAnnotation(remark="流程状态 待提交/审核中/已完结")
	private String approvalState;

	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeFromConstruction() {
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction) {
		this.eCodeFromConstruction = eCodeFromConstruction;
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
		List<String>  peddingApprovalkey = new ArrayList<>();
		peddingApprovalkey.add("busiState");
		peddingApprovalkey.add("theNameOfProject");
		//TODO  存放需要审批的字段
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

}
