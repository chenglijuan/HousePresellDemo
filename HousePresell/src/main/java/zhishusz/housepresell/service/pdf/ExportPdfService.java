package zhishusz.housepresell.service.pdf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.ureport.build.ReportBuilder;
import com.bstek.ureport.definition.ReportDefinition;
import com.bstek.ureport.export.ReportRender;
import com.bstek.ureport.export.pdf.PdfProducer;
import com.bstek.ureport.model.Report;
import com.bstek.ureport.parser.ReportParser;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesResponse;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgxy_TripleAgreementVerMngForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.FIleDownLoadService;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.fileupload.OssServerUtil;

/*
 * Service PDF打印
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class ExportPdfService
{

	private String fileSuffix = ".pdf";

	private MyDatetime myTime = MyDatetime.getInstance();

	private ReportParser reportParser = new ReportParser();

	private ReportRender reportRender = new ReportRender();

	private ReportBuilder reportBuilder = new ReportBuilder();

	private PdfProducer pdfProducer = new PdfProducer();

	@Autowired
	private Sm_AttachmentCfgDao attacmentcfgDao;

	@Autowired
	private Sm_AttachmentDao attacmentDao;

	public Properties execute(ExportPdfForm model)
	{
		Properties properties = new MyProperties();

		String sourceId = model.getSourceId();// 获取数据来源Id

		String sourceBusiCode = model.getSourceBusiCode();// 获取数据来源编码

		String reqAddress = model.getReqAddress();// 请求地址

		Sm_User user = model.getUser();// 获取登陆用户

		if (null == sourceId || sourceId.trim().length() < 1)
		{
			return MyBackInfo.fail(properties, "关联数据Id为空");
		}

		if (null == sourceBusiCode || sourceBusiCode.trim().length() < 1)
		{
			return MyBackInfo.fail(properties, "关联数据业务编号为空");
		}

		// 初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();

		String localPath = directoryUtil.getProjectRoot();// 项目路径

		if (localPath.contains("%20"))
		{
			localPath = localPath.replace("%20", " ");
		}
		if(localPath.contains("file:/")){
			localPath = localPath.replace("file:/", "");
		}

		// 查询是否已经存在PDF附件
		Sm_Attachment attachment = isSaveAttachment(sourceBusiCode, sourceId,user);
		if (null != attachment)
		{
			String pdfUrl = attachment.getTheLink();

			// 判断地址
			pdfUrl = getUrl(localPath, reqAddress, pdfUrl);

			if (null == pdfUrl)
			{
				return MyBackInfo.fail(properties, "加载oss配置文件失败，请联系管理员");
			}

			properties.put("pdfUrl", pdfUrl);

			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

			return properties;
		}

		// 获取当前日期时间 格式：yyyyMMddhhmmss
		String currentTime = myTime.currentTime();
		// 输出文件名
		String fileOutPath = localPath + currentTime + fileSuffix;

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("table_ID", sourceId);

		String fileName = getFileName(sourceBusiCode, sourceId, localPath);// 获取PDF模板名称

		if (null == fileName)
		{
			return MyBackInfo.fail(properties, "未查询到有效的单据数据，请核对");
		}

		properties = upLoad(fileName, fileOutPath, parameters, sourceId, sourceBusiCode, user, reqAddress, localPath);

		return properties;
	}

	@Autowired
	private OssServerUtil ossUtil;

	/**
	 * 上传OSS-Server
	 * 
	 * @param fileName
	 *            文件名称
	 * @param fileOutPath
	 *            文件输出地址
	 * @param parameters
	 *            查询条件
	 * @param sourceId
	 *            数据来源Id
	 * @param sourceBusiCode
	 *            数据业务编号
	 * @param user
	 *            登陆用户
	 * @param reqAddress
	 *            请求地址
	 * @param localPath
	 *            项目所在地址
	 * @return
	 */
	Properties upLoad(String fileName, String fileOutPath, Map<String, Object> parameters, String sourceId,
			String sourceBusiCode, Sm_User user, String reqAddress, String localPath)
	{
		Properties properties = new Properties();

		OutputStream out = null;
		try
		{
			out = new FileOutputStream(new File(fileOutPath));

			InputStream io = new FileInputStream(new File(fileName));

			ReportDefinition reportDef = reportParser.parse(io, fileName);

			reportRender.rebuildReportDefinition(reportDef);

			Report report = reportBuilder.buildReport(reportDef, parameters);

			pdfProducer.produce(report, out);

			out.flush();
			out.close();
			io.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();

			return MyBackInfo.fail(properties, e.getMessage());
		}
		catch (IOException e)
		{
			e.printStackTrace();

			return MyBackInfo.fail(properties, e.getMessage());
		}

		// 上传Oss-Server
		ReceiveMessage upload = ossUtil.upload(fileOutPath);

		if (null == upload)
		{
			return MyBackInfo.fail(properties, "文件上传失败");
		}

		FileBytesResponse ossMessage = upload.getData().get(0);

		if (null == ossMessage)
		{
			return MyBackInfo.fail(properties, "文件上传失败");
		}

		String httpUrl = ossMessage.getUrl();

		// 保存url
		Sm_Attachment attacment = new Sm_Attachment();
		//attacment.setUserStart(user);
		user = new Sm_User();
		user.setTableId(605L);
		attacment.setUserStart(user);
		attacment.setCreateTimeStamp(System.currentTimeMillis());
		attacment.setUserStart(user);
		attacment.setLastUpdateTimeStamp(System.currentTimeMillis());
		attacment.setTheState(S_TheState.Normal);

		attacment.setTheLink(httpUrl);
		attacment.setSourceId(sourceId);
		attacment.setBusiType(sourceBusiCode);
		attacment.setFileType(ossMessage.getObjType());
		attacment.setTheSize(ossMessage.getByteToStr());
		attacment.setRemark(ossMessage.getOriginalName());

		String attacmentcfg = getFJBusiCode(sourceBusiCode,user);// 获取附件业务编号

		// 根据业务编号查询配置文件
		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setTheState(S_TheState.Normal);
		form.setBusiType(attacmentcfg);

		Sm_AttachmentCfg sm_AttachmentCfg = isSaveAttachmentCfg(attacmentcfg);

		if (null == sm_AttachmentCfg)
		{
			return MyBackInfo.fail(properties, "未进行档案配置");
		}

		attacment.setAttachmentCfg(sm_AttachmentCfg);
		attacment.setSourceType(sm_AttachmentCfg.geteCode());

		attacmentDao.save(attacment);

		String pdfUrl = getUrl(localPath, reqAddress, httpUrl);
		if (null == pdfUrl)
		{
			return MyBackInfo.fail(properties, "加载oss配置文件失败");
		}

		properties.put("pdfUrl", pdfUrl);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * 获取附件配置业务编号
	 * 
	 * @param sourceBusiCode
	 *            单据业务编号
	 * @return
	 */
	String getFJBusiCode(String sourceBusiCode,Sm_User user)
	{

		String attacmentcfg = "";// 附件配置表业务编号
		Integer theType;
		switch (sourceBusiCode)
		{
		case "06120201": // 退房退款-已结清
			
			theType = user.getTheType();//1 - 正泰   2 - 开发企业
			if(null == theType)
			{
				theType = 1;
			}
			
			if(theType == 1)
			{
				attacmentcfg = "240101";
			}
			else
			{
				attacmentcfg = "240121";
			}

			

			break;

		case "06120202": // 退房退款-未结清

			theType = user.getTheType();//1 - 正泰   2 - 开发企业
			if(null == theType)
			{
				theType = 1;
			}
			
			if(theType == 1)
			{
				attacmentcfg = "240102";
			}
			else
			{
				attacmentcfg = "240122";
			}

			break;

		case "21020304": // 风险函提示函

			attacmentcfg = "240103";

			break;

		case "06110201": // 贷款托管合作协议签署

			attacmentcfg = "240104";

			break;

		case "06110301": // 贷款三方托管协议签署

			attacmentcfg = "240105";

			break;

		case "06110304": // 三方协议结算确认

			attacmentcfg = "240106";

			break;

		case "03030101": // 受限额度变更

			attacmentcfg = "240107";

			break;

		case "03030102": // 楼幢托管终止确认

			attacmentcfg = "240108";

			break;

		case "06120303": // 托管资金拨付确认

			attacmentcfg = "240109";

			break;

		case "06120301": // 用款申请拨付确认

			attacmentcfg = "240110";

			break;

		case "06120603": // 特殊拨付确认

			attacmentcfg = "240111";

			break;

		case "03030100": // 进度节点变更

			attacmentcfg = "240125";

			break;
			
		case "06120501": // 支付保函

            attacmentcfg = "240131";
            
            break;
            
		case "06120500": // 支付保函2

            attacmentcfg = "240132";

            break;
            
		case "240180": // 公安-施工编号对照表

            attacmentcfg = "240180";

            break;
			
		default:
			break;
		}
		return attacmentcfg;
	}

	/**
	 * 是否进行档案配置
	 * 
	 * @param attacmentcfg
	 *            档案类型编码
	 * @return
	 */
	Sm_AttachmentCfg isSaveAttachmentCfg(String attacmentcfg)
	{
		// 根据业务编号查询配置文件
		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setTheState(S_TheState.Normal);
		form.setBusiType(attacmentcfg);

		Sm_AttachmentCfg sm_AttachmentCfg = attacmentcfgDao
				.findOneByQuery_T(attacmentcfgDao.getQuery(attacmentcfgDao.getBasicHQL(), form));

		if (null == sm_AttachmentCfg)
		{
			return null;
		}
		return sm_AttachmentCfg;
	}

	/**
	 * 是否保存PDF
	 * 
	 * @param sourceBusiCode
	 *            业务编码
	 * @param sourceId
	 *            业务数据Id
	 * @return
	 */
	Sm_Attachment isSaveAttachment(String sourceBusiCode, String sourceId,Sm_User user)
	{
		String attacmentcfg = getFJBusiCode(sourceBusiCode,user);
		
		Sm_AttachmentCfg sm_AttachmentCfg = isSaveAttachmentCfg(attacmentcfg);
		
		if (null == sm_AttachmentCfg)
		{
			return null;
		}
		
		Sm_AttachmentForm form = new Sm_AttachmentForm();
		form.setSourceId(sourceId);
		form.setBusiType(sourceBusiCode);
		form.setSourceType(sm_AttachmentCfg.geteCode());
		form.setTheState(S_TheState.Normal);
		form.setAttachmentCfg(sm_AttachmentCfg);

		Sm_Attachment attachment = attacmentDao
				.findOneByQuery_T(attacmentDao.getQuery(attacmentDao.getBasicHQL(), form));

		if (null == attachment)
		{
			return null;
		}
		return attachment;
	}

	/**
	 * 获取Oss-server路径
	 * 
	 * @param localPath
	 *            项目所在地址
	 * @param reqAddress
	 *            请求路径
	 * @param httpUrl
	 *            oss地址
	 * @return
	 */
	String getUrl(String localPath, String reqAddress, String httpUrl)
	{
		String pdfUrl = "";
		/**
		 * 判断请求地址是否是内网地址
		 * 如果是内网地址，判断oss-server是否是内网地址。 如果是返回地址，如果不是替换oss-server地址
		 */
		if (FIleDownLoadService.isInner(reqAddress) && FIleDownLoadService.isInner(httpUrl))
		{
			pdfUrl = httpUrl;
		}
		else
		{
			if (!FIleDownLoadService.isInner(reqAddress))
			{
				Properties pro = null;
				try
				{
					String uploadUrl = localPath + "WEB-INF/classes/Oss-Server.properties";// 项目路径

					// 创建对象
					pro = new Properties();
					InputStream in = new BufferedInputStream(new FileInputStream(uploadUrl));
					pro.load(in);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return null;
				}

				String ossURL = pro.getProperty("remote");// 获取oss本地路径

				String spiltUrl = httpUrl.substring(httpUrl.indexOf("http://"), httpUrl.lastIndexOf(":"));

				httpUrl = httpUrl.replace(spiltUrl, ossURL);// 替换下载路径

				pdfUrl = httpUrl;
			}
		}
		return pdfUrl;
	}

	@Autowired
	private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;// 查询三方协议

	@Autowired
	private Tgxy_TripleAgreementVerMngDao tgxy_TripleAgreementVerMngDao;// 三方协议版本控制

	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;// 托管合作协议

	/**
	 * 获取PDF模板名称
	 * 
	 * @param sourceBusiCode
	 *            业务单据编码
	 * @param sourceId
	 *            业务数据Id
	 * @param localPath
	 *            项目路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	String getFileName(String sourceBusiCode, String sourceId, String localPath)
	{
		String fileName = "";

		String theVresion = "0";// 版本号

		switch (sourceBusiCode)
		{
		case "06120201": // 退房退款-已结清

			fileName = localPath + sourceBusiCode +"_" + theVresion + ".ureport.xml";

			break;

		case "06120202": // 退房退款-未结清

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;

		case "21020304": // 风险函提示函

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;

		case "06110201": // 贷款托管合作协议签署

			// 查询版本号
			Tgxy_EscrowAgreement tgxy_EscrowAgreement = tgxy_EscrowAgreementDao.findById(new Long(sourceId));
			if (null == tgxy_EscrowAgreement)
			{
				return null;
			}

			theVresion = tgxy_EscrowAgreement.getAgreementVersion();

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;

		case "06110301": // 贷款三方托管协议签署

			// 查询版本号
			Tgxy_TripleAgreement tripleAgreement = tgxy_TripleAgreementDao.findById(new Long(sourceId));
			if (null == tripleAgreement)
			{
				return null;
			}

			Tgxy_TripleAgreementVerMngForm tripleAgreementVerMngForm = new Tgxy_TripleAgreementVerMngForm();
			tripleAgreementVerMngForm.setEnableTimeStamp(
					myTime.stringToLong(tripleAgreement.getTripleAgreementTimeStamp(), "yyyy-MM-dd HH:mm:ss"));
			tripleAgreementVerMngForm.setTheState(S_TheState.Normal);
			List<Tgxy_TripleAgreementVerMng> tripleAgreementVerMngList = tgxy_TripleAgreementVerMngDao
					.findByPage(tgxy_TripleAgreementVerMngDao
							.getQuery(tgxy_TripleAgreementVerMngDao.getBasicHQLByTiem(), tripleAgreementVerMngForm));
			if (null != tripleAgreementVerMngList && tripleAgreementVerMngList.size() >= 1)
			{
				for (Tgxy_TripleAgreementVerMng tripleAgreementVerMng : tripleAgreementVerMngList)
				{
					// 获取停用日期
					Long downTimeStamp = tripleAgreementVerMng.getDownTimeStamp();

					if (downTimeStamp >= myTime.stringToLong(tripleAgreement.getTripleAgreementTimeStamp(),
							"yyyy-MM-dd HH:mm:ss"))
					{

						theVresion = tripleAgreementVerMng.getTheVersion();

					}
				}
			}

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;

		case "06110304": // 三方协议结算确认

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;

		case "03030101": // 受限额度变更

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;

		case "03030102": // 楼幢托管终止确认

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;

		case "06120303": // 托管资金拨付确认

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;

		case "06120301": // 用款申请拨付确认

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;

		case "06120603": // 特殊拨付确认

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;
			    
		case "03030100": // 进度节点变更

			fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

			break;
			
		case "06120501": // 支付保函

            fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

            break;
            
		case "06120500": // 支付保函2

            fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

            break;
            
		case "240180": // 公安-施工编号对照表

            fileName = localPath + sourceBusiCode + "_" + theVresion + ".ureport.xml";

            break;

		default:
			break;
		}

		return fileName;
	}
}
