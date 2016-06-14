package com.example.expressscanningsystem.tools;

import android.text.TextUtils;

/**
 * 验证工具类
 * 
 * @author Administrator
 * 
 */
public class VerifyTools {

	private static VerifyTools verifyTools;

	private VerifyTools() {

		if (verifyTools == null) {

			verifyTools = new VerifyTools();

		}

	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}
	
	/**
     * 验证身份证号是否符合规则
     * @param text 身份证号
     * @return
     */
     public static boolean personIdValidation(String text) {
          String regx = "[0-9]{17}x";
          String reg1 = "[0-9]{15}";
          String regex = "[0-9]{18}";
          return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

}
