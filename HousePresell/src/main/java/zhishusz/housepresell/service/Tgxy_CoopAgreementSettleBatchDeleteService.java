package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleDtlForm;
import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDtlDao;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettleDtl;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service批量删除：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementSettleBatchDeleteService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{
		Properties properties = new MyProperties();
		
		Long[] idArr = model.getIdArr();
		
		if(idArr == null || idArr.length < 1)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		for(Long tableId : idArr)
		{
			Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = (Tgxy_CoopAgreementSettle)tgxy_CoopAgreementSettleDao.findById(tableId);
			if(tgxy_CoopAgreementSettle == null)
			{
				return MyBackInfo.fail(properties, "'Tgxy_CoopAgreementSettle(Id:" + tableId + ")'不存在");
			}
		
			tgxy_CoopAgreementSettle.setTheState(S_TheState.Deleted);
			tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);
			
			Tgxy_CoopAgreementSettleDtlForm tgxy_CoopAgreementSettleDtlForm = new Tgxy_CoopAgreementSettleDtlForm();
			tgxy_CoopAgreementSettleDtlForm.setMainTable(tgxy_CoopAgreementSettle);
			tgxy_CoopAgreementSettleDtlForm.setTheState(S_TheState.Normal);
			
			int totalCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));
			// 子表
			List<Tgxy_CoopAgreementSettleDtl> tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
			if(totalCount > 0)
			{
				tgxy_CoopAgreementSettleDtlList = tgxy_CoopAgreementSettleDtlDao.findByPage(tgxy_CoopAgreementSettleDtlDao.getQuery(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));	
				
				for(Tgxy_CoopAgreementSettleDtl tgxy_CoopAgreementSettleDtl : tgxy_CoopAgreementSettleDtlList)
				{						
					Tgxy_TripleAgreement tripleAgreement = tgxy_CoopAgreementSettleDtl.getTgxy_TripleAgreement();
					
					Empj_HouseInfo house = tripleAgreement.getHouse();
					
					house.setSettlementStateOfTripleAgreement(0);
					
					empj_HouseInfoDao.save(house);
					
					tgxy_CoopAgreementSettleDtl.setTheState(S_TheState.Deleted);
					
					tgxy_CoopAgreementSettleDtlDao.save(tgxy_CoopAgreementSettleDtl);
				}				
			}
			else
			{
				tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();
			}
			
			//删除审批流
			deleteService.execute(tableId, model.getBusiCode());

		}
		

		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
