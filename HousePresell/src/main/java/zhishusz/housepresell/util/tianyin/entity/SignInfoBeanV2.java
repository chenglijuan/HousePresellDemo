package zhishusz.housepresell.util.tianyin.entity;

public class SignInfoBeanV2 {

    //签署页码;连续页码用'-'连接（如'1-3'），单独页码用','连接（如'1,3,5'）；
    // signType=单页签/多页签/骑缝签时posPage必填，表示在指定页指定位置签署；
    // signType=关键字签时posPage非必填，填写则表示在指定页定位关键字签署signType=0不限，
    // 不传posPage表示全文档自由拖章；传入posPage表示只能在指定页码内自由签署；异常传参，
    // 如不传或者传参不在文档页码范围内的都视为无效传参，还是全文档自由签署。
   private String  posPage;

   //x轴偏移量，正数向右偏移，负数向左偏移;单页签/多页签必填，以相对页左下角为原点进行偏移；
   // 骑缝签无视该参数；关键字签非必填，以关键字的左下角为原点进行偏移
   private float posX;

   // y轴偏移量，正数向上偏移，负数向下偏移;单页签/多页签/骑缝签必填，以相对页左下角为原点进行偏移；关键字签非必填，以关键字的左下角为原点进行偏移
   private float posY;

   //签署类型；0-不限(需用户手动拖拽印章完成签署，自动签署不支持)、1-单页签、2-多页签、3-骑缝章、4关键字签
   private Integer signType;

    public String getPosPage() {
        return posPage;
    }

    public void setPosPage(String posPage) {
        this.posPage = posPage;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public Integer getSignType() {
        return signType;
    }

    public void setSignType(Integer signType) {
        this.signType = signType;
    }
}
