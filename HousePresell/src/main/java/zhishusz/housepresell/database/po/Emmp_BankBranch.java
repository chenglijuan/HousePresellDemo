package zhishusz.housepresell.database.po;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：银行网点(开户行)
 * */
@ITypeAnnotation(remark="银行网点(开户行)")
public class Emmp_BankBranch implements Serializable
{
	private static final long serialVersionUID = 4940412926246349439L;
	
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;
	
	@IFieldAnnotation(remark="编号")
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
	
	@Getter @Setter @IFieldAnnotation(remark="所属银行")
	private Emmp_BankInfo bank;

	@Getter @Setter @IFieldAnnotation(remark="名称")
	private String theName;
	
	@Getter @Setter @IFieldAnnotation(remark="简称")
	private String shortName;
	
	@Getter @Setter @IFieldAnnotation(remark="所在地址")
	private String address;
	
	@Getter @Setter @IFieldAnnotation(remark="联系人")
	private String contactPerson;

	@Getter @Setter @IFieldAnnotation(remark="联系电话")
	private String contactPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="负责人")
	private String leader;
	
	@Getter @Setter @IFieldAnnotation(remark="乐观锁")
	private Long version;

	@Getter @Setter @IFieldAnnotation(remark="是否启用")
	private Integer isUsing;
	
	@Getter @Setter @IFieldAnnotation(remark="科目代码")
	private String subjCode;
	
	@Getter @Setter @IFieldAnnotation(remark="大额存单科目代码")
	private String desubjCode;
	
	@Getter @Setter @IFieldAnnotation(remark="保本理财科目代码")
	private String bblcsubjCode;
	
	@Getter @Setter @IFieldAnnotation(remark="结构性存款科目代码")
	private String jgcksubjCode;
	
	@Getter @Setter @IFieldAnnotation(remark="联行号")
	private String interbankCode;
	
	@Getter @Setter @IFieldAnnotation(remark="对接资金系统 下拉选择：1-是0-否")
	private Integer isDocking;
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public List getNeedFieldList(){
		return Arrays.asList("theName");
	}
}
