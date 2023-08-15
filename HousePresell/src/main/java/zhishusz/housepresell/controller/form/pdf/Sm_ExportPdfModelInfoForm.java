package zhishusz.housepresell.controller.form.pdf;

import zhishusz.housepresell.controller.form.NormalActionForm;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：模板配置
 * Company：ZhiShuSZ
 */
@ToString(callSuper = true)
public class Sm_ExportPdfModelInfoForm extends NormalActionForm
{
	
	private static final long serialVersionUID = -867829280037642787L;

	@Getter @Setter 
	private Long tableId;//主键
	
	@Getter @Setter
	private String barCodePath;//条形码
	
	@Getter @Setter 
	private String qrCodePath;//二维码
	
	@Getter @Setter 
	private String watermarkPaht;//水印图片路径
	
	@Getter @Setter
	private String parametersMap;//查询语句不循环
	
	@Getter @Setter
	private String busiCode;//业务编码
	
	@Getter @Setter 
	private String sourceId;//业务主键
	
	@Getter @Setter 
	private String impModelPath;//导入路径
	
	@Getter @Setter 
	private String querySql;//查询语句循环
	
	@Getter @Setter
	private String createTime;//创建时间
	
	@Getter @Setter
	private String dateSourceType;//数据源类型
	
	@Getter @Setter 
	private Integer isUsing;//是否启用（0 未启用  1 启用）
	
	@Getter @Setter 
	private String url;//数据库地址
	
	@Getter @Setter
	private String driver;//数据库驱动
	
	@Getter @Setter
	private String userName;//用户名称
	
	@Getter @Setter 
	private String passWord;//密码
	
	@Getter @Setter 
	private String queryCondition;//查询条件
}
