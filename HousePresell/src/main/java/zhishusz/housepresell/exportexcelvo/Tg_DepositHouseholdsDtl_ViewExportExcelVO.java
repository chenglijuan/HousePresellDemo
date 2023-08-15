package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

public class Tg_DepositHouseholdsDtl_ViewExportExcelVO
{

	@Getter @Setter 
	private Integer ordinal;//序号 

	//---------公共字段-Start---------//
	
	@Getter @Setter 
	private String eCodeOfContractRecord;
	
	@Getter @Setter
	private String position;
	
	@Getter @Setter
	private String buyerName;
	
	@Getter @Setter
	private String eCodeOfcertificate;

	@Getter @Setter
	public String contactPhone;
	
	@Getter @Setter 
	public String payWay;
	
	@Getter @Setter 
	private String contractCteateState;
	
	@Getter @Setter
	private String contractCreateTime;
	
	@Getter @Setter 
	private String contractRecordTime;
	
	@Getter @Setter 
	private String tripleAgreementRecordState;
	
	@Getter @Setter 
	private String tripleAgreementRecordTime;

	@Getter @Setter 
	private Double totalAmountOfHouse;
	
	@Getter @Setter 
	private Double theAmountOfRetainedEquity;
}
