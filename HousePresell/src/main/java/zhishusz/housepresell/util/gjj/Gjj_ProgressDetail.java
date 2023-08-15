package zhishusz.housepresell.util.gjj;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;

import java.util.List;

/**
 * @Author: chenglijuan
 * @Data: 2021/6/4  16:22
 * @Decription:
 * @Modified:
 */
public class Gjj_ProgressDetail {

    //2、开发公司名称、项目名称、施工编号、楼栋ID（公积金）、在建层数、总层数、进度文字说明、进度图片(以何种形式推送？是否存在多张？)—图片有就推送，非一次性，4-5张

    @Getter @Setter @IFieldAnnotation(remark="开发企业名称")
    private String companyName;

    @Getter @Setter @IFieldAnnotation(remark="项目名称")
    private String projectName;

    @Getter @Setter @IFieldAnnotation(remark="施工编号")
    private String eCodeFromConstruction;

    @Getter @Setter @IFieldAnnotation(remark="楼栋ID（公积金）")
    private String gjjTableId;

    @Getter @Setter @IFieldAnnotation(remark="在建层数")
    private String currentBuildProgress;//当前建设进度（托管）

    @Getter @Setter @IFieldAnnotation(remark="地上总层数")
    private String totalOfoverground;

}
