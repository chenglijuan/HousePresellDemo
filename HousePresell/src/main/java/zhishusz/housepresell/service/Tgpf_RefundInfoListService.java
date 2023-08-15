package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_RefundInfoForm;
import zhishusz.housepresell.database.dao.Tgpf_RefundInfoDao;
import zhishusz.housepresell.database.po.Tgpf_RefundInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：退房退款-贷款已结清
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgpf_RefundInfoListService
{
	@Autowired
	private Tgpf_RefundInfoDao tgpf_RefundInfoDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_RefundInfoForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String refundTimeStamp = model.getRefundTimeStamp();

		Integer refundStatus = model.getRefundStatus();

		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%" + keyword + "%");
		}
		else
		{
			model.setKeyword(null);
		}

		if (null != refundStatus && 0 == refundStatus)
		{
			model.setBusiState("待提交");
		}
		
		if (null != refundStatus && 1 == refundStatus)
		{
			model.setBusiState("未备案");
		}
		
		if (null != refundStatus && 2 == refundStatus)
		{
			model.setBusiState("已备案");
		}

		if (null == refundTimeStamp || refundTimeStamp.trim().isEmpty())
		{
			model.setRefundTimeStamp(null);
		}

		model.setTheState(S_TheState.Normal);
		model.setRefundType("0");

		Integer totalCount = tgpf_RefundInfoDao
				.findByPage_Size(tgpf_RefundInfoDao.getQuery_Size(tgpf_RefundInfoDao.getBasicHQL2(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		// 本次退款金额（元）统计
		Double refundAmountCount = 0.000;

		// 本次退款金额（元）统计
		Double actualRefundAmountCount = 0.000;

		List<Tgpf_RefundInfo> tgpf_RefundInfoList;
		if (totalCount > 0)
		{
			tgpf_RefundInfoList = tgpf_RefundInfoDao.findByPage(
					tgpf_RefundInfoDao.getQuery(tgpf_RefundInfoDao.getBasicHQL2(), model), pageNumber, countPerPage);

			if (null != tgpf_RefundInfoList && tgpf_RefundInfoList.size() > 0)
			{
				for (Tgpf_RefundInfo tgpf_RefundInfo : tgpf_RefundInfoList)
				{
					refundAmountCount = MyDouble.getInstance().doubleAddDouble(refundAmountCount,
							tgpf_RefundInfo.getRefundAmount());
					refundAmountCount = MyDouble.getInstance().getShort(actualRefundAmountCount, 3);
					actualRefundAmountCount = MyDouble.getInstance().doubleAddDouble(actualRefundAmountCount,
							tgpf_RefundInfo.getActualRefundAmount());
					actualRefundAmountCount = MyDouble.getInstance().getShort(actualRefundAmountCount, 3);
				}
			}
		}
		else
		{
			tgpf_RefundInfoList = new ArrayList<Tgpf_RefundInfo>();
		}

		properties.put("tgpf_RefundInfoList", tgpf_RefundInfoList);
		properties.put("refundAmountCount", refundAmountCount);
		properties.put("actualRefundAmountCount", actualRefundAmountCount);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
