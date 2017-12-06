package com.k002422.tochkatest.ui.activity.MainUtils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.k002422.tochkatest.R;

class FooterViewHolder extends RecyclerView.ViewHolder {

    FooterViewHolder(View itemView) {
        super(itemView);
        ProgressBar pb = itemView.findViewById(R.id.paginationPB);
    }
}
