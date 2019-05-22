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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class PostPage extends AppCompatActivity {
    EditText post_name;
    ImageButton post_image;
    EditText post_phone;
    EditText post_time;
    EditText post_location;
    Button btn_save;
    DatabaseReference mData;
    FirebaseDatabase firebaseDatabase;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selected;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);
        post_name = (EditText) findViewById(R.id.post_name_store);
        post_image = (ImageButton) findViewById(R.id.post_image_store);
        post_phone = (EditText) findViewById(R.id.post_phone_store);
        post_time = (EditText) findViewById(R.id.post_time_store);
        post_location = (EditText) findViewById(R.id.post_location_store);
        btn_save = (Button) findViewById(R.id.save_btn_post);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mData = firebaseDatabase.getReference("Bar").push();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Selecte Picture"),PICK_IMAGE_REQUEST);
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
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
            final StorageReference ref = storageReference.child
                    ("images/"+ UUID.randomUUID().toString());
            ref.putFile(selected)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           ref.getDownloadUrl().addOnSuccessListener(
                                   new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                 final String downloadURL = uri.toString();
                                 final String name =  post_name.getText().toString();
                                 final String phone = post_phone.getText().toString();
                                 final String time = post_time.getText().toString();
                                 final String location = post_location.getText().toString();

                                  mData.child("ImageUrl").setValue(downloadURL);
                                  mData.child("Name").setValue(name);
                                  mData.child("Phone").setValue(phone);
                                  mData.child("Time").setValue(time);
                                  mData.child("Location").setValue(location);
                               }
                           });
                           progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Upload Success",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),
                                    Main2Activity.class);
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null){
            selected = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selected);
                BitmapDrawable ob = new BitmapDrawable(getResources(),bitmap);
                post_image.setBackgroundDrawable(ob);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
