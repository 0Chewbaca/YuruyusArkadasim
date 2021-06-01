package com.erenmeric.yuruyus_arkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    EditText nameText, surnameText, ageText, cityText, passwordText, emailText;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button button;
    int gender; // 0 -> erkek, 1 -> kız

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        button = (Button) findViewById(R.id.button105);
        nameText = (EditText) findViewById(R.id.name);
        surnameText = (EditText) findViewById(R.id.surname);
        ageText = (EditText) findViewById(R.id.age);
        cityText = (EditText) findViewById(R.id.city);
        passwordText = (EditText) findViewById(R.id.passwordText);
        emailText = (EditText) findViewById(R.id.emailText);
        firebaseAuth = (FirebaseAuth) FirebaseAuth.getInstance();
        db = (FirebaseFirestore) FirebaseFirestore.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });
    }

    public void signup(View view) {

        String name, surname, city, mail, password;
        int age;

        Toast.makeText(getApplicationContext(), "Çalışıyor", Toast.LENGTH_SHORT).show();
        Log.d("signup_button_clicked", "basıldı");
        gender = 0;

        try {
            name = nameText.getText().toString();
            surname = surnameText.getText().toString();
            city = cityText.getText().toString();
            mail = emailText.getText().toString();
            password = passwordText.getText().toString();

            //Log.d("signup_button_clicked", "password un altı");

            age = Integer.parseInt(ageText.getText().toString());

            //Radio Button
            /*
            int ID = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(ID);



            if (radioButton.getText().toString() == "Erkek"){
                gender = 0;
            } else{
                gender = 1;
            }
            */

            //Log.d("signup_button_clicked", "try ın sonu");
        } catch (Exception e) {
            Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            Log.d("signup_button_clicked", "catch!!!");
            Log.d("signup_button_clicked", e.getLocalizedMessage());
            return;
        }

        Log.d("signup_button_clicked", "try-catch bitti");
        User userToReg = new User(name, surname, city, mail, password ,age, gender);
        db.collection("users").add(userToReg);


        firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignupActivity.this, "Kullanıcı oluşturuldu", Toast.LENGTH_SHORT).show();
                //db.collection("users").add(userToReg);

                Log.d("signup_button_clicked", "Kullanıcı oluşuruldu");

                Intent intent = new Intent(SignupActivity.this, MainMenu.class);
                startActivity(intent);


                //finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                Log.d("signup_button_clicked", "hata çıktı");

            }
        });


    }
}