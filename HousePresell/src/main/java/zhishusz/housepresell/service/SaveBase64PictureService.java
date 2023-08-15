package zhishusz.housepresell.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.SavePicForm;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesResponse;

import cn.hutool.core.codec.Base64;

/*
 * Service 保存操作：高拍仪
 * 		base64 保存本地 上传 Oss-server
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class SaveBase64PictureService
{

	@Autowired
	private Gson gson;
	@Autowired
	private OssServerUtil ossUtil;
	
	private static final String savePicPath = "/fjFile";//文件名称
	
	@SuppressWarnings("unchecked")
	public Properties execute(SavePicForm model)
	{
		Properties properties = new MyProperties();
		
		List<FileBytesResponse> fileBytesList = new ArrayList<FileBytesResponse>();
		
		//获取Json格式的List Base64字符串
		String base64List = model.getBase64List();
		if(null == base64List || base64List.trim().isEmpty()){
			return MyBackInfo.fail(properties, "图片数据为空，请核对要保存的数据！");
		}

		//格式类型转换
		List<Map<String, String>> listMap = gson.fromJson(base64List, List.class);
		if(null == listMap || listMap.size() == 0){
			return MyBackInfo.fail(properties, "文件格式转换失败！");
		}
		
		for (Map<String, String> map : listMap)
		{
			//附件类型
			String attachmentType = map.get("attachmentType");
			//图片Base64编码
			String picBase64 = map.get("picBase64");
			//图片类型
			String picType = map.get("picType");
			
			/*--------------------  保存图片到本地  -------------------*/
			//初始化文件保存路径，创建相应文件夹
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String localPath = directoryUtil.getProjectRoot();//项目路径
			
			if (localPath.contains("%20"))
			{
				localPath = localPath.replace("%20", " ");
			}
			
			if(localPath.contains("file:/")){
				localPath = localPath.replace("file:/", "");
			}
			
			String fileNamePath = localPath + attachmentType;
			File file = new File(fileNamePath);
			if (!file.exists())
			{
				file.mkdir();
			}
			//判断是否包含Base64头部
			if(picBase64.contains("data:img/jpg;base64,")){
				picBase64 = picBase64.replaceAll("data:img/jpg;base64,", ""); //将编码中的data:image/jpeg;base64,替换
			}
			
			String fileName = fileNamePath + savePicPath+picType;
			//解码
			byte[] bytes = Base64.decode(new String(picBase64).getBytes());
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			byte[] buffer = new byte[1024];
			try
			{
				FileOutputStream out = new FileOutputStream(fileName);
				int bytesum = 0;
				int byteread = 0;
				while ((byteread = in.read(buffer)) != -1) {
					bytesum = bytesum + byteread;
					out.write(buffer, 0, byteread); // 文件写操作
					out.flush();
				}
				out.close();
			}
			catch (Exception e)
			{
				properties.put(S_NormalFlag.result, S_NormalFlag.fail);
				properties.put(S_NormalFlag.info, e.getMessage());
				return properties;
			}
			
			//获取本地图片上传Oss-server
			ReceiveMessage upload = ossUtil.upload(fileName);
			
			List<FileBytesResponse> list = upload.getData();
			
			if(list != null && list.size() > 0){
				for (FileBytesResponse fileBytesResponse : list)
				{
					fileBytesResponse.setExtra(attachmentType);
				}
			}else{
				list = new ArrayList<FileBytesResponse>();
			}
			
			fileBytesList.addAll(list);
			
			//删除文件夹
			file.delete();
		}

		properties.put("fileBytesList", fileBytesList);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}
