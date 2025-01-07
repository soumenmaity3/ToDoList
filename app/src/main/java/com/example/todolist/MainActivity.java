package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextTask;
    private Button buttonAddTask;
    private ListView listViewTasks;
    private ArrayList<String> tasksList;
    private CustomTaskAdapter customAdapter;
    private SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.edtTask);
        buttonAddTask = findViewById(R.id.addtask);
        listViewTasks = findViewById(R.id.list_view);

        sharedPreferences = getSharedPreferences("ToDoListPrefs", MODE_PRIVATE);
        tasksList = loadTasks();
        customAdapter = new CustomTaskAdapter(this, tasksList);
        listViewTasks.setAdapter(customAdapter);

        buttonAddTask.setOnClickListener(v -> {
            String task = editTextTask.getText().toString().trim();
            if (!task.isEmpty()) {
                String currentTime = getCurrentTime();
                tasksList.add(task + " - " + currentTime);
                customAdapter.notifyDataSetChanged();
                saveTasks();
                editTextTask.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editTextTask.getWindowToken(), 0);
                }
                editTextTask.clearFocus();
            } else {
                Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
            }
        });

        final Handler handler = new Handler();
        Runnable timeRunnable = new Runnable() {
            @Override
            public void run() {
                customAdapter.notifyDataSetChanged();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(timeRunnable);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yy ");
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.format(formatter);
    }

    protected void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasksList);
        editor.putString("tasks", json);
        editor.apply();
    }

    private ArrayList<String> loadTasks() {
        String json = sharedPreferences.getString("tasks", null);
        if (json != null) {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                return gson.fromJson(json, type);
            } catch (Exception e) {
                e.printStackTrace();
                sharedPreferences.edit().clear().apply();
            }
        }
        return new ArrayList<>();
    }
}
