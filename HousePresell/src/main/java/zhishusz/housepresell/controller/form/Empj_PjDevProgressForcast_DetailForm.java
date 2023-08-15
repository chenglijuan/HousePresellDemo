package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Empj_PjDevProgressForcast;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：项目-工程进度预测 -明细表 
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Empj_PjDevProgressForcast_DetailForm extends NormalActionForm
{
	private static final long serialVersionUID = 8114150837715574965L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//修改人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private Empj_PjDevProgressForcast mainTable;//关联工程进度预测-主表
	@Getter @Setter
	private Long mainTableId;//关联工程进度预测-主表-Id
	@Getter @Setter
	private Long patrolTimestamp;//巡查日期
	@Getter @Setter
	private Double currentProgressNode;//当前进度节点
	@Getter @Setter
	private Double predictedFigureProgress;//预测进度节点
	@Getter @Setter
	private Long predictedFinishDatetime;//预测完成日期
	@Getter @Setter
	private Integer progressJudgement;//进度判定
	@Getter @Setter
	private String causeDescriptionForDelay;//进度滞后原因说明
	@Getter @Setter
	private String remark;//备注
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
