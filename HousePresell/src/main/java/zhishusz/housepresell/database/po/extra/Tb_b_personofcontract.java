package zhishusz.housepresell.database.po.extra;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 中间库买受人信息
 * @ClassName:  Tb_b_personofcontract   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年9月7日 上午10:43:120  
 * @version V1.0 
 *
 */
@Entity
@Table(name="tb_b_personofcontract")
public class Tb_b_personofcontract implements Serializable
{	
	
	private static final long serialVersionUID = 9071615698605926708L;
	
	@Id @Getter @Setter @IFieldAnnotation(remark="合同表rowguid")
	private String contractId;			
	
	@Getter @Setter @IFieldAnnotation(remark="买受人姓名")
	private String buyerName;
	
	@Getter @Setter @IFieldAnnotation(remark="证件类型")
	private String certificateType;
	
	@Getter @Setter @IFieldAnnotation(remark="证件号码")
	private String eCodeOfcertificate;
	
	@Getter @Setter @IFieldAnnotation(remark="联系电话")
	private String contactPhone;
	
	@Getter @Setter @IFieldAnnotation(remark="联系地址")
	private String contactAdress = "-";
	
	@Getter @Setter @IFieldAnnotation(remark="代理人姓名")
	private String agentName = "-";
	
	@Getter @Setter @IFieldAnnotation(remark="代理人证件类型")
	private String agentCertType = "-";
	
	@Getter @Setter @IFieldAnnotation(remark="代理人证件号")
	private String agentCertNumber = "-";
	
	@Getter @Setter @IFieldAnnotation(remark="代理人联系电话")
	private String agentPhone = "-";
	
	@Getter @Setter @IFieldAnnotation(remark="代理人联系地址")
	private String agentAddress = "-";
	
	@Getter @Setter @IFieldAnnotation(remark="合同备案号")
	private String eCodeOfContract;
	
}