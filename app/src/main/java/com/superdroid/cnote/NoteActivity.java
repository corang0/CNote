package com.superdroid.cnote;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    private CustomDialog customDialog;
    private LinearLayout titleBar;
    private EditText titleText;
    private TextView dateText;
    private LinedEditText contentText;
    private ImageView backButton;
    private ImageView editButton;
    private ImageView checkButton;
    private ImageView deleteButton;
    private ImageView lockButton;
    private ColorBox colorBox;

    private int mode = 0;
    private int code = 0;
    private int check = 0;
    private int lock = 0;
    private int id;

    SQLiteDatabase sampleDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");

        titleBar = (LinearLayout) findViewById(R.id.titleBar);
        titleText = (EditText) findViewById(R.id.titleText);
        dateText = (TextView) findViewById(R.id.dateText);
        contentText = (LinedEditText) findViewById(R.id.contentText);

        colorBox = new ColorBox(this);
        customDialog = new CustomDialog(this, colorListener);
        sampleDB = this.openOrCreateDatabase("cnote", MODE_PRIVATE, null);

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mode == 0) {
                        if (id == -1 && titleText.getText().toString().length() > 0) {
                            insertQuery();
                            mode = 1;
                            changeMode();
                        }
                        else if (id == -1 && titleText.getText().toString().length() <= 0) {
                            finish();
                        }
                        else {
                            if (titleText.getText().toString().length() <= 0) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( "(yy/MM/dd)");
                                titleText.setText(simpleDateFormat.format(System.currentTimeMillis()));
                                updateQuery(simpleDateFormat.format(System.currentTimeMillis()));
                            }
                            else {
                                updateQuery(titleText.getText().toString());
                            }

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( "yy/MM/dd a HH:mm");
                            dateText.setText(simpleDateFormat.format(System.currentTimeMillis()));
                            mode = 1;
                            changeMode();
                        }
                    }
                    else {
                        finish();
                    }
                } catch (SQLiteException se) {
                    Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("", se.getMessage());
                }
            }
        });

        editButton = (ImageView) findViewById(R.id.editButton);
        editButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode == 0) {
                    customDialog.show();
                }
                else {
                    mode = 0;
                    changeMode();
                }
            }
        });

        checkButton = (ImageView) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = 1 - check;
                applyCheck();
                updateQuery(titleText.getText().toString());
            }
        });

        deleteButton = (ImageView) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        lockButton = (ImageView) findViewById(R.id.lockButton);
        lockButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });

        if (id != -1) {
            loadData();
            mode = 1;
            changeMode();
        }
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yy/MM/dd a HH:mm");
            String date = simpleDateFormat.format(System.currentTimeMillis());
            dateText.setText(date);
            changeMode();
        }

        applyCheck();
    }

    private View.OnClickListener colorListener = new View.OnClickListener() {
        public void onClick(View v) {
            TextView textView = (TextView) v;
            customDialog.dismiss();

            code = Integer.parseInt(textView.getText().toString());
            titleBar.setBackgroundColor(colorBox.getColor(code));
            contentText.setLineColor(colorBox.getColor(code));
            contentText.setBackgroundColor(colorBox.getLightColor(code));
            editButton.setBackgroundColor(colorBox.getLightColor(code));
            dateText.setBackgroundColor(colorBox.getLightColor(code));
        }
    };

    protected void loadData(){
        try {
            Cursor c = sampleDB.rawQuery("SELECT * FROM " + "memo" + " WHERE id = " + id, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        titleText.setText(c.getString(c.getColumnIndex("title")));
                        dateText.setText(c.getString(c.getColumnIndex("date")));
                        contentText.setText(c.getString(c.getColumnIndex("content")));
                        code = c.getInt(c.getColumnIndex("color"));
                        check = c.getInt(c.getColumnIndex("done"));
                        titleBar.setBackgroundColor(colorBox.getColor(code));
                        contentText.setLineColor(colorBox.getColor(code));
                        contentText.setBackgroundColor(colorBox.getLightColor(code));
                        dateText.setBackgroundColor(colorBox.getLightColor(code));
                        checkPassword(c.getString(c.getColumnIndex("pwd")));
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }
    }

    private void createDialog() {
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);

        oDialog.setMessage("노트를 삭제하시겠습니까?")
                .setTitle("삭제")
                .setPositiveButton("확인", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        sampleDB.execSQL("DELETE FROM " + "memo"
                                + " WHERE id = " + id + ";");
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                })
                .show();
    }

    private void changeMode() {
        if (mode == 0) {
            titleText.setFocusable(true);
            titleText.setClickable(true);
            titleText.setFocusableInTouchMode(true);
            titleText.setBackgroundColor(colorBox.getLightColor(8));
            contentText.setFocusable(true);
            contentText.setClickable(true);
            contentText.setFocusableInTouchMode(true);
            backButton.setImageResource(R.drawable.ic_check_24px);
            editButton.setImageResource(0);
            editButton.setBackgroundColor(colorBox.getLightColor(code));
        }
        else {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(titleText.getWindowToken(), 0);
            titleText.setFocusable(false);
            titleText.setClickable(false);
            titleText.setBackgroundColor(colorBox.getColor(code));
            contentText.setFocusable(false);
            contentText.setClickable(false);
            backButton.setImageResource(R.drawable.ic_24px_black);
            editButton.setImageResource(R.drawable.ic_create_24px);
            editButton.setBackgroundColor(0);
        }
    }

    private void applyCheck() {
        if (check == 0) {
            titleText.setPaintFlags(0);
            titleText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            contentText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            checkButton.setImageResource(R.drawable.ic_check_box_24px);
        }
        else {
            titleText.setPaintFlags(titleText.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            titleText.setTextColor(colorBox.getColor(7));
            contentText.setTextColor(colorBox.getColor(7));
            checkButton.setImageResource(R.drawable.ic_check_box_outline_blank_24px);
        }
    }

    private void insertQuery() {
        String title = titleText.getText().toString();
        String content = contentText.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( "yy/MM/dd a HH:mm");
        String date = simpleDateFormat.format(System.currentTimeMillis());
        int color = code;
        int type = 0;
        int done = check;

        sampleDB.execSQL("INSERT INTO " + "memo"
                + " (title, content, date, color, type, done)  Values ('" + title + "', '" + content + "', '" + date + "', '" + color +  "', '" + type +  "', '" + done + "');");

        try {
            Cursor c = sampleDB.rawQuery("SELECT * FROM " + "memo" + " WHERE date = " + "'" + date + "'", null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        id = c.getInt(c.getColumnIndex("id"));
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }
    }

    private void updateQuery(String title) {
        String content = contentText.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( "yy/MM/dd a HH:mm");
        String date = simpleDateFormat.format(System.currentTimeMillis());
        int color = code;
        int type = 0;
        int done = check;

        sampleDB.execSQL("UPDATE " + "memo" + " SET "
                + "title = '" + title + "', content = '" + content + "', date = '" + date + "', color = '" + color +  "', type = '" + type +  "', done = '" + done + "'"
                + " WHERE id = " + id + ";");
    }

    private void setPassword() {
        if (lock == 0) {
            final EditText editText = new EditText(NoteActivity.this);

            AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);

            oDialog.setTitle("암호를 입력하세요")
                    .setView(editText)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pwd = editText.getText().toString();

                            sampleDB.execSQL("UPDATE " + "memo" + " SET "
                                    + "pwd = '" + pwd + "'"
                                    + " WHERE id = " + id + ";");

                            lock = 1;
                            lockButton.setImageResource(R.drawable.ic_lock_open_24px);

                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        else {
            AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);

            oDialog.setMessage("정말 잠금을 해제하시겠습니까?")
                    .setTitle("잠금 해제")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            sampleDB.execSQL("UPDATE " + "memo" + " SET "
                                    + "pwd = NULL"
                                    + " WHERE id = " + id + ";");
                            lock = 0;
                            lockButton.setImageResource(R.drawable.ic_lock_24px);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                        }
                    })
                    .show();
        }
    }

    private void checkPassword(String pwd) {
        final String key = pwd;
        if (key != null) {
            lock = 1;
            lockButton.setImageResource(R.drawable.ic_lock_open_24px);
            final String content = contentText.getText().toString();
            contentText.setText("");
            final EditText editText = new EditText(NoteActivity.this);

            AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);

            oDialog.setTitle("암호를 입력하세요")
                    .setView(editText)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String input = editText.getText().toString();
                            if (input.compareTo(key) == 0) {
                                dialog.dismiss();
                                contentText.setText(content);
                            }
                            else {
                                finish();
                            }
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }
}
