package com.joyliu.simpletodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etEditItem = (EditText) findViewById(R.id.etEditItem);

        String selectedItem = getIntent().getStringExtra("item text");

        etEditItem.setText(selectedItem);
        etEditItem.setSelection(etEditItem.getText().length());
    }

    public void onUpdateItem(View view) {
        Intent data = new Intent();
        data.putExtra("new item text", etEditItem.getText().toString());
        data.putExtra("item position", getIntent().getIntExtra("item position", 0));

        setResult(RESULT_OK, data);
        finish();
        overridePendingTransition(R.animator.activity_back_in, R.animator.activity_back_out);
    }

}
