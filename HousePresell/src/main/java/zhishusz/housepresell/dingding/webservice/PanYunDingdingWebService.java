package zhishusz.housepresell.dingding.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @Description 攀云钉钉保存图片webService接口
 * @Author jxx
 * @Date 2020/9/27 10:42
 * @Version
 **/
@WebService
public interface PanYunDingdingWebService {
    /**
     * 保存图片
     * 
     * @param result
     * @return
     */
    public String savePhotoUrls(@WebParam(name = "result") String result);
    
    /**
     * 保存现场签到时间
     * 
     * @param result
     * @return
     */
    public String saveSign(@WebParam(name = "result") String result);
}
