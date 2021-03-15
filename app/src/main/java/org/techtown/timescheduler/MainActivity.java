package org.techtown.timescheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    String mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button btn = findViewById(R.id.recordTime);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNow = System.currentTimeMillis();
                mDate = new Date(mNow);
                mTime = mFormat.format(mDate);

                insertPerson();
            }
        });

        Button btn2 = findViewById(R.id.query);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPerson();
            }
        });
    }

    public void insertPerson() {

        println("insertPerson 호출됨");

        String uriString = "content://org.techtown.timescheduler/person";
        Uri uri = new Uri.Builder().build().parse(uriString);

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String[] columns = cursor.getColumnNames();


        ContentValues values = new ContentValues();
        values.put("time", mTime);
        values.put("content", "STUDY");

        uri = getContentResolver().insert(uri, values);
        println("insert 결과 -> " + uri.toString());
    }

    public void queryPerson() {
        try {
            String uriString = "content://org.techtown.timescheduler/person";
            Uri uri = new Uri.Builder().build().parse(uriString);

            String[] columns = new String[] {"time", "content"};
            Cursor cursor = getContentResolver().query(uri, columns, null, null, "time ASC");
            println("query 결과 : " + cursor.getCount());

            int index = 0;
            while(cursor.moveToNext()) {
                String time = cursor.getString(cursor.getColumnIndex(columns[0]));
                String content = cursor.getString(cursor.getColumnIndex(columns[1]));

                println("#" + index + " -> " + time + ", " +  content);
                index += 1;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePerson() {
        String uriString = "content://org.techtown.timescheduler/person";
        Uri uri = new Uri.Builder().build().parse(uriString);

        String selection = "mobile = ?";
        String[] selectionArgs = new String[] {"010-1000-1000"};
        ContentValues updateValue = new ContentValues();
        updateValue.put("mobile", "010-2000-2000");

        int count = getContentResolver().update(uri, updateValue, selection, selectionArgs);
        println("update 결과 : " + count);
    }

    public void deletePerson() {
        String uriString = "content://org.techtown.timescheduler/person";
        Uri uri = new Uri.Builder().build().parse(uriString);

        String selection = "name = ?";
        String[] selectionArgs = new String[] {"john"};

        int count = getContentResolver().delete(uri, selection, selectionArgs);
        println("delete 결과 : " + count);
    }

    public void println(String data) {
        textView.append(data + "\n");
    }
}