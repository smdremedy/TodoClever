package com.soldiersofmobile.todoekspert.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import com.soldiersofmobile.todoekspert.R;
import com.soldiersofmobile.todoekspert.api.Todo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by madejs on 19.05.17.
 */

public class TodoAdapter extends BaseAdapter {

    private ArrayList<Todo> todos = new ArrayList<>();


    public void addAll(Todo... todoArray) {
        for (Todo todo : todoArray) {
            todos.add(todo);
        }

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Todo getItem(int position) {
        return todos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
            view.setTag(new ViewHolder(view));
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Todo todo = getItem(position);
        viewHolder.itemCheckbox.setChecked(todo.done);
        viewHolder.itemCheckbox.setText(todo.content);
        viewHolder.itemButton.setText(todo.objectId);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.item_checkbox)
        CheckBox itemCheckbox;
        @BindView(R.id.item_button)
        Button itemButton;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
