package zhishusz.housepresell.util.ftp;

import java.io.File;
import java.util.ArrayList;

public class DocDeal {/*

	public static Boolean saveDoc(ArrayList al_del, ArrayList al_up)
			throws Exception {
		Boolean bl = false;
		if (getFtpData() != null && getFtpData().length > 0) {
			MpBaseFilepathVO ljvo = (MpBaseFilepathVO) getFtpData()[0];
			FTPManager mana = FTPManager.getInstance();
			try {
				mana.setInfo(ljvo.getIp(), ljvo.getPort() == null ? 21
						: Integer.parseInt(ljvo.getPort()), ljvo
						.getUsername(), ljvo.getPassword(), null);

				if (al_del != null && al_del.size() > 0) {
					for (int i = 0, size = al_del.size(); i < size; i++) {
						// 删除
						mana.changePath("/");
						mana.delete(al_del.get(i).toString().trim(), true);
					}
				}

				if (al_up != null && al_up.size() > 0) {
					for (int i = 0, size = al_up.size(); i < size; i++) {
						// 上传
						mana.changePath("/");
						mana.upload(((Object[]) al_up.get(i))[1].toString(),
								((Object[]) al_up.get(i))[0].toString(), true);
					}
				}

				bl = true;
			} catch (Exception ex1) {
				throw new Exception("上传档案时出错:" + ex1.getMessage()
						+ " 请联系系统管理员!");
			}finally{
				mana.disconnect();
			}
		} else {
			throw new Exception("档案路径读取错误!请检查基础档案设置!");
		}
		return bl;
	}

	*//**
	 * 删除档案方法
	 * 
	 * @param currVO
	 * @return
	 * @throws Exception
	 *//*
	public static boolean deleteDoc(ArrayList al_del) throws Exception {
		Boolean bl = false;
		if (getFtpData() != null && getFtpData().length > 0) {
			MpBaseFilepathVO ljvo = (MpBaseFilepathVO) getFtpData()[0];
			FTPManager mana = FTPManager.getInstance();
			try {

				mana.setInfo(ljvo.getIp(), ljvo.getPort() == null ? 21
						: Integer.parseInt(ljvo.getPort()), ljvo
						.getUsername(), ljvo.getPassword(), null);
				if (al_del != null && al_del.size() > 0) {
					for (int i = 0, size = al_del.size(); i < size; i++) {
						// 删除
						mana.changePath("/");
						mana.delete(al_del.get(i).toString().trim(), true);
					}
				}
				bl = true;
			} catch (Exception ex1) {
				ex1.printStackTrace();
				throw new Exception("删除档案时出错:" + ex1.getMessage()
						+ " 请联系系统管理员!");
			}finally{
				mana.disconnect();
			}
		} else {
			throw new Exception("档案路径读取错误!请检查基础档案设置!");
		}
		return bl;
	}

	*//**
	 * 下载档案方法
	 * 
	 * @param currVO
	 * @return
	 * @throws Exception
	 *//*
	public static boolean downloadDoc(ArrayList al_down) throws Exception {
		boolean bl = false;
		String dir = null;
		if (getFtpData() != null && getFtpData().length > 0) {
			MpBaseFilepathVO ljvo = (MpBaseFilepathVO) getFtpData()[0];
			FTPManager mana = FTPManager.getInstance();
			try {
				mana.setInfo(ljvo.getIp(), ljvo.getPort() == null ? 21
						: Integer.parseInt(ljvo.getPort()), ljvo
						.getUsername(), ljvo.getPassword(), null);

				dir = al_down.get(0).toString().trim();
				String filePath = al_down.get(1).toString().trim();

				String newfilePath = null;
				String newDir = null;
				java.io.File file = new java.io.File(filePath);
				if (!file.isDirectory()) {
					int endIndex = filePath.lastIndexOf("\\\\");
					newfilePath = filePath.substring(0, endIndex);
					newDir = filePath.substring(endIndex, filePath.length());
				} else
					newfilePath = filePath;

				String fullName = (newfilePath.endsWith("\\\\\\\\") ? newfilePath
						: newfilePath + "\\\\")
						+ ((newDir == null || newDir.toString().length() == 0) ? dir
								.substring(dir.lastIndexOf("/") + 1, dir
										.length())
								: (newDir + dir.substring(dir.lastIndexOf("."),
										dir.length())));

				mana.download(dir, fullName, true);
				bl = true;
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("下载文件时发生错误:读取文件\n" + dir + "\n失败！请联系系统管理员!");
			}finally{
				mana.disconnect();
			}
		}
		return bl;
	}

	public static String checkfileExists(ArrayList al_down) {
		String[] strArr = { "flag", "flagtrue", "bl" };
		String returnName = null;
		String dir = al_down.get(0).toString().trim();
		String filePath = al_down.get(1).toString().trim();

		String newfilePath = null;
		String newDir = null;
		java.io.File file = new java.io.File(filePath);
		if (!file.isDirectory()) {
			int endIndex = filePath.lastIndexOf("\\\\");
			newfilePath = filePath.substring(0, endIndex);
			newDir = filePath.substring(endIndex + 2, filePath.length());
		} else
			newfilePath = filePath;
		if (newDir != null && newDir.length() > 0) {
			String doc = "~!@#$%^&*+|`-=\\{}[]:\";\'<>?,./";

			for (int i = 0; i < newDir.toCharArray().length; i++) {
				if (doc.indexOf(newDir.charAt(i)) >= 0
						|| newDir.startsWith("(") || newDir.startsWith(")")) {
					returnName = strArr[0];
					break;

				}
			}
			if (returnName == null)
				returnName = strArr[1];
		}
		String fullName = (newfilePath.endsWith("\\\\\\\\") ? newfilePath
				: newfilePath + "\\\\")
				+ ((newDir == null || newDir.toString().length() == 0) ? dir
						.substring(dir.lastIndexOf("/") + 1, dir.length())
						: (newDir + dir.substring(dir.lastIndexOf("."), dir
								.length())));
		if (new File(fullName).exists())
			returnName = strArr[2];
		return returnName;
	}

	*//**
	 * 初始化FTP数据
	 * 
	 * @throws Exception
	 *//*
	private static SuperVO[] getFtpData() throws Exception {
		IMpDataPub ipub = (IMpDataPub) NCLocator.getInstance().lookup(
				IMpDataPub.class.getName());

		SuperVO[] vos = null;
		try {
			vos = ipub.queryData(MpBaseFilepathVO.class, "nvl(dr,0)=0 order by dr");
		} catch (Exception e) {
			throw new Exception("档案上传路径读取时发发生错误:" + e.getMessage()
					+ " 请设置基础档案!");
		}
		return vos;
	}
*/}
