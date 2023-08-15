package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：项目-工程进度预测 -明细表 
 * */
//TODO 参照需求文档 2.2.7.6 --p88 
@ITypeAnnotation(remark="项目-工程进度预测 -明细表")
public class Empj_PjDevProgressForcastDtl implements Serializable
{
	private static final long serialVersionUID = -6628989620790744728L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;
	
	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="关联工程进度预测-主表")
	private Empj_PjDevProgressForcast mainTable;
	
	@Getter @Setter @IFieldAnnotation(remark="巡查日期")
	private Long patrolTimestamp;
	
	@Getter @Setter @IFieldAnnotation(remark="当前进度节点")  
	private Double currentProgressNode;

	@Getter @Setter @IFieldAnnotation(remark="预测进度节点")  
	private Double predictedFigureProgress;

	@Getter @Setter @IFieldAnnotation(remark="前一个关联受限额度节点，用于收支存预测")
	private Tgpj_BldLimitAmountVer_AFDtl beforeBldLimitAmountVerAfDtl;

	@Getter @Setter @IFieldAnnotation(remark="楼幢Id，用于收支存预测")
	private Empj_BuildingInfo buildingInfo;

	@Getter @Setter @IFieldAnnotation(remark="关联受限额度节点")
	private Tgpj_BldLimitAmountVer_AFDtl bldLimitAmountVerAfDtl;

	@Getter @Setter @IFieldAnnotation(remark="预测完成日期（原）")
	private Long ogPredictedFinishDatetime;

	@Getter @Setter @IFieldAnnotation(remark="预测完成日期（新）")
	private Long predictedFinishDatetime;
	
	@Getter @Setter @IFieldAnnotation(remark="进度判定")
	private Integer progressJudgement;//【默认正常（0-正常、1-滞后）】
	
	@Getter @Setter @IFieldAnnotation(remark="进度滞后原因说明")
	private String causeDescriptionForDelay;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
