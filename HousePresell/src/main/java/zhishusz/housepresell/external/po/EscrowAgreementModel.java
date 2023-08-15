package zhishusz.housepresell.external.po;

import lombok.Getter;
import lombok.Setter;
/**
 * 合作协议模型
 * @author Administrator
 *
 */
public class EscrowAgreementModel {
	
	/*xybh	String	必填	协议编号
	qymc	String	必填	企业名称
	xmmc	String	必填	项目名称
	tglz	String	必填	托管楼幢
	qyrq	String	必填	签约日期
	ssqy	String	必填	所属区域
	qtydsx	String	必填	其他约定事项
	fjm	String	必填	附件名*/
	
	@Getter @Setter
	private String xybh;//协议编号
	
	@Getter @Setter
	private String qymc;//企业名称
	
	@Getter @Setter
	private String xmmc;//项目名称
	
	@Getter @Setter
	private String xmdm;//项目代码
	
	@Getter @Setter
	private String tglz;//托管楼幢

	@Getter @Setter
	private String qyrq;//签约日期
	
	@Getter @Setter
	private String ssqy;//所属区域

	@Getter @Setter
	private String qtydsx;//其他约定事项
	
	@Getter @Setter
	private String fjm;//附件名

	@Override
	public String toString() {
		return "xybh=" + xybh + "&qymc=" + qymc + "&xmdm=" + xmdm + "&xmmc=" + xmmc + "&tglz=" + tglz + "&qyrq="
				+ qyrq + "&ssqy=" + ssqy + "&qtydsx=" + qtydsx + "&fj=" + fjm;
	}
	

}
