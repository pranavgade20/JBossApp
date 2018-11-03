package com.example.p.jbossapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ListBaseAdapter extends BaseAdapter {

    private ArrayList<ListDetails> listDetailsArrayList;
    private LayoutInflater inflater;
    private Context context;

    public ListBaseAdapter(Context context, ArrayList<ListDetails> results) {
        listDetailsArrayList = results;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listDetailsArrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        return listDetailsArrayList.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.activity_listview, null);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name_text);
            holder.description = (TextView) view.findViewById(R.id.email_text);
            holder.profileImg = (ImageView) view.findViewById(R.id.profile_image);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set any name you want here
        holder.name.setText(listDetailsArrayList.get(pos).getName());
        // Set any email you want here
        holder.description.setText(listDetailsArrayList.get(pos).getDescription());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();

        Glide.with(context)
                .load(listDetailsArrayList.get(pos).getUrl())
                .apply(options)
                .into((ImageView) holder.profileImg);

        return view;
    }

    static class ViewHolder {
        public TextView name;
        public TextView description;
        public ImageView profileImg;
    }
}

