package com.example.littlebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.littlebooks.old.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";

    TextView nazov, podnadpis, alreadyExist;
    EditText email, password1, meno, password2, priezvisko;
    Button button;
    FirebaseAuth fAuth;
    String userID;
    FirebaseUser current_user;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nazov = findViewById(R.id.nazov);
        podnadpis = findViewById(R.id.podnadpis);
        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        meno = findViewById(R.id.meno);
        priezvisko = findViewById(R.id.priezvisko);
        button = findViewById(R.id.button);
        alreadyExist = findViewById(R.id.alreadyExist);

        fAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emaill = email.getText().toString().trim();
                String passwordd = password1.getText().toString().trim();
                String passworddd = password2.getText().toString().trim();
                final String menoo = meno.getText().toString().trim();
                final String priezviskoo = priezvisko.getText().toString().trim();

                if (TextUtils.isEmpty(emaill)){
                    email.setError("Vyplňte toto pole!");
                    return;
                }

                if (TextUtils.isEmpty(menoo)){
                    meno.setError("Vyplňte toto pole!");
                    return;
                }

                if (TextUtils.isEmpty(priezviskoo)){
                    priezvisko.setError("Vyplňte toto pole!");
                    return;
                }

                if (TextUtils.isEmpty(passwordd)){
                    password1.setError("Vyplňte toto pole!");
                    return;
                }

                if (passwordd.length() < 6){
                    password1.setError("Heslo musí obsahovať minimálne 6 znakov.");
                    return;
                }

                if (!passwordd.equals(passworddd)) {
                    password2.setError("Hesla sa nezhodujú.");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(emaill, passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            current_user = fAuth.getCurrentUser();
                            userID = fAuth.getCurrentUser().getUid();


                            String url = "http://165.227.134.175/user1.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    response -> {
                                        Log.d("RegR", response.toString());
                                        if (response.equals("ok")){

                                            Intent mainIntent = new Intent(Register.this, MainActivity.class);
                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainIntent);
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), MainActivityT.class));

                                        }
                                    },
                                    error -> {
                                        Log.d("RegE", error.toString());

                                    }
                            ){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("action", "newUser");
                                    params.put("uid", userID);
                                    params.put("meno", menoo);
                                    params.put("priezvisko", priezviskoo);
                                    params.put("email", emaill);
                                    return params;
                                }
                            };
                            requestQueue = Volley.newRequestQueue(Register.this);
                            requestQueue.add(stringRequest);

                            /*Intent mainIntent = new Intent(Register.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();*/

                            startActivity(new Intent(getApplicationContext(), MainActivityT.class));
                        }
                        else {
                            Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        alreadyExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}