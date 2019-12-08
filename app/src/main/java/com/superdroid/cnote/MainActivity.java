package com.superdroid.cnote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MemoData> mArrayList;
    private MemoAdapter mAdapter;
    private MemoGridAdapter mGridAdapter;
    private RecyclerView mRecyclerView;

    private ColorBox colorBox;

    private int viewMode = 0;

    SQLiteDatabase sampleDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.listView);

        colorBox = new ColorBox(this);

        try {
            sampleDB = this.openOrCreateDatabase("cnote", MODE_PRIVATE, null);
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + "memo"
                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT, date TEXT, color INTEGER, type INTEGER, done INTEGER, pwd TEXT );");
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + "item"
                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT, mid INTEGER, content TEXT, done INTEGER );");

        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MemoData memoData = mArrayList.get(position);
                if (memoData.getType() == 0) {
                    Intent intent = new Intent(getBaseContext(), NoteActivity.class);
                    intent.putExtra("id", memoData.getId());
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), CheckListActivity.class);
                    intent.putExtra("id", memoData.getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuitem_bottombar_1:
                createDialog();
                return true;
            case R.id.menuitem_bottombar_2:
                viewMode = 1 - viewMode;
                showList();
                if (viewMode == 0) {
                    item.setIcon(R.drawable.ic_view_list_24px);
                }
                else {
                    item.setIcon(R.drawable.ic_apps_24px);
                }
                return true;
        }
        super.onOptionsItemSelected(item);
        return false;
    }

    private void createDialog() {
        final CharSequence[] oItems = {"문서", "체크리스트"};

        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);

        oDialog.setTitle("노트 추가")
                .setItems(oItems, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (oItems[which].toString().compareTo("문서") == 0) {
                            Intent intent = new Intent(getBaseContext(), NoteActivity.class);
                            intent.putExtra("id", -1);
                            startActivity(intent);
                        }
                        if (oItems[which].toString().compareTo("체크리스트") == 0) {
                            Intent intent = new Intent(getBaseContext(), CheckListActivity.class);
                            intent.putExtra("id", -1);
                            startActivity(intent);
                        }
                    }
                })
                .show();
    }

    protected void showList(){
        if (viewMode == 0) {
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mArrayList = new ArrayList<>();
            mAdapter = new MemoAdapter(mArrayList);
            mRecyclerView.setAdapter(mAdapter);
            try {
                Cursor c = sampleDB.rawQuery("SELECT * FROM " + "memo", null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            MemoData memoData = new MemoData();
                            memoData.setId(c.getInt(c.getColumnIndex("id")));
                            memoData.setTitle(c.getString(c.getColumnIndex("title")));
                            memoData.setDate(c.getString(c.getColumnIndex("date")));
                            memoData.setColor(colorBox.getColor(c.getInt(c.getColumnIndex("color"))));
                            memoData.setLightColor(colorBox.getLightColor(c.getInt(c.getColumnIndex("color"))));
                            memoData.setType(c.getInt(c.getColumnIndex("type")));
                            memoData.setDone(c.getInt(c.getColumnIndex("done")));
                            memoData.setPwd(c.getString(c.getColumnIndex("pwd")));
                            if (memoData.getDone() == 0) {
                                memoData.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                            }
                            else {
                                memoData.setTextColor(colorBox.getColor(7));
                            }
                            mArrayList.add(memoData);
                        } while (c.moveToNext());
                    }
                }
            } catch (SQLiteException se) {
                Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("",  se.getMessage());
            }
        }
        else {
            LinearLayoutManager mGridLayoutManager = new GridLayoutManager(this, 3);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mArrayList = new ArrayList<>();
            mGridAdapter = new MemoGridAdapter(mArrayList);
            mRecyclerView.setAdapter(mGridAdapter);
            try {
                Cursor c = sampleDB.rawQuery("SELECT * FROM " + "memo", null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {
                            MemoData memoData = new MemoData();
                            memoData.setId(c.getInt(c.getColumnIndex("id")));
                            memoData.setTitle(c.getString(c.getColumnIndex("title")));
                            memoData.setDate(c.getString(c.getColumnIndex("content")));
                            memoData.setColor(colorBox.getColor(c.getInt(c.getColumnIndex("color"))));
                            memoData.setLightColor(colorBox.getLightColor(c.getInt(c.getColumnIndex("color"))));
                            memoData.setType(c.getInt(c.getColumnIndex("type")));
                            memoData.setDone(c.getInt(c.getColumnIndex("done")));
                            memoData.setPwd(c.getString(c.getColumnIndex("pwd")));
                            if (memoData.getDone() == 0) {
                                memoData.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                            }
                            else {
                                memoData.setTextColor(colorBox.getColor(7));
                            }

                            if (memoData.getType() == 1) {
                                Cursor c2 = sampleDB.rawQuery("SELECT * FROM " + "item" + " WHERE mid = " + memoData.getId(), null);
                                if (c2 != null) {
                                    String str = "";
                                    if (c2.moveToFirst()) {
                                        do {
                                            str = str + "ㆍ" + c2.getString(c2.getColumnIndex("content")) + "\n";
                                        } while (c2.moveToNext());
                                    }
                                    memoData.setDate(str);
                                }
                            }

                            mArrayList.add(memoData);
                        } while (c.moveToNext());
                    }
                }
            } catch (SQLiteException se) {
                Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("",  se.getMessage());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sampleDB.close();
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
