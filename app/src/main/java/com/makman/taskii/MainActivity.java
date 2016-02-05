package com.makman.taskii;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener{
    final static String tag = "MAIN";
    Button prev, save, next;
    EditText enter;
    int day;
    String today;
    String[] days;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Spinner spin;
    ArrayAdapter<CharSequence> spinAdapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prev = (Button) findViewById(R.id.main_button_prev);
        save = (Button) findViewById(R.id.main_button_save);
        next = (Button) findViewById(R.id.main_button_next);
        enter = (EditText) findViewById(R.id.main_edit_text);
        spin = (Spinner) findViewById(R.id.main_spinner);
        spinAdapt = ArrayAdapter.createFromResource(this, R.array.week_days,  android.R.layout.simple_spinner_item);
        spinAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(spinAdapt);
        spin.setOnItemSelectedListener(this);
        prev.setOnClickListener(this);
        save.setOnClickListener(this);
        next.setOnClickListener(this);
        enter.setOnEditorActionListener(this);
        enter.setLines(10);
        enter.setHorizontallyScrolling(false);
        preferences = getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        days = getResources().getStringArray(R.array.week_days);
        setTodayStrings();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){

            case R.id.main_button_prev:
                day = (day - 1 + days.length)%days.length;
                enter.setText("");
                setTodayStrings();
                break;

            case R.id.main_button_save:
                Toast.makeText(this, getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
                editor.putString(today, enter.getText().toString());
                editor.apply();
                enter.setText("");
                setTodayStrings();
                break;

            case R.id.main_button_next:
                day = (day + 1)%days.length;
                enter.setText("");
                setTodayStrings();
                break;
        }
    }

    private void setTodayStrings(){
        today = days[(day)%days.length];
        String display = getString(R.string.free_day);
        display =  String.format(display, today);
        display = display +"\n" + (preferences.getString(today, ""));
        enter.setHint(display);
        prev.setText(days[(day - 1 + days.length) % days.length]);
        next.setText(days[(day+1)%days.length]);
        spin.setSelection(day%days.length);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Toast.makeText(this, getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
            editor.putString(today, enter.getText().toString());
            editor.apply();
            enter.setText("");
            setTodayStrings();
            handled = true;
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0,0);
        }
        return handled;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        day = position;
        setTodayStrings();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
