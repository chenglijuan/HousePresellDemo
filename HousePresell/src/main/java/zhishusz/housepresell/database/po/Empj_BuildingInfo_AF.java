package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：申请表-楼幢变更
 * */
@ITypeAnnotation(remark="申请表-楼幢变更")
public class Empj_BuildingInfo_AF implements Serializable,IApprovable
{
	private static final long serialVersionUID = 5947273945174221361L;
    
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
	
	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo developCompany;
	
	@IFieldAnnotation(remark="开发企业编号")
	private String eCodeOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
	
	@IFieldAnnotation(remark="项目编号")
	public String eCodeOfProject;

	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo building;
	
	@IFieldAnnotation(remark="楼幢编号")
	private String eCodeOfBuilding;

	@Getter @Setter @IFieldAnnotation(remark="建筑面积（㎡）")
	private Double buildingArea;
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积")
	private Double escrowArea;
	
	@Getter @Setter @IFieldAnnotation(remark="交付类型")
	private String deliveryType;

	@Getter @Setter @IFieldAnnotation(remark="地上楼层数")
	private Double upfloorNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="地下楼层数")
	private Double downfloorNumber;

	@Getter @Setter @IFieldAnnotation(remark="土地抵押状态")
	private Integer landMortgageState;
	
	@Getter @Setter @IFieldAnnotation(remark="土地抵押权人")
	private String landMortgagor;
	
	@Getter @Setter @IFieldAnnotation(remark="土地抵押金额")
	private Double landMortgageAmount;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;

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

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteCodeOfDevelopCompany() {
		return eCodeOfDevelopCompany;
	}

	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) {
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}

	public String geteCodeOfProject() {
		return eCodeOfProject;
	}

	public void seteCodeOfProject(String eCodeOfProject) {
		this.eCodeOfProject = eCodeOfProject;
	}

	public String geteCodeOfBuilding() {
		return eCodeOfBuilding;
	}

	public void seteCodeOfBuilding(String eCodeOfBuilding) {
		this.eCodeOfBuilding = eCodeOfBuilding;
	}
}
