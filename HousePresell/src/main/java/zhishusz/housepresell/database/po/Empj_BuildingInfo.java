package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/*
 * 对象名称：楼幢-基础信息
 * */
@ITypeAnnotation(remark="楼幢-基础信息")
public class Empj_BuildingInfo implements Serializable,ILogable,IApprovable
{
	private static final long serialVersionUID = 7637778529676632875L;
	
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
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="关联支付保证")
	public Empj_PaymentGuarantee payment;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String theNameOfProject;
	
	@IFieldAnnotation(remark="项目编号-冗余")
	private String eCodeOfProject;

	@Getter @Setter  @IFieldAnnotation(remark="关联楼幢账户")
	private Tgpj_BuildingAccount buildingAccount;

	@Getter @Setter @IFieldAnnotation(remark="扩展信息")
	private Empj_BuildingExtendInfo extendInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="预售项目名称")
	private String theNameFromPresellSystem;
	
	@IFieldAnnotation(remark="预售系统所属项目编号",columnName="eCodeOfPjFromPS")
	private String eCodeOfProjectFromPresellSystem;
	
	@IFieldAnnotation(remark="预售系统楼幢编号")
	private String eCodeFromPresellSystem;
	
	@Getter @Setter @IFieldAnnotation(remark="财务核算项目名称",columnName="theNameFromFA")
	private String theNameFromFinancialAccounting;
	
	@IFieldAnnotation(remark="预售证号")
	private String eCodeFromPresellCert;
	
	@IFieldAnnotation(remark="施工编号")
	private String eCodeFromConstruction;
	
	@IFieldAnnotation(remark="公安编号")
	private String eCodeFromPublicSecurity;
	
	@IFieldAnnotation(remark="项目分区编号")
	private String eCodeOfProjectPartition;
	
	@Getter @Setter @IFieldAnnotation(remark="行政区划")
	private String zoneCode;
	
	@Getter @Setter @IFieldAnnotation(remark="所属区域")
	private Sm_CityRegionInfo cityRegion;
	
	@Getter @Setter @IFieldAnnotation(remark="区域名称")
	private String theNameOfCityRegion;
	
	@Getter @Setter @IFieldAnnotation(remark="所属街道")
	private Sm_StreetInfo streetInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="街道名称")
	private String theNameOfStreet;
	
	@IFieldAnnotation(remark="宗地号")
	private String eCodeOfGround;
	
	@IFieldAnnotation(remark="幢编号（国土）")
	private String eCodeOfLand;//界面上不显示
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢坐落")
	private String position;
	
	@Getter @Setter @IFieldAnnotation(remark="房屋用途")
	private String purpose;
	
	@Getter @Setter @IFieldAnnotation(remark="结构属性")
	private String structureProperty;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢类型")
	private String theType;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢性质")
	private Integer theProperty;
	
	@Getter @Setter @IFieldAnnotation(remark="装修类型")
	private String decorationType;
	
	@Getter @Setter @IFieldAnnotation(remark="组合形式")
	private String combType;
	
	@Getter @Setter @IFieldAnnotation(remark="总层数")
	private Double floorNumer;//有半层的
	
	@Getter @Setter @IFieldAnnotation(remark="地上楼层数")
	private Double upfloorNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="地下楼层数")
	private Double downfloorNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="层高")
	private Double heigh;
	
	@Getter @Setter @IFieldAnnotation(remark="单元数")
	private Integer unitNumber;//0表示没有单元
	
	@Getter @Setter @IFieldAnnotation(remark="总户数")
	private Integer sumFamilyNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="建筑面积")
	private Double buildingArea;
	
	@Getter @Setter @IFieldAnnotation(remark="占地面积（㎡）")
	private Double occupyArea;
	
	@Getter @Setter @IFieldAnnotation(remark="分摊面积（㎡）")
	private Double shareArea;
	
	@Getter @Setter @IFieldAnnotation(remark="开工日期yyyyMMdd")
	private String beginDate;
	
	@Getter @Setter @IFieldAnnotation(remark="竣工日期yyyyMMdd")
	private String endDate;
	
	@Getter @Setter @IFieldAnnotation(remark="交付日期yyyyMMdd")
	private String deliveryDate;
	
	@Getter @Setter @IFieldAnnotation(remark="交付类型（1-毛坯房 2-成品房）")
	private String deliveryType;
	
	@Getter @Setter @IFieldAnnotation(remark="保修日期yyyyMMdd")
	private String warrantyDate;

	@Getter @Setter @IFieldAnnotation(remark="地理坐标")
	private String geoCoordinate;
	
	@IFieldAnnotation(remark="GIS图幅编号")
	private String eCodeOfGis;
	
	@IFieldAnnotation(remark="测绘编号")
	private String eCodeOfMapping;
	
	@IFieldAnnotation(remark="图幅号")
	private String eCodeOfPicture;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢配套")
	private String buildingFacilities;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢周边")
	private String buildingArround;
	
	@Getter @Setter @IFieldAnnotation(remark="楼幢简介")
	private String introduction;

	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
	private Emmp_CompanyInfo developCompany;

	@IFieldAnnotation(remark="开发企业编号 - 冗余")
	private String eCodeOfDevelopCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联物业公司")
	private Emmp_CompanyInfo busCompany;
	
	@IFieldAnnotation(remark="物业公司编号")
	private String eCodeOfBusCompany;

	@Getter @Setter @IFieldAnnotation(remark="关联业主委员会")
	private Emmp_CompanyInfo ownerCommittee;

	@IFieldAnnotation(remark="业主委员会编号")
	private String eCodeOfOwnerCommittee;

	@Getter @Setter @IFieldAnnotation(remark="关联测绘单位")
	private Emmp_CompanyInfo mappingUnit;
	
	@IFieldAnnotation(remark="测绘单位编号")
	private String eCodeOfMappingUnit;

	@Getter @Setter @IFieldAnnotation(remark="关联销售代理单位")
	private Emmp_CompanyInfo saleAgencyUnit;
	
	@IFieldAnnotation(remark="销售代理单位编号")
	private String eCodeOfS;

	@Getter @Setter @IFieldAnnotation(remark="施工建设单位")
	private Emmp_CompanyInfo consUnit;
	
	@IFieldAnnotation(remark="施工建设单位编号")
	private String eCodeOfConsUnit;

	@Getter @Setter @IFieldAnnotation(remark="监理单位")
	private Emmp_CompanyInfo controlUnit;
	
	@IFieldAnnotation(remark="监理单位编号")
	private String eCodeOfControlUnit;
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积")
	private Double escrowArea;
	
	@Getter @Setter @IFieldAnnotation(remark="托管标准")
	private String escrowStandard;
	
	@Getter @Setter @IFieldAnnotation(remark="托管标准对象")
	private Tgpj_EscrowStandardVerMng escrowStandardVerMng;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联字段")
	private String externalCode;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联主键")
	private String externalId;
	
	@Getter @Setter @IFieldAnnotation(remark="数据来源说明")
	private String resourceNote;
	
	@Getter @Setter @IFieldAnnotation(remark="托管模式  0-贷款、1-全额")
	private String escrowPattern;
	
	@Getter @Setter @IFieldAnnotation(remark="托管方式 0-按幢、1-按户")
	private String escrowMode;
	
	@Getter @Setter @IFieldAnnotation(remark="是否预售楼幢")
	private String isAdvanceSale;

	@Getter @Setter @IFieldAnnotation(remark="楼幢ID（公积金）")
	private String gjjTableId;

	@Getter @Setter @IFieldAnnotation(remark="审批状态")
	private String gjjState;

	@Getter @Setter @IFieldAnnotation(remark="支付状态")
	private String payState;

	@Getter @Setter @IFieldAnnotation(remark="审批年月")
	private String approveMonth;

	@Override
	public String toString() {
		return "{\"eCode\":\"" + eCode + "\",\"project\":\"" + project + "\",\"theNameFromPresellSystem\":\""
				+ theNameFromPresellSystem + "\",\"eCodeFromPublicSecurity\":\""
				+ eCodeFromPublicSecurity + "\",\"eCodeFromConstruction\":\"" + eCodeFromConstruction
				+ "\",\"buildingArea\":\"" + buildingArea + "\",\"developCompany\":\"" + developCompany
				+ "\",\"escrowArea\":\"" + escrowArea + "\"}";
	}

	public String geteCode()
	{
		return eCode;
	}

	public void seteCode(String eCode) 
	{
		this.eCode = eCode;
	}

	public String geteCodeOfProject() 
	{
		return eCodeOfProject;
	}

	public void seteCodeOfProject(String eCodeOfProject) 
	{
		this.eCodeOfProject = eCodeOfProject;
	}

	public String geteCodeOfProjectFromPresellSystem() 
	{
		return eCodeOfProjectFromPresellSystem;
	}

	public void seteCodeOfProjectFromPresellSystem(String eCodeOfProjectFromPresellSystem) 
	{
		this.eCodeOfProjectFromPresellSystem = eCodeOfProjectFromPresellSystem;
	}

	public String geteCodeFromPresellSystem() 
	{
		return eCodeFromPresellSystem;
	}

	public void seteCodeFromPresellSystem(String eCodeFromPresellSystem) 
	{
		this.eCodeFromPresellSystem = eCodeFromPresellSystem;
	}

	public String geteCodeFromPresellCert() 
	{
		return eCodeFromPresellCert;
	}

	public void seteCodeFromPresellCert(String eCodeFromPresellCert) 
	{
		this.eCodeFromPresellCert = eCodeFromPresellCert;
	}

	public String geteCodeFromConstruction() 
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction) 
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}

	public String geteCodeFromPublicSecurity() 
	{
		return eCodeFromPublicSecurity;
	}

	public void seteCodeFromPublicSecurity(String eCodeFromPublicSecurity) 
	{
		this.eCodeFromPublicSecurity = eCodeFromPublicSecurity;
	}

	public String geteCodeOfProjectPartition() 
	{
		return eCodeOfProjectPartition;
	}

	public void seteCodeOfProjectPartition(String eCodeOfProjectPartition) 
	{
		this.eCodeOfProjectPartition = eCodeOfProjectPartition;
	}

	public String geteCodeOfGround() 
	{
		return eCodeOfGround;
	}

	public void seteCodeOfGround(String eCodeOfGround) 
	{
		this.eCodeOfGround = eCodeOfGround;
	}

	public String geteCodeOfLand() 
	{
		return eCodeOfLand;
	}

	public void seteCodeOfLand(String eCodeOfLand) 
	{
		this.eCodeOfLand = eCodeOfLand;
	}

	public String geteCodeOfGis() 
	{
		return eCodeOfGis;
	}

	public void seteCodeOfGis(String eCodeOfGis) 
	{
		this.eCodeOfGis = eCodeOfGis;
	}

	public String geteCodeOfMapping() 
	{
		return eCodeOfMapping;
	}

	public void seteCodeOfMapping(String eCodeOfMapping) 
	{
		this.eCodeOfMapping = eCodeOfMapping;
	}

	public String geteCodeOfPicture() 
	{
		return eCodeOfPicture;
	}

	public void seteCodeOfPicture(String eCodeOfPicture) 
	{
		this.eCodeOfPicture = eCodeOfPicture;
	}

	public String geteCodeOfDevelopCompany() 
	{
		return eCodeOfDevelopCompany;
	}

	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany) 
	{
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}

	public String geteCodeOfBusCompany() 
	{
		return eCodeOfBusCompany;
	}

	public void seteCodeOfBusCompany(String eCodeOfBusCompany) 
	{
		this.eCodeOfBusCompany = eCodeOfBusCompany;
	}

	public String geteCodeOfOwnerCommittee() 
	{
		return eCodeOfOwnerCommittee;
	}

	public void seteCodeOfOwnerCommittee(String eCodeOfOwnerCommittee) 
	{
		this.eCodeOfOwnerCommittee = eCodeOfOwnerCommittee;
	}

	public String geteCodeOfMappingUnit() 
	{
		return eCodeOfMappingUnit;
	}

	public void seteCodeOfMappingUnit(String eCodeOfMappingUnit) 
	{
		this.eCodeOfMappingUnit = eCodeOfMappingUnit;
	}

	public String geteCodeOfS() 
	{
		return eCodeOfS;
	}

	public void seteCodeOfS(String eCodeOfS) 
	{
		this.eCodeOfS = eCodeOfS;
	}

	public String geteCodeOfConsUnit() 
	{
		return eCodeOfConsUnit;
	}

	public void seteCodeOfConsUnit(String eCodeOfConsUnit) 
	{
		this.eCodeOfConsUnit = eCodeOfConsUnit;
	}

	public String geteCodeOfControlUnit() 
	{
		return eCodeOfControlUnit;
	}

	public void seteCodeOfControlUnit(String eCodeOfControlUnit) 
	{
		this.eCodeOfControlUnit = eCodeOfControlUnit;
	}

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

	@Override
	public List<String> getPeddingApprovalkey() 
	{
		List<String> peddingApprovalkey = new ArrayList<String>();
		
		peddingApprovalkey.add("escrowArea");
		peddingApprovalkey.add("upfloorNumber");
		peddingApprovalkey.add("downfloorNumber");
		peddingApprovalkey.add("buildingArea");
		peddingApprovalkey.add("deliveryType");
		peddingApprovalkey.add("remark");
		
		peddingApprovalkey.add("landMortgageState");
		peddingApprovalkey.add("landMortgagor");
		peddingApprovalkey.add("landMortgageAmount");
		
		peddingApprovalkey.add("generalAttachmentList");
		
		return peddingApprovalkey;
	}

	@SuppressWarnings("rawtypes")
	public List getNeedFieldList(){
		return Arrays.asList(
				"escrowArea", 
				"buildingArea",
				"deliveryType",
				"upfloorNumber",
				"downfloorNumber",
				"extendInfo/landMortgagor",
				"extendInfo/landMortgageAmount",
				"extendInfo/landMortgageState",
				"remark"
				);
	}
	
	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterFail() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getLogId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogId(Long logId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLogData() {
		// TODO Auto-generated method stub
		return null;
	}
}
