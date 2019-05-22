package th.ac.kku.cis.lab.reviewbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class EditPage extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mData;
    EditText ed_name;
    ImageButton btn_st_ed1;
    ImageButton btn_st_ed2;
    EditText ed_phone;
    EditText ed_time;
    EditText ed_location;
    Button btn_ed_post;
    String postKey;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selected;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);
        postKey = getIntent().getExtras().getString("PostKey");

        ed_name = (EditText) findViewById(R.id.ed_name_store);
        btn_st_ed1 = (ImageButton) findViewById(R.id.ed_image_store);
        btn_st_ed2 = (ImageButton) findViewById(R.id.ed2_image_store);
        btn_st_ed2.setVisibility(View.INVISIBLE);
        btn_st_ed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Selecte Picture"),PICK_IMAGE_REQUEST);
            }
        });
        btn_st_ed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Selecte Picture"),PICK_IMAGE_REQUEST);
            }
        });
        ed_phone = (EditText) findViewById(R.id.ed_phone_store);
        ed_time = (EditText) findViewById(R.id.ed_time_store);
        ed_location = (EditText) findViewById(R.id.ed_location_store);
        btn_ed_post = (Button) findViewById(R.id.ed_btn_post);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mData = firebaseDatabase.getReference("Bar").child(postKey);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bar showData = dataSnapshot.getValue(Bar.class);
                ed_name.setText(showData.getName());
                ed_phone.setText(showData.getPhone());
                ed_time.setText(showData.getTime());
                ed_location.setText(showData.getLocation());
                Picasso.with(getApplicationContext()).load(showData.getImageUrl()).fit()
                        .into(btn_st_ed1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_ed_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage();
            }
        });
    }
    public void UploadImage(){
        if(selected != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(selected)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadURL = uri.toString();
                                    final String name =  ed_name.getText().toString();
                                    final String phone = ed_phone.getText().toString();
                                    final String time = ed_time.getText().toString();
                                    final String location = ed_location.getText().toString();

                                    mData.child("ImageUrl").setValue(downloadURL);
                                    mData.child("Name").setValue(name);
                                    mData.child("Phone").setValue(phone);
                                    mData.child("Time").setValue(time);
                                    mData.child("Location").setValue(location);
                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Upload Success",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Upload :"+(int)progress+" %");
                        }
                    });
        }
        else{
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();
            final String name =  ed_name.getText().toString();
            final String phone = ed_phone.getText().toString();
            final String time = ed_time.getText().toString();
            final String location = ed_location.getText().toString();

            mData.child("Name").setValue(name);
            mData.child("Phone").setValue(phone);
            mData.child("Time").setValue(time);
            mData.child("Location").setValue(location);

            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Upload Success",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null) {
            selected = data.getData();
            btn_st_ed1.setVisibility(View.INVISIBLE);
            btn_st_ed2.setVisibility(View.VISIBLE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selected);
                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                btn_st_ed2.setBackgroundDrawable(ob);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
