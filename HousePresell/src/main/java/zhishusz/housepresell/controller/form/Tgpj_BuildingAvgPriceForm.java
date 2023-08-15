package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：楼幢-备案均价
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpj_BuildingAvgPriceForm extends NormalActionForm
{
	private static final long serialVersionUID = -981195213018642642L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//最后修改人人
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
	private Double recordAveragePrice;//备案均价
	@Getter @Setter
	private Empj_BuildingInfo buildingInfo;//关联楼幢
	@Getter @Setter
	private Long buildingInfoId;//关联楼幢-Id
	@Getter @Setter
	private String buildingInfoString;//关联楼幢-Json String
	@Getter @Setter
	private Long averagePriceRecordDate;//物价备案日期
	@Getter @Setter
	private String averagePriceRecordDateString;//物价备案日期
	@Getter @Setter
	private Double recordAveragePriceFromPresellSystem;//预售系统楼幢住宅备案均价
	@Getter @Setter
	private Long projectId;
	@Getter @Setter
	private Long companyId;
	@Getter @Setter
	private String remark;//备注
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}
	
	
}
