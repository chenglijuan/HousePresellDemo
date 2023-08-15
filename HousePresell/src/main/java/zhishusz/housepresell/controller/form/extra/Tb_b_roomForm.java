package zhishusz.housepresell.controller.form.extra;

import zhishusz.housepresell.controller.form.NormalActionForm;

import lombok.Getter;
import lombok.Setter;

/**
 * 中间库-户室取数
 * 
 * @ClassName: Tb_b_roomForm
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月26日 上午10:07:04
 * @version V1.0
 *
 */
public class Tb_b_roomForm extends NormalActionForm
{

	private static final long serialVersionUID = 8669462094907879698L;
	@Getter
	@Setter
	private String type;// 请求类型
	@Getter
	@Setter
	private String ROWGUID;// 户Guid
	@Getter
	@Setter
	private String ROOMID;// 户编号
	@Getter
	@Setter
	private String ROOMVERSION;// 户版本
	@Getter
	@Setter
	private String BUILDINGID;// 幢编号
	@Getter
	@Setter
	private String BUILDINGVERSION;// 幢版本
	@Getter
	@Setter
	private String ROOMNO;// 房号（简称，主要显示）
	@Getter
	@Setter
	private String ROOMLOCATION;// 坐落（全称）
	@Getter
	@Setter
	private String UNITCODE;// 【选项】所在单元（字典971）
	@Getter
	@Setter
	private String FLOORNUMBER;// 所在楼层（起始层）
	@Getter
	@Setter
	private String FLOORNAME;// 楼层名称（逻辑层、名义层、）
	@Getter
	@Setter
	private String FLOORSPAN;// 跃层数
	@Getter
	@Setter
	private String ROOMNUMBER;// 楼盘表显示编码
	@Getter
	@Setter
	private String PLANUSAGE;// 【选项】规划用途（字典210）
	@Getter
	@Setter
	private String ROOMUSAGE;// 【选项】房屋用途（字典208）
	@Getter
	@Setter
	private String PUBLICFACILITYTYPE;// 【选项】公建配套类型（字典209）
	@Getter
	@Setter
	private String AREA_BUILDING;// 建筑面积
	@Getter
	@Setter
	private String AREA_SHARE;// 分摊面积
	@Getter
	@Setter
	private String AREA_IN;// 套内面积
	@Getter
	@Setter
	private String DIAGRAMID;// 分户平面图ID
	@Getter
	@Setter
	private String CREATETIME;// 生成时间
	@Getter
	@Setter
	private String DIETIME;// 消亡时间
	@Getter
	@Setter
	private String SOURCETYPE;// 数据来源（文字描述）
	@Getter
	@Setter
	private String PARENTRELATIONGUID;// 父户GUID（预留给阁楼）
	@Getter
	@Setter
	private String IS_FORMAL;// 是否正式版本（1是；0否）
	@Getter
	@Setter
	private String IS_DELETE;// 是否删除（1是；0否）
	@Getter
	@Setter
	private String STATE;// 【选项】房屋状态（字典302）
	@Getter
	@Setter
	private String LIMIT_STATE;// 被限制次数（默认0为没有限制）
	@Getter
	@Setter
	private String SEQUESTRATION_STATE;// 被处置次数（默认0为没有处置）
	@Getter
	@Setter
	private String CLOSE_STATE;// 被查封次数（默认0为没有查封）
	@Getter
	@Setter
	private String EDITING;// 是否在修改（1:在修改0:未修改）
	@Getter
	@Setter
	private String IS_LOCK;// 操作锁(0:解锁,1:锁住)
	@Getter
	@Setter
	private String ROOMTYPE;// 房屋类型（字典121401，毛坯、精装房）
	@Getter
	@Setter
	private String SURVEYTYPE;// 测绘类型
	@Getter
	@Setter
	private String SURVEYROOMID;// 测绘户id
	@Getter
	@Setter
	private Long tableId;// 托管系统楼幢主键

}