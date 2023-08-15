package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：项目信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_ProjectInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = 3350722725259844111L;
	
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
	private Emmp_CompanyInfo developCompany;//关联开发企业
	@Getter @Setter
	private Long developCompanyId;//关联开发企业-Id
	@Getter @Setter
	private String eCodeOfDevelopCompany;//开发企业编号
	@Getter @Setter
	private String theType;//项目类型
	@Getter @Setter
	private String zoneCode;//行政区代码
	@Getter @Setter
	private Sm_CityRegionInfo cityRegion;//所属区域
	@Getter @Setter
	private Long cityRegionId;//所属区域-Id
	@Getter @Setter
	private Sm_StreetInfo street;//所属街道
	@Getter @Setter
	private Long streetId;//所属街道-Id
	@Getter @Setter
	private String address;//详细地址
	@Getter @Setter
	private String doorNumber;//门牌号
	@Getter @Setter
	private String doorNumberAnnex;//附门牌号
	@Getter @Setter
	private String introduction;//简介
	@Getter @Setter
	private Double longitude;//经度
	@Getter @Setter
	private Double latitude;//纬度
	@Getter @Setter
	private String propertyType;//物业类型 S_PropertyType
	@Getter @Setter
	private String theName;//项目名称
	@Getter @Setter
	private String legalName;//项目法定名称
	@Getter @Setter
	private String buildYear;//建造年份
	@Getter @Setter
	private Boolean isPartition;//是否分区
	@Getter @Setter
	private Integer theProperty;//项目性质
	@Getter @Setter
	private String contactPerson;//项目联系人
	@Getter @Setter
	private String contactPhone;//项目联系人电话
	@Getter @Setter
	private String projectLeader;//项目负责人
	@Getter @Setter
	private String leaderPhone;//项目负责人电话
	@Getter @Setter
	private Double landArea;//项目总占地面积（㎡）
	@Getter @Setter
	private Integer obtainMethod;//项目用地取得方式
	@Getter @Setter
	private Double investment;//计划总投资（万元）
	@Getter @Setter
	private Double landInvest;//土地投资金额
	@Getter @Setter
	private Double coverArea;//计划总建筑面积（㎡）
	@Getter @Setter
	private Double houseArea;//计划住宅总面积（㎡）
	@Getter @Setter
	private Double siteArea;//立项面积（㎡）
	@Getter @Setter
	private Double planArea;//规划面积（㎡）
	@Getter @Setter
	private Double agArea;//非住宅地上面积（㎡）
	@Getter @Setter
	private Double ugArea;//非住宅地下面积（㎡）
	@Getter @Setter
	private Double greenRatio;//绿化率
	@Getter @Setter
	private Double capacity;//容积率
	@Getter @Setter
	private Double parkRatio;//车位配比率
	@Getter @Setter
	private Integer unitCount;//住宅总套数
	@Getter @Setter
	private Integer buildingCount;//总幢数
	@Getter @Setter
	private Long payDate;//交付日期
	@Getter @Setter
	private String planStartDate;//计划开工日期yyyyMMdd
	@Getter @Setter
	private String planEndDate;//计划竣工日期yyyyMMdd
	@Getter @Setter
	private String developDate;//开发日期yyyyMMdd
	@Getter @Setter
	private Emmp_CompanyInfo designCompany;//设计单位
	@Getter @Setter
	private Long designCompanyId;//设计单位-Id
	@Getter @Setter
	private String eCodeOfDesignCompany;//设计单位编号
	@Getter @Setter
	private String remark;//备注
	@Getter @Setter
	private Integer developProgress;//项目开发进度
	@Getter @Setter
	private String eastAddress;//四界-东界-地址
	@Getter @Setter
	private Double eastLongitude;//四界-东界-经度
	@Getter @Setter
	private Double eastLatitude;//四界-东界-纬度
	@Getter @Setter
	private String westAddress;//四界-西界-地址
	@Getter @Setter
	private Double westLongitude;//四界-西界-经度
	@Getter @Setter
	private Double westLatitude;//四界-西界-纬度
	@Getter @Setter
	private String southAddress;//四界-南界-地址
	@Getter @Setter
	private Double southLongitude;//四界-南界-经度
	@Getter @Setter
	private Double southLatitude;//四界-南界-纬度
	@Getter @Setter
	private String northAddress;//四界-北界-地址
	@Getter @Setter
	private Double northLongitude;//四界-北界-经度
	@Getter @Setter
	private Double northLatitude;//四界-北界-纬度
	@Getter @Setter
	private String projectextraid;
	@Getter @Setter
	private String externalId;//外来数据关联字段
	@Getter @Setter
	private String getDetailType; //获取详情类型（1、详情信息带审批流，2、详情信息不带审批流）
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
	public String geteCodeOfDevelopCompany()
	{
		return eCodeOfDevelopCompany;
	}
	public void seteCodeOfDevelopCompany(String eCodeOfDevelopCompany)
	{
		this.eCodeOfDevelopCompany = eCodeOfDevelopCompany;
	}
	public String geteCodeOfDesignCompany()
	{
		return eCodeOfDesignCompany;
	}
	public void seteCodeOfDesignCompany(String eCodeOfDesignCompany)
	{
		this.eCodeOfDesignCompany = eCodeOfDesignCompany;
	}
}
