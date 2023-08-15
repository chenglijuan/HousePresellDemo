package zhishusz.housepresell.util.fileupload;

import com.xiaominfo.oss.sdk.OSSClient;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import cn.hutool.http.HttpUtil;

/**
 * Created by Dechert on 2018-08-17.
 * Company: zhishusz
 * OSS server 文件上传工具类，需要配置url、projectCode、APPID、APPSecret
 */
@Repository 
public class OssServerUtil
{
	@Autowired 
	private OSSClient ossClient;

	private String remoteType;

	public String getRemoteType() {
		return remoteType;
	}

	public void setRemoteType(String remoteType) {
		this.remoteType = remoteType;
	}

	/**
	 * 本地文件上传类，用于上传到OSS Server
	 *
	 * @param filePath 需要上传的本地文件的路径
	 * @return 上传后返回的详细结果，包括url、保存的路径等等
	 */
	public ReceiveMessage upload(String filePath)
	{
		return upload(new File(filePath));
	}

	/**
	 * 本地文件上传类，用于上传到OSS Server
	 *
	 * @param file 需要上传的本地文件
	 * @return 上传后返回的详细结果，包括url、保存的路径等等
	 */
	public ReceiveMessage upload(File file)
	{
//		ossClient.uploadFile(file);
		System.out.println("remoteType="+remoteType);
		System.out.println("dayinshangchuan：" + ossClient.toString());
		return ossClient.uploadFile(file);
	}

	/**
	 * 下载网络文件的方法
	 *
	 * @param url 需要下载文件的url路径
	 * @return String类型的文件的内容
	 */
	public String download(String url)
	{
		return HttpUtil.get(url);
	}

	/**
	 * 下载网络文件的方法
	 *
	 * @param url 需要下载文件的url路径
	 * @return String类型的文件的内容
	 */
	public void download(String url,OnDownload onDownload)
	{
		new Thread(() -> {
			try
			{
				String str = HttpUtil.get(url);
				onDownload.onSuccess(str);
			}catch (Exception e){
				onDownload.onFail(e);
			}
		}).start();
	}

	/**
	 * 下载文件到本地的方法
	 *
	 * @param url     下载地址的url，例："http://192.168.1.155/OssSave/DCTUpload/201808/28/b0df136caf07426299f3c3e20bc6c189.txt"
	 * @param desPath 目标文件保存的路径，例："D:\\1_MyProject\\download.txt"
	 * @return 文件大小
	 */
	public long download(String url, String desPath)
	{
		return HttpUtil.downloadFile(url, desPath);
	}

	/**
	 * 将字符串转为一个临时文件
	 *
	 * @param file    用于输出的文件
	 * @param content 文件的内容
	 * @return 返回那个上传用的文件
	 */
	private File string2File(File file, String content)
	{
		String string = new String(content.getBytes(Charset.forName("UTF-8")));
		String path = file.getAbsolutePath();
//		System.out.println("path is " + path);
		path = path.replace("\\", "/");
		int indexOf = path.lastIndexOf("\\");
		String dirPath = path.substring(0, indexOf + 1);
		File dirFile = new File(dirPath);
		if (!dirFile.exists())
		{
			dirFile.mkdirs();
		}
		indexOf = path.lastIndexOf("/");
		dirPath = path.substring(0, indexOf + 1);
		dirFile = new File(dirPath);
		if (!dirFile.exists())
		{
			dirFile.mkdirs();
		}

		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			/*FileOutputStream fileOutputStream = new FileOutputStream(file);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
			bufferedOutputStream.write(bytes, 0, bytes.length);
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
			fileOutputStream.close();*/
			FileWriter writer = new FileWriter(file);
            writer.write(string);
            writer.flush();
            writer.close();
			
			return file;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * String字符串作为文件上传到OSS server
	 *
	 * @param content String类型的文件内容
	 */
	public ReceiveMessage stringUpload(String content,DownloadListener downloadListener)
	{

		ossClient.setRemoteType(this.remoteType);
//		System.out.println("upload content is "+content);

		String rootPath = getClass().getClassLoader().getResource("").getPath() + "upload_temp/OrgInfo/";
//		System.out.println("file1 is " + rootPath);
		File file = new File(rootPath, "OrgInfo" + System.currentTimeMillis() + ".txt");
		File string2File = string2File(file, content);
		ReceiveMessage upload = upload(string2File);
		if(downloadListener!=null){
			if(upload.getCode().equals("8500")){
				downloadListener.onFail(upload.getMessage());
			}else{
				downloadListener.onSuccess(upload);
			}
		}
		return upload;
	}

	public ReceiveMessage stringUpload(String content){
		return stringUpload(content, null);
	}

	public void stringUploadGetFilePath(String content,OnGetFilePath onGetFilePath){
	    stringUpload(content, new DownloadListener() {
            @Override
            public void onSuccess(ReceiveMessage upload) {
                List<FileBytesResponse> data = upload.getData();
                if(data!=null){
                    String url = data.get(0).getUrl();
//                    System.out.println("stringUploadGetFilePath url is "+url);
                    onGetFilePath.onGet(url);
                }
            }

            @Override
            public void onFail(String info) {
                onGetFilePath.onGet(null);
            }
        });
    }

	/**
	 * 下载 Oss server  文件
	 * @param urllink   服务器地址
	 * @param outFilePath   保存地址
	 */
	public void method(String urllink, String outFilePath)
	{

		try
		{
			URL url = new URL(urllink);
			//打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。
			InputStream in = url.openStream();
			download(in, outFilePath);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 保存文件到本地
	 * @param inputStream  io流
	 * @param outFilePath  保存路径
	 */
	public void download(InputStream inputStream, String outFilePath)
	{

		try
		{
			File file = new File(outFilePath);
			OutputStream outputStream = new FileOutputStream(file);

			int byteCount = 0;
			// 1M逐个读取
			byte[] bytes = new byte[1024 * 1024];
			while ((byteCount = inputStream.read(bytes)) != -1)
			{
				outputStream.write(bytes, 0, byteCount);
			}
			inputStream.close();
			outputStream.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public OSSClient getOssClient()
	{
		return ossClient;
	}

	public void setOssClient(OSSClient ossClient)
	{
		this.ossClient = ossClient;
	}

	public interface OnDownload{
		void onSuccess(String result);
		void onFail(Exception e);
	}

	public interface DownloadListener{
		void onSuccess(ReceiveMessage upload);
		void onFail(String info);
	}

	public interface OnGetFilePath{
        void onGet(String filePath);
    }
}
