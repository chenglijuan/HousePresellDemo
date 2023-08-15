package zhishusz.housepresell.database.po.extra;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;

/**
 * 合同模型
 * @author ZS
 *
 */
public class ResponseContract {

	@Getter
	@Setter
	@IFieldAnnotation(remark = "合同备案号")
	private String contractNo;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "合同编号")
	private String recordNo;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "房屋坐落")
	private String position;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "买受人")
	private String buyer;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "出卖人")
	private String seller;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "建筑面积")
	private String roomArea;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "项目id")
	private String projectId;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "楼幢id")
	private String buildingId;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "房屋id")
	private String roomId;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "签订日期")
	private String signingDate;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "合同金额")
	private String contractAmount;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "付款方式")
	private String paymentMethod;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "首付款金额")
	private String downPayment;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "贷款金额")
	private String loanAmount;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "交付日期")
	private String deliveryDate;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "买受人姓名")
	private String buyerName;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "买受人证件类型")
	private String buyerCardType;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "买受人证件号")
	private String buyerCardNo;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "买受人联系电话")
	private String buyerPhone;
	@Getter
	@Setter
	@IFieldAnnotation(remark = "买受人联系地址")
	private String buyerAddress;
	
	@Override
	public String toString() {
		return "ResponseContract [contractNo=" + contractNo + ", recordNo=" + recordNo + ", position=" + position
				+ ", buyer=" + buyer + ", seller=" + seller + ", roomArea=" + roomArea + ", projectId=" + projectId
				+ ", buildingId=" + buildingId + ", roomId=" + roomId + ", signingDate=" + signingDate
				+ ", contractAmount=" + contractAmount + ", paymentMethod=" + paymentMethod + ", downPayment="
				+ downPayment + ", loanAmount=" + loanAmount + ", deliveryDate=" + deliveryDate + ", buyerName="
				+ buyerName + ", buyerCardType=" + buyerCardType + ", buyerCardNo=" + buyerCardNo + ", buyerPhone="
				+ buyerPhone + ", buyerAddress=" + buyerAddress + "]";
	}
	
	

}
