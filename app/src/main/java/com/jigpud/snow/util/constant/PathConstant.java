package com.jigpud.snow.util.constant;

/**
 * @author : jigpud
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
    public static final String UPDATE_INFO = "/user/updateInfo";

    // token service
    public static final String REFRESH_TOKEN = "/refreshToken";

    // story service
    public static final String GET_SELF_STORY_LIST = "/story/self";
    public static final String GET_USER_STORY_LIST = "/story/list";
    public static final String GET_MOMENTS_STORY_LIST = "/story/moments/self";
    public static final String POST_STORY = "/story/post";
    public static final String LIKE_STORY = "/story/like";
    public static final String UNLIKE_STORY = "/story/unlike";
    public static final String GET_STORY = "/story";
    public static final String COMMENT_STORY = "/story/comment/post";
    public static final String STORY_COMMENT_LIST = "/story/comment/list";
    public static final String LIKE_COMMENT = "/story/comment/like";
    public static final String UNLIKE_COMMENT = "/story/comment/unlike";
    public static final String GET_COMMENT = "/story/comment";
    public static final String FAVORITE_STORY = "/favorite/story/favorite";
    public static final String UN_FAVORITE_STORY = "/favorite/story/unFavorite";
    public static final String GET_SELF_FAVORITE_STORY_LIST = "/favorite/story/self";

    // search service
    public static final String SEARCH_STORY = "/search/story";
    public static final String SEARCH_USER = "/search/user";
    public static final String SEARCH_ATTRACTION = "/search/attraction";

    // qiniu service
    public static final String GET_AVATAR_UPLOAD_TOKEN = "/upload/token/avatar";
    public static final String GET_ATTRACTION_IMG_UPLOAD_TOKEN = "/upload/token/attraction";
    public static final String GET_STORY_IMG_UPLOAD_TOKEN = "/upload/token/story";
    public static final String GET_USER_PROFILE_BG_UPLOAD_TOKEN = "/upload/token/userProfileBackground";

    // recommend service
    public static final String GET_HOT_ATTRACTION_LIST = "/recommend/attraction/hot/list";
    public static final String GET_RECOMMEND_ATTRACTION_LIST = "/recommend/attraction/list";
    public static final String GET_RECOMMEND_USER_LIST = "/recommend/user/list";

    // attraction service
    public static final String GET_ATTRACTION = "/attraction";
    public static final String GET_ATTRACTION_STORY_LIST = "/attraction/story/list";
    public static final String SCORE_ATTRACTION = "/attraction/score";
    public static final String GET_ATTRACTION_PICTURE_LIST = "/attraction/picture/list";
    public static final String UPLOAD_ATTRACTION_PICTURE = "/attraction/picture/upload";
    public static final String DELETE_ATTRACTION_PICTURE = "/attraction/picture/delete";
    public static final String FOLLOW_ATTRACTION = "attraction/follow";
    public static final String UNFOLLOW_ATTRACTION = "attraction/unfollow";
    public static final String FOLLOWING_ATTRACTION_LIST = "/attraction/following/list";
    public static final String GET_ATTRACTION_FOOD_LIST = "/attraction/food/list";
    
    // food service
    public static final String GET_FOOD = "/food";
    public static final String GET_FOOD_LIST = "/food/list";
    public static final String GET_FOOD_PICTURE_LIST = "/food/picture/list";
    public static final String UPLOAD_FOOD_PICTURE = "/food/picture/upload";
    public static final String DELETE_FOOD_PICTURE = "/food/picture/delete";
    public static final String GET_FOOD_ATTRACTION_LIST = "/food/attraction/list";
}
