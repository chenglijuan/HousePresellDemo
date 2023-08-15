package zhishusz.housepresell.controller.form.extra;

import zhishusz.housepresell.controller.form.NormalActionForm;

import lombok.Getter;
import lombok.Setter;

public class Tb_b_buildingForm extends NormalActionForm
{

	private static final long serialVersionUID = -1652200554152850226L;
	@Getter
	@Setter
	private String type;// 请求类型
	@Getter
	@Setter
	private String ROWGUID;// 幢Guid
	@Getter
	@Setter
	private String BUILDINGID;// 幢编号
	@Getter
	@Setter
	private String BUILDINGVERSION;// 幢版本
	@Getter
	@Setter
	private String PROJECTID;// 项目ID
	@Getter
	@Setter
	private String PROJECTVERSION;// 项目版本
	@Getter
	@Setter
	private String BUILDINGNO;// 幢号
	@Getter
	@Setter
	private String BUILDINGLOCATION;// 幢坐落
	@Getter
	@Setter
	private String GISLANDNO;// 测绘报告丘号
	@Getter
	@Setter
	private String GISBUILDINGNO;// 测绘报告幢号
	@Getter
	@Setter
	private String AREA_TOTAL;// 幢总建筑面积
	@Getter
	@Setter
	private String AREA_IN;// 幢总套内面积
	@Getter
	@Setter
	private String AREA_SHARE;// 幢总公摊面积
	@Getter
	@Setter
	private String FLOORS_UP;// 地上层数
	@Getter
	@Setter
	private String FLOORS_DOWN;// 地下层数
	@Getter
	@Setter
	private String UNITCOUNT;// 单元数
	@Getter
	@Setter
	private String SURVEYTYPE;// 【枚举】测绘类型（字典202）
	@Getter
	@Setter
	private String SOURCETYPE;// 数据来源类型（文字）
	@Getter
	@Setter
	private String BUILDING_BAIDU;// 幢地图坐标范围(百度)
	@Getter
	@Setter
	private String BUILDING_GIS;// 幢地图坐标范围(GIS)
	@Getter
	@Setter
	private String FILEID;// 附件id（多附件）
	@Getter
	@Setter
	private String REMARK;// 备注
	@Getter
	@Setter
	private String UNITINFO;// 住宅单元信息（例如：2,2,2 表示3个单元，每个单元2房间）
	@Getter
	@Setter
	private String CREATETIME;// 生成时间
	@Getter
	@Setter
	private String DIETIME;// 消亡时间
	@Getter
	@Setter
	private String STATE;// 幢状态（启用、禁用，以去除）
	@Getter
	@Setter
	private String IS_DELETE;// 是否删除（1是；0否）
	@Getter
	@Setter
	private String IS_FORMAL;// 是否正式（1是；0否）
	@Getter
	@Setter
	private String EDITING;// 是否编辑（预留）
	@Getter
	@Setter
	private String SURVEYBUILDINGID;// 测绘幢id
	@Getter
	@Setter
	private String CREATEUSER;// 创建人
	@Getter
	@Setter
	private String UPDATETIME;// 更新时间
	@Getter
	@Setter
	private String UPDATEUSER;// 更新人
	@Getter
	@Setter
	private String saveBuildingList;// 保存楼幢信息
	@Getter
	@Setter
	private String projectLocalId;//选择本地项目id

}