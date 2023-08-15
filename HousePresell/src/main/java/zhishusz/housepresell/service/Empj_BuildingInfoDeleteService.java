package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.controller.form.Empj_UnitInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingExtendInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.po.Empj_BuildingExtendInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：楼幢-基础信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingInfoDeleteService
{
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	@Autowired
	private Empj_BuildingExtendInfoDao empj_BuildingExtendInfoDao;
	
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;
	
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;
	
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;//户室
	
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;//单元

	@SuppressWarnings("unchecked")
	public Properties execute(Empj_BuildingInfoForm model)
	{
		Properties properties = new MyProperties();

		Long[] idArr = model.getIdArr();
		
		String busiCode = "03010201";//楼幢信息初始维护（含楼幢、单元、户室信息维护）
		
		if(idArr == null || idArr.length <= 0)
		{
			return MyBackInfo.fail(properties, "请选择需要删除的楼幢");
		}
		
		for(Long empj_BuildingInfoId : idArr)
		{
			Empj_BuildingInfo empj_BuildingInfo = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(empj_BuildingInfoId);
			
			if(empj_BuildingInfo == null)
			{
				return MyBackInfo.fail(properties, "删除的楼幢不存在");
			}
			
			//如果是已备案则不能删除
			if(S_BusiState.HaveRecord.equals(empj_BuildingInfo.getBusiState()))
			{
				return MyBackInfo.fail(properties, "删除的楼幢是已备案的不能删除");
			}
			
			if(S_ApprovalState.Examining.equals(empj_BuildingInfo.getApprovalState()))
			{
				return MyBackInfo.fail(properties, "删除的楼幢是审核中的不能删除");
			}
			
			String escrowState = null;
			if(empj_BuildingInfo.getExtendInfo() != null)
			{
				escrowState = empj_BuildingInfo.getExtendInfo().getEscrowState();
			}
			
			if(S_EscrowState.HasEscrowState.equals(escrowState) || 
					S_EscrowState.ApprovalEscrowState.equals(escrowState) ||
					S_EscrowState.EndEscrowState.equals(escrowState))
			{
				return MyBackInfo.fail(properties, "已托管、申请托管终止、托管终止的楼幢在不能删除");
			}
			empj_BuildingInfo.setTheState(S_TheState.Deleted);
			empj_BuildingInfoDao.save(empj_BuildingInfo);

			//同时删除子表信息和附件
			Empj_BuildingExtendInfo empj_BuildingExtendInfo = empj_BuildingInfo.getExtendInfo();
			empj_BuildingExtendInfo.setTheState(S_TheState.Deleted);
			
			empj_BuildingExtendInfoDao.save(empj_BuildingExtendInfo);
			
			Tgpj_BuildingAccount tgpj_BuildingAccount = empj_BuildingInfo.getBuildingAccount();
			tgpj_BuildingAccount.setTheState(S_TheState.Deleted);
			
			tgpj_BuildingAccountDao.save(tgpj_BuildingAccount);
			
			/*
			 * xsz by time 2018-12-14 09:56:14
			 * 删除单元及户室信息
			 */
			Empj_UnitInfoForm unitModel = new Empj_UnitInfoForm();
			unitModel.setTheState(S_TheState.Normal);
			unitModel.setBuilding(empj_BuildingInfo);
			List<Empj_UnitInfo> unitList;
			unitList = empj_UnitInfoDao.findByPage(empj_UnitInfoDao.getQuery(empj_UnitInfoDao.getBasicHQL(), unitModel));
			if(null!=unitList&&unitList.size()>0)
			{
				for (Empj_UnitInfo empj_UnitInfo : unitList)
				{
					empj_UnitInfo.setTheState(S_TheState.Deleted);
					empj_UnitInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
					empj_UnitInfo.setUserUpdate(model.getUser());
					empj_UnitInfoDao.save(empj_UnitInfo);
				}
			}
			
			
			Empj_HouseInfoForm houseModel = new Empj_HouseInfoForm();
			houseModel.setTheState(S_TheState.Normal);
			houseModel.setBuildingId(empj_BuildingInfoId);
			List<Empj_HouseInfo> houseList;
			houseList = empj_HouseInfoDao.findByPage(empj_HouseInfoDao.getQuery(empj_HouseInfoDao.getBuildingHQL(), houseModel));
			if(null!=houseList&&houseList.size()>0)
			{
				for (Empj_HouseInfo empj_HouseInfo : houseList)
				{
					empj_HouseInfo.setTheState(S_TheState.Deleted);
					empj_HouseInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
					empj_HouseInfo.setUserUpdate(model.getUser());
					empj_HouseInfoDao.save(empj_HouseInfo);
				}
			}
			
			
			//删除审批流
			deleteService.execute(empj_BuildingInfoId, busiCode);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
