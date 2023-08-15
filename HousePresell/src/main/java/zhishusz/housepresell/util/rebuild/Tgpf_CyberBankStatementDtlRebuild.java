package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：网银对账-后台上传的账单原始Excel数据-明细表
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_CyberBankStatementDtlRebuild extends RebuilderBase<Tgpf_CyberBankStatementDtl> {
	@Override
	public Properties getSimpleInfo(Tgpf_CyberBankStatementDtl object) {
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("tradeTimeStamp", object.getTradeTimeStamp());
		properties.put("income", object.getIncome());
		properties.put("recipientAccount", object.getRecipientAccount());
		properties.put("recipientName", object.getRecipientName());
		properties.put("remark", object.getRemark());
		properties.put("reconciliationState", object.getReconciliationState());

		switch (null == object.getSourceType() ? "0" : object.getSourceType()) {
		case "0":
			properties.put("sourceType", "手动");
			break;
		case "1":
			properties.put("sourceType", "接口");
			break;
		default:
			properties.put("sourceType", "手动");
			break;
		}

		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_CyberBankStatementDtl object) {
		if (object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("mainTable", object.getMainTable());
		properties.put("mainTableId", object.getMainTable().getTableId());
		properties.put("tradeTimeStamp", object.getTradeTimeStamp());
		properties.put("recipientAccount", object.getRecipientAccount());
		properties.put("recipientName", object.getRecipientName());
		properties.put("remark", object.getRemark());
		properties.put("income", object.getIncome());
		properties.put("payout", object.getPayout());
		properties.put("balance", object.getBalance());
		properties.put("reconciliationState", object.getReconciliationState());
		properties.put("reconciliationUser", object.getReconciliationUser());
		properties.put("tradeAmount", object.getTradeAmount());
		properties.put("reconciliationStamp", object.getReconciliationStamp());
		properties.put("coreNo", object.getCoreNo());

		switch (null == object.getSourceType() ? "0" : object.getSourceType()) {
		case "0":
			properties.put("sourceType", "手动");
			break;
		case "1":
			properties.put("sourceType", "接口");
			break;
		default:
			properties.put("sourceType", "手动");
			break;
		}

		return properties;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_CyberBankStatementDtl> tgpf_CyberBankStatementDtlList) {
		List<Properties> list = new ArrayList<Properties>();
		if (tgpf_CyberBankStatementDtlList != null) {
			for (Tgpf_CyberBankStatementDtl object : tgpf_CyberBankStatementDtlList) {
				Properties properties = new MyProperties();

				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("mainTable", object.getMainTable());
				properties.put("mainTableId", object.getMainTable().getTableId());
				properties.put("tradeTimeStamp", object.getTradeTimeStamp());
				properties.put("recipientAccount", object.getRecipientAccount());
				properties.put("recipientName", object.getRecipientName());
				properties.put("remark", object.getRemark());
				properties.put("income", object.getIncome());
				properties.put("payout", object.getPayout());
				properties.put("balance", object.getBalance());
				properties.put("reconciliationState", object.getReconciliationState());
				properties.put("reconciliationUser", object.getReconciliationUser());
				properties.put("tradeAmount", object.getTradeAmount());
				properties.put("reconciliationStamp", object.getReconciliationStamp());
				properties.put("coreNo", object.getCoreNo());

				switch (null == object.getSourceType() ? "0" : object.getSourceType()) {
				case "0":
					properties.put("sourceType", "手动");
					break;
				case "1":
					properties.put("sourceType", "接口");
					break;
				default:
					properties.put("sourceType", "手动");
					break;
				}

				list.add(properties);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List getDetailList(List<Tgpf_CyberBankStatementDtl> tgpf_CyberBankStatementDtlList) {
		List<Properties> list = new ArrayList<Properties>();
		if (tgpf_CyberBankStatementDtlList != null) {
			for (Tgpf_CyberBankStatementDtl object : tgpf_CyberBankStatementDtlList) {
				Properties properties = new MyProperties();

				properties.put("tableId", object.getTableId());
				properties.put("theState", object.getTheState());
				// properties.put("busiState", object.getBusiState());
				// properties.put("eCode", object.geteCode());
				// properties.put("userStart", object.getUserStart());
				// properties.put("userStartId",
				// object.getUserStart().getTableId());
				// properties.put("createTimeStamp",
				// object.getCreateTimeStamp());
				// properties.put("lastUpdateTimeStamp",
				// object.getLastUpdateTimeStamp());
				// properties.put("userRecord", object.getUserRecord());
				// properties.put("userRecordId",
				// object.getUserRecord().getTableId());
				// properties.put("recordTimeStamp",
				// object.getRecordTimeStamp());
				// properties.put("mainTable", object.getMainTable());
				// properties.put("mainTableId",
				// object.getMainTable().getTableId());
				properties.put("tradeTimeStamp", object.getTradeTimeStamp());
				properties.put("recipientAccount", object.getRecipientAccount());
				properties.put("recipientName", object.getRecipientName());
				properties.put("remark", object.getRemark());
				properties.put("income", object.getIncome());
				// properties.put("payout", object.getPayout());
				// properties.put("balance", object.getBalance());
				properties.put("reconciliationState", object.getReconciliationState());
				// properties.put("reconciliationUser",
				// object.getReconciliationUser());
				properties.put("tradeAmount", object.getTradeAmount());
				// properties.put("reconciliationStamp",
				// object.getReconciliationStamp());
				// properties.put("coreNo", object.getCoreNo());
				properties.put("busRecipientName", object.getBusRecipientName());
				properties.put("busTradeTimeStamp", object.getBusTradeTimeStamp());
				properties.put("busIecipientAccount", object.getBusIecipientAccount());
				properties.put("tripleAgreementNum", object.getTripleAgreementNum());
				properties.put("busIncome", object.getBusIncome());
				properties.put("busRemark", object.getBusRemark());
				properties.put("cyBankTripleAgreementNum", object.getCyBankTripleAgreementNum());

				switch (null == object.getSourceType() ? "0" : object.getSourceType()) {
				case "0":
					properties.put("sourceType", "手动");
					break;
				case "1":
					properties.put("sourceType", "接口");
					break;
				default:
					properties.put("sourceType", "手动");
					break;
				}

				list.add(properties);
			}
		}
		return list;
	}
}
