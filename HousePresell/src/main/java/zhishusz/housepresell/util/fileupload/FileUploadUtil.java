package zhishusz.housepresell.util.fileupload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyInteger;
import zhishusz.housepresell.util.MyProperties;

public class FileUploadUtil {
	public static final Integer M_BYTE = 1024*1024;
	public Properties execute(Map<String, Object> paramMap)
	{
		Properties properties = new MyProperties();
		FileUtil fileutil = FileUtil.getInstance();
		
		HttpServletRequest request = (HttpServletRequest)paramMap.get("p1");
		Integer maxFileSize = MyInteger.getInstance().parse(paramMap.get("p2"));

		try {
			request.setCharacterEncoding("UTF-8");
			String orgFileName = null;
			String strFullPath = null;
			String fileSavePath = null;
			String fileNewName = null;
			String newFileNetworkPath = null;
			SingleFileUpload sf = null;

			sf = SingleFileUpload.getInstance();
			sf.parseRequest(request);
			
			if (sf.getFileItem() != null && sf.getFileItem().getSize() > 0)
			{
				sf.setSizeMax(maxFileSize * M_BYTE);//上传文件的限制大小
				sf.setRepository(new File(System.getProperty("java.io.tmpdir")));

				orgFileName = sf.getFileItem().getName();
			}
			else
			{
				properties.put(S_NormalFlag.result, S_NormalFlag.fail);
				properties.put(S_NormalFlag.info, "没有找到要上传的文件");
			}
			
			if(!S_NormalFlag.fail.equals(properties.get(S_NormalFlag.result)))
			{
				String path = request.getContextPath();
				int serverPort = request.getServerPort();
				String basePath;
				if(serverPort != 80)
				{
					basePath = request.getScheme() + "://" + request.getServerName() + ":" + serverPort + path + "/";
				}
				else
				{
					basePath = request.getScheme() + "://" + request.getServerName() + path + "/";
				}
				
				DirectoryUtil directoryUtil = new DirectoryUtil();
				String projectPath = directoryUtil.getProjectRoot();//项目所在物理路径
				//初始化文件保存路径，创建相应文件夹
				String strSaveToRelativeDir = directoryUtil.createRelativePathWithDate("excel");
				String strProjectRoot = directoryUtil.getProjectRoot();
				String saveDirectory = strProjectRoot + strSaveToRelativeDir;
				directoryUtil.mkdir(saveDirectory);

				FileRenamePolicyUtil fileRenamePolicyUtil = new FileRenamePolicyUtil();
				String strNewFileName = fileRenamePolicyUtil.getNewFileName(orgFileName);
				strFullPath = saveDirectory + strNewFileName;
				fileSavePath = strSaveToRelativeDir + strNewFileName;
				fileNewName = strNewFileName;//传给前台的文件名（包含扩展名）
				//新文件网络路径
				newFileNetworkPath = fileutil.getFileNetworkPath(projectPath, basePath, fileSavePath);

				//文件生成路径
				try {
					//将文件写到硬盘上，并记录到数据库
					File file = new File(strFullPath);
					if (file != null) 
					{
						sf.upload(strFullPath);//开始上传文件;
						properties.put(S_NormalFlag.result, S_NormalFlag.success);
						properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
						properties.put("filePath", fileSavePath);
						properties.put("fileFullPath", strFullPath);
						properties.put("fileNewName", fileNewName);
						properties.put("newFileNetworkPath", newFileNetworkPath);
					}
				} catch (Exception e) {
					e.printStackTrace();
					properties.put(S_NormalFlag.result, S_NormalFlag.fail);
					properties.put(S_NormalFlag.info, "文件上传过程中出现异常");
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, "数据编码错误");
		} catch (Exception ex) {
			ex.printStackTrace();
			properties.put(S_NormalFlag.result, S_NormalFlag.fail);
			properties.put(S_NormalFlag.info, "文件上传失败");
		}
		
		return properties;
	}
}
