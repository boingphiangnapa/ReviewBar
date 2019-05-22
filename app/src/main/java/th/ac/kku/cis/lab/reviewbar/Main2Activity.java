package th.ac.kku.cis.lab.reviewbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    FragmentManager manager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, new HomePage());
                    transaction.commit();
                    return true;
                case R.id.navigation_review:
                    FragmentTransaction transaction1 = manager.beginTransaction();
                    transaction1.replace(R.id.container, new ReviewPage());
                    transaction1.commit();
                    return true;
                case R.id.navigation_notifications:
                    FragmentTransaction transaction2 = manager.beginTransaction();
                    transaction2.replace(R.id.container, new NotePage());
                    transaction2.commit();
                    return true;
                case R.id.navigation_admin:
                    if(user == null) {
                        Intent intent = new Intent(Main2Activity.this, LoginPage.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Main2Activity.this,"Sign In Already !!",Toast.LENGTH_LONG).show();
                    }
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, new HomePage());
        transaction.commit();

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}
