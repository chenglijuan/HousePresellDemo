package zhishusz.housepresell.database.dao;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.Comm_BankUploadDtlModel;

/*
 * Dao数据库操作：网银数据明细接收模型
 * Company：ZhiShuSZ
 * */
@Repository
public class Comm_BankUploadDtlModelDao extends BaseDao<Comm_BankUploadDtlModel> {
	public String getBasicHQL() {
		return "from Comm_BankUploadDtlModel where 1=1";
	}

}
