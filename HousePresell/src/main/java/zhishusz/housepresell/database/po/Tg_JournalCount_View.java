package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 日记账统计表  接受Bean
 * @author ZS004
 */
@ITypeAnnotation(remark="日记账统计表")
public class Tg_JournalCount_View  implements Serializable
{
	private static final long serialVersionUID = 8923116091452735841L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	@Getter @Setter @IFieldAnnotation(remark="入账日期")
	private String loanInDate;//入账日期 
	@Getter @Setter @IFieldAnnotation(remark="托管账户名称")
	private String escrowAcountName;//托管账户名称
	@Getter @Setter @IFieldAnnotation(remark="确认总笔数")
	private Integer tradeCount;//确认总笔数
	@Getter @Setter @IFieldAnnotation(remark="确认总金额")
	private Double totalTradeAmount;//确认总金额
	@Getter @Setter @IFieldAnnotation(remark="公积金贷款总笔数")
	private Integer aToatlLoanAmoutCount;//公积金贷款总笔数
	@Getter @Setter @IFieldAnnotation(remark="公积金贷款总金额")
	private Double aToatlLoanAmout;//公积金贷款总金额
	@Getter @Setter @IFieldAnnotation(remark="商业贷款总笔数")
	private Integer bToatlLoanAmoutCount;//商业贷款总笔数
	@Getter @Setter @IFieldAnnotation(remark="商业贷款总金额")
	private Double bToatlLoanAmout;//商业贷款总金额
	@Getter @Setter @IFieldAnnotation(remark="自有资金总笔数")
	private Integer oToatlLoanAmoutCount;//自有资金总笔数
	@Getter @Setter @IFieldAnnotation(remark="自有资金总金额")
	private Double oToatlLoanAmout;//自有资金总金额
	@Getter @Setter @IFieldAnnotation(remark="公积金首付款总笔数")
	private Integer aFristToatlLoanAmoutCount;//公积金首付款总笔数
	@Getter @Setter @IFieldAnnotation(remark="公积金首付款总金额")
	private Double aFristToatlLoanAmout;//公积金首付款总金额
	@Getter @Setter @IFieldAnnotation(remark="公转商贷款总笔数")
	private Integer aToBusinessToatlLoanAmoutCount;//公转商贷款总笔数
	@Getter @Setter @IFieldAnnotation(remark="公转商贷款总金额")
	private Double aToBusinessToatlLoanAmout;//公转商贷款总金额
	
	public Integer getaToatlLoanAmoutCount()
	{
		return aToatlLoanAmoutCount;
	}
	public void setaToatlLoanAmoutCount(Integer aToatlLoanAmoutCount)
	{
		this.aToatlLoanAmoutCount = aToatlLoanAmoutCount;
	}
	public Double getaToatlLoanAmout()
	{
		return aToatlLoanAmout;
	}
	public void setaToatlLoanAmout(Double aToatlLoanAmout)
	{
		this.aToatlLoanAmout = aToatlLoanAmout;
	}
	public Integer getbToatlLoanAmoutCount()
	{
		return bToatlLoanAmoutCount;
	}
	public void setbToatlLoanAmoutCount(Integer bToatlLoanAmoutCount)
	{
		this.bToatlLoanAmoutCount = bToatlLoanAmoutCount;
	}
	public Double getbToatlLoanAmout()
	{
		return bToatlLoanAmout;
	}
	public void setbToatlLoanAmout(Double bToatlLoanAmout)
	{
		this.bToatlLoanAmout = bToatlLoanAmout;
	}
	public Integer getoToatlLoanAmoutCount()
	{
		return oToatlLoanAmoutCount;
	}
	public void setoToatlLoanAmoutCount(Integer oToatlLoanAmoutCount)
	{
		this.oToatlLoanAmoutCount = oToatlLoanAmoutCount;
	}
	public Double getoToatlLoanAmout()
	{
		return oToatlLoanAmout;
	}
	public void setoToatlLoanAmout(Double oToatlLoanAmout)
	{
		this.oToatlLoanAmout = oToatlLoanAmout;
	}
	public Integer getaFristToatlLoanAmoutCount()
	{
		return aFristToatlLoanAmoutCount;
	}
	public void setaFristToatlLoanAmoutCount(Integer aFristToatlLoanAmoutCount)
	{
		this.aFristToatlLoanAmoutCount = aFristToatlLoanAmoutCount;
	}
	public Double getaFristToatlLoanAmout()
	{
		return aFristToatlLoanAmout;
	}
	public void setaFristToatlLoanAmout(Double aFristToatlLoanAmout)
	{
		this.aFristToatlLoanAmout = aFristToatlLoanAmout;
	}
	public Integer getaToBusinessToatlLoanAmoutCount()
	{
		return aToBusinessToatlLoanAmoutCount;
	}
	public void setaToBusinessToatlLoanAmoutCount(Integer aToBusinessToatlLoanAmoutCount)
	{
		this.aToBusinessToatlLoanAmoutCount = aToBusinessToatlLoanAmoutCount;
	}
	public Double getaToBusinessToatlLoanAmout()
	{
		return aToBusinessToatlLoanAmout;
	}
	public void setaToBusinessToatlLoanAmout(Double aToBusinessToatlLoanAmout)
	{
		this.aToBusinessToatlLoanAmout = aToBusinessToatlLoanAmout;
	}
	
	
	
}
