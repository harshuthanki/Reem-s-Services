package com.reemsservices.fragment.loginfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.reemsservices.MainActivity;
import com.reemsservices.R;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class OTPFragment extends Fragment {

    @BindView(R.id.edt_otp_digit1)
    EditText edtOtp1;

    @BindView(R.id.edt_otp_digit2)
    EditText edtOtp2;

    @BindView(R.id.edt_otp_digit3)
    EditText edtOtp3;

    @BindView(R.id.edt_otp_digit4)
    EditText edtOtp4;

    @BindView(R.id.countdown_timer)
    CountdownView countdown_timer;

    @BindView(R.id.txt_user_mobile)
    TextView txt_user_mobile;

    private EditText[] editTexts;
    String otp;

    String mobileNumber;
    public OTPFragment(String mobileNumber) {
        this.mobileNumber = mobileNumber;

    }

    private KProgressHUD kProgressHUD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otpfragment, container, false);
        ButterKnife.bind(this, view);

        editTexts = new EditText[]{edtOtp1, edtOtp2, edtOtp3, edtOtp4};

        edtOtp1.addTextChangedListener(new PinTextWatcher(0));
        edtOtp2.addTextChangedListener(new PinTextWatcher(1));
        edtOtp3.addTextChangedListener(new PinTextWatcher(2));
        edtOtp4.addTextChangedListener(new PinTextWatcher(3));

        edtOtp1.setOnKeyListener(new PinOnKeyListener(0));
        edtOtp2.setOnKeyListener(new PinOnKeyListener(1));
        edtOtp3.setOnKeyListener(new PinOnKeyListener(2));
        edtOtp4.setOnKeyListener(new PinOnKeyListener(3));

        countdown_timer.start(30000);
        countdown_timer.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                getFragmentManager().popBackStack();
                Toasty.error(getActivity(), getResources().getString(R.string.otperror), 7000).show();
            }
        });

        txt_user_mobile.setText(mobileNumber);

        return view;
    }

    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            if (text.length() > 1)
                text = String.valueOf(text.charAt(0));

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getActivity().getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }

    @OnClick(R.id.img_back)
    public void img_back() {
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.btn_verify)
    public void btn_verify() {
        otp = edtOtp1.getText().toString() + edtOtp2.getText().toString() + edtOtp3.getText().toString() + edtOtp4.getText().toString();
        if (otp.isEmpty()) {
            Toasty.error(getActivity(),"OTP can't be empty",5000).show();
        }
        else {
          OTPVerify();
        }
    }
    public void OTPVerify(){

        kProgressHUD = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setAnimationSpeed(5).setDimAmount(0.5f);
        kProgressHUD.show();

        String user_id = SecurePreferences.getStringPreference(getActivity(),AppConstant.USERID);
        String user_unique_id = SecurePreferences.getStringPreference(getActivity(),AppConstant.USERUNIQUEID);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("user_id",user_id);
        params.put("user_unique_id",user_unique_id);
        params.put("user_mobile",mobileNumber);
        params.put("user_last_otp",otp);

        asyncHttpClient.post(AppConstant.BaseURL + "otp_verify.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean status = jsonObject.getBoolean("status");
                    if(status){
                        Toasty.success(getActivity(),jsonObject.optString("message"),5000).show();
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERID, jsonObject.getString("user_id"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERNAME, jsonObject.getString("user_name"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERPROFILE, jsonObject.getString("user_profile"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USEREMAIL, jsonObject.getString("user_email"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERMOB, jsonObject.getString("user_mobile"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERTYPE, jsonObject.getString("user_type"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERUNIQUEID, jsonObject.getString("user_unique_id"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERUNIQUECODE, jsonObject.getString("user_ref_code"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.USERCOIN, jsonObject.getString("user_coin"));
                        SecurePreferences.savePreferences(getActivity(), AppConstant.IS_LOGIN, true);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else{
                        Toasty.error(getActivity(),jsonObject.optString("message"),5000).show();
                    }
                } catch (JSONException e) {
                    if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
