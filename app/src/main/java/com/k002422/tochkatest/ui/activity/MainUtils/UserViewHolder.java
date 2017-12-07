package com.k002422.tochkatest.ui.activity.MainUtils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.k002422.tochkatest.R;

class UserViewHolder extends RecyclerView.ViewHolder {
    TextView tv;
    ImageView iv;

    UserViewHolder(View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.gitHubUserName);
        iv = itemView.findViewById(R.id.gitHubUserAvatar);
    }
}
