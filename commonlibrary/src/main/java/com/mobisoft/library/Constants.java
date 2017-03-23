package com.mobisoft.library;

public class Constants {

	public static final String RELEASE = android.os.Build.VERSION.RELEASE;
	public static final String MODEL = android.os.Build.MODEL;
	public static final String OS_SYSTEM = "android";

	/** APP 版本号，版本名称 */
	public static String VERSION_NAME = "";
	/** APP 版本号，数字 */
	public static int VERSION_CODE = 0;

	public static int SCREEN_WIDTH = 0;

	public static int SCREEN_HEIGTH = 0;
	/** 登录用户id:123456789012 */
	public static String ACCOUNT = "";
	/** 登陆用户的密码 */
	public static String PASSWORD = "";


	// 在发正式版本的时候，改为false，在设置为true的时候，回输入各种调试信息以及异常，在设置false的时候，只输出异常信息

	public static final boolean DEBUG = true;

	public static class URL {

		// 我的部门
		public static final String GET_CONTACT_DEPARTMENT = "/contact/getMyOrg";

		// 我的部门下面的子部门及人员
		// public static final String GET_MY_CHILD_DEPARTMENT =
		// "/contact/getOrgAndPersonByOrgId";
		public static final String GET_MY_CHILD_DEPARTMENT = "/contact/getOrgAndPersonByOrgId2";

		// 查询组织架构
		public static final String GET_CONTACT_ORG = "/contact/getChildOrg";

		// 查询联系人详情
		public static final String GET_CONTACT_INFO = "/contact/getUserDetail";

		// 获取部门下面所有的人员
		public static final String GET_CONTACT_ALL_ORG_USER = "/contact/getAllUserByOrgId";

		// 编辑联系人的备注
		public static final String SET_CONTACT_REMARK = "/contact/addUserRemark";

		// 添加设置为常用联系人
		public static final String ADD_CONTACT_LINKMAN = "/contact/addFrequentLinkman";

		// 删除常用联系人
		public static final String DEL_CONTATC_LINKMAN = "/contact/deleteFrequentLinkman";

		// 设置手机的可见性
		public static final String SET_PHONE_STATUE = "/contact/setPhoneVisibility";

		// 查询手机的可见性
		public static final String GET_PHONE_STATUE = "/contact/getPhoneVisibility";

		// 获取常用联系人列表
		public static final String GET_LINKMAN_LIST = "/contact/getFrequentLinkmanList";

		// 搜索人员
		public static final String SEARCH_MAN = "/contact/searLinkman";
		// 搜索部门
		public static final String SEARCH_ORG = "/contact/searchOrg";

		// 获取群组列表
		public static final String GET_GROUP_LIST = "/qx/getContact";
	}

}
