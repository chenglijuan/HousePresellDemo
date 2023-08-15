package zhishusz.housepresell.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
public class CallableTest {

    /**
     * 将file文件转换成Byte数组
     *
     * @param file 转换文件
     * @return Byte数组
     */
    public static byte[] getBytesByFile(File file) throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            byte[] data = bos.toByteArray();
            return data;
        } catch (Exception e) {
            log.error("将文件转换成Byte数组失败", e);
        } finally {
            if (fis != null) {
                fis.close();
            }
            bos.close();
        }
        return null;
    }

    /**
     * @param bytes     byte数组
     * @param fileRoute 文件路径
     * @param fileName  文件名
     */
    public static void upload(byte[] bytes, String fileRoute, String fileName) throws Exception {
        File directory = new File(fileRoute);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
        bos.write(bytes);
        bos.close();
    }

    public static void test1() throws IOException {
        String path = "F:\\uploaded\\杨同凤.pdf";
        File file = new File(path);
        byte[] bytes = getBytesByFile(file);
        String fileBase64 = Base64.encode(bytes);
        System.out.println(fileBase64);
//        try {
//            upload(Base64.decode(fileBase64), "D://", "共产党宣言.pdf");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    //转base64
    public String fileToBase64() {
        String imgFilePath = "F:\\uploaded\\周月红.pdf";
        String[] res = imgFilePath.split("\\.");
        String pos = res[res.length - 1];
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data1 = Base64Utils.encodeToString(data);
        System.out.println(data1);
        return data1;
    }

    //转pdf
    public  void sfad() {
        String s = this.fileToBase64();
        byte[] decode = Base64Decoder.decode(s);
        File file = new File("d:/file1.pdf");
        try {
            FileOutputStream fop = new FileOutputStream(file);
            try {
                fop.write(decode);
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws IOException {
//        test1();
//


//        String imgFilePath = "F:\\uploaded\\杨同凤.pdf";
//        String[] res = imgFilePath.split("\\.");
//        String pos = res[res.length - 1];
//        byte[] data = null;
//        // 读取图片字节数组
//        try {
//            InputStream in = new FileInputStream(imgFilePath);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String data1 = Base64Utils.encodeToString(data);
//        System.out.println(data1);
        CallableTest test= new CallableTest();
//        test.sfad();
        test.fileToBase64();

    }
}