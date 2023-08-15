package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 项目风险明细表
 * */
@ITypeAnnotation(remark="项目风险明细表")
public class Tg_ProjectRiskView	implements Serializable
{

	private static final long serialVersionUID = -1555254585712052695L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	
	
	@Getter @Setter @IFieldAnnotation(remark="托管项目")
	private String managedProjects;
	
	@Getter @Setter @IFieldAnnotation(remark="托管楼幢")
	private String floorBuilding;
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积(平方米)")
	private String managedArea;
	
	@Getter @Setter @IFieldAnnotation(remark="预售许可批准日期")
	private String dateOfPresale;
	
	@Getter @Setter @IFieldAnnotation(remark="地上总层数")
	private String totalOfoverground;
	
	@Getter @Setter @IFieldAnnotation(remark="当前建设进度")
	private String currentConstruction;
	
	@Getter @Setter @IFieldAnnotation(remark="进度更新时间")
	private String updateTime;
	
	@Getter @Setter @IFieldAnnotation(remark="合同约定交付时间")
	private String appointedTime;
	
	@Getter @Setter @IFieldAnnotation(remark="进度评定")
	private String progressEvaluation;
	
	@Getter @Setter @IFieldAnnotation(remark="合同签订率")
	private String signTheEfficiency;
	
	@Getter @Setter @IFieldAnnotation(remark="合同备案率")
	private String contractFilingRate;
	
	@Getter @Setter @IFieldAnnotation(remark="合同贷款率")
	private String contractLoanRatio;
	
	@Getter @Setter @IFieldAnnotation(remark="托管满额率")
	private String hostingFullRate;
	
	@Getter @Setter @IFieldAnnotation(remark="未签订合同查封")
	private String unsignedContract;
	
	@Getter @Setter @IFieldAnnotation(remark="已签订合同查封")
	private String alreadyUnsignedContract;
	
	@Getter @Setter @IFieldAnnotation(remark="限制")
	private String astrict;
	
	@Getter @Setter @IFieldAnnotation(remark="有无保函(有/无)")
    private String payguarantee;
	
	@Getter @Setter @IFieldAnnotation(remark="土地抵押情况(有/无)")
	private String landMortgage;
	
	@Getter @Setter @IFieldAnnotation(remark="其他风险")
	private String otherRisks;
	
	@Getter @Setter @IFieldAnnotation(remark="风险评级(高/中/低)")
	private String riskRating ;
	
	@Getter @Setter @IFieldAnnotation(remark="所属区域")
	private String area;
	
	@Getter @Setter @IFieldAnnotation(remark="日期")
	private String dateQuery;
	
	@Getter @Setter @IFieldAnnotation(remark="查封情况")
	private String attachment;
	
	
	
	
	
	
	
	
}
