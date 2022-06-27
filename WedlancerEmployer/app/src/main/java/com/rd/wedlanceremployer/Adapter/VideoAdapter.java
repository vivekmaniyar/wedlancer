package com.rd.wedlanceremployer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.rd.wedlanceremployer.Models.Portfolio;
import com.rd.wedlanceremployer.Activity.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    List<Portfolio> imglist;
    Context context;

    // Constructor for initialization
    public VideoAdapter(Context context, List<Portfolio> objBookingList) {
        this.context = context;
        imglist = objBookingList;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_portfolio_video, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
//AIzaSyDqcEukRAZHquyS9f1sYyhbnRCCnJuyfP0



        holder.img.initialize(
                "AIzaSyDqcEukRAZHquyS9f1sYyhbnRCCnJuyfP0",
                new YouTubePlayer.OnInitializedListener() {

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(imglist.get(position).getVideo());
//                        youTubePlayer.play();
                    }


                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult
                                                                youTubeInitializationResult) {
                        Toast.makeText(context, "Video player Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return imglist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        YouTubePlayerView img;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.youtube_player_view);

        }
    }
}
