package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：差异对比：留存权益
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_RemainRightDifferenceForm extends NormalActionForm
{
	private static final long serialVersionUID = 6700551498158946476L;
	
	@Getter @Setter
	private String[] deleCodes;//del eCodes
	@Getter @Setter
	private String url;//excel url
	@Getter @Setter
	private Long buildingRemainRightLogId;//楼幢留存权益-id
	@Getter @Setter
	private String billTimeStamp;//时间日期
	@Getter @Setter
	private String eCodeFromConstruction;//施工编号
	@Getter @Setter
	private String theNameOfProject;//项目名称
}
