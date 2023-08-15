package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：买受人信息
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgxy_BuyerInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = 7549377763719583698L;
	
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
	private String buyerName;//买受人姓名
	@Getter @Setter
	private String buyerType;//买受人类型
	@Getter @Setter
	private String certificateType;//证件类型
	@Getter @Setter
	private String eCodeOfcertificate;//证件号码
	@Getter @Setter
	private String contactPhone;//联系电话
	@Getter @Setter
	private String contactAdress;//联系地址
	@Getter @Setter
	private String agentName;//代理人姓名
	@Getter @Setter
	private String agentCertType;//代理人证件类型
	@Getter @Setter
	private String agentCertNumber;//代理人证件号
	@Getter @Setter
	private String agentPhone;//代理人联系电话
	@Getter @Setter
	private String agentAddress;//代理人联系地址
	@Getter @Setter
	private String eCodeOfContract;//合同备案号
	@Getter @Setter
	private Long contractInfoId;//合同Id
	@Getter @Setter
	private Long houseInfoId;//户室Id
	@Getter @Setter 
	private Tgxy_TripleAgreement tripleAgreement;
	private String eCodeOfTripleAgreement;
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	public String geteCodeOfcertificate() {
		return eCodeOfcertificate;
	}
	public void seteCodeOfcertificate(String eCodeOfcertificate) {
		this.eCodeOfcertificate = eCodeOfcertificate;
	}
	public String geteCodeOfContract()
	{
		return eCodeOfContract;
	}
	public void seteCodeOfContract(String eCodeOfContract)
	{
		this.eCodeOfContract = eCodeOfContract;
	}
	public String geteCodeOfTripleAgreement() {
		return eCodeOfTripleAgreement;
	}
	public void seteCodeOfTripleAgreement(String eCodeOfTripleAgreement) {
		this.eCodeOfTripleAgreement = eCodeOfTripleAgreement;
	}
}
