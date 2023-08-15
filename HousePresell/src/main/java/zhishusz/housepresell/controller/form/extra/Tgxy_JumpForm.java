package zhishusz.housepresell.controller.form.extra;

import java.util.List;

import zhishusz.housepresell.controller.form.NormalActionForm;
import zhishusz.housepresell.database.po.Tgxy_BuyerInfo;
import zhishusz.housepresell.database.po.extra.Tb_b_personofcontract;

import lombok.Getter;
import lombok.Setter;

/**
 * 楼盘表跳转参数
 * 
 * @author ZS_DEV_05
 *
 */
public class Tgxy_JumpForm extends NormalActionForm
{

	private static final long serialVersionUID = -1204266897988398092L;
	@Getter
	@Setter
	private String roomid;// 户室主键
	@Getter
	@Setter
	private String buildingid;// 楼幢主键
	@Getter
	@Setter
	private List<Tgxy_BuyerInfo> buyerList;
	//tripleAgreementTimeStamp：协议日期（默认当天，后台直接获取
	@Getter
	@Setter
	private String eCodeOfContractRecord;//：合同备案号
	@Getter
	@Setter
	private String sellerName;//：开发企业（出卖人）
	@Getter
	@Setter
	private String buyerName;//：买受人
	@Getter
	@Setter
	private String escrowCompany;//：托管机构（常州正泰房产居间服务有限公司）
	@Getter
	@Setter
	private Long projectId;//：项目Id
	@Getter
	@Setter
	private String projectName;//：项目名称
	@Getter
	@Setter
	private Long buildingInfoId;//：楼幢Id
	@Getter
	@Setter
	private String eCodeFromConstruction;//：楼幢施工编号
	@Getter
	@Setter
	private String eCodeFromPublicSecurity;//：楼幢公安编号
	@Getter
	@Setter
	private String eCodeFromPresellSystem;//：预售系统楼幢编号
	@Getter
	@Setter
	private Long houseId;//：户室Id
	@Getter
	@Setter
	private String houseRoom;//：户室号
	@Getter
	@Setter
	private Double contractSumPrice;//：合同总价
	@Getter
	@Setter
	private Double buildingArea;//：建筑面积
	@Getter
	@Setter
	private String position;//：房屋坐落
	@Getter
	@Setter
	private String contractSignDate;//：合同签订日期
	@Getter
	@Setter
	private String paymentMethod;//：付款方式
	@Getter
	@Setter
	private String payDate;//：交付日期
	@Getter
	@Setter
	private String eCodeOfBuilding;//：备案系统楼幢编号
	
	////合作协议/////
	@Getter
	@Setter
	private String cityRegionName;//：所属区域
	@Getter
	@Setter
	private String developCompanyName;//：开发企业名称
	

}