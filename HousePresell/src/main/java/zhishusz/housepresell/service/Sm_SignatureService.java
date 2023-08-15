package zhishusz.housepresell.service;import zhishusz.housepresell.controller.form.Sm_AttachmentForm;import zhishusz.housepresell.controller.form.Sm_SignatureForm;import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;import zhishusz.housepresell.database.dao.Sm_AttachmentDao;import zhishusz.housepresell.database.dao.Sm_UserDao;import zhishusz.housepresell.database.po.Sm_Attachment;import zhishusz.housepresell.database.po.Sm_User;import zhishusz.housepresell.database.po.state.S_TheState;import zhishusz.housepresell.util.MyBackInfo;import zhishusz.housepresell.util.MyProperties;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import java.util.Properties;import java.util.Random;import javax.transaction.Transactional;/* * Service添加操作：签章service * Company：ZhiShuSZ */@Service@Transactionalpublic class Sm_SignatureService{	@Autowired	private Sm_AttachmentDao sm_AttachmentDao;// 附件	@Autowired	private Sm_UserDao sm_UserDao;// 用户	@SuppressWarnings("unchecked")	public Properties execute(Sm_SignatureForm model)	{		Properties properties = new MyProperties();		// 当前操作用户（调用传递）		Sm_User sm_User = model.getSm_User();		// 业务编码（调用传递）		String busi_code = model.getBusi_code();		// 单据主键（调用传递）		Long tableId = model.getTableId();		// 附件配置eCode		// String sourceType = model.getSourceType();		String sourceType = model.getPicTypeCode();		// 设备码		String ukeyRealNumber = model.getUkeyRealNumber();		/*		 * 根据以上信息查询需要签章的文件，		 * 并将签章所需信息返回到前端调用签章接口		 */		Map<String, Object> map = new HashMap<>();		map.put("ukeyNumber", sm_User.getUkeyNumber());		// 根据业务类型、单据Id及附件配置eCode查询需要签章的PDF		Sm_AttachmentForm attachmentModel = new Sm_AttachmentForm();		attachmentModel.setTheState(S_TheState.Normal);		attachmentModel.setBusiType(busi_code);		attachmentModel.setSourceId(String.valueOf(tableId));		attachmentModel.setSourceType(sourceType);		List<Sm_Attachment> list;		list = sm_AttachmentDao				.findByPage(sm_AttachmentDao.getQuery_Size(sm_AttachmentDao.getBasicHQL(), attachmentModel));		if (null != list && list.size() > 0)		{			/*			 * 校验当前登录人是否具有签章权限及当前登录人与设备码是否匹配			 */			String isSignature = sm_User.getIsSignature();			if (null == isSignature || "0".equals(isSignature))			{				return MyBackInfo.fail(properties, "当前登录用户不具备签章权限！");			}			String ukeyNumber = sm_User.getUkeyNumber();			if (!ukeyRealNumber.equals(ukeyNumber))			{				return MyBackInfo.fail(properties, "当前登录用户UKEY不匹配！");			}			// 生成随机坐标 x = {0~400}; y = {0~700}			Random r = new Random();			int x = r.nextInt(300);			int y = r.nextInt(700);			map.put("x", x);			map.put("y", y);		}		else		{			list = new ArrayList<Sm_Attachment>();		}		map.put("Sm_SignatureList", list);				properties.put("signatureMap", map);		return properties;	}}