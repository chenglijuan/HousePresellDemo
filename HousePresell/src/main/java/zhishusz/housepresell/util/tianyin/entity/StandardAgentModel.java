package zhishusz.housepresell.util.tianyin.entity;

/**
 * @Author: chenglijuan
 * @Data: 2021/5/14  13:54
 * @Decription:
 * @Modified:
 */
public class StandardAgentModel {

    //外部机构Id。用户id和用户唯一标识必填其一。用户id和用户唯一标识都不为空时，以用户id为准
    private String accountId;

    //默认经办人标志位，1为默认，0为非默认
    private Integer isDefault;

    //外部用户唯一标识。用户id和用户唯一标识必填其一。用户id和用户唯一标识都不为空时，以用户id为准
    private String uniqueId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
