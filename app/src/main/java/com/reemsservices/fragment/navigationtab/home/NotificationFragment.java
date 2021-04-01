package com.reemsservices.fragment.navigationtab.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reemsservices.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class NotificationFragment extends Fragment {

    @BindView(R.id.recycle_notification)
    RecyclerView recycle_notification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificationfragment,container,false);
        ButterKnife.bind(this,view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycle_notification.setLayoutManager(layoutManager);

        NotificationAdapter notificationAdapter = new NotificationAdapter();
        recycle_notification.setAdapter(notificationAdapter);

        return view;
    }
    @OnClick(R.id.img_back)
    public void back(){
        getFragmentManager().popBackStack();
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
        @NonNull
        @Override
        public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.row_notification_recycle,parent,false);
            NotificationHolder notificationHolder = new NotificationHolder(view);
            return notificationHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 3;
        }

        class NotificationHolder extends RecyclerView.ViewHolder {




            public NotificationHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
