package zhishusz.housepresell.external.po;

import lombok.Getter;
import lombok.Setter;
/**
 * 三方协议模型
 * @author Administrator
 *
 */
public class TripleAgreementModel {
	
	/*htbh	String	必填	合同编号
	xybh	String	必填	协议编号
	qymc	String	必填	企业名称
	xmmc	String	必填	项目名称
	msrxm	String	必填	买受人姓名
	sgzl	String	必填	施工座落
	jzmj	String	必填	建筑面积
	fjm	String	必填	附件
	参数名	类型	描述
	--	Int	返回值“1”表示数据插入成功
	返回值“0”表示数据插入异常*/
	
	@Getter @Setter
	private String htbh;//合同编号
	
	@Getter @Setter
	private String xybh;//协议编号
	
	@Getter @Setter
	private String qymc;//企业名称
	
	@Getter @Setter
	private String xmmc;//项目名称
	
	@Getter @Setter
	private String msrxm;//买受人姓名

	@Getter @Setter
	private String sgzl;//施工座落
	
	@Getter @Setter
	private String jzmj;//建筑面积

	@Getter @Setter
	private String fjm;//附件
	
	/*czfs	String	必填	操作方式
	1-删除
	2-撤销*/
	@Getter @Setter
	private String czfs;//操作方式
	
	/*cljg	String	必填	处理结果
	0-不通过
	1-通过
	clyj	String	必填	处理意见*/
	@Getter @Setter
	private String cljg;//处理结果
	
	@Getter @Setter
	private String clyj;//处理意见
	
	/**
	 * 新增
	 * @return
	 */
	public String toStringAdd() {
		return "htbh=" + htbh + "&xybh=" + xybh + "&qymc=" + qymc + "&xmmc=" + xmmc
				+ "&msrxm=" + msrxm + "&sgzl=" + sgzl + "&jzmj=" + jzmj + "&fj=" + fjm;
	}
	
	/**
	 * 撤销或删除
	 * @return
	 */
	public String toStringDelete() {
		return "xybh=" + xybh + "&gzlx=" + czfs;
	}
	
	

}
