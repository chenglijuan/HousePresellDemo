package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 托管项目统计表（财务部）  接受Bean
 * @author ZS004
 */
@ITypeAnnotation(remark="托管项目统计表（财务部）")
public class Tg_LoanProjectCountM_View  implements Serializable
{
	
	private static final long serialVersionUID = -6582354015095148638L;
	
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	@Getter @Setter @IFieldAnnotation(remark="区域")
	private String cityRegion;//区域
	@Getter @Setter @IFieldAnnotation(remark="企业信息")
	private String companyName;//企业信息
	@Getter @Setter @IFieldAnnotation(remark="项目名称")
	private String projectName;//项目名称
	@Getter @Setter @IFieldAnnotation(remark="地上楼层数（总）")
	private Integer upTotalFloorNumber;//地上楼层数（总）
	@Getter @Setter @IFieldAnnotation(remark="托管合作协议签订日期")
	private String contractSigningDate;//托管合作协议签订日期
	@Getter @Setter @IFieldAnnotation(remark="预售证日期")
	private String preSaleCardDate;//预售证日期
	@Getter @Setter @IFieldAnnotation(remark="预售许可证")
	private String preSalePermits;//预售许可证
	@Getter @Setter @IFieldAnnotation(remark="协议编号")
	private String eCodeOfAgreement;//协议编号
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;//备注
	
	
	public String geteCodeOfAgreement()
	{
		return eCodeOfAgreement;
	}
	public void seteCodeOfAgreement(String eCodeOfAgreement)
	{
		this.eCodeOfAgreement = eCodeOfAgreement;
	}
	
	
}
