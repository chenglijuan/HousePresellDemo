package zhishusz.housepresell.database.po.internal;

import java.util.List;

//可审批的
public interface IApprovable
{
	//主键
	String getSourceType();
	Long getSourceId();
	String getEcodeOfBusiness();

	//获取需要审批的字段列表
	List<String> getPeddingApprovalkey();

	//审批通过后的数据更新
	Boolean updatePeddingApprovalDataAfterSuccess();

	//审批驳回后的数据更新
	Boolean updatePeddingApprovalDataAfterFail();
}
