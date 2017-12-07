package com.k002422.tochkatest.ui.activity.MainUtils;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.k002422.tochkatest.R;
import com.k002422.tochkatest.utils.GitHubUtils.ResponseEntity.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    public ArrayList<Item> usersList = new ArrayList<>();
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
        if (holder instanceof UserViewHolder) {
            UserViewHolder uvHolder = ((UserViewHolder) holder);
            uvHolder.tv.setText(usersList.get(position).login);
            Picasso.with(uvHolder.iv.getContext()).
                    load(Uri.parse(usersList.get(position).avatarUrl)).
                    fit().
                    into(uvHolder.iv);
        }
    }

    @Override
    public int getItemCount() {
        return footerVisible ? (usersList.size() + 1) : usersList.size();
    }
}
