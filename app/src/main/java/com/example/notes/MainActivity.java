package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SharedPreferences sharedPreferences;
    static ArrayList<String> notesList;
    static ArrayList<String> contentList;
    static ArrayAdapter<String> arrayAdapterfornotesList;
    static ArrayAdapter<String> arrayAdapterforcontentList;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Want To Close ?")
                .setMessage("Are you sure you want to close the app ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        super.onOptionsItemSelected(item);

        Intent intent = new Intent(this,NotesActivity.class);
        startActivity(intent);
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.noteList);

        notesList = new ArrayList<>();
        contentList = new ArrayList<>();
        ArrayList<String> notes = new ArrayList<>();
        ArrayList<String> content = new ArrayList<>();

        sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes", ObjectSerializer.serialize(new ArrayList<String>())));
            content = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("content", ObjectSerializer.serialize(new ArrayList<String>())));

            arrayAdapterfornotesList = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,notesList);
            arrayAdapterforcontentList = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contentList);

            listView.setAdapter(arrayAdapterfornotesList);
            if (notes.size()>0)
            {
                for(int i=0; i<notes.size();i++) {
                    notesList.add(notes.get(i));
                    contentList.add(content.get(i));
                }
            }
            else
            {

                notesList.add("Example note");

                contentList.add("Example note");

                arrayAdapterfornotesList.notifyDataSetChanged();

                arrayAdapterforcontentList.notifyDataSetChanged();

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this,NotesActivity.class);
                intent.putExtra("itemPressed",position);

                startActivity(intent);

            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            int positionLocal;
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                positionLocal=pos;
               new AlertDialog.Builder(MainActivity.this)
                       .setTitle("Do you want to delete ?")
                       .setMessage("Do you want to delete this note ?")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               contentList.remove(positionLocal);
                               notesList.remove(positionLocal);

                             /*  sharedPreferences.edit().remove(notesList.get(positionLocal)).apply();
                               sharedPreferences.edit().remove(contentList.get(positionLocal)).apply();*/
                             try{
                               sharedPreferences.edit().putString("content", ObjectSerializer.serialize(MainActivity.contentList)).apply();
                               sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notesList)).apply();

                               arrayAdapterforcontentList.notifyDataSetChanged();
                               arrayAdapterfornotesList.notifyDataSetChanged();
                             } catch (Exception e) {
                                     e.printStackTrace();
                               }
                           }
                       })
                       .setNegativeButton("No",null)
                       .show();

                return true;
            }
        });
    }

}
