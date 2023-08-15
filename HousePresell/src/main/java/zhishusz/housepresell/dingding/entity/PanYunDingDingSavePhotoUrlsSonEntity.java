package zhishusz.housepresell.dingding.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description 攀云钉钉保存图片接口子对象
 * @Author jxx
 * @Date 2020/9/28 15:58
 * @Version
 **/
public class PanYunDingDingSavePhotoUrlsSonEntity implements Serializable {
    /**
     * 楼幢CODE
     */
    @JSONField(name = "BuildingCode", ordinal = 1)
    private String buildingCode;

    /**
     * 变更节点
     */
    @JSONField(name = "ChangeNode", ordinal = 2)
    private String changeNode;

    /**
     * 图片url
     */
    @JSONField(name = "PicUrls", ordinal = 4)
    private List<String> picUrls;

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getChangeNode() {
        return changeNode;
    }

    public void setChangeNode(String changeNode) {
        this.changeNode = changeNode;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }

}
