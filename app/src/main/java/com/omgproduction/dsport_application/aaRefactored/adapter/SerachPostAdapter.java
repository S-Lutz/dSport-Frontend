package com.omgproduction.dsport_application.aaRefactored.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omgproduction.dsport_application.R;
import com.omgproduction.dsport_application.aaRefactored.models.nodes.PostNode;

import java.util.ArrayList;

public class SerachPostAdapter extends RecyclerView.Adapter<SerachPostAdapter.PostItemViewHolder> {

    private ArrayList<PostNode> posts;
    private Context context;

    public class PostItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemPic;
        private TextView postTitle;
        private TextView postOwner;
        private TextView created;

        public PostItemViewHolder(View itemView) {
            super(itemView);
            itemPic = (ImageView) itemView.findViewById(R.id.post_item_pic);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postOwner = (TextView) itemView.findViewById(R.id.post_owner);
            created = (TextView) itemView.findViewById(R.id.post_created);
        }

        public ImageView getItemPic() {
            return itemPic;
        }
    }

    public SerachPostAdapter(Context context, ArrayList<PostNode> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public SerachPostAdapter.PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_row_layout_post, parent, false);
        SerachPostAdapter.PostItemViewHolder vh = new SerachPostAdapter.PostItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SerachPostAdapter.PostItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}