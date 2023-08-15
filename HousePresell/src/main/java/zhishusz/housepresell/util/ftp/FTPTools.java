package zhishusz.housepresell.util.ftp;

import java.io.*;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

public class FTPTools
{
    //用于打印日志
    private static final Logger log = Logger.getLogger(FTPTools.class);

    //设置私有不能实例化
    private FTPTools() {

    }

    /**
     * 上传
     *
     * @param hostname ip或域名地址
     * @param port  端口
     * @param username 用户名
     * @param password 密码
     * @param workingPath 服务器的工作目
     * @param inputStream 要上传文件的输入流
     * @param saveName    设置上传之后的文件名
     * @return
     */
//    public static boolean upload(String hostname, int port, String username, String password, String workingPath, InputStream inputStream, String saveName) {
//        boolean flag = false;
//        FTPClient ftpClient = new FTPClient();
//        //1 测试连接
//        System.out.println("FTP连接开始......");
//        if (connect(ftpClient, hostname, port, username, password)) {
//            System.out.println("FTP连接成功......");
//            try {
//                //2 检查工作目录是否存在
//                System.out.println("FTP检查文件夹是否存在......");
//                if (ftpClient.changeWorkingDirectory(workingPath)) {
//                    // 3 检查是否上传成功
//                    System.out.println("FTP文件上传开始......");
//                    if (storeFile(ftpClient, saveName, inputStream)) {
//                        flag = true;
//                        disconnect(ftpClient);
//                        System.out.println("FTP文件上传结束......");
//                    }
//                    else
//                    {
//                        System.out.println("FTP文件上传失败......");
//                    }
//                }
//                else
//                {
//                    System.out.println("FTP检查文件夹是否存在......");
//                }
//
//            } catch (IOException e) {
//                System.out.println("工作目录不存在");
//                e.printStackTrace();
//                disconnect(ftpClient);
//            }
//        }
//        else
//        {
//            System.out.println("FTP连接失败......");
//        }
//        return flag;
//    }


    public static boolean upload(String hostname, int port, String username, String password, String workingPath, InputStream inputStream, String saveName) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        //1 测试连接
        System.out.println("FTP连接开始......");
        if (connect(ftpClient, hostname, port, username, password)) {
            System.out.println("FTP连接成功......");
            try {
                //2 检查工作目录是否存在
                System.out.println("FTP检查文件夹是否存在......");

                System.out.println("workingPath----------"+workingPath);
                workingPath = new String(workingPath.getBytes("GBK"),"iso-8859-1");
                System.out.println("workingPath==============="+workingPath);

                boolean dir = ftpClient.changeWorkingDirectory(workingPath);
                if (dir) {
                    FTPFile[] fs = ftpClient.listFiles();
                    System.out.println("fslength==========="+fs.length);
                    // 3 检查是否上传成功
                    System.out.println("FTP文件上传开始......");
                    if (storeFile(ftpClient, saveName, inputStream)) {
                        flag = true;
                        disconnect(ftpClient);
                        System.out.println("FTP文件上传结束......");
                    }
                    else
                    {
                        System.out.println("FTP文件上传失败......");
                    }
                }
                else
                {
                    System.out.println("FTP检查文件夹是否存在......");
                }

            } catch (IOException e) {
                System.out.println("工作目录不存在");
                e.printStackTrace();
                disconnect(ftpClient);
            }
        }
        else
        {
            System.out.println("FTP连接失败......");
        }
        return flag;
    }




    /**
     * 断开连接
     *
     * @param ftpClient
     * @throws Exception
     */
    public static void disconnect(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
                System.out.println("已关闭连接");
            } catch (IOException e) {
                System.out.println("没有关闭连接");
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试是否能连接
     *
     * @param ftpClient
     * @param hostname  ip或域名地址
     * @param port      端口
     * @param username  用户名
     * @param password  密码
     * @return 返回真则能连接
     */
    public static boolean connect(FTPClient ftpClient, String hostname, int port, String username, String password) {
        boolean flag = false;
        try {
            //ftp初始化的一些参数
            ftpClient.connect(hostname, port);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            if (ftpClient.login(username, password)) {
                System.out.println("连接ftp成功");
                flag = true;
            } else {
                System.out.println("连接ftp失败，可能用户名或密码错误");
                try {
                    disconnect(ftpClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("连接失败，可能ip或端口错误");
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param ftpClient
     * @param saveName        全路径。如/home/public/a.txt
     * @param fileInputStream 要上传的文件流
     * @return
     */
    public static boolean storeFile(FTPClient ftpClient, String saveName, InputStream fileInputStream) {
        boolean flag = false;
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            if (ftpClient.storeFile(saveName, fileInputStream)) {
                flag = true;
                System.out.println("上传成功");
                disconnect(ftpClient);
            }
        } catch (IOException e) {
            System.out.println("上传失败");
            disconnect(ftpClient);
            e.printStackTrace();
        }
        return flag;
    }

}
