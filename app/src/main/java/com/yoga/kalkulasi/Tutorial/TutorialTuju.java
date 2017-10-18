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

public class TutorialTuju extends Dialog
{
    Button btnLanjutkan;

    public TutorialTuju(Context context)
    {

        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_tutorial_tuju);
        setTitle("Tambah Barang");
		btnLanjutkan = (Button) findViewById(R.id.btnLanjut);

		btnLanjutkan.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent in = new Intent(getContext(), MainActivity.class);
					in.putExtra("menu","Project");
					in.putExtra("first",true);
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
