package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_RetainedRightsForm;
import zhishusz.housepresell.database.dao.Tgpf_RetainedRightsDao;
import zhishusz.housepresell.database.po.Tg_RetainedRightsView2;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：留存权益拨付明细查询
 * create by li
 * 2018/09/18
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tg_RetainedRightsSearchService
{
	@Autowired
	private Tgpf_RetainedRightsDao tgpf_RetainedRightsDao;

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_RetainedRightsForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		String keyword = model.getKeyword();
		String userid = String.valueOf(model.getUserId());// 用户主键
		String enterTimeStampStart = model.getFromDate();// 起始日期
		String enterTimeStampEnd = model.getFromDate();// 结束日期
		String projectId = String.valueOf(model.getProjectId());// 项目ID	

		List<Tg_RetainedRightsView2> tg_RetainedRightsList=new ArrayList<Tg_RetainedRightsView2>();
		
		Map<String, Object> retmap = new HashMap<String, Object>();
		Integer totalPage = 0;
		Integer totalCount = 0;
		try {
//			retmap = tgpf_RetainedRightsDao.getRetainedRightsView(userid, projectId,
//					keyword, enterTimeStampStart, enterTimeStampEnd, pageNumber, countPerPage);
			
			retmap = tgpf_RetainedRightsDao.getRetainedRightsView2(model.getUserId(), model.getKeyword(), model.getCompanyId(), model.getProjectId(), model.getBuildingId(), model.getBilltTimeStampStart(), model.getBilltTimeStampStart(), pageNumber, countPerPage);
			
			totalPage = (Integer) retmap.get("totalPage");
			totalCount = (Integer) retmap.get("totalCount");
			tg_RetainedRightsList = (List<Tg_RetainedRightsView2>) retmap.get("tg_RetainedRightsList");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		properties.put("tg_RetainedRightsList", tg_RetainedRightsList);
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
