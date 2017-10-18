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

public class DialogSelectTool extends Dialog
{


    toolHelper myTool;
    ListView lv;
    MyAdapterChevron adapter;

    Button btnCancel;

    LinearLayout textAlert;
    int totalData = 0;

    String[] id_tool,nama_tool;          
    public DialogSelectTool(Context context)
    {

        super(context);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_select_tool);
        setTitle("Tambah Barang");

        btnCancel = (Button) findViewById(R.id.btnCancel);

        myTool = new toolHelper(getContext());
        
        textAlert = (LinearLayout) findViewById(R.id.textAlert);
        loadData();
        btnCancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    dismiss();
                }
            });

        show();
    }

    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }
    public void loadData()
    {
        readData();

        if (totalData > 0)
        {
            textAlert.setVisibility(LinearLayout.GONE);
            lv = (ListView) findViewById(R.id.listTool);
            adapter = new MyAdapterChevron(getContext(), nama_tool);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
                    {
                        DialogToolAction dt = new DialogToolAction(getContext(), id_tool[p3]);
                        dt.show();
                        dismiss();
                    }

                });
        }
        else
        {
            textAlert.setVisibility(LinearLayout.VISIBLE);
        }
    }

    public void readData()
    {
        Cursor res = myTool.getAllData();
        StringBuffer nama = new StringBuffer();
        StringBuffer id = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                nama.append(res.getString(1) + ",");
                id.append(res.getString(0) + ",");
            }
        }
        totalData = res.getCount();
      

        id_tool = id.toString().split(",");
        nama_tool = nama.toString().split(",");

    }





}
