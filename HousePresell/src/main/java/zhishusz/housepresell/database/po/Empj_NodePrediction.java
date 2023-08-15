package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/*
 * 对象名称：楼幢节点预测
 * */
@ITypeAnnotation(remark="节点预测表")
public class Empj_NodePrediction implements Serializable
{
    
    private static final long serialVersionUID = 5193235952139025003L;

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
	private String eCode;

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
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业名称")
	private String companyName;

	@Getter @Setter @IFieldAnnotation(remark="关联项目")
	public Empj_ProjectInfo project;
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称-冗余")
	public String projectName;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
    private Empj_BuildingInfo building;

	@IFieldAnnotation(remark="施工编号")
    private String eCodeFromConstruction;
	
	@Getter @Setter @IFieldAnnotation(remark="变更形象进度")
    private Tgpj_BldLimitAmountVer_AFDtl expectFigureProgress;
    
    @Getter @Setter @IFieldAnnotation(remark="变更受限比例")
    private Double expectLimitedRatio;
    
    @Getter @Setter @IFieldAnnotation(remark="变更节点名称")
    private String expectLimitedName;
	
    @Getter @Setter @IFieldAnnotation(remark="预测完成日期")
    private Date completeDate;

	
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
	
	

}
