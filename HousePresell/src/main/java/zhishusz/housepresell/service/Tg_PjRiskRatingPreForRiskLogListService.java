package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_PjRiskRatingForm;
import zhishusz.housepresell.database.dao.Tg_PjRiskRatingDao;
import zhishusz.housepresell.database.po.Tg_PjRiskRating;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目风险评级
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_PjRiskRatingPreForRiskLogListService
{
	@Autowired
	private Tg_PjRiskRatingDao tg_PjRiskRatingDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_PjRiskRatingForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		//项目主键
		Long projectId = model.getProjectId();

		if (null == projectId || projectId < 1)
		{
			return MyBackInfo.fail(properties, "请选择项目");
		}

		Integer totalCount = tg_PjRiskRatingDao.findByPage_Size(
				tg_PjRiskRatingDao.getQuery_Size(tg_PjRiskRatingDao.getBasicDescoperateDateHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Tg_PjRiskRating> tg_PjRiskRatingList;
		Tg_PjRiskRating riskRating;
		if (totalCount > 0)
		{
			tg_PjRiskRatingList = tg_PjRiskRatingDao.findByPage(
					tg_PjRiskRatingDao.getQuery(tg_PjRiskRatingDao.getBasicDescoperateDateHQL(), model));

			/*
			 * xsz by time 2018-10-16 09:54:08
			 * 对评级为低（2）的，提示：
			 * 根据项目自动带出项目风险评级，只有值为“高”需要输入，值为“中”或“低”，则系统报错提示“该项目风险评级为XX，请确认！”
			 */
			riskRating = tg_PjRiskRatingList.get(0);

			String level = riskRating.getTheLevel();
			
			switch (level)
			{
			case "0":
				//高
				
				break;
			case "1":
				//中
				return MyBackInfo.fail(properties, "该项目风险评级为'中'，请确认！");
			case "2":
				//低
				return MyBackInfo.fail(properties, "该项目风险评级为'低'，请确认！");

			default:
				//其他
				return MyBackInfo.fail(properties, "该项目风险评级为不在正常范围内，请确认！");
				
			}

		}
		else
		{
			return MyBackInfo.fail(properties, "该项目还未进行项目风险评级");
		}

		properties.put("tg_PjRiskRatingDetail", riskRating);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
