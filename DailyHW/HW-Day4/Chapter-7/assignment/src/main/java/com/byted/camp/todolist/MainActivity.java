package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.db.entity.NoteE;
import com.byted.camp.todolist.ui.NoteListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * TODOLIST 主页面
 * @author YDJSIR
 * @date 2021-07-18
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;

    private TodoDbHelper todoDbHelper;
    public static final String TAG = "TODOLIST";

    private Note curNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, NoteActivity.class), REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
                notesAdapter.refresh(loadNotesFromDatabase()); // 删除完毕要更新
                Toast.makeText(MainActivity.this,
                        "Note Deleted.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
                notesAdapter.refresh(loadNotesFromDatabase()); // 删除完毕要更新
            }
        });
        todoDbHelper = new TodoDbHelper(this);
        recyclerView.setAdapter(notesAdapter);
        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_debug:
                return true;
            case R.id.action_database:
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    /**
     * 从数据库中查询数据，并转换成 JavaBeans类
     * @return note列表
     */
    private List<Note> loadNotesFromDatabase() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = todoDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                TodoContract.FeedEntry.COLUMN_NAME_DATE,
                TodoContract.FeedEntry.COLUMN_NAME_STATE,
                TodoContract.FeedEntry.COLUMN_NAME_CONTENT,
                TodoContract.FeedEntry.COLUMN_NAME_PRIORITY
        };
        String sortOrder =
                TodoContract.FeedEntry.COLUMN_NAME_PRIORITY + " DESC, " + TodoContract.FeedEntry.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.query(
                TodoContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        Log.i(TAG, "Perform query data:");
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(TodoContract.FeedEntry._ID));
            long date = cursor.getLong(cursor.getColumnIndex(TodoContract.FeedEntry.COLUMN_NAME_DATE));
            int state = cursor.getInt(cursor.getColumnIndex(TodoContract.FeedEntry.COLUMN_NAME_STATE));
            String content = cursor.getString(cursor.getColumnIndex(TodoContract.FeedEntry.COLUMN_NAME_CONTENT));
            int priority = cursor.getInt(cursor.getColumnIndex(TodoContract.FeedEntry.COLUMN_NAME_PRIORITY));
            Note newNote = new Note(id, date, state, content, priority);
            notes.add(newNote);
        }
        cursor.close();
        return notes;
    }

    /**
     * 删除note
     * @param note 对应Beans对象
     */
    private void deleteNote(Note note) {
        SQLiteDatabase db = todoDbHelper.getWritableDatabase();
        String selection = TodoContract.FeedEntry._ID + " = ?"; // 根据主键进行查找
        String[] selectionArgs = {String.valueOf(note.id)};
        int deletedRows = db.delete(TodoContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        Log.i(TAG, "Perform delete data, result:" + deletedRows);
    }

    /**
     * 设置已读未读
     * @param note 对应beans对象
     */
    private void updateNode(Note note) {
        SQLiteDatabase db = todoDbHelper.getWritableDatabase();
//        int state = (note.getState().intValue == 0? 1 : 0);
        int state = note.getState().intValue;
        ContentValues values = new ContentValues();
        values.put(TodoContract.FeedEntry.COLUMN_NAME_STATE, state);
        String selection = TodoContract.FeedEntry._ID + " = ?"; // 根据主键进行查找
        String[] selectionArgs = {String.valueOf(note.id)};
        int count = db.update(
                TodoContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        Log.i(TAG, "Perform update data, result:" + count);
    }
}
