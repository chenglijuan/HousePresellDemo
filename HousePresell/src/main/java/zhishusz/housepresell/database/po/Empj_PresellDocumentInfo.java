package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：预售证信息
 */
@ITypeAnnotation(remark="预售证信息")
public class Empj_PresellDocumentInfo implements Serializable,IApprovable
{
	private static final long serialVersionUID = 3491829403555629027L;
    
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;//许可证状态
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="项目地址")
	public String addressOfProject;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Set<Empj_BuildingInfo> buildingInfoSet;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联字段")
	private String externalCode;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联主键")
	private String externalId;
	
	@Getter @Setter @IFieldAnnotation(remark="数据来源说明")
	private String resourceNote;
	
	@Getter @Setter @IFieldAnnotation(remark="预售范围")
	private String saleRange;
	
	@Getter @Setter @IFieldAnnotation(remark="预售许可证状态")
	private String saleState;
	
	@Getter @Setter @IFieldAnnotation(remark="托管状态")
	private String escorwState;
	
	@Getter @Setter @IFieldAnnotation(remark="发证时间")
	private String certificationDate;
	
	@Getter @Setter @IFieldAnnotation(remark="预售总户数")
	private String saleCounts;
	
	@Getter @Setter @IFieldAnnotation(remark="预售总面积")
	private String saleAreaCounts;

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

	@Override
	public String getSourceType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getSourceId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey()
	{
		// TODO Auto-generated method stub
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
