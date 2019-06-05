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
//    public static final String TRIGGER_EMAIL = BASE_URL + "profileApp/v1/profile/triggerOtpEmail";
    public static final String TRIGGER_EMAIL = "https://technicalanna.000webhostapp.com/sendemail.php";
    public static final String UPDATE_PASSWORD = BASE_URL + "profileApp/v1/profile/updatePassword";

    // Booster API
    public static final String BOOSTER_ADD = BASE_URL + "boosterApp/v1/booster/add";
    public static final String BOOSTER_DETAIL = BASE_URL + "boosterApp/v1/booster/getBoosterDetail";
    public static final String BOOSTER_UPDATE = BASE_URL + "boosterApp/v1/booster/updateBoosterDetail";
    public static final String BOOSTER_DELETE = BASE_URL + "boosterApp/v1/booster/deleteBooster";
    public static final String ALL_BOOSTER = BASE_URL + "boosterApp/v1/booster/getAllBooster";

    // Subject-wise Exam API
    public static final String SUBJECT_EXAM_ADD = BASE_URL + "subjectExamsApp/v1/subjectexams/add";
    public static final String SUBJECT_EXAM_DELETE = BASE_URL + "subjectExamsApp/v1/subjectexams/delete";
    public static final String ALL_SUBJECT_EXAM = BASE_URL + "subjectExamsApp/v1/subjectexams/getAllExams";
    public static final String SUBJECT_EXAM_DETAIL = BASE_URL + "subjectExamsApp/v1/subjectexams/getExamDetails";

    // Full-length Exam API
    public static final String FULL_EXAM_ADD_NOTIFICATION = BASE_URL + "fullLengthExamsApp/v1/fullexam/addNotification";
    public static final String FULL_EXAM_ADD_SYLLABUS = BASE_URL + "fullLengthExamsApp/v1/fullexam/addSyllabus";
    public static final String FULL_EXAM_ADD_PREV_QUES_PAPER = BASE_URL + "fullLengthExamsApp/v1/fullexam/previousQuestionPaper";
    public static final String FULL_EXAM_ADD = BASE_URL + "fullLengthExamsApp/v1/fullexam/add";
    public static final String FULL_EXAM_EXAM_DETAIL = BASE_URL + "fullLengthExamsApp/v1/fullexam/getExamDetails";
    public static final String FULL_EXAM_DELETE = BASE_URL + "fullLengthExamsApp/v1/fullexam/delete";
    public static final String ALL_FULL_EXAM = BASE_URL + "fullLengthExamsApp/v1/fullexam/getAllExams";
    public static final String FULL_EXAM_CHECK_SUBSCRIBTION = BASE_URL + "fullLengthExamsApp/v1/fullexam/checkSubscribtion";
    public static final String FULL_EXAM_ADD_SUBSCRIBTION = BASE_URL + "fullLengthExamsApp/v1/fullexam/addSubscribtion";
    public static final String FULL_EXAM_GET_DETAIL_BY_NAME = BASE_URL + "fullLengthExamsApp/v1/fullexam/fetchDataByName";


    // Wallet API
    public static final String GET_WALLET_DETAIL = BASE_URL + "walletApp/v1/wallet/getWalletAmount";
    public static final String CREDIT_WALLET = BASE_URL + "walletApp/v1/wallet/credit";
    public static final String DEBIT_WALLET = BASE_URL + "walletApp/v1/wallet/debit";

}
