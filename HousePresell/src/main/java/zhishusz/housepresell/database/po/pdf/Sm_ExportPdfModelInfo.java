package zhishusz.housepresell.database.po.pdf;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：打印模板信息存储
 * */
@ITypeAnnotation(remark="打印模板信息存储")
public class Sm_ExportPdfModelInfo  implements Serializable
{
	
	private static final long serialVersionUID = -6362459397147810751L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	
	@Getter @Setter @IFieldAnnotation(remark="条形码")
	private String barCodePath;//条形码
	
	@Getter @Setter @IFieldAnnotation(remark="二维码")
	private String qrCodePath;//二维码
	
	@Getter @Setter @IFieldAnnotation(remark="水印图片路径")
	private String watermarkPaht;//水印图片路径
	
	@Getter @Setter @IFieldAnnotation(remark="查询语句不循环")
	private String parametersMap;//查询语句不循环
	
	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;//业务编码
	
	@Getter @Setter @IFieldAnnotation(remark="业务主键")
	private String sourceId;//业务主键
	
	@Getter @Setter @IFieldAnnotation(remark="导入路径")
	private String impModelPath;//导入路径
	
	@Getter @Setter @IFieldAnnotation(remark="查询语句循环")
	private String querySql;//查询语句循环
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private String createTime;//创建时间
	
	@Getter @Setter @IFieldAnnotation(remark="数据源类型")
	private String dateSourceType;//数据源类型
	
	@Getter @Setter @IFieldAnnotation(remark="是否启用（0 未启用  1 启用）")
	private Integer isUsing;//是否启用（0 未启用  1 启用）
	
	@Getter @Setter @IFieldAnnotation(remark="数据库地址")
	private String url;//数据库地址
	
	@Getter @Setter @IFieldAnnotation(remark="数据库驱动")
	private String driver;//数据库驱动
	
	@Getter @Setter @IFieldAnnotation(remark="用户名称")
	private String userName;//用户名称
	
	@Getter @Setter @IFieldAnnotation(remark="密码")
	private String passWord;//密码
	
	@Getter @Setter @IFieldAnnotation(remark="查询条件")
	private String queryCondition;//查询条件
}
