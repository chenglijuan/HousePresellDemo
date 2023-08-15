package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.ITypeAnnotation;

/*
 * 对象名称：项目风险明细表 - 导出EXCEL
 * */
@ITypeAnnotation(remark="项目风险明细表")
public class Tg_ProjectRiskViewExportExcelVO 
{

	@Getter @Setter
	private Integer ordinal;//序号 
	
	@Getter @Setter
	private String managedProjects;//托管项目
	
	@Getter @Setter
	private String floorBuilding;//托管楼幢
	
	@Getter @Setter
	private String managedArea;//托管面积(平方米)
	
	@Getter @Setter
	private String dateOfPresale;//预售许可批准日期
	
	@Getter @Setter
	private String totalOfoverground;//地上总层数
	
	@Getter @Setter
	private String currentConstruction;//当前建设进度
	
	@Getter @Setter
	private String updateTime;//进度更新时间
	
	@Getter @Setter
	private String appointedTime;//合同约定交付时间
	
	@Getter @Setter
	private String progressEvaluation;//进度评定
	
	@Getter @Setter 
	private String signTheEfficiency;//合同签订率
	
	@Getter @Setter
	private String contractFilingRate;//合同备案率
	
	@Getter @Setter
	private String contractLoanRatio;//合同贷款率
	
	@Getter @Setter 
	private String hostingFullRate;//托管满额率
	
	@Getter @Setter 
	private String unsignedContract;//未签订合同查封
	
	@Getter @Setter
	private String alreadyUnsignedContract;//已签订合同查封
	
	@Getter @Setter 
	private String astrict;//限制
	
	@Getter @Setter
    private String payguarantee;//有无保函(有/无)
	
	@Getter @Setter
	private String landMortgage;//土地抵押情况(有/无)
	
	@Getter @Setter
	private String otherRisks;//其他风险
	
	@Getter @Setter
	private String riskRating;//风险评级(高/中/低)
	
}
