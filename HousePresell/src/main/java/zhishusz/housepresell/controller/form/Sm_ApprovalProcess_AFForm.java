package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/*
 * Form表单：审批流-申请单
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_ApprovalProcess_AFForm extends NormalActionForm
{
	private static final long serialVersionUID = 7234585220443321531L;
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
	private Sm_ApprovalProcess_Cfg configuration;//流程配置
	@Getter @Setter
	private Long configurationId;//流程配置-Id
	@Getter @Setter
	private Emmp_CompanyInfo companyInfo;//归属企业
	@Getter @Setter
	private Long companyInfoId;//归属企业-Id
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Long startTimeStamp;//开始时间
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private Long sourceId;//数据源Id
	@Getter @Setter
	private String sourceType;//数据源类型
	@Getter @Setter
	private String orgObjJsonFilePath;//修改前数据Json文件路径
	@Getter @Setter
	private String expectObjJsonFilePath;//修改后数据Json文件路径
	@Getter @Setter
	private List attachmentList;//附件
	@Getter @Setter
	private List workFlowList;//审批流程
	@Getter @Setter
	private Integer currentIndex;//当前审批进度
	@Getter @Setter
	private String  approvalApplyDate; //申请日期

	@Getter @Setter
	private Long endTimeStamp;//申请结束日期
	@Getter @Setter
	private String  isCountPage; //是否分页 0-否
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
}
