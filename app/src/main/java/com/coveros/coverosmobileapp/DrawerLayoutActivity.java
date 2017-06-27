package com.coveros.coverosmobileapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.coveros.coverosmobileapp.post.PostListActivity;
import com.coveros.coverosmobileapp.website.MainActivity;


/**
 * Created by SRynestad on 6/26/2017.
 */

public class DrawerLayoutActivity extends ListActivity {
    private String[] menuTitles = new String[]{"Menu","Blog","Bookmarks"};
    private ListView menuDrawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        menuDrawerList = getListView();

        // Set the adapter for the list view
        menuDrawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1 , menuTitles));

        menuDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        Intent a = new Intent(DrawerLayoutActivity.this, MainActivity.class);
                        startActivity(a);
                        break;

                    case 1:
                        Intent b = new Intent(DrawerLayoutActivity.this, PostListActivity.class);
                        startActivity(b);
                        break;
                }
            }
        });
    }
}
