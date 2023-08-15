package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：日志-关键操作
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_Operate_LogForm extends NormalActionForm
{
	private static final long serialVersionUID = -574560371312226655L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private Sm_User userOperate;//操作人员
	@Getter @Setter
	private Long userOperateId;//操作人员-Id
	@Getter @Setter
	private String remoteAddress;//访问来源IP
	@Getter @Setter
	private String operate;//操作事项
	@Getter @Setter
	private String inputForm;//输入参数
	@Getter @Setter
	private String result;//操作结果
	@Getter @Setter
	private String info;//操作结果提示信息
	@Getter @Setter
	private String returnJson;//返回的Json数据
	@Getter @Setter
	private Long startTimeStamp;//操作开始时间
	@Getter @Setter
	private Long endTimeStamp;//操作结束时间
}
