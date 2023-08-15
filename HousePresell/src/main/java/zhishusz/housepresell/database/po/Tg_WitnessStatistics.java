package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 对象名称：见证报告统计表
 * @author ZS004
 */
@ITypeAnnotation(remark="见证报告统计表")
public class Tg_WitnessStatistics  implements Serializable
{
	private static final long serialVersionUID = 8923116091452735841L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键	
	
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//项目名称  
	
	@Getter @Setter @IFieldAnnotation(remark="项目区域")
	private String projectArea;//项目区域
	
	@Getter @Setter @IFieldAnnotation(remark="施工编号")
	private String constructionNumber;//施工编号
	
	@Getter @Setter @IFieldAnnotation(remark="见证节点")
	private String witnessNode;//见证节点
	
	@Getter @Setter @IFieldAnnotation(remark="监理公司")
	private String supervisionCompany;//监理公司
	
	@Getter @Setter @IFieldAnnotation(remark="见证预约时间")
	private String witnessAppoinTime;//见证预约时间
	
	@Getter @Setter @IFieldAnnotation(remark="报告上传时间")
	private String reportUploadTime;//报告上传时间
	
}
