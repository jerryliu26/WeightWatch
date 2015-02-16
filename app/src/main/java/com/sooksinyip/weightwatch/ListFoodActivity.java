package com.sooksinyip.weightwatch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class ListFoodActivity extends ActionBarActivity
        implements AdapterView.OnItemLongClickListener, ActionMode.Callback {

    FoodDBHelper helper;
    SimpleCursorAdapter adapter;
    long selectedId;
    ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course);

        helper = new FoodDBHelper(this.getApplicationContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, code, " +
                "(grade || ' (' || credit || ' credits)') gc FROM course;", null);

        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[] {"code", "gc"},
                new int[] {android.R.id.text1, android.R.id.text2},
                0);
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(this);
        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        // Save the selected
        selectedId = id;
        // Start the ActionMode with an item is long-clicked
        actionMode = this.startActionMode(this);
        return true;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_actionmode, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    private void deleteClicked() {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowCount = db.delete("course", "_id = ?",
                new String[]{Long.toString(selectedId)});
        if (rowCount == 1) {
            Toast t = Toast.makeText(this.getApplicationContext(),
                    "Deleted one course", Toast.LENGTH_SHORT);
            t.show();
        }
        else {
            Toast t = Toast.makeText(this.getApplicationContext(),
                    "Unable to delete the course",
                    Toast.LENGTH_SHORT);
            t.show();
        }
        Cursor cursor = db.rawQuery("SELECT _id, code, " +
                "(grade || ' (' || credit || ' credits)') gc FROM course;", null);
        adapter.changeCursor(cursor);
        db.close();
    }

    private void editClicked() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM course WHERE _id=?;",
                new String[]{Long.toString(selectedId)});
        if (c.getCount() == 1) {
            // Retrieved exactly one row
            c.moveToFirst();
            String code = c.getString(c.getColumnIndex("code"));
            int credit = c.getInt(c.getColumnIndex("credit"));
            String grade = c.getString(c.getColumnIndex("grade"));
            Intent i = new Intent(this, AddFoodActivity.class);
            i.putExtra("code", code);
            i.putExtra("credit", credit);
            i.putExtra("grade", grade);
            startActivityForResult(i, 77);
        }
        else {
            // Unable to get the selected id
            Toast t = Toast.makeText(this.getApplicationContext(),
                    "Unable to retrieve the selected course",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                deleteClicked();
                mode.finish();
                break;
            case R.id.menu_edit:
                editClicked();
                mode.finish();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 77) {
            if (resultCode == RESULT_OK) {
                String code = data.getStringExtra("code");
                int credit = data.getIntExtra("credit", 0);
                String grade = data.getStringExtra("grade");

                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues r = new ContentValues();
                r.put("code", code);
                r.put("credit", credit);
                r.put("grade", grade);
                r.put("value", MainActivity.gradeToValue(grade));
                long newId = db.update("course", r, "_id = ?",
                        new String[]{Long.toString(selectedId)});

                if (newId != -1) {
                    Toast t = Toast.makeText(this.getApplicationContext(),
                            "Successfully updated the course",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
                else {
                    Toast t = Toast.makeText(this.getApplicationContext(),
                            "Unable to update the course",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
                Cursor cursor = db.rawQuery("SELECT _id, code, " +
                        "(grade || ' (' || credit || ' credits)') gc FROM course;", null);
                adapter.changeCursor(cursor);
                db.close();
            }
        }

        Log.d("course", "onActivityResult");
    }
}
