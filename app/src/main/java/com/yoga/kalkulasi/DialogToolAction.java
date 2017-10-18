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

public class DialogToolAction extends Dialog
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

    Button btnSimpan;

    String[] nama_variabel,id_variabel,formula,satuan = {""};

    String[] arr_alias = {"X","Y","Z","A","B","C","D","E","F","G","H","I","J","K","L","M","N"};

    List<EditText> valueVariabel; 
    
   
    public DialogToolAction(Context context, String tool)
    {

        super(context);
        this.id_tool = tool;

try{

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_tool_action);
        setTitle("Tambah Barang");


        Button btnHitung = (Button) findViewById(R.id.btnHitung);
        Button btnClose = (Button) findViewById(R.id.btnClose);
        
        myTool = new toolHelper(getContext());
        myDetailTool = new detailToolHelper(getContext());
        myVariabel = new variabelHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        valueVariabel= new ArrayList<EditText>();
        loadData();
        
        btnHitung.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try
                    {


                        for (int i = 0; i < nama_variabel.length; i++)
                        {
                           
                            if (!formula[i].equals("-"))
                            {
                                double hasil = hitung(changeVar(formula[i]));
                                valueVariabel.get(i).setText(String.valueOf(hasil));

                            }
                            else
                            {
                                if (formula.equals(""))
                                {
                                    setText(getContext(),"Diisi dong");
                                }
                            }       
                        }
                    }
                    catch (Exception e)
                    {
                        setText(getContext(),e.toString());
                    }
                }
            });

        btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dismiss();
                }
            });
            
            }catch(Exception e ){
                setText(getContext(),e.toString());
            }
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

    public String changeVar(String formula)
    {

        for (int i = 0; i < nama_variabel.length; i++)
        {

            formula = formula.replace(arr_alias[i], valueVariabel.get(i).getText().toString());
        }

        return formula;
    }
    public double hitung(String rumus)
    {

        EvaluateEngine evaluateEngine = new EvaluateEngine();
        Double answer = evaluateEngine.evaluate(rumus);
        return answer;

    }

    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }

    public void Add_Line(int i)
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.layoutItem);

        // add edittext
        // add edittext
        LinearLayout layout = new LinearLayout(getContext());
        LinearLayout layout2 = new LinearLayout(getContext());

        TextView namaVariabel = new TextView(getContext());
        TextView satuanVariabel = new TextView(getContext()); 
        EditText value = new EditText(getContext());

        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(p1);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setLayoutParams(p1);

        p2.width = 130;
        p3.weight = 4;
        p4.width = 50;

        namaVariabel.setLayoutParams(p2);
        namaVariabel.setHint("Nama Variabel");
        namaVariabel.setId(i);
        namaVariabel.setText(nama_variabel[i]);

        satuanVariabel.setLayoutParams(p4);
        satuanVariabel.setHint("cm");
        satuanVariabel.setId(100 + i);
        satuanVariabel.setText(satuan[i]);

        value.setLayoutParams(p3);
        value.setId(100 + i);

        layout.addView(layout2);
        layout2.addView(namaVariabel);
        layout2.addView(value);
        layout2.addView(satuanVariabel);

        ll.addView(layout);

        valueVariabel.add(value);

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
