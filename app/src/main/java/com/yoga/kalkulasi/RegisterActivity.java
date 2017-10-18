package com.yoga.kalkulasi;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.yoga.kalkulasi.Model.*;

public class RegisterActivity extends Activity 
{
	Button btnSave, btnCancel;
	EditText username, nama, password;
 	UserHelper myDb;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

		btnSave = (Button) findViewById(R.id.btnRegister);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		username = (EditText) findViewById(R.id.txtUsername);
		nama = (EditText) findViewById(R.id.txtNamaUser);
		password = (EditText) findViewById(R.id.txtPassword);


		myDb = new UserHelper(this);

		btnCancel.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v){
					Intent i = new Intent(RegisterActivity.this,MainActivity.class);
					startActivity(i);
				}
			});	

		btnSave.setOnClickListener(new View.OnClickListener(){
				public void onClick(View v){
					String n,u,p;
					final String TAG = "info : ";
					n = nama.getText().toString();
					u = username.getText().toString();
					p = password.getText().toString();
					boolean flag = myDb.insertData(n,u,p);
					if(flag == true){

						Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
						startActivity(i);
					}else{

					}

				}
			});	
	}
}
