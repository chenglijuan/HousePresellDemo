package zhishusz.housepresell.util.tianyin.entity;

import java.util.List;

public class StandardSignDocBean {

    //ǩ���ĵ�fileKey
    private String docFilekey;

    //ǩ��λ����Ϣ;����ָ��ǩ��λ����Ϣ�ſ���ǩ��
    private List<SignInfoBeanV2> signPos;

    public String getDocFilekey() {
        return docFilekey;
    }

    public void setDocFilekey(String docFilekey) {
        this.docFilekey = docFilekey;
    }

    public List<SignInfoBeanV2> getSignPos() {
        return signPos;
    }

    public void setSignPos(List<SignInfoBeanV2> signPos) {
        this.signPos = signPos;
    }
}
