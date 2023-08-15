package test.util;

import zhishusz.housepresell.util.license.VerifyLicense;

import org.junit.Test;

/**
 * Created by Dechert on 2018-09-17.
 * Company: zhishusz
 */

public class TestLicense {
	@Test
	public void verify(){
		VerifyLicense vLicense = new VerifyLicense();
		//获取参数
		vLicense.setParam("license/HousePresellVerification.properties");
		//验证证书
		vLicense.verify();

	}

}
