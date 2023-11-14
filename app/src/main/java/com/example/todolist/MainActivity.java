package com.example.todolist;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DataSource dataSource;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new DataSource(this);
        dataSource.open();

        final EditText taskEditText = findViewById(R.id.editText);
        Button addButton = findViewById(R.id.addButton);
        ListView listView = findViewById(R.id.todoListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = taskEditText.getText().toString();
                if (!task.isEmpty()) {
                    dataSource.createTask(task);
                    updateList();
                    taskEditText.getText().clear();
                } else {
                    Toast.makeText(MainActivity.this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dataSource.deleteTask(id);
                updateList();
            }
        });

        updateList();
    }

    private void updateList() {
        adapter.clear();
        Cursor cursor = dataSource.getAllTasks();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                @SuppressLint("Range") String task = cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_TASK));
                adapter.add(task);
                cursor.moveToNext();
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
