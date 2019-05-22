package th.ac.kku.cis.lab.reviewbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

public class DetailPage extends AppCompatActivity {
    String postkey;
    ImageButton image_back;
    ImageButton not_img;
    ImageView cover_image;
    TextView name_bar;
    TextView text_phone;
    TextView text_store;
    TextView text_add;
    RecyclerView recyclerView;
    String NameBar;
    DatabaseReference mData;
    FirebaseDatabase firebaseDatabase;
    Query mCom;
    DatabaseReference mNote;
    FirebaseAuth mAuth;
    FirebaseUser user;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);
        postkey = getIntent().getExtras().getString("PostKey");
        NameBar =getIntent().getExtras().getString("NameBar");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mData = firebaseDatabase.getReference("Bar").child(postkey);
        mNote = firebaseDatabase.getReference("Note").child(postkey);
        image_back = (ImageButton) findViewById(R.id.btn_back_page);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPage.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
        not_img = (ImageButton) findViewById(R.id.image_note);
        not_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Bar showData = dataSnapshot.getValue(Bar.class);
                        mNote.child("ImageUrl").setValue(showData.getImageUrl());
                        mNote.child("Name").setValue(showData.getName());
                        mNote.child("Phone").setValue(showData.getPhone());
                        mNote.child("Time").setValue(showData.getTime());
                        mNote.child("Location").setValue(showData.getLocation());
                        Toast.makeText(DetailPage.this,"Add to Note",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        cover_image = (ImageView) findViewById(R.id.image_background);
        name_bar = (TextView) findViewById(R.id.Name_bar);
        text_phone = (TextView) findViewById(R.id.text_phone_call);
        text_store = (TextView) findViewById(R.id.text_store_open);
        text_add = (TextView) findViewById(R.id.text_address);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_comment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bar showData = dataSnapshot.getValue(Bar.class);
                name_bar.setText(showData.getName());
                text_phone.setText(showData.getPhone());
                text_store.setText(showData.getTime());
                text_add.setText(showData.getLocation());
                Picasso.with(getApplicationContext()).load(showData.getImageUrl()).fit()
                        .into(cover_image);
              /*  String name_Bar = name_bar.getText().toString();
                Toast.makeText(DetailPage.this,name_Bar,Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mCom = firebaseDatabase.getReference("Comment").orderByChild("Name_Store").equalTo(NameBar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Comment> options = new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(mCom,Comment.class)
                .build();

        FirebaseRecyclerAdapter<Comment,Card_View> adapter = new FirebaseRecyclerAdapter<Comment, Card_View>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Card_View holder, int position, @NonNull Comment model) {
                final String postkey = getRef(position).getKey();
                holder.setDetail(model.getMessage_Person());
                holder.setEmail(model.getEmail_Person());
                holder.setName(model.getName_Person());
                holder.del.setVisibility(View.INVISIBLE);
                if(user != null){
                    holder.del.setVisibility(View. VISIBLE);
                }
                holder.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      /*  builder = new AlertDialog.Builder(getApplicationContext(),R.style.Theme_AppCompat);
                        builder.setTitle("ยืนยัน");
                        builder.setMessage("ลบหรือไม่ ?");
                        builder.setPositiveButton("ใช้", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {*/
                                FirebaseDatabase.getInstance().getReference("Comment").child(postkey).removeValue();
                                Toast.makeText(getApplicationContext(),"Delete already !!",Toast.LENGTH_LONG).show();
                            }
                   /*     }).setNegativeButton("ไม่",null);
                        builder.show();
*/

                  //  }
                });
            }

            @NonNull
            @Override
            public Card_View onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View Sview = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.card_comment,viewGroup,false);
                return new Card_View(Sview);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class Card_View extends RecyclerView.ViewHolder{
        View mView;
        ImageButton del;
        public Card_View(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            del = (ImageButton) mView.findViewById(R.id.delete_comment);
        }
        public void setName(String name){
            TextView nameTx = (TextView) mView.findViewById(R.id.name_comment);
            nameTx.setText(name);
        }
        public void setEmail(String email){
            TextView emailTx = (TextView) mView.findViewById(R.id.text_email);
            emailTx.setText(email);
        }
        public void setDetail(String detail){
            TextView detailTx = (TextView) mView.findViewById(R.id.text_detail);
            detailTx.setText(detail);
        }

    }
}
