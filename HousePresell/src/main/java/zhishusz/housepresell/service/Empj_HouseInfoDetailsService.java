package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_HouseInfoDetailsService
{
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Empj_UnitInfoForm model)
	{
		Properties properties = new MyProperties();
		Empj_UnitInfo outEmpj_UnitInfo=new Empj_UnitInfo();
		
		Long empj_UnitInfoId = model.getTableId();
		Empj_UnitInfo empj_UnitInfo = (Empj_UnitInfo)empj_UnitInfoDao.findById(empj_UnitInfoId);
		if(empj_UnitInfo == null || S_TheState.Deleted.equals(empj_UnitInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_UnitInfo(Id:" + empj_UnitInfoId + ")'不存在");
		}
		
		//单元的id
		outEmpj_UnitInfo.setTableId(empj_UnitInfo.getTableId());
		
		Empj_BuildingInfo building=empj_UnitInfo.getBuilding();
		if(building!=null){
			outEmpj_UnitInfo.setBuilding(building);
			//户关联楼幢中项目的名字
			outEmpj_UnitInfo.getBuilding().theNameOfProject=building.getTheNameOfProject();
			//户关联的楼幢的楼幢施工编号
			outEmpj_UnitInfo.getBuilding().seteCodeFromConstruction(building.geteCodeFromConstruction());			
			//户关联的楼幢的地上层数
			outEmpj_UnitInfo.getBuilding().setUpfloorNumber(building.getUpfloorNumber());
			//户关联的楼幢的地下层数
			outEmpj_UnitInfo.getBuilding().setDownfloorNumber(building.getDownfloorNumber());
			//户关联的楼幢的总户数			
			outEmpj_UnitInfo.getBuilding().setSumFamilyNumber(building.getSumFamilyNumber());					
		}
		//单元名称
		outEmpj_UnitInfo.setTheName(empj_UnitInfo.getTheName());			
		
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		model.setKeyword(null);
				
		Empj_HouseInfoForm  empj_HouseInfoForm=new Empj_HouseInfoForm();
		empj_HouseInfoForm.setUnitInfo(empj_UnitInfo);
		Integer totalCount = empj_HouseInfoDao.findByPage_Size(empj_HouseInfoDao.getQuery_Size(empj_HouseInfoDao.getHouseInfoByUnitHQL(), empj_HouseInfoForm));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Empj_HouseInfo> Empj_HouseInfoList=new ArrayList<Empj_HouseInfo>();
		List<Empj_HouseInfo> empj_HouseInfoList;
		if(totalCount > 0)
		{
			empj_HouseInfoList = empj_HouseInfoDao.findByPage(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getHouseInfoByUnitHQL(), empj_HouseInfoForm), pageNumber, countPerPage);
		
			for(Empj_HouseInfo HouseInfoList:empj_HouseInfoList){
				//获取户列表每一行展示的数据
				Empj_HouseInfo newempj_HouseInfo=new Empj_HouseInfo();
				//户的id
				newempj_HouseInfo.setTableId(HouseInfoList.getTableId());				
				//室号
				newempj_HouseInfo.setRoomId(HouseInfoList.getRoomId());
				//房屋坐落
				newempj_HouseInfo.setPosition(HouseInfoList.getPosition());
				//所在楼层
				newempj_HouseInfo.setFloor(HouseInfoList.getFloor());
				//建筑面积（预测）（㎡）
				newempj_HouseInfo.setForecastArea(HouseInfoList.getForecastArea());
				//分摊面积（预测）（㎡）
				newempj_HouseInfo.setShareConsArea(HouseInfoList.getShareConsArea());
				//套内面积（预测）（㎡）
				newempj_HouseInfo.setInnerconsArea(HouseInfoList.getInnerconsArea());
				//房屋用途
				newempj_HouseInfo.setPurpose(HouseInfoList.getPurpose());
				//物价备案价格
				newempj_HouseInfo.setRecordPrice(HouseInfoList.getRecordPrice());
				//物价备案时间
				newempj_HouseInfo.setLastTimeStampSyncRecordPriceToPresellSystem(HouseInfoList.getLastTimeStampSyncRecordPriceToPresellSystem());
				//房屋状态
				newempj_HouseInfo.setTheHouseState(HouseInfoList.getTheHouseState());
				//托管状态
				newempj_HouseInfo.setBusiState(HouseInfoList.getBusiState());				
				//三方协议编号
				if(HouseInfoList.getTripleAgreement()!=null){
					newempj_HouseInfo.setTripleAgreement(HouseInfoList.getTripleAgreement());
					newempj_HouseInfo.getTripleAgreement().seteCodeOfTripleAgreement(HouseInfoList.getTripleAgreement().geteCodeOfTripleAgreement());
				}										
				Empj_HouseInfoList.add(newempj_HouseInfo);
			}		
		}
		else
		{
			Empj_HouseInfoList = new ArrayList<Empj_HouseInfo>();
		}		
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("empj_HouseInfo", outEmpj_UnitInfo);
		properties.put("empj_HouseInfoList", Empj_HouseInfoList);

		return properties;
	}
}
