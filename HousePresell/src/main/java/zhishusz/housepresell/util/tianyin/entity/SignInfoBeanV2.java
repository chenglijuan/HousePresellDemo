package zhishusz.housepresell.util.tianyin.entity;

public class SignInfoBeanV2 {

    //ǩ��ҳ��;����ҳ����'-'���ӣ���'1-3'��������ҳ����','���ӣ���'1,3,5'����
    // signType=��ҳǩ/��ҳǩ/���ǩʱposPage�����ʾ��ָ��ҳָ��λ��ǩ��
    // signType=�ؼ���ǩʱposPage�Ǳ����д���ʾ��ָ��ҳ��λ�ؼ���ǩ��signType=0���ޣ�
    // ����posPage��ʾȫ�ĵ��������£�����posPage��ʾֻ����ָ��ҳ��������ǩ���쳣���Σ�
    // �粻�����ߴ��β����ĵ�ҳ�뷶Χ�ڵĶ���Ϊ��Ч���Σ�����ȫ�ĵ�����ǩ��
   private String  posPage;

   //x��ƫ��������������ƫ�ƣ���������ƫ��;��ҳǩ/��ҳǩ��������ҳ���½�Ϊԭ�����ƫ�ƣ�
   // ���ǩ���Ӹò������ؼ���ǩ�Ǳ���Թؼ��ֵ����½�Ϊԭ�����ƫ��
   private float posX;

   // y��ƫ��������������ƫ�ƣ���������ƫ��;��ҳǩ/��ҳǩ/���ǩ��������ҳ���½�Ϊԭ�����ƫ�ƣ��ؼ���ǩ�Ǳ���Թؼ��ֵ����½�Ϊԭ�����ƫ��
   private float posY;

   //ǩ�����ͣ�0-����(���û��ֶ���קӡ�����ǩ���Զ�ǩ��֧��)��1-��ҳǩ��2-��ҳǩ��3-����¡�4�ؼ���ǩ
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
