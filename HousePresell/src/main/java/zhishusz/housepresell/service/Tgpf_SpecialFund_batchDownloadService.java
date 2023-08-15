package zhishusz.housepresell.service;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.google.gson.Gson;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_SpecialFundAppropriated_AFForm;
import zhishusz.housepresell.controller.form.pdf.Sm_ExportPdfModelInfoForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.pdf.Sm_ExportPdfModelInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.service.pdf.ExportPdfService;
import zhishusz.housepresell.util.*;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.pdf.WordTemplate;
import zhishusz.housepresell.util.picture.MatrixUtil;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
 * Service批量删除：特殊拨付-申请主表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_SpecialFund_batchDownloadService
{
	private static String BUSI_CODE = "061206";

	@Autowired
	private Sm_AttachmentDao sm_attacmentDao;

	@Autowired
	private Sm_ExportPdfModelInfoDao sm_exportPdfModelInfoDao;

	@Autowired
	private Tgpf_SpecialFundAppropriated_AFDao tgpf_specialFundAppropriated_afDao;


	private String sourceBusiCode = "06120603";

	private String fileSuffix = ".pdf";



	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_SpecialFundAppropriated_AFForm model,HttpServletResponse response)
	{
		Properties properties = new MyProperties();
		Sm_User user = model.getUser();
		if (null == user)
		{
			return MyBackInfo.fail(properties, "登录失效，请重新登录");
		}
		Long[] idArr = model.getIdArr();
		if(idArr == null){
			return MyBackInfo.fail(properties, "请选择需要下载的附件");
		}

		//附件的网络地址
		List<String> filePaths = new ArrayList<>();

		Sm_AttachmentForm form = new Sm_AttachmentForm();
		form.setBusiType(sourceBusiCode);
		form.setSourceType("010201N18121400002");
		form.setTheState(S_TheState.Normal);


		//初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String localPath = directoryUtil.getProjectRoot();//项目路径

		if (localPath.contains("%20"))
		{
			localPath = localPath.replace("%20", " ");
		}


		// 获取当前日期时间 格式：yyyyMMddhhmmss
		String currentTime = MyDatetime.getInstance().currentTime();
		// 输出文件名
		String fileOutPath = localPath + currentTime + fileSuffix;


		/*
		 * 获取Word 报表名称
		 * 查询模板配置表获取对应的信息
		*/
		Sm_ExportPdfModelInfoForm modelInfoForm = new Sm_ExportPdfModelInfoForm();
		modelInfoForm.setIsUsing(1);
		form.setBusiCode("240111_0");

		Sm_ExportPdfModelInfo pdfModelInfo = sm_exportPdfModelInfoDao
				.findOneByQuery_T(sm_exportPdfModelInfoDao.getQuery(sm_exportPdfModelInfoDao.getBasicHQL(), form));

		if (null == pdfModelInfo)
		{
			return MyBackInfo.fail(properties, "未查询到有效的打印模板配置");
		}

		Map<String, Object> tables = new HashMap<String, Object>();

		String querySql = pdfModelInfo.getQuerySql();

		if (null != querySql && querySql.trim().length() > 1)
		{
			Gson g = new Gson();

			tables = g.fromJson(querySql.trim(), Map.class);
		}

		System.out.println("localPath="+localPath);

		Properties tempp = null;
		for (int i = 0; i < idArr.length ; i++) {
			String sourceid = idArr[i]+"";

			Long tgpf_SpecialFundAppropriated_AFId = idArr[i];
			Tgpf_SpecialFundAppropriated_AF tgpf_SpecialFundAppropriated_AF = (Tgpf_SpecialFundAppropriated_AF) tgpf_specialFundAppropriated_afDao
					.findById(tgpf_SpecialFundAppropriated_AFId);
			// 如果状态已为已经拨付的
			/*
			 * 1-未拨付(初始）
			 * 2-已拨付（出纳完成系统的资金拨付更新）
			 * applyState
			 */

			if(tgpf_SpecialFundAppropriated_AF.getApplyState() == null || 1 == tgpf_SpecialFundAppropriated_AF.getApplyState().intValue())
				continue;

			tempp = new Properties();
			// 查询是否已经存在PDF附件
			form.setSourceId(sourceid);
			Sm_Attachment attachment = sm_attacmentDao.findOneByQuery_T(sm_attacmentDao.getQuery(sm_attacmentDao.getBasicHQL(), form));
			if(attachment != null){
				//存在返回
				filePaths.add(attachment.getTheLink());
				continue;
			}
//			//不在打印之后返回
//
			String wordName = getWord(pdfModelInfo.getUrl(), pdfModelInfo.getDriver(), pdfModelInfo.getUserName(),
					pdfModelInfo.getPassWord(), tables, pdfModelInfo.getParametersMap(), sourceid,
					pdfModelInfo.getImpModelPath(), localPath);
			System.out.println("wordName="+wordName);
//
			if (null == wordName || wordName.trim().isEmpty() || wordName.contains("fail"))
			{
				return MyBackInfo.fail(properties, "模板转Word失败");
			}
			word2pdf(wordName, fileOutPath);

		//保存
			tempp = upLoad(fileOutPath, sourceid, sourceBusiCode, localPath);

			if("success".equals(tempp.get("result"))){
				System.out.println("新的附件地址是："+tempp.get("pdfUrl"));
				filePaths.add((String)tempp.get("pdfUrl"));
			}
		}

		if(filePaths == null || filePaths.size() == 0 ){
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, "没有可以打印的数据");
			return properties;
		}


		System.out.println("开始");

		//网络附件地址下载
		List<String> resultPath = PdfToZipUtil.downloadOnlineZip(filePaths);
		for (int i = 0; i < resultPath.size(); i++) {
			System.out.println(resultPath.get(i));
		}


		//小于两个pdf的 不用合并
		if(resultPath.size() < 2){
			properties.put("fileURL",   filePaths.get(0));
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			return properties;
		}

		//生成压缩文件
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String pdfresultName = sdf.format(new Date()) + "_特殊拨付" + ".pdf";
		PdfToZipUtil.mergePdfFiles(resultPath,localPath + pdfresultName);

		properties.put("fileURL",   pdfresultName);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
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
	 *            请求地址
	 * @param localPath
	 *            项目所在地址
	 * @return
	 */
	Properties upLoad(String fileOutPath, String sourceId,
					  String sourceBusiCode, String localPath)
	{
		Properties properties = new Properties();

		System.out.println("dayin：" + " #fileOutPath = " + fileOutPath + " #sourceId = " + sourceId  + " #localPath = " + localPath);

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
		Sm_User user = new Sm_User();
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


		// 根据业务编号查询配置文件
		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setTheState(S_TheState.Normal);
		form.setBusiType(sourceBusiCode);

		Sm_AttachmentCfg cfg = new Sm_AttachmentCfg();
		cfg.setTableId(28l);
		cfg.seteCode("010201N18121400002");
		attacment.setAttachmentCfg(cfg);

		attacment.setSourceType("010201N18121400002");

		sm_attacmentDao.save(attacment);
		properties.put("pdfUrl", httpUrl);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

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
	 * @return
	 */
	String getWord(String url, String driver, String username, String password, Map<String, Object> tables,
				   String parametersMap, String sourceId, String impPaht, String loacalPath)
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

			wordDataMap.put("parametersMap", parametersMap1);

			// 报表循环的数据
			Iterator<Map.Entry<String, Object>> iterator = tables.entrySet().iterator();
			while (iterator.hasNext())
			{
				// 建表
				List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();
				Map.Entry<String, Object> entry = iterator.next();
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

	//删除文件
	private void deleteFile(String localPath)
	{
		File file = new File(localPath);

		if (file.isFile() && file.exists())
		{
			file.delete();
		}
	}



}
