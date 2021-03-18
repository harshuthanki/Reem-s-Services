package com.reemsservices.fragment.navigationtab;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.reemsservices.R;
import com.reemsservices.fragment.navigationtab.wallet.WalletFragment;
import com.reemsservices.helper.AppConstant;
import com.reemsservices.helper.SecurePreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingFragment extends Fragment {

    @BindView(R.id.recycle_booking)
    RecyclerView recycle_booking;

    @BindView(R.id.tablayout)
    TabLayout tablayout;

    String partner = "p";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookingfragment,container,false);
        ButterKnife.bind(this,view);

        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.yourBooking)));
        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.businessBooking)));

        if(SecurePreferences.getStringPreference(getActivity(), AppConstant.USERTYPE).equalsIgnoreCase(partner)){
            tablayout.setVisibility(View.VISIBLE);
        }
        else {
            tablayout.setVisibility(View.GONE);
        }


        tablayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#112d4e"));


        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    Toast.makeText(getActivity(), "Tab 0", Toast.LENGTH_SHORT).show();
                }
                else if(tab.getPosition()==1){
                    Toast.makeText(getActivity(), "Tab 1", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recycle_booking.setLayoutManager(layoutManager);

        BookingAdapter bookingAdapter = new BookingAdapter();
        recycle_booking.setAdapter(bookingAdapter);

        return view;
    }
    class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingHolder> {
        @NonNull
        @Override
        public BookingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_booking_recycle,parent,false);
            BookingHolder bookingHolder = new BookingHolder(view);
            return bookingHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BookingHolder holder, int position) {
            holder.img_typebooking.setImageResource(R.drawable.ic_salonman);
            holder.txt_time.setText("Time");
            holder.txt_timeofbooking.setText("8:00 AM");
            holder.txt_typeofbooking.setText("Salon for Men");
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class BookingHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.img_typebooking)
            ImageView img_typebooking;

            @BindView(R.id.txt_typeofbooking)
            TextView txt_typeofbooking;

            @BindView(R.id.txt_time)
            TextView txt_time;

            @BindView(R.id.txt_timeofbooking)
            TextView txt_timeofbooking;

            public BookingHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
