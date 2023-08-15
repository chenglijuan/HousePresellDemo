package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_HouseInfoForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_HouseInfoDao;
import zhishusz.housepresell.database.dao.Empj_UnitInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_UnitInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：楼幢-户室
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Empj_HouseInfoAddsService
{
	private static final String BUSI_CODE = "03010205";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	@Autowired
	private Empj_HouseInfoDao empj_HouseInfoDao;
	@Autowired
	private Empj_UnitInfoDao empj_UnitInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;
	//业务编码
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	public Properties execute(Empj_HouseInfoForm model)
	{
		Properties properties = new MyProperties();

		//判断室号是否唯一
		if(model.getRoomId()!=null){
			Empj_HouseInfoForm empj_HouseInfoForm=new Empj_HouseInfoForm();
			empj_HouseInfoForm.setRoomId(model.getRoomId());
			empj_HouseInfoForm.setTheState(S_TheState.Normal);
			Long empj_UnitInfoId = model.getUnitInfoId();//关联单元-Id
			//根据单元的id获取对应单元的信息
			Empj_UnitInfo empj_UnitInfo=(Empj_UnitInfo)empj_UnitInfoDao.findById(empj_UnitInfoId);
			empj_HouseInfoForm.setUnitInfo(empj_UnitInfo);	
			Integer totalCount =empj_HouseInfoDao.findByPage_Size(empj_HouseInfoDao.getQuery_Size(empj_HouseInfoDao.getBasicHQL(), empj_HouseInfoForm));
			if(totalCount>0){
				return MyBackInfo.fail(properties, "室号已存在");
			}
		}else{
			return MyBackInfo.fail(properties, "室号不能为空");
		}
			
		//新增时 theState默认状态为Normal
		Integer theState=S_TheState.Normal;
		//String busiState = model.getBusiState();//业务状态
		//String eCode = model.geteCode();//编号
		String roomId = model.getRoomId();//室号
		String position = model.getPosition();//房屋坐落
		Double floor = model.getFloor();//所在楼层
		Double forecastArea = model.getForecastArea();//建筑面积		
		Double shareConsArea = model.getShareConsArea();//分摊面积
		Double innerconsArea = model.getInnerconsArea();//套内建筑面积（㎡）
		String purpose = model.getPurpose();//房屋用途
		Double recordPrice = model.getRecordPrice();//物价备案价格
		//对日期进行处理	(将String类型转换成long类型)
		Long lastTimeStampSyncRecordPriceToPresellSystem = MyDatetime.getInstance().stringToLong(model.getP1().toString());//物价备案时间
		String remark = model.getRemark();//备注
		Long logId = model.getLogId();//日志-Id	
		String eCodeFromPresellSystem="";//预售系统户编号
		String eCodeFromPublicSecurity="";//公安编号
		String eCodeFromPresellCert="";//预售证号-来源于楼栋
		String deliveryType="";//交付类型
		Double heigh=0.0;//层高
		String eCodeOfMapping="";//测绘编号
		String eCodeOfPicture="";//图幅号(分层分户图号)
		Long empj_UnitInfoId = model.getUnitInfoId();//关联单元-Id
		Long tripleAgreementId = model.getTripleAgreementId();//关联三方协议		
		//根据单元的id获取对应单元的信息
		Empj_UnitInfo empj_UnitInfo=(Empj_UnitInfo)empj_UnitInfoDao.findById(empj_UnitInfoId);				
		if(empj_UnitInfo == null || S_TheState.Deleted.equals(empj_UnitInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "'Empj_UnitInfo(Id:" + empj_UnitInfoId + ")'不存在");
		}
		//关联楼幢的信息
		
		Empj_BuildingInfo empj_BuildingInfo =empj_UnitInfo.getBuilding();
		if(empj_BuildingInfo!=null){
			 eCodeFromPresellSystem= empj_BuildingInfo.geteCodeFromPresellSystem();		
			 eCodeFromPublicSecurity = empj_BuildingInfo.geteCodeFromPublicSecurity(); 
			 eCodeFromPresellCert = empj_BuildingInfo.geteCodeFromPresellCert();	
			 deliveryType = empj_BuildingInfo.getDeliveryType();
			 heigh = empj_BuildingInfo.getHeigh();
			 eCodeOfMapping = empj_BuildingInfo.geteCodeOfMapping();
			 eCodeOfPicture = empj_BuildingInfo.geteCodeOfPicture();
		}
		//根据三方协议的编号查到对应的三方协议的信息
		Tgxy_TripleAgreement tgxy_TripleAgreement=(Tgxy_TripleAgreement)tgxy_TripleAgreementDao.findById(tripleAgreementId);							
		if(roomId == null || roomId.length() == 0)
		{
			return MyBackInfo.fail(properties, "室号不能为空");
		}
		if(position == null || position.length() == 0)
		{
			return MyBackInfo.fail(properties, "房屋坐落不能为空");
		}
		if(floor == null || floor < 1)
		{
			return MyBackInfo.fail(properties, "所在楼层不能为空");
		}
		if(forecastArea == null || forecastArea < 1)
		{
			return MyBackInfo.fail(properties, "建筑面积（预测）不能为空");
		}
		if(shareConsArea == null || shareConsArea < 1)
		{
			return MyBackInfo.fail(properties, "分摊面积（预测）不能为空");
		}
		if(innerconsArea == null || innerconsArea < 1)
		{
			return MyBackInfo.fail(properties, "套内面积（预测）不能为空");
		}
		if(purpose == null || purpose.length() == 0)
		{
			return MyBackInfo.fail(properties, "房屋用途不能为空");
		}
		if(recordPrice == null || recordPrice < 1)
		{
			return MyBackInfo.fail(properties, "物价备案价格不能为空");
		}
		if(lastTimeStampSyncRecordPriceToPresellSystem == null || lastTimeStampSyncRecordPriceToPresellSystem < 1)
		{
			return MyBackInfo.fail(properties, "物价备案时间不能为空");
		}
		if(remark == null || remark.length() == 0)
		{
			return MyBackInfo.fail(properties, "备注不能为空");
		}
	
		//根据id获取获取当前的登陆着
		Sm_User userStart = model.getUser();
		if(userStart == null || S_TheState.Deleted.equals(userStart.getTheState()))
		{
			return MyBackInfo.fail(properties, "用户未登录，请先登录！");
		}
		//获取创建时间
		Long createTimeStamp=System.currentTimeMillis();	
		Empj_HouseInfo empj_HouseInfo = new Empj_HouseInfo();
		empj_HouseInfo.setTheState(theState);
		empj_HouseInfo.setUserStart(userStart);
		empj_HouseInfo.setCreateTimeStamp(createTimeStamp);
		empj_HouseInfo.setRecordPrice(recordPrice);
		empj_HouseInfo.setFloor(floor);
		empj_HouseInfo.setRoomId(roomId);
		empj_HouseInfo.setPosition(position);
		empj_HouseInfo.setPurpose(purpose);
		empj_HouseInfo.setInnerconsArea(innerconsArea);
		empj_HouseInfo.setShareConsArea(shareConsArea);
		empj_HouseInfo.setRemark(remark);
		empj_HouseInfo.setLogId(logId);
		empj_HouseInfo.setUnitInfo(empj_UnitInfo);
		empj_HouseInfo.seteCodeFromPresellSystem(eCodeFromPresellSystem);
		empj_HouseInfo.seteCodeFromPublicSecurity(eCodeFromPublicSecurity);
		empj_HouseInfo.seteCodeFromPresellCert(eCodeFromPresellCert);
		empj_HouseInfo.setDeliveryType(deliveryType);
		empj_HouseInfo.setHeigh(heigh);	
		empj_HouseInfo.seteCodeOfMapping(eCodeOfMapping);
		empj_HouseInfo.seteCodeOfPicture(eCodeOfPicture);
		empj_HouseInfo.setForecastArea(forecastArea);
		empj_HouseInfo.setLastTimeStampSyncRecordPriceToPresellSystem(lastTimeStampSyncRecordPriceToPresellSystem);
		empj_HouseInfo.setTripleAgreement(tgxy_TripleAgreement);
		empj_HouseInfo.setBuilding(empj_BuildingInfo);
		empj_HouseInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		empj_HouseInfoDao.save(empj_HouseInfo);
		//保存成功后，楼幢中的户数添加
		if(empj_HouseInfo.getTableId()!=null){
		Empj_HouseInfo empj_HouseInfo1=	empj_HouseInfoDao.findById(empj_HouseInfo.getTableId());
		Integer sumFamilyNumber=0;
		Empj_BuildingInfo empj_BuildingInfo1=null;
			if(empj_HouseInfo1.getBuilding()!=null){
				empj_BuildingInfo1=empj_HouseInfo1.getBuilding();
				sumFamilyNumber= empj_BuildingInfo1.getSumFamilyNumber();
				if(sumFamilyNumber==null){
					sumFamilyNumber=0;
					sumFamilyNumber=sumFamilyNumber+1;
				}else{
					sumFamilyNumber=sumFamilyNumber+1;
				}
				
				empj_BuildingInfo1.setSumFamilyNumber(sumFamilyNumber);
				empj_BuildingInfoDao.save(empj_BuildingInfo1);
			}		
		}		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
