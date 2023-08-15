package zhishusz.housepresell.service;

import cn.hutool.core.date.DateUtil;
import zhishusz.housepresell.controller.form.*;
import zhishusz.housepresell.database.dao.Tgpf_FundAppropriated_AFDao;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiCode;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
 * Service详情：工作时限检查
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_WorkTimeLimitCheckDetailService
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
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

		List<WorkTimeLimitDetail> workTimeLimitDetailList = new ArrayList<WorkTimeLimitDetail>();
		List<Tg_HandleTimeLimitConfig> tg_HandleTimeLimitConfigList = (List<Tg_HandleTimeLimitConfig>) tg_handleTimeLimitConfigListService.execute(new Tg_HandleTimeLimitConfigForm()).get("tg_HandleTimeLimitConfigList");
		Tg_WorkTimeLimitCheckListService.WorkTimeLimit workTimeLimit = null;
		Tg_HandleTimeLimitConfig handleTimeLimitConfig = null;
		Integer totalCount = 0;

		if (model.getBusiCode() != null) {
			for (int m = 0; m < Tg_WorkTimeLimitCheckListService.BusiCoeds.length; m++) {
				String busiCode = Tg_WorkTimeLimitCheckListService.BusiCoeds[m];
				if (busiCode.equals(model.getBusiCode())) {
					workTimeLimit = new Tg_WorkTimeLimitCheckListService.WorkTimeLimit();

					workTimeLimit.setType(Tg_WorkTimeLimitCheckListService.Types[m]);
					workTimeLimit.setTypeName(Tg_WorkTimeLimitCheckListService.TypeNames[m]);

					Sm_ApprovalProcess_AFForm form = new Sm_ApprovalProcess_AFForm();
					form.setIsCountPage("0");
					form.setBusiCode(model.getBusiCode());
					form.setBusiState(S_ApprovalState.Completed);
					form.setApprovalApplyDate(model.getDateStr());
					form.setTheState(0);

					List<Sm_ApprovalProcess_AF> sm_ApprovalProcess_AFList = new ArrayList<Sm_ApprovalProcess_AF>();
					List<Tgpf_DepositDetail> tgpf_DepositDetailList = new ArrayList<Tgpf_DepositDetail>();
					if (form.getBusiCode().equals(S_BusiCode.busiCode20)) {
						form.setBusiCode(S_BusiCode.busiCode_06120201);
						sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

						form.setBusiCode(S_BusiCode.busiCode_06120202);
						sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

						form.setBusiState(S_ApprovalState.Examining);
						form.setBusiCode(S_BusiCode.busiCode_06120201);
						sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

						form.setBusiCode(S_BusiCode.busiCode_06120202);
						sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

						form.setBusiCode(S_BusiCode.busiCode20);
					} else if (form.getBusiCode().equals(S_BusiCode.busiCode19)) {
						form.setBusiCode(S_BusiCode.busiCode_06120301);
						sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

						form.setBusiState(S_ApprovalState.Examining);
						form.setBusiCode(S_BusiCode.busiCode_06120301);
						sm_ApprovalProcess_AFList.addAll((List<Sm_ApprovalProcess_AF>) sm_approvalProcess_afListService.execute(form).get("sm_ApprovalProcess_AFList"));

						form.setBusiCode(S_BusiCode.busiCode19);
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

										WorkTimeLimitDetail workTimeLimitDetail = new WorkTimeLimitDetail();
										workTimeLimitDetail.seteCode(tgpf_depositDetail.geteCode());
										workTimeLimitDetail.setApplyDate(DateUtil.formatDate(new Date(MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getBillTimeStamp()))));
										if (MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getDayEndBalancing().getSettlementTime()) != null) {
											workTimeLimitDetail.setCompleteDate(DateUtil.formatDate(new Date(MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getDayEndBalancing().getSettlementTime()))));
											int days = (int) ((MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getDayEndBalancing().getSettlementTime()) - MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getBillTimeStamp())) / (24L * 60 * 60 * 1000));
											workTimeLimitDetail.setDays(getCount(MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getBillTimeStamp()), MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getDayEndBalancing().getSettlementTime()), tg_holidayMap));
											workTimeLimitDetail.setTimeOutDays(getOutTimeCount(MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getBillTimeStamp()), MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getDayEndBalancing().getSettlementTime()), timeLimit, tg_holidayMap));
										}
										workTimeLimitDetailList.add(workTimeLimitDetail);
									}
								} else {
									if (isOutTime(MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getBillTimeStamp()), null, timeLimit, tg_holidayMap)) {
										timeOutCount++;

										WorkTimeLimitDetail workTimeLimitDetail = new WorkTimeLimitDetail();
										workTimeLimitDetail.seteCode(tgpf_depositDetail.geteCode());
										workTimeLimitDetail.setApplyDate(DateUtil.formatDate(new Date(MyDatetime.getInstance().stringToLong(tgpf_depositDetail.getBillTimeStamp()))));
										workTimeLimitDetailList.add(workTimeLimitDetail);
									}
								}
							}
							workTimeLimit.setTimeOutCount(timeOutCount);
						} else {
							workTimeLimit.setTimeOutCount(0);
						}
					} else {
						int size = sm_ApprovalProcess_AFList.size();
						workTimeLimit.setSize(size);
						if (size > 0) {
							if (form.getBusiCode().equals(S_BusiCode.busiCode19)) {
								for (Sm_ApprovalProcess_AF sm_approvalProcess_af : sm_ApprovalProcess_AFList) {
									Tgpf_FundAppropriated_AF tgpf_FundAppropriated_AF = (Tgpf_FundAppropriated_AF)tgpf_FundAppropriated_AFDao.findById(sm_approvalProcess_af.getSourceId());
									if (tgpf_FundAppropriated_AF == null) {
										return MyBackInfo.fail(properties, "申请用款tableId：" + sm_approvalProcess_af.getSourceId() + "不存在");
									}
									
									if(S_TheState.Normal == tgpf_FundAppropriated_AF.getTheState()){
									    if (tgpf_FundAppropriated_AF.getFundAppropriatedList() != null && tgpf_FundAppropriated_AF.getFundAppropriatedList().size() > 0) {
//	                                      if (isOutTime(tgpf_FundAppropriated_AF.getCreateTimeStamp(), tgpf_FundAppropriated_AF.getFundAppropriatedList().get(0).getRecordTimeStamp(), timeLimit, tg_holidayMap)) {
	                                        if (isOutTime(sm_approvalProcess_af.getStartTimeStamp(), tgpf_FundAppropriated_AF.getFundAppropriatedList().get(0).getRecordTimeStamp(), timeLimit, tg_holidayMap)) {
	                                            timeOutCount++;

	                                            WorkTimeLimitDetail workTimeLimitDetail = new WorkTimeLimitDetail();
	                                            workTimeLimitDetail.setTableId(sm_approvalProcess_af.getSourceId());
	                                            workTimeLimitDetail.setBusiCode(sm_approvalProcess_af.getBusiCode());
	                                            workTimeLimitDetail.seteCode(sm_approvalProcess_af.geteCode());
//	                                          workTimeLimitDetail.setApplyDate(DateUtil.formatDate(new Date(tgpf_FundAppropriated_AF.getCreateTimeStamp())));
	                                            workTimeLimitDetail.setApplyDate(DateUtil.formatDate(new Date(sm_approvalProcess_af.getStartTimeStamp())));
	                                            if (tgpf_FundAppropriated_AF.getFundAppropriatedList().get(0).getRecordTimeStamp() != null) {
	                                                workTimeLimitDetail.setCompleteDate(DateUtil.formatDate(new Date(tgpf_FundAppropriated_AF.getFundAppropriatedList().get(0).getRecordTimeStamp())));
	                                                int days = (int) ((tgpf_FundAppropriated_AF.getFundAppropriatedList().get(0).getRecordTimeStamp() - tgpf_FundAppropriated_AF.getCreateTimeStamp()) / (24L * 60 * 60 * 1000));
	                                                workTimeLimitDetail.setDays(getCount(sm_approvalProcess_af.getStartTimeStamp(), tgpf_FundAppropriated_AF.getFundAppropriatedList().get(0).getRecordTimeStamp(), tg_holidayMap));
	                                                workTimeLimitDetail.setTimeOutDays(getOutTimeCount(sm_approvalProcess_af.getStartTimeStamp(), tgpf_FundAppropriated_AF.getFundAppropriatedList().get(0).getRecordTimeStamp(), timeLimit, tg_holidayMap));
	                                            }
	                                            workTimeLimitDetailList.add(workTimeLimitDetail);
	                                        }
	                                    } else {
	                                        if (isOutTime(sm_approvalProcess_af.getStartTimeStamp(), null, timeLimit, tg_holidayMap)) {
	                                            timeOutCount++;

	                                            WorkTimeLimitDetail workTimeLimitDetail = new WorkTimeLimitDetail();
	                                            workTimeLimitDetail.setTableId(sm_approvalProcess_af.getSourceId());
	                                            workTimeLimitDetail.setBusiCode(sm_approvalProcess_af.getBusiCode());
	                                            workTimeLimitDetail.seteCode(sm_approvalProcess_af.geteCode());
	                                            workTimeLimitDetail.setApplyDate(DateUtil.formatDate(new Date(sm_approvalProcess_af.getStartTimeStamp())));
	                                            workTimeLimitDetailList.add(workTimeLimitDetail);
	                                        }
	                                    } 
									}
									
								}
							} else {
								for (int i = 0; i < sm_ApprovalProcess_AFList.size(); i++) {
									Sm_ApprovalProcess_AF sm_approvalProcess_af = sm_ApprovalProcess_AFList.get(i);
									if (isOutTime(sm_approvalProcess_af.getStartTimeStamp(), sm_approvalProcess_af.getLastUpdateTimeStamp(), timeLimit, tg_holidayMap)) {
										timeOutCount++;

										WorkTimeLimitDetail workTimeLimitDetail = new WorkTimeLimitDetail();
										workTimeLimitDetail.setTableId(sm_approvalProcess_af.getSourceId());
										workTimeLimitDetail.setBusiCode(sm_approvalProcess_af.getBusiCode());
										workTimeLimitDetail.seteCode(sm_approvalProcess_af.geteCode());
										workTimeLimitDetail.setApplyDate(DateUtil.formatDate(new Date(sm_approvalProcess_af.getStartTimeStamp())));
										if (sm_approvalProcess_af.getLastUpdateTimeStamp() != null) {
											workTimeLimitDetail.setCompleteDate(DateUtil.formatDate(new Date(sm_approvalProcess_af.getLastUpdateTimeStamp())));
											int days = (int) ((sm_approvalProcess_af.getLastUpdateTimeStamp() - sm_approvalProcess_af.getStartTimeStamp()) / (24L * 60 * 60 * 1000));
											workTimeLimitDetail.setDays(getCount(sm_approvalProcess_af.getStartTimeStamp(), sm_approvalProcess_af.getLastUpdateTimeStamp(), tg_holidayMap));
											workTimeLimitDetail.setTimeOutDays(getOutTimeCount(sm_approvalProcess_af.getStartTimeStamp(), sm_approvalProcess_af.getLastUpdateTimeStamp(), timeLimit, tg_holidayMap));
										}
										workTimeLimitDetailList.add(workTimeLimitDetail);
									} else {
										sm_ApprovalProcess_AFList.remove(i);
										i--;
									}
								}
							}
							workTimeLimit.setTimeOutCount(timeOutCount);
						} else {
							workTimeLimit.setTimeOutCount(0);
						}
					}
					break;
				}
			}
		}

		if (workTimeLimit != null) {
			handleTimeLimitConfig = getHandleTimeLimitConfig(tg_HandleTimeLimitConfigList, workTimeLimit.getTypeName());
		}
		totalCount = workTimeLimitDetailList.size();
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		if (pageNumber > 0) {
			List<WorkTimeLimitDetail> workTimeLimitDetailListPre = new ArrayList<WorkTimeLimitDetail>();
			List<WorkTimeLimitDetail> workTimeLimitDetailListNext = new ArrayList<WorkTimeLimitDetail>();
			workTimeLimitDetailListPre.addAll(workTimeLimitDetailList.subList(0, (pageNumber - 1) * countPerPage));
			if (pageNumber * countPerPage < workTimeLimitDetailList.size()) {
				workTimeLimitDetailListNext.addAll(workTimeLimitDetailList.subList(pageNumber * countPerPage, workTimeLimitDetailList.size()));
			}
			workTimeLimitDetailList.removeAll(workTimeLimitDetailListPre);
			workTimeLimitDetailList.removeAll(workTimeLimitDetailListNext);
		}

		properties.put("workTimeLimitDetailList", workTimeLimitDetailList);
		properties.put("workTimeLimit", workTimeLimit);
		properties.put("handleTimeLimitConfig", handleTimeLimitConfig);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
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

	private int getCount(Long startTime, Long endTime, Map<String, Tg_Holiday> tg_holidayMap) {
		String startDateStr = MyDatetime.getInstance().dateToSimpleString(startTime);
		String endDateStr = MyDatetime.getInstance().dateToSimpleString(endTime);
		if (endDateStr.equals(startDateStr)) return 0;
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

		int count = (int) ((endDateTimeStamp - startDateTimeStamp) / (24L * 60 * 60 * 1000)) - (holidayCount);
		return count > 0 ? count : 0;
	}

	private int getOutTimeCount(Long startTime, Long endTime, int timeLimit, Map<String, Tg_Holiday> tg_holidayMap) {
		String startDateStr = MyDatetime.getInstance().dateToSimpleString(startTime);
		String endDateStr = MyDatetime.getInstance().dateToSimpleString(endTime);
		if (endDateStr.equals(startDateStr)) return 0;
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

		int count = (int) ((endDateTimeStamp - startDateTimeStamp) / (24L * 60 * 60 * 1000)) - (timeLimit + holidayCount);
		return count > 0 ? count : 0;
	}

	private Integer getTimeLimit(List<Tg_HandleTimeLimitConfig> tg_HandleTimeLimitConfigList, String theType) {
		for (Tg_HandleTimeLimitConfig tg_handleTimeLimitConfig : tg_HandleTimeLimitConfigList) {
			if (theType.equals(tg_handleTimeLimitConfig.getTheType())) {
				return tg_handleTimeLimitConfig.getLimitDayNumber();
			}
		}
		return null;
	}

	private Tg_HandleTimeLimitConfig getHandleTimeLimitConfig(List<Tg_HandleTimeLimitConfig> tg_HandleTimeLimitConfigList, String theType) {
		for (Tg_HandleTimeLimitConfig tg_handleTimeLimitConfig : tg_HandleTimeLimitConfigList) {
			if (theType.equals(tg_handleTimeLimitConfig.getTheType())) {
				return tg_handleTimeLimitConfig;
			}
		}
		return null;
	}

	public static class WorkTimeLimitDetail {
		@Getter @Setter @IFieldAnnotation(remark="主键")
		private Long tableId;
		@Getter @Setter @IFieldAnnotation(remark="序号")
		private Integer index;
		@Getter @Setter @IFieldAnnotation(remark="业务编号")
		private String busiCode;
		@Getter @Setter @IFieldAnnotation(remark="编号")
		private String eCode;
		@Getter @Setter @IFieldAnnotation(remark="提交申请日期")
		private String applyDate = "-";
		@Getter @Setter @IFieldAnnotation(remark="审核完成日期")
		private String completeDate = "未完成";
		@Getter @Setter @IFieldAnnotation(remark="办理业务所用天数")
		private Integer days;
		@Getter @Setter @IFieldAnnotation(remark="超时天数")
		private Integer timeOutDays;

		public String geteCode() {
			return eCode;
		}

		public void seteCode(String eCode) {
			this.eCode = eCode;
		}
	}
	
}
