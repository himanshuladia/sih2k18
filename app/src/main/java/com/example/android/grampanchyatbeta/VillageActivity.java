package com.example.android.grampanchyatbeta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.grampanchyatbeta.Login.SignInActivity;
import com.example.android.grampanchyatbeta.UserWork.UserInfoActivity;
import com.google.firebase.auth.FirebaseAuth;

public class VillageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village);

        mAuth=FirebaseAuth.getInstance();
        //Check whether user is already login in or not
        if(mAuth.getCurrentUser()==null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
            mToolBar=(Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.village_activity_toolbar_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.villagesetting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.village_adopted_menu)
        {

        }
        if(item.getItemId()==R.id.log_out_menu)
        {
            mAuth.signOut();
            startActivity(new Intent(VillageActivity.this,SignInActivity.class));
            finish();
        }
        if(item.getItemId()==R.id.settings_menu)
        {
            startActivity(new Intent(VillageActivity.this, UserInfoActivity.class));

        }
        return true;

    }
}
