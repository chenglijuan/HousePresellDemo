package zhishusz.housepresell.controller.form;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：留存权益(此表为留存权益计算时临时表)
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpf_RemainRight_BakForm extends NormalActionForm
{
	private static final long serialVersionUID = 2949844209565476079L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String eCode;//编号
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
