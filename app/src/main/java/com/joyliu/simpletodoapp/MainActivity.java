package com.joyliu.simpletodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lvItems;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private EditText etNewItem;
    private String selectedItem;
    private int selectedPosition;
    private final int REQUEST_CODE = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etNewItem);

        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        etNewItem.clearFocus();

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newText = data.getExtras().getString("new item text");
            int position = data.getExtras().getInt("item position", 0);

            items.set(position, newText);
            itemsAdapter.notifyDataSetChanged();
            writeItems();

            Toast.makeText(this, "Item Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupListViewListener() {
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedItem = items.get(position);
                        selectedPosition = position;

                        launchEditVew();
                    }
                }
        );
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        Toast.makeText(getBaseContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
        );
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddItem(View view) {
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    public void launchEditVew() {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        i.putExtra("item text", selectedItem);
        i.putExtra("item position", selectedPosition);

        startActivityForResult(i, REQUEST_CODE);
        overridePendingTransition(R.animator.activity_in, R.animator.activity_out);
    }
}
