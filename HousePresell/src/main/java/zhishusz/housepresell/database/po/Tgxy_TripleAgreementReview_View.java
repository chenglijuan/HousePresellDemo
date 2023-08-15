package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 三方协议考评
 * @author ZS004
 */
@ITypeAnnotation(remark="三方协议考评表")
public class Tgxy_TripleAgreementReview_View  implements Serializable
{
	private static final long serialVersionUID = 8923116091452735841L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	@Getter @Setter @IFieldAnnotation(remark="协议编号")
	private String eCode;//协议编号  
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//托管账户 
	@Getter @Setter @IFieldAnnotation(remark="房屋坐落")
	private String position;//房屋坐落
	@Getter @Setter @IFieldAnnotation(remark="买受人姓名")
	private String buyerName;//买受人姓名
	@Getter @Setter @IFieldAnnotation(remark="区域负责人")
	private String areaManager;//区域负责人
	@Getter @Setter @IFieldAnnotation(remark="退回原因")
	private String rejectReason;//退回原因
	@Getter @Setter @IFieldAnnotation(remark="退回时间")
	private String rejectTimeStamp;//退回时间
	@Getter @Setter @IFieldAnnotation(remark="代理公司")
	private String proxyCompany;//代理公司
	@Getter @Setter @IFieldAnnotation(remark="整改时间")
	private String changeTimeStamp;//整改时间
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
