package zhishusz.housepresell.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.zip.ZipUtil;

/*
 * Service 文件下载操作：文件下载
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class FIleDownLoadService
{

	// 压缩图片地址
	private static String zipFilePath = "fileDoc.zip";
	// 文件下载根文件夹
	private static String fileName = "fileDoc";

	public Properties execute(Sm_AttachmentForm model)
	{
		Properties properties = new MyProperties();

		String smAttachmentList = model.getSmAttachmentList();
		// smAttachmentList =
		// "[{\"sourceType\":\"营业执照\",\"theLink\":\"http://192.168.1.8:19001/OssSave/bananaUpload/201808/23/eabf01f1c8214073a012fb6c465af7b4.png\",\"fileType\":\"png\",\"theSize\":\"23.85KB\",\"remark\":\"\",\"id\":\"eabf01f1c8214073a012fb6c465af7b4\"},{\"sourceType\":\"营业执照\",\"theLink\":\"http://192.168.1.8:19001/OssSave/bananaUpload/201808/23/eabf01f1c8214073a012fb6c465af7b4.png\",\"fileType\":\"png\",\"theSize\":\"23.85KB\",\"remark\":\"\",\"id\":\"eabf01f1c8214073a012fb6c465af7b4\"}]";
		if (smAttachmentList == null || smAttachmentList.length() < 1)
		{
			return MyBackInfo.fail(properties, "附件不能为空");
		}
		List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);


		if (null == gasList || gasList.size() < 1)
		{
			return MyBackInfo.fail(properties, "附件不能为空");
		}

		OssServerUtil ossUtil = new OssServerUtil();

		//初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String localPath = directoryUtil.getProjectRoot();//项目路径
		
		if (localPath.contains("%20"))
		{
			localPath = localPath.replace("%20", " ");
		}

		// 创建文件类型文件夹
		//String folder = gasList.get(0).getFileType();
		
		String fileNamePath = localPath + fileName;
		File file = new File(fileNamePath);
		if (!file.exists())
		{
			file.mkdir();
		}
		
		List<String> fileTypeList = new ArrayList<String>();

		for (Sm_Attachment sm_Attachment : gasList)
		{
			/*
			 * String uuid =
			 * UUID.randomUUID().toString().split("-")[0]+Long.toString(System.
			 * currentTimeMillis());
			 */
			String folder = sm_Attachment.getSourceType();
			if(fileTypeList.size() == 0){
				fileTypeList.add(folder);
				
				//生成文件夹
				File file2 = new File(fileNamePath+"/"+folder);
				if (!file2.exists())
				{
					file2.mkdir();
				}
			}else{
				int index = fileTypeList.size();//总数量
				
				for (int i = 0; i < fileTypeList.size(); i++)
				{
					if(!folder.equals(fileTypeList.get(i))){
						if(index == (i + 1)){
							fileTypeList.add(folder);
							
							//生成文件夹
							File file2 = new File(fileNamePath+"/"+folder);
							if (!file2.exists())
							{
								file2.mkdir();
							}
						}
					}
				}
			}
			
			// 获取附件地址
			String httpPath = sm_Attachment.getTheLink();
			// 获取文件名称
			String fileName = sm_Attachment.getRemark();

			if (null == httpPath || httpPath.length() == 0)
			{
				return MyBackInfo.fail(properties, "附件路径为空：'" + httpPath + "' 不能为空");
			}

			/*boolean isInner = isInner(httpPath);//判断ip地址
			if(!isInner){
				Properties pro = null;
				try
				{
					String uploadUrl = localPath+"WEB-INF/classes/Oss-Server.properties";// 项目路径
					
					//创建对象
					pro = new Properties();
					InputStream in = new BufferedInputStream(new FileInputStream(uploadUrl));
					pro.load(in);
				}
				catch (Exception e)
				{
					return MyBackInfo.fail(properties, "加载Oss-Server配置文件出现问题，请联系管理员");
				}
				
				String upload = pro.getProperty("remote");//获取下载地址
				
				String spiltUrl = httpPath.substring(httpPath.indexOf("http://"), httpPath.lastIndexOf(":"));
				
				httpPath = httpPath.replace(spiltUrl, upload);//替换下载路径
			}*/
			
			ossUtil.method(httpPath, fileNamePath + "/"  + folder + "/" + fileName);
		}

		// 保存压缩图片
		ZipUtil zipUtil = new ZipUtil(localPath +zipFilePath);
		zipUtil.compress(fileNamePath);
		
		properties.put("zipFileName", zipFilePath);


		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	/**
	 * 判断请求地址是否是内网ip
	 * @param ip
	 * @return
	 */
   public static boolean isInner(String ip)
	{
	    String reg = "(10|172|192|127)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";
	    Pattern p = Pattern.compile(reg);
	    Matcher matcher = p.matcher(ip);
	    return matcher.find();
	}
}
