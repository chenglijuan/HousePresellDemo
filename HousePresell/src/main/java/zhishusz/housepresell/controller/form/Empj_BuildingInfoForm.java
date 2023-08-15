package zhishusz.housepresell.controller.form;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhishusz.housepresell.database.po.*;

/*
 * Form表单：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_BuildingInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = 1720150160823716206L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private Empj_ProjectInfo project;//关联项目
	@Getter @Setter
	private Long projectId;//关联项目-Id
	//
	@Getter @Setter
	private Empj_PaymentGuarantee payment;//关联支付保证
	@Getter @Setter
	private Long paymentId;//关联支付保证-Id
	
	@Getter @Setter
	private String theNameOfProject;//项目名称-冗余
	@Getter @Setter
	private String eCodeOfProject;//项目编号-冗余
	@Getter @Setter
	private Empj_BuildingExtendInfo extendInfo;//扩展信息
	@Getter @Setter
	private Long extendInfoId;//扩展信息-Id
	@Getter @Setter 
	private Integer landMortgageState;//土地抵押状态
	@Getter @Setter 
	private String landMortgagor;//土地抵押权人
	@Getter @Setter
	private Double landMortgageAmount;//土地抵押金额
	@Getter @Setter
	private String theNameFromPresellSystem;//预售项目名称
	@Getter @Setter
	private String eCodeOfProjectFromPresellSystem;//预售系统所属项目编号
	@Getter @Setter
	private String eCodeFromPresellSystem;//预售系统楼幢编号
	@Getter @Setter
	private String theNameFromFinancialAccounting;//财务核算项目名称
	@Getter @Setter
	private String eCodeFromPresellCert;//预售证号
	@Getter @Setter
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter
	private String eCodeFromPublicSecurity;//公安编号
	@Getter @Setter
	private List bankAccountSupervisedList;//使用的监管银行账号，多对多
	@Getter @Setter
	private String eCodeOfProjectPartition;//项目分区编号
	@Getter @Setter
	private String zoneCode;//行政区划
	@Getter @Setter
	private Sm_CityRegionInfo cityRegion;//所属区域
	@Getter @Setter
	private Long cityRegionId;//所属区域-Id
	@Getter @Setter
	private String theNameOfCityRegion;//区域名称
	@Getter @Setter
	private Sm_StreetInfo streetInfo;//所属街道
	@Getter @Setter
	private Long streetInfoId;//所属街道-Id
	@Getter @Setter
	private String theNameOfStreet;//街道名称
	@Getter @Setter
	private String eCodeOfGround;//宗地号
	@Getter @Setter
	private String eCodeOfLand;//幢编号（国土）
	@Getter @Setter
	private String position;//楼幢坐落
	@Getter @Setter
	private String purpose;//房屋用途
	@Getter @Setter
	private String structureProperty;//结构属性
	@Getter @Setter
	private String theType;//楼幢类型
	@Getter @Setter
	private Integer theProperty;//楼幢性质
	@Getter @Setter
	private String decorationType;//装修类型
	@Getter @Setter
	private String combType;//组合形式
	@Getter @Setter
	private Double floorNumer;//总层数
	@Getter @Setter
	private Double upfloorNumber;//地上楼层数
	@Getter @Setter
	private Double downfloorNumber;//地下楼层数
	@Getter @Setter
	private Double heigh;//层高
	@Getter @Setter
	private Integer unitNumber;//单元数
	@Getter @Setter
	private Integer sumFamilyNumber;//总户数
	@Getter @Setter
	private Double buildingArea;//建筑面积（㎡）
	@Getter @Setter
	private Double occupyArea;//占地面积（㎡）
	@Getter @Setter
	private Double shareArea;//分摊面积（㎡）
	@Getter @Setter
	private String beginDate;//开工日期yyyyMMdd
	@Getter @Setter
	private String endDate;//竣工日期yyyyMMdd
	@Getter @Setter
	private String deliveryDate;//交付日期yyyyMMdd
	@Getter @Setter
	private String deliveryType;//交付类型
	@Getter @Setter
	private String warrantyDate;//保修日期yyyyMMdd
	@Getter @Setter
	private String geoCoordinate;//地理坐标
	@Getter @Setter
	private String eCodeOfGis;//GIS图幅编号
	@Getter @Setter
	private String eCodeOfMapping;//测绘编号
	@Getter @Setter
	private String eCodeOfPicture;//图幅号
	@Getter @Setter
	private String buildingFacilities;//楼幢配套
	@Getter @Setter
	private String buildingArround;//楼幢周边
	@Getter @Setter
	private String introduction;//楼幢简介
	@Getter @Setter
	private Emmp_CompanyInfo developCompany;//关联开发企业
	@Getter @Setter
	private Long developCompanyId;//关联开发企业-Id
	@Getter @Setter
	private String eCodeOfDevelopCompany;//开发企业编号
	@Getter @Setter
	private Emmp_CompanyInfo busCompany;//关联物业公司
	@Getter @Setter
	private Long busCompanyId;//关联物业公司-Id
	@Getter @Setter
	private String eCodeOfBusCompany;//物业公司编号
	@Getter @Setter
	private Emmp_CompanyInfo ownerCommittee;//关联业主委员会
	@Getter @Setter
	private Long ownerCommitteeId;//关联业主委员会-Id
	@Getter @Setter
	private String eCodeOfOwnerCommittee;//业主委员会编号
	@Getter @Setter
	private Emmp_CompanyInfo mappingUnit;//关联测绘单位
	@Getter @Setter
	private Long mappingUnitId;//关联测绘单位-Id
	@Getter @Setter
	private String eCodeOfMappingUnit;//测绘单位编号
	@Getter @Setter
	private Emmp_CompanyInfo saleAgencyUnit;//关联销售代理单位
	@Getter @Setter
	private Long saleAgencyUnitId;//关联销售代理单位-Id
	@Getter @Setter
	private String eCodeOfS;//销售代理单位编号
	@Getter @Setter
	private Emmp_CompanyInfo consUnit;//施工建设单位
	@Getter @Setter
	private Long consUnitId;//施工建设单位-Id
	@Getter @Setter
	private String eCodeOfConsUnit;//施工建设单位编号
	@Getter @Setter
	private Emmp_CompanyInfo controlUnit;//监理单位
	@Getter @Setter
	private Long controlUnitId;//监理单位-Id
	@Getter @Setter
	private String eCodeOfControlUnit;//监理单位编号
	@Getter @Setter
	private Double escrowArea;//托管面积
	@Getter @Setter
	private String escrowStandard;//托管标准
	@Getter @Setter
	private Long tgpj_EscrowStandardVerMngId;//托管标准ID
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private String buildingextraid;
	@Getter @Setter
	private String externalId;//预售系统关联主键
	@Getter @Setter
	private Double paymentLines;//支付保证上限比例
	@Getter @Setter
	private String escrowState;//托管状态
	@Getter @Setter
	private String startEscrow;//已托管
	@Getter @Setter
	private String endEscrow;//托管终止
	@Getter @Setter
	private String nowLimitRatio;//目前受限额度比例
	@Getter @Setter
	private String exceptEscrowState;//不包含托管未申请
	@Getter @Setter
	private String gjjTableId;//楼幢ID（公积金）
	@Getter @Setter
	private String gjjState;//审批状态
	@Getter @Setter
	private String approveMonth;//审批年月
	
	
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


	public String getGjjTableId() {
		return gjjTableId;
	}
	public void setGjjTableId(String gjjTableId) {
		this.gjjTableId = gjjTableId;
	}
	public String getGjjState() {
		return gjjState;
	}
	public void setGjjState(String gjjState) {
		this.gjjState = gjjState;
	}
}
