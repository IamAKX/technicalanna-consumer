package com.akashapplications.technicalanna.Utils;

public class API {
    public static final String BASE_URL = "https://us-central1-technical-anna.cloudfunctions.net/";

//    PROFILE APIs
    public static final String REGISTER = BASE_URL + "profileApp/v1/profile/register";
    public static final String LOGIN = BASE_URL + "profileApp/v1/profile/login";
    public static final String UPDATE_PROFILE = BASE_URL + "profileApp/v1/profile/updateProfile";
    public static final String SET_PHONE_VERIFIED = BASE_URL + "profileApp/v1/profile/setPhoneVerified";
    public static final String SET_EMAIL_VERIFIED = BASE_URL + "profileApp/v1/profile/setEmailVerified";
    public static final String GET_PROFILE_DETAIL = BASE_URL + "profileApp/v1/profile/getProfileDetail";
    public static final String DELETE_PROFILE = BASE_URL + "profileApp/v1/profile/deleteAccount";
    public static final String TRIGGER_EMAIL = BASE_URL + "profileApp/v1/profile/triggerOtpEmail";
    public static final String UPDATE_PASSWORD = BASE_URL + "profileApp/v1/profile/updatePassword";
}
