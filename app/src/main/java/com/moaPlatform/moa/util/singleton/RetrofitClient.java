package com.moaPlatform.moa.util.singleton;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.util.interfaces.MoaService;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private String accessToken = "";
    private Long expirationDate;
    private MoaService moaService;
    private MoaService basicService;
    private MoaService addressService;

    private RetrofitClient() {
        // Access Token 이 없는 Retrofit
        Retrofit restApi = new Retrofit.Builder()
                .baseUrl(BuildConfig.REST_API_BASE_URL)
                .client(new OkHttpClient().newBuilder().addInterceptor(
                        new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE)).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        basicService = restApi.create(MoaService.class);

        // 주소 관련 Retrofit
        Retrofit addressRestApi = new Retrofit.Builder()
                .baseUrl("http://www.juso.go.kr/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        addressService = addressRestApi.create(MoaService.class);
    }

    public static RetrofitClient getInstance() {
        return Singleton.instance;
    }

    /**
     * 재발급된 Access Token 이 있는지 체크한다.
     * 재발급된 Access Token 이 있는 경우, 내부적으로 Access Token 을 갱신한다.
     * Access Token 이 갱신된 경우, 서버 통신을 재요청 해야한다.
     *
     * @param commonModel 재발급된 Access Token 과 만료날짜
     * @return true : 재발급된 Access Token 이 존재
     * false : 재발급된 Access Token 이 없음
     */
    public boolean hasReissuedAccessToken(CommonModel commonModel) {
        String accessToken = commonModel.accessToken;
        if (accessToken == null || accessToken.length() == 0)
            return false;
        else {
            updateAccessToken(commonModel.accessToken, commonModel.accessExpirationDate);
            return true;
        }
    }

    /**
     * Access Token 이 설정된 Service 를 리턴한다.
     */
    public MoaService getMoaService() {
        setAccessTokenHeader();
        return moaService;
    }

    /**
     * Access Token 이 없는 Service 를 리턴한다.
     */
    public MoaService getMoaBasicService() {
        return basicService;
    }

    /**
     * 주소 관련 Service 를 리턴한다.
     */
    public MoaService getMoaAddressService() {
        return addressService;
    }

    private void setAccessToken(String accessToken, Long accessExpirationDate) {
        this.accessToken = accessToken;
        this.expirationDate = accessExpirationDate;
    }

    /**
     * 발급받은 Access Token 만료시간과 현재 시간을 비교한다.
     * 만료되었을 경우, Access Token 을 재발급 받기 위하여 헤더를 변경한다.
     */
    private void setAccessTokenHeader() {
        if (accessToken.length() == 0 || expirationDate == 0L)
            return;
        Long currentDate = new Date().getTime();
        if (currentDate - expirationDate >= 0)
            setReIssuedMoaService(true);
    }

    private void setReIssuedMoaService(boolean isReIssue) {
        /**
         * 해당부분 필요시.. 조정하도록 한다.
         * default connectTimeout 10 SECONDS
         * default readTimeout 10 SECONDS
         * default writeTimeout 10 SECONDS
         */
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(20, TimeUnit.SECONDS)
//                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", isReIssue ? "Basic " + accessToken : "Bearer " + accessToken)
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.REST_API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        moaService = retrofit.create(MoaService.class);
    }

    private void updateAccessToken(String accessToken, String accessExpirationDate) {
        setAccessToken(accessToken, Long.parseLong(accessExpirationDate, 10));
        setReIssuedMoaService(false);
    }

    private final static class Singleton {
        private final static RetrofitClient instance = new RetrofitClient();
    }
}
