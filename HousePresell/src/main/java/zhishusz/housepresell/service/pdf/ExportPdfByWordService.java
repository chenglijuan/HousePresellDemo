package zhishusz.housepresell.service.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.aspectj.weaver.ast.Test;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.google.gson.Gson;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesResponse;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.pdf.ExportPdfForm;
import zhishusz.housepresell.controller.form.pdf.Sm_ExportPdfModelInfoForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_ExportPdfModelInfoDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementDao;
import zhishusz.housepresell.database.dao.Tgxy_TripleAgreementVerMngDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.pdf.Sm_ExportPdfModelInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.pdf.WordTemplate;
import zhishusz.housepresell.util.picture.MatrixUtil;

/*
 * Service Word 转 PDF
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class ExportPdfByWordService
{

	private String fileSuffix = ".pdf";
	
	private String picName = "b.png";

	private MyDatetime myTime = MyDatetime.getInstance();

	@Autowired
	private Sm_AttachmentDao attacmentDao;

	@Autowired
	private ExportPdfService service;

	@Autowired
	private Sm_ExportPdfModelInfoDao sm_ExportPdfModelInfoDao;
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
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
		
		String attacmentcfg = service.getFJBusiCode(sourceBusiCode,user);// 获取附件业务编号

		Sm_AttachmentCfg sm_AttachmentCfg = service.isSaveAttachmentCfg(attacmentcfg);

		if (null == sm_AttachmentCfg)
		{
			return MyBackInfo.fail(properties, "未进行档案配置");
		}

		// 初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();

		String localPath = directoryUtil.getProjectRoot();// 项目路径

		System.out.println("localPath="+localPath);

		if (localPath.contains("%20"))
		{
			localPath = localPath.replace("%20", " ");
		}
		if (localPath.contains("file:/"))
		{
			localPath = localPath.replace("file:/", "");
		}

		// 查询是否已经存在PDF附件
		Sm_Attachment attachment = service.isSaveAttachment(sourceBusiCode, sourceId,user);
		if (null != attachment)
		{
			String pdfUrl = attachment.getTheLink();

			// 判断地址
			/*pdfUrl = service.getUrl(localPath, reqAddress, pdfUrl);

			if (null == pdfUrl)
			{
				return MyBackInfo.fail(properties, "加载oss配置文件失败，请联系管理员");
			}*/

			properties.put("pdfUrl", pdfUrl);

			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

			return properties;
		}

		// 获取当前日期时间 格式：yyyyMMddhhmmss
		String currentTime = myTime.currentTime();
		// 输出文件名
		String fileOutPath = localPath + currentTime + fileSuffix;

		/*
		 * 获取Word 报表名称
		 * 查询模板配置表获取对应的信息
		 */
		String busiCode = getFileName(sourceBusiCode, sourceId,user);
		
		Sm_ExportPdfModelInfoForm form = new Sm_ExportPdfModelInfoForm();
		form.setIsUsing(1);
		form.setBusiCode(busiCode);

		Sm_ExportPdfModelInfo pdfModelInfo = sm_ExportPdfModelInfoDao
				.findOneByQuery_T(sm_ExportPdfModelInfoDao.getQuery(sm_ExportPdfModelInfoDao.getBasicHQL(), form));

		if (null == pdfModelInfo)
		{
			return MyBackInfo.fail(properties, "未查询到有效的打印模板配置");
		}

		Map<String, Object> tables = new HashMap<String, Object>();
		
		/*tables.put("table2", "SELECT tableid,ecodefromconstruction FROM empj_buildinginfo where project = ?");
		
		String sqls = g.toJson(tables);*/

		String querySql = pdfModelInfo.getQuerySql();

		if (null != querySql && querySql.trim().length() > 1)
		{
			Gson g = new Gson();

			tables = g.fromJson(querySql.trim(), Map.class);
		}

		String wordName = getWord(pdfModelInfo.getUrl(), pdfModelInfo.getDriver(), pdfModelInfo.getUserName(),
				pdfModelInfo.getPassWord(), tables, pdfModelInfo.getParametersMap(), sourceId,
				pdfModelInfo.getImpModelPath(), localPath, pdfModelInfo.getQrCodePath());

		if (null == wordName || wordName.trim().isEmpty() || wordName.contains("fail"))
		{
			return MyBackInfo.fail(properties, "模板转Word失败");
		}
		
		//方法1
		//doc2pdf(wordName, fileOutPath);
		//方法2
		word2pdf(wordName, fileOutPath);
		
		//保存
		properties = upLoad(fileOutPath, sourceId, sourceBusiCode, user, reqAddress, localPath);
		
		//更新业务单据打印状态
		
		updatePrintStatus(sourceBusiCode,sourceId);

		return properties;
	}

	/**
	 * 获取word信息
	 * 
	 * @param url
	 *            数据库URL
	 * @param driver
	 *            数据库驱动
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param tables
	 *            子表（存放Sql）
	 * @param parametersMap
	 *            主表（存放Sql）
	 * @param sourceId
	 *            业务Id
	 * @param impPaht
	 *            导入模板位置
	 * @param loacalPath
	 *            本地项目路径
	 * @param codeNum
	 *            二维码内容
	 * @return
	 */
	String getWord(String url, String driver, String username, String password, Map<String, Object> tables,
			String parametersMap, String sourceId, String impPaht, String loacalPath, String codeNum)
	{

		Map<String, Object> wordDataMap = new HashMap<String, Object>();// 存储报表全部数据

		Map<String, Object> parametersMap1 = new HashMap<String, Object>();// 存储报表中不循环的数据

		try
		{
			// 加载驱动
			Class.forName(driver.trim());
			// 建立连接
			Connection conn = DriverManager.getConnection(url, username, password);
			// 报表不循环的数据
			PreparedStatement prs = conn.prepareStatement(parametersMap);
			prs.setString(1, sourceId);
			ResultSet resultSet = prs.executeQuery();
			// 把结果集（map）封装到list中为一个表
			while (resultSet.next())
			{
				ResultSetMetaData metaData = prs.getMetaData();
				for (int i = 1; i <= metaData.getColumnCount(); i++)
				{
					String key = metaData.getColumnName(i);
					parametersMap1.put(key, resultSet.getString(key));
				}
			}
			
			if (null != codeNum && codeNum.trim().length() > 0)
			{
				// 获取二维码内容
				String content = (String) parametersMap1.get(codeNum);

				MatrixUtil.inserPIC2(content, loacalPath);

				Map<String, Object> header = new HashMap<String, Object>();
				header.put("width", 50);
				header.put("height", 50);
				header.put("type", "png");
				header.put("content", inputStream2ByteArray(new FileInputStream(loacalPath + picName), true));
				parametersMap1.put("QR", header);

				// 删除文件
				deleteFile(loacalPath + picName);
			}
			
			wordDataMap.put("parametersMap", parametersMap1);

			// 报表循环的数据
			Iterator<Entry<String, Object>> iterator = tables.entrySet().iterator();
			while (iterator.hasNext())
			{
				// 建表
				List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();
				Entry<String, Object> entry = iterator.next();
				String table = entry.getKey();
				String sql = (String) entry.getValue();
				// get sql 的 字段名select name,age,email from project
				PreparedStatement pre = conn.prepareStatement(sql);
				pre.setString(1, sourceId);
				ResultSet rs = pre.executeQuery();
				// 把结果集（map）封装到list中为一个表
				while (rs.next())
				{
					Map<String, Object> map1 = new HashMap<>();
					ResultSetMetaData metaData = rs.getMetaData();
					for (int i = 1; i <= metaData.getColumnCount(); i++)
					{
						String key = metaData.getColumnName(i);
						map1.put(key, rs.getString(key));
					}
					table1.add(map1);
				}
				wordDataMap.put(table, table1);
				pre.close();
				rs.close();
			}

			// 关闭连接
			prs.close();
			resultSet.close();
			conn.close();

			// 读取word模板
			File file = new File(impPaht);// 改成你本地文件所在目录
			if (!file.exists())
			{
				return "fail";
			}

			FileInputStream fileInputStream = new FileInputStream(file);
			WordTemplate template = new WordTemplate(fileInputStream);

			// 替换数据
			template.replaceDocument(wordDataMap);

			// 生成文件
			String outputPath = loacalPath + System.currentTimeMillis() + ".docx";
			File outputFile = new File(outputPath);// 改成你本地文件所在目录
			FileOutputStream fos = new FileOutputStream(outputFile);
			template.getDocument().write(fos);

			return outputPath;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "fail";
	}
	
	static final int wdDoNotSaveChanges = 0;// 不保存待定的更改。
	static final int wdFormatPDF = 17;// word转PDF 格式
	
	synchronized boolean word2pdf(String source, String target) {
		System.out.println("Word转PDF开始启动...");
		long start = System.currentTimeMillis();
		ActiveXComponent app = null;
		try {
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", false);
			Dispatch docs = app.getProperty("Documents").toDispatch();
			System.out.println("打开文档：" + source);
			Dispatch doc = Dispatch.call(docs, "Open", source, false, true).toDispatch();
			System.out.println("转换文档到PDF：" + target);
			File tofile = new File(target);
			if (tofile.exists()) {
				tofile.delete();
			}
			Dispatch.call(doc, "SaveAs", target, wdFormatPDF);
			Dispatch.call(doc, "Close", false);
			long end = System.currentTimeMillis();
			System.out.println("转换完成，用时：" + (end - start) + "ms");
			
			//删除文件
			deleteFile(source);
			
			return true;
		} catch (Exception e) {
			System.out.println("Word转PDF出错：" + e.getMessage());
			
			if(e.getMessage().contains("Could not initialize class com.jacob.activeX.ActiveXComponent")){
				System.out.println("请配置jacob.dll到 C:/Windows/System32 该路径下面去");
			}
			return false;
		} finally {
			if (app != null) {
				app.invoke("Quit", wdDoNotSaveChanges);
			}
		}
	}
	
	boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = Test.class.getClassLoader().getResourceAsStream("license.xml"); // license.xml应放在..\WebRoot\WEB-INF\classes路径下
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
 
   void doc2pdf(String inPath, String outPath) {
        if (!getLicense()) { // 验证License 若不验证则转化出的pdf文档会有水印产生
            return;
        }
        try {
            long old = System.currentTimeMillis();
            File file = new File(outPath); // 新建一个空白pdf文档
            FileOutputStream os = new FileOutputStream(file);
            Document doc = new Document(inPath); // Address是将要被转化的word文档
            doc.save(os, SaveFormat.PDF);// 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF,
                                         // EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒"); // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
   @Autowired
	private OssServerUtil ossUtil;
   
   /**
	 * 上传OSS-Server
	 * 
	 * @param fileOutPath
	 *            文件输出地址
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
	Properties upLoad(String fileOutPath, String sourceId,
			String sourceBusiCode, Sm_User user, String reqAddress, String localPath)
	{
		Properties properties = new Properties();
		
		System.out.println("dayin：" + " #fileOutPath = " + fileOutPath + " #sourceId = " + sourceId + " #reqAddress = " + reqAddress + " #localPath = " + localPath);

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

		//删除文件
//		deleteFile(fileOutPath);
		
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

		String attacmentcfg = service.getFJBusiCode(sourceBusiCode,user);// 获取附件业务编号

		// 根据业务编号查询配置文件
		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setTheState(S_TheState.Normal);
		form.setBusiType(attacmentcfg);

		Sm_AttachmentCfg sm_AttachmentCfg = service.isSaveAttachmentCfg(attacmentcfg);

		if (null == sm_AttachmentCfg)
		{
			return MyBackInfo.fail(properties, "未进行档案配置");
		}

		attacment.setAttachmentCfg(sm_AttachmentCfg);
		attacment.setSourceType(sm_AttachmentCfg.geteCode());

		attacmentDao.save(attacment);

		/*String pdfUrl = service.getUrl(localPath, reqAddress, httpUrl);
		if (null == pdfUrl)
		{
			return MyBackInfo.fail(properties, "加载oss配置文件失败");
		}*/

		properties.put("pdfUrl", httpUrl);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	String getFileName(String sourceBusiCode, String sourceId,Sm_User user)
	{
		String attacmentcfg = "";// 附件配置表业务编号
		
		String fileName = "";

		String theVresion = "0";// 版本号

		Integer theType;
		switch (sourceBusiCode)
		{
		case "06120201": // 退房退款-已结清
			
//			attacmentcfg = "240101";
			
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

			fileName = attacmentcfg +"_" + theVresion ;

			break;

		case "06120202": // 退房退款-未结清
			
//			attacmentcfg = "240102";
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

			fileName = attacmentcfg + "_" + theVresion ;

			break;

		case "21020304": // 风险函提示函
			
			attacmentcfg = "240103";

			fileName = attacmentcfg + "_" + theVresion ;

			break;

		case "06110201": // 贷款托管合作协议签署

			// 查询版本号
			Tgxy_EscrowAgreement tgxy_EscrowAgreement = tgxy_EscrowAgreementDao.findById(new Long(sourceId));
			if (null == tgxy_EscrowAgreement)
			{
				return null;
			}

			theVresion = tgxy_EscrowAgreement.getAgreementVersion();
			
			attacmentcfg = "240104";

			fileName = attacmentcfg + "_" + theVresion ;

			break;

		case "06110301": // 贷款三方托管协议签署

			// 查询版本号
			Tgxy_TripleAgreement tripleAgreement = tgxy_TripleAgreementDao.findById(new Long(sourceId));
			if (null == tripleAgreement)
			{
				return null;
			}
			
			//查询合作协议	
			String sql="select d.theversion " + 
					"  from tgxy_tripleagreementvermng d " + 
					" where exists (select AGREEMENTVERSION " + 
					"          from Tgxy_EscrowAgreement b " + 
					"         where theState = 0 " + 
					"           and businessProcessState = '7' " + 
					"           and exists (select null " + 
					"                  from Rel_EscrowAgreement_Building A " + 
					"                 where a.tgxy_escrowagreement = b.tableid " + 
					"                   and a.empj_buildinginfo = "+ tripleAgreement.getBuildingInfo().getTableId() + ") " + 
					"           and b.AGREEMENTVERSION = d.ecodeofca) " ; 
			
			theVresion = sessionFactory.getCurrentSession().createSQLQuery(sql).uniqueResult().toString();			
			
			
			attacmentcfg = "240105";

			fileName = attacmentcfg + "_" + theVresion ;

			break;

		case "06110304": // 三方协议结算确认
			
			attacmentcfg = "240106";

			fileName = attacmentcfg + "_" + theVresion ;

			break;

		case "03030101": // 受限额度变更
			
			attacmentcfg = "240107";

			fileName = attacmentcfg + "_" + theVresion ;

			break;

		case "03030102": // 楼幢托管终止确认
			
			attacmentcfg = "240108";

			fileName = attacmentcfg + "_" + theVresion ;

			break;

		case "06120303": // 托管资金拨付确认
			
			attacmentcfg = "240109";

			fileName = attacmentcfg + "_" + theVresion ;

			break;

		case "06120301": // 用款申请拨付确认
			
			attacmentcfg = "240110";

			fileName = attacmentcfg + "_" + theVresion ;

			break;

		case "06120603": // 特殊拨付确认
			
			attacmentcfg = "240111";

			fileName = attacmentcfg + "_" + theVresion ;

			break;
		
		case "03030100": // 进度节点变更
			
			attacmentcfg = "240125";

			fileName = attacmentcfg + "_" + theVresion ;

			break;
			
		case "06120501": // 支付保函
            
            attacmentcfg = "240131";

            fileName = attacmentcfg + "_" + theVresion ;

            break;
            
		case "06120500": // 支付保函2
            
            attacmentcfg = "240132";

            fileName = attacmentcfg + "_" + theVresion ;

            break;
            
		case "240180": // 公安-施工编号对照表
            
            attacmentcfg = "240180";

            fileName = attacmentcfg + "_" + theVresion ;

            break;

		default:
			break;
		}

		return fileName;
	}
	
	//删除文件
	private void deleteFile(String localPath)
	{
		File file = new File(localPath);

		if (file.isFile() && file.exists())
		{
			file.delete();
		}
	}
	/**
	 * 打印后更新业务单对应的状态
	 * 
	 * @param sourceBusiCode
	 *            业务单据编码
	 * @param sourceId
	 *            业务数据Id
	 * @return
	 */
	private void updatePrintStatus(String sourceBusiCode, String sourceId) {
		switch (sourceBusiCode)
		{
		case "06110301": // 贷款三方托管协议签署
			Tgxy_TripleAgreement tripleAgreement = tgxy_TripleAgreementDao.findById(new Long(sourceId));
			if("0".equals(tripleAgreement.getTheStateOfTripleAgreement())) {
				tripleAgreement.setTheStateOfTripleAgreement("1");
			}
			tgxy_TripleAgreementDao.save(tripleAgreement);
			break;

		default:
			break;
		}
	}
	
	byte[] inputStream2ByteArray(InputStream in,boolean isClose){  
        byte[] byteArray = null;  
        try {  
            int total = in.available();  
            byteArray = new byte[total];  
            in.read(byteArray);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            if(isClose){  
                try {  
                    in.close();  
                } catch (Exception e2) {  
                    System.out.println("关闭流失败");  
                }  
            }  
        }  
        return byteArray;  
    } 
}
