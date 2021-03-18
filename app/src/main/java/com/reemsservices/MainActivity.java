package com.reemsservices;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.reemsservices.fragment.navigationtab.BookingFragment;
import com.reemsservices.fragment.navigationtab.HomeFragment;
import com.reemsservices.fragment.navigationtab.profile.EditProfileFragment;
import com.reemsservices.fragment.navigationtab.profile.ProfileFragment;
import com.reemsservices.fragment.navigationtab.wallet.WalletFragment;
import com.reemsservices.fragment.navigationtab.profile.addbusiness.EditBusinessFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img_home)
    ImageView img_home;

    @BindView(R.id.txt_home)
    TextView txt_home;

    @BindView(R.id.img_booking)
    ImageView img_booking;

    @BindView(R.id.txt_booking)
    TextView txt_booking;

    @BindView(R.id.img_add)
    ImageView img_add;

    @BindView(R.id.txt_addbusiness)
    TextView txt_addbusiness;

    @BindView(R.id.img_wallet)
    ImageView img_wallet;

    @BindView(R.id.txt_wallet)
    TextView txt_wallet;

    @BindView(R.id.img_profile)
    ImageView img_profile;

    @BindView(R.id.txt_profile)
    TextView txt_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        linear_home();
    }
    @OnClick(R.id.linear_home)
    public void linear_home(){
        AppConstant.replaceFragmentNav(getSupportFragmentManager(),new HomeFragment(),"HomeFragment");
        img_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.darkblue), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_home.setTextColor(getResources().getColor(R.color.darkblue));
        img_booking.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_booking.setTextColor(getResources().getColor(R.color.gray));
        img_add.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_addbusiness.setTextColor(getResources().getColor(R.color.gray));
        img_wallet.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_wallet.setTextColor(getResources().getColor(R.color.gray));
        img_profile.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_profile.setTextColor(getResources().getColor(R.color.gray));
    }
    @OnClick(R.id.linear_booking)
    public void linear_booking(){
        if (SecurePreferences.getBooleanPreference(getApplicationContext(), AppConstant.IS_LOGIN)) {
            AppConstant.replaceFragmentNav(getSupportFragmentManager(),new BookingFragment(),"BookingFragment");
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        img_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_home.setTextColor(getResources().getColor(R.color.gray));
        img_booking.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.darkblue), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_booking.setTextColor(getResources().getColor(R.color.darkblue));
        img_add.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_addbusiness.setTextColor(getResources().getColor(R.color.gray));
        img_wallet.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_wallet.setTextColor(getResources().getColor(R.color.gray));
        img_profile.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_profile.setTextColor(getResources().getColor(R.color.gray));
    }

    @OnClick(R.id.linear_wallet)
    public void linear_wallet(){
        if (SecurePreferences.getBooleanPreference(getApplicationContext(), AppConstant.IS_LOGIN)) {
            AppConstant.replaceFragmentNav(getSupportFragmentManager(),new WalletFragment(),"WalletFragment");
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        img_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_home.setTextColor(getResources().getColor(R.color.gray));
        img_booking.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_booking.setTextColor(getResources().getColor(R.color.gray));
        img_add.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_addbusiness.setTextColor(getResources().getColor(R.color.gray));
        img_wallet.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.darkblue), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_wallet.setTextColor(getResources().getColor(R.color.darkblue));
        img_profile.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_profile.setTextColor(getResources().getColor(R.color.gray));
    }
    @OnClick(R.id.linear_profile)
    public void linear_profile(){
        if (SecurePreferences.getBooleanPreference(getApplicationContext(), AppConstant.IS_LOGIN)) {
            AppConstant.replaceFragmentNav(getSupportFragmentManager(),new ProfileFragment(),"ProfileFragment");
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        img_home.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_home.setTextColor(getResources().getColor(R.color.gray));
        img_booking.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_booking.setTextColor(getResources().getColor(R.color.gray));
        img_add.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_addbusiness.setTextColor(getResources().getColor(R.color.gray));
        img_wallet.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_wallet.setTextColor(getResources().getColor(R.color.gray));
        img_profile.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.darkblue), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_profile.setTextColor(getResources().getColor(R.color.darkblue));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("EditProfileFragment");
        if(fragment!=null){
            EditProfileFragment editFragment = (EditProfileFragment)fragment;
            editFragment.onActivityResult(requestCode, resultCode, data);
        }
        Fragment fragment_editBusiness = getSupportFragmentManager().findFragmentByTag("EditBusinessFragment");
        if(fragment_editBusiness!=null){
            EditBusinessFragment editBusinessFragment = (EditBusinessFragment) fragment_editBusiness;
            editBusinessFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}