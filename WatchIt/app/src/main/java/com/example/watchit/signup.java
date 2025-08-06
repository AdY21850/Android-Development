package com.example.watchit;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.CredentialManager;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManagerCallback;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CredentialManager credentialManager;

    private EditText nameET, emailET, passwordET;
    private FrameLayout signupBtn, googleSignupBtn;
    private TextView signinTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        credentialManager = CredentialManager.create(this);

        // Set status bar color
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.main_color, getTheme()));

        // Initialize views
        nameET = findViewById(R.id.name1);
        emailET = findViewById(R.id.email1);
        passwordET = findViewById(R.id.password1);
        signupBtn = findViewById(R.id.signupbtn);
        googleSignupBtn = findViewById(R.id.OAuth);


        signupBtn.setOnClickListener(v -> registerWithEmail());
        googleSignupBtn.setOnClickListener(v -> launchGoogleSignIn());
        signinTxt.setOnClickListener(v -> {
            startActivity(new Intent(signup.this, login.class));
            finish();
        });
    }

    private void registerWithEmail() {
        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        // Update display name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        user.updateProfile(profileUpdates);

                        // Save user details in Firestore
                        saveUserToFirestore(user.getUid(), name, email);
                        updateUI(user);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Custom Sign-Up failed: " + e.getMessage());
                    Toast.makeText(this, "Sign up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void launchGoogleSignIn() {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                this,
                request,
                null,
                new CredentialManagerCallback<GetCredentialResponse, Void>() {
                    @Override
                    public void onError(@NonNull Void unused) {
                        Toast.makeText(signup.this, "failed due to call back error :(", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResult(GetCredentialResponse response) {
                        Credential credential = response.getCredential();
                        if (credential instanceof CustomCredential) {
                            if (TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(((CustomCredential) credential).getType())) {
                                try {
                                    GoogleIdTokenCredential googleCredential = GoogleIdTokenCredential.createFrom(((CustomCredential) credential).getData());
                                    String idToken = googleCredential.getIdToken();
                                    firebaseAuthWithGoogle(idToken);
                                } catch (Exception e) {
                                    Log.e(TAG, "Google credential parsing error", e);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Exception e) {
                        Log.e(TAG, "Google Sign-In error", e);
                        Toast.makeText(signup.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        saveUserToFirestore(user.getUid(), user.getDisplayName(), user.getEmail());
                        updateUI(user);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Google Sign-In failed: ", e);
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveUserToFirestore(String uid, String name, String email) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("uid", uid);
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("createdAt", System.currentTimeMillis());

        db.collection("users").document(uid).set(userMap)
                .addOnSuccessListener(unused -> Log.d(TAG, "User added to Firestore"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to save user: " + e.getMessage()));
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(signup.this, dashboard.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
