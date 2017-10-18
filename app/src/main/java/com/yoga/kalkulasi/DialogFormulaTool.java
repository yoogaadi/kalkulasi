package com.yoga.kalkulasi;
import android.app.*;
import android.view.Window;
import android.widget.*;
import android.view.*;
import android.content.*;
import com.yoga.kalkulasi.Model.*;
import android.database.*;
import java.text.*;
import java.util.*;
import com.yoga.kalkulasi.Control.*;
import android.view.View.*;

public class DialogFormulaTool extends Dialog
{
    detailToolHelper myDetailTool;
    toolHelper myTool;
    variabelHelper myVariabel;
    satuanHelper mySatuan;

    ListView lv;
    ArrayAdapter adapter;
    String[] arr,arr_id = {};
    int totalData = 0;
    String id_kalkulasi,nama;
    Cursor res;
    String id_tool = "-";
    View view;
    String select_id_var;

    Button btnSimpan,btnCancel;

    String[] nama_variabel,id_variabel,formula,satuan = {""};

    String[] arr_alias = {"X","Y","Z","A","B","C","D","E","F","G","H","I","J","K","L","M","N"};

    List<EditText> persamaanVar = new ArrayList<EditText>();

    public DialogFormulaTool(Context context, String id_tool1)
    {

        super(context);
        this.id_tool = id_tool1;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_formula_tool);
        setTitle("Tambah Barang");

        myTool = new toolHelper(getContext());
        myDetailTool = new detailToolHelper(getContext());
        myVariabel = new variabelHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        loadData();

        btnSimpan.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    boolean flag=false;
                    for(int a = 0;a< id_variabel.length;a++){
                        
                        flag = myDetailTool.updateFormula(id_tool,id_variabel[a],persamaanVar.get(a).getText().toString());
                    }
                    if(flag == true){
                        setText("Formula tersimpan");
                    }else{
                        setText("Formula tidak dapat disimpan"); 
                    }
                    dismiss();
                    
                }

            });	

        btnCancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                   dismiss();

                }

            });	
            
        show();
    }

    private void loadData(){

        getVariabelByTool();
        if(totalData >0 ){
            for(int i = 0; i<nama_variabel.length;i++){
                Add_Line(i);
                //setText(satuan[i]);

            }

        }
    }

    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }


    public void Add_Line(int i)
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.layoutFormula);

        // add edittext
        LinearLayout layout = new LinearLayout(getContext());

        LinearLayout layout2 = new LinearLayout(getContext());

        TextView namaVariabel = new TextView(getContext());
        TextView aliasVariabel = new TextView(getContext());
        TextView satuanVariabel = new TextView(getContext()); 
        EditText Formula = new EditText(getContext());

        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(p3);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setLayoutParams(p1);

        p2.weight = 1;
        p3.weight = 4;
        p4.weight = 1;

        aliasVariabel.setLayoutParams(p2);
        aliasVariabel.setText(arr_alias[i]);
        aliasVariabel.setTextSize(20);
        aliasVariabel.setGravity(Gravity.CENTER_VERTICAL);

        namaVariabel.setLayoutParams(p1);
        namaVariabel.setHint("Nama Variabel");
        namaVariabel.setId(i);
        namaVariabel.setText(nama_variabel[i]);

        satuanVariabel.setLayoutParams(p4);
        satuanVariabel.setHint("cm");
        satuanVariabel.setId(100 + i);
        satuanVariabel.setText(satuan[i]);

        Formula.setLayoutParams(p1);
        Formula.setHint("Misal X*Y");
        Formula.setText(formula[i]);
        Formula.setId(200 + i);

        ll.addView(layout2);

        layout2.addView(aliasVariabel);
        layout.addView(namaVariabel);
        layout.addView(Formula);
        layout2.addView(layout);
        layout2.addView(satuanVariabel);


        persamaanVar.add(Formula);

    }


    public void getVariabelByTool(){
        Cursor res = myDetailTool.getVariableByTool(id_tool);
        StringBuffer nama = new StringBuffer();
        StringBuffer id_var = new StringBuffer();
        StringBuffer satuanVar = new StringBuffer();
        StringBuffer form = new StringBuffer();
        if(res!=null && res.getCount()>0){
            while(res.moveToNext()){
                nama.append(myVariabel.getNamaVariabel(res.getString(1))+",");
                id_var.append(res.getString(1)+",");
                form.append(res.getString(2)+",");
                satuanVar.append(mySatuan.getNamaSatuan(myVariabel.getSatuan(res.getString(1)))+",");
            }

        }
        totalData = res.getCount();
       
        id_variabel = id_var.toString().split(",");
        nama_variabel = nama.toString().split(",");
        formula = form.toString().split(",");
        satuan = satuanVar.toString().split(",");
    }
    


}
    
