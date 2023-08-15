package zhishusz.housepresell.service;

import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：退房退款-贷款已结清
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_RefundInfoDeleteService
{
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;

	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;

	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tgpf_RefundInfoId = model.getTableId();

		if (tgpf_RefundInfoId == null || tgpf_RefundInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'tgpf_RefundInfoId'不能为空");
		}

		Tgpf_RefundInfo tgpf_RefundInfo = (Tgpf_RefundInfo) tgpf_RefundInfoDao.findById(tgpf_RefundInfoId);
		if (tgpf_RefundInfo == null)
		{
			return MyBackInfo.fail(properties, "'Tgpf_RefundInfo(Id:" + tgpf_RefundInfoId + ")'不存在");
		}
		tgpf_RefundInfo.setTheState(S_TheState.Deleted);
		tgpf_RefundInfoDao.save(tgpf_RefundInfo);

		// 本次退款金额
		Double refundAmount = tgpf_RefundInfo.getRefundAmount();

		if (refundAmount == null || refundAmount < 1)
		{
			return MyBackInfo.fail(properties, "'refundAmount'不能为空");
		}

		// 查询楼幢账户
		Empj_BuildingInfo building = tgpf_RefundInfo.getBuilding();

		if (building == null)
		{
			return MyBackInfo.fail(properties, "'building'不能为空");
		}

		Long tableId = building.getTableId();

		if (tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "'tableId'不能为空");
		}

		Tgpj_BuildingAccount tgpj_BuildingAccount = tgpj_BuildingAccountDao.findById(tableId);

		if (null == tgpj_BuildingAccount)
		{
			return MyBackInfo.fail(properties, "楼幢账户查询为空");
		}

		// 已申请退款未拨付金额（元）
		Double appliedNoPayoutAmount = tgpj_BuildingAccount.getAppliedNoPayoutAmount();
		// 可拨付金额（元）
		Double allocableAmount = tgpj_BuildingAccount.getAllocableAmount();

		/*
		 * 保存后更新<楼幢账户>：增加“已申请退款未拨付金额（元）”、减少“可拨付金额（元）”
		 */
		// 重新计算楼幢已申请退款未拨付金额（元）
		appliedNoPayoutAmount = MyDouble.getInstance().doubleSubtractDouble(appliedNoPayoutAmount, refundAmount);
		// 重新计算可拨付金额（元）
		allocableAmount = MyDouble.getInstance().doubleAddDouble(allocableAmount, refundAmount);

		tgpj_BuildingAccount.setAppliedNoPayoutAmount(appliedNoPayoutAmount);
		tgpj_BuildingAccount.setAllocableAmount(allocableAmount);
		tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
