package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_BaseParameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：参数定义
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_BaseParameterForm extends NormalActionForm
{
	private static final long serialVersionUID = 3691853774176879323L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
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
	private Sm_BaseParameter parentParameter;//父级参数
	@Getter @Setter
	private Long parentParameterId;//父级参数-Id
	@Getter @Setter
	private String theName;//参数名
	@Getter @Setter
	private String theValue;//参数值
	@Getter @Setter
	private Long validDateFrom;//有效期-开始时间
	@Getter @Setter
	private Long validDateTo;//有效期-结束时间
	@Getter @Setter
	private Integer theVersion;//版本号
	@Getter @Setter
	private String parametertype;//参数类型
	@Getter @Setter
	private String remark;//类型说明
	@Getter @Setter
	private String exceptZhengTai;//排除正泰用户
}
