package zhishusz.housepresell.service;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.AesUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.PwdUtil;

/*
 * Service用户修改密码
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_UserEditPwdService {
    @Autowired
    private Sm_UserDao sm_UserDao;

    public Properties execute(Sm_UserForm model) {
        Properties properties = new MyProperties();

        Long userId = model.getUserId();
        String oldPwd = model.getOldPwd();
        String newPwd = model.getNewPwd();

        if (userId == null || userId < 1) {
            return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
        }
        Sm_User sm_User = sm_UserDao.findById(userId);
        if (sm_User == null) {
            return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
        }
        if (oldPwd == null || oldPwd.length() == 0) {
            return MyBackInfo.fail(properties, "请输入旧密码");
        }
        if (!AesUtil.getInstance().encrypt(oldPwd).equals(sm_User.getLoginPassword())) {
            return MyBackInfo.fail(properties, "旧密码错误");
        }
        if (newPwd == null || newPwd.length() == 0) {
            return MyBackInfo.fail(properties, "请输入新密码");
        }
        String temp = PwdUtil.checkPwd(newPwd);
        if (StringUtils.isNotBlank(temp)) {
            return MyBackInfo.fail(properties, temp);
        }
        sm_User.setLoginPassword(AesUtil.getInstance().encrypt(newPwd));
        //设置密码过期时间为修改后的90天
        long time = new Date().getTime() + 24 * 3600 * 1000l * 90;
        sm_User.setPwdExpireTimeStamp(time);

        sm_UserDao.save(sm_User);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }

    public Properties expirePwdexecute(Sm_UserForm model) {
        Properties properties = new MyProperties();
        try {
            Long userId = model.getUserId();
            String oldPwd = model.getOldPwd();
            String newPwd = model.getNewPwd();
            String cNewPwd = model.getCNewPwd();

//            System.out.println("userId=" + userId);
//            System.out.println("oldPwd=" + oldPwd);
//            System.out.println("newPwd=" + newPwd);
//            System.out.println("cNewPwd=" + cNewPwd);

            if (userId == null || userId < 1) {
                return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
            }
            if (StringUtils.isBlank(oldPwd)) {
                return MyBackInfo.fail(properties, "旧密码不能为空");
            }
            // 如果不为空
            String temp = PwdUtil.checkPwd(newPwd);
            if (StringUtils.isNotBlank(temp)) {
                return MyBackInfo.fail(properties, temp);
            }
            String temp1 = PwdUtil.checkPwd(cNewPwd);
            if (StringUtils.isNotBlank(temp1)) {
                return MyBackInfo.fail(properties, temp1);
            }
            if (!newPwd.equals(cNewPwd)) {
                return MyBackInfo.fail(properties, "新密码和确认密码不相同");
            }
            Sm_User sm_User = sm_UserDao.findById(userId);
            if (sm_User == null) {
                return MyBackInfo.fail(properties, S_NormalFlag.info_NeedLogin);
            }
            if (!AesUtil.getInstance().encrypt(oldPwd).equals(sm_User.getLoginPassword())) {
                return MyBackInfo.fail(properties, "旧密码错误");
            }
            sm_User.setLoginPassword(AesUtil.getInstance().encrypt(newPwd));
//            sm_User.setPwdExpireTimeStamp(1849660302000l);
            //设置密码过期时间为注册后的90天
            long time = new Date().getTime() + 24 * 3600 * 1000l * 90;
            sm_User.setPwdExpireTimeStamp(time);

            sm_UserDao.save(sm_User);
            properties.put(S_NormalFlag.result, S_NormalFlag.success);
            properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}
