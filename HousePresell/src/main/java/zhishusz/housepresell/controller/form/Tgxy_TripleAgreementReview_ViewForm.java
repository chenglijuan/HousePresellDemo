package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * 三方协议考评
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_TripleAgreementReview_ViewForm extends NormalActionForm
{
	private static final long serialVersionUID = -3529177904134579855L;

	@Getter @Setter 
	private Long tableId;//主键
	@Getter @Setter
	private Long rejectReasonId;//退回原因主键
	@Getter @Setter
	private Long proxyCompanyId;//代理公司主键
	@Getter @Setter
	private String beginTime;//开始时间
	@Getter @Setter
	private String endTime;//结束时间
	@Getter @Setter
	private String rejectReason;//退回原因
	@Getter @Setter
	private String proxyCompany;//代理公司
}
