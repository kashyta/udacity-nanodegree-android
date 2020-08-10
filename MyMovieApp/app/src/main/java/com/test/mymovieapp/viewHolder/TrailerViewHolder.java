package com.test.mymovieapp.viewHolder;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.test.mymovieapp.R;
import com.test.mymovieapp.adapter.TrailerAdapter;
import com.test.mymovieapp.model.Trailer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.iv_trailer)
    public
    ImageView trailerView;
    @BindView(R.id.tv_trailer_name)
    public
    TextView trailerName;
    @BindView(R.id.play_trailer)
    ImageButton trailerPlay;

    public TrailerViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        trailerPlay.setOnClickListener(this);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Trailer trailer = TrailerAdapter.trailers.get(getAdapterPosition());
        if (trailer != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(TrailerAdapter.YOUTUBE_VIDEO + trailer.getKey()));
            if (intent.resolveActivity(TrailerAdapter.context.getPackageManager()) != null) {
                TrailerAdapter.context.startActivity(intent);
            } else {
                Toast.makeText(TrailerAdapter.context, "Error playing the trailer", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
