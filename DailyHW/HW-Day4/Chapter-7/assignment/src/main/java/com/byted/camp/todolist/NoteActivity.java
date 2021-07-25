package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;

/**
 * 新增Note项目页面
 * @author YDJSIR
 * @date 2021-07-18
 */
public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private RadioGroup priorities;
    private TodoDbHelper todoDbHelper;
    int priority = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);
        todoDbHelper = new TodoDbHelper(this);
        editText = findViewById(R.id.edit_text);
        priorities = findViewById(R.id.priority);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        priorities.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.trivial:
                        priority = 0;
                        break;
                    case R.id.normal:
                        priority = 1;
                        break;
                    case R.id.important:
                        priority = 2;
                        break;
                    default:
                        break;
                }
            }
        });

        addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(v -> {
            CharSequence content = editText.getText();

            if (TextUtils.isEmpty(content)) {
                Toast.makeText(NoteActivity.this,
                        "No content to add.", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean succeed = saveNote2Database(content.toString().trim(), priority);
            if (succeed) {
                Toast.makeText(NoteActivity.this,
                        "Note added.", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
            } else {
                Toast.makeText(NoteActivity.this,
                        "Error!", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 插入新的待办项目
     * @param content 新待办项内容
     * @return 返回插入结果
     */
    private boolean saveNote2Database(String content, int priority) {
        SQLiteDatabase db = todoDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoContract.FeedEntry.COLUMN_NAME_STATE, 0);
        contentValues.put(TodoContract.FeedEntry.COLUMN_NAME_DATE, System.currentTimeMillis());
        contentValues.put(TodoContract.FeedEntry.COLUMN_NAME_CONTENT, content);
        contentValues.put(TodoContract.FeedEntry.COLUMN_NAME_PRIORITY, priority);
        long newRowId = db.insert(TodoContract.FeedEntry.TABLE_NAME, null, contentValues);
        return newRowId != -1;
    }
}
