package com.yoga.kalkulasi;
import android.content.*;
import android.database.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import com.yoga.kalkulasi.Control.*;
import com.yoga.kalkulasi.Model.*;
import com.yoga.kalkulasi.Tutorial.*;
import java.io.*;

public class ProjectFragmentActivity extends Fragment
{

    projectHelper myDb;
    GridView lv;
    Cursor res;
    MyAdapterGrid adapter;
    String[] arr, arr_id,deskripsi;
    int totalData = 0;
    String id_user;
    String[] arr_data = {"ce","12"};
    FloatingActionButton fab;
    LinearLayout linearEmpty;
	
	boolean isFirsTime = false;
	int status = 0;

    public ProjectFragmentActivity()
    {

    }

    public ProjectFragmentActivity(String id_usr)
    {
        id_user = id_usr;
    }
	public ProjectFragmentActivity(String id_usr,int stat)
    {
        id_user = id_usr;
		status = stat;
    }

    public static ProjectFragmentActivity newInstance()
    {
        ProjectFragmentActivity fragment = new ProjectFragmentActivity();
        return fragment;
    }
    public static ProjectFragmentActivity newInstance(String id_usr)
    {
        ProjectFragmentActivity fragment = new ProjectFragmentActivity(id_usr);
        return fragment;
    }
	public static ProjectFragmentActivity newInstance(String id_usr,int status)
    {
        ProjectFragmentActivity fragment = new ProjectFragmentActivity(id_usr,status);
        return fragment;
    }
	

	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_tambah, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.tambah:
                Intent i = new Intent(getContext(), ProjectAction.class);
                i.putExtra("id_user", id_user);
                i.putExtra("status", "tambah");
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        myDb = new projectHelper(getContext());
        View view =  inflater.inflate(R.layout.project_activity, container, false);
        readData();
        linearEmpty = (LinearLayout) view.findViewById(R.id.linearEmpty);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
					
                    Intent i = new Intent(getContext(), ProjectAction.class);
                    i.putExtra("id_user", id_user);
                    i.putExtra("status", "tambah");
                    startActivity(i);
                }

            });	

        if (totalData < 1)
        {
            linearEmpty.setVisibility(LinearLayout.VISIBLE);
            Toast.makeText(getContext(), "Tidak ada data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            linearEmpty.setVisibility(LinearLayout.GONE);
            lv = (GridView) view.findViewById(R.id.listProject);
            adapter = new MyAdapterGrid(getActivity(), arr,deskripsi);
            lv.setAdapter(adapter);


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                    {
		 				Intent in = new Intent(getContext(), ProjectView.class);
                        in.putExtra("id_project", arr_id[i]);
                        startActivity(in);
		 	//Toast.makeText(getContext(),arr[i]+" id = "+arr_id[i],Toast.LENGTH_SHORT).show();
                    }
                });
        }
		
		
		if(getStatusTutorial()){
			if(status == 9){
				TutorialDelapan td = new TutorialDelapan(getContext());
			}else if(status == 1){
				TutorialSatu td = new TutorialSatu(getContext());
			}
		}
		
        return view;
    }

    public void readData()
    {
        String id, tes;
        res = myDb.getAllData();
        StringBuffer stringBuffer = new StringBuffer();
		StringBuffer desc = new StringBuffer();
        StringBuffer id_buffer = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                stringBuffer.append(res.getString(2) + ",");

                desc.append(res.getString(3) + ",");
                id_buffer.append(res.getString(0) + ",");
            }
      

        }
      
       
        totalData = res.getCount();
        arr = stringBuffer.toString().split(",");
        arr_id = id_buffer.toString().split(",");
		deskripsi = desc.toString().split(",");
		
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
