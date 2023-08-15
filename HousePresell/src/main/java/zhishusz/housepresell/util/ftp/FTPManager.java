package zhishusz.housepresell.util.ftp;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/***
 * TODO FTP上传管理
 * @author huazhang
 *
 */
public class FTPManager {
	private String ftpHost = null;

	private int ftpPort = 21;

	private String ftpUser = null;

	private String ftpPassword = null;

	private String ftpDir = null;

	private FTPClient ftpClient = null;

	private static FTPManager ftpManager = null;

	private FTPManager() {

		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("GBK");//"GBK" ,,"UTF-8"
		FTPClientConfig conf = new FTPClientConfig();
		conf.setServerLanguageCode("zh");// 解决中文乱码
		ftpClient.configure(conf);
	}

	public synchronized static FTPManager getInstance() {
		if (ftpManager == null) {
			ftpManager = new FTPManager();
		}

		return ftpManager;
	}

	public void setInfo(String host, int port, String user, String password,
			String dir) {
        System.out.println("FtpManager : setInfo host:"+host+" port:"+port);
		ftpHost = host;
		ftpPort = port;
		ftpUser = user;
		ftpPassword = password;
		ftpDir = dir;
	}

	public void connect() throws Exception {
		if (!ftpClient.isConnected()) {
			try {
				ftpClient.connect(ftpHost, ftpPort);
				//ftpClient解决中文乱码问题============
				ftpClient.setControlEncoding("GBK");   
				FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);   
				conf.setServerLanguageCode("zh"); 
				ftpClient.configure(conf);
//				FTPClientConfig ftpConfig = new FTPClientConfig(FTPClientConfig.SYST_UNIX); 
//                ftpConfig.setServerLanguageCode(FTP.DEFAULT_CONTROL_ENCODING); 
				//====================================
				ftpClient.login(ftpUser, ftpPassword);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE); //使用二进制流上传文件
//				ftpClient.changeWorkingDirectory(remotePath); //跳转到服务器对应目录
	            System.out.println("FtpManager :  Connected to " + ftpHost );
			} catch (SocketException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
	}

	public void disconnect() throws Exception {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
		        System.out.println("FtpManager :  disconnect " );
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
	}

	public void delete(String remotePath, boolean isAbsolutePath)
			throws Exception {
		try {
	        System.out.println("FtpManager  delete:  remotePath:"+remotePath+" isAbsolutePath:"+isAbsolutePath );
			connect();
			if (isAbsolutePath)
				ftpClient.deleteFile("/" + remotePath);
			else
				ftpClient.deleteFile(getFtpDir() + remotePath);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	/** 
	* 删除的ftp的空目录 
	*/ 
	public void deleteEmptyDictory(String remotePath, boolean isAbsolutePath) {
		try {
            System.out.println("FtpManager  deleteEmptyDictory:  remotePath:"+remotePath+" isAbsolutePath:"+isAbsolutePath );
			if (isAbsolutePath)
				ftpClient.removeDirectory("/" + remotePath);
			else
				ftpClient.removeDirectory(getFtpDir() + remotePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void upload(String remotePath, String localPath,
			boolean isAbsolutePath) throws Exception {
        System.out.println("FtpManager  upload:  remotePath:"+remotePath+" localPath:"+localPath+" isAbsolutePath:"+isAbsolutePath );
		connect();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(localPath);
			String dir = "";
			if (isAbsolutePath)
				dir = "/" + remotePath;
			else
				dir = getFtpDir() + remotePath;

			createDir(dir.substring(0, dir.lastIndexOf("/")));

			boolean result = ftpClient.storeFile(dir, fis);
			System.out.println("上传文件结果:"+result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception(e.getMessage());
				}
			}
		}
	}

	public void download(String remotePath, String localPath,
			boolean isAbsolutePath) throws Exception {
        System.out.println("FtpManager  download:  remotePath:"+remotePath+" localPath:"+localPath+"isAbsolutePath:"+isAbsolutePath );
		connect();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(localPath);

			String dir = "";
			if (isAbsolutePath)
				dir = "/" + remotePath;
			else
				dir = getFtpDir() + remotePath;

			createDir(dir.substring(0, dir.lastIndexOf("/")));

			boolean result = ftpClient.retrieveFile(dir, fos);
            System.out.println("下载文件结果:"+result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (fos != null) {
				try {
					   //使用IO包关闭流    
		            IOUtils.closeQuietly(fos);   
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception(e.getMessage());
				}
			}
		}
	}

	public void changePath(String path) throws Exception {
        System.out.println("FtpManager  changePath:  remotePath:"+path+" path" );
		connect();
		try {
			// String dir = getFtpDir() + path;
			String dir = path;
			if (!ftpClient.printWorkingDirectory().equalsIgnoreCase(dir)) {
				createDir(path);
				ftpClient.changeWorkingDirectory(dir);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	private void createDir(String path) throws Exception {
		if (path != null) {
			try {
					path =  path.replace('\\', '/');
					path = path.replace("//", "/");
					if(path.startsWith("/")){
						path = path.substring(1, path.length());
					}

				String[] splitPath = path.split("/");
				if (splitPath != null && splitPath.length > 0) {
					for (int i = 0; i < splitPath.length; i++) {
					    String path0 = splitPath[i];
						if (path0 != null 	&& path0.trim().length() > 0) {
							ftpClient.changeWorkingDirectory(path0);
							String currDir = ftpClient.printWorkingDirectory();
							String currPath0 = currDir.substring(currDir.lastIndexOf("/")+1, currDir.length());
							if(path0.equals(currPath0)){
							    boolean result =  ftpClient.changeWorkingDirectory(path0);
							    System.out.println("切换目录到 "+path0+" "+result);
								continue;
							}	
							if (!isDirExist(path0)) {
								boolean result = ftpClient.makeDirectory(path0);
                                System.out.println("创建目录 "+path0+" "+result);
                                System.out.println(result);
							}
							boolean result =  ftpClient.changeWorkingDirectory(path0);
                            System.out.println("切换目录到 "+path0+" "+result);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
	}

	/**
	 * 判断dir是否在当前目录下存在
	 * 
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	private boolean isDirExist(String dir) throws IOException {
		boolean exist = false;
		// 解决卡死
		ftpClient.enterLocalPassiveMode();
		FTPFile[] files = ftpClient.listFiles();
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getType() == FTPFile.DIRECTORY_TYPE) {
					if (files[i].getName().equalsIgnoreCase(dir)) {
						exist = true;
						break;
					}
				}
			}
		}

		return exist;
	}

	/**
	 * TODO 查找指定目录下面指定名称的文件是否存在
	 * 
	 * @param filePath
	 *            要查找的目录
	 * @param fileName
	 *            要查找的文件名称
	 * @return 存在:true，不存在:false
	 * @throws
	 * @author zhanghua
	 */
	public boolean checkFileExist(String filePath, String fileName)
			throws IOException {
		boolean existFlag = false;
		// filePath = getFtpDir() + filePath;
		try {
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 跳转到指定的文件目录
		if (filePath != null && !filePath.equals("")) {
			if (filePath.indexOf("/") != -1) {
				int index = 0;
				while ((index = filePath.indexOf("/")) != -1) {
					ftpClient.changeWorkingDirectory(filePath.substring(0,
							index));
					filePath = filePath.substring(index + 1, filePath.length());
				}
				if (!filePath.equals("")) {
					ftpClient.changeWorkingDirectory(filePath);
				}
			} else {
				ftpClient.changeWorkingDirectory(filePath);
			}
		}
		String[] fileNames = ftpClient.listNames();
		if (fileNames != null && fileNames.length > 0) {
			for (int i = 0; i < fileNames.length; i++) {
				if (fileNames[i] != null
						&& fileNames[i].substring(0, fileNames[i].length() - 1)
								.trim().equals(fileName)) {
					existFlag = true;
					break;
				}
			}
		}

		ftpClient.changeToParentDirectory();
		return existFlag;
	}

	private String getFtpDir() {
		return ftpDir == null || ftpDir.length() == 0 ?( "/" + "nccw/CQXT" ): "/" + ftpDir
				+ "/";
	}
}