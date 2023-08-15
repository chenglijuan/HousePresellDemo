package zhishusz.housepresell.database.po;

import zhishusz.housepresell.database.po.internal.IApprovable;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：楼幢-备案均价
 * */
@ITypeAnnotation(remark="楼幢-备案均价")
public class Tgpj_BuildingAvgPrice implements Serializable,IApprovable
{
	private static final long serialVersionUID = 3699334554962323729L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;

	@Getter @Setter @IFieldAnnotation(remark="流程状态")
	private String approvalState;
	
	@Getter @Setter @IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="最后修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="备案均价")
	private Double recordAveragePrice;
	
	@Getter @Setter @IFieldAnnotation(remark="关联楼幢")
	private Empj_BuildingInfo buildingInfo;
	
	@Getter @Setter @IFieldAnnotation(remark="物价备案日期")
	private Long averagePriceRecordDate;
	
	@Getter @Setter @IFieldAnnotation(remark="预售系统楼幢住宅备案均价",columnName="recordAvgPriceFromPS")
	private Double recordAveragePriceFromPresellSystem;

	@Getter @Setter @IFieldAnnotation(remark = "备注")
	private String remark;
	
	@Getter @Setter @IFieldAnnotation(remark="外来数据关联主键")
	private String externalId;

	@Override
	public String getSourceType() {
		return getClass().getName();
	}

	@Override
	public Long getSourceId() {
		return tableId;
	}

	@Override
	public String getEcodeOfBusiness() {
		return eCode;
	}

	@Override
	public List<String> getPeddingApprovalkey() {
		List<String> peddingApprovalkey = new ArrayList<String>();

		peddingApprovalkey.add("recordAveragePrice");
		peddingApprovalkey.add("remark");
		peddingApprovalkey.add("generalAttachmentList");

		return peddingApprovalkey;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterSuccess()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updatePeddingApprovalDataAfterFail()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public List getNeedFieldList(){
		return Arrays.asList("eCode","theName");
	}

}
