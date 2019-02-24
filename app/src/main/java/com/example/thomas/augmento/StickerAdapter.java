package com.example.thomas.augmento;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import Utils.GifImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    private List<Sticker> stickerList;

    public class StickerViewHolder extends RecyclerView.ViewHolder
    {
        public GifImageView gifImageView;

        public StickerViewHolder(View view)
        {
            super(view);

            gifImageView=view.findViewById(R.id.gifImageView);
        }

    }

    public StickerAdapter(List<Sticker> stickerList)
    {
        this.stickerList=stickerList;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sticker_layout, viewGroup, false);
        return new StickerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder stickerViewHolder, int i) {

        Sticker sticker=stickerList.get(i);
        stickerViewHolder.gifImageView.setGifImageResource(sticker.getGifUrl());

    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }
}
