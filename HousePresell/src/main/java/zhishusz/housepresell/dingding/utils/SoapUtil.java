package zhishusz.housepresell.dingding.utils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * @Description Soap工具类
 * @Author jxx
 * @Date 2020/6/5 17:34
 * @Version
 **/
public class SoapUtil {

    public static void main(String[] args) {
        String postUrl = "http://218.4.84.171:5445/AppWebServiceShareSZYQ/GHBackBone_DBSharWS_sz.asmx?WSDL";
        String soapAction = "http://tempuri.org/DownLoadProjectInfo";
        String namespace = "http://tempuri.org/";
        Map<String, Object> params = new HashMap<>();
        params.put("pass", "123456");
        // PrjNum":"320594180929013804",
        // "xmmc":"建屋D座主体总承包工程",
        // "sgdw":"中亿丰建设集团股份有限公司",
        // "jsdw":"苏州工业园区建屋宏业房地产有限公司"
    }

    /**
     * 访问webservice接口
     *
     * @param namespace
     *            命名空间
     * @param postUrl
     *            webservice接口地址
     * @param soapAction
     *            soapAction地址
     * @param method
     *            方法名
     * @param params
     *            参数组
     * @return 返回值
     */
    public static String splicingMessage(String namespace, String postUrl, String soapAction, String method,
        Map<String, Object> params) {
        // 自定义soap报文模板 注意：xmlns:web 与<soapenv:Body>后的属性（web）要一致
        StringBuffer sb = new StringBuffer(
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"");
        sb.append(namespace);
        sb.append("\"><soapenv:Header/><soapenv:Body><web:");
        sb.append(method);
        sb.append(">");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append("<web:" + entry.getKey() + ">" + entry.getValue() + "</web:" + entry.getKey() + ">");
        }
        sb.append("</web:");
        sb.append(method);
        sb.append("></soapenv:Body></soapenv:Envelope>");

        System.out.println("sb : " + sb.toString());

        String result = doPostSoap1_1(postUrl, sb.toString(), soapAction);
        return result;
    }

    /**
     * 使用SOAP1.1发送消息
     *
     * @param postUrl
     * @param soapXml
     * @param soapAction
     * @return
     */
    public static String doPostSoap1_1(String postUrl, String soapXml, String soapAction) {
        String retStr = "";
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

        System.out.println("PUSH URL：" + postUrl);

        HttpPost httpPost = new HttpPost(postUrl);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
        httpPost.setConfig(requestConfig);
        try {
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            httpPost.setHeader("SOAPAction", soapAction);
            StringEntity data = new StringEntity(soapXml, Charset.forName("UTF-8"));
            httpPost.setEntity(data);
            CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                retStr = EntityUtils.toString(httpEntity, "UTF-8");
            }
            // 释放资源
            closeableHttpClient.close();
        } catch (Exception e) {

            System.out.println("SoapUtilException : ");
            e.printStackTrace();

        }
        return retStr;
    }
}
