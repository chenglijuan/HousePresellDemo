package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：机构信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_RemainRightImportExcelForm extends NormalActionForm
{
	private static final long serialVersionUID = 136025580502029648L;
	
	@Getter @Setter
	private String url;//excel url
	@Getter @Setter
	private String billTimeStamp;//时间日期
}
