package com.yoga.kalkulasi;
import android.support.v7.app.*;
import com.yoga.kalkulasi.Model.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;

public class ToolAction extends AppCompatActivity 
{
    toolHelper myDb;
    variabelHelper myVar;
    Button btnSimpan,btnCancel,btnTambahVariabel;
    EditText namaTool,deskripsiTool;

    String FileName = "MyFile";
    String data = "";
    String id_user = "";
    String line ="";
    String[] arrVar = {"","X","Y","Z","A","B","C","D","E"}; 
    int jumlahVar = 1;
    List<EditText> namaVar = new ArrayList<EditText>();
    List<EditText> satuanVar = new ArrayList<EditText>();
    List<EditText> persamaanVar = new ArrayList<EditText>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_action);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnCancel = (Button) findViewById(R.id.btnCancel);


        btnTambahVariabel = (Button) findViewById(R.id.btnTambahVariabel);
        namaTool = (EditText) findViewById(R.id.namaTool);
        deskripsiTool = (EditText) findViewById(R.id.deskripsiTool);

        myDb = new toolHelper(this);
        myVar = new variabelHelper(this);

        Add_Line();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (!namaTool.getText().toString().equals("") && !deskripsiTool.getText().toString().equals(""))
                    {
                        if(myDb.insertData(namaTool.getText().toString(), deskripsiTool.getText().toString(), "")){
                            Intent i = new Intent(ToolAction.this, MainActivity.class);
                            i.putExtra("menu", "Tool");
                            startActivity(i);
                        }else{
                            setText("Gagal");
                        }
                        
                    }
                    else
                    {
                        setText("Isi nama dan deskripsi");
                    }


                }
            });
        btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                }
            });
        btnTambahVariabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Add_Line();
                }
            });		
    }

    public void Add_Line()
    {
        LinearLayout ll = (LinearLayout)findViewById(R.id.layoutVariabel);
        // add edittext
        LinearLayout layout = new LinearLayout(this);
        LinearLayout layout2 = new LinearLayout(this);

        EditText namaVariabel = new EditText(this);
        EditText besaran = new EditText(this);
        EditText Formula = new EditText(this);
        TextView alias = new TextView(this);
        TextView txtPersamaan = new TextView(this);

        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(p1);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setLayoutParams(p1);

        p2.weight = 4;
        p3.weight = 1;
        p4.weight = 5;

        alias.setLayoutParams(p3);
        alias.setText(arrVar[jumlahVar]);
        alias.setTextSize(16);

        namaVariabel.setLayoutParams(p2);
        namaVariabel.setHint("Nama Variabel");
        namaVariabel.setId(jumlahVar);

        besaran.setLayoutParams(p3);
        besaran.setHint("cm");
        besaran.setId(100 + jumlahVar);

        Formula.setLayoutParams(p4);
        Formula.setHint("Misal X*Y");
        Formula.setId(200 + jumlahVar);

        txtPersamaan.setLayoutParams(p3);
        txtPersamaan.setText("Persamaan");
        txtPersamaan.setTextSize(16);

        ll.addView(layout);
        ll.addView(layout2);
        layout.addView(alias);
        layout.addView(namaVariabel);
        layout.addView(besaran);
        layout2.addView(txtPersamaan);
        layout2.addView(Formula);

        namaVar.add(namaVariabel);
        satuanVar.add(besaran);
        persamaanVar.add(Formula);

        jumlahVar++;
    }

    public void setText(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

    }
}
