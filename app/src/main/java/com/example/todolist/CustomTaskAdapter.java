package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class CustomTaskAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> tasksList;

    public CustomTaskAdapter(Context context, ArrayList<String> tasksList) {
        this.context = context;
        this.tasksList = tasksList;
    }

    @Override
    public int getCount() {
        return tasksList.size();
    }

    @Override
    public Object getItem(int position) {
        return tasksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    CheckBox checkBox;
    String task;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.task_item, parent, false);
        }

        checkBox= convertView.findViewById(R.id.taskCheckbox);
        Button deleteButton = convertView.findViewById(R.id.btnDeleteTask);
        TextView timeTextView = convertView.findViewById(R.id.time);

        task = tasksList.get(position);
        checkBox.setText(task.split(" - ")[0]);
        timeTextView.setText(task.split(" - ")[1]);

        deleteButton.setOnClickListener(v -> {
            tasksList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Congratulation", Toast.LENGTH_SHORT).show();
            ((MainActivity) context).saveTasks();
        });

        check2();

        return convertView;
    }

    private void check2(){
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(context, "Task Completed: " + task, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
