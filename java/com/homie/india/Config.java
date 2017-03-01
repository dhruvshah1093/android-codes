package com.homie.india;

public class Config {
   //dhruv-config of file written in php script =====================================//
    //Address of our scripts of the CRUD
    //public static final String URL_ADD="http://10.0.2.2:8888/Homi/get_listings.php";
   public static final String UPLOAD_URL = "http://homie.es/apps/register.php";
   public static final String UPLOAD_LIKE = "http://homie.es/apps/likeupload.php";
   public static final String UPLOAD_UNLIKE = "http://homie.es/apps/unlike.php";
    public static final String URL_GET_ALL = "http://homie.es/apps/get_listings.php";
    public static final String URL_GET_FAVORITE = "http://homie.es/apps/favorite.php?id=";
    public static final String URL_GET_CATEGORY = "http://homie.es/apps/category_finder.php?query=";
    public static final String URL_GET_FEATURED = "http://homie.es/apps/featured.php";
    public static final String URL_GET_BEST_PLACES = "http://homie.es/apps/best_places.php";
    public static final String URL_GET_LOGIN = "http://homie.es/apps/login.php";
  /* public static final String URL_UPDATE_EMP = "http://10.0.2.2:8888//crud/updateEmp.php";
   public static final String URL_DELETE_EMP = "http://10.0.2.2:8888//crud/deleteEmp.php?id=";
*/
    //Keys that will be used to send the request to php scripts
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_LISTING_NAME = "listing_name";
    public static final String KEY_LISTING_ADDRESS = "listing_address";
    public static final String KEY_LISTING_IMAGES= "listing_images";
    public static final String KEY_LISTING_CREATED_BY = "listing_created_by";
    public static final String KEY_LISTING_RENT= "listing_rent";
    public static final String KEY_LISTING_FILTERS = "listing_filters";

    //JSON Tags=======================   will get you the data ===============================
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_LISTING_NAME = "listing_name";
    public static final String TAG_LISTING_ADDRESS = "listing_address";
    public static final String TAG_LISTING_IMAGES = "listing_images";
    public static final String TAG_LISTING_CREATED_BY = "listing_created_by";
    public static final String TAG_LISTING_RENT = "listing_rent";
    public static final String TAG_LISTING_FILTERS = "listing_filters";


    //employee id to pass with intent
    public static final String LIST_ID = "emp_id";
}