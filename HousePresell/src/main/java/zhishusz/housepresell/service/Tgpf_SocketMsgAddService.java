package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Tgpf_SocketMsgForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.transaction.Transactional;
	
/*
 * Service添加操作：接口报文表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_SocketMsgAddService
{
	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	public Properties execute(Tgpf_SocketMsgForm model)
	{
		Properties properties = new MyProperties();

		Integer theState = model.getTheState();
		String busiState = model.getBusiState();
		String eCode = model.geteCode();
		Long userStartId = model.getUserStartId();
		Long createTimeStamp = model.getCreateTimeStamp();
		Long lastUpdateTimeStamp = model.getLastUpdateTimeStamp();
		Long userRecordId = model.getUserRecordId();
		Long recordTimeStamp = model.getRecordTimeStamp();
		String msgLength = model.getMsgLength();
		String locationCode = model.getLocationCode();
		String msgBusinessCode = model.getMsgBusinessCode();
		String bankCode = model.getBankCode();
		String returnCode = model.getReturnCode();
		String remark = model.getRemark();
		String msgDirection = model.getMsgDirection();
		Integer msgStatus = model.getMsgStatus();
		String md5Check = model.getMd5Check();
		Long msgTimeStamp = model.getMsgTimeStamp();
		String eCodeOfTripleAgreement = model.geteCodeOfTripleAgreement();
		String eCodeOfPermitRecord = model.geteCodeOfPermitRecord();
		String eCodeOfContractRecord = model.geteCodeOfContractRecord();
		String msgSerialno = model.getMsgSerialno();
		String msgContent = model.getMsgContent();
		
		if(theState == null || theState < 1)
		{
			return MyBackInfo.fail(properties, "'theState'不能为空");
		}
		if(busiState == null || busiState.length()< 1)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}
		if(eCode == null || eCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCode'不能为空");
		}
		if(userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(createTimeStamp == null || createTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'createTimeStamp'不能为空");
		}
		if(lastUpdateTimeStamp == null || lastUpdateTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'lastUpdateTimeStamp'不能为空");
		}
		if(userRecordId == null || userRecordId < 1)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
		if(recordTimeStamp == null || recordTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'recordTimeStamp'不能为空");
		}
		if(msgLength == null || msgLength.length() == 0)
		{
			return MyBackInfo.fail(properties, "'msgLength'不能为空");
		}
		if(locationCode == null || locationCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'locationCode'不能为空");
		}
		if(msgBusinessCode == null || msgBusinessCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'msgBusinessCode'不能为空");
		}
		if(bankCode == null || bankCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'bankCode'不能为空");
		}
		if(returnCode == null || returnCode.length() == 0)
		{
			return MyBackInfo.fail(properties, "'returnCode'不能为空");
		}
		if(remark == null || remark.length() == 0)
		{
			return MyBackInfo.fail(properties, "'remark'不能为空");
		}
		if(msgDirection == null || msgDirection.length() == 0)
		{
			return MyBackInfo.fail(properties, "'msgDirection'不能为空");
		}
		if(msgStatus == null || msgStatus < 1)
		{
			return MyBackInfo.fail(properties, "'msgStatus'不能为空");
		}
		if(md5Check == null || md5Check.length() == 0)
		{
			return MyBackInfo.fail(properties, "'md5Check'不能为空");
		}
		if(msgTimeStamp == null || msgTimeStamp < 1)
		{
			return MyBackInfo.fail(properties, "'msgTimeStamp'不能为空");
		}
		if(eCodeOfTripleAgreement == null || eCodeOfTripleAgreement.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfTripleAgreement'不能为空");
		}
		if(eCodeOfPermitRecord == null || eCodeOfPermitRecord.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfPermitRecord'不能为空");
		}
		if(eCodeOfContractRecord == null || eCodeOfContractRecord.length() == 0)
		{
			return MyBackInfo.fail(properties, "'eCodeOfContractRecord'不能为空");
		}
		if(msgSerialno == null || msgSerialno.length() == 0)
		{
			return MyBackInfo.fail(properties, "'msgSerialno'不能为空");
		}
		if(msgContent == null || msgContent.length() == 0)
		{
			return MyBackInfo.fail(properties, "'msgContent'不能为空");
		}

		Sm_User userStart = (Sm_User)sm_UserDao.findById(userStartId);
		Sm_User userRecord = (Sm_User)sm_UserDao.findById(userRecordId);
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "'userStart'不能为空");
		}
		if(userRecord == null)
		{
			return MyBackInfo.fail(properties, "'userRecord'不能为空");
		}
	
		Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
		tgpf_SocketMsg.setTheState(theState);
		tgpf_SocketMsg.setBusiState(busiState);
		tgpf_SocketMsg.seteCode(eCode);
		tgpf_SocketMsg.setUserStart(userStart);
		tgpf_SocketMsg.setCreateTimeStamp(createTimeStamp);
		tgpf_SocketMsg.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		tgpf_SocketMsg.setUserRecord(userRecord);
		tgpf_SocketMsg.setRecordTimeStamp(recordTimeStamp);
		tgpf_SocketMsg.setMsgLength(msgLength);
		tgpf_SocketMsg.setLocationCode(locationCode);
		tgpf_SocketMsg.setMsgBusinessCode(msgBusinessCode);
		tgpf_SocketMsg.setBankCode(bankCode);
		tgpf_SocketMsg.setReturnCode(returnCode);
		tgpf_SocketMsg.setRemark(remark);
		tgpf_SocketMsg.setMsgDirection(msgDirection);
		tgpf_SocketMsg.setMsgStatus(msgStatus);
		tgpf_SocketMsg.setMd5Check(md5Check);
		tgpf_SocketMsg.setMsgTimeStamp(msgTimeStamp);
		tgpf_SocketMsg.seteCodeOfTripleAgreement(eCodeOfTripleAgreement);
		tgpf_SocketMsg.seteCodeOfPermitRecord(eCodeOfPermitRecord);
		tgpf_SocketMsg.seteCodeOfContractRecord(eCodeOfContractRecord);
		tgpf_SocketMsg.setMsgSerialno(msgSerialno);
		tgpf_SocketMsg.setMsgContent(msgContent);
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
