package com.coveros.coverosmobileapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maria on 6/15/2017.
 */

public class PostListView extends ListActivity {
    List<String> posts = new ArrayList<String>();

    ArrayAdapter<String> postsAdapter;


    int i = 0;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.posts);
        postsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, posts);
        setListAdapter(postsAdapter);

        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                JsonObject post = (JsonObject) responseList.get(position);
//                int postId = post.get("id").getAsInt();
//
//                Intent intent = new Intent(getApplicationContext(), Post.class);
//                intent.putExtra("id", "" + postId);
//                currentPostIntent = intent;
//                startActivity(intent);
                addPosts();
            }
        });

    }

    public void addPosts() {
        posts.add("Example item" + i++);
        postsAdapter.notifyDataSetChanged();
    }
}
