package com.k002422.tochkatest.ui.activity.MainUtils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.k002422.tochkatest.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    public ArrayList<String> usersList = new ArrayList<>();
    public boolean footerVisible = false;


    @Override
    public int getItemViewType(int position) {
        if ((position == getItemCount() - 1) && footerVisible)
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_users, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_footer_users, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder)
            ((UserViewHolder) holder).tv.setText(usersList.get(position));
    }

    @Override
    public int getItemCount() {
        int ret;
        if (footerVisible)
            ret = (usersList.size() + 1);
        else
            ret = usersList.size();
        return ret;
    }
}
