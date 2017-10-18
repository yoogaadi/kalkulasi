package com.yoga.kalkulasi;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.*;
import android.database.*;
import android.content.*;
import com.yoga.kalkulasi.Control.*;
import android.support.design.widget.*;
import com.yoga.kalkulasi.Tutorial.*;
import java.io.*;

public class KalkulasiFragmentActivity extends Fragment
{

    kalkulasiHelper myDb;
    detailKalkulasiHelper myDetail;
    ListView lv;
    Cursor res;
    MyAdapterChevron adapter;
    String[] arr, arr_id;
    int totalData = 0;
    String id_user;
    String id_project = "-";

    LinearLayout linearEmpty;
    
    FloatingActionButton fab;
	
	boolean isFirstTime = false;


    public KalkulasiFragmentActivity()
    {

    }
    public KalkulasiFragmentActivity(String user)
    {
        this.id_user = user;
    }	

    public static KalkulasiFragmentActivity newInstance()
    {
        KalkulasiFragmentActivity fragment = new KalkulasiFragmentActivity();
        return fragment;
    }
    public static KalkulasiFragmentActivity newInstance(String user)
    {
        KalkulasiFragmentActivity fragment = new KalkulasiFragmentActivity(user);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //myDb = new kalkulasiHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        myDb = new kalkulasiHelper(getContext());
        myDetail = new detailKalkulasiHelper(getContext());


        //Toast.makeText(getContext(),id_user,Toast.LENGTH_SHORT).show();
        View view =  inflater.inflate(R.layout.kalkulasi_activity, container, false);
        linearEmpty = (LinearLayout) view.findViewById(R.id.linearEmpty);

        readData();
		
        
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    Intent i = new Intent(getContext(), KalkulasiAction.class);
                    //i.putExtra("id_user",id_user);
                    i.putExtra("id_project", id_project);
                    i.putExtra("status", "tambah");
                    startActivity(i); 
                }

            }); 
        

        if (totalData > 0)
        {
            linearEmpty.setVisibility(LinearLayout.GONE);
            lv = (ListView) view.findViewById(R.id.idListKalkulasi);
            adapter = new MyAdapterChevron(getActivity(), arr);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                    {
                        Intent in = new Intent(getContext(), KalkulasiView.class);
                        in.putExtra("id_kalkulasi", arr_id[i]);
                        in.putExtra("id_project", id_project);
                        startActivity(in);
                        //Toast.makeText(getContext(),arr[i]+" id = "+arr_id[i],Toast.LENGTH_SHORT).show();
                    }
                });
        }else{
            linearEmpty.setVisibility(LinearLayout.VISIBLE);
        }
		
		if(getStatusTutorial()){
			TutorialTiga ck = new TutorialTiga(getContext());
		}


        return view;
    }

    public void readData()
    {
        String id, tes;
        res = myDb.getKalkulasiParrent();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer id_buffer = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                if(myDetail.isTemplate(res.getString(0))){
                    stringBuffer.append(res.getString(1) + ",");
                    id_buffer.append(res.getString(0) + ",");
                    totalData++;
                }
            }
            tes = stringBuffer.toString();
            id = id_buffer.toString();
        }
        else
        {
            tes = "";
            id = "";	
        }
        
        arr = tes.split(",");
        arr_id = id.split(",");
    }

	public boolean getStatusTutorial(){

        boolean flag = true;
        try
        {
			File file = new File(getContext().getCacheDir() , "Tutorial");
            if (file.canRead())
            {
                flag = false;
            }
            else
            {
                flag = true;
            }
        }catch(Exception e){

		}

        return flag;
    }


}
