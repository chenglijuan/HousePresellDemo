package zhishusz.housepresell.controller.form;

import java.util.Date;
import java.util.List;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentGuarantee;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;
import zhishusz.housepresell.util.IFieldAnnotation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_NodePredictionForm extends NormalActionForm
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
	
	
	@Getter @Setter @IFieldAnnotation(remark="关联开发企业")
    private Emmp_CompanyInfo developCompany;
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业Id")
    private Long developCompanyId;
    
    @Getter @Setter @IFieldAnnotation(remark="开发企业名称")
    private String companyName;

    @Getter @Setter @IFieldAnnotation(remark="关联项目")
    public Empj_ProjectInfo project;
    
    @Getter @Setter @IFieldAnnotation(remark="项目Id")
    public Long projectId;
    
    @Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
    public String projectName;
    
    @Getter @Setter @IFieldAnnotation(remark="关联楼幢")
    private Empj_BuildingInfo building;
    
    @Getter @Setter @IFieldAnnotation(remark="楼幢Id")
    private Long buildingId;

    @IFieldAnnotation(remark="施工编号")
    private String eCodeFromConstruction;
    
    @Getter @Setter @IFieldAnnotation(remark="变更形象进度")
    private Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress;
    
    @Getter @Setter @IFieldAnnotation(remark="形象进度Id")
    private Long expectFigureProgressId;
    
    @Getter @Setter @IFieldAnnotation(remark="变更受限比例")
    private Double expectLimitedRatio;
    
    @Getter @Setter @IFieldAnnotation(remark="变更节点名称")
    private String expectLimitedName;
    
    @Getter @Setter @IFieldAnnotation(remark="预测完成日期")
    private Date completeDate;
    
    @Getter @Setter
    private String theName;
    
    
}
