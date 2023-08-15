package zhishusz.housepresell.util.license;

import de.schlichtherle.license.*;

/**
 * Created by Dechert on 2018-09-17.
 * Company: zhishusz
 */

public class LicenseManagerHolder {
	private static LicenseManager licenseManager;

	public static synchronized LicenseManager getLicenseManager(LicenseParam licenseParams) {
		if (licenseManager == null) {
			licenseManager = new LicenseManager(licenseParams);
		}
		return licenseManager;
	}

}
