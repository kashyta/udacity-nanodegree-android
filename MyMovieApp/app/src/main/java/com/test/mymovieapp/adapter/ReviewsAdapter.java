package com.test.mymovieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mymovieapp.R;
import com.test.mymovieapp.viewHolder.ReviewsViewHolder;
import com.test.mymovieapp.model.Reviews;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsViewHolder> {

    private Context context;
    private List<Reviews> reviewList;

    public ReviewsAdapter(Context context, List<Reviews> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewsViewHolder(LayoutInflater.from(context).inflate(R.layout.reviews_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewsViewHolder holder, int position) {
        final Reviews review = reviewList.get(position);
        holder.bindReview(reviewList.get(position));

        holder.itemView.setOnClickListener(v -> {
            boolean expanded = review.isExpanded();
            review.setExpanded(!expanded);
            notifyItemChanged(position);
        });

    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

}
