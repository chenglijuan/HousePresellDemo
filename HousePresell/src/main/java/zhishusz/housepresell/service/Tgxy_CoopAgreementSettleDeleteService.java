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
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个删除：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_CoopAgreementSettleDeleteService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Tgxy_CoopAgreementSettleDtlDao tgxy_CoopAgreementSettleDtlDao;
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;

	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{
		Properties properties = new MyProperties();
		
		// 子表
		List<Tgxy_CoopAgreementSettleDtl> tgxy_CoopAgreementSettleDtlList = new ArrayList<Tgxy_CoopAgreementSettleDtl>();

		Long[] idArr =  model.getIdArr();
		for(int i = 0 ; i<idArr.length ; i++)
		{
			Long tgxy_CoopAgreementSettleId = model.getTableId();
			
			Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle = (Tgxy_CoopAgreementSettle)tgxy_CoopAgreementSettleDao.findById(tgxy_CoopAgreementSettleId);
			if(tgxy_CoopAgreementSettle == null)
			{
				return MyBackInfo.fail(properties, "'Tgxy_CoopAgreementSettle(Id:" + tgxy_CoopAgreementSettleId + ")'不存在");
			}
			if(tgxy_CoopAgreementSettle.getSettlementState()==1 || tgxy_CoopAgreementSettle.getSettlementState()==2)
			{
				continue;
			}		
			tgxy_CoopAgreementSettle.setTheState(S_TheState.Deleted);
			
			tgxy_CoopAgreementSettleDao.save(tgxy_CoopAgreementSettle);
			
			Tgxy_CoopAgreementSettleDtlForm tgxy_CoopAgreementSettleDtlForm = new Tgxy_CoopAgreementSettleDtlForm();
			tgxy_CoopAgreementSettleDtlForm.setMainTable(tgxy_CoopAgreementSettle);
			tgxy_CoopAgreementSettleDtlForm.setTheState(S_TheState.Normal);
			
			Integer totalCount = tgxy_CoopAgreementSettleDtlDao.findByPage_Size(tgxy_CoopAgreementSettleDtlDao.getQuery_Size(tgxy_CoopAgreementSettleDtlDao.getBasicHQL(), tgxy_CoopAgreementSettleDtlForm));
			
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
			
			
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
