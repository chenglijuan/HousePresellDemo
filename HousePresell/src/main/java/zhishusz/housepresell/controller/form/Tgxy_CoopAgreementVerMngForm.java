package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：合作协议版本管理
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_CoopAgreementVerMngForm extends NormalActionForm
{
	private static final long serialVersionUID = 7228237746451687542L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private Integer theState1;//是否启用
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
	private String userUpdate;//修改人
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
	private String theName;//协议版本名称
	@Getter @Setter
	private String theVersion;//版本号
	@Getter @Setter
	private String theType;//协议类型
	@Getter @Setter
	private String enableTimeStamp;//启用时间
	@Getter @Setter
	private String downTimeStamp;//停用时间
	@Getter @Setter
	private String templateFilePath;//模板文件路径
	@Getter @Setter
	private String contractApplicationDate;//签约申请日期
	//附件参数
	@Getter @Setter
	private String smAttachmentList;
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}
