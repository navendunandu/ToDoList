package com.example.todolist;

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

    // Declare a DataSource to interact with the database
    private DataSource dataSource;
    // Declare an ArrayAdapter for the ListView
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the DataSource and open the database connection
        dataSource = new DataSource(this);
        dataSource.open();

        // Get references to UI elements
        final EditText taskEditText = findViewById(R.id.editText);
        Button addButton = findViewById(R.id.addButton);
        ListView listView = findViewById(R.id.todoListView);

        // Get all tasks from the database and populate the ListView
        updateList();

        // Set click listeners for the "Add" button and ListView items
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the task from the EditText
                String task = taskEditText.getText().toString();
                // Check if the task is not empty
                if (!task.isEmpty()) {
                    // Add the task to the database and update the ListView
                    dataSource.createTask(task);
                    updateList();
                    // Clear the EditText
                    taskEditText.getText().clear();
                } else {
                    // Display a message if the task is empty
                    Toast.makeText(MainActivity.this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set a click listener for ListView items to delete tasks
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Delete the selected task from the database and update the ListView
                dataSource.deleteTask(id);
                updateList();
            }
        });
    }

    // Method to update the ListView with tasks from the database
    private void updateList() {
        // Clear the ArrayAdapter
        adapter.clear();
        // Retrieve all tasks from the database
        Cursor cursor = dataSource.getAllTasks();

        // Check if the cursor is not null and move to the first row
        if (cursor != null && cursor.moveToFirst()) {
            // Get the column index for the task column
            int columnIndex = cursor.getColumnIndex(DbHelper.COLUMN_TASK);

            // Check if the column index is valid
            if (columnIndex != -1) {
                // Loop through the cursor and add tasks to the ArrayAdapter
                do {
                    String task = cursor.getString(columnIndex);
                    adapter.add(task);
                } while (cursor.moveToNext());
            } else {
                // Handle the case where the column is not found
                Toast.makeText(this, "Column not found in cursor", Toast.LENGTH_SHORT).show();
            }

            // Close the cursor
            cursor.close();
        }

        // Notify the ArrayAdapter that the data set has changed
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        // Open the database connection when the activity resumes
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Close the database connection when the activity pauses
        dataSource.close();
        super.onPause();
    }
}
