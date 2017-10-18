package com.yoga.kalkulasi;
import android.app.*;
import android.view.Window;
import android.widget.*;
import android.view.*;
import android.content.*;
import com.yoga.kalkulasi.Model.*;

public class DialogVariabel
{
	String id_detail = "";
	variabelHelper myVar;
	detailVariabelHelper myDetail;
	
	public DialogVariabel(){
		
	}
	public DialogVariabel(String id_detail_){
		id_detail = id_detail_;
	}
	String[] arr_var;
	String[] arr_menu = {"Tambah Baru"};
	ListView lv1,lv2;
	ArrayAdapter adapter1,adapter2;
	public void showDialog(final Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
		
		myVar = new variabelHelper(dialog.getContext());
		myDetail = new detailVariabelHelper(dialog.getContext());
		
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_variabel);
		
		lv1 = (ListView) dialog.findViewById(R.id.listVariabel);
		lv2 = (ListView) dialog.findViewById(R.id.listMenu);
        
		adapter1 = new ArrayAdapter(dialog.getContext(), android.R.layout.simple_list_item_1, arr_menu);
        lv1.setAdapter(adapter1);
		
		adapter2 = new ArrayAdapter(dialog.getContext(), android.R.layout.simple_list_item_1, arr_menu);
        lv2.setAdapter(adapter2);

		lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					if(i == 0){
						dialog.dismiss();
						String[] array = {"1","2"};
						VariabelAction va = new VariabelAction(dialog.getContext(),id_detail,"","");
						
						
					}
					//setText(dialog.getContext(),"item di klik "+i);
					//Intent in = new Intent(dialog.getContext(),KalkulasiView.class );
					//in.putExtra("id_kalkulasi",arr_id[i]);
					//startActivity(in);
					//Toast.makeText(getContext(),arr[i]+" id = "+arr_id[i],Toast.LENGTH_SHORT).show();
				}
			});
		Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

        dialog.show();

    }
	
	public void setText(Context con,String teks){
		Toast.makeText(con, teks,Toast.LENGTH_SHORT).show();
	}
}
