package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    static EditText editText;
    int position;
    boolean savedStatus;
    String oldText;

    @Override
    public void onBackPressed() {

        if(editText.getText().toString().length()==0||(position!=-1&&editText.getText().toString().contentEquals(oldText)))
            finish();
        else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Want to save the changes ?")
                    .setMessage("Do you want to save the work before closing ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                if (position == -1) {

                                    MainActivity.contentList.add(editText.getText().toString());
                                    if (editText.getText().toString().contains(" "))
                                        MainActivity.notesList.add(editText.getText().toString().substring(0, editText.getText().toString().indexOf(' ')));
                                    else if (editText.getText().toString().contains("\n"))
                                        MainActivity.notesList.add(editText.getText().toString().substring(0, editText.getText().toString().indexOf('\n')));
                                    else
                                        MainActivity.notesList.add(editText.getText().toString());

                                    sharedPreferences.edit().putString("content", ObjectSerializer.serialize(MainActivity.contentList)).apply();
                                    sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notesList)).apply();
                                    MainActivity.arrayAdapterfornotesList.notifyDataSetChanged();
                                    MainActivity.arrayAdapterforcontentList.notifyDataSetChanged();
                                    finish();

                                } else {

                                        MainActivity.contentList.set(position,editText.getText().toString());
                                        if (editText.getText().toString().contains(" "))
                                            MainActivity.notesList.set(position,editText.getText().toString().substring(0, editText.getText().toString().indexOf(' ')));
                                        else if (editText.getText().toString().contains("\n"))
                                            MainActivity.notesList.set(position,editText.getText().toString().substring(0, editText.getText().toString().indexOf('\n')));
                                        else
                                            MainActivity.notesList.set(position,editText.getText().toString());

                                        sharedPreferences.edit().putString("content", ObjectSerializer.serialize(MainActivity.contentList)).apply();
                                        sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notesList)).apply();
                                        MainActivity.arrayAdapterfornotesList.notifyDataSetChanged();
                                        MainActivity.arrayAdapterforcontentList.notifyDataSetChanged();
                                        finish();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(NotesActivity.this, " " + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.savemenu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(position==-1) {
            new AlertDialog.Builder(this)
                    .setTitle("Want to save ?")
                    .setMessage("Do you want to save this ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sharedPreferences = NotesActivity.this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

                            MainActivity.contentList.add(editText.getText().toString());
                            if(editText.getText().toString().contains(" "))
                                MainActivity.notesList.add(editText.getText().toString().substring(0, editText.getText().toString().indexOf(' ')));
                            else if(editText.getText().toString().contains("\n"))
                                MainActivity.notesList.add(editText.getText().toString().substring(0, editText.getText().toString().indexOf('\n')));
                            else
                                MainActivity.notesList.add(editText.getText().toString());
                            try {
                                sharedPreferences.edit().putString("content", ObjectSerializer.serialize(MainActivity.contentList)).apply();
                                sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notesList)).apply();
                                MainActivity.arrayAdapterfornotesList.notifyDataSetChanged();
                                MainActivity.arrayAdapterforcontentList.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(NotesActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        else
        {
            new AlertDialog.Builder(this)
                    .setTitle("Want to save ?")
                    .setMessage("Do you want to save the changes ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sharedPreferences = NotesActivity.this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

                            MainActivity.contentList.set(position,editText.getText().toString());
                            if(editText.getText().toString().contains(" "))
                                MainActivity.notesList.set(position,editText.getText().toString().substring(0, editText.getText().toString().indexOf(' ')));
                            else if(editText.getText().toString().contains("\n"))
                                MainActivity.notesList.set(position,editText.getText().toString().substring(0, editText.getText().toString().indexOf('\n')));
                            else
                                MainActivity.notesList.set(position,editText.getText().toString());
                            try {
                                sharedPreferences.edit().putString("content", ObjectSerializer.serialize(MainActivity.contentList)).apply();
                                sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notesList)).apply();
                                MainActivity.arrayAdapterfornotesList.notifyDataSetChanged();
                                MainActivity.arrayAdapterforcontentList.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(NotesActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        editText = findViewById(R.id.noteEditText);
        sharedPreferences = NotesActivity.this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        position = intent.getIntExtra("itemPressed",-1);
        savedStatus=false;
        try {
            if(position!=-1) {
                editText.setText(MainActivity.contentList.get(position));
                oldText=editText.getText().toString();
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this,e+"",Toast.LENGTH_SHORT).show();

        }
    }
}
