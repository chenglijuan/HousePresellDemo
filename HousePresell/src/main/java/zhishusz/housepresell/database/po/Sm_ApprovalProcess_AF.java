package zhishusz.housepresell.database.po;

import java.io.Serializable;
import java.util.List;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import cn.hutool.http.HttpUtil;
import lombok.Getter;
import lombok.Setter;

@ITypeAnnotation(remark="审批流-申请单")
public class Sm_ApprovalProcess_AF implements Serializable
{
	private static final long serialVersionUID = 5329805893318310864L;
	//---------公共字段-Start---------//
	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;

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

	@Getter @Setter @IFieldAnnotation(remark="流程配置")
	private Sm_ApprovalProcess_Cfg configuration;

	@Getter @Setter @IFieldAnnotation(remark="发起该流程的用户角色")
	private Sm_Permission_Role permissionRole;

	@Getter @Setter @IFieldAnnotation(remark="业务编码")
	private String busiCode;

	@Getter @Setter @IFieldAnnotation(remark="业务类型")
	private String busiType;
  
	@Getter @Setter @IFieldAnnotation(remark="归属企业")
	private Emmp_CompanyInfo companyInfo;

	@Getter @Setter @IFieldAnnotation(remark="开始时间")
	private Long startTimeStamp;

	@Getter @Setter @IFieldAnnotation(remark="主题")
	private String theme;

	@Getter @Setter @IFieldAnnotation(remark="状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter @Setter @IFieldAnnotation(remark="业务状态")
	private String busiState;//待提交、审核中、已完结
	
	@Getter @Setter @IFieldAnnotation(remark="数据源Id")
	private Long sourceId;

	@Getter @Setter @IFieldAnnotation(remark="数据源类型")
	private String sourceType;//对应到业务模型名称  po名称

	@Getter @Setter @IFieldAnnotation(remark="修改前数据Json文件路径")
	private String orgObjJsonFilePath;

	@Getter @Setter @IFieldAnnotation(remark="修改后数据Json文件路径")
	private String expectObjJsonFilePath;
	
	@Getter @Setter @IFieldAnnotation(remark="附件")
	private List<Sm_Attachment> attachmentList;

	@Getter @Setter @IFieldAnnotation(remark="审批流程")
	private List<Sm_ApprovalProcess_Workflow> workFlowList;

	@Getter @Setter @IFieldAnnotation(remark="当前审批进度")
	private Long currentIndex;

	@Getter @Setter @IFieldAnnotation(remark="是否需要备案")
	private Integer isNeedBackup;   //是：1 否 0

	@Getter @Setter @IFieldAnnotation(remark="申请人")
	private String applicant;

	@Getter @Setter @IFieldAnnotation(remark="申请机构")
	private String theNameOfCompanyInfo;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	//从OSS-Server读取Json文件内容
	public String getOrgObjJson()
	{
		if(null == this.orgObjJsonFilePath || this.orgObjJsonFilePath.trim().isEmpty()){
			return "{}";
		}else{
			return HttpUtil.get(orgObjJsonFilePath);
		}
		
	}
	public String getExpectObjJson()
	{
		System.out.println(expectObjJsonFilePath);
		if(null == this.expectObjJsonFilePath || this.expectObjJsonFilePath.trim().isEmpty()){
			return "{}";
		}else{
			return HttpUtil.get(expectObjJsonFilePath);
		}
		//档案管理服务器连接异常，请联系系统管理员
		
	}
}
