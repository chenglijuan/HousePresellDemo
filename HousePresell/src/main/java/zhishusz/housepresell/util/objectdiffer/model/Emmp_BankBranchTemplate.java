package zhishusz.housepresell.util.objectdiffer.model;

import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.util.IFieldAnnotation;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dechert on 2018-10-16.
 * Company: zhishusz
 */

public class Emmp_BankBranchTemplate extends BaseTemplate{
    @Getter @Setter
    private Emmp_BankBranch bankBranch;
    @Getter @Setter @IFieldAnnotation(remark = "是否启用")
    private String isUsing;
    @Getter @Setter @IFieldAnnotation(remark = "状态")
    private String state;

    public List getNeedFieldList(){
        return Arrays.asList("bankBranch/bank/theName","bankBranch/theName", "bankBranch/shortName",
                "bankBranch/contactPerson","bankBranch/contactPhone","isUsing","state");
    }

    //银行名称、开户行名称、开户行简称、联系人、联系电话、开户行地址、是否启用、状态、修改人、修改时间
}
