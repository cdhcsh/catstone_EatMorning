package com.capstone.catstone_eatmorning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;


public class Activity_LoginPage extends AppCompatActivity {
    private ImageButton btn_naver_login;
    private ImageButton btn_kakao_login;
    private ImageButton btn_facebook_login;
    private ImageButton btn_google_login;
    private EditText input_userID;
    private EditText input_password;
    private Button btn_login;
    private Button btn_register;
    private SessionCallback_Kakao sessionCallback = new SessionCallback_Kakao();
    Session kakao_Session;
    private static final String TAG = "LoginActivity";
    public static Activity_LoginPage activity_login;
    OAuthLogin naver_M0AuthLoginModule;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        Activity_LoginPage.activity_login = this;
        context = getApplicationContext();
        btn_naver_login = (ImageButton) findViewById(R.id.btn_naver_login);
        btn_kakao_login = (ImageButton) findViewById(R.id.btn_kakao_login);
        btn_facebook_login = (ImageButton) findViewById(R.id.btn_facebook_login);
        btn_google_login = (ImageButton) findViewById(R.id.btn_google_login);
        input_userID = (EditText) findViewById(R.id.input_userID);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_naver_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naver_M0AuthLoginModule = OAuthLogin.getInstance();
                naver_M0AuthLoginModule.init(
                        context
                        ,getString(R.string.naver_client_id)
                        ,getString(R.string.naver_client_secret)
                        ,getString(R.string.naver_client_name)
                );
                @SuppressLint("HandlerLeak")
                OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
                    @Override
                    public void run(boolean success) {
                        if (success) {
                            String accessToken = naver_M0AuthLoginModule.getAccessToken(context);
                            String refreshToken = naver_M0AuthLoginModule.getRefreshToken(context);
                            long expiresAt = naver_M0AuthLoginModule.getExpiresAt(context);
                            String tokenType = naver_M0AuthLoginModule.getTokenType(context);
                            Log.i("LoginData","accessToken : "+ accessToken);
                            Log.i("LoginData","refreshToken : "+ refreshToken);
                            Log.i("LoginData","expiresAt : "+ expiresAt);
                            Log.i("LoginData","tokenType : "+ tokenType);
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra(Member.CONNECTED_SOCIAL_TYPE,Member.NAVER);
                            intent.putExtra(Member.CONNECTED_SOCIAL_ID,SHA256.encode(String.valueOf(expiresAt)));
                            startActivity(intent);

                        } else {
                            String errorCode = naver_M0AuthLoginModule
                                    .getLastErrorCode(context).getCode();
                            String errorDesc = naver_M0AuthLoginModule.getLastErrorDesc(context);
                        }
                    };
                };

                naver_M0AuthLoginModule.startOauthLoginActivity(Activity_LoginPage.this, mOAuthLoginHandler);
            }
        });
// 카카오 로그인
        kakao_Session = Session.getCurrentSession();
        kakao_Session.addCallback(sessionCallback);
        btn_kakao_login.setOnClickListener(v -> {
            long id = 0;
            if (Session.getCurrentSession().checkAndImplicitOpen()) {
                Log.d(TAG, "onClick: 로그인 세션살아있음");
                // 카카오 로그인 시도 (창이 안뜬다.)
                //sessionCallback.requestMe();
            }
            else{
                Log.d(TAG, "onClick: 로그인 세션끝남");
                // 카카오 로그인 시도 (창이 뜬다.)
                kakao_Session.open(AuthType.KAKAO_LOGIN_ALL, Activity_LoginPage.this);
                sessionCallback.requestMe();
            }
        });
        btn_register.setOnClickListener(v -> {
            //네이버 로그아웃
            naver_M0AuthLoginModule.logout(context);
            //카카오 로그아웃
            UserManagement.getInstance()
                    .requestLogout(new LogoutResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            super.onSessionClosed(errorResult);
                            Log.d(TAG, "onSessionClosed: "+errorResult.getErrorMessage());

                        }
                        @Override
                        public void onCompleteLogout() {
                            if (sessionCallback != null) {
                                Session.getCurrentSession().removeCallback(sessionCallback);
                            }
                            Log.d(TAG, "onCompleteLogout:logout ");
                        }
                    });
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public class SessionCallback_Kakao implements ISessionCallback {
        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            String id = String.valueOf(result.getId());
                            Log.i("KAKAO_API", "사용자 아이디는 : " + result.getId());
                            UserAccount kakaoAccount = result.getKakaoAccount();
                            if (kakaoAccount != null) {

                                // 이메일
                                String email = kakaoAccount.getEmail();
                                Profile profile = kakaoAccount.getProfile();
                                if (profile ==null){
                                    Log.d("KAKAO_API", "onSuccess:profile null ");
                                }else{
                                    Log.d("KAKAO_API", "onSuccess:getProfileImageUrl "+profile.getProfileImageUrl());
                                    Log.d("KAKAO_API", "onSuccess:getNickname "+profile.getNickname());
                                }
                                if (email != null) {

                                    Log.d("KAKAO_API", "onSuccess:email "+email);
                                }

                                // 프로필
                                Profile _profile = kakaoAccount.getProfile();

                                if (_profile != null) {

                                    Log.d("KAKAO_API", "nickname: " + _profile.getNickname());
                                    Log.d("KAKAO_API", "profile image: " + _profile.getProfileImageUrl());
                                    Log.d("KAKAO_API", "thumbnail image: " + _profile.getThumbnailImageUrl());

                                } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                    // 동의 요청 후 프로필 정보 획득 가능

                                } else {
                                    // 프로필 획득 불가
                                }
                            }else{
                                Log.i("KAKAO_API", "onSuccess: kakaoAccount null");
                            }
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra(Member.CONNECTED_SOCIAL_TYPE,Member.KAKAO);
                            intent.putExtra(Member.CONNECTED_SOCIAL_ID,SHA256.encode(id));
                            startActivity(intent);
                        }
                    });

        }

    }
}