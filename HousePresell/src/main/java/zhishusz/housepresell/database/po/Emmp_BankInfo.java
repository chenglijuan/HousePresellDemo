package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：金融机构(承办银行)
 * */
@ITypeAnnotation(remark="金融机构(承办银行)")
public class Emmp_BankInfo implements Serializable
{
	private static final long serialVersionUID = -3375412203215302879L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")//以前叫银行代码，现在叫银行编码
	private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

	@Getter @Setter @IFieldAnnotation(remark="创建人")
	private Sm_User userStart;
	
	@Getter @Setter @IFieldAnnotation(remark="创建时间")
	private Long createTimeStamp;
	
	@Getter @Setter @IFieldAnnotation(remark="修改人")
	private Sm_User userUpdate;

	@Getter @Setter @IFieldAnnotation(remark="最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="备案人")
	private Sm_User userRecord;
	
	@Getter @Setter @IFieldAnnotation(remark="备案日期")
	private Long recordTimeStamp;
	//---------公共字段-Start---------//
	
	@Getter @Setter @IFieldAnnotation(remark="银行代码")//以前叫银行代码，现在叫银行代码
	private String bankCode;//用户输入
	
	@Getter @Setter @IFieldAnnotation(remark="银行代号")//以前没有，现在叫银行编号
	private String bankNo;//用户输入
	
	@Getter @Setter @IFieldAnnotation(remark="名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="简称")
	private String shortName;
	
	@Getter @Setter @IFieldAnnotation(remark="负责人")
	private String leader;
	
	@Getter @Setter @IFieldAnnotation(remark="所在地址")
	private String address;
	
	@Getter @Setter @IFieldAnnotation(remark="资金归集模式")
	private String capitalCollectionModel;//【手工输入接口归集：1-对接银行核心系统2-正泰银行端3-对接银行特色平台】
	
	@Getter @Setter @IFieldAnnotation(remark="类型 S_CompanyType")
	private String theType;
	
	@Getter @Setter @IFieldAnnotation(remark="通讯地址")
	private String postalAddress;
	
	@Getter @Setter @IFieldAnnotation(remark="通讯端口")
	private String postalPort;
	
	@Getter @Setter @IFieldAnnotation(remark="联系人")
	private String contactPerson;

	@Getter @Setter @IFieldAnnotation(remark="联系电话")
	private String contactPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="FTP目录地址")
	private String ftpDirAddress;

	@Getter @Setter @IFieldAnnotation(remark="FTP地址")
	private String ftpAddress;

	@Getter @Setter @IFieldAnnotation(remark="FTP端口")
	private String ftpPort;
	
	@Getter @Setter @IFieldAnnotation(remark="FTP用户名")
	private String ftpUserName;
	
	@Getter @Setter @IFieldAnnotation(remark="FTP密码")
	private String ftpPwd;
	
	@Getter @Setter @IFieldAnnotation(remark="金融机构")
	//todo 这个字段的作用是什么？
	private String financialInstitution;
	
	@Getter @Setter @IFieldAnnotation(remark="POS机型号")
	private String theTypeOfPOS;
	
	@IFieldAnnotation(remark="科目代码")
	private String eCodeOfSubject;
	
	@IFieldAnnotation(remark="公积金中心代码")
	private String eCodeOfProvidentFundCenter;
	
	@Getter @Setter @IFieldAnnotation(remark="备注")
	private String remark;

	@Getter @Setter @IFieldAnnotation(remark="是否启用")
	private Integer isUsing;//0：启用，1：不启用

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteCodeOfSubject() {
		return eCodeOfSubject;
	}

	public void seteCodeOfSubject(String eCodeOfSubject) {
		this.eCodeOfSubject = eCodeOfSubject;
	}

	public String geteCodeOfProvidentFundCenter() {
		return eCodeOfProvidentFundCenter;
	}

	public void seteCodeOfProvidentFundCenter(String eCodeOfProvidentFundCenter) {
		this.eCodeOfProvidentFundCenter = eCodeOfProvidentFundCenter;
	}

	public List getNeedFieldList(){
		return Arrays.asList("theName", "shortName","userStart/theName");
	}
}
