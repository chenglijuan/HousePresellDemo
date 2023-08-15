package zhishusz.housepresell.external.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import zhishusz.housepresell.controller.form.*;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.po.CxContractInfoModel;
import zhishusz.housepresell.external.po.TripleAgreementModel;
import zhishusz.housepresell.service.Sm_BusinessCodeGetService;
import zhishusz.housepresell.util.CallableTest;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.SocketUtil;
import zhishusz.housepresell.util.fileupload.OssServerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * 网银数据推送接收service
 *
 * @author Administrator
 */
@Service
@Transactional
@Slf4j
public class CjzxService {

    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;// 接口报文表

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// eCode生成规则

    @Autowired
    private Tgxy_TripleAgreementDao tgxy_TripleAgreementDao;

    @Autowired
    private Empj_BuildingInfoDao empj_buildingInfoDao;

    @Autowired
    private Empj_HouseInfoDao empjHouseInfoDao;

    @Autowired
    private OssServerUtil ossUtil;// 本地上传OSS

    @Autowired
    private Tgxy_BuyerInfoDao tgxy_buyerInfoDao;

    @Autowired
    private Tgxy_ContractInfoDao tgxy_contractInfoDao;

    @Autowired
    private Sm_BaseParameterDao sm_BaseParameterDao;

    @Autowired
    private Sm_AttachmentDao sm_attachmentDao;

    //生成三方协议存储路径
    static String tripleagreementurl = "D:\\uploadxy\\";


    //通过楼栋id返回协议版本号以及三方协议流水号
    public Properties getXybhByBuildingId(HttpServletRequest request, JSONObject obj) {
        Properties properties = new Properties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        try {
            if (obj == null) {

                return MyBackInfo.fail(properties, "请输入传递参数");
            }

            // 记录接口交互信息
            Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
            tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
            tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
            tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
            tgpf_SocketMsg.setMsgStatus(1);// 发送状态
            tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
            //公积金提交数据到正泰
            tgpf_SocketMsg.setMsgDirection("CJZX_TO_ZT01");// 报文方向
            tgpf_SocketMsg.setMsgContentArchives(obj.toJSONString());// 报文内容
            tgpf_SocketMsg.setReturnCode("200");// 返回码
            tgpf_SocketMsgDao.save(tgpf_SocketMsg);

            //预售楼栋id
            String buildingId = obj.getString("buildingId");
            //预售户室id
//            String roomId = obj.getString("roomId");
//
//            System.out.println("buildingId=" + buildingId);
//            System.out.println("roomId=" + roomId);
            // 通过楼栋id返回协议编号和版本号

            // 三方协议号
            // 系统生成，生成规则：Tgxy_EscrowAgreement.eCodeOfAgreement+四位流水号（按天流水）
            String sql = "select * from Tgxy_EscrowAgreement where theState = 0 and businessProcessState ='7' " +
                    "and tableId=(select A.TGXY_ESCROWAGREEMENT from Rel_EscrowAgreement_Building a, empj_buildinginfo b " +
                    "where a.empj_buildinginfo = b.tableid  and b.thestate = 0 and b.EXTERNALID = '" + buildingId + "' ) ";

            Tgxy_EscrowAgreement tgxy_EscrowAgreement = null;
            try {
                tgxy_EscrowAgreement = sessionFactory.getCurrentSession().createNativeQuery(sql, Tgxy_EscrowAgreement.class)
                        .uniqueResult();
            } catch (HibernateException e) {
                return MyBackInfo.fail(properties, "查询楼幢签署的合作协议异常，请确认后重试");
            }
            if (null == tgxy_EscrowAgreement) {
                return MyBackInfo.fail(properties, "楼幢施工编号:" + buildingId + "未查询到已审核的合作协议相关信息");
            }


            /*
             * 1.编号（eCode）:新增时自动生成，规则待定
             * 2.三方协议号(eCodeOfTripleAgreement)：自动编号，不可修改，编号规则：
             * 合作协议号+四位流水号（按天流水）
             *
             * 合作协议号由楼幢施工编号查询合作协议带出
             */
            //查询楼栋合作协议
            String tripleAgreementEcode = "";
            Tgxy_TripleAgreementForm codeModel;
            Integer totalCount = 1;
            while (totalCount > 0) {
                tripleAgreementEcode = sm_BusinessCodeGetService
                        .getTripleAgreementEcode(tgxy_EscrowAgreement.geteCodeOfAgreement());

                codeModel = new Tgxy_TripleAgreementForm();
                codeModel.seteCodeOfTripleAgreement(tripleAgreementEcode);

                totalCount = tgxy_TripleAgreementDao.findByPage_Size(
                        tgxy_TripleAgreementDao.getQuery_Size(tgxy_TripleAgreementDao.getBasicHQL(), codeModel));
            }

            //查询三方协议版本号
            String triversionsql = "select d.theversion from tgxy_tripleagreementvermng d,Tgxy_EscrowAgreement b,Rel_EscrowAgreement_Building a,empj_buildinginfo c" +
                    " where b.thestate = 0 and b.businessProcessState = '7' and a.tgxy_escrowagreement = b.tableid  and a.empj_buildinginfo = c.tableid " +
                    " and b.AGREEMENTVERSION = d.ecodeofca and c.EXTERNALID = '" + buildingId + "'";

            String tripleAgreementversion = sessionFactory.getCurrentSession().createSQLQuery(triversionsql).uniqueResult().toString();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tripleagreementCode", tripleAgreementEcode);
            jsonObject.put("tripleagreementVersion", tripleAgreementversion);

            properties.put("tripleagreementInfo", jsonObject);

            // 记录接口交互信息
            Tgpf_SocketMsg tgpf_SocketMsg1 = new Tgpf_SocketMsg();
            tgpf_SocketMsg1.setTheState(S_TheState.Normal);// 状态：正常
            tgpf_SocketMsg1.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
            tgpf_SocketMsg1.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
            tgpf_SocketMsg1.setMsgStatus(1);// 发送状态
            tgpf_SocketMsg1.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
            //公积金提交数据到正泰
            tgpf_SocketMsg1.setMsgDirection("ZT_TO_CJZX01");// 报文方向
            tgpf_SocketMsg1.setMsgContentArchives(jsonObject.toJSONString());// 报文内容
            tgpf_SocketMsg1.setReturnCode("200");// 返回码
            tgpf_SocketMsgDao.save(tgpf_SocketMsg1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }


    //预售系统返回合同基本信息
    public Properties getXyContractInfo(HttpServletRequest request, @RequestBody JSONObject obj) {

        Properties properties = new Properties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        try {

            if (obj == null) {
                return MyBackInfo.fail(properties, "请输入传递参数");
            }

            // 记录接口交互信息
            Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
            tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
            tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
            tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
            tgpf_SocketMsg.setMsgStatus(1);// 发送状态
            tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
            //公积金提交数据到正泰
            tgpf_SocketMsg.setMsgDirection("CJZX_TO_ZT02");// 报文方向
            tgpf_SocketMsg.setMsgContentArchives(obj.toJSONString());// 报文内容
            tgpf_SocketMsg.setReturnCode("200");// 返回码
            tgpf_SocketMsgDao.save(tgpf_SocketMsg);

            //合同编号
            String contractNo = obj.getString("contractNo");
            //房屋坐落
            String position = obj.getString("position");
            //买受人
            String buyer = obj.getString("buyer");
            //出卖人（公司名称）
            String seller = obj.getString("seller");
            //房屋面积
            String roomArea = obj.getString("roomArea");
            //预售项目id
            String projectId = obj.getString("projectId");
            //预售系统楼栋id
            String buildingId = obj.getString("buildingId");
            //预售系统户室id
            String roomId = obj.getString("roomId");
            //合同金额
            String contractAmount = obj.getString("contractAmount");
            //付款方式
            /**
             * 1.贷款方式付款
             * 2.一次性付款
             * 3.分期付款
             * 4.其他方式
             */
            String paymentMethod = obj.getString("paymentMethod");
            //首付款金额
            String downPayment = obj.getString("downPayment");
            //交付日期
            String deliveryDate = obj.getString("deliveryDate");
            //买受人证件类型
            String buyerCardType = obj.getString("buyerCardType");
            //买受人证件号码
            String buyerCardNo = obj.getString("buyerCardNo");
            //买受人联系方式
            String buyerPhone = obj.getString("buyerPhone");
            //买受人联系地址
            String buyerAddress = obj.getString("buyerAddress");
            //文件base64
            String tripleagreementfj = obj.getString("tripleagreementfj");
            //三方协议编号
            String tripleagreementEcode = obj.getString("tripleagreementEcode");

            if (StringUtils.isBlank(contractNo)) {
                return MyBackInfo.fail(properties, "合同备案号不能为空");
            }
            if (StringUtils.isBlank(position)) {
                return MyBackInfo.fail(properties, "房屋坐落不能为空");
            }
            if (StringUtils.isBlank(buyer)) {
                return MyBackInfo.fail(properties, "买受人不能为空");
            }
            if (StringUtils.isBlank(seller)) {
                return MyBackInfo.fail(properties, "出卖人不能为空");
            }
            if (StringUtils.isBlank(roomId)) {
                return MyBackInfo.fail(properties, "roomId不能为空");
            }
            if (StringUtils.isBlank(buildingId)) {
                return MyBackInfo.fail(properties, "楼栋id不能为空");
            }
            if (StringUtils.isBlank(contractAmount)) {
                return MyBackInfo.fail(properties, "合同金额不能为空");
            }
            if (StringUtils.isBlank(paymentMethod)) {
                return MyBackInfo.fail(properties, "首付款金额不能为空");
            }
            if (StringUtils.isBlank(tripleagreementfj)) {
                return MyBackInfo.fail(properties, "三方协议附件不能为空");
            }
            if (StringUtils.isBlank(tripleagreementEcode)) {
                return MyBackInfo.fail(properties, "三方协议编号不能为空");
            }

            Double contractAmountdouble = Double.parseDouble(contractAmount);
            Double roomAreaDouble = Double.parseDouble(roomArea);
            Double downPaymentDouble = Double.parseDouble(downPayment);


            Empj_BuildingInfoForm form = new Empj_BuildingInfoForm();
            form.setExternalId(buildingId);  //预售关联id

            List<Empj_BuildingInfo> empj_BuildingInfoList = empj_buildingInfoDao.findByPage(empj_buildingInfoDao.getQuery(empj_buildingInfoDao.getBaseHql(), form));
            if (empj_BuildingInfoList == null || empj_BuildingInfoList.size() <= 0) {
                return MyBackInfo.fail(properties, "查询楼幢信息为空");
            }
            Empj_BuildingInfo buildingInfo = empj_BuildingInfoList.get(0);
            //查询楼栋是否存在
            if (null == buildingInfo.getTheState() || "1".equals(buildingInfo.getTheState() + "")) {
                return MyBackInfo.fail(properties, "该楼栋已经删除，请联系管理员");
            }
            // 查询户室是否存在
            Empj_HouseInfo house;
            Empj_HouseInfoForm houseInfoForm = new Empj_HouseInfoForm();
            houseInfoForm.setExternalId(roomId);
            List<Empj_HouseInfo> houseInfoList = empjHouseInfoDao.findByPage(empjHouseInfoDao.getQuery(empjHouseInfoDao.getDetailHQL(), houseInfoForm));

            if (empj_BuildingInfoList == null || empj_BuildingInfoList.size() <= 0) {
                return MyBackInfo.fail(properties, "查询户室信息为空或者已经被删除，请联系他管理员");
            }
            house = houseInfoList.get(0);
            //查询合同是否存在
            String sql = "select * from Tgxy_TripleAgreement where theState = 0 and eCodeOfContractRecord ='" + contractNo + "'";

            List<Tgxy_TripleAgreement> tripleAgreements = sessionFactory.getCurrentSession().createNativeQuery(sql, Tgxy_TripleAgreement.class).getResultList();

            //将附件提交到oss服务器
            String tripleagreementurl = this.parseAndUpload(tripleagreementfj);
            // 合同备案号不存在，新增
            if (tripleAgreements == null || tripleAgreements.size() <= 0) {
                addNewTripleAgreement(buildingInfo, tripleagreementEcode, house, contractNo,
                        seller, buyer, position, contractAmountdouble, roomAreaDouble, downPaymentDouble,
                        paymentMethod, deliveryDate, buyerCardType, buyerCardNo, buyerPhone, buyerAddress, tripleagreementurl);

            } else {
                // 先删除协议 ，再重新添加一份
                Tgxy_TripleAgreement deleteTri = tripleAgreements.get(0);
                if (deleteTri.getTotalAmount() != null && deleteTri.getTotalAmount() > 0) {
                    return MyBackInfo.fail(properties, "已经入账不能重新新增");
                }

                deleteTri.setTheState(1);
                tgxy_TripleAgreementDao.update(deleteTri);
                // 删除合同备案号
                String contractSql = "select * from tgxy_contractinfo where theState = 0 and ECODEOFCONTRACTRECORD ='" + contractNo + "'";
                List<Tgxy_ContractInfo> contractInfos = sessionFactory.getCurrentSession().createNativeQuery(contractSql, Tgxy_ContractInfo.class).getResultList();
                if (contractInfos != null && contractInfos.size() > 0) {
                    for (Tgxy_ContractInfo contractInfo : contractInfos) {
                        tgxy_contractInfoDao.delete(contractInfo);
                    }
                }
                //删除买受人
                String buyerSql = "select * from tgxy_buyerinfo where theState = 0 and ecodeofcontract ='" + contractNo + "'";
                List<Tgxy_BuyerInfo> delbuyer = sessionFactory.getCurrentSession().createNativeQuery(buyerSql, Tgxy_BuyerInfo.class).getResultList();
                if (delbuyer != null && delbuyer.size() > 0) {
                    for (Tgxy_BuyerInfo buyerInfo : delbuyer) {
                        tgxy_buyerInfoDao.delete(buyerInfo);
                    }
                }

                //从档案系统撤销数据
                TripleAgreementModel model = new TripleAgreementModel();
                model.setXybh(tripleagreementEcode);
                model.setCzfs("0");

                String query = model.toStringDelete();
//
                Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
                paraModel.setParametertype("70");
                paraModel.setTheValue("700002");
                List<Sm_BaseParameter> list = new ArrayList<>();
                list = sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));
                if (list.isEmpty()) {
                    return MyBackInfo.fail(properties, "未查询到相应的请求接口，请查询基础参数是否正确！");
                }
                String url = list.get(0).getTheName();
//
//                //正式接口请求
                int restFul = SocketUtil.getInstance().getRestFul(url, query);
                //调试接口
//                int restFul = 1;
//                //记录接口交互信息
                Tgpf_SocketMsg tgpf_SocketMsgcx = new Tgpf_SocketMsg();
                tgpf_SocketMsgcx.setTheState(S_TheState.Normal);// 状态：正常
                tgpf_SocketMsgcx.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
                tgpf_SocketMsgcx.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
                tgpf_SocketMsgcx.setMsgStatus(1);// 发送状态
                tgpf_SocketMsgcx.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
//
                tgpf_SocketMsgcx.setRemark(url);
                tgpf_SocketMsgcx.setMsgDirection("ZT_TO_DA_001");// 报文方向
                tgpf_SocketMsgcx.setMsgContent(query);// 报文内容
                tgpf_SocketMsgcx.setMsgContentArchives(query);// 报文内容
                tgpf_SocketMsgcx.setReturnCode(String.valueOf(restFul));// 返回码

                tgpf_SocketMsgDao.save(tgpf_SocketMsgcx);
                if (restFul == 0) {
                    return MyBackInfo.fail(properties, "请求异常，请稍后再试！");
                }
                System.out.println("------------------重新新增一份");
                addNewTripleAgreement(buildingInfo, tripleagreementEcode, house, contractNo,
                        seller, buyer, position, contractAmountdouble, roomAreaDouble, downPaymentDouble,
                        paymentMethod, deliveryDate, buyerCardType, buyerCardNo, buyerPhone, buyerAddress, tripleagreementurl);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }


    public String parseAndUpload(String tripleagreementfj) {
        //文件base64
        String fjname = UUID.randomUUID() + ".pdf";
        String urlPath = "";
        try {
            CallableTest.upload(Base64.decode(tripleagreementfj), tripleagreementurl, fjname);

            FileBytesResponse fileBytesResponse = null;//返回参数
            ossUtil.setRemoteType("1");
            System.out.println("url=" + tripleagreementurl + fjname);
            ReceiveMessage uploadOrgObjJson = ossUtil.upload(tripleagreementurl + fjname);//模拟上传获取返回路径
            if (uploadOrgObjJson.getData() != null) {
                fileBytesResponse = uploadOrgObjJson.getData().get(0);
                if (uploadOrgObjJson.getData().get(0).getUrl() != null) {
                    urlPath = uploadOrgObjJson.getData().get(0).getUrl();//获取文件路径
                }
            }
            System.out.println("urlPath=" + urlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlPath;
    }


    /**
     * @param buildingInfo         楼栋id
     * @param tripleagreementEcode 三方协议编号
     * @param house                户室
     * @param contractNo           合同备案号
     * @param seller               买受人
     * @param buyer                出卖人
     * @param position             房屋坐落
     * @param contractAmountdouble 合同金额
     * @param roomAreaDouble       房屋面积
     * @param downPaymentDouble    首付款金额
     * @param paymentMethod        支付方式
     * @param deliveryDate         交付日期
     * @return
     */
    public Properties addNewTripleAgreement(Empj_BuildingInfo buildingInfo, String tripleagreementEcode, Empj_HouseInfo house, String contractNo,
                                            String seller, String buyer, String position, Double contractAmountdouble, Double roomAreaDouble, Double downPaymentDouble,
                                            String paymentMethod, String deliveryDate, String buyerCardType, String buyerCardNo, String buyerPhone, String buyerAddress, String tripleagreementurl) {

        Properties properties = new Properties();

        System.out.println("新增数据tripleagreementurl=" + tripleagreementurl);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        Tgxy_TripleAgreement tgxy_TripleAgreement = new Tgxy_TripleAgreement();
        tgxy_TripleAgreement.setTheState(S_TheState.Normal);

        Sm_User userStart = new Sm_User();
        userStart.setTableId(652L);
        // 创建人、操作人、创建时间、最后修改时间
        tgxy_TripleAgreement.setUserStart(userStart);
        tgxy_TripleAgreement.setUserUpdate(userStart);
        tgxy_TripleAgreement.setCreateTimeStamp(System.currentTimeMillis());
        tgxy_TripleAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());

        // 获取项目信息
        Empj_ProjectInfo project = buildingInfo.getProject();
        if (null == project) {
            return MyBackInfo.fail(properties, "查询项目信息为空");
        }
        if (null == project.getTheName() || project.getTheName().trim().isEmpty()) {
            return MyBackInfo.fail(properties, "项目名称为空");
        }
        tgxy_TripleAgreement.setProject(project);
        tgxy_TripleAgreement.setTheNameOfProject(project.getTheName());

        tgxy_TripleAgreement.setBuildingInfo(buildingInfo);
        tgxy_TripleAgreement.seteCodeOfBuilding(buildingInfo.geteCodeOfLand());
        tgxy_TripleAgreement.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());


        String[] idd = UUID.randomUUID().toString().split("-");
        // 合同eCode
        String eCode = idd[0] + idd[1];
        tgxy_TripleAgreement.setEcodeOfContract(eCode);

        // 审批流程状态  审核中
        tgxy_TripleAgreement.setApprovalState(S_ApprovalState.Examining);
        tgxy_TripleAgreement.seteCode(tripleagreementEcode);
        tgxy_TripleAgreement.setBusiState("99");

        tgxy_TripleAgreement.seteCodeOfTripleAgreement(tripleagreementEcode);
        tgxy_TripleAgreement.setHouse(house);
        tgxy_TripleAgreement.setUnitRoom(house.getRoomId());

        // 单元
        tgxy_TripleAgreement.setUnitInfo(house.getUnitInfo());


        // 其他信息
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String tripleAgreementTimeStamp = formatDate.format(System.currentTimeMillis());// 协议日期
        tgxy_TripleAgreement.seteCodeOfContractRecord(contractNo);// 合同备案号
        tgxy_TripleAgreement.setTripleAgreementTimeStamp(tripleAgreementTimeStamp);// 协议日期
        tgxy_TripleAgreement.setSellerName(seller);// 出卖人
        tgxy_TripleAgreement.setBuyerName(buyer);// 买受人
        tgxy_TripleAgreement.setEscrowCompany("常州正泰房产居间服务有限公司");// 托管机构

        // 协议相关状态信息
        tgxy_TripleAgreement.setTheStateOfTripleAgreement("2");// 三方协议状态
        tgxy_TripleAgreement.setTheStateOfTripleAgreementFiling("0");// 三方协议归档状态
        tgxy_TripleAgreement.setTheStateOfTripleAgreementEffect("0");// 三方协议效力状态

        tgxy_TripleAgreement.setPrintMethod("1");// 打印方式
        tgxy_TripleAgreement.setTotalAmountOfHouse(0.00);// 户托管入账总金额


        // 三方协议存储合同信息
        /*
         * 合同相关信息
         */
        tgxy_TripleAgreement.setRoomlocation(position);

        if (null == contractAmountdouble || contractAmountdouble < 0) {
            tgxy_TripleAgreement.setContractAmount(0.00);
        } else {
            tgxy_TripleAgreement.setContractAmount(contractAmountdouble);
        }

        tgxy_TripleAgreement.setBuildingArea(roomAreaDouble);

        // 首付款
        // 贷款金额=合同总价-首付款

        MyDouble dplan = MyDouble.getInstance();
        Double loanAmount = dplan.doubleSubtractDouble(contractAmountdouble, downPaymentDouble);

        // 保存首付款和贷款金额
        tgxy_TripleAgreement.setLoanAmount(loanAmount < 0 ? 0.00 : loanAmount);
        tgxy_TripleAgreement.setFirstPayment(downPaymentDouble);

        Serializable tableId = tgxy_TripleAgreementDao.save(tgxy_TripleAgreement);

        // 三方协议保存后将信息存入户室关联
        house.setTripleAgreement(tgxy_TripleAgreement);
        empjHouseInfoDao.save(house);

        /*
         * xsz by time 2018-9-18 15:19:02
         * 后台附件信息整合
         */

        // 查询合同是否存在，存在删除并更新，不存在直接新增
        String sql = "select * from tgxy_contractinfo where theState = 0 and ECODEOFCONTRACTRECORD ='" + contractNo + "'";

        List<Tgxy_ContractInfo> contractInfos = sessionFactory.getCurrentSession().createNativeQuery(sql, Tgxy_ContractInfo.class).getResultList();
        if (contractInfos != null && contractInfos.size() > 0) {
            for (Tgxy_ContractInfo contractInfo : contractInfos) {
                tgxy_contractInfoDao.delete(contractInfo);
            }
        } else {
            // 保存合同信息
            Tgxy_ContractInfo tgxy_ContractInfo = new Tgxy_ContractInfo();

            // 基本信息
            tgxy_ContractInfo.setUserStart(userStart);
            tgxy_ContractInfo.setUserUpdate(userStart);
            tgxy_ContractInfo.setCreateTimeStamp(System.currentTimeMillis());
            tgxy_ContractInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
            tgxy_ContractInfo.setTheState(S_TheState.Normal);
            tgxy_ContractInfo.setSyncPerson(userStart.getTheName());// 同步人
            tgxy_ContractInfo.setSyncDate(System.currentTimeMillis());// 同步时间

            tgxy_ContractInfo.setBuildingInfo(buildingInfo);//楼幢信息
            tgxy_ContractInfo.seteCodeOfContractRecord(contractNo);// 合同备案号
            tgxy_ContractInfo.setTheNameFormCompany(seller);// 开发企业名称
            tgxy_ContractInfo.setTheNameOfProject(project.getTheName());// 项目名称
            tgxy_ContractInfo.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());// 施工编号
            tgxy_ContractInfo.setHouseInfo(house);// 户室信息
            tgxy_ContractInfo.setRoomIdOfHouseInfo(house.getRoomId());// 室号
            tgxy_ContractInfo.setContractSumPrice(contractAmountdouble);// 合同总价
            tgxy_ContractInfo.setBuildingArea(roomAreaDouble);// 建筑面积
            tgxy_ContractInfo.setPosition(position);// 房屋坐落
            tgxy_ContractInfo.setContractSignDate(tripleAgreementTimeStamp);// 合同签订日期


            String fkfs = StrUtil.isBlank(paymentMethod) ? "" : paymentMethod;
            switch (fkfs) {
                case "一次性付款":
                    tgxy_ContractInfo.setPaymentMethod("1");// 付款方式
                    break;
                case "分期付款":
                    tgxy_ContractInfo.setPaymentMethod("2");// 付款方式
                    break;
                case "贷款方式付款":
                    tgxy_ContractInfo.setPaymentMethod("3");// 付款方式
                    break;
                case "其他方式":
                    tgxy_ContractInfo.setPaymentMethod("4");// 付款方式
                    break;
                default:
                    tgxy_ContractInfo.setPaymentMethod("3");// 付款方式
                    break;
            }

            tgxy_ContractInfo.seteCodeOfBuilding(buildingInfo.geteCodeFromPresellSystem());// 备案系统楼幢编号
            tgxy_ContractInfo.seteCodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());// 公安编号
            tgxy_ContractInfo.setContractRecordDate(tripleAgreementTimeStamp);// 合同备案日期
//            // 贷款金额
            tgxy_ContractInfo.setLoanAmount(loanAmount < 0 ? 0.00 : loanAmount);
//            // 首付款
            tgxy_ContractInfo.setFirstPaymentAmount(downPaymentDouble < 0 ? 0.00 : downPaymentDouble);
//
            tgxy_ContractInfo.setPayDate(deliveryDate);

            tgxy_contractInfoDao.save(tgxy_ContractInfo);

            house.setTripleAgreement(tgxy_TripleAgreement);
            house.setContractInfo(tgxy_ContractInfo);
            empjHouseInfoDao.save(house);
        }


        /*
         * 保存预售系统买受人信息
         *
         * 先根据合同备案号查询出买受人信息
         * 将查询出的买受人信息删除
         * 再保存
         */
        /*
         * 根据合同备案号查询买受人信息
         *  如果合同存在买受人，需要先删除再新增
         */
        Tgxy_BuyerInfoForm buyForm = new Tgxy_BuyerInfoForm();
        buyForm.seteCodeOfContract(contractNo);
        buyForm.setTheState(S_TheState.Normal);
        List<Tgxy_BuyerInfo> tgxy_BuyerInfoList = new ArrayList<Tgxy_BuyerInfo>();
        tgxy_BuyerInfoList = tgxy_buyerInfoDao
                .findByPage(tgxy_buyerInfoDao.getQuery(tgxy_buyerInfoDao.getBasicHQL(), buyForm));
        if (null != tgxy_BuyerInfoList && tgxy_BuyerInfoList.size() > 0) {
            for (Tgxy_BuyerInfo tgxy_BuyerInfo : tgxy_BuyerInfoList) {
                tgxy_buyerInfoDao.delete(tgxy_BuyerInfo);
            }
        }

        //多人购买
        if (buyer.contains(",")) {
            String[] names = buyer.split(",");
            String[] cardnos = buyerCardNo.split(",");
            String[] phones = buyerPhone.split(",");
            for (int i = 0; i < names.length; i++) {
                Tgxy_BuyerInfo buyerInfo = new Tgxy_BuyerInfo();
                buyerInfo.setTheState(S_TheState.Normal);
                buyerInfo.setUserStart(userStart);
                buyerInfo.setUserUpdate(userStart);
                buyerInfo.setCreateTimeStamp(System.currentTimeMillis());
                buyerInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
                buyerInfo.seteCodeOfContract(contractNo);// 合同备案号
                buyerInfo.seteCodeOfTripleAgreement(tgxy_TripleAgreement.geteCodeOfTripleAgreement());// 三方协议号
                buyerInfo.setContactAdress(StrUtil.isBlank(position) ? "" : position);
                buyerInfo.setAgentAddress(
                        StrUtil.isBlank(buyerAddress) ? "" : buyerAddress);
                buyerInfo.setCertificateType("1");
                buyerInfo.setBuyerName(names[i]);
                buyerInfo.seteCodeOfTripleAgreement(tripleagreementEcode);

                if (cardnos.length < names.length) {
                    buyerInfo.seteCodeOfcertificate(cardnos[0]);
                } else {
                    buyerInfo.seteCodeOfcertificate(cardnos[i]);
                }
                if (phones.length < names.length) {
                    buyerInfo.setContactPhone(phones[0]);
                } else {
                    buyerInfo.setContactPhone(phones[i]);
                }
                tgxy_buyerInfoDao.save(buyerInfo);
            }
        }else{
            //如果只有一个买受人
            Tgxy_BuyerInfo buyerInfo = new Tgxy_BuyerInfo();
            buyerInfo.setTheState(S_TheState.Normal);
            buyerInfo.setUserStart(userStart);
            buyerInfo.setUserUpdate(userStart);
            buyerInfo.setCreateTimeStamp(System.currentTimeMillis());
            buyerInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
            buyerInfo.seteCodeOfContract(contractNo);// 合同备案号
            buyerInfo.seteCodeOfTripleAgreement(tgxy_TripleAgreement.geteCodeOfTripleAgreement());// 三方协议号
            buyerInfo.setContactAdress(StrUtil.isBlank(position) ? "" : position);
            buyerInfo.setAgentAddress(StrUtil.isBlank(buyerAddress) ? "" : buyerAddress);
            buyerInfo.setCertificateType("1");
            buyerInfo.setBuyerName(buyer);
            buyerInfo.seteCodeOfTripleAgreement(tripleagreementEcode);
            buyerInfo.seteCodeOfcertificate(buyerCardNo);
            buyerInfo.setContactPhone(buyerPhone);
            tgxy_buyerInfoDao.save(buyerInfo);
        }

        Sm_AttachmentCfg cfg = new Sm_AttachmentCfg();
        cfg.setBusiType("010201N18092200028");
        cfg.setTableId(71874l);
        Sm_Attachment sm_Attachment = new Sm_Attachment();
        sm_Attachment.setUserStart(userStart);
        sm_Attachment.setAttachmentCfg(cfg);
        sm_Attachment.setUserUpdate(userStart);
        sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
        sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
        sm_Attachment.setSourceId(tableId.toString());// 关联Id
        sm_Attachment.setBusiType("06110301");// 业务类型
        sm_Attachment.setTheState(S_TheState.Normal);
        sm_Attachment.setFileType("pdf");
        sm_Attachment.setRemark(buyer + ".pdf");
        sm_Attachment.setTheLink(tripleagreementurl);
        sm_Attachment.setSourceType("010201N18092200028");
        sm_Attachment.setTheSize("236.79KB");
        sm_attachmentDao.save(sm_Attachment);


        //成功以后将数据推送到档案系统
        System.out.println("推送SFXY附件信息END：" + System.currentTimeMillis());

        TripleAgreementModel modelVo = new TripleAgreementModel();
        modelVo.setHtbh(contractNo);
        modelVo.setXybh(tripleagreementEcode);
        modelVo.setQymc(seller);
        modelVo.setXmmc(project.getTheName());
        modelVo.setSgzl(house.getPosition());
        modelVo.setJzmj(house.getActualArea().toString());
        modelVo.setFjm(tripleagreementurl);
        modelVo.setMsrxm(buyer);
        /*
         *  参数名  类型 描述  Int
         *  返回值“1”表示数据插入成功
         *  返回值“0”表示数据插入异常
         */
        String query = modelVo.toStringAdd();

        Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
        paraModel.setParametertype("70");
        paraModel.setTheValue("700001");
        List<Sm_BaseParameter> list = new ArrayList<>();
        list = sm_BaseParameterDao
                .findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));

        if (list.isEmpty()) {
            return MyBackInfo.fail(properties, "未查询到相应的请求接口，请查询基础参数是否正确！");
        }

        String url = list.get(0).getTheName();

        log.info("推送SFXY========START：" + System.currentTimeMillis());
        System.out.println("推送SFXY========START：" + System.currentTimeMillis());
        // 正式接口请求
//        int restFul = SocketUtil.getInstance().getRestFul(url, query);
        int restFul = 1;
        log.info("推送SFXY========END：" + System.currentTimeMillis());
        System.out.println("推送SFXY========END：" + System.currentTimeMillis());


        ///新增三方协议
        Tgpf_SocketMsg tgpf_SocketMsgda = new Tgpf_SocketMsg();

        tgpf_SocketMsgda.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsgda.setRemark(url);
        tgpf_SocketMsgda.setMsgContentArchives(query);
        tgpf_SocketMsgda.setMsgDirection("ZT_TO_DA_002");// 报文方向
        tgpf_SocketMsgda.setReturnCode(String.valueOf(restFul));
        tgpf_SocketMsgda.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsgDao.save(tgpf_SocketMsgda);


        if (restFul == 0) {
            return MyBackInfo.fail(properties, "推送失败，请检查后再试！");
        }
        System.out.println("推送完成了------------------");
        //
        return properties;
    }


    //批量撤销合同信息  托管系统删除
    public Properties getCxContractInfo(HttpServletRequest request, JSONArray obj) {

        Properties properties = new Properties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        try {
            if (obj == null) {
                return MyBackInfo.fail(properties, "请输入传递参数");
            }
            // 记录接口交互信息
            Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
            tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
            tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
            tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
            tgpf_SocketMsg.setMsgStatus(1);// 发送状态
            tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
            //公积金提交数据到正泰
            tgpf_SocketMsg.setMsgDirection("CJZX_TO_ZT03");// 报文方向
            tgpf_SocketMsg.setMsgContentArchives(obj.toJSONString());// 报文内容
            tgpf_SocketMsg.setReturnCode("200");// 返回码
            tgpf_SocketMsgDao.save(tgpf_SocketMsg);

            String contractNo = "";
            String tripleagreementCode = "";

            //从档案系统撤销数据

            Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
            paraModel.setParametertype("70");
            paraModel.setTheValue("700002");
            List<Sm_BaseParameter> list = new ArrayList<>();
            list = sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));
            if (list.isEmpty()) {
                return MyBackInfo.fail(properties, "未查询到相应的请求接口，请查询基础参数是否正确！");
            }
            String url = list.get(0).getTheName();


//            System.out.println("------"+obj.size());

            for (int i = 0; i < obj.size(); i++) {

                String jsonObject = JSON.toJSONString(obj.get(i));
                //将json转成需要的对象
                CxContractInfoModel zbVo = JSONObject.parseObject(jsonObject, CxContractInfoModel.class);
                contractNo = zbVo.getContractNo();
                tripleagreementCode = zbVo.getTripleagreementCode();
                if(StringUtils.isBlank(contractNo) || StringUtils.isBlank(tripleagreementCode))
                    continue;

                // 删除三方协议信息
                String tripleagreementSql = "select * from Tgxy_TripleAgreement where theState = 0 and eCodeOfContractRecord ='" + contractNo + "'";

                List<Tgxy_TripleAgreement> tripleAgreements = sessionFactory.getCurrentSession().createNativeQuery(tripleagreementSql, Tgxy_TripleAgreement.class).getResultList();
                if(tripleAgreements == null || tripleAgreements.size() <= 0){
                    continue;
                }

                if (tripleAgreements != null && tripleAgreements.size() > 0) {
                    Tgxy_TripleAgreement tripleAgreement = tripleAgreements.get(0);
                    if(tripleAgreement.getTotalAmount() != null && tripleAgreement.getTotalAmount() > 0){
                        continue;
                    }
                    tripleAgreement.setTheState(S_TheState.Deleted);
                    tgxy_TripleAgreementDao.update(tripleAgreement);
                }

                // 删除合同备案号
                String sql = "select * from tgxy_contractinfo where theState = 0 and ECODEOFCONTRACTRECORD ='" + contractNo + "'";
                List<Tgxy_ContractInfo> contractInfos = sessionFactory.getCurrentSession().createNativeQuery(sql, Tgxy_ContractInfo.class).getResultList();
                if (contractInfos != null && contractInfos.size() > 0) {
                    for (Tgxy_ContractInfo contractInfo : contractInfos) {
                        tgxy_contractInfoDao.delete(contractInfo);
                    }
                }
                //删除买受人
                Tgxy_BuyerInfoForm buyForm = new Tgxy_BuyerInfoForm();
                buyForm.seteCodeOfContract(contractNo);
                buyForm.setTheState(S_TheState.Normal);
                List<Tgxy_BuyerInfo> tgxy_BuyerInfoList = new ArrayList<Tgxy_BuyerInfo>();
                tgxy_BuyerInfoList = tgxy_buyerInfoDao
                        .findByPage(tgxy_buyerInfoDao.getQuery(tgxy_buyerInfoDao.getBasicHQL(), buyForm));
                if (null != tgxy_BuyerInfoList && tgxy_BuyerInfoList.size() > 0) {
                    for (Tgxy_BuyerInfo tgxy_BuyerInfo : tgxy_BuyerInfoList) {
                        tgxy_buyerInfoDao.delete(tgxy_BuyerInfo);
                    }
                }



                //删除合同
                TripleAgreementModel model = new TripleAgreementModel();
                model.setXybh(tripleagreementCode);
                model.setCzfs("0");
                String query = model.toStringDelete();

//                //正式接口请求
                int restFul = SocketUtil.getInstance().getRestFul(url, query);
//                int restFul = 1;
                //
//                //记录接口交互信息
                Tgpf_SocketMsg tgpf_SocketMsgcx = new Tgpf_SocketMsg();
                tgpf_SocketMsgcx.setTheState(S_TheState.Normal);// 状态：正常
                tgpf_SocketMsgcx.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
                tgpf_SocketMsgcx.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
                tgpf_SocketMsgcx.setMsgStatus(1);// 发送状态
                tgpf_SocketMsgcx.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
//
                tgpf_SocketMsgcx.setRemark(url);
                tgpf_SocketMsgcx.setMsgDirection("ZT_TO_DA_003");// 报文方向
                tgpf_SocketMsgcx.setMsgContent(query);// 报文内容
                tgpf_SocketMsgcx.setMsgContentArchives(query);// 报文内容
                tgpf_SocketMsgcx.setReturnCode(String.valueOf(restFul));// 返回码

                tgpf_SocketMsgDao.save(tgpf_SocketMsgcx);

            }
            // 记录接口交互信息
            Tgpf_SocketMsg tgpf_SocketMsg1 = new Tgpf_SocketMsg();
            tgpf_SocketMsg1.setTheState(S_TheState.Normal);// 状态：正常
            tgpf_SocketMsg1.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
            tgpf_SocketMsg1.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
            tgpf_SocketMsg1.setMsgStatus(1);// 发送状态
            tgpf_SocketMsg1.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
            //公积金提交数据到正泰
            tgpf_SocketMsg1.setMsgDirection("ZT_TO_CJZX03");// 报文方向
//            tgpf_SocketMsg1.setMsgContentArchives(jsonObject.toJSONString());// 报文内容
            tgpf_SocketMsg1.setReturnCode("200");// 返回码
            tgpf_SocketMsgDao.save(tgpf_SocketMsg1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }


}
