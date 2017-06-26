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
    private String[] menuTitles;
    private DrawerLayout menuDrawerLayout;
    private ListView menuDrawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuTitles = getResources().getStringArray(R.array.menu_names);
        menuDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        menuDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.menu_list_text, menuTitles));

        // Set the list's click listener
       // menuDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }
}
