package com.sooksinyip.weightwatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class AddFoodActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Intent i = this.getIntent();
        if (i.hasExtra("code")) {
            String code = i.getStringExtra("code");
            int credit = i.getIntExtra("credit", 0);
            String grade = i.getStringExtra("grade");

            EditText etCode = (EditText)findViewById(R.id.etCode);
            etCode.setText(code);

            EditText etCR = (EditText)findViewById(R.id.etCR);
            etCR.setText(Integer.toString(credit));

            RadioGroup rgGrade = (RadioGroup)findViewById(R.id.rgGrade);
            if (grade.equals("A")) {
                rgGrade.check(R.id.rbA);
            }
            else if (grade.equals("B+")) {
                rgGrade.check(R.id.rbBP);
            }
            else if (grade.equals("B")) {
                rgGrade.check(R.id.rbB);
            }
            else if (grade.equals("C+")) {
                rgGrade.check(R.id.rbCP);
            }
            else if (grade.equals("C")) {
                rgGrade.check(R.id.rbC);
            }
            else if (grade.equals("D+")) {
                rgGrade.check(R.id.rbDP);
            }
            else if (grade.equals("D")) {
                rgGrade.check(R.id.rbD);
            }
            else {
                rgGrade.check(R.id.rbF);
            }

            Button btAdd = (Button)findViewById(R.id.btAdd);
            btAdd.setText("Edit Course");
        }
    }

    public void addClicked(View v) {
        EditText etCode = (EditText)findViewById(R.id.etCode);
        EditText etCR = (EditText)findViewById(R.id.etCR);
        RadioGroup rg = (RadioGroup)findViewById(R.id.rgGrade);

        String sCode = etCode.getText().toString();
        String sCR = etCR.getText().toString();

        if (sCode.trim().length() == 0 || sCR.trim().length() == 0) {
            Toast t = Toast.makeText(this.getApplicationContext(),
                    "Both course code and credit are necessary.",
                    Toast.LENGTH_SHORT);
            t.show();
        }
        else {
            Intent result = new Intent();
            result.putExtra("code", sCode);
            result.putExtra("credit", Integer.valueOf(sCR));
            int rID = rg.getCheckedRadioButtonId();
            String grade = ((RadioButton)findViewById(rID)).getText().toString();
            result.putExtra("grade", grade);
            this.setResult(RESULT_OK, result);
            this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_course, menu);
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
}
