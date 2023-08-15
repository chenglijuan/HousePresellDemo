package zhishusz.housepresell.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PdfToZipUtil {

    //文件下载目录
   static String DocumentPath = "c:/test/Downloads/";


    /**
     * 文件压缩
     *
     * @param filePaths 压缩文件路径
     * @param zipPath   zip文件路径
     */
    public static void zip(List<String> filePaths, String zipPath) {
        try {
            File zipFile = new File(zipPath);
            // 判断文件是否存在，如文件不存在创建一个新文件
            if (!zipFile.exists()) {
                zipFile.createNewFile();
            }

            // 创建一个zip文件输出流
            ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(zipFile));

            for (String filePath : filePaths) {
                File file = new File(filePath);

                // 判断文件是否存在，如不存在直接跳过
                if (!file.exists()) {
                    continue;
                }
                /**
                 * 创建一个缓冲读取流，提高读取效率
                 * 也可以直接创建一个 FileInputStream 对象，BufferedInputStream内部维护了一个8KB的缓冲区，BufferedInputStream本身不具备读取能力
                 * BufferedInputStream 可以手动指定缓冲区大小 单位为字节例如：new BufferedInputStream(new FileInputStream(file), 10240)
                 */
                BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(file));
//                InputStream inputStream = new FileInputStream(file);

                // 设置压缩条目名称
                zipOutput.putNextEntry(new ZipEntry(file.getName()));
                byte[] bytes = new byte[1024];
                int len = -1;
                // 读取file内的字节流，写入到zipOutput内
                while ((len = bufferedInput.read(bytes)) != -1) {
                    zipOutput.write(bytes, 0, len);
                }
                // 关闭输入流
                // 无需关闭new FileInputStream(file)的输入流 因为BufferedInputStream.close()方法内部已经调用了FileInputStream.close()方法
                bufferedInput.close();
                // 写入完毕后关闭条目
                zipOutput.closeEntry();
            }
            zipOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //先将附件批量下载到本地
    public static List<String> downloadOnlineZip(List<String> filePaths) {

        List<String> resultPath = new ArrayList<>();

        String documentName = "";
        for (String filepath:filePaths) {
            if(!filepath.contains("http"))
                continue;
            // 下载后的文件名称
//            documentName = filepath.substring(filepath.lastIndexOf("/")+1, filepath.length());
            documentName = UUID.randomUUID() + ".pdf";
//            System.out.println("documentName="+documentName);
            try {
                //自动创建文件夹
                File file = new File(DocumentPath);
                if (!file.exists() && !file.isDirectory()) {
                    file.mkdirs();
                }
                //解析下载地址
                URL url = new URL(filepath);
                URLConnection cnt = url.openConnection();
                InputStream inStream = cnt.getInputStream();
                FileOutputStream fos = new FileOutputStream(DocumentPath + documentName);
                int bytesum = 0;
                int byteread;
                byte[] buffer = new byte[1204];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fos.write(buffer, 0, byteread);
                }
//                System.out.println("下载完成");
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultPath.add(DocumentPath+documentName);
        }

        return resultPath;

    }


    /**
     * 合并pdf
     * @param files 需要合并的pdf路径
     * @param newfile 合并成新的文件的路径
     */
    public static boolean mergePdfFiles(List<String> files, String newfile) {
        boolean retValue = false;
        Document document = null;
        PdfCopy copy = null;
        PdfReader reader = null;
        try {
            document = new Document(new PdfReader(files.get(0)).getPageSize(1));
            copy = new PdfCopy(document, new FileOutputStream(newfile));
            document.open();
            for (int i = 0; i < files.size(); i++) {
                reader = new PdfReader(files.get(i));
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
                reader.close();
            }
            retValue = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (copy != null) {
                copy.close();
            }
            if (document != null) {
                document.close();
            }
        }
        return retValue;
    }

    public static void main(String[] args) {

//        String[] files =
//                {"C:\\test\\Downloads\\8afc52ea114e4e08895366f1b5210aab.pdf",
//                "C:\\test\\Downloads\\12fa92c21b6e42d585b86d2235b218ca.pdf",
//                "C:\\test\\Downloads\\45e08eadbb6144ec83ccb5ca90732611.pdf" };

        List<String> file = new ArrayList<>();
        file.add("C:\\test\\Downloads\\8afc52ea114e4e08895366f1b5210aab.pdf");
        file.add("C:\\test\\Downloads\\12fa92c21b6e42d585b86d2235b218ca.pdf");
        file.add("C:\\test\\Downloads\\45e08eadbb6144ec83ccb5ca90732611.pdf");
        String savepath = "D:\\back.pdf";
        boolean b = mergePdfFiles(file, savepath);
        System.out.println(b);
    }


}
