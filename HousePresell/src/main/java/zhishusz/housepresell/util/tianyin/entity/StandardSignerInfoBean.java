package zhishusz.housepresell.util.tianyin.entity;

import java.util.List;

public class StandardSignerInfoBean {

    //签署用户id;和uniqueId必填其一， 机构签时须为机构的印章管理员或 经办人
    private String accountId;

    //签署用户类型;1:内部,2外部
    private Integer accountType;

    //是否静默签署;开启静默签署后会自 动使用用户的印章和证书完成签署
    private boolean autoSign;

    //企业id/部门id;不为空时为企业签 署，和authorizationOrganizeNo 必填其一
    private String authorizationOrganizeId;


    //签署文档信息;指定文档进行签署， 未指定的文档将作为只读
    private List<StandardSignDocBean> signDocDetails;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public List<StandardSignDocBean> getSignDocDetails() {
        return signDocDetails;
    }

    public void setSignDocDetails(List<StandardSignDocBean> signDocDetails) {
        this.signDocDetails = signDocDetails;
    }

    public boolean isAutoSign() {
        return autoSign;
    }

    public void setAutoSign(boolean autoSign) {
        this.autoSign = autoSign;
    }

    public String getAuthorizationOrganizeId() {
        return authorizationOrganizeId;
    }

    public void setAuthorizationOrganizeId(String authorizationOrganizeId) {
        this.authorizationOrganizeId = authorizationOrganizeId;
    }
}
