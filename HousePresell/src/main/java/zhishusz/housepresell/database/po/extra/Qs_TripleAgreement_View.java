package zhishusz.housepresell.database.po.extra;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;

/**
 * 三方协议视图
 * @ClassName:  Qs_TripleAgreement_View   
 * @Description:TODO   
 * @author: zachary.zhu 
 * @date:   2018年11月26日 下午4:10:00   
 * @version V1.0 
 *
 */
@ITypeAnnotation(remark="三方协议视图")
public class Qs_TripleAgreement_View implements Serializable
{
	private static final long serialVersionUID = -7048450326117202624L;

	@Getter @Setter @IFieldAnnotation(remark="表ID")
	private Long tableId;
	
	@Getter @Setter @IFieldAnnotation(remark="托管账户ID")
	private Long bankAccountEscrowedId;
	
	@Getter @Setter @IFieldAnnotation(remark="项目ID")
	private Long projectId;
	
	@Getter @Setter @IFieldAnnotation(remark="三方协议ID")
	private Long tripleAgreementId;
	
	@Getter @Setter @IFieldAnnotation(remark="资金归集ID")
	private Long depositDetailId;
}