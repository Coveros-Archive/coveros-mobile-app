package com.coveros.coverosmobileapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by SRynestad on 6/26/2017.
 */

public class DrawerLayoutActivity extends ListActivity {
    private String[] menuTitles = new String[]{"Menu","Blog","Team"};
    private ListView menuDrawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        menuDrawerList = getListView();

        // Set the adapter for the list view
        menuDrawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1 , menuTitles));

        // Set the list's click listener
       //menuDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }
}
