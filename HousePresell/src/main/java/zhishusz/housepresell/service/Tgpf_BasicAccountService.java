package zhishusz.housepresell.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Tgpf_BasicAccountForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BasicAccountDao;
import zhishusz.housepresell.database.po.Tgpf_BasicAccount;
import zhishusz.housepresell.database.po.Tgpf_BasicAccountVoucher;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：基本户凭证操作 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_BasicAccountService {
    @Autowired
    private Tgpf_BasicAccountDao tgpf_BasicAccountDao;
    @Autowired
    private Sm_UserDao sm_UserDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Tgpf_BasicAccountForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        String keyword = model.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            model.setKeyword("%" + model.getKeyword() + "%");
        } else {
            model.setKeyword(null);
        }

        String month = model.getMonth();
        if (StrUtil.isBlank(month)) {
            return MyBackInfo.fail(properties, "请选择月份！");
        }

        model.setMonth(model.getMonth() + "-%");

        String voucherType = model.getVoucherType();
        if (StrUtil.isBlank(voucherType)) {
            return MyBackInfo.fail(properties, "请选择类别！");
        }

        String accountName = model.getAccountName();
        if (StrUtil.isBlank(accountName)) {
            model.setAccountName(null);
        }else{
            model.setAccountName(accountName.trim());
        }

        Integer totalCount = tgpf_BasicAccountDao
            .findByPage_Size(tgpf_BasicAccountDao.getQuery_Size(tgpf_BasicAccountDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Tgpf_BasicAccount> tgpf_BasicAccountList;
        if (totalCount > 0) {
            tgpf_BasicAccountList = tgpf_BasicAccountDao.findByPage(
                tgpf_BasicAccountDao.getQuery(tgpf_BasicAccountDao.getBasicHQL(), model), pageNumber, countPerPage);
        } else {
            tgpf_BasicAccountList = new ArrayList<Tgpf_BasicAccount>();
        }

        List<Properties> list = new ArrayList<>();
        Properties pro;
        for (Tgpf_BasicAccount basicAccount : tgpf_BasicAccountList) {
            pro = new MyProperties();
            pro.put("tableId", basicAccount.getTableId());
            pro.put("eCode", basicAccount.geteCode());
            pro.put("accountName",
                StringUtils.isBlank(basicAccount.getAccountName()) ? "" : basicAccount.getAccountName());
            pro.put("voucherType",
                StringUtils.isBlank(basicAccount.getVoucherType()) ? "" : basicAccount.getVoucherType());
            pro.put("billTimeStamp",
                StringUtils.isBlank(basicAccount.getBillTimeStamp()) ? "" : basicAccount.getBillTimeStamp());
            pro.put("remark", StringUtils.isBlank(basicAccount.getRemark()) ? "" : basicAccount.getRemark());
            pro.put("subCode", StringUtils.isBlank(basicAccount.getSubCode()) ? "" : basicAccount.getSubCode());
            pro.put("totalTradeAmount",
                null == basicAccount.getTotalTradeAmount() ? 0.00 : basicAccount.getTotalTradeAmount());
            pro.put("sendState", StringUtils.isBlank(basicAccount.getSendState()) ? "0" : basicAccount.getSendState());
            pro.put("sendTime", StringUtils.isBlank(basicAccount.getSendTime()) ? "0" : basicAccount.getSendTime());
            pro.put("vou_No", StringUtils.isBlank(basicAccount.getVou_No()) ? "0" : basicAccount.getVou_No());
            pro.put("contentJson",
                StringUtils.isBlank(basicAccount.getContentJson()) ? "" : basicAccount.getContentJson());
            list.add(pro);
        }

        properties.put("tgpf_BasicAccountList", list);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        return properties;
    }

    public Properties sendExecute(Tgpf_BasicAccountForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long[] idArr = model.getIdArr();
        if (null == idArr || idArr.length < 0) {
            return MyBackInfo.fail(properties, "请选择需要推送的凭证数据！");
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < idArr.length; i++) {
                if (i == 0) {
                    sb.append(String.valueOf(idArr[i]));
                } else {
                    sb.append("," + String.valueOf(idArr[i]));
                }
            }

            try {
                Map<String, Object> map = tgpf_BasicAccountDao.prc_Insert_pzts("1", sb.toString(),
                    String.valueOf(model.getUser().getTableId()));

                if (S_NormalFlag.fail.equals(map.get("sign"))) {
                    return MyBackInfo.fail(properties, (String)map.get("info"));
                }

            } catch (SQLException e) {

                e.printStackTrace();
                return MyBackInfo.fail(properties, "操作失败，请稍后重试！");
            }

        }

        return properties;
    }
}
