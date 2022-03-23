package com.wyksofts.saveone.Adapters.AlbumAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Albums.AlbumModels;

import java.util.ArrayList;
import java.util.List;

public class AlbumsAdapter extends SliderViewAdapter<AlbumsAdapter.SliderAdapterVH> {

    private Context context;
    private List<AlbumModels> list_array;

    public AlbumsAdapter(Context context, List<AlbumModels> list_array) {
        this.context = context;
        this.list_array = list_array;
    }

    public void renewItems(List<AlbumModels> sliderItems) {
        this.list_array = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.list_array.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(AlbumModels sliderItem) {
        this.list_array.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.albums_image, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH holder, final int position) {

        AlbumModels sliderItem = list_array.get(position);

        Glide.with(holder.itemView)
                .load(sliderItem.getAlbum_img_rc())
                .fitCenter()
                .into(holder.imageViewBackground);

    }

    @Override
    public int getCount() {
        return list_array.size();
    }

    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.album_image);
        }
    }

}