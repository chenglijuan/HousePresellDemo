package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Empj_BuildingAccountSupervisedForm;
import zhishusz.housepresell.database.dao.Empj_BuildingAccountSupervisedDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BankAccountSupervisedDao;
import zhishusz.housepresell.database.po.Empj_BuildingAccountSupervised;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BankAccountSupervised;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;
	
/*
 * Service添加操作：楼幢与楼幢监管账号关联表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_BuildingAccountSupervisedAddService
{
	private static final String BUSI_CODE = "03010210";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"

	@Autowired
	private Empj_BuildingAccountSupervisedDao empj_BuildingAccountSupervisedDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpj_BankAccountSupervisedDao tgpj_BankAccountSupervisedDao;
	
	public Properties execute(Empj_BuildingAccountSupervisedForm model)
	{
		Properties properties = new MyProperties();
		MyDatetime myDatetime = MyDatetime.getInstance();

		Integer theState = S_TheState.Normal;
		String busiState = "1";
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = System.currentTimeMillis();
//		Long userUpdateId = model.getUserUpdateId();
//		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
//		Long userRecordId = model.getUserRecordId();
//		Long recordTimeStamp = model.getRecordTimeStamp();
		Long buildingInfoId = model.getBuildingInfoId();
		Long bankAccountSupervisedId = model.getBankAccountSupervisedId();
//		Long beginTimeStamp = model.getBeginTimeStamp();
//		String beginTimeStampString = model.getBeginTimeStampString();
//		Long endTimeStamp = model.getEndTimeStamp();
//		String endTimeStampString = model.getEndTimeStampString();
		Integer isUsing=model.getIsUsing();

		//查找是否是唯一的
		Empj_BuildingAccountSupervisedForm findUniqueForm = new Empj_BuildingAccountSupervisedForm();
		findUniqueForm.setTheState(S_TheState.Normal);
		findUniqueForm.setBuildingInfoId(buildingInfoId);
		findUniqueForm.setBankAccountSupervisedId(bankAccountSupervisedId);
		List byPage = empj_BuildingAccountSupervisedDao.findByPage(empj_BuildingAccountSupervisedDao
				.getQuery(empj_BuildingAccountSupervisedDao.getBasicHQL(), findUniqueForm));
		if(byPage.size()>0){
			return MyBackInfo.fail(properties, "该楼幢与监管账户对应关系已存在，无法添加");
		}

		//		if(theState == null || theState < 1)
//		{
//			return MyBackInfo.fail(properties, "'theState'不能为空");
//		}
//		if(busiState == null || busiState.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'busiState'不能为空");
//		}
//		if(eCode == null || eCode.length() == 0)
//		{
//			return MyBackInfo.fail(properties, "'eCode'不能为空");
//		}
//		if(userStartId == null || userStartId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userStart'不能为空");
//		}
//		if(createTimeStamp == null || createTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
//		}
//		if(userUpdateId == null || userUpdateId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
//		}
//		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
//		}
//		if(userRecordId == null || userRecordId < 1)
//		{
//			return MyBackInfo.fail(properties, "'userRecord'不能为空");
//		}
//		if(recordTimeStamp == null || recordTimeStamp < 1)
//		{
//			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
//		}

		if(buildingInfoId == null || buildingInfoId < 1)
		{
			return MyBackInfo.fail(properties, "'buildingInfo'不能为空");
		}
		if(bankAccountSupervisedId == null || bankAccountSupervisedId < 1)
		{
			return MyBackInfo.fail(properties, "'bankAccountSupervised'不能为空");
		}
//		if(beginTimeStampString == null || beginTimeStampString.length() < 1)
//		{
//			return MyBackInfo.fail(properties, "'beginTimeStamp'不能为空");
//		}
//		if(endTimeStampString == null || endTimeStampString.length() < 1)
//		{
//			return MyBackInfo.fail(properties, "'endTimeStamp'不能为空");
//		}
		if (isUsing == null)
		{
			return MyBackInfo.fail(properties, "请选择是否启用");
		}
//		beginTimeStamp = myDatetime.stringToLong(beginTimeStampString);
//		endTimeStamp = myDatetime.stringToLong(endTimeStampString);

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
//		Sm_User userUpdate = (Sm_User)sm_UserDao.findById(userUpdateId);
//		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		Empj_BuildingInfo buildingInfo = (Empj_BuildingInfo)empj_BuildingInfoDao.findById(buildingInfoId);
		Tgpj_BankAccountSupervised bankAccountSupervised = (Tgpj_BankAccountSupervised)tgpj_BankAccountSupervisedDao.findById(bankAccountSupervisedId);
//		if(userStart == null)
//		{
//			return MyBackInfo.fail(properties, "'userStart'不能为空");
//		}
//		if(userUpdate == null)
//		{
//			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
//		}
//		if(userRecord == null)
//		{
//			return MyBackInfo.fail(properties, "'userRecord'不能为空");
//		}
		if(buildingInfo == null)
		{
			return MyBackInfo.fail(properties, "'buildingInfo'不能为空");
		}
		if(bankAccountSupervised == null)
		{
			return MyBackInfo.fail(properties, "'bankAccountSupervised'不能为空");
		}
	
		Empj_BuildingAccountSupervised empj_BuildingAccountSupervised = new Empj_BuildingAccountSupervised();
		empj_BuildingAccountSupervised.setTheState(theState);
		empj_BuildingAccountSupervised.setBusiState(busiState);
		empj_BuildingAccountSupervised.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		empj_BuildingAccountSupervised.setUserStart(userStart);
		empj_BuildingAccountSupervised.setCreateTimeStamp(createTimeStamp);
		empj_BuildingAccountSupervised.setUserStart(model.getUser());
		empj_BuildingAccountSupervised.setUserUpdate(model.getUser());
//		empj_BuildingAccountSupervised.setUserUpdate(userUpdate);
//		empj_BuildingAccountSupervised.setLastUpdateTimeStamp(lastUpdateTimeStamp);
//		empj_BuildingAccountSupervised.setUserRecord(userRecord);
//		empj_BuildingAccountSupervised.setRecordTimeStamp(recordTimeStamp);
		empj_BuildingAccountSupervised.setLastUpdateTimeStamp(System.currentTimeMillis());
		empj_BuildingAccountSupervised.setBuildingInfo(buildingInfo);
		empj_BuildingAccountSupervised.setBankAccountSupervised(bankAccountSupervised);
//		empj_BuildingAccountSupervised.setBeginTimeStamp(beginTimeStamp);
//		empj_BuildingAccountSupervised.setEndTimeStamp(endTimeStamp);
		empj_BuildingAccountSupervised.setIsUsing(isUsing);
		empj_BuildingAccountSupervisedDao.save(empj_BuildingAccountSupervised);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tableId", empj_BuildingAccountSupervised.getTableId());

		return properties;
	}
}
