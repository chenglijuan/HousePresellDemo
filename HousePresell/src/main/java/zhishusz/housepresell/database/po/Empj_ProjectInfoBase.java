package zhishusz.housepresell.database.po;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.database.po.internal.ILogable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * 对象名称：项目信息
 * */
@ITypeAnnotation(remark="项目信息")
public class Empj_ProjectInfoBase implements Serializable, ILogable, IApprovable
{
	private static final long serialVersionUID = 7090227718192699021L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;  //暂定：未备案，已备案

	@Getter @Setter @IFieldAnnotation(remark="流程状态")
	private String approvalState;

	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”
	
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

	@Getter @Setter @IFieldAnnotation(remark="项目类型")
	private String theType;

	@Getter @Setter @IFieldAnnotation(remark="行政区代码")
	private String zoneCode;
	
	@Getter @Setter @IFieldAnnotation(remark="所属区域")
	private Sm_CityRegionInfo cityRegion;

	@Getter @Setter @IFieldAnnotation(remark="所属街道")
	private Sm_StreetInfo street;

	@Getter @Setter @IFieldAnnotation(remark="详细地址")
	private String address;

	@Getter @Setter @IFieldAnnotation(remark="门牌号")
	private String doorNumber;
	
	@Getter @Setter @IFieldAnnotation(remark="附门牌号")
	private String doorNumberAnnex;
	
	@Getter @Setter @IFieldAnnotation(remark="简介")
	private String introduction;
	
	@Getter @Setter @IFieldAnnotation(remark="经度")
	private Double longitude;
	
	@Getter @Setter @IFieldAnnotation(remark="纬度")
	private Double latitude;
	
	@Getter @Setter @IFieldAnnotation(remark="物业类型 S_PropertyType")
	private String propertyType;

	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="项目法定名称")
	private String legalName;
	
	@Getter @Setter @IFieldAnnotation(remark="建造年份")
	private String buildYear;
	
	@Getter @Setter @IFieldAnnotation(remark="是否分区")
	private Boolean isPartition;//项目地块分成N个区域承建
	
	@Getter @Setter @IFieldAnnotation(remark="项目性质")
	private Integer theProperty;
	
	@Getter @Setter @IFieldAnnotation(remark="联系人")
	private String contactPerson;
	
	@Getter @Setter @IFieldAnnotation(remark="联系人电话")
	private String contactPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="负责人")
	private String projectLeader;
	
	@Getter @Setter @IFieldAnnotation(remark="负责人电话")
	private String leaderPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="项目总占地面积（㎡）")
	private Double landArea;
	
	@Getter @Setter @IFieldAnnotation(remark="项目用地取得方式")
	private Integer obtainMethod;
	
	@Getter @Setter @IFieldAnnotation(remark="计划总投资（万元）")
	private Double investment;
	
	@Getter @Setter @IFieldAnnotation(remark="土地投资金额")
	private Double landInvest;
	
	@Getter @Setter @IFieldAnnotation(remark="计划总建筑面积（㎡）")
	private Double coverArea;
	
	@Getter @Setter @IFieldAnnotation(remark="计划住宅总面积（㎡）")
	private Double houseArea;
	
	@Getter @Setter @IFieldAnnotation(remark="立项面积（㎡）")
	private Double siteArea;
	
	@Getter @Setter @IFieldAnnotation(remark="规划面积（㎡）")
	private Double planArea;
	
	@Getter @Setter @IFieldAnnotation(remark="非住宅地上面积（㎡）")
	private Double agArea;
	
	@Getter @Setter @IFieldAnnotation(remark="非住宅地下面积（㎡）")
	private Double ugArea;
	
	@Getter @Setter @IFieldAnnotation(remark="绿化率")
	private Double greenRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="容积率")
	private Double capacity;
	
	@Getter @Setter @IFieldAnnotation(remark="车位配比率")
	private Double parkRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="住宅总套数")
	private Integer unitCount;
	
	@Getter @Setter @IFieldAnnotation(remark="总幢数")
	private Integer buildingCount;
	
	@Getter @Setter @IFieldAnnotation(remark="交付日期")
	private Long payDate;
	
	@Getter @Setter @IFieldAnnotation(remark="计划开工日期yyyyMMdd")
	private String planStartDate;
	
	@Getter @Setter @IFieldAnnotation(remark="计划竣工日期yyyyMMdd")
	private String planEndDate;
	
	@Getter @Setter @IFieldAnnotation(remark="开发日期yyyyMMdd")
	private String developDate;
	
	@Getter @Setter @IFieldAnnotation(remark="设计单位")
	private Emmp_CompanyInfo designCompany;
	
	@IFieldAnnotation(remark="设计单位编号")
	private String eCodeOfDesignCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="项目开发进度")
	private Integer developProgress;

	@Getter @Setter @IFieldAnnotation(remark="四界-东界-地址")
	private String eastAddress;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-东界-经度")
	private Double eastLongitude;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-东界-纬度")
	private Double eastLatitude;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-西界-地址")
	private String westAddress;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-西界-经度")
	private Double westLongitude;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-西界-纬度")
	private Double westLatitude;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-南界-地址")
	private String southAddress;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-南界-经度")
	private Double southLongitude;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-南界-纬度")
	private Double southLatitude;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-北界-地址")
	private String northAddress;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-北界-经度")
	private Double northLongitude;
	
	@Getter @Setter @IFieldAnnotation(remark="四界-北界-纬度")
	private Double northLatitude;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联字段")
	private String externalCode;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联主键")
	private String externalId;
	
	@Getter @Setter @IFieldAnnotation(remark="数据来源说明")
	private String resourceNote;

	@Override
	public String toString() {
		return "{\"theName\":\"" + theName + "\"}";
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

	public String geteCodeOfDesignCompany() {
		return eCodeOfDesignCompany;
	}

	public void seteCodeOfDesignCompany(String eCodeOfDesignCompany) {
		this.eCodeOfDesignCompany = eCodeOfDesignCompany;
	}

	//日志查看（比对字段）
	public List getNeedFieldList()
	{
		return Arrays.asList("theName" ,"cityRegion/theName", "street/theName",
				"address", "introduction", "longitude", "latitude", "projectLeader" ,"leaderPhone" ,"remark");
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

	//审批流（比对字段）
	@Override
	public List<String> getPeddingApprovalkey()
	{
		List<String> peddingApprovalkey = new ArrayList<String>();

		peddingApprovalkey.add("theName");
		peddingApprovalkey.add("cityRegionId");
		peddingApprovalkey.add("streetId");
		peddingApprovalkey.add("introduction");
		peddingApprovalkey.add("address");
		peddingApprovalkey.add("longitude");
		peddingApprovalkey.add("latitude");
		peddingApprovalkey.add("projectLeader");
		peddingApprovalkey.add("leaderPhone");
		peddingApprovalkey.add("remark");
		peddingApprovalkey.add("generalAttachmentList");

		return peddingApprovalkey;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess() {
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterFail() {
		return null;
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
}
