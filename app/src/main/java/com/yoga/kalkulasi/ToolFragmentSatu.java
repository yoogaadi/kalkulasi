package com.yoga.kalkulasi;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.database.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.Toast;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.*;
import java.util.*;
import com.yoga.kalkulasi.Control.*;
import android.content.*;


public class ToolFragmentSatu extends Fragment
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

    Button btnSimpan,btnCancel,btnFormula;

    String[] nama_variabel,id_variabel,formula,satuan = {""};
    
    LinearLayout ll;

    String[] arr_alias = {"X","Y","Z","A","B","C","D","E","F","G","H","I","J","K","L","M","N"};

    List<EditText> valueVariabel; 


    public ToolFragmentSatu()
    {

    }

    public ToolFragmentSatu(String id_tool)
    {
        this.id_tool = id_tool;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: Implement this method
        view = inflater.inflate(R.layout.tool_fragment_satu, container, false);
        myTool = new toolHelper(getContext());
        myDetailTool = new detailToolHelper(getContext());
        myVariabel = new variabelHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        btnSimpan = (Button) view.findViewById(R.id.btnHitung);
        
        loadData();
        
        btnFormula = (Button) view.findViewById(R.id.btnFormula);
        btnFormula.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    DialogFormulaTool dv = new DialogFormulaTool(getContext(), id_tool);
                    //dv.showDialog(this,"Tambah Variabel");
                    dv.setOnDismissListener(new DialogInterface.OnDismissListener(){
                            @Override
                            public void onDismiss(DialogInterface p1)
                            {
                                loadData();
                            }
                        });
                }
        });


        return view; 
    }

    private void loadData(){
        valueVariabel= new ArrayList<EditText>();
        getVariabelByTool();
        if(totalData >0 ){
            for(int i = 0; i<nama_variabel.length;i++){
                Add_Line(i);
            }

        }
        
        btnSimpan.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
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
                                    setText("Diisi dong");
                                }
                            }       
                        }
                    }
                    catch (Exception e)
                    {
                        setText("ups.. terjadi kesalahan dalam perhitungan, silahkan cek formula");
                        
                    }
                }

            }); 
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

    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }


    public void Add_Line(int i)
    {
        ll = (LinearLayout) view.findViewById(R.id.layoutItem);
        if(i==0){
            ll.removeAllViews();
        }
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
        value.setHint("Formula : "+formula[i]);
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

/*
public void Add_Line(int i)
    {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.layoutItem);

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

        p2.weight = 3;
        p3.weight = 4;
        p4.weight = 1;
        
        namaVariabel.setLayoutParams(p2);
        namaVariabel.setHint("Nama Variabel");
        namaVariabel.setId(i);
        namaVariabel.setText(nama_variabel[i]);

        satuanVariabel.setLayoutParams(p4);
        satuanVariabel.setHint("cm");
        satuanVariabel.setId(100 + i);
        satuanVariabel.setText(satuan[i]);

        value.setLayoutParams(p3);
        value.setHint("Formula : "+formula[i]);
        value.setId(100 + i);
        
        layout.addView(layout2);
        layout2.addView(namaVariabel);
        layout2.addView(value);
        layout2.addView(satuanVariabel);

        ll.addView(layout);
        
        valueVariabel.add(value);

    }

    */
