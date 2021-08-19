package com.erenmeric.yuruyus_arkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText nameText, usernameText, ageText, cityText, passwordText, emailText;


    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button;
    int gender; // 0 -> erkek, 1 -> kız

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference mRootRef;
    //ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        button = (Button) findViewById(R.id.button105);
        nameText = (EditText) findViewById(R.id.name);
        usernameText = (EditText) findViewById(R.id.surname);
        ageText = (EditText) findViewById(R.id.age);
        cityText = (EditText) findViewById(R.id.city);
        passwordText = (EditText) findViewById(R.id.passwordText);
        emailText = (EditText) findViewById(R.id.emailText);

        firebaseAuth = (FirebaseAuth) FirebaseAuth.getInstance();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRootRef = database.getReference();
        //pd = new ProgressDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });
    }

    public void signup(View view) {

        String name, username, city, mail, password;
        int age;

        try {
            name = nameText.getText().toString();
            username = usernameText.getText().toString();
            city = cityText.getText().toString();
            mail = emailText.getText().toString();
            password = passwordText.getText().toString();

            age = Integer.parseInt(ageText.getText().toString());

            int ID = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(ID);

            if (radioButton.getId() == R.id.radio_one){
                gender = 0;
            } else{
                gender = 1;
            }


        } catch (Exception e) {
            Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            return;
        }



        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //pd.dismiss();
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("name",name);
                            map.put("username", username);
                            map.put("email", mail);
                            map.put("password", password);
                            map.put("bio", "");
                            map.put("imageUrl", "default");
                            map.put("id", mAuth.getCurrentUser().getUid());

                            mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "Update your profile for better experience",
                                                        Toast.LENGTH_LONG).show();
                                                Intent toMain = new Intent(getApplicationContext(), MainMenu.class);
                                                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(toMain);
                                                finish();

                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    //pd.dismiss();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), task.getException().toString(),
                                    Toast.LENGTH_LONG).show();
                            //pd.dismiss();
                        }
                    }
                });


        /*
        User userToReg = new User(name, surname, city, mail, password ,age, gender);
        firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignupActivity.this, "Kullanıcı oluşturuldu", Toast.LENGTH_SHORT).show();

                //db.collection("users").add(userToReg);
                db.collection("users").document(firebaseAuth.getUid()).set(userToReg);

                Intent intent = new Intent(SignupActivity.this, MainMenu.class);
                startActivity(intent);
                //finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });*/



    }
}