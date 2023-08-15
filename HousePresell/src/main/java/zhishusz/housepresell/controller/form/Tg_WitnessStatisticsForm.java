package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.controller.form.NormalActionForm;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：见证报告统计表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_WitnessStatisticsForm extends NormalActionForm
{

	private static final long serialVersionUID = -6733725668100461708L;

	@Getter @Setter 
	private Long tableId;//主键	
	
	@Getter @Setter 
	private String projectName;//项目名称  
	
	@Getter @Setter 
	private String projectArea;//项目区域
	
	@Getter @Setter 
	private String constructionNumber;//施工编号
	
	@Getter @Setter 
	private String witnessNode;//见证节点
	
	@Getter @Setter 
	private String supervisionCompany;//监理公司
	
	@Getter @Setter 
	private String witnessAppoinTime;//见证预约时间
	
	@Getter @Setter 
	private String reportUploadTime;//报告上传时间
	
	@Getter @Setter 
	private Long projectAreaId;//区域名称id
	
	@Getter @Setter 
	private Long supervisionCompanyId;//监理公司id
	
	@Getter @Setter
	private Long projectNameId;//项目名称id
	
	@Getter @Setter 
	private String billTimeStamp;//报告上传开始时间
	
	@Getter @Setter 
	private String endBillTimeStamp;//报告上传结束时间
}
