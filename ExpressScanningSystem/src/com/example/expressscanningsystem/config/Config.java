package com.example.expressscanningsystem.config;
/**
 * 常量类
 * @author Administrator
 *
 */
public class Config {

	/**查询所有员工*/
	public static final String SELECTALLEMPLOYEE="http://121.201.61.40:8080/KuaiDi/SelectAllEmployee";
	
	/**查询所有网点*/
	public static final String SELECTALLNETBEAN="http://121.201.61.40:8080/KuaiDi/SelectAllNetBean";
	
	/**判断登录用户是否存在*/
	public static final String LOGINUSERISEXIST="http://121.201.61.40:8080/KuaiDi/LoginUserIsExist";
	
	/**修改密码*/
	public static final String UPDATEPASSWORD="http://121.201.61.40:8080/KuaiDi/UpdatePassword";
	
	/**添加扫描*/
	public static final String ADDALLSCAN="http://121.201.61.40:8080/KuaiDi/AddAllScan";

	/**跟踪查询*/
	public static final String TRACKTHEQUERY="http://121.201.61.40:8080/KuaiDi/TrackTheQuery";
	
	/**根据电话查询员工名称和网点*/
	public static final String SelectNameAndNetByPhone="http://121.201.61.40:8080/KuaiDi/SelectNameAndNetByPhone";
	
	/**访问网址*/
	public static final String Net="http://121.201.61.40:8080/KuaiDi";
	
}
