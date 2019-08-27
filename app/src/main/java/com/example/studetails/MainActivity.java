package com.example.studetails;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout Rollno, Name, Age, Phone, Email;
    private Button Insert, ViewAll, View;
    private RadioGroup radioGender, radioName;
    private RadioButton radioButton, genderButton;
    private CheckBox cbiot, cbrobo, cbai, cbml;

    private SQLiteDatabase db;
    int getSelected;
    int getGetSelected1;
    String text="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Rollno=findViewById(R.id.main_roll);
        Name=findViewById(R.id.main_name);
        Age=findViewById(R.id.main_age);
        Phone=findViewById(R.id.main_cell);
        Email=findViewById(R.id.main_email);
        Insert=(Button)findViewById(R.id.main_insert);
        ViewAll=(Button)findViewById(R.id.main_viewAll);
        View = (Button)findViewById(R.id.main_view);

        radioName = (RadioGroup)findViewById(R.id.radioName);
        radioGender = (RadioGroup)findViewById(R.id.radioGender);
        radioName.clearCheck();
        radioGender.clearCheck();

        cbiot = (CheckBox) findViewById(R.id.cbiot);
        cbrobo = (CheckBox) findViewById(R.id.cbrobo);
        cbai = (CheckBox) findViewById(R.id.cbai);
        cbml = (CheckBox) findViewById(R.id.cbml);

        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR, name VARCHAR,age VARCHAR, phone VARCHAR, email VARCHAR, fullname VARCHAR, gender VARCHAR, subject VARCHAR);");

        Insert.setOnClickListener(this);
        ViewAll.setOnClickListener(this);
        View.setOnClickListener(this);
    }

    public void onClick(View view)
    {

        if(cbiot.isChecked())
            text = text + " IoT ";
        if(cbrobo.isChecked())
            text = text + " Robotics ";
        if(cbai.isChecked())
            text = text + " AI ";
        if(cbml.isChecked())
            text = text + " ML ";

        // Inserting a record to the Student table
        if(view==Insert)
        {

            getSelected = radioName.getCheckedRadioButtonId();
            getGetSelected1 = radioGender.getCheckedRadioButtonId();

            radioButton = (RadioButton)findViewById(getSelected);
            genderButton = (RadioButton)findViewById(getGetSelected1);
            // Checking for empty fields
            if(Rollno.getEditText().getText().toString().trim().length()==0||
                    Name.getEditText().getText().toString().trim().length()==0||
                    Age.getEditText().getText().toString().trim().length()==0 ||
                    Phone.getEditText().getText().toString().trim().length()==0||
                    Email.getEditText().getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO student VALUES('"+Rollno.getEditText().getText()+"','"+Name.getEditText().getText()+
                    "','"+Age.getEditText().getText()+"','"+Phone.getEditText().getText()+"','"+Email.getEditText().getText()+
                    "','"+radioButton.getText()+"','"+genderButton.getText()+"','"+text+"');");
            showMessage("Success", "Record added");
            clearText();
        }

        // Display a record from the Student table
        if(view==View)
        {
            // Checking for empty roll number
            if(Rollno.getEditText().getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+Rollno.getEditText().getText()+"'", null);
            if(c.moveToFirst())
            {
                Name.getEditText().setText(c.getString(5) + c.getString(1));
                Age.getEditText().setText(c.getString(2));
                Phone.getEditText().setText(c.getString(3));
                Email.getEditText().setText(c.getString(4));
            }
            else
            {
                showMessage("Error", "Invalid Rollno");
                clearText();
            }
        }

        // Displaying all the records
        if(view==ViewAll)
        {
            Cursor c=db.rawQuery("SELECT * FROM student", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("Rollno: "+c.getString(0)+"\n");
                buffer.append("Name: "+c.getString(5)+" "+c.getString(1)+"\n");
                buffer.append("Age: "+c.getString(2)+"\n");
                buffer.append("Phone: "+c.getString(3)+"\n");
                buffer.append("Email: "+c.getString(4)+"\n");
                buffer.append("Gender: "+c.getString(6)+"\n");
                buffer.append("Subjects: "+c.getString(7)+"\n\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText()
    {
        Rollno.getEditText().setText("");
        Name.getEditText().setText("");
        Age.getEditText().setText("");
        Phone.getEditText().setText("");
        Email.getEditText().setText("");
        radioName.clearCheck();
        radioGender.clearCheck();
        cbiot.setChecked(false);
        cbrobo.setChecked(false);
        cbai.setChecked(false);
        cbml.setChecked(false);
        Phone.clearFocus();
    }
}
