package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @autor Sergey Shustikov
 * (pandarium.shustikov@gmail.com)
 */
public class DisplayActivity extends ActionBarActivity {

    @InjectObject
    public ArrayList<World> worldArrayList;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);

        mListView = (ListView) findViewById(R.id.listView);
        final String[] titles = new String[worldArrayList.size()];
        for (int i = 0; i < worldArrayList.size(); i++) {
            titles[i] = worldArrayList.get(i).name;
        }
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(DisplayActivity.this).create();
                alertDialog.setTitle(titles[position]);
                World world = worldArrayList.get(position);
                alertDialog.setMessage("Information :\n"
                                + "name : " + world.name + "\n"
                                + "mapURL : " + world.mapURL + "\n"
                                + "worldStatus : id = " + world.worldStatus.id + ", description = " + world.worldStatus.description + "\n"
                                + "mapURL : " + world.mapURL + "\n"
                                + "country : " + world.country + "\n"
                                + "language : " + world.language + "\n"
                                + "id : " + world.id + "\n"
                                + "url : " + world.url + "\n"
                );
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
    }
}
