package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_AFForm;
import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Empj_PaymentBondChildForm;
import zhishusz.housepresell.controller.form.Empj_PaymentGuaranteeChildForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementForm;
import zhishusz.housepresell.database.dao.Empj_BldLimitAmount_AFDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_PaymentBondChildDao;
import zhishusz.housepresell.database.dao.Empj_PaymentGuaranteeChildDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_PaymentBondChild;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/**
 * @category 校验互斥业务
 * @since 2019-8-2 13:59:57
 * @author xsz
 * @version 1.0
 *
 */
@Service
@Transactional
public class CheckMutexService {

	public Properties execute(Tgxy_TripleAgreementForm model) {

		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	
	@Autowired
	private Empj_PaymentGuaranteeChildDao empj_PaymentGuaranteeChildDao;//支付保证子表
	@Autowired
	private Tgpf_FundAppropriated_AFDtlDao tgpf_FundAppropriated_AFDtlDao;//用款申请子表
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;//楼幢
	@Autowired
	private Empj_BldLimitAmount_AFDao empj_BldLimitAmount_AFDao;//受限额度变更
	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;//特殊拨付
	@Autowired
    private Empj_PaymentBondChildDao empj_PaymentBondChildDao;
	
	/**
	 * 支付保证与用款申请与受限额度变更互斥
	 * @param model
	 * @return
	 */
	public Properties checkPaymentGuaranteeApply(Empj_BuildingInfoForm model) {
		
		Properties properties = new MyProperties();
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
//		Long[] buildingInfoIdIdArr = model.getBuildingInfoIdIdArr();
		
		Empj_PaymentGuaranteeChildForm paymentGuaranteeChildForm;
		Tgpf_FundAppropriated_AFDtlForm fundAppropriated_AFDtlForm;
		Empj_BldLimitAmount_AFForm bldLimitAmount_AFForm;
//		for(int i = 0;i < buildingInfoIdIdArr.length;i++){
			
			Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(model.getTableId());
			
			paymentGuaranteeChildForm = new Empj_PaymentGuaranteeChildForm();
			paymentGuaranteeChildForm.setTheState(S_TheState.Normal);
			paymentGuaranteeChildForm.setEmpj_BuildingInfoId(model.getTableId());
			Integer findByPage_Size = empj_PaymentGuaranteeChildDao.findByPage_Size(empj_PaymentGuaranteeChildDao.getQuery_Size(empj_PaymentGuaranteeChildDao.getCheckHQL(), paymentGuaranteeChildForm));
			if(findByPage_Size > 0)
			{
				return MyBackInfo.fail(properties, "楼幢编号："+buildingInfo.geteCodeFromConstruction()+" 已进行支付保证业务！");
			}
			
			fundAppropriated_AFDtlForm = new Tgpf_FundAppropriated_AFDtlForm();
			fundAppropriated_AFDtlForm.setTheState(S_TheState.Normal);
			fundAppropriated_AFDtlForm.setBuildingId(model.getTableId());
			Integer findByPage_Size2 = tgpf_FundAppropriated_AFDtlDao.findByPage_Size(tgpf_FundAppropriated_AFDtlDao.getQuery_Size(tgpf_FundAppropriated_AFDtlDao.getCheckHQL(), fundAppropriated_AFDtlForm));
			if(findByPage_Size2 > 0)
			{
				return MyBackInfo.fail(properties, "楼幢编号："+buildingInfo.geteCodeFromConstruction()+" 已进行用款申请业务！");
			}
			
			bldLimitAmount_AFForm = new Empj_BldLimitAmount_AFForm();
			bldLimitAmount_AFForm.setTheState(S_TheState.Normal);
			bldLimitAmount_AFForm.setBuildingId(model.getTableId());
			
			Integer findByPage_Size3 = empj_BldLimitAmount_AFDao.findByPage_Size(empj_BldLimitAmount_AFDao.getQuery_Size(empj_BldLimitAmount_AFDao.getCheckHQL(), bldLimitAmount_AFForm));
			if(findByPage_Size3 > 0)
			{
				return MyBackInfo.fail(properties, "楼幢编号："+buildingInfo.geteCodeFromConstruction()+" 已进行受限额度变更业务！");
			}
//		}
		return properties;
	}
	
	
	/**
	 * 特殊拨付与受限额度变更互斥
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public Properties checkSpecialFundAppropriated(Empj_BuildingInfoForm model) {
		
		Properties properties = new MyProperties();
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
//		Long[] buildingInfoIdIdArr = model.getBuildingInfoIdIdArr();
		
		Empj_BldLimitAmount_AFForm bldLimitAmount_AFForm;
		Tgpf_SpecialFundAppropriated_AFForm specialFundAppropriated_AFForm;
		Tgpf_FundAppropriated_AFDtlForm fundAppropriated_AFDtlForm;
//		for(int i = 0;i < buildingInfoIdIdArr.length;i++){
			
			Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(model.getTableId());
			
			specialFundAppropriated_AFForm = new Tgpf_SpecialFundAppropriated_AFForm();
			specialFundAppropriated_AFForm.setTheState(S_TheState.Normal);
			specialFundAppropriated_AFForm.setBuildingId(model.getTableId());
			Integer findByPage_Size = tgpf_SpecialFundAppropriated_AFDao.findByPage_Size(tgpf_SpecialFundAppropriated_AFDao.getQuery_Size(tgpf_SpecialFundAppropriated_AFDao.getCheckHQL(), specialFundAppropriated_AFForm));
			if(findByPage_Size > 0)
			{
				return MyBackInfo.fail(properties, "楼幢编号："+buildingInfo.geteCodeFromConstruction()+" 已进行特殊拨付业务，请通过审批后再进行此业务！");
			}
			
			fundAppropriated_AFDtlForm = new Tgpf_FundAppropriated_AFDtlForm();
			fundAppropriated_AFDtlForm.setTheState(S_TheState.Normal);
			fundAppropriated_AFDtlForm.setBuildingId(model.getTableId());
			Integer findByPage_Size2 = tgpf_FundAppropriated_AFDtlDao.findByPage_Size(tgpf_FundAppropriated_AFDtlDao.getQuery_Size(tgpf_FundAppropriated_AFDtlDao.getCheckHQL(), fundAppropriated_AFDtlForm));
			if(findByPage_Size2 > 0)
			{
				return MyBackInfo.fail(properties, "楼幢编号："+buildingInfo.geteCodeFromConstruction()+" 已进行用款申请业务，请通过审批后再进行此业务！");
			}
			
			bldLimitAmount_AFForm = new Empj_BldLimitAmount_AFForm();
			bldLimitAmount_AFForm.setTheState(S_TheState.Normal);
			bldLimitAmount_AFForm.setBuildingId(model.getTableId());
			Integer findByPage_Size3 = empj_BldLimitAmount_AFDao.findByPage_Size(empj_BldLimitAmount_AFDao.getQuery_Size(empj_BldLimitAmount_AFDao.getCheckHQL(), bldLimitAmount_AFForm));
			if(findByPage_Size3 > 0)
			{
				return MyBackInfo.fail(properties, "楼幢编号："+buildingInfo.geteCodeFromConstruction()+" 已进行受限额度变更业务，请通过审批后再进行此业务！");
			}
			
			//支付保函
			Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
            childModel.setTheState(S_TheState.Normal);
            childModel.setEmpj_BuildingInfo(buildingInfo);
            List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao
                .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));
            if (!childList.isEmpty()) {
                
                for (Empj_PaymentBondChild empj_PaymentBondChild : childList) {
                    if(!S_ApprovalState.Completed.equals(empj_PaymentBondChild.getEmpj_PaymentBond().getApprovalState())){
                        return MyBackInfo.fail(properties,
                            "楼幢：" + buildingInfo.geteCodeFromConstruction() + "已发起支付保函申请！");
                    }
                }
                
            }

//		}
		return properties;
	}
	
	/**
	 * 特殊拨付与受限额度变更互斥
	 * @param model
	 * @return
	 */
	public Properties checkFundAppropriated_AFDtl(Empj_BuildingInfoForm model) {
		
		Properties properties = new MyProperties();
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		Empj_BldLimitAmount_AFForm bldLimitAmount_AFForm;
		Tgpf_SpecialFundAppropriated_AFForm specialFundAppropriated_AFForm;
			
			Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(model.getTableId());
			
			specialFundAppropriated_AFForm = new Tgpf_SpecialFundAppropriated_AFForm();
			specialFundAppropriated_AFForm.setTheState(S_TheState.Normal);
			specialFundAppropriated_AFForm.setBuildingId(model.getTableId());
			Integer findByPage_Size = tgpf_SpecialFundAppropriated_AFDao.findByPage_Size(tgpf_SpecialFundAppropriated_AFDao.getQuery_Size(tgpf_SpecialFundAppropriated_AFDao.getCheckHQL(), specialFundAppropriated_AFForm));
			if(findByPage_Size > 0)
			{
				return MyBackInfo.fail(properties, "楼幢编号："+buildingInfo.geteCodeFromConstruction()+" 已进行特殊拨付业务，请通过审批后再进行此业务！");
			}
			
			//支付保函
            Empj_PaymentBondChildForm childModel = new Empj_PaymentBondChildForm();
            childModel.setTheState(S_TheState.Normal);
            childModel.setEmpj_BuildingInfo(buildingInfo);
            List<Empj_PaymentBondChild> childList = empj_PaymentBondChildDao
                .findByPage(empj_PaymentBondChildDao.getQuery(empj_PaymentBondChildDao.getBasicHQL(), childModel));
            if (!childList.isEmpty()) {
                
                for (Empj_PaymentBondChild empj_PaymentBondChild : childList) {
                    if(!S_ApprovalState.Completed.equals(empj_PaymentBondChild.getEmpj_PaymentBond().getApprovalState())){
                        return MyBackInfo.fail(properties,
                            "楼幢：" + buildingInfo.geteCodeFromConstruction() + "已发起支付保函申请！");
                    }
                }
                
            }
			
			/*bldLimitAmount_AFForm = new Empj_BldLimitAmount_AFForm();
			bldLimitAmount_AFForm.setTheState(S_TheState.Normal);
			bldLimitAmount_AFForm.setBuildingId(model.getTableId());
			Integer findByPage_Size3 = empj_BldLimitAmount_AFDao.findByPage_Size(empj_BldLimitAmount_AFDao.getQuery_Size(empj_BldLimitAmount_AFDao.getCheckHQL(), bldLimitAmount_AFForm));
			if(findByPage_Size3 > 0)
			{
				return MyBackInfo.fail(properties, "楼幢编号："+buildingInfo.geteCodeFromConstruction()+" 已进行受限额度变更业务，请通过审批后再进行此业务！");
			}*/
		return properties;
	}
	
}
