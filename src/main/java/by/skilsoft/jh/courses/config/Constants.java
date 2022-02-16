package by.skilsoft.jh.courses.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";

    public static final String MAIN_ACL_URL = "https://practice.sqilsoft.by/sandbox/yury_sinkevich/microservice_acl/dev";

    public static final String CHECK_PERMISSION_URL = MAIN_ACL_URL + "/api/permission/check";

    public static final String ADD_PERMISSION_FOR_USER = MAIN_ACL_URL + "/api/permissions/user";

    public static final String TENANT_ID = "courses";

    private Constants() {}
}
