package com.jigpud.snow.util.constant;

/**
 * @author jigpud
 */
public class PathConstant {
    // user service
    public static final String LOGIN_WITH_PASSWORD = "/user/login";
    public static final String LOGIN_WITH_VERIFICATION_CODE = "/user/loginWithVC";
    public static final String USER_INFO = "/user/info";
    public static final String REGISTER = "/user/register";
    public static final String RETRIEVE_PASSWORD = "/user/retrieve";
    public static final String GET_VERIFICATION_CODE = "/verificationCode";
    public static final String FOLLOW = "/user/follow";
    public static final String UNFOLLOW = "/user/unfollow";

    // token service
    public static final String REFRESH_TOKEN = "/refreshToken";

    // story service
    public static final String GET_SELF_STORY_LIST = "/story/self";
    public static final String GET_USER_STORY_LIST = "/story/list";
    public static final String GET_SELF_MOMENTS_STORY_LIST = "/story/moments/self";
    public static final String RELEASE_STORY = "/story/release";
    public static final String LIKE_STORY = "/story/like";
    public static final String UNLIKE_STORY = "/story/unlike";
    public static final String GET_STORY = "/story";

    // search service
    public static final String SEARCH_STORY = "/search/story";
    public static final String SEARCH_USER = "/search/user";

    // qiniu service
    public static final String GET_AVATAR_UPLOAD_TOKEN = "/upload/token/avatar";
    public static final String GET_ATTRACTION_IMG_UPLOAD_TOKEN = "/upload/token/attraction";
    public static final String GET_STORY_IMG_UPLOAD_TOKEN = "/upload/token/story";

    // recommend service
    public static final String GET_HOT_ATTRACTION_LIST = "/recommend/attraction/hot";
    public static final String GET_RECOMMEND_ATTRACTION_LIST = "/recommend/attraction";
    public static final String GET_RECOMMEND_USER_LIST = "/recommend/user";
}
