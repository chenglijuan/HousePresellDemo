package zhishusz.housepresell.controller.form;

import java.util.List;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：支付保证申请
 * Company：ZhiShuSZ
 * create by wang
 * 2018/09/25
 * */
@ToString(callSuper=true)
public class Empj_PaymentGuaranteeForm extends NormalActionForm
{
	
	private static final long serialVersionUID = 3418501671848698839L;

	@Getter @Setter 
	private Long tableId;
	
	private String eCode;//eCode=支付保证单号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”
	@Getter @Setter
	private String applyDate;//申请日期
	@Getter @Setter 
	private Long companyId;//开发企业 ID
	@Getter @Setter 
	private Emmp_CompanyInfo emmp_CompanyInfo;//开发企业
	@Getter @Setter 
	private String companyName;//开发企业 ID
	@Getter @Setter 
	private Empj_ProjectInfo empj_ProjectInfo ;//项目
	@Getter @Setter 
	private Long projectId ;//项目ID
	@Getter @Setter 
	private String projectName;//项目名称
	@Getter @Setter 
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter 
	private Long eCodeFromConstructionId;//施工编号ID
	@Getter @Setter 
	private String guaranteeNo;//支付保证单号
	@Getter @Setter 
	private Long guaranteeCompanyId;//支付保证出具单位("机构信息表")
	@Getter @Setter 
	private String guaranteeCompany;//支付保证出具单位("机构信息表")
	@Getter @Setter 
	private String guaranteeType;//支付保证类型(1-银行支付保证 ,2- 支付保险 ,3- 支付担保 )
	@Getter @Setter 
	private Double alreadyActualAmount;// 项目建设工程已实际支付金额 
	@Getter @Setter
	private Double actualAmount; //项目建设工程待支付承保金额 
	@Getter @Setter 
	private Double guaranteedAmount; //已落实支付保证金额
	@Getter @Setter
	private String remark; //备注
	@Getter @Setter
	private String oprUser;//操作人，关联用户表
	@Getter @Setter
	private Long oprUserId;//操作人ID
	@Getter @Setter 
	private String oprTime;//操作日期 	
	@Getter @Setter 
	private Sm_User auditUset;//审核人，关联用户表
	@Getter @Setter
	private Long auditUsetId;//审核人ID
	@Getter @Setter 
	private Integer theState;
	@Getter @Setter 
	private String busiState;	
	@Getter @Setter 
	private String apply;
	@Getter @Setter 
	private String cancel;
	@Getter @Setter 
	private Sm_User userStart;
	@Getter @Setter 
	private Long userStartId;
	@Getter @Setter 
	private Long createTimeStamp;
	@Getter @Setter 
	private Sm_User userUpdate;
	@Getter @Setter 
	private Long userUpdateId;
	@Getter @Setter 
	private Long lastUpdateTimeStamp;
	@Getter @Setter 
	private Long userStartId1;	
	@Getter @Setter 
	private Sm_User userRecord;
	@Getter @Setter 
	private Long userRecordId;
	@Getter @Setter 
	private Long recordTimeStamp;
	@Getter @Setter 
	private Integer versionNo;//版本号	
	//附件参数
	@Getter @Setter
	private String smAttachmentList;
	
	@Getter @Setter 
	private Long paymentId;
	@Getter @Setter 
	private String empj_PaymentGuarantee;
	
	@Getter @Setter 
	private String empj_PaymentGuaranteeChildList;
	
	@Getter @Setter 
	private String revokeNo;
	
	@Getter @Setter 
	private String paymentLines;//支付保证上限比例
	@Getter @Setter 
	private String paymentProportion;//支付保证封顶额度
	
	
	
	//楼幢ID
	@Getter @Setter
	private Long buildId;
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}
	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
	public Long geteCodeFromConstructionId()
	{
		return eCodeFromConstructionId;
	}
	public void seteCodeFromConstructionId(Long eCodeFromConstructionId)
	{
		this.eCodeFromConstructionId = eCodeFromConstructionId;
	}
	
	
}
