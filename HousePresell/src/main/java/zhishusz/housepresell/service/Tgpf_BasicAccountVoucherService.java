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
import zhishusz.housepresell.controller.form.Tgpf_BasicAccountVoucherDtlForm;
import zhishusz.housepresell.controller.form.Tgpf_BasicAccountVoucherForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_BasicAccountVoucherDao;
import zhishusz.housepresell.database.dao.Tgpf_BasicAccountVoucherDtlDao;
import zhishusz.housepresell.database.po.Tgpf_BasicAccountVoucher;
import zhishusz.housepresell.database.po.Tgpf_BasicAccountVoucherDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：非基本户凭证操作 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_BasicAccountVoucherService {
    @Autowired
    private Tgpf_BasicAccountVoucherDao tgpf_BasicAccountVoucherDao;
    @Autowired
    private Tgpf_BasicAccountVoucherDtlDao tgpf_BasicAccountVoucherDtlDao;
    @Autowired
    private Sm_UserDao sm_UserDao;

    @SuppressWarnings("unchecked")
    public Properties execute(Tgpf_BasicAccountVoucherForm model) {
        Properties properties = new MyProperties();

        model.setTheState(S_TheState.Normal);

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

        String sendState = model.getSendState();
        if (StrUtil.isBlank(sendState)) {
            model.setSendState(null);
        }

        model.setBillTimeStamp(null);
        
        
        String beginDate = model.getBeginDate();
        String endDate = model.getEndDate();
        if(StrUtil.isBlank(beginDate)){
            model.setBeginDate(null);
        }else{
            model.setBeginDate(beginDate.trim());
        }
        
        if(StrUtil.isBlank(endDate)){
            model.setEndDate(null);
        }else{
            model.setEndDate(endDate.trim());
        }
       

        Integer totalCount = tgpf_BasicAccountVoucherDao.findByPage_Size(
            tgpf_BasicAccountVoucherDao.getQuery_Size(tgpf_BasicAccountVoucherDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Tgpf_BasicAccountVoucher> tgpf_BasicAccountVoucherList;
        if (totalCount > 0) {
            tgpf_BasicAccountVoucherList = tgpf_BasicAccountVoucherDao.findByPage(
                tgpf_BasicAccountVoucherDao.getQuery(tgpf_BasicAccountVoucherDao.getBasicHQL(), model), pageNumber,
                countPerPage);
        } else {
            tgpf_BasicAccountVoucherList = new ArrayList<Tgpf_BasicAccountVoucher>();
        }

        List<Properties> list = new ArrayList<>();
        Properties pro;
        for (Tgpf_BasicAccountVoucher basicAccountVoucher : tgpf_BasicAccountVoucherList) {
            pro = new MyProperties();
            pro.put("tableId", basicAccountVoucher.getTableId());
            pro.put("eCode", basicAccountVoucher.geteCode());
            pro.put("billTimeStamp", StringUtils.isBlank(basicAccountVoucher.getBillTimeStamp()) ? ""
                : basicAccountVoucher.getBillTimeStamp());
            pro.put("remark",
                StringUtils.isBlank(basicAccountVoucher.getRemark()) ? "" : basicAccountVoucher.getRemark());
            pro.put("subCode",
                StringUtils.isBlank(basicAccountVoucher.getSubCode()) ? "" : basicAccountVoucher.getSubCode());
            pro.put("totalTradeAmount",
                null == basicAccountVoucher.getTotalTradeAmount() ? 0.00 : basicAccountVoucher.getTotalTradeAmount());
            pro.put("totalTradeAmount",
                null == basicAccountVoucher.getTotalTradeAmountSum() ? 0.00 : basicAccountVoucher.getTotalTradeAmountSum());
            pro.put("sendState",
                StringUtils.isBlank(basicAccountVoucher.getSendState()) ? "0" : basicAccountVoucher.getSendState());
            pro.put("sendTime",
                StringUtils.isBlank(basicAccountVoucher.getSendTime()) ? "" : basicAccountVoucher.getSendTime());
            pro.put("vou_No",
                StringUtils.isBlank(basicAccountVoucher.getVou_No()) ? "" : basicAccountVoucher.getVou_No());
            pro.put("isSplit",
                StringUtils.isBlank(basicAccountVoucher.getIsSplit()) ? "0" : basicAccountVoucher.getIsSplit());
            pro.put("contentJson",
                StringUtils.isBlank(basicAccountVoucher.getContentJson()) ? "" : basicAccountVoucher.getContentJson());
            list.add(pro);
        }

        properties.put("tgpf_BasicAccountVoucherList", list);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        return properties;
    }

    public Properties detailExecute(Tgpf_BasicAccountVoucherForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据！");
        }

        Tgpf_BasicAccountVoucher basicAccountVoucher = tgpf_BasicAccountVoucherDao.findById(tableId);
        if (null == basicAccountVoucher) {
            return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
        }

        Properties pro = new MyProperties();
        pro.put("tableId", basicAccountVoucher.getTableId());
        pro.put("eCode", basicAccountVoucher.geteCode());
        pro.put("billTimeStamp",
            StringUtils.isBlank(basicAccountVoucher.getBillTimeStamp()) ? "" : basicAccountVoucher.getBillTimeStamp());
        pro.put("remark", StringUtils.isBlank(basicAccountVoucher.getRemark()) ? "" : basicAccountVoucher.getRemark());
        pro.put("subCode",
            StringUtils.isBlank(basicAccountVoucher.getSubCode()) ? "" : basicAccountVoucher.getSubCode());
        pro.put("totalTradeAmount",
            null == basicAccountVoucher.getTotalTradeAmount() ? 0.00 : basicAccountVoucher.getTotalTradeAmount());
        pro.put("sendState",
            StringUtils.isBlank(basicAccountVoucher.getSendState()) ? "0" : basicAccountVoucher.getSendState());
        pro.put("sendTime",
            StringUtils.isBlank(basicAccountVoucher.getSendTime()) ? "" : basicAccountVoucher.getSendTime());
        pro.put("vou_No", StringUtils.isBlank(basicAccountVoucher.getVou_No()) ? "" : basicAccountVoucher.getVou_No());
        pro.put("isSplit",
            StringUtils.isBlank(basicAccountVoucher.getIsSplit()) ? "0" : basicAccountVoucher.getIsSplit());
        pro.put("contentJson",
            StringUtils.isBlank(basicAccountVoucher.getContentJson()) ? "" : basicAccountVoucher.getContentJson());

        properties.put("tgpf_BasicAccountVoucherDetail", pro);

        return properties;
    }

    @SuppressWarnings("unchecked")
    public Properties dtlListExecute(Tgpf_BasicAccountVoucherDtlForm model) {
        Properties properties = new MyProperties();

        model.setTheState(S_TheState.Normal);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据！");
        }

        Tgpf_BasicAccountVoucher basicAccountVoucher = tgpf_BasicAccountVoucherDao.findById(tableId);
        if (null == basicAccountVoucher) {
            return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
        }
        model.setAccountVoucher(basicAccountVoucher);

        Integer totalCount = tgpf_BasicAccountVoucherDtlDao.findByPage_Size(
            tgpf_BasicAccountVoucherDtlDao.getQuery_Size(tgpf_BasicAccountVoucherDtlDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Tgpf_BasicAccountVoucherDtl> tgpf_BasicAccountVoucherDtlList;
        if (totalCount > 0) {
            tgpf_BasicAccountVoucherDtlList = tgpf_BasicAccountVoucherDtlDao.findByPage(
                tgpf_BasicAccountVoucherDtlDao.getQuery(tgpf_BasicAccountVoucherDtlDao.getBasicHQL(), model),
                pageNumber, countPerPage);
        } else {
            tgpf_BasicAccountVoucherDtlList = new ArrayList<Tgpf_BasicAccountVoucherDtl>();
        }

        List<Properties> list = new ArrayList<>();
        Properties pro;
        for (Tgpf_BasicAccountVoucherDtl basicAccountVoucherDtl : tgpf_BasicAccountVoucherDtlList) {
            pro = new MyProperties();
            pro.put("tableId", basicAccountVoucherDtl.getTableId());
            pro.put("eCode", basicAccountVoucherDtl.geteCode());
            pro.put("billTimeStamp", StringUtils.isBlank(basicAccountVoucherDtl.getBillTimeStamp()) ? ""
                : basicAccountVoucherDtl.getBillTimeStamp());
            pro.put("remark",
                StringUtils.isBlank(basicAccountVoucherDtl.getRemark()) ? "" : basicAccountVoucherDtl.getRemark());
            pro.put("subCode",
                StringUtils.isBlank(basicAccountVoucherDtl.getSubCode()) ? "" : basicAccountVoucherDtl.getSubCode());
            pro.put("totalTradeAmount", null == basicAccountVoucherDtl.getTotalTradeAmount() ? 0.00
                : basicAccountVoucherDtl.getTotalTradeAmount());
            pro.put("sendState", StringUtils.isBlank(basicAccountVoucherDtl.getSendState()) ? "0"
                : basicAccountVoucherDtl.getSendState());
            pro.put("sendTime",
                StringUtils.isBlank(basicAccountVoucherDtl.getSendTime()) ? "" : basicAccountVoucherDtl.getSendTime());
            pro.put("vou_No",
                StringUtils.isBlank(basicAccountVoucherDtl.getVou_No()) ? "" : basicAccountVoucherDtl.getVou_No());
            list.add(pro);
        }

        properties.put("tgpf_BasicAccountVoucherDtlList", list);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        return properties;
    }

    public Properties dtlSaveExecute(Tgpf_BasicAccountVoucherDtlForm model) {
        Properties properties = new MyProperties();

        Long nowDate = System.currentTimeMillis();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        String billTimeStamp = model.getBillTimeStamp();
        if (StrUtil.isBlank(billTimeStamp)) {
            return MyBackInfo.fail(properties, "请选择日期！");
        }

        String remark = model.getRemark();
        if (StrUtil.isBlank(remark)) {
            return MyBackInfo.fail(properties, "请输入摘要信息！");
        }

        String subCode = model.getSubCode();
        if (StrUtil.isBlank(subCode)) {
            return MyBackInfo.fail(properties, "请输入科目代码！");
        }

        Double totalTradeAmount = model.getTotalTradeAmount();
        if (null == totalTradeAmount || (0.00 == totalTradeAmount)) {
            return MyBackInfo.fail(properties, "请输入有效的金额！");
        }

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据！");
        }

        Tgpf_BasicAccountVoucher basicAccountVoucher = tgpf_BasicAccountVoucherDao.findById(tableId);
        if (null == basicAccountVoucher) {
            return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
        }
        
        if(basicAccountVoucher.getTotalTradeAmount() - totalTradeAmount < 0.00){
            return MyBackInfo.fail(properties, "本次拆分金额超出可拆分金额！");
        }

        model.setTheState(S_TheState.Normal);
        /*Double sunAmount =
            (Double)tgpf_BasicAccountVoucherDtlDao.findOneByQuery(tgpf_BasicAccountVoucherDtlDao.getSpecialQuery(
                tgpf_BasicAccountVoucherDtlDao.getBasicHQL(), model, " nvl(sum(nvl(totalTradeAmount,0)),0) "));
        if ((basicAccountVoucher.getTotalTradeAmount() - sunAmount - totalTradeAmount) < 0) {
            return MyBackInfo.fail(properties, "本次拆分金额超出总金额！");
        }*/
        
        

        Tgpf_BasicAccountVoucherDtl dtl = new Tgpf_BasicAccountVoucherDtl();
        dtl.setAccountVoucher(basicAccountVoucher);
        dtl.setBillTimeStamp(billTimeStamp);
        dtl.setBusiState("0");
        dtl.setCreateTimeStamp(nowDate);
        dtl.setIsSplit("0");
        dtl.setLastUpdateTimeStamp(nowDate);
        dtl.setRemark(remark);
        dtl.setSendState("0");
        dtl.setSubCode(subCode);
        dtl.setTheState(S_TheState.Normal);
        dtl.setTotalTradeAmount(totalTradeAmount);
        dtl.setUserStart(model.getUser());
        dtl.setUserUpdate(model.getUser());
        tgpf_BasicAccountVoucherDtlDao.save(dtl);
        
        basicAccountVoucher.setTotalTradeAmount(basicAccountVoucher.getTotalTradeAmount() - totalTradeAmount);
        tgpf_BasicAccountVoucherDao.update(basicAccountVoucher);

        return properties;
    }

    public Properties dtlUpdateExecute(Tgpf_BasicAccountVoucherDtlForm model) {
        Properties properties = new MyProperties();

        Long nowDate = System.currentTimeMillis();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        String billTimeStamp = model.getBillTimeStamp();
        if (StrUtil.isBlank(billTimeStamp)) {
            return MyBackInfo.fail(properties, "请选择日期！");
        }

        String remark = model.getRemark();
        if (StrUtil.isBlank(remark)) {
            return MyBackInfo.fail(properties, "请输入摘要信息！");
        }

        String subCode = model.getSubCode();
        if (StrUtil.isBlank(subCode)) {
            return MyBackInfo.fail(properties, "请输入科目代码！");
        }

        Double totalTradeAmount = model.getTotalTradeAmount();
        if (null == totalTradeAmount || (0.00 == totalTradeAmount)) {
            return MyBackInfo.fail(properties, "请输入有效的金额！");
        }

        Long tableId = model.getTableId();
        if (null == tableId) {
            return MyBackInfo.fail(properties, "请选择需要查看的单据！");
        }

        Tgpf_BasicAccountVoucherDtl dtl = tgpf_BasicAccountVoucherDtlDao.findById(tableId);
        if (null == dtl) {
            return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
        }
        
        //计算差额
        Double addTotalTradeAmount = totalTradeAmount - dtl.getTotalTradeAmount();
        Tgpf_BasicAccountVoucher accountVoucher = dtl.getAccountVoucher();
        
        if(accountVoucher.getTotalTradeAmount() - addTotalTradeAmount < 0.00){
            return MyBackInfo.fail(properties, "本次拆分金额超出可拆分金额！");
        }
        
        dtl.setBillTimeStamp(billTimeStamp);
        dtl.setLastUpdateTimeStamp(nowDate);
        dtl.setRemark(remark);
        dtl.setSubCode(subCode);
        dtl.setTotalTradeAmount(totalTradeAmount);
        dtl.setUserUpdate(model.getUser());
        tgpf_BasicAccountVoucherDtlDao.update(dtl);
        
        accountVoucher.setTotalTradeAmount(accountVoucher.getTotalTradeAmount() - addTotalTradeAmount);
        tgpf_BasicAccountVoucherDao.update(accountVoucher);

        return properties;
    }

    public Properties dtlDeleteExecute(Tgpf_BasicAccountVoucherDtlForm model) {
        Properties properties = new MyProperties();

        Long nowDate = System.currentTimeMillis();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long[] idArr = model.getIdArr();
        if (null == idArr || idArr.length < 1) {
            return MyBackInfo.fail(properties, "请选择需要删除的单据信息！");
        }

        Tgpf_BasicAccountVoucher accountVoucher;
        Tgpf_BasicAccountVoucherDtl dtl;
        for (Long tableId : idArr) {
            dtl = tgpf_BasicAccountVoucherDtlDao.findById(tableId);
            if (null != dtl) {
                
                if(!S_TheState.Deleted.equals(dtl.getTheState())){
                    accountVoucher = dtl.getAccountVoucher();
                    
                    dtl.setTheState(S_TheState.Deleted);
                    dtl.setUserUpdate(model.getUser());
                    dtl.setLastUpdateTimeStamp(nowDate);
                    
                    accountVoucher.setTotalTradeAmount(accountVoucher.getTotalTradeAmount() + dtl.getTotalTradeAmount());
                    tgpf_BasicAccountVoucherDao.update(accountVoucher);
                    tgpf_BasicAccountVoucherDtlDao.update(dtl);
                }
            }
        }

        return properties;
    }

    public Properties sendExecute(Tgpf_BasicAccountVoucherForm model) {
        Properties properties = new MyProperties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Long[] idArr = model.getIdArr();
        if (null == idArr || idArr.length < 1) {
            return MyBackInfo.fail(properties, "请选择需要推送的凭证信息！");
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
                Map<String, Object> map = tgpf_BasicAccountVoucherDao.prc_Insert_pzts("0", sb.toString(),
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
