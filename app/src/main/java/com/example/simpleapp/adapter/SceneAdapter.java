package com.example.simpleapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.simpleapp.R;
import com.example.simpleapp.model.Scene;

import java.util.ArrayList;

// 어댑터 안에 뷰홀더를 정의하기 위해서
// 뷰홀더 : 각 아이템 뷰를 담는 객체
public class SceneAdapter extends RecyclerView.Adapter<SceneAdapter.ViewHolder> {
    Context context;
    ArrayList<Scene> scenes = new ArrayList<Scene>();

    public interface MyOnItemClickListener {
        void OnItemClick(ViewHolder holder, View view, int position);
    }

    MyOnItemClickListener mListener;
    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.mListener = listener;
    }

    public SceneAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.gallery_item_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scene item = scenes.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return scenes.size();
    }

    public void addItem(Scene item) {
        scenes.add(item);
    }

    public void addItems(ArrayList<Scene> items) {
        this.scenes = items;
    }

    public Scene getItem(int position) {
        return scenes.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView videoView;

        public ViewHolder(@NonNull View itemView) { // 뷰 홀더가 갖을 view
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.sceneImageView);
            videoView = (ImageView)itemView.findViewById(R.id.sceneVideoView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(mListener != null) {
                        mListener.OnItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(Scene scene) {
            Glide.with(context).load(scene.getImageUrl()).into(imageView);
            if(scene.getIsImage()) {
                videoView.setVisibility(View.GONE);
            }else {
                videoView.setVisibility(View.VISIBLE);
            }
        }

    }

}
