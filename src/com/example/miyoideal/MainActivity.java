package com.example.miyoideal;

import java.util.HashMap;

import com.example.miyoideal.widget.SQLiteUserDataBase;

import android.support.v7.app.ActionBarActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
	
	//layout components
	private Button button;
	
	//global variables;
	private int width;
	private int height;
	
	//database
	private SQLiteUserDataBase userDB;
	private ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Diagnostico");
        
        //Calculates Screen size
    	Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
        
        button = (Button) findViewById(R.id.button1);
        
        userDB = new SQLiteUserDataBase(this);
		
		userDB.getReadableDatabase();
		String query = "Select * FROM " + "users" + " WHERE " + "id" + " =  \"" + "1" + "\"";
		values = new ContentValues();
		Cursor cursor = userDB.getReadableDatabase().rawQuery(query, null);
		if(cursor.moveToFirst())
		{
			Intent intent = new Intent(MainActivity.this, HomeActivity.class);
			startActivity(intent);
		}

		userDB.getReadableDatabase();
        
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userDB.getWritableDatabase();
				values.put("id", "1");
				values.put("name", "Hugo");
				values.put("weight", "85 kg");
				userDB.getWritableDatabase().insert("users", null, values);
				userDB.close();
				Intent intent = new Intent(MainActivity.this, HomeActivity.class);
				startActivity(intent);
			}
		});
        
        
		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
