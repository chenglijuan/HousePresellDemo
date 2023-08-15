package zhishusz.housepresell.controller.form;

import java.util.List;

import zhishusz.housepresell.database.po.Sm_User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：审批流-流程配置
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_ApprovalProcess_CfgForm extends NormalActionForm
{
	private static final long serialVersionUID = -3711609538883108904L;
	
	@Getter @Setter
	private Long tableId;//表ID
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
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String theName;//名称
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Long busiId;//参数定义表Id
	@Getter @Setter
	private String busiCode; //业务编码
	@Getter @Setter
	private String busiType; //业务类型
	@Getter @Setter
	private Integer isNeedBackup;//是否需要备案
	@Getter @Setter
	private Integer rejectModel;  //驳回模式 S_RejectModel  0 : 驳回到起草人 1 ： 驳回到上一级
	@Getter @Setter
	private List nodeList;//节点列表
	@Getter @Setter
	private String remark;//备注

	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
