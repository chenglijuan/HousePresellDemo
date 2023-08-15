package zhishusz.housepresell.exportexcelvo;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 三方协议考评
 * @author ZS004
 */
public class Tgxy_TripleAgreementReviewExcelVO
{

	@Getter @Setter 
	private Integer ordinal;//序号
	@Getter @Setter
	private String eCode;//协议编号  
	@Getter @Setter
	private String projectName;//托管账户 
	@Getter @Setter
	private String position;//房屋坐落
	@Getter @Setter
	private String buyerName;//买受人姓名
	@Getter @Setter
	private String areaManager;//区域负责人
	@Getter @Setter
	private String rejectReason;//退回原因
	@Getter @Setter
	private String rejectTimeStamp;//退回时间
	@Getter @Setter
	private String proxyCompany;//代理公司
	@Getter @Setter
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
