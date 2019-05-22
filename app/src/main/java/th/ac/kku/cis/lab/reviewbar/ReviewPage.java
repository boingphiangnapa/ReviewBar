package th.ac.kku.cis.lab.reviewbar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;

public class ReviewPage extends Fragment {

    View view;
    EditText name_store;
    EditText person_name;
    EditText email_name;
    EditText message_name;
    Button btn_post;
    DatabaseReference mData;
    FirebaseDatabase firebaseDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_review_page,container,
                false);
        name_store = (EditText) view.findViewById(R.id.edit_store_name);
        person_name = (EditText) view.findViewById(R.id.edit_person_name);
        email_name = (EditText) view.findViewById(R.id.edit_email_name);
        message_name = (EditText) view.findViewById(R.id.edit_message_name);
        btn_post = (Button) view.findViewById(R.id.btn_post_review);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mData = firebaseDatabase.getReference("Comment").push();

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_st = name_store.getText().toString();
                String person_st = person_name.getText().toString();
                String email_st = email_name.getText().toString();
                String message_st = message_name.getText().toString();
                mData.child("Name_Person").setValue(person_st);
                mData.child("Email_Person").setValue(email_st);
                mData.child("Message_Person").setValue(message_st);
                mData.child("Name_Store").setValue(name_st);
                Toast.makeText(getActivity(),"Review Success",
                        Toast.LENGTH_LONG).show();

            }
        });
        return view;
    }
}
