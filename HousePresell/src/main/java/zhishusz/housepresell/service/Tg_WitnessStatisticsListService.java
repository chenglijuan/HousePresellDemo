package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_CityRegionInfoForm;
import zhishusz.housepresell.controller.form.Tg_WitnessStatisticsForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Tg_WitnessStatisticsDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Tg_WitnessStatistics;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：见证报告统计表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_WitnessStatisticsListService
{
	@Autowired
	private Tg_WitnessStatisticsDao tg_WitnessStatisticsDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;
	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_WitnessStatisticsForm model)
	{
		Properties properties = new MyProperties();
		//查询区域	
		Sm_CityRegionInfoForm cityRegionInfoForm=new Sm_CityRegionInfoForm(); 
	    List<Sm_CityRegionInfo> sm_CityRegionInfolist=sm_CityRegionInfoDao.getQuery(sm_CityRegionInfoDao.getBasicHQL(),cityRegionInfoForm).getResultList();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		//关键字
		String keyword = model.getKeyword();
		if(null !=keyword && keyword.length()>0){
			model.setKeyword("%" + keyword + "%");
		}else{
			model.setKeyword(null);
		}
		//获取区域名称
		Long projectAreaId=model.getProjectAreaId();
		if(projectAreaId!=null){
			Sm_CityRegionInfo sm_CityRegionInfo=sm_CityRegionInfoDao.findById(projectAreaId);
			if(sm_CityRegionInfo!=null){
				model.setProjectArea(sm_CityRegionInfo.getTheName());
			}else{
				model.setProjectArea(null);
			}
		}		
		//上传报告时间
		String billTimeStamp=model.getBillTimeStamp()==null?null:model.getBillTimeStamp().trim();
		model.setBillTimeStamp(billTimeStamp);
		String endBillTimeStamp=model.getEndBillTimeStamp()==null?null:model.getEndBillTimeStamp().trim();
		model.setEndBillTimeStamp(endBillTimeStamp);
		
		//监理公司名称
		Long supervisionCompanyId= model.getSupervisionCompanyId();
		if(supervisionCompanyId!=null){
			Emmp_CompanyInfo emmp_CompanyInfo=	emmp_CompanyInfoDao.findById(supervisionCompanyId);
			if(emmp_CompanyInfo!=null){
				model.setSupervisionCompany(emmp_CompanyInfo.getTheName());
			}else{
				model.setSupervisionCompany(null);
			}			
		}
		//项目名称
		Long projectNameId= model.getProjectNameId();
		if(projectNameId!=null){
			Empj_ProjectInfo empj_ProjectInfo=empj_ProjectInfoDao.findById(projectNameId);
			if(empj_ProjectInfo!=null){
				model.setProjectName(empj_ProjectInfo.getTheName());
			}else{
				model.setProjectName(null);
			}
		}
				
		Integer totalCount = tg_WitnessStatisticsDao.findByPage_Size(tg_WitnessStatisticsDao.getQuery_Size(tg_WitnessStatisticsDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tg_WitnessStatistics> Tg_WitnessStatisticsList;
		if(totalCount > 0)
		{
			Tg_WitnessStatisticsList = tg_WitnessStatisticsDao.findByPage(tg_WitnessStatisticsDao.getQuery(tg_WitnessStatisticsDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			Tg_WitnessStatisticsList = new ArrayList<Tg_WitnessStatistics>();
		}
		
		properties.put("Tg_WitnessStatisticsList", Tg_WitnessStatisticsList);
		properties.put("Sm_CityRegionInfolist", sm_CityRegionInfolist);
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