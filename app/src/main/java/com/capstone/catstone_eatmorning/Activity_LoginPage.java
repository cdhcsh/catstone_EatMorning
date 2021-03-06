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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
                    Toast toast = Toast.makeText(Activity_LoginPage.this,"???????????? ?????? ????????? ????????????",Toast.LENGTH_SHORT);
                    Log.d("Toast",toast.toString());
                    toast.show();
                }
                else {
                    Log.d("TEST","=====================================================" + password);
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
    private void socialSignIn(String connected_social_type,String connected_social_ID){
        startActivity_Login_social(connected_social_type,connected_social_ID);
    }
    private void socialLogout(){
        //???????????? ????????????
        LoginManager.getInstance().logOut();
        //?????? ????????????
        signOut();
        //????????? ????????????
        naver_M0AuthLoginModule = OAuthLogin.getInstance();
        naver_M0AuthLoginModule.logout(context);
        //????????? ????????????
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
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
    }
    private void initBtn_Google_Login(){
        //?????? ?????????
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
// ????????? ?????????
        kakao_Session = Session.getCurrentSession();
        kakao_Session.addCallback(sessionCallback);
        btn_kakao_login.setOnClickListener(v -> {
            long id = 0;
            if (Session.getCurrentSession().checkAndImplicitOpen()) {
                Log.d(TAG, "onClick: ????????? ??????????????????");
                // ????????? ????????? ?????? (?????? ?????????.)
//                sessionCallback.requestMe();
            }
            else{
                Log.d(TAG, "onClick: ????????? ????????????");
                // ????????? ????????? ?????? (?????? ??????.)
                kakao_Session.open(AuthType.KAKAO_LOGIN_ALL, Activity_LoginPage.this);
                sessionCallback.requestMe();
            }
        });
    }
    private void initBtn_Naver_Login(){
        //????????? ?????????
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
                            socialSignIn(Member.NAVER,String.valueOf(expiresAt));

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
        //???????????? ?????????
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
        intent.putExtra(Member.ID,userID);
        intent.putExtra(Member.PASSWORD,password);
        startActivity(intent);
        finish();
    }
    private void startActivity_Login_social(String connected_social_type,String connected_social_ID){
        Intent intent = new Intent(getApplicationContext(),Activity_Main.class);
        intent.putExtra(Member.CONNECTED_SOCIAL_TYPE,connected_social_type);
        intent.putExtra(Member.CONNECTED_SOCIAL_ID,SHA256.encode(connected_social_ID));
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // ?????? ?????? ??????
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //????????????
        if(facebook_CallbackManager.onActivityResult(requestCode,resultCode,data)){
        }
        // ????????????|????????? ??????????????? ?????? ????????? ????????? SDK??? ??????
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        // ??????
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
// ?????? ????????? ??????
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
                            //????????? ??????
                            socialSignIn(Member.GOOGLE,user.getUid());

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
    //????????? ????????????
    public class SessionCallback_Kakao implements ISessionCallback {
        // ???????????? ????????? ??????
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // ???????????? ????????? ??????
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // ????????? ?????? ??????
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "????????? ?????? ??????: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "????????? ?????? ?????? ??????: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            String id = String.valueOf(result.getId());
                            Log.i("KAKAO_API", "????????? ???????????? : " + result.getId());
                            socialSignIn(Member.KAKAO,id);
                        }
                    });

        }
    }
    //???????????? ???????????????
    class LoginCallback_Facebook implements FacebookCallback<LoginResult> {

        // ????????? ?????? ??? ?????? ?????????. Access Token ?????? ??????.
        @Override
        public void onSuccess(LoginResult loginResult) {
            String id = loginResult.getAccessToken().getUserId();
            socialSignIn(Member.FACEBOOK,id);
        }

        // ????????? ?????? ?????? ??????, ???????????????.
        @Override
        public void onCancel() {
            Log.e("Callback :: ", "onCancel");
        }

        // ????????? ?????? ?????? ???????????????.
        @Override
        public void onError(FacebookException error) {
            Log.e("Callback :: ", "onError : " + error.getMessage());
        }

        // ????????? ?????? ??????
        public void requestMe(AccessToken token) {
            GraphRequest graphRequest = GraphRequest.newMeRequest(token,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.e("????????? ???????????",object.toString());
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();
        }
    }
}