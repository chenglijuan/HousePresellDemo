package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管楼幢入账统计表报表
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_BuildCount_ViewForm extends NormalActionForm
{
	private static final long serialVersionUID = -3529177904134579855L;

	@Getter @Setter 
	private Long tableId;//主键
	@Getter @Setter
	private String companyName;//开发企业 
	@Getter @Setter 
	private String projectName;//项目名称  
	@Getter @Setter
	private String eCodeFroMconstruction;//楼幢  
	@Getter @Setter
	private String bankName;//银行名称
	@Getter @Setter 
	private Double income;//托管收入
	@Getter @Setter 
	private Double payout;//托管支出
	@Getter @Setter 
	private Double balance;//余额
	@Getter @Setter 
	private Double commercialLoan;//余额
	@Getter @Setter 
	private Double accumulationFund;//余额
	@Getter @Setter 
	private String billTimeStamp;//记账日期
	
	
	//页面接收字段
	@Getter @Setter
	private String endBillTimeStamp;//记账结束日期;
	@Getter @Setter
	private Long companyId;//开发企业ID
	@Getter @Setter
	private Long projectId;//项目ID
	@Getter @Setter
	private Long buildId;//楼幢ID
}
