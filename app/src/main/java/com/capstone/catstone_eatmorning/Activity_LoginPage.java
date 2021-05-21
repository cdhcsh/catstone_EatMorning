package com.capstone.catstone_eatmorning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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

import org.json.JSONObject;

import java.util.Arrays;


public class Activity_LoginPage extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private ImageButton btn_naver_login;
    private ImageButton btn_kakao_login;
    private ImageButton btn_facebook_login;
    private ImageButton btn_google_login;
    private EditText input_userID;
    private EditText input_password;
    private Button btn_login;
    private Button btn_register;
    private SessionCallback_Kakao sessionCallback = new SessionCallback_Kakao();
    private Session kakao_Session;
    public static Activity_LoginPage activity_login;
    private OAuthLogin naver_M0AuthLoginModule;
    private Context context;
    private CallbackManager facebook_CallbackManager;
    private LoginCallback_Facebook facebook_Callback;
    private GoogleSignInOptions googleSignInOptions;
    private FirebaseAuth google_mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        Activity_LoginPage.activity_login = this;
        context = this;
        btn_naver_login = (ImageButton) findViewById(R.id.btn_naver_login);
        btn_kakao_login = (ImageButton) findViewById(R.id.btn_kakao_login);
        btn_facebook_login = (ImageButton) findViewById(R.id.btn_facebook_login);
        btn_google_login = (ImageButton) findViewById(R.id.btn_google_login);
        input_userID = (EditText) findViewById(R.id.input_userID);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        initBtn_Login();
        initBtn_Naver_Login();
        initBtn_Kakao_Login();
        initBtn_Facebook_Login();
        initBtn_Google_Login();
        initBtn_Register();


    }
    private void initBtn_Login(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String userID = input_userID.getText().toString();
                String password = input_password.getText().toString();
                if(userID.length() < 1 || password.length() < 1){
                    Toast toast = Toast.makeText(Activity_LoginPage.this,"입력하지 않은 내용이 있습니다",Toast.LENGTH_SHORT);
                    Log.d("Toast",toast.toString());
                    toast.show();
                }
                else {
                    startActivity_Login_normal(userID, SHA256.encode(password));
                }
            }
        });
    }
    private void initBtn_Register(){
        btn_register.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_LoginPage.this,Activity_register.class);
            startActivity(intent);
        });
    }
    private void initBtn_Logout(){
        btn_register.setOnClickListener(v -> {
            //페이스북 로그아웃
            LoginManager.getInstance().logOut();
            //구글 로그아웃
            signOut();
            //네이버 로그아웃
            naver_M0AuthLoginModule = OAuthLogin.getInstance();
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
    private void initBtn_Google_Login(){
        //구글 로그인
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        google_mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        btn_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    private void initBtn_Kakao_Login(){
// 카카오 로그인
        kakao_Session = Session.getCurrentSession();
        kakao_Session.addCallback(sessionCallback);
        btn_kakao_login.setOnClickListener(v -> {
            long id = 0;
            if (Session.getCurrentSession().checkAndImplicitOpen()) {
                Log.d(TAG, "onClick: 로그인 세션살아있음");
                // 카카오 로그인 시도 (창이 안뜬다.)
//                sessionCallback.requestMe();
            }
            else{
                Log.d(TAG, "onClick: 로그인 세션끝남");
                // 카카오 로그인 시도 (창이 뜬다.)
                kakao_Session.open(AuthType.KAKAO_LOGIN_ALL, Activity_LoginPage.this);
                sessionCallback.requestMe();
            }
        });
    }
    private void initBtn_Naver_Login(){
        //네이버 로그인
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
                            startActivity_Login_social(Member.NAVER,String.valueOf(expiresAt));

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
    }
    private void initBtn_Facebook_Login(){
        //페이스북 로그인
        facebook_CallbackManager = CallbackManager.Factory.create();
        facebook_Callback = new LoginCallback_Facebook();
        btn_facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager loginManager = LoginManager.getInstance();
                loginManager.logInWithReadPermissions(Activity_LoginPage.this, Arrays.asList("public_profile","email"));
                loginManager.registerCallback(facebook_CallbackManager, facebook_Callback);


            }
        });
    }
    private void startActivity_Login_normal(String userID,String password){
        Intent intent = new Intent(getApplicationContext(),Activity_Login.class);
        intent.putExtra(Member.CONNECTED_SOCIAL_TYPE,Member.NONE);
        intent.putExtra(Member.USERID,userID);
        intent.putExtra(Member.PASSWORD,SHA256.encode(password));
        startActivity(intent);
    }
    private void startActivity_Login_social(String connected_social_type,String connected_social_ID){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra(Member.CONNECTED_SOCIAL_TYPE,connected_social_type);
        intent.putExtra(Member.CONNECTED_SOCIAL_ID,SHA256.encode(connected_social_ID));
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //페이스북
        if(facebook_CallbackManager.onActivityResult(requestCode,resultCode,data)){
        }
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        // 구글
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(getApplicationContext(), "Google sign in Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
// 구글 로그인 모듈
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" +acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        google_mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = google_mAuth.getCurrentUser();
                            //updateUI(user);
                            //성공시 실행
                            startActivity_Login_social(Member.GOOGLE,user.getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_LONG).show();

                            // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }
    private void signOut(){
        google_mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
    private void revokeAccess() {
        // Firebase sign out
        google_mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
                    }
                });
    }
    //카카오 세션롤백
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
                            startActivity_Login_social(Member.KAKAO,id);
                        }
                    });

        }
    }
    //페이스북 로그인콜백
    class LoginCallback_Facebook implements FacebookCallback<LoginResult> {

        // 로그인 성공 시 호출 됩니다. Access Token 발급 성공.
        @Override
        public void onSuccess(LoginResult loginResult) {
            String id = loginResult.getAccessToken().getUserId();
            startActivity_Login_social(Member.FACEBOOK,id);
        }

        // 로그인 창을 닫을 경우, 호출됩니다.
        @Override
        public void onCancel() {
            Log.e("Callback :: ", "onCancel");
        }

        // 로그인 실패 시에 호출됩니다.
        @Override
        public void onError(FacebookException error) {
            Log.e("Callback :: ", "onError : " + error.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe(AccessToken token) {
            GraphRequest graphRequest = GraphRequest.newMeRequest(token,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.e("결과는 뭔가요??",object.toString());
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();
        }
    }
}