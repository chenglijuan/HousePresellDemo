package zhishusz.housepresell.service;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_BankAccountEscrowedForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_BankAccountEscrowedDao;
import zhishusz.housepresell.database.po.Tgxy_BankAccountEscrowed;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service销户：托管账户
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_BankAccountEscrowedAccountCloseService {
	@Autowired
	private Tgxy_BankAccountEscrowedDao tgxy_BankAccountEscrowedDao;
	@Autowired
	private Sm_UserDao sm_UserDao;

	// 附件相关
	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;

	public Properties execute(Tgxy_BankAccountEscrowedForm model) {
		Properties properties = new MyProperties();

		String toECode = model.getToECode();
		Long toTableId = model.getToTableId();
		String theName = model.getTheName();
		if (StringUtils.isBlank(toECode)) {
			return MyBackInfo.fail(properties, "请选择要转入的账号");
		}
		Sm_AttachmentForm[] list = model.getGeneralAttachmentList();
		/*if (list.length == 0) {
			return MyBackInfo.fail(properties, "请选择附件");
		}*/
		// 要销户的托管账号主键
		Long tgxy_BankAccountEscrowedId = model.getTableId();
		Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed = (Tgxy_BankAccountEscrowed) tgxy_BankAccountEscrowedDao
				.findById(tgxy_BankAccountEscrowedId);
		if (tgxy_BankAccountEscrowed == null) {
			return MyBackInfo.fail(properties, "该托管账户不存在");
		}
		// tgxy_BankAccountEscrowed.setTableId(tgxy_BankAccountEscrowedId);
		tgxy_BankAccountEscrowed.setHasClosing(1);
		tgxy_BankAccountEscrowed.setClosingTime(System.currentTimeMillis());
		tgxy_BankAccountEscrowed.setClosingPerson(model.getUser());
		tgxy_BankAccountEscrowed.setToECode(toECode);
		//tgxy_BankAccountEscrowed.setTheState(S_TheState.Cache);
		// 转出金额=活期余额
		Double currentBalance = null == tgxy_BankAccountEscrowed.getCurrentBalance() ? 0.00
				: tgxy_BankAccountEscrowed.getCurrentBalance();
		tgxy_BankAccountEscrowed.setTransferOutAmount(currentBalance);
		// 可拨付金额=可拨付金额-活期余额
		Double canPayAmount = null == tgxy_BankAccountEscrowed.getCanPayAmount() ? 0.00
				: tgxy_BankAccountEscrowed.getCanPayAmount();
		tgxy_BankAccountEscrowed.setCanPayAmount(canPayAmount - currentBalance);

		// 活期余额=0
		tgxy_BankAccountEscrowed.setCurrentBalance(0.00);
		tgxy_BankAccountEscrowed.setIsUsing(1);
		tgxy_BankAccountEscrowedDao.update(tgxy_BankAccountEscrowed);

		// 转入账号
		Tgxy_BankAccountEscrowed tgxy_BankAccountEscrowed1 = (Tgxy_BankAccountEscrowed) tgxy_BankAccountEscrowedDao
				.findById(toTableId);
		if (tgxy_BankAccountEscrowed1 == null) {
			return MyBackInfo.fail(properties, "该转入的托管账户不存在");
		}
		// 转入金额=转出金额(活期)
		tgxy_BankAccountEscrowed1.setTransferInAmount(tgxy_BankAccountEscrowed.getTransferOutAmount());
		// 活期=活期+转入(活期)
		Double currentBalance1 = tgxy_BankAccountEscrowed1.getCurrentBalance() == null ? 0.00
				: tgxy_BankAccountEscrowed1.getCurrentBalance();
		tgxy_BankAccountEscrowed1.setCurrentBalance(currentBalance1 + currentBalance);

		// 可拨付金额=可拨付金额+转入
		Double canPayAmount1 = tgxy_BankAccountEscrowed1.getCanPayAmount() == null ? 0.00
				: tgxy_BankAccountEscrowed1.getCanPayAmount();
		tgxy_BankAccountEscrowed1.setCanPayAmount(canPayAmount1 + currentBalance);

		tgxy_BankAccountEscrowedDao.update(tgxy_BankAccountEscrowed1);

		if (list.length > 0) {
			model.setBusiType("200104");
			sm_AttachmentBatchAddService.execute(model, tgxy_BankAccountEscrowed.getTableId());
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
