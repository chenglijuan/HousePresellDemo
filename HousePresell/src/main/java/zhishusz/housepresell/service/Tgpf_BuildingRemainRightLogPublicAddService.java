package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：公共留存权益计算工具
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BuildingRemainRightLogPublicAddService {
	private static final String BUSI_CODE = "200302";// 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;

	public Properties execute(Tgpf_BuildingRemainRightLogForm model) {
		Properties properties = new MyProperties();

		Long buildingId = model.getBuildingId();// 楼幢Id
		String billTimeStamp = model.getBillTimeStamp();// 记账日期
		String srcBusiType = model.getSrcBusiType();// 来源业务类型

		if (buildingId == null || buildingId < 1) {
			return MyBackInfo.fail(properties, "请选择需要计算留存权益的楼幢");
		}

		if (billTimeStamp == null || billTimeStamp.length() < 1)
			billTimeStamp = MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis());

		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(buildingId);
		if (empj_BuildingInfo == null) {
			return MyBackInfo.fail(properties, "选择的楼幢不存在");
		}

		Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog = new Tgpf_BuildingRemainRightLog();
		tgpf_BuildingRemainRightLog.setTheState(S_TheState.Normal);
		tgpf_BuildingRemainRightLog.setBusiState(Tgpf_BuildingRemainRightLog.Uncompared);
		tgpf_BuildingRemainRightLog.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_BuildingRemainRightLog.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tgpf_BuildingRemainRightLog.setProject(empj_BuildingInfo.getProject());
		if (empj_BuildingInfo.getProject() != null) {
			tgpf_BuildingRemainRightLog.setTheNameOfProject(empj_BuildingInfo.getProject().getTheName());
			tgpf_BuildingRemainRightLog.seteCodeOfProject(empj_BuildingInfo.getProject().geteCode());
		}
		
		// 开发企业
		tgpf_BuildingRemainRightLog.setDevelopCompany(empj_BuildingInfo.getDevelopCompany());
		if (empj_BuildingInfo.getDevelopCompany() != null) {
			tgpf_BuildingRemainRightLog.seteCodeOfDevelopCompany(empj_BuildingInfo.getDevelopCompany().geteCode());
		}
		tgpf_BuildingRemainRightLog.setBuilding(empj_BuildingInfo);
		tgpf_BuildingRemainRightLog.seteCodeFromConstruction(empj_BuildingInfo.geteCodeFromConstruction());// 施工编号
		tgpf_BuildingRemainRightLog.seteCodeFromPublicSecurity(empj_BuildingInfo.geteCodeFromPublicSecurity());// 公安编号
		tgpf_BuildingRemainRightLog.setBuildingAccount(empj_BuildingInfo.getBuildingAccount());
		tgpf_BuildingRemainRightLog.setBuildingExtendInfo(empj_BuildingInfo.getExtendInfo());
		if (empj_BuildingInfo.getBuildingAccount() != null) {
			tgpf_BuildingRemainRightLog
					.setCurrentFigureProgress(empj_BuildingInfo.getBuildingAccount().getCurrentFigureProgress());
			tgpf_BuildingRemainRightLog
					.setCurrentLimitedRatio(empj_BuildingInfo.getBuildingAccount().getCurrentLimitedRatio());
			tgpf_BuildingRemainRightLog
					.setNodeLimitedAmount(empj_BuildingInfo.getBuildingAccount().getNodeLimitedAmount());
		}
		tgpf_BuildingRemainRightLog.setSrcBusiType(srcBusiType);
		
		/**
		 * 楼幢入账总金额=该户所在楼幢托管入账总金额-已退房退款金额
		 */
		// 该户所在楼幢托管入账总金额
		Tgpj_BuildingAccount buildingAccount = empj_BuildingInfo.getBuildingAccount();
		Double totalAccountAmount = null;
		if (buildingAccount != null && buildingAccount.getTotalAccountAmount() != null) {
			totalAccountAmount = buildingAccount.getTotalAccountAmount()
					+ (buildingAccount.getApplyRefundPayoutAmount() == null ? 0.0
							: buildingAccount.getApplyRefundPayoutAmount())
					+ (buildingAccount.getRefundAmount() == null ? 0.0 : buildingAccount.getRefundAmount());
		} else {
			totalAccountAmount = 0.0;
		}

		// 楼幢入账总金额
		tgpf_BuildingRemainRightLog.setTotalAccountAmount(totalAccountAmount);
		tgpf_BuildingRemainRightLog.setBillTimeStamp(billTimeStamp);
		tgpf_BuildingRemainRightLog.setSrcBusiType(srcBusiType);
		Serializable save = tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);
		
		Long tgpf_BuildingRemainRightLogId = new Long(save.toString());
		/*
		 * TODO 
		 * xsz by time 2019-3-1 14:18:14
		 * 生成 楼栋每日留存权益计算日志（Tgpf_BuildingRemainRightLog） 后，
		 * 用此信息调用存储过程进行户室留存权益计算
		 * 
		 */
		properties = tgpf_RemainRightDao.buildingRemainRight(tgpf_BuildingRemainRightLogId);
		
		return properties;
	}
}
