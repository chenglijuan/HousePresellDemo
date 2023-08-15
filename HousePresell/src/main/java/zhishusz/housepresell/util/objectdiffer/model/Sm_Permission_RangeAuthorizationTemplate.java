package zhishusz.housepresell.util.objectdiffer.model;

import zhishusz.housepresell.database.po.Sm_Permission_RangeAuthorization;
import zhishusz.housepresell.util.IFieldAnnotation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dechert on 2018-10-16.
 * Company: zhishusz
 */

public class Sm_Permission_RangeAuthorizationTemplate extends BaseTemplate
{
    @Getter @Setter
    private Sm_Permission_RangeAuthorization rangeAuthorization;
    @Getter @Setter @IFieldAnnotation(remark = "范围授权能够勾选的数据")
    private List<HashMap> rangeInfoAllList;
    @Getter @Setter @IFieldAnnotation(remark = "范围授权已经勾选的数据")
    private HashMap rangeInfoSelectDetail;
    @Getter @Setter @IFieldAnnotation(remark = "授权类别")
    private String rangeAuthTypeString;
    @Getter @Setter @IFieldAnnotation(remark = "授权起始日期")
    private String authStartTimeStampString;
    @Getter @Setter @IFieldAnnotation(remark = "授权截止日期")
    private String authEndTimeStampString;

    @SuppressWarnings("rawtypes")
	public List getNeedFieldList(){
        return Arrays.asList("rangeInfoListString", "rangeInfoAllList","rangeInfoSelectDetail","rangeAuthTypeString","authStartTimeStampString","authEndTimeStampString");
//        return Arrays.asList("rangeInfoSelectDetail");
    }
}
