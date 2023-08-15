package zhishusz.housepresell.controller.form;

import lombok.Getter;
import lombok.Setter;

/*
 * Form表单：接收网站审核返回参数
 * Company：ZhiShuSZ
 * */
@Getter
@Setter
public class ApprovalForm extends NormalActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -288570182730233647L;

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
		return "ApprovalForm [businessCode=" + businessCode + ", ts_id=" + ts_id + ", ts_pj_id="
				+ ts_pj_id + ", ts_bld_id=" + ts_bld_id + ", actionType=" + actionType + ", approvalInfo="
				+ approvalInfo + "]";
	}
	
	
}
