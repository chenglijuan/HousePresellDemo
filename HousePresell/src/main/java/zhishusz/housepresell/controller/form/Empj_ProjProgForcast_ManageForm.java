package zhishusz.housepresell.controller.form;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_DTL;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 工程进度巡查管理
 * 
 * @author xsz
 * @since 2021-4-14 13:17:25
 */
@ITypeAnnotation(remark = "工程进度巡查管理")
public class Empj_ProjProgForcast_ManageForm extends NormalActionForm {

	private static final long serialVersionUID = -730979148793070326L;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "表ID", isPrimarykey = true)
	private Long tableId;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "状态 S_TheState 初始为Normal")
	private Integer theState;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "业务状态")
	private String busiState;

	@IFieldAnnotation(remark = "编号")
	private String eCode;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "创建人")
	private Sm_User userStart;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "创建时间")
	private Long createTimeStamp;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "修改人")
	private Sm_User userUpdate;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "最后修改日期")
	private Long lastUpdateTimeStamp;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "备案人")
	private Sm_User userRecord;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "备案日期")
	private Long recordTimeStamp;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "巡查单号")
	private String code;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "关联主表信息")
	private Empj_ProjProgForcast_AF afEntity;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "主表Id")
	private Long afEntityId;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "关联子表信息")
	private Empj_ProjProgForcast_DTL dtlEntity;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "子表Id")
	private Long dtlEntityId;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "提交日期")
	private String submitDate;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "巡查时间")
	private String forcastTime;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "巡查人")
	private String forcastPeople;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "所属区域")
	private String areaName;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "关联区域")
	private Sm_CityRegionInfo area;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "区域Id")
	private Long areaId;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "项目名称")
	private String projectName;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "关联项目")
	private Empj_ProjectInfo project;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "项目Id")
	private Long projectId;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "关联楼幢")
	private Empj_BuildingInfo buildingInfo;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "楼幢Id")
	private Long buildId;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "楼幢编号")
	private String buildCode;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "备注")
	private String remark;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "流程状态 待提交/审核中/已完结")
	private String approvalState;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "申请时间")
	private Long applyDate;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "图片处理状态 1-已处理 ")
	private String handleState;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "网站推送状态")
	private String webPushState;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "网站审核状态")
	private String webHandelState;

	@Getter
	@Setter
	@IFieldAnnotation(remark = "网站审核说明")
	private String webHandelInfo;
	
	@Getter
    @Setter
    private List<Map<String, Object>> attachementList;

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

}
