package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Empj_HouseInfoListsService
{
	
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		if(keyword!=""&& keyword.length()>0){
			model.setKeyword("%" + keyword + "%");
		}else{
			model.setKeyword(null);
		}
		if(null!=model.getProjectName()&& !model.getProjectName().trim().isEmpty()) {
			model.setProjectName(model.getProjectName());
		}else {
			model.setProjectName(null);				
		}
		if(null!=model.getBuildingId() && model.getBuildingId()>0) {
			model.setBuildingId(model.getBuildingId());
		}else {
			model.setBuildingId(null);
		}
		
		//获取没有删除的信息
		model.setTheState(S_TheState.Normal);
		Integer totalCount = empj_UnitInfoDao.findByPage_Size(empj_UnitInfoDao.getQuery_Size(empj_UnitInfoDao.getBasicHQLByLIke(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_UnitInfo> Empj_UnitInfoList=new ArrayList<Empj_UnitInfo>();
		List<Empj_UnitInfo> empj_UnitInfoList;
		if(totalCount > 0)
		{
			empj_UnitInfoList = empj_UnitInfoDao.findByPage(empj_UnitInfoDao.getQuery(empj_UnitInfoDao.getBasicHQLByLIke(), model), pageNumber, countPerPage);
		
			//根据查到的户的信息关联单元和楼幢的基础信息
			for(Empj_UnitInfo empj_UnitInfo:empj_UnitInfoList){
				//获取单元列表每一行展示的数据
				Empj_UnitInfo newempj_UnitInfo=new Empj_UnitInfo();
				//单元的id
				newempj_UnitInfo.setTableId(empj_UnitInfo.getTableId());
				
				Empj_BuildingInfo building=empj_UnitInfo.getBuilding();
				if(building!=null){
					newempj_UnitInfo.setBuilding(building);
					//单元项目的名字
					newempj_UnitInfo.getBuilding().theNameOfProject=building.getTheNameOfProject();
					//户关联的楼幢的楼幢施工编号
					newempj_UnitInfo.getBuilding().seteCodeFromConstruction(building.geteCodeFromConstruction());
					//户关联的楼幢的楼幢坐落
					newempj_UnitInfo.getBuilding().setPosition(building.getPosition());			
					//户关联的楼幢的地上层数
					newempj_UnitInfo.getBuilding().setUpfloorNumber(building.getUpfloorNumber());
					//户关联的楼幢的地下层数
					newempj_UnitInfo.getBuilding().setDownfloorNumber(building.getDownfloorNumber());
					//户关联的楼幢的总户数			
					newempj_UnitInfo.getBuilding().setSumFamilyNumber(building.getSumFamilyNumber());					
				}else{		
					Empj_BuildingInfo buildingInfo=new Empj_BuildingInfo();
					newempj_UnitInfo.setBuilding(buildingInfo);
					//单元项目的名字
					newempj_UnitInfo.getBuilding().theNameOfProject="";
					//户关联的楼幢的楼幢施工编号
					newempj_UnitInfo.getBuilding().seteCodeFromConstruction("");
					//户关联的楼幢的楼幢坐落
					newempj_UnitInfo.getBuilding().setPosition("");			
					//户关联的楼幢的地上层数
					newempj_UnitInfo.getBuilding().setUpfloorNumber(0d);
					//户关联的楼幢的地下层数
					newempj_UnitInfo.getBuilding().setDownfloorNumber(0d);
					//户关联的楼幢的总户数			
					newempj_UnitInfo.getBuilding().setSumFamilyNumber(0);
					
				}								
					//户关联的单元的单元名称
				newempj_UnitInfo.setTheName(empj_UnitInfo.getTheName());		
				Empj_UnitInfoList.add(newempj_UnitInfo);
			}
		}
		else
		{
			Empj_UnitInfoList = new ArrayList<Empj_UnitInfo>();
		}		
		properties.put("empj_UnitInfoList", Empj_UnitInfoList);
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
