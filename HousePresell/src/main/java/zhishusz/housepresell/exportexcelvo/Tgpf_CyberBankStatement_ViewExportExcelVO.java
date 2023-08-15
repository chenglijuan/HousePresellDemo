package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/**
 * 银行放款项目入账情况表  接受Bean
 * @author ZS004
 */
public class Tgpf_CyberBankStatement_ViewExportExcelVO
{

	@Getter @Setter 
	private Integer ordinal;//序号
	@Getter @Setter
	private String billTimeStamp;//记账日期
	@Getter @Setter
	private String theNameOfBank;//银行名称
	@Getter @Setter
	private String theNameOfBankBranch;//开户行
	@Getter @Setter
	private String theAccountOfBankAccountEscrowed;//托管账户
	@Getter @Setter
	private Integer transactionCount;//总笔数
	@Getter @Setter
	private Double transactionAmount;//总金额
	@Getter @Setter
	private String uploadTimeStamp;//上传日期
	@Getter @Setter
	private String fileUploadState;//上传状态
	
}
