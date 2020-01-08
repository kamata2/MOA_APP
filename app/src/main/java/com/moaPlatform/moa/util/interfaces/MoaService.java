package com.moaPlatform.moa.util.interfaces;

import com.moaPlatform.moa.auth.sign_up.model.ReqKgCertifiedModel;
import com.moaPlatform.moa.auth.sign_up.model.ReqPhoneNumberModel;
import com.moaPlatform.moa.auth.sign_up.model.ReqResExistsCheckModel;
import com.moaPlatform.moa.auth.sign_up.model.ResExistsPhoneNumberModel;
import com.moaPlatform.moa.auth.sign_up.model.ResKgCertifiedModel;
import com.moaPlatform.moa.auth.sign_up.model.ResLoginActivityUserInfoShellModel;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.req.ReqBadgeModel;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res.ResBadgeDataModel;
import com.moaPlatform.moa.bottom_menu.badge.badge_main.model.res.ResCoinTreeReword;
import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ReqShoppingCartDetailModel;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ResShoppingCartDetailModel;
import com.moaPlatform.moa.bottom_menu.shopping_cart.main.ReqInputShoppingCart;
import com.moaPlatform.moa.bottom_menu.shopping_cart.main.ResShoppingCartList;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.model.ReqQrMakeOrderId;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.model.ReqQrPayInfo;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.model.ResQrMakeOrderId;
import com.moaPlatform.moa.bottom_menu.wallet.barcode.model.ResQrPayInfo;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.MoaPayAgrmnt;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.MoaPaySetAgrmnt;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.ReqExchangeCoinModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.ReqPointCoin;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.request.ReqPointSaveUseModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.PointCoinModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.ResCoinExchangeInfoModel;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.ResPointSaveUseListModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.EatOutStoreInfoModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.ResEatOutStoreDetailInfo;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.ResEatOutStoreProductListInfo;
import com.moaPlatform.moa.delivery_menu.eatout_store_list.model.ReqEatOutStoreListModel;
import com.moaPlatform.moa.delivery_menu.eatout_store_list.model.ResEatOutStoreListModel;
import com.moaPlatform.moa.delivery_menu.store_detail.model.ResStoreDetailInfo;
import com.moaPlatform.moa.delivery_menu.store_list.model.ReqStoreListModel;
import com.moaPlatform.moa.delivery_menu.store_list.model.ResBannerModel;
import com.moaPlatform.moa.delivery_menu.store_list.model.ResStoreListModel;
import com.moaPlatform.moa.delivery_menu.store_product.model.ReqProductDetailInfoModel;
import com.moaPlatform.moa.delivery_menu.store_product.model.ResStoreProductDetailInfoModel;
import com.moaPlatform.moa.intro.model.ReqAgreementInfoData;
import com.moaPlatform.moa.intro.model.ResVersionModel;
import com.moaPlatform.moa.model.req_model.ReqReviewModel;
import com.moaPlatform.moa.model.req_model.ReqStoreInfoModel;
import com.moaPlatform.moa.model.res_model.ResReviewChangeScreen;
import com.moaPlatform.moa.model.res_model.ResReviewListModel;
import com.moaPlatform.moa.model.res_model.ResReviewModel;
import com.moaPlatform.moa.notification.ReqNotificationModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ReqPaymentOrderModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ReqUseCouponModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ResGetPintDataModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ResUseCouponModel;
import com.moaPlatform.moa.online_payment.order_payment.model.res.ResPaymentResult;
import com.moaPlatform.moa.payment.RequestCardData;
import com.moaPlatform.moa.payment.ResponseCardData;
import com.moaPlatform.moa.side_menu.ResSideMainList;
import com.moaPlatform.moa.side_menu.coupon.model.ReqCouponRegistration;
import com.moaPlatform.moa.side_menu.coupon.model.ResSelectCoupon;
import com.moaPlatform.moa.side_menu.customercenter.faq.model.FaqModel;
import com.moaPlatform.moa.side_menu.customercenter.faq.model.ResFaq;
import com.moaPlatform.moa.side_menu.customercenter.faq.model.ResFaqDetailItems;
import com.moaPlatform.moa.side_menu.customercenter.myquestion.model.ReqMyQuestion;
import com.moaPlatform.moa.side_menu.customercenter.myquestion.model.ResMyQuestion;
import com.moaPlatform.moa.side_menu.eventnotice.model.ResEventOrNotice;
import com.moaPlatform.moa.side_menu.favorite.model.ReqBookmarkStoreRemove;
import com.moaPlatform.moa.side_menu.favorite.model.ReqFavorite;
import com.moaPlatform.moa.side_menu.favorite.model.ResFavorite;
import com.moaPlatform.moa.side_menu.order.detail.model.ReqOrderDetail;
import com.moaPlatform.moa.side_menu.order.detail.model.ResOrderDetailHistoryShellModel;
import com.moaPlatform.moa.side_menu.order.model.ReqOrder;
import com.moaPlatform.moa.side_menu.order.model.ResOrderCancel;
import com.moaPlatform.moa.side_menu.order.model.ResOrderItems;
import com.moaPlatform.moa.side_menu.profile.model.ResUserProfile;
import com.moaPlatform.moa.side_menu.settings.account.model.AccountSettingInfoDataModel;
import com.moaPlatform.moa.side_menu.settings.noticeagree.model.NoticeAgreeCheckModel;
import com.moaPlatform.moa.side_menu.settings.noticeagree.model.ResNoticeAgreeCheck;
import com.moaPlatform.moa.side_menu.usernotice.model.ResUserNotice;
import com.moaPlatform.moa.top_menu.location.model.ReqAddressSave;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;
import com.moaPlatform.moa.top_menu.search.ReqSearchHistoryModel;
import com.moaPlatform.moa.top_menu.search.ResSearchModel;
import com.moaPlatform.moa.util.auth.model.RequestMoaAuth;
import com.moaPlatform.moa.util.auth.model.ResponseMoaAuth;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.EmptyModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;
import com.moaPlatform.moa.wallet.RequestWalletData;
import com.moaPlatform.moa.wallet.ResponseWalletData;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MoaService {
    // 사용자가 초기 집입시 이용약관 동의 데이터
    @Headers("Content-Type: application/json")
    @POST("home/setBasicAgrmntInfo")
    Call<CommonModel> postAgreementData(@Body ReqAgreementInfoData agreementInfoData);

    // 주소 검색
    @POST("addrlink/addrLinkApi.do")
    Call<ResAddressSearchBaseModel> addressSearch(@Query("confmKey") String key,
                                                  @Query("currentPage") int currentPage,
                                                  @Query("countPerPage") int countPerPage,
                                                  @Query("keyword") String keyword,
                                                  @Query("resultType") String resultType);

    // 주소 저장
    @Headers("Content-Type: application/json")
    @POST("home/setUserAddr")
    Call<CommonModel> addressSave(@Body ReqAddressSave reqAddressSave);

    // 좌표 검색
    @Headers("Content-Type: application/json")
    @POST("addrlink/addrCoordApi.do")
    Call<ResAddressSearchBaseModel> locationSearchXy(@Query("confmKey") String confmKey,
                                                     @Query("admCd") String admCd,
                                                     @Query("rnMgtSn") String rnMgtSn,
                                                     @Query("udrtYn") String udrtYn,
                                                     @Query("buldMnnm") int buldMnnm,
                                                     @Query("buldSlno") int buldSlno,
                                                     @Query("resultType") String resultType);

    // 기본 주소 및 최근 주소 불러오기
    @Headers("Content-Type: application/json")
    @POST("home/getUserAddr")
    Call<ResAddressSearchBaseModel> addressHistory(@Body CommonModel commonModel);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "home/deleteUserAddr", hasBody = true)
    Call<CommonModel> removeAddress(@Body ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel);

    @Headers("Content-Type: application/json")
    @POST("home/setDefltUserAddr")
    Call<CommonModel> defaultAddressChange(@Body ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel);

    // xy 좌표 변환
    @Headers("Content-Type: application/json")
    @POST("map/mapXyTransform")
    Call<ResAddressSearchBaseModel.LocationCoordinate> coordinatesTransform(@Body ResAddressSearchBaseModel.AddressModel addressModel);

    // 메인화면에서 카테고리, 매장 정보 불러오기
    @Headers("Content-Type: application/json")
    @POST("home/getHomeData")
    Call<ResMainServiceModel> mainServiceInfo(@Body CommonModel reqModel);

    // 가맹점 리스트 불러오기
    @Headers({"Content-Type: application/json"})
    @POST("store/getStorList")
    Call<ResStoreListModel> getStoreList(@Body ReqStoreListModel reqStoreListModel);

    // 가맹점 상세 정보 불러오기
    @Headers({"Content-Type: application/json"})
    @POST("store/getStorDtlInfo")
    Call<ResStoreDetailInfo> getStoreDetailInfo(@Body ReqStoreInfoModel model);

    // 가맹점 부가 정보 불러오기
    @Headers({"Content-Type: application/json"})
    @POST("store/getStorAddInfo")
    Call<ResStoreDetailInfo> getStoreAddInfo(@Body ReqStoreInfoModel model);

    // 상품 상세 정보 조회
    @Headers({"Content-Type: application/json"})
    @POST("store/getProdDtlInfo")
    Call<ResStoreProductDetailInfoModel> getProductDetailInfo(@Body ReqProductDetailInfoModel reqProductDetailInfoModel);

    // [외식]가맹점 리스트 불러오기
    @Headers({"Content-Type: application/json"})
    @POST("store/getEatOutStorList")
    Call<ResEatOutStoreListModel> getEatOutStoreList(@Body ReqEatOutStoreListModel reqEatOutStoreListModel);

    // [외식] 상세 정보 불러오기
    @Headers({"Content-Type: application/json"})
    @POST("store/getEatOutStorDtl")
    Call<ResEatOutStoreDetailInfo> getEatOutStoreDetailInfo(@Body ReqStoreInfoModel reqStoreInfoModel);

    // [외식] 상품 리스트 불러오기
    @Headers({"Content-Type: application/json"})
    @POST("store/getStorProdList")
    Call<ResEatOutStoreProductListInfo> getEatOutStoreProductList(@Body ReqStoreInfoModel reqStoreInfoModel);

    // [외식] 즐겨찾기 추가
    @Headers({"Content-Type: application/json"})
    @POST("bmark/setBmark")
    Call<EatOutStoreInfoModel> addEatOutStoreBookmark(@Body ReqStoreInfoModel model);

    // [외식] 즐겨찾기 삭제
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "bmark/deleteBmark", hasBody = true)
    Call<EatOutStoreInfoModel> removeEatOutStoreBookmark(@Body ReqStoreInfoModel model);

    //[외식] 리뷰 리스트
    @Headers({"Content-Type: application/json"})
    @POST("revw/getEatOutRevw")
    Call<ResReviewListModel> getEatOutStoreReviewList(@Body ReqStoreInfoModel reqStoreInfoModel);

    //[외식] 리뷰등록
    @POST("revw/setEatOutRevw ")
    Call<CommonModel> postEatOutReview(@Body RequestBody file);

    // 포인트 및 코인 조회
    @Headers({"Content-Type: application/json"})
    @POST("pay/getCoinAndPoint")
    Call<PointCoinModel> getCoinAndPoint(@Body ReqPointCoin reqPointCoin);

    // 업적(배지) 이벤트 정보 조회
    @Headers({"Content-Type: application/json"})
    @POST("event/getEventPageInfo")
    Call<ResBadgeDataModel> getEventPageInfo(@Body ReqBadgeModel reqBadgeModel);

    // 코인 나무 보상 받기
    @Headers({"Content-Type: application/json"})
    @POST("event/coinReceive")
    Call<ResCoinTreeReword> getCoinTreeReword(@Body ReqBadgeModel reqBadgeModel);

    // 배달 메뉴
    // 장바구니 입력
    @Headers({"Content-Type: application/json"})
    @POST("cart/setCart")
    Call<ResShoppingCartList> inputShoppingCart(@Body ReqInputShoppingCart reqInputShoppingCart);

    // 장바구니 개수 조회
    @Headers({"Content-Type: application/json"})
    @POST("cart/getCartCnt")
    Call<ResStoreDetailInfo.ShoppingCartCountModel> shoppingCartCountCheck(@Body CommonModel commonModel);

    // 하단 메뉴
    // 장바구니 목록 조회
    @Headers({"Content-Type: application/json"})
    @POST("cart/getCartList")
    Call<ResShoppingCartList> getShoppingCartList(@Body CommonModel commonModel);

    // 장바구니 상세 조회
    @Headers({"Content-Type: application/json"})
    @POST("cart/getCartDtlInfo")
    Call<ResShoppingCartDetailModel> getShoppingCartDetailInfo(@Body ReqShoppingCartDetailModel reqShoppingCartDetailModel);

    // 장바구니 데이터 전부 삭제
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "cart/deleteCartItem", hasBody = true)
    Call<CommonModel> deleteShoppingCart(@Body ReqShoppingCartDetailModel reqShoppingCartDetailModel);

    // 장바구니 상품 개수 증가
    @Headers({"Content-Type: application/json"})
    @PUT("cart/incrsProdCnt")
    Call<CommonModel> productPlus(@Body ReqShoppingCartDetailModel reqShoppingCartDetailModel);

    // 장바구니 상품 개수 감소
    @Headers({"Content-Type: application/json"})
    @PUT("cart/decrsProdCnt")
    Call<CommonModel> productMinus(@Body ReqShoppingCartDetailModel reqShoppingCartDetailModel);

    // 장바구니 상품 삭제
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "cart/deleteCartProd", hasBody = true)
    Call<CommonModel> shoppingCartItemRemove(@Body ReqShoppingCartDetailModel reqShoppingCartDetailModel);

    // 주문
    @Headers({"Content-Type: application/json"})
    @POST("order/setOrder")
    Call<ResPaymentResult> setOrder(@Body ReqPaymentOrderModel reqPaymentOrder);

    @Headers({"Content-Type: application/json"})
    @POST("order/getOrderUserCp")
    Call<ResUseCouponModel> getUseCoupon(@Body ReqUseCouponModel reqUseCouponModel);

    // 포인트 가져오기
    @Headers({"Content-Type: application/json"})
    @POST("pay/getCoinAndPoint")
    Call<ResGetPintDataModel> getPointModel(@Body CommonModel commonModel);

    // 포인트 가져오기
    @Headers({"Content-Type: application/json"})
    @POST("pay/getUsablePointAmt")
    Call<ResGetPintDataModel> getPaymentPointModel(@Body CommonModel commonModel);

    // 비회원 ID 등록
    @Headers({"Content-Type: application/json"})
    @POST("auth/nonMmbrIdIssue")
    Call<ResponseMoaAuth> registerNonMemberID(@Body RequestMoaAuth reqNonMemberId);

    // 비회원 ID 로그인
    @Headers({"Content-Type: application/json"})
    @POST("auth/nonMmbrLogin")
    Call<ResponseMoaAuth> doLoginNonMemberID(@Body RequestMoaAuth reqNonMemberId);


    // 전화번호 가입 유무 체크
    @Headers({"Content-Type: application/json"})
    @POST("auth/getExistsPhoneNum")
    Call<ResExistsPhoneNumberModel> getExistsPhoneNumber(@Body ReqPhoneNumberModel model);

    // ID Check
    @Headers({"Content-Type: application/json"})
    @POST("auth/mmbrIdIssue")
    Call<ResponseMoaAuth> canRegisterMemberPin(@Body RequestMoaAuth reqMemberPin);

    // PIN 등록
    @Headers({"Content-Type: application/json"})
    @POST("auth/mmbrIdIssue")
    Call<ResponseMoaAuth> registerMemberPin(@Body RequestMoaAuth reqMemberPin);

    // PIN 로그인 시작(ID/PW 기반 회원 로그인 > 유효성 검사 체크)
    @Headers({"Content-Type: application/json"})
    @POST("auth/mmbrPinLogin")
    Call<ResponseMoaAuth> startLoginMemberPin(@Body RequestMoaAuth reqMemberPin);

    // PIN 로그인 요청(ID/PW 기반 회원 로그인 > 최종 로그인)
    @Headers({"Content-Type: application/json"})
    @POST("auth/mmbrPinLogin")
    Call<ResponseMoaAuth> doLoginMemberPin(@Body RequestMoaAuth reqMemberPin);

    // PIN 재설정
    @Headers({"Content-Type: application/json"})
    @PUT("auth/mmbrPswReSet")
    Call<ResponseMoaAuth> resetPin(@Body RequestMoaAuth requestMoaAuth);

    // PIN 변경
    @Headers({"Content-Type: application/json"})
    @PUT("auth/mmbrPswChange")
    Call<ResponseMoaAuth> changePin(@Body RequestMoaAuth requestMoaAuth);

    // Fingerprint 등록 가능 여부 체크
    @Headers({"Content-Type: application/json"})
    @POST("auth/fngrpIssue")
    Call<ResponseMoaAuth> canRegisterFingerprint(@Body RequestMoaAuth reqMemberFingerprint);

    // Fingerprint 등록 요청
    @Headers({"Content-Type: application/json"})
    @POST("auth/fngrpIssue")
    Call<ResponseMoaAuth> registerFingerprint(@Body RequestMoaAuth reqMemberFingerprint);

    // Fingerprint 로그인 시작
    @Headers({"Content-Type: application/json"})
    @POST("auth/fngrpLogin")
    Call<ResponseMoaAuth> canLoginFingerprint(@Body RequestMoaAuth reqMemberFingerprint);

    // Fingerprint 로그인 요청
    @Headers({"Content-Type: application/json"})
    @POST("auth/fngrpLogin")
    Call<ResponseMoaAuth> doLoginFingerprint(@Body RequestMoaAuth reqMemberFingerprint);

    // 로그아웃
    @Headers({"Content-Type: application/json"})
    @POST("auth/logout")
    Call<ResponseMoaAuth> doLogout(@Body RequestMoaAuth reqLogout);

    // 사용자 알람 조회
    @Headers({"Content-Type: application/json"})
    @POST("notice/getUserNoti")
    Call<ResUserNotice> alarmSelect(@Body CommonModel commonModel);

    //사이드 메뉴 쿠폰
    @Headers({"Content-Type: application/json"})
    @POST("event/getUsePsblCp")
    Call<ResSelectCoupon> couponSelect(@Body CommonModel commonModel);

    //사이드 메뉴 사용된 쿠폰
    @Headers({"Content-Type: application/json"})
    @POST("event/getPassCp")
    Call<ResSelectCoupon> couponUsedSelect(@Body CommonModel commonModel);

    //사이드 메뉴 주문내역 조회
    @Headers({"Content-Type: application/json"})
    @POST("order/getOrderList")
    Call<ResOrderItems> orderListSelect(@Body ReqOrder reqOrder);

    //사이드 메뉴 주문내역 상세 조회
    @Headers({"Content-Type: application/json"})
    @POST("order/getOrderDtl")
    Call<ResOrderDetailHistoryShellModel> orderDetailSelect(@Body ReqOrderDetail reqOrderDetail);

    //사이드 메뉴 주문내역 취소내역
    @Headers({"Content-Type: application/json"})
    @POST("order/getOrderCancelList")
    Call<ResOrderCancel> orderListCancle(@Body ReqOrder reqOrder);

    //사용자 매장 즐겨찾기 삭제
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "bmark/deleteBmark", hasBody = true)
    Call<CommonModel> favoriteDelete(@Body ReqFavorite reqFavorite);

    //사이드 메뉴 쿠폰등록
    @Headers({"Content-Type: application/json"})
    @POST("event/setNewCp")
    Call<CommonModel> couponRegisterList(@Body ReqCouponRegistration reqCouponRegistration);

    //배달 리뷰등록
    @POST("revw/setStorRevw")
    Call<CommonModel> reviewRegisterList(@Body RequestBody file);

    //외식 수정 화면 조회
    @Headers({"Content-Type: application/json"})
    @POST("revw/getModfEatOutRevwInfo")
    Call<ResReviewChangeScreen> reviewEatOut(@Body ReqReviewModel model);

    //배달 리뷰 수정화면 조회
    @Headers({"Content-Type: application/json"})
    @POST("revw/getModfUserRevwInfo")
    Call<ResReviewChangeScreen> reviewChangeScreen(@Body ReqReviewModel model);

    //배달&외식 리뷰 수정[통합]
    @POST("revw/modfDlvryAndEatOutRevw")
    Call<ResReviewModel> postReviewModify(@Body RequestBody file);

    /*
     * DELETE 방식에서 @Body를 사용하기 위해서는 아래처럼 해야함.
     */
    //리뷰 삭제[통합]
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "revw/deleteDlvryAndEatOutRevw", hasBody = true)
    Call<CommonModel> reviewDelete(@Body ReqReviewModel model);

    // 계정 설정 정보 조회
    @Headers({"Content-Type: application/json"})
    @POST("home/getAcntStngInfo")
    Call<AccountSettingInfoDataModel> accountSettingInfoSearch(@Body CommonModel reqModel);

    // 계정 설정의 SMS, 이메일 수신 동의 데이터 변경
    @Headers({"Content-Type: application/json"})
    @PUT("home/setAcntStngAgrmntInfo")
    Call<CommonModel> accountSettingReceiveChange(@Body AccountSettingInfoDataModel.AccountSettingReceiveModel reqModel);

    // 본인인증
    @Headers({"Content-Type: application/json"})
    @POST("kg/getKgData")
    Call<ResKgCertifiedModel> kgResponse(@Body ReqKgCertifiedModel reqKgCertifiedModel);

    // 카드 등록 시 필수 데이터 요청 (Token, Return URL)
    @Headers({"Content-Type: application/json"})
    @POST("PaymentKicc")
    Call<ResponseCardData> requestRequiredCardRegisterData(@Body RequestCardData requestCardData);

    // 카드 등록 시 초기 데이터 요청 ()
    @Headers({"Content-Type: application/json"})
    @POST("kicc/getKiccValue")
    Call<ResponseCardData> requestInitialCardRegisterData(@Body RequestCardData requestCardData);

    // 등록된 카드 조회
    @Headers({"Content-Type: application/json"})
    @POST("PaymentKicc")
    Call<ResponseCardData> showCardList(@Body RequestCardData requestCardData);

    // 카드 결제 (MoaPay)
    @Headers({"Content-Type: application/json"})
    @POST("PaymentKicc")
    Call<ResponseCardData> payCard(@Body RequestCardData requestCardData);

    // 카드 결제 취소
    @Headers({"Content-Type: application/json"})
    @PUT("order/setOrderCancel")
    Call<ResponseCardData> refundPay(@Body RequestCardData requestCardData);

    // 카드 삭제
    @Headers({"Content-Type: application/json"})
    @POST("PaymentKicc")
    Call<ResponseCardData> deleteOneCard(@Body RequestCardData requestCardData);

    // 카드 닉네임 변경
    @Headers({"Content-Type: application/json"})
    @POST("PaymentKicc")
    Call<ResponseCardData> changeCardNick(@Body RequestCardData requestCardData);

    // 결제 비밀번호 변경
    @Headers({"Content-Type: application/json"})
    @POST("PaymentKicc")
    Call<ResponseCardData> changePayPsw(@Body RequestCardData requestCardData);

    // 결제 비밀번호 초기화
    @Headers({"Content-Type: application/json"})
    @POST("PaymentKicc")
    Call<ResponseCardData> initPayPsw(@Body RequestCardData requestCardData);

    // 카드 결제 (EasyPay)
    @Headers({"Content-Type: application/json"})
    @POST("kicc/getUserPayInfo")
    Call<ResponseCardData> setEasyPayData(@Body RequestCardData requestCardData);

    //이벤트 공지 조회
    @Headers({"Content-Type: application/json"})
    @POST("notice/getEvntList")
    Call<ResEventOrNotice> eventList(@Body CommonModel commonModel);

    //공지 조회
    @Headers({"Content-Type: application/json"})
    @POST("notice/getNoticeList")
    Call<ResEventOrNotice> noticeList(@Body CommonModel commonModel);

    //자주묻는 질문 항목 조회
    @Headers({"Content-Type: application/json"})
    @POST("/faq/getFaqList")
    Call<ResFaq> faqList();

    //자주묻는질문 상세 질문 조회
    @Headers({"Content-Type: application/json"})
    @POST("faq/getFaqDtlList")
    Call<ResFaqDetailItems> faqDtList(@Body FaqModel FAQModel);

    //자주묻는질문 1:1 문의 등록
    @POST("inquiry/setInqry")
    Call<CommonModel> uploadImage(@Body RequestBody file);

    //문의 내역 조회
    @Headers({"Content-Type: application/json"})
    @POST("inquiry/getInqryList")
    Call<ResMyQuestion> InqryList(@Body ReqMyQuestion reqMyQuestion);

    //설정 화면 동의 정보 get
    @Headers({"Content-Type: application/json"})
    @POST("home/getStngAgrmntInfo")
    Call<ResNoticeAgreeCheck> getInfo(@Body NoticeAgreeCheckModel noticeAgreeCheckModel);

    //설정 화면 정보 set
    @Headers({"Content-Type: application/json"})
    @PUT("home/setStngAgrmntInfo ")
    Call<ResNoticeAgreeCheck> setInfo(@Body NoticeAgreeCheckModel model);

    //사이드메뉴 메인
    @Headers({"Content-Type: application/json"})
    @POST("home/getSideMenu")
    Call<ResSideMainList> getSide(@Body CommonModel commonModel);

    //프로필 가져오기
    @Headers({"Content-Type: application/json"})
    @POST("user/getUserProfile")
    Call<ResUserProfile> getProfile(@Body CommonModel commonModel);

    //프로필 수정
    @POST("user/modfUserProfile")
    Call<CommonModel> setProfile(@Body RequestBody file);

    //배달 리뷰 리스트
    @Headers({"Content-Type: application/json"})
    @POST("revw/getDlvryRevw")
    Call<ResReviewListModel> getDeliveryReviewList(@Body ReqStoreInfoModel reqStoreInfoModel);

    // 최근 검색어 조회
    @Headers({"Content-Type: application/json"})
    @POST("home/getRcntSrchWord")
    Call<ResSearchModel> recentlySearchHistory(@Body CommonModel commonModel);

    // 검색어로 매장 조회
    @Headers({"Content-Type: application/json"})
    @POST("home/getSrchStor")
    Call<ResSearchModel> getSearchStoreList(@Body ReqSearchHistoryModel reqSearchHistoryModel);

    // 최근 검색어 삭제
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "home/deleteRcntSrchWord", hasBody = true)
    Call<CommonModel> removeSearchHistory(@Body ReqSearchHistoryModel reqSearchHistoryModel);

    // 최근 검색어 삭제
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "home/deleteAllRcntSrchWord", hasBody = true)
    Call<CommonModel> allRemoveSearchHistory(@Body CommonModel commonModel);

    //모아페이 약관 동의 존재유무
    @Headers({"Content-Type: application/json"})
    @POST("pay/getUserMoaPayAgrmntAvailable")
    Call<CommonModel> getMoaPayAgrmnt(@Body MoaPayAgrmnt moaPayAgrmnt);

    @Headers({"Content-Type: application/json"})
    @POST("pay/setMoaPayAgrmntInfo")
    Call<CommonModel> setMoaPayAgrmnt(@Body MoaPaySetAgrmnt moaPaySetAgrmnt);

    // 전자지갑 존재 여부 확인 (서버)
    @Headers({"Content-Type: application/json"})
    @POST("elecwallet/walletExistCheck")
    Call<ResponseWalletData> confirmIfWalletExist(@Body RequestWalletData requestWalletData);

    // 전자지갑 등록
    @Headers({"Content-Type: application/json"})
    @POST("elecwallet/moaBcWalletIssue")
    Call<ResponseWalletData> registerRestoreWallet(@Body RequestWalletData requestWalletData);

    // 전자지갑 복원
    @Headers({"Content-Type: application/json"})
    @POST("elecwallet/blockWalletRestore")
    Call<ResponseWalletData> requestRestoreWallet(@Body RequestWalletData requestWalletData);

    //코인변환 정보 조회
    @Headers({"Content-Type: application/json"})
    @POST("elecwallet/getCoinExchangeInfo")
    Call<ResCoinExchangeInfoModel> getCoinExchangeInfo(@Body CommonModel model);

    //코인 포인트 전환
    @Headers({"Content-Type: application/json"})
    @POST("elecwallet/exchangeCoinForPoint")
    Call<CommonModel> exchangeCoinForPoint(@Body ReqExchangeCoinModel model);

    // 베너 불러오기
    @Headers({"Content-Type: application/json"})
    @POST("event/getTopBanner")
    Call<ResBannerModel> getBanner(@Body CommonModel commonModel);

    //포인트 사용내역 조회
    // 주문
    @Headers({"Content-Type: application/json"})
    @POST("pay/getPointSaveUseList ")
    Call<ResPointSaveUseListModel> getPointSaveUseList(@Body ReqPointSaveUseModel model);

    //푸시키 등록
    @Headers({"Content-Type: application/json"})
    @PUT("user/setFcmKey")
    Call<CommonModel> setFcmKey(@Body ReqNotificationModel model);

    // 회원가입 관련
    // 중복 이메일 조회
    @Headers({"Content-Type: application/json"})
    @POST("auth/getExistsEmail")
    Call<ReqResExistsCheckModel> emailSearch(@Body ReqResExistsCheckModel reqModel);

    // 추천인 핸드폰 번호 조회
    @Headers({"Content-Type: application/json"})
    @POST("auth/getExistsPhoneNum")
    Call<ReqResExistsCheckModel> recommenderNumber(@Body ReqResExistsCheckModel reqModel);

    //앱 업데이트 체크
    @Headers({"Content-Type: application/json"})
    @POST("home/getStartData")
    Call<ResVersionModel> getStartData(@Body EmptyModel model);

    //QR 코드 결제 정보 조회
    @Headers({"Content-Type: application/json"})
    @POST("/pay/getQrCodePayInfo")
    Call<ResQrPayInfo> getQrPayInfo(@Body ReqQrPayInfo reqQrPayInfo);

    //QR 코드 주문 등록
    @Headers({"Content-Type: application/json"})
    @POST("/order/setOrderToQrCode")
    Call<ResQrMakeOrderId> setQrMakeOrderId(@Body ReqQrMakeOrderId reqQrMakeOrderId);

    // 주문 취소 (환불)
    @Headers({"Content-Type: application/json"})
    @PUT("/order/setOrderCancel")
    Call<CommonModel> orderCancel(@Body ReqOrderDetail reqOrderDetail);

    // 회원탈퇴
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "/auth/mmbrUnRegist", hasBody = true)
    Single<Response<ResponseMoaAuth>> unregisterMember(@Body RequestMoaAuth requestMoaAuth);

    // 주문 삭제
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "/order/deleteOrder", hasBody = true)
    Single<Response<CommonModel>> requestDeleteOrder(@Body ReqOrderDetail reqOrderDetail);

    // 유저 정보
    @Headers({"Content-Type: application/json"})
    @POST("/order/getOrderUserInfo")
    Call<ResLoginActivityUserInfoShellModel> getUserInfo(@Body CommonModel commonModel);

    // 전자지갑 패스워드 초기화 [1차(시작), 2차(서버 업데이트), 3차(롤백 여부)]
    @Headers({"Content-Type: application/json"})
    @POST("elecwallet/moaBcWalletPswReSet")
    Single<Response<ResponseWalletData>> requestInitWalletPsw(@Body RequestWalletData requestWalletData);

    /**
     * ********************
     * 즐겨 찾기 관련 start
     * ********************
     */
    //사용자 즐겨찾기 조회
    @Headers({"Content-Type: application/json"})
    @POST("bmark/getBmarkLst")
    Call<ResFavorite> favoriteSelect(@Body CommonModel commonModel);

    // 즐겨찾기 추가
    @Headers({"Content-Type: application/json"})
    @POST("bmark/setBmark")
    Call<StoreInfoModel> addBookmark(@Body ReqStoreInfoModel model);

    // 즐겨찾기 삭제
    @Headers({"Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "bmark/deleteBmark", hasBody = true)
    Call<StoreInfoModel> removeBookmark(@Body ReqBookmarkStoreRemove reqBookmarkStoreRemove);
}
