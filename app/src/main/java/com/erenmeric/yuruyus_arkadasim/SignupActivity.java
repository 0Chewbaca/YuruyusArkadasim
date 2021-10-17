package com.erenmeric.yuruyus_arkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText nameText, usernameText, ageText, cityText, passwordText, emailText;
    Spinner cities;
    ArrayList<String> cityList;
    ArrayAdapter<String> cityAdapter;

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
        //cityText = (EditText) findViewById(R.id.city);
        passwordText = (EditText) findViewById(R.id.passwordText);
        emailText = (EditText) findViewById(R.id.emailText);
        cities = (Spinner) findViewById(R.id.cities);
        firebaseAuth = (FirebaseAuth) FirebaseAuth.getInstance();

        //radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRootRef = database.getReference();
        //pd = new ProgressDialog(this);


        cityList = new ArrayList<String>();
        addCities();
        cityAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item
                , cityList);

        cities.setAdapter(cityAdapter);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup(v);
            }
        });
    }

    private void addCities(){
        cityList.add("Adana");
        cityList.add("Adıyaman");
        cityList.add("Afyon");
        cityList.add("Ağrı");
        cityList.add("Amasya");
        cityList.add("Ankara");
        cityList.add("Antalya");
        cityList.add("Artvin");
        cityList.add("Aydın");
        cityList.add("Balıkesir");
        cityList.add("Bilecik");
        cityList.add("Bingöl");
        cityList.add("Bitlis");
        cityList.add("Bolu");
        cityList.add("Burdur");
        cityList.add("Bursa");
        cityList.add("Çanakkale");
        cityList.add("Çankırı");
        cityList.add("Çorum");
        cityList.add("Denizli");
        cityList.add("Diyarbakır");
        cityList.add("Edirne");
        cityList.add("Elazığ");
        cityList.add("Erzincan");
        cityList.add("Erzurum");
        cityList.add("Eskişehir");
        cityList.add("Gaziantep");
        cityList.add("Giresun");
        cityList.add("Gümüşhane");
        cityList.add("Hakkari");
        cityList.add("Hatay");
        cityList.add("Isparta");
        cityList.add("İçel (Mersin)");
        cityList.add("İstanbul");
        cityList.add("İzmir");
        cityList.add("Kars");
        cityList.add("Kastamonu");
        cityList.add("Kayseri");
        cityList.add("Kırklareli");
        cityList.add("Kırşehir");
        cityList.add("Kocaeli");
        cityList.add("Konya");
        cityList.add("Kütahya");
        cityList.add("Malatya");
        cityList.add("Manisa");
        cityList.add("Kahramanmaraş");
        cityList.add("Mardin");
        cityList.add("Muğla");
        cityList.add("Muş");
        cityList.add("Nevşehir");
        cityList.add("Niğde");
        cityList.add("Ordu");
        cityList.add("Rize");
        cityList.add("Sakarya");
        cityList.add("Samsun");
        cityList.add("Siirt");
        cityList.add("Sinop");
        cityList.add("Sivas");
        cityList.add("Tekirdağ");
        cityList.add("Tokat");
        cityList.add("Trabzon");
        cityList.add("Tunceli");
        cityList.add("Şanlıurfa");
        cityList.add("Uşak");
        cityList.add("Van");
        cityList.add("Yozgat");
        cityList.add("Zonguldak");
        cityList.add("Aksaray");
        cityList.add("Bayburt");
        cityList.add("Karaman");
        cityList.add("Kırıkkale");
        cityList.add("Batman");
        cityList.add("Şırnak");
        cityList.add("Bartın");
        cityList.add("Ardahan");
        cityList.add("Iğdır");
        cityList.add("Yalova");
        cityList.add("Karabük");
        cityList.add("Kilis");
        cityList.add("Osmaniye");
        cityList.add("Düzce");
    }

    public void signup(View view) {

        String name, username, city, mail, password;
        int age;


        try {
            name = nameText.getText().toString();
            username = usernameText.getText().toString();
            city = cities.getSelectedItem().toString();
            mail = emailText.getText().toString();
            password = passwordText.getText().toString();

            age = Integer.parseInt(ageText.getText().toString());

            //int ID = radioGroup.getCheckedRadioButtonId();
            //radioButton = findViewById(ID);

            /*
            if (radioButton.getId() == R.id.radio_one){
                gender = 0;
            } else{
                gender = 1;
            }*/


        } catch (Exception e) {
            Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            Log.d("eren4477", e.getLocalizedMessage().toString());
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
                            map.put("city", city);
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