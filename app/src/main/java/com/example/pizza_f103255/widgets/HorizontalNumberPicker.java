package com.example.pizza_f103255.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.pizza_f103255.R;

public class HorizontalNumberPicker extends LinearLayout {
    private EditText number;
    private int min;
    private int max;

    public HorizontalNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.horizontal_numberpicker, this);

        number = findViewById(R.id.number);

        Button btnDown = findViewById(R.id.btn_down);
        btnDown.setOnClickListener(v -> changeValue(-1));

        Button btnUp = findViewById(R.id.btn_up);
        btnUp.setOnClickListener(v -> changeValue(1));
    }

    public int getValue() {
        if (number != null) {
            String value = number.getText().toString();
            return Integer.parseInt(value);
        }
        return 0;
    }

    public void setValue(final int value) {
        if (number != null) {
            number.setText(String.valueOf(value));
        }
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    private void changeValue(int diff) {
        int newValue = getValue() + diff;
        if (newValue < min) {
            newValue = min;
        } else if (newValue > max) {
            newValue = max;
        }
        number.setText(String.valueOf(newValue));
    }
}
