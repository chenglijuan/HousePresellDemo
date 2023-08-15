package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：金融机构(承办银行)
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Emmp_BankInfoForm extends NormalActionForm
{
	private static final long serialVersionUID = -734133070657796164L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
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
	private String bankCode;//银行代码
	@Getter @Setter
	private String bankNo;//银行代号
	@Getter @Setter
	private String theName;//名称
	@Getter @Setter
	private String shortName;//简称
	@Getter @Setter
	private String leader;//负责人
	@Getter @Setter
	private String address;//所在地址
	@Getter @Setter
	private String capitalCollectionModel;//资金归集模式
	@Getter @Setter
	private String theType;//类型 S_CompanyType
	@Getter @Setter
	private String postalAddress;//通讯地址
	@Getter @Setter
	private String postalPort;//通讯端口
	@Getter @Setter
	private String contactPerson;//联系人
	@Getter @Setter
	private String contactPhone;//联系电话
	@Getter @Setter
	private String ftpDirAddress;//FTP目录地址
	@Getter @Setter
	private String ftpAddress;//FTP地址
	@Getter @Setter
	private String ftpPort;//FTP端口
	@Getter @Setter
	private String ftpUserName;//FTP用户名
	@Getter @Setter
	private String ftpPwd;//FTP密码
	@Getter @Setter
	private String financialInstitution;//金融机构
	@Getter @Setter
	private String theTypeOfPOS;//POS机型号
	@Getter @Setter
	private String eCodeOfSubject;//科目代码
	@Getter @Setter
	private String eCodeOfProvidentFundCenter;//公积金中心代码
	@Getter @Setter
	private String remark;//备注
	//机构成员
	@Getter @Setter
	private Emmp_OrgMemberForm[] orgMemberList;

	
	public String geteCode()
	{
		return eCode;
	}
	public void seteCode(String eCode)
	{
		this.eCode = eCode;
	}
	public String geteCodeOfSubject()
	{
		return eCodeOfSubject;
	}
	public void seteCodeOfSubject(String eCodeOfSubject)
	{
		this.eCodeOfSubject = eCodeOfSubject;
	}
	public String geteCodeOfProvidentFundCenter()
	{
		return eCodeOfProvidentFundCenter;
	}
	public void seteCodeOfProvidentFundCenter(String eCodeOfProvidentFundCenter)
	{
		this.eCodeOfProvidentFundCenter = eCodeOfProvidentFundCenter;
	}
}
