package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：日志-业务状态
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_BusiState_LogForm extends NormalActionForm
{
	private static final long serialVersionUID = -3491659624722576571L;
	
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
	private Long operateTimeStamp;//操作时间
	@Getter @Setter
	private Long sourceId;//数据源Id
	@Getter @Setter
	private String sourceType;//数据源类型
	@Getter @Setter
	private String orgObjJsonFilePath;//修改前数据Json文件路径
	@Getter @Setter
	private String newObjJsonFilePath;//修改后数据Json文件路径
	/**
	 * class类所在的包名，例如Emmp_BankBranch这个po的路径就是zhishusz.housepresell.database.po.
	 * 如果是Template的类就是zhishusz.housepresell.util.objectdiffer.model.
	 * 如果不传这个值，就默认为使用zhishusz.housepresell.database.po.
	 */
	@Getter @Setter
	private String packagePath;
}
