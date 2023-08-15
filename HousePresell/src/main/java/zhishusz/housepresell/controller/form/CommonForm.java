package zhishusz.housepresell.controller.form;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AFDtl;

/*
 * Form表单：公共
 * Company：ZhiShuSZ
 * */
@Getter
@Setter
public class CommonForm extends NormalActionForm {

	private static final long serialVersionUID = 3452880086308221214L;

	// 表ID
	private Long tableId;

	// ===工程进度节点更新===
	// 项目ID
	private Long projectId;

	// 企业Id
	private Long companyId;

	// 楼幢Id
	private Long buildId;

	// 楼幢Id
	private Long buildingId;

	// 楼幢预测节点数据
	List<Empj_NodePredictionForm> versionList;

	// 项目
	private Empj_ProjectInfo project;

	// 项目编号
	private String projectCode;

	private Integer theState;

	// 当前建设进度
	private Integer nowBuildNum;

	// 楼幢总层数
	private Integer buildCountNum;

	// 关联当前进度节点
	private Tgpj_BldLimitAmountVer_AFDtl nowNode;

	// 当前建设进度
	private String buildProgress;

	// 建设类型（）
	private String buildProgressType;

	// 巡查时间
	private String forcastTime;

	// 测试字符串
	private String testStr;

	// 合同编号
	private String ContractNO;

	/*
	 * 
	 */
	private String businessCode;

	/*
	 * 单据号
	 */
	private String ts_id;

	/*
	 * 项目Id
	 */
	private String ts_pj_id;

	/*
	 * 楼幢Id
	 */
	private String ts_bld_id;

	/*
	 * 审核结果
	 * 0：不通过 1：审核通过
	 */
	private String actionType;
	
	/*
	 * 审核说明
	 */
	private String approvalInfo;

	@Override
	public String toString() {
		return "CommonForm [tableId=" + tableId + ", businessCode=" + businessCode + ", ts_id=" + ts_id + ", ts_pj_id="
				+ ts_pj_id + ", ts_bld_id=" + ts_bld_id + ", actionType=" + actionType + ", approvalInfo="
				+ approvalInfo + "]";
	}
	
	
	
}
