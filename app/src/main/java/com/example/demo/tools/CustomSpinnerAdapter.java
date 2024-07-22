package com.example.demo.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demo.R;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> mValues;

    public CustomSpinnerAdapter(Context context, List<String> values) {
        super(context, R.layout.custom_spinner, values);
        this.mContext = context;
        this.mValues = values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.custom_spinner, parent, false);
        }

        TextView textView = view.findViewById(R.id.custom_spinner_item_text);
        textView.setText(mValues.get(position));

        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);

    }
}

