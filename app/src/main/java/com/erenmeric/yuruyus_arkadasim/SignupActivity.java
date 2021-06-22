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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    EditText nameText, surnameText, ageText, cityText, passwordText, emailText;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    RadioGroup genderGroup;
    RadioButton maleButton, femaleButton;
    Button button;
    FirebaseUser user;

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
        user = (FirebaseUser) FirebaseAuth.getInstance().getCurrentUser();
        genderGroup = (RadioGroup) findViewById(R.id.radioGroup);
        maleButton = (RadioButton) findViewById(R.id.maleRadioButton);
        femaleButton = (RadioButton) findViewById(R.id.femaleRadioButton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });
    }

    public void signup(View view) {

        Toast.makeText(getApplicationContext(), "Egenin pushu", Toast.LENGTH_SHORT).show();

        String name, surname, city, mail, password;
        int age, gender;

        Log.d("signup_button_clicked", "basıldı");


        name = nameText.getText().toString();
        surname = surnameText.getText().toString();
        city = cityText.getText().toString();
        mail = emailText.getText().toString();
        password = passwordText.getText().toString();

        if(name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "isim alanı boş bırakılamaz",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if(surname.isEmpty()) {
            Toast.makeText(getApplicationContext(), "soyisim alanı boş bırakılamaz",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if(city.isEmpty()) {
            Toast.makeText(getApplicationContext(), "şehir alanı boş bırakılamaz",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if(mail.isEmpty()) {
            Toast.makeText(getApplicationContext(), "mail alanı boş bırakılamaz",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if(password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "şifre alanı boş bırakılamaz",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(ageText.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "yaş alanı boş bırakılamaz",
                    Toast.LENGTH_LONG).show();
            return;
        }
        age = Integer.parseInt(ageText.getText().toString());


        int checkedGender = genderGroup.getCheckedRadioButtonId();
        if(checkedGender == -1){
            Toast.makeText(getApplicationContext(), "cinsiyet alanı boş bırakılamaz",
                    Toast.LENGTH_LONG).show();
            return;
        } else if( checkedGender == maleButton.getId() ){
            gender = 1;
        } else {
            gender = 0;
        }

        Log.d("signup_button_clicked", "try-catch bitti");



        firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                String uid;
                if (user != null) {
                    uid = user.getUid().toString();
                    User userToReg = new User(name, surname, city, mail,  password, age, gender, uid);
                    db.collection("users").document(uid).set(userToReg);
                    Toast.makeText(getApplicationContext(), userToReg.toString(), Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(SignupActivity.this, MainMenu.class);
                startActivity(intent);
                finish();
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