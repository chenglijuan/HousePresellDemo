package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zhishusz.housepresell.controller.form.Tgpf_BankUploadDataDetailForm;
import zhishusz.housepresell.controller.form.Tgpf_DepositDetailForm;
import zhishusz.housepresell.database.dao.Tgpf_BankUploadDataDetailDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Tgpf_BankUploadDataDetail;
import zhishusz.housepresell.database.po.Tgpf_DepositDetail;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
/*
 * Service列表查询：业务对账-详情页-删除
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_DepositDetailContrastDeleteService {

	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;
	@Autowired
	private Tgpf_BankUploadDataDetailDao tgpf_BankUploadDataDetailDao;// 银行对账单
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_DepositDetailForm model)
	{
		Properties properties = new MyProperties();

		Long[] idArr= model.getIdArr();
		
		if( null == idArr || idArr.length < 0)
		{
			return MyBackInfo.fail(properties, "请选择需要删除的对账单");
		}
		
		for(int i = 0; i< idArr.length;i++)
		{
			Long tgpf_DepositDetailId = idArr[i];

			Tgpf_DepositDetail tgpf_DepositDetail = (Tgpf_DepositDetail) tgpf_DepositDetailDao
					.findById(tgpf_DepositDetailId);

			// 银行平台流水号
			String eCodeFromBankPlatform = tgpf_DepositDetail.geteCodeFromBankPlatform();

			// 查询日终结算
			// 查询条件： 1.银行流水号 2.状态：正常
			Tgpf_BankUploadDataDetailForm tgpf_BankUploadDataDetailForm = new Tgpf_BankUploadDataDetailForm();
			tgpf_BankUploadDataDetailForm.setBkpltNo(eCodeFromBankPlatform);
			tgpf_BankUploadDataDetailForm.setTheState(S_TheState.Normal);

			Integer totalCount = tgpf_BankUploadDataDetailDao.findByPage_Size(tgpf_BankUploadDataDetailDao
					.getQuery_Size(tgpf_BankUploadDataDetailDao.getBasicHQL(), tgpf_BankUploadDataDetailForm));
			List<Tgpf_BankUploadDataDetail> tgpf_BankUploadDataDetailList;

			Tgpf_BankUploadDataDetail tgpf_BankUploadDataDetail = new Tgpf_BankUploadDataDetail();
			if (totalCount > 0) {
				tgpf_BankUploadDataDetailList = tgpf_BankUploadDataDetailDao.findByPage(tgpf_BankUploadDataDetailDao
						.getQuery(tgpf_BankUploadDataDetailDao.getBasicHQL(), tgpf_BankUploadDataDetailForm));
				tgpf_BankUploadDataDetail = tgpf_BankUploadDataDetailList.get(0);

				tgpf_BankUploadDataDetail.setTheState(S_TheState.Deleted);

				tgpf_BankUploadDataDetailDao.save(tgpf_BankUploadDataDetail);
			}
		}
		

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
