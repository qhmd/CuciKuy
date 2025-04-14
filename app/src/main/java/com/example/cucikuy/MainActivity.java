package com.example.cucikuy;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity {
    LinearLayout sign_btn;
    private ProgressBar spinner;
    private View darkOverlay;
    TextView googleText;

    private static final int REQ_ONE_TAP = 1;  // Can be any integer unique to the Activity.
    ConstraintLayout blurTarget;
    BeginSignInRequest begin_sign_in_request;
    private SignInClient one_tap_client;

    private FirebaseAuth auth;
    // Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.googleProgress);
        darkOverlay = findViewById(R.id.darkOverlay);
        googleText = findViewById(R.id.googlelogintext);
        blurTarget = findViewById(R.id.constraintUtama);

        auth = FirebaseAuth.getInstance();
        sign_btn = findViewById(R.id.ButtonGoogle);
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleText.setText("Loading...");
                darkOverlay.setVisibility(View.VISIBLE);
                Blurry.with(MainActivity.this)
                        .radius(5)         // tingkat blur (0–25)
                        .sampling(2)        // pengambilan sampel (semakin besar, semakin ringan proses)
                        .onto(blurTarget);  // layout yang akan diblur
                sign_btn.setEnabled(false); // opsional: disable agar tidak ditekan berkali-kali
                spinner.setVisibility(View.VISIBLE);
                signup();
            }
        });

    }

    private void signup() {
        // trigger One Tap Sign-In
        one_tap_client = Identity.getSignInClient(this);

        // configures the BeginSignInRequest object, then calls setGoogleIdTokenRequestOptions
        begin_sign_in_request = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        if (one_tap_client != null) {
            one_tap_client.beginSignIn(begin_sign_in_request)
                    .addOnSuccessListener(MainActivity.this, result -> {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(),
                                    REQ_ONE_TAP,
                                    null,  // fill in null or appropriate extras
                                    0, 0, 0
                            );
                        } catch (Exception e) {
                            Log.e(TAG, "Error starting One Tap Sign-In: ", e);
                        }
                    })
                    .addOnFailureListener(MainActivity.this, e -> {
                        Log.e(TAG, "One Tap Sign-In failed: ", e);
                        Toast.makeText(MainActivity.this, "Sign-In Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.GONE);
                        googleText.setText("Sign in with Google");
                        sign_btn.setEnabled(true);
                        darkOverlay.setVisibility(View.GONE);
                        Blurry.delete(blurTarget);
                    });
        }
    }

    private void firebaseAuthWithGoogle(@Nullable Intent data) {
        try {
            SignInCredential googleCredential = one_tap_client.getSignInCredentialFromIntent(data);
            String idToken = googleCredential.getGoogleIdToken();
            if (idToken != null) {
                // got an ID token from Google. Use it to authenticate
                // with Firebase.
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    FirebaseUser user = auth.getCurrentUser();

                                    // if sign up or sign in successful, update UI to home activity
                                    updateUI(user);
                                } else {
                                    // if sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                                }
                            }
                        });
            }
            Log.d(TAG, "Got ID token.");
        } catch (ApiException e) {
            Log.e(TAG, "Google Sign-In failed", e);
            spinner.setVisibility(View.GONE);
            googleText.setText("Sign in with Google");
            sign_btn.setEnabled(true);
            darkOverlay.setVisibility(View.GONE);
            Blurry.delete(blurTarget);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ONE_TAP) {
            firebaseAuthWithGoogle(data);
        }
    }

    private void updateUI(FirebaseUser user) {
        spinner.setVisibility((View.GONE));
        googleText.setText("Sign in with Google");
        sign_btn.setEnabled(true);
        darkOverlay.setVisibility(View.GONE);
        Blurry.delete(blurTarget);
//        if (blurTarget != null) {
//            Blurry.delete(blurTarget);
//        }
        if (user != null){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }
}
