package com.yoga.kalkulasi.Tutorial;
import android.os.*;
import android.view.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.*;
import android.database.*;
import android.content.*;
import android.support.design.widget.FloatingActionButton;
import android.widget.AdapterView.*;
import android.app.AlertDialog;
import com.yoga.kalkulasi.*;
import android.app.Dialog;

public class TutorialTiga extends Dialog
{
    Button btnLanjutkan;

    public TutorialTiga(Context context)
    {

        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_tutorial_tiga);
        setTitle("Tambah Barang");
		btnLanjutkan = (Button) findViewById(R.id.btnLanjut);

		btnLanjutkan.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					dismiss();
				}


			});



        show();
    }


    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }

	
}
