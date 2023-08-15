package zhishusz.housepresell.util.excel.model;

import java.util.HashMap;
import java.util.Map;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_EscrowStandardVerMng;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyString;
import lombok.Getter;
import lombok.Setter;

public class Tgpj_EscrowStandardVerMngTemplate implements IExportExcel<Tgpj_EscrowStandardVerMng>
{
//    @Getter @Setter @IFieldAnnotation(remark="编号")
//    private String eCode;//eCode=业务编号+N+YY+MM+DD+日自增长流水号（5位），业务编码参看“功能菜单-业务编码.xlsx”

    @Getter @Setter @IFieldAnnotation(remark="版本名称")
    private String theName;

    @Getter @Setter @IFieldAnnotation(remark="版本号")
    private String theVersion;

    @Getter @Setter @IFieldAnnotation(remark="是否启用")
    private String hasEnable;

    @Getter @Setter @IFieldAnnotation(remark="托管标准类型 (枚举选择:0-标准金额1-标准比例)")
    private String theType;

    @Getter @Setter @IFieldAnnotation(remark="托管标准")
    private String theContent;//【手工输入，当前最新的政策是：楼幢物价备案均价*40%；老政策（毛坯房）：3500元】

    @Getter @Setter @IFieldAnnotation(remark="启用日期")
    private String beginExpirationDate;

    @Getter @Setter @IFieldAnnotation(remark="停用日期")
    private String endExpirationDate;

    @Override
    public Map<String, String> GetExcelHead() {
        // TODO Auto-generated method stub

        Map<String, String> map = new HashMap<String, String>();
        map.put("theName", "托管标准协议版本号");
        map.put("theVersion", "版本名称");
        map.put("theType", "托管标准类型");
        map.put("theContent", "托管标准");
        map.put("beginExpirationDate", "启用日期");
        map.put("endExpirationDate", "停用日期");
        map.put("hasEnable", "状态");
        return map;
    }

    @Override
    public void init(Tgpj_EscrowStandardVerMng object) {
        this.setTheName(object.getTheName());
        this.setTheVersion(object.getTheVersion());
        String theType = object.getTheType();
        if ("0".equals(theType))
        {
            this.setTheType("托管金额");
            this.setTheContent(MyString.getInstance().parse(object.getAmount()));
        }
        else
        {
            this.setTheType("托管比例");
            this.setTheContent(MyString.getInstance().parse(object.getPercentage()+"%"));
        }
        this.setBeginExpirationDate(MyDatetime.getInstance().dateToSimpleString(object.getBeginExpirationDate()));
        this.setEndExpirationDate(MyDatetime.getInstance().dateToSimpleString(object.getEndExpirationDate()));
        Boolean hasEnable = object.getHasEnable();
        if (hasEnable)
        {
            this.setHasEnable("禁用");
        }
        else
        {
            this.setHasEnable("启用");
        }
    }
}
