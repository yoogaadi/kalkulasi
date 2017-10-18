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
import com.yoga.kalkulasi.Control.*;


public class KalkulasiFragmentDua extends Fragment
{
    kalkulasiHelper myDb;
    detailKalkulasiHelper myDet;
    ListView lv;
    MyAdapterChevron adapter;
    String[] arr,arr_id = {};
    int totalData = 0;
    String id_kalkulasi,nama;
    Cursor res;
    String id_project = "-";

    public KalkulasiFragmentDua()
    {

    }

    public KalkulasiFragmentDua(String id_kal, String id_proj)
    {
        id_kalkulasi = id_kal;
        id_project = id_proj;
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
        View view = inflater.inflate(R.layout.kalkulasi_fragment_2, container, false);
        myDb = new kalkulasiHelper(getContext());
        myDet = new detailKalkulasiHelper(getContext());

        //setText(id_kalkulasi);
        readData();
        readNama();
        //setText(id_kalkulasi);
        if (totalData > 0)
        {
            lv = (ListView) view.findViewById(R.id.listSub);
            adapter = new MyAdapterChevron(getActivity(), arr);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                    {
                        Intent in = new Intent(getContext(), KalkulasiView.class);
                        in.putExtra("id_kalkulasi", arr_id[i]);
                        in.putExtra("id_project", id_project);
                        in.putExtra("breadcrumbs", nama + " > " + arr[i]);
                        startActivity(in);
                        //Toast.makeText(getContext(),arr[i]+" id = "+arr_id[i],Toast.LENGTH_SHORT).show();
                    }
                });




        }
        else
        {
            //setText("Gaada data");
        }


        return view;
    }

    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }
    public void readNama()
    {
        res = myDb.getKalkulasiById(id_kalkulasi);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                //setText(res.getString(0)+" - "+res.getString(1)+" - "+res.getString(2)+" - "+res.getString(3));
                nama = res.getString(1);

            }
            //Toast.makeText(getContext(),"ada",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Toast.makeText(getContext(),"gak ada",Toast.LENGTH_SHORT).show();

        }
    }
    public void readData()
    {
        String id, tes;
        res = myDb.getKalkulasiByParrent(id_kalkulasi);
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer id_buffer = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                Cursor get = myDet.getDetailKalkulasi(res.getString(0), id_project);

                if (get != null && get.getCount() > 0)
                {
                    while (get.moveToNext())
                    {
                        if (get.getString(2).toString().equals(id_project))
                        {
                            stringBuffer.append(res.getString(1) + ",");
                            id_buffer.append(res.getString(0) + ",");
                            totalData++;
                        }
                        else
                        {
                            continue;
                        } 
                    }
                }

            }
        }

        //totalData = res.getCount();
        arr = stringBuffer.toString().split(",");
        arr_id = id_buffer.toString().split(",");
    }

}
