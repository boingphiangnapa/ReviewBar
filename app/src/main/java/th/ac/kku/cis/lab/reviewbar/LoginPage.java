package th.ac.kku.cis.lab.reviewbar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    ImageButton image_btn;
    Button btn_lo;
    EditText email_in;
    EditText pass_in;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        btn_lo = (Button) findViewById(R.id.btn_login);
        email_in = (EditText) findViewById(R.id.edit_email);
        pass_in = (EditText) findViewById(R.id.edit_password);

        mAuth = FirebaseAuth.getInstance();


        btn_lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_in.getText().toString();
                String pass = pass_in.getText().toString();
                mAuth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   Intent intent = new Intent(LoginPage.this,
                                           Main2Activity.class);
                                   startActivity(intent);
                                   finish();
                               }
                               else{
                                   Toast.makeText(LoginPage.this,"Failed !!",
                                           Toast.LENGTH_LONG).show();
                               }
                            }
                        });
            }
        });

        image_btn = (ImageButton) findViewById(R.id.btn_back_page);
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
