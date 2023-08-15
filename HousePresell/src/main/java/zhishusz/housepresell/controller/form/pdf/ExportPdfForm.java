package zhishusz.housepresell.controller.form.pdf;

import zhishusz.housepresell.controller.form.NormalActionForm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：导出PDF
 * Company：ZhiShuSZ
 */
@ToString(callSuper = true)
public class ExportPdfForm extends NormalActionForm
{
	private static final long serialVersionUID = -5376297533461273193L;

	@Getter
	@Setter
	private String sourceId;// 关联数据Id
	
	@Getter
	@Setter
	private String sourceBusiCode;// 关联数据编码
	
	@Getter
	@Setter
	private String reqAddress;//请求地址
}
