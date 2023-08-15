package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：协定存款统计表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_DepositFloat_ViewForm  extends NormalActionForm
{

	private static final long serialVersionUID = -1151366946826029418L;

	@Getter @Setter 
	private Long tableId;//主表主键
	
	@Getter @Setter 
	private String bankName;//银行名称

	@Getter @Setter 
	private Double depositRate;//协定存款利率
	
	@Getter @Setter 
	private Double orgAmount;//起始金额（万元）
	
	@Getter @Setter 
	private String signDate;//签订日期
	
	@Getter @Setter 
	private String timeLimit;//期限
	
	@Getter @Setter 
	private String endExpirationDate;//到期日期
	
	@Getter @Setter 
	private String remark;//备注
	
	// 接收页面传递的参数
	@Getter @Setter 
	private Long bankBrenchId;//开户行Id
	
	@Getter @Setter 
	private String signDateStart;//签订时间开始
	
	@Getter @Setter 
	private String signDateEnd;//签订时间结束
	
	@Getter @Setter 
	private String endExpirationDateStart;//到期时间开始
	
	@Getter @Setter 
	private String endExpirationDateEnd;//到期时间结束
}
