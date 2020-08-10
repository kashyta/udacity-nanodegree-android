package com.test.mymovieapp.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.test.mymovieapp.R;
import com.test.mymovieapp.model.Reviews;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_reviewer)
    TextView author;
    @BindView(R.id.tv_review_details)
    TextView content;
    @BindView(R.id.content)
    LinearLayout subItem;

    public ReviewsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindReview(Reviews review) {
        boolean expanded = review.isExpanded();
        subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
        author.setText(review.getAuthor());
        content.setText(review.getContent());
    }
}

