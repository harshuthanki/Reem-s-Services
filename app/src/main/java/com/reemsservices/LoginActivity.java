package com.reemsservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.reemsservices.fragment.loginfragment.SignInFragment;
import com.reemsservices.fragment.loginfragment.SignUpFragment;
import com.reemsservices.helper.AppConstant;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AppConstant.replaceLoginFragment(getSupportFragmentManager(),new SignInFragment(),"SignInFragment");
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}