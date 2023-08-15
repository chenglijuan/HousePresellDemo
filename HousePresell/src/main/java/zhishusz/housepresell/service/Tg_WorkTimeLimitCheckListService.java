package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.*;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
 * Service列表查询：工作时限检查
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_WorkTimeLimitCheckListService
{
	@Autowired
	private Sm_ApprovalProcess_AFListService sm_approvalProcess_afListService;
	@Autowired
	private Tg_HandleTimeLimitConfigListService tg_handleTimeLimitConfigListService;
	@Autowired
	private Tg_HolidayListService tg_holidayListService;
	@Autowired
	private Tgpf_FundAppropriated_AFDao tgpf_FundAppropriated_AFDao;
	@Autowired
	private Tgpf_DepositDetailListService tgpf_depositDetailListService;

	public static final String[] TypeNames = new String[] {"合作协议", "三方协议", "受限额度变更","工程进度节点更新", "托管终止", "资金归集", "托管资金一般拨付", "退房退款", "支付保证"};
	public static final String[] BusiCoeds = new String[] {
			S_BusiCode.busiCode_06110201,
			S_BusiCode.busiCode_06110301,
			S_BusiCode.busiCode_03030101,
			S_BusiCode.busiCode_03030100,
			S_BusiCode.busiCode_03030102,
			"061201",
			S_BusiCode.busiCode19,
			S_BusiCode.busiCode20,
			S_BusiCode.busiCode_06120401};
	public static final Integer[] Types = new Integer[] {Tg_HandleTimeLimitConfig.Type_Tgxy_CoopAgreement,
			Tg_HandleTimeLimitConfig.Type_Tgxy_TripleAgreement,
			Tg_HandleTimeLimitConfig.Type_Empj_BldLimitAmount_AF,
			Tg_HandleTimeLimitConfig.Type_Empj_BldLimitAmount_CH,
			Tg_HandleTimeLimitConfig.Type_Empj_BldEscrowCompleted,
			Tg_HandleTimeLimitConfig.Type_Tgpf_DepositDetail,
			Tg_HandleTimeLimitConfig.Type_Tgpf_FundAppropriated,
			Tg_HandleTimeLimitConfig.Type_Tgpf_RefundInfo,
			Tg_HandleTimeLimitConfig.Type_Empj_PaymentGuaranteeApply};

	@SuppressWarnings("unchecked")
	public Properties execute(Tg_WorkTimeLimitCheckForm model)
	{
		Properties properties = new MyProperties();
		String enableDateSearchStr = model.getDateStr();
		Map<String, Tg_Holiday> tg_holidayMap = new HashMap<String, Tg_Holiday>();
		if(enableDateSearchStr != null && enableDateSearchStr.length() > 0)
		{
			String[] dateArr = enableDateSearchStr.split(" - ");
			model.setStartDateTimeStamp(MyDatetime.getInstance().stringToLong(dateArr[0]));
			model.setEndDateTimeStamp(MyDatetime.getInstance().stringToLong(dateArr[1]) + 24 * 60 * 60 * 1000);

			Tg_HolidayForm form = new Tg_HolidayForm();
			form.setStartDateTime(dateArr[0]);
			long dateEndTime = MyDatetime.getInstance().stringToLong(dateArr[1]);
			form.setEndDateTime(MyDatetime.getInstance().dateToSimpleString(dateEndTime + 30L * 24 *60 * 60 * 1000));
			List<Tg_Holiday> tg_holidays = (List<Tg_Holiday>) tg_holidayListService.execute(form).get("tg_holidays");
			for (Tg_Holiday tg_holiday : tg_holidays) {
				tg_holidayMap.put(tg_holiday.getDateTime(), tg_holiday);
			}
		}

		List<Tg_HandleTimeLimitConfig> tg_HandleTimeLimitConfigList = (List<Tg_HandleTimeLimitConfig>) tg_handleTimeLimitConfigListService.execute(new Tg_HandleTimeLimitConfigForm()).get("tg_HandleTimeLimitConfigList");

		List<WorkTimeLimit> workTimeLimits = new ArrayList<WorkTimeLimit>();
		List<Tgpf_DepositDetail> tgpf_DepositDetailList = new ArrayList<Tgpf_DepositDetail>();


		for (int i = 0; i < BusiCoeds.length; i++) {
			String busiCode = BusiCoeds[i];
			String typeName = TypeNames[i];
			Integer type = Types[i];
			WorkTimeLimit workTimeLimit = new WorkTimeLimit();
			workTimeLimit.setTypeName(typeName);
			workTimeLimit.setBusiCode(busiCode);
			workTimeLimit.setType(type);
			Sm_ApprovalProcess_AFForm form = new Sm_ApprovalProcess_AFForm();
			form.setIsCountPage("0");
			form.setBusiCode(busiCode);
			form.setBusiState(S_ApprovalState.Completed);
			form.setApprovalApplyDate(model.getDateStr());
			form.setTheState(0);

			List<Sm_ApprovalProcess_AF> sm_ApprovalProcess_AFList = new ArrayList<Sm_ApprovalProcess_AF>();
			if (form.getBusiCode().equals(S_BusiCode.busiCode20)) {
				form.setBusiCode(S_BusiCode.busiCode_06120201);//退房退款申请-贷款已结清（新增）
				sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

				form.setBusiCode(S_BusiCode.busiCode_06120202);//退房退款申请-贷款未结清
				sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

				form.setBusiState(S_ApprovalState.Examining);
				form.setBusiCode(S_BusiCode.busiCode_06120201);//退房退款申请-贷款已结清（新增）
				sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

				form.setBusiCode(S_BusiCode.busiCode_06120202);//退房退款申请-贷款未结清
				sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

				form.setBusiCode(S_BusiCode.busiCode20);//退房退款管理
			} else if (form.getBusiCode().equals(S_BusiCode.busiCode19)) {
				form.setBusiCode(S_BusiCode.busiCode_06120301);//用款申请与复核
				sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

				form.setBusiState(S_ApprovalState.Examining);
				form.setBusiCode(S_BusiCode.busiCode_06120301);//用款申请与复核
				sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

				form.setBusiCode(S_BusiCode.busiCode19);//托管一般拨付管理
			} else if (form.getBusiCode().equals("061201")) {
				Tgpf_DepositDetailForm tgpf_depositDetailForm =  new Tgpf_DepositDetailForm();
				tgpf_depositDetailForm.setTheState(0);
				tgpf_depositDetailForm.setStartTimeStamp(MyDatetime.getInstance().dateToString(model.getStartDateTimeStamp()));
				tgpf_depositDetailForm.setEndTimeStamp(MyDatetime.getInstance().dateToString(model.getEndDateTimeStamp()));

				tgpf_DepositDetailList = (List<Tgpf_DepositDetail>) tgpf_depositDetailListService.execute(tgpf_depositDetailForm).get("tgpf_DepositDetailList");
			} else {
				sm_ApprovalProcess_AFList = (List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList");

				form.setBusiState(S_ApprovalState.Examining);
				sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));
			}
			int timeOutCount = 0;
			Integer timeLimit = getTimeLimit(tg_HandleTimeLimitConfigList, workTimeLimit.getTypeName());
			if (timeLimit == null) {
				return MyBackInfo.fail(properties, "请先办理" + workTimeLimit.getTypeName() +"时限配置！");
			}
			if (form.getBusiCode().equals("061201")) {
				workTimeLimit.setSize(tgpf_DepositDetailList.size());
				if (workTimeLimit.getSize() > 0) {
					for (Tgpf_DepositDetail tgpf_depositDetail : tgpf_DepositDetailList) {
						if (tgpf_depositDetail.getDayEndBalancing() != null && tgpf_depositDetail.getDayEndBalancing().getSettlementTime() != null) {
							if (isOutTime(MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getBillTimeStamp()), MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getDayEndBalancing().getSettlementTime()), timeLimit, tg_holidayMap)) {
								timeOutCount++;
							}
						} else {
							if (isOutTime(MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getBillTimeStamp()), null, timeLimit, tg_holidayMap)) {
								timeOutCount++;
							}
						}
					}
					workTimeLimit.setTimeOutCount(timeOutCount);
				} else {
					workTimeLimit.setTimeOutCount(0);
				}
			} else {
				workTimeLimit.setSize(sm_ApprovalProcess_AFList.size());
				if (workTimeLimit.getSize() > 0) {
					if (form.getBusiCode().equals(S_BusiCode.busiCode19)) {//托管一般拨付管理
						for (Sm_ApprovalProcess_AF sm_approvalProcess_af : sm_ApprovalProcess_AFList) {
							Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = (Tgpf_FundAppropriated_AF)tgpf_FundAppropriated_AFDao.findById(sm_approvalProcess_af.getSourceId());
							
							if (tgpf_FundAppropriated_AF == null) {
                                return MyBackInfo.fail(properties, "申请用款tableId：" + sm_approvalProcess_af.getSourceId() + "不存在");
                            }
							if(S_TheState.Normal == tgpf_FundAppropriated_AF.getTheState()){
	                            if (tgpf_FundAppropriated_AF.getFundAppropriatedList() != null && tgpf_FundAppropriated_AF.getFundAppropriatedList().size() > 0) {
//	                              if (isOutTime(tgpf_FundAppropriated_AF.getCreateTimeStamp(), tgpf_FundAppropriated_AF.getFundAppropriatedList().get(0).getRecordTimeStamp(), timeLimit, tg_holidayMap)) {
	                                if (isOutTime(sm_approvalProcess_af.getStartTimeStamp(), tgpf_FundAppropriated_AF.getFundAppropriatedList().get(0).getRecordTimeStamp(), timeLimit, tg_holidayMap)) {
	                                    timeOutCount++;
	                                }
	                            } else {
	                                if (isOutTime(tgpf_FundAppropriated_AF.getCreateTimeStamp(), null, timeLimit, tg_holidayMap)) {
	                                    timeOutCount++;
	                                }
	                            }
							}
						}
					} else {
						for (Sm_ApprovalProcess_AF sm_approvalProcess_af : sm_ApprovalProcess_AFList) {
							/*if (isOutTime(sm_approvalProcess_af.getCreateTimeStamp(), sm_approvalProcess_af.getLastUpdateTimeStamp(), timeLimit, tg_holidayMap)) {
								timeOutCount++;
							}*/
						    if(S_TheState.Normal == sm_approvalProcess_af.getTheState()){
						        if (isOutTime(sm_approvalProcess_af.getStartTimeStamp(), sm_approvalProcess_af.getLastUpdateTimeStamp(), timeLimit, tg_holidayMap)) {
	                                timeOutCount++;
	                            }
						    }
						}
					}

					workTimeLimit.setTimeOutCount(timeOutCount);
				} else {
					workTimeLimit.setTimeOutCount(0);
				}
			}

			workTimeLimits.add(workTimeLimit);
		}

		properties.put("tg_WorkTimeLimitCheckList", workTimeLimits);

		List<String> typeNames = new ArrayList<String>();
		for (WorkTimeLimit workTimeLimit : workTimeLimits) {
			typeNames.add(workTimeLimit.getTypeName());
		}
		properties.put("typeNames", typeNames);

		List<Integer> sizes = new ArrayList<Integer>();
		for (WorkTimeLimit workTimeLimit : workTimeLimits) {
			sizes.add(workTimeLimit.getSize());
		}
		properties.put("sizes", sizes);

		List<Integer> timeOutCounts = new ArrayList<Integer>();
		for (WorkTimeLimit workTimeLimit : workTimeLimits) {
			timeOutCounts.add(workTimeLimit.getTimeOutCount());
		}
		properties.put("timeOutCounts", timeOutCounts);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
	
	private boolean isOutTime(Long startTime, Long endTime, int timeLimit, Map<String, Tg_Holiday> tg_holidayMap) {
		String startDateStr = MyDatetime.getInstance().dateToSimpleString(startTime);
		if (endTime == null) {
			endTime = System.currentTimeMillis();
		}
		String endDateStr = MyDatetime.getInstance().dateToSimpleString(endTime);
		if (endDateStr.equals(startDateStr)) return false;
		long startDateTimeStamp = MyDatetime.getInstance().stringToLong(startDateStr);
		long endDateTimeStamp = MyDatetime.getInstance().stringToLong(endDateStr);
		int holidayCount = 0;
		long dateTimeStamp = startDateTimeStamp;
		while (endDateTimeStamp > dateTimeStamp + 24L * 60 * 60 * 1000) {
			dateTimeStamp = dateTimeStamp + 24L * 60 * 60 * 1000;
			Tg_Holiday tg_holiday = tg_holidayMap.get(MyDatetime.getInstance().dateToSimpleString(dateTimeStamp));
			if (tg_holiday != null && tg_holiday.getType() != 0) {
				holidayCount++;
			}
		}

		return endDateTimeStamp - startDateTimeStamp > (timeLimit + holidayCount) * 24L * 60 * 60 * 1000;
	}

	private Integer getTimeLimit(List<Tg_HandleTimeLimitConfig> tg_HandleTimeLimitConfigList, String theType) {
		for (Tg_HandleTimeLimitConfig tg_handleTimeLimitConfig : tg_HandleTimeLimitConfigList) {
			if (theType.equals(tg_handleTimeLimitConfig.getTheType())) {
				return tg_handleTimeLimitConfig.getLimitDayNumber();
			}
		}
		return null;
	}
	
	public static class WorkTimeLimit {
		
		@Getter @Setter @IFieldAnnotation(remark="类型")
		private Integer type;
		@Getter @Setter @IFieldAnnotation(remark="类型名称")
		private String typeName;
		@Getter @Setter @IFieldAnnotation(remark="总数")
		private Integer size;
		@Getter @Setter @IFieldAnnotation(remark="超时个数")
		private Integer timeOutCount;
		@Getter @Setter @IFieldAnnotation(remark="业务代码")
		private String busiCode;
	}
}
