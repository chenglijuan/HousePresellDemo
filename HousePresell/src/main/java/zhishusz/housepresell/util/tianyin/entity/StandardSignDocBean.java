package zhishusz.housepresell.util.tianyin.entity;

import java.util.List;

public class StandardSignDocBean {

    //签署文档fileKey
    private String docFilekey;

    //签署位置信息;必须指定签署位置信息才可以签署
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
