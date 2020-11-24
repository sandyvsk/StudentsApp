package com.example.studentsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button btnnext;
    TextView txtsignup;
    EditText usrname,phoneno,password,cnfpswd;
    String NameHolder, PhonenoHolder, PasswordHolder,CnfmPasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String F_Result = "Not_Found";
    UserSessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initial();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initial();
        Log.d("lifecycle","onStart invoked");
    }

    private void initial() {

        btnnext=findViewById(R.id.next2);

        txtsignup=findViewById(R.id.signup2);

        usrname=findViewById(R.id.editTextTextPersonName);

        phoneno=findViewById(R.id.editTextTextPhno);

        password=findViewById(R.id.editTextTextPassword);

        cnfpswd=findViewById(R.id.editTextTextxCnfmPswd);

        sqLiteHelper = new SQLiteHelper(this);

        // User Session Manager
        sessionManager = new UserSessionManager(getApplicationContext());

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
            }
        });

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    private void register()
    {
        // Creating SQLite database if dose n't exists
        SQLiteDataBaseBuild();

        // Creating SQLite table if dose n't exists.
        SQLiteTableBuild();

        // Checking EditText is empty or Not.
        CheckEditTextStatus();

        // Method to check Name is already exists or not.
        CheckUsernameExists();

        // Empty EditText After done inserting process.
        EmptyEditTextAfterDataInsert();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked");
        finish();
    }

    // SQLite database build method.
    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    // SQLite table build method.
    public void SQLiteTableBuild() {

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" + SQLiteHelper.Table_Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Phone + " VARCHAR, " + SQLiteHelper.Table_Column_3_Password + " VARCHAR);");

    }

    // Insert data into SQLite database method.
    public void InsertDataIntoSQLiteDatabase(){

        // If editText is not empty then this block will executed.
        if(EditTextEmptyHolder)
        {

            // SQLite query to insert data into table.
            SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (name,phone,password) VALUES('"+NameHolder+"', '"+PhonenoHolder+"', '"+PasswordHolder+"');";

            // Executing query.
            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            // Closing SQLite database object.
            sqLiteDatabaseObj.close();

            sessionManager.createUserLoginSession(NameHolder,
                    PhonenoHolder);

            // Printing toast message after done inserting.
            Toast.makeText(RegisterActivity.this,"User Registered Successfully", Toast.LENGTH_LONG).show();

            // Starting MainActivity
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);

            finish();

        }
        // This block will execute if any of the registration EditText is empty.
        else {

            // Printing toast message if any of EditText is empty.
            Toast.makeText(RegisterActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }

    }

    // Empty edittext after done inserting process method.
    public void EmptyEditTextAfterDataInsert(){

        usrname.getText().clear();

        phoneno.getText().clear();

        password.getText().clear();

        cnfpswd.getText().clear();

    }

    // Method to check EditText is empty or Not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        NameHolder = usrname.getText().toString() ;
        PhonenoHolder = phoneno.getText().toString();
        PasswordHolder = password.getText().toString();
        CnfmPasswordHolder = cnfpswd.getText().toString();

        if(TextUtils.isEmpty(NameHolder) || TextUtils.isEmpty(PhonenoHolder) || TextUtils.isEmpty(PasswordHolder)|| TextUtils.isEmpty(CnfmPasswordHolder)){

            if(PasswordHolder.equals(CnfmPasswordHolder)){

                EditTextEmptyHolder = false ;

            }

            else {

                Toast.makeText(RegisterActivity.this,"Password & confirm password doesn't match", Toast.LENGTH_LONG).show();

            }

        }
        else {

            EditTextEmptyHolder = true ;
        }
    }

    // Checking Name is already exists or not.
    public void CheckUsernameExists(){

        // Opening SQLite database write permission.
        sqLiteDatabaseObj = sqLiteHelper.getWritableDatabase();

        // Adding search email query to cursor.
        cursor = sqLiteDatabaseObj.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_1_Name + "=?", new String[]{NameHolder}, null, null, null);

        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                // If Name is already exists then Result variable value set as Name Found.
                F_Result = "Name Found";

                // Closing cursor.
                cursor.close();
            }
        }

        // Calling method to check final result and insert data into SQLite database.
        CheckFinalResult();

    }


    // Checking result
    public void CheckFinalResult(){

        // Checking whether Name is already exists or not.
        if(F_Result.equalsIgnoreCase("Name Found"))
        {

            // If Name is exists then toast msg will display.
            Toast.makeText(RegisterActivity.this,"Name Already Exists", Toast.LENGTH_LONG).show();

        }
        else {

            // If email already dose n't exists then user registration details will entered to SQLite database.
            InsertDataIntoSQLiteDatabase();

        }

        F_Result = "Not_Found" ;

    }
}