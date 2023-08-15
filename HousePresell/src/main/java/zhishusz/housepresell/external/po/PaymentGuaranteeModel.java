package zhishusz.housepresell.external.po;

import lombok.Getter;
import lombok.Setter;
/**
 * 支付保证模型
 * @author Administrator
 *
 */
public class PaymentGuaranteeModel {
	
	/*qymc	String	必填	企业名称
	xmmc	String	必填	项目名称
	cbjg	String	必填	承保机构
	cblz	String	必填	承保楼幢
	cbje	String	必填	承保金额
	cbsj	String	必填	承保时间
	fjlb	String	必填	附件类别
	fjm		String  		附件*/
	
	@Getter @Setter
	private String qymc;//企业名称
	
	@Getter @Setter
	private String xmmc;//项目名称
	
	@Getter @Setter
	private String bdhh;//保单涵号
	
	@Getter @Setter
	private String cbjg;//承保机构

	@Getter @Setter
	private String cblz;//承保楼幢
	
	@Getter @Setter
	private String cbje;//承保金额

	@Getter @Setter
	private String cbsj;//承保时间
	
	@Getter @Setter
	private String fjlb;//附件类别
	
	@Getter @Setter
	private String fjm;//附件名

	@Override
	public String toString() {
		return "qymc=" + qymc + "&bdhh=" + bdhh + "&xmmc=" + xmmc + "&cbjg=" + cbjg + "&cblz=" + cblz
				+ "&cbje=" + cbje + "&cbsj=" + cbsj + "&fj=" + fjm;
	}
	

}
