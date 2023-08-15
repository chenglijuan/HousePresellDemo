package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_AcceptFileType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：附件配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentCfgAddService
{
	private static final String BUSI_CODE = "010201";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	//业务编码
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	
	public Properties execute(Sm_AttachmentCfgForm model,String busiTypeid)
	{
		Properties properties = new MyProperties();		
		//判断业务类型是不是为空
		if(busiTypeid==null){			
		  return MyBackInfo.fail(properties, "业务编码不能为空");
	    }			
		//添加时为theSate付默认初始值
		model.setTheState(0);	
		//业务状态
		String busiState = model.getBusiState();
		//编码
		String eCode = model.geteCode();
		//创建时间
		Long createTimeStamp =System.currentTimeMillis();	
		//附件类型名称
		String theName = model.getTheName();
		//接受的文件类型
		String acceptFileType = model.getAcceptFileType();
		//接受的文件数量
		Integer acceptFileCount = model.getAcceptFileCount();
		//单个文件大小限制（最大）
		Integer maxFileSize = model.getMaxFileSize();
		//单个文件大小限制（最小）
		Integer minFileSize = model.getMinFileSize();
		//备注
		String remark = model.getRemark();
		//是否为图片
		Boolean isImage = model.getIsImage();
		//是否必须
		Boolean isNeeded = model.getIsNeeded();
		//附件列表类型	
		String listType = model.getListType();
		//是否签章
		String isCfgSignature = model.getIsCfgSignature();
	 		
		/*if(busiState == null || busiState.length() == 0)
		{
			return MyBackInfo.fail(properties, "'busiState'不能为空");
		}*/
		/*if(eCode == null || eCode.length() == 0)
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
		if(userUpdateId == null || userUpdateId < 1)
		{
			return MyBackInfo.fail(properties, "'userUpdate'不能为空");
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
		}*/
		/*if(busiType == null || busiType.length() == 0)
		{
			return MyBackInfo.fail(properties, "'busiType'不能为空");
		}*/
		if(theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "附件类型名称不能为空");
		}
		if(acceptFileType == null || acceptFileType.length() == 0)
		{
			return MyBackInfo.fail(properties, "可接受文件类型不能为空");
		}
		if(acceptFileCount == null || acceptFileCount < 1)
		{
			return MyBackInfo.fail(properties, "可接收文件数量不能为空");
		}
		if(maxFileSize == null || maxFileSize < 1)
		{
			return MyBackInfo.fail(properties, "文件大小的最大值不能为空");
		}
		if(minFileSize == null || minFileSize < 1)
		{
			return MyBackInfo.fail(properties, "文件大小的最小值不能为空");
		}
		if(remark == null || remark.length() == 0)
		{
			return MyBackInfo.fail(properties, "备注不能为空");
		}
		if(isImage == null)
		{
			return MyBackInfo.fail(properties, "是否是图片不能为空");
		}
		if(isNeeded == null)
		{
			return MyBackInfo.fail(properties, "是否必须上传不能为空");
		}
		if(listType == null || listType.length() == 0)
		{
			return MyBackInfo.fail(properties, "附件列表类型不能为空");
		}
		if(isCfgSignature == null || isCfgSignature.length() == 0)
		{
			return MyBackInfo.fail(properties, "请选择是否需要签章");
		}

		Sm_User userStart = model.getUser();
		if(userStart == null)
		{
			return MyBackInfo.fail(properties, "用户未登录，请先登录！");
		}
		
		//选择文件签章时，可接受类型必须是PDF
		if("1".equals(isCfgSignature))
		{
			if(!"PDF".equals(acceptFileType))
			{
				return MyBackInfo.fail(properties, "需要签章时，文件类型必须是PDF！");
			}
		}
		
		//获取可接受文件的类型
		String newacceptFileType="";
		String[] acceptFileTypes=acceptFileType.split(",");
		
		for (S_AcceptFileType s_AcceptFileType :S_AcceptFileType.values()) {
			for(int i=0;i<acceptFileTypes.length;i++){
				if(s_AcceptFileType.getName().equals(acceptFileTypes[i])){
					newacceptFileType+=	s_AcceptFileType.getType()+"/";
				}			
			}
		}		
		if(!newacceptFileType.trim().isEmpty()){
			newacceptFileType=newacceptFileType.substring(0,newacceptFileType.length()-1);
		}
				
		Sm_AttachmentCfg sm_AttachmentCfg = new Sm_AttachmentCfg();
		sm_AttachmentCfg.setTheState(model.getTheState());		
		sm_AttachmentCfg.setBusiState(busiState); 
		sm_AttachmentCfg.seteCode(eCode);
		sm_AttachmentCfg.setUserStart(userStart);
		sm_AttachmentCfg.setCreateTimeStamp(createTimeStamp);					
		sm_AttachmentCfg.setTheName(theName);
		sm_AttachmentCfg.setAcceptFileType(newacceptFileType);
		sm_AttachmentCfg.setAcceptFileCount(acceptFileCount);
		sm_AttachmentCfg.setMaxFileSize(maxFileSize);
		sm_AttachmentCfg.setMinFileSize(minFileSize);
		sm_AttachmentCfg.setRemark(remark);
		sm_AttachmentCfg.setIsImage(isImage);
		sm_AttachmentCfg.setIsNeeded(isNeeded);
		sm_AttachmentCfg.setListType(listType);	
		sm_AttachmentCfg.setIsCfgSignature(isCfgSignature);
		sm_AttachmentCfg.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		//保存前，附件类型名称和业务类型关联
		Sm_BaseParameterForm BaseParameterFrom=new Sm_BaseParameterForm();
		BaseParameterFrom.setTheName(busiTypeid);
		//根据业务类型获取业务名称
		@SuppressWarnings("unchecked")
		Query<Sm_BaseParameter> query = sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getTheValueHQL(), BaseParameterFrom);					
		List<Sm_BaseParameter> list =query.list();
		for(Sm_BaseParameter BaseParameter:list){
			if(BaseParameter!=null && BaseParameter.getTheValue()!=""){
				sm_AttachmentCfg.setBusiType(BaseParameter.getTheValue());	
				sm_AttachmentCfg.setBasetheName(BaseParameter.getTheName());
			}
		}
		sm_AttachmentCfgDao.save(sm_AttachmentCfg);	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
