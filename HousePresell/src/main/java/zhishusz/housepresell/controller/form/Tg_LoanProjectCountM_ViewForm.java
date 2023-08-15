package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：托管项目统计表（财务部）
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tg_LoanProjectCountM_ViewForm extends NormalActionForm
{

	private static final long serialVersionUID = -118278856018046927L;
	
	@Getter @Setter 
	private Long tableId;//主键
	@Getter @Setter 
	private String cityRegion;//区域
	@Getter @Setter 
	private String companyName;//企业信息
	@Getter @Setter
	private String projectName;//项目名称
	@Getter @Setter
	private Integer upTotalFloorNumber;//地上楼层数（总）
	@Getter @Setter
	private String contractSigningDate;//托管合作协议签订日期
	@Getter @Setter
	private String preSaleCardDate;//预售证日期
	@Getter @Setter
	private String preSalePermits;//预售许可证
	@Getter @Setter
	private String eCodeOfAgreement;//协议编号
	@Getter @Setter
	private String remark;//备注
	
	
	//接收页面传递的参数
	@Getter @Setter
	private Long cityRegionId;//区域Id
	@Getter @Setter
	private Long projectId;//项目Id
	@Getter @Setter
	private Long companyId;//企业Id
	@Getter @Setter
	private String endContractSigningDate;//托管合作协议签订日期（结束）
	@Getter @Setter
	private String endPreSaleCardDate;//预售证日期（结束）
}
