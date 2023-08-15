package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：银行网点(开户行)
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Emmp_BankBranchForm extends NormalActionForm
{
	private static final long serialVersionUID = 2406487433686376094L;
	
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
	private Emmp_BankInfo bank;//所属银行
	@Getter @Setter
	private Long bankId;//所属银行-Id
	@Getter @Setter
	private String theName;//名称
	@Getter @Setter
	private String shortName;//简称
	@Getter @Setter
	private String address;//所在地址
	@Getter @Setter
	private String contactPerson;//联系人
	@Getter @Setter
	private String contactPhone;//联系电话
	@Getter @Setter
	private String leader;//负责人
	@Getter @Setter
	private Integer isUsing;//是否启用
	@Getter @Setter
	private String subjCode;//科目代码
	@Getter @Setter
	private String desubjCode;//大额存单科目代码
	@Getter @Setter
	private String bblcsubjCode;//保本理财科目代码
	@Getter @Setter
	private String jgcksubjCode;//结构性存款科目代码
	@Getter @Setter 
	private String interbankCode;//联行号
	@Getter @Setter
	private Integer isDocking;//对接资金系统 下拉选择：1-是0-否
	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
}
