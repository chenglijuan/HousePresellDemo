package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼栋每日留存权益计算日志
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_BuildingRemainRightLogListService
{
	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_BuildingRemainRightLogForm model)
	{
		Checker checker = Checker.getInstance();
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		String keyword_Old = model.getKeyword();
		String keyword = checker.checkKeyword(model.getKeyword());
		model.setKeyword(keyword);
		
		if (model.getBillTimeStamp() != null && model.getBillTimeStamp().trim().length() == 0) {
			model.setBillTimeStamp(null);
		}
		
		if (model.getBusiState() != null && model.getBusiState().trim().length() == 0) {
			model.setBusiState(null);
		}
		
		Integer totalCount = tgpf_BuildingRemainRightLogDao.findByPage_Size(tgpf_BuildingRemainRightLogDao.createCriteriaForList(model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_BuildingRemainRightLog> tgpf_BuildingRemainRightLogList;
		if(totalCount > 0)
		{
			tgpf_BuildingRemainRightLogList = tgpf_BuildingRemainRightLogDao.findByPage(tgpf_BuildingRemainRightLogDao.createCriteriaForList(model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_BuildingRemainRightLogList = new ArrayList<Tgpf_BuildingRemainRightLog>();
		}
		properties.put("tgpf_BuildingRemainRightLogList", tgpf_BuildingRemainRightLogList);
		properties.put(S_NormalFlag.keyword, keyword_Old);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}
