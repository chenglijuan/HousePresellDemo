package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Sm_User;

import zhishusz.housepresell.util.IFieldAnnotation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sun.rmi.runtime.Log;

/*
 * Form表单：版本管理-托管标准
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpj_EscrowStandardVerMngForm extends NormalActionForm
{
	private static final long serialVersionUID = 712462783394394624L;
	
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
	private Sm_User userUpdate;//最后修改人人
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
	private String theName;//版本名称
	@Getter @Setter
	private String theVersion;//版本号
	@Getter @Setter
	private Boolean hasEnable;//是否启用
	@Getter @Setter
	private String enableState;//启用状态：0全部，1启用，2停用
	@Getter @Setter
	private String theType;//托管标准类型 (枚举选择:0-标准金额1-标准比例)
	@Getter @Setter
	private String theContent;//托管标准
	@Getter @Setter
	private Double amount;//金额
	@Getter @Setter
	private Double percentage;//比例
	@Getter @Setter
	private String extendParameter1;//备选参数1
	@Getter @Setter
	private String extendParameter2;//备选参数2
	@Getter @Setter
	private Long beginExpirationDate;//启用日期
	@Getter @Setter
	private Long endExpirationDate;//停用日期
	@Getter @Setter
	private Long expirationDate;//当前使用时间
	@Getter @Setter
	private String getDetailType; //获取详情类型（1、详情信息带审批流，2、详情信息不带审批流）
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}
