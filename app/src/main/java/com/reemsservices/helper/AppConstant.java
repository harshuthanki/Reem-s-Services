package com.reemsservices.helper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.reemsservices.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppConstant  {

    public static String cleaning="Cleaning";
    public static String salonforwomen="Salon for Women";
    public static String salonformen="Salon for Men";
    public static String repairs="Repairs";
    public static String painting="Painting";
    public static String itprofessionals="IT Professionals";
    public static String movingandpacking="Moving & Packaging";
    public static String IS_LOGIN="isLogin";
    public static String USERNAME="userName";
    public static String USERMOB="mobileNumber";
    public static String USEREMAIL="email";
    public static String USERID="userId";
    public static String USERUNIQUEID="userUniqueId";
    public static String USERUNIQUECODE="userUniqueCode";
    public static String USERCOIN="userCoin";
    public static String USERPROFILE="userProfile";
    public static String USERTYPE="userType";
    public static String USERSELECTEDCITY="userSelectedCity";
    public static String BaseURL="http://192.168.1.20/Reemsservices/api/";
    public static String ImageURL="http://192.168.1.20/Reemsservices/images/";



    public static void addLoginFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_login_main, fragment, tag).addToBackStack(null);
        fragmentTransaction.commit();
    }
    public static void replaceLoginFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_login_main, fragment, tag);
        fragmentTransaction.commit();
    }
    public static void replaceFragmentNav(FragmentManager fragmentManager, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, fragment, tag);
        fragmentTransaction.commit();
    }

    public static void addFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_full, fragment, tag).addToBackStack(null);
        fragmentTransaction.commit();
    }
    public static String dateFormate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public static String timeFormate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
