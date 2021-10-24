package com.erenmeric.yuruyus_arkadasim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.erenmeric.yuruyus_arkadasim.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView close;
    private CircleImageView imageProfile;
    private TextView save;
    private TextView changePhoto;
    private MaterialEditText fullName;
    private MaterialEditText username;
    private MaterialEditText bio;
    String city;
    private ArrayList<String> cityList;
    private ArrayAdapter<String> cityAdapter;
    String currentCity = "";
    private FirebaseUser fUser;
    Spinner changeCity;
    private Uri mImageUri;

    private StorageTask uploadTask;
    private StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        changeCity = findViewById(R.id.changeCity);
        close = findViewById(R.id.close);
        imageProfile = findViewById(R.id.image_profile);
        save = findViewById(R.id.save);
        changePhoto = findViewById(R.id.change_photo);
        fullName = findViewById(R.id.full_name);
        username = findViewById(R.id.username);
        bio = findViewById(R.id.bio);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("Uploads");


        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                fullName.setText(user.getName());
                username.setText(user.getUsername());
                bio.setText(user.getBio());
                Picasso.get().load(user.getImageUrl()).into(imageProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);

            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        cityList = new ArrayList<String>();

        addCities();

        cityAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item
                , cityList);
        changeCity.setAdapter(cityAdapter);


        FirebaseDatabase.getInstance().getReference().child("Users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ).child("city").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                city = snapshot.getValue().toString();
                cityList.add(0,city);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    /*private void addTheCurrentCity() {

    }*/

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

    private void updateProfile() {
        city = changeCity.getSelectedItem().toString();

        HashMap<String, Object> map = new HashMap<>();
        map.put("fullname", fullName.getText().toString());
        //map.put("name", fullName.getText().toString());
        map.put("username", username.getText().toString());
        map.put("bio", bio.getText().toString());
        map.put("city", city);

        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).updateChildren(map);
        Toast.makeText(getApplicationContext(), "Profile Updated Successfully",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            uploadImage();
        } else {
            Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage() {

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");
        pd.show();

        if(mImageUri != null){
            StorageReference fileRef = storageReference.child(System.currentTimeMillis() + ".jpeg");
            uploadTask = fileRef.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = (Uri)task.getResult();
                        String url = downloadUri.toString();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid())
                                .child("imageUrl").setValue(url);
                        pd.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_LONG).show();
        }

    }


}