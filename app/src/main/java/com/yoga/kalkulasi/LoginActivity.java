package com.yoga.kalkulasi;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.yoga.kalkulasi.Model.*;
import java.io.*;

public class LoginActivity extends Activity 
{
	UserHelper myDb;
	Button btn_login,btn_register;
	EditText username,password;
	TextView coba;

	String FileName = "MyFile";
	String data = "";
	String id_user,nama = "";
	String line ="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
		myDb = new UserHelper(this);

		btn_login = (Button) findViewById(R.id.btnLogin);
		btn_register = (Button) findViewById(R.id.btnRegister);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		coba = (TextView) findViewById(R.id.textAlert);


		if(readFile() == true){
			//coba.setVisibility(View.VISIBLE);
			//coba.setText("Sudah login");
			Intent i = new Intent(LoginActivity.this,MainActivity.class);
			i.putExtra("id_user",id_user);
			startActivity(i);

		}else{
			//coba.setVisibility(View.VISIBLE);
			//coba.setText("Belum login");

			btn_login.setOnClickListener(new View.OnClickListener(){
					public void onClick(View v){
						if(username.getText().toString().equals("") || password.getText().toString().equals("")){
							coba.setText("Username atau password tidak boleh kosong");	
						}else{

							if(myDb.VerifyLogin(username.getText().toString(),password.getText().toString()) == true){
								id_user = myDb.getUserLogin(username.getText().toString(),password.getText().toString());
								saveFile(id_user);
								Intent i = new Intent(LoginActivity.this,MainActivity.class);
								i.putExtra("id_user",id_user);
								startActivity(i);
							}else{
								coba.setVisibility(View.VISIBLE);
								coba.setText("Tidak dapat login");
							}
						}
					}
				});	

			btn_register.setOnClickListener(new View.OnClickListener(){
					public void onClick(View v){
						Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
						startActivity(i);
					}
				});	
		}
		 
    }

	public boolean readFile() {
		boolean flag = false;
        try {
            File file = new File(getCacheDir(),FileName);
			if(file.canRead()){
				FileInputStream fin = new FileInputStream(file);
				InputStreamReader inputStream = new InputStreamReader(fin);
				BufferedReader bufferedReader  = new BufferedReader(inputStream);
				StringBuilder stringBuilder = new StringBuilder();
				line = null;
				while ((line=bufferedReader.readLine()) != null){
					stringBuilder.append(line);
				}

				fin.close();
				inputStream.close();
				id_user = stringBuilder.toString();
				flag = true;

			}else{
				flag = false;

			}
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
		return flag;
    }

    public void saveFile(String id_user) {
        try {

            File file = new File(getCacheDir(),FileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(id_user.getBytes());
            fos.close();
//          Toast.makeText(this,"Data Saved",Toast.LENGTH_SHORT).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}
