package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpf_SpecialFundAppropriated_AFDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：特殊拨付-申请子表
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgpf_SpecialFundAppropriated_AFDtlListService
{
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_SpecialFundAppropriated_AFDtlDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_SpecialFundAppropriated_AFDtlForm model)
	{
		Properties properties = new MyProperties();
		String keyword = model.getKeyword();
		if (null != keyword)
		{
			model.setKeyword("%"+keyword+"%");
		}

		Integer totalCount = tgpf_SpecialFundAppropriated_AFDtlDao.findByPage_Size(tgpf_SpecialFundAppropriated_AFDtlDao
				.getQuery_Size(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), model));

		List<Tgpf_SpecialFundAppropriated_AFDtl> tgpf_SpecialFundAppropriated_AFDtlList;
		if (totalCount > 0)
		{
			tgpf_SpecialFundAppropriated_AFDtlList = tgpf_SpecialFundAppropriated_AFDtlDao
					.findByPage(tgpf_SpecialFundAppropriated_AFDtlDao
							.getQuery(tgpf_SpecialFundAppropriated_AFDtlDao.getBasicHQL(), model));
		}
		else
		{
			tgpf_SpecialFundAppropriated_AFDtlList = new ArrayList<Tgpf_SpecialFundAppropriated_AFDtl>();
		}

		properties.put("tgpf_SpecialFundAppropriated_AFDtlList", tgpf_SpecialFundAppropriated_AFDtlList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
