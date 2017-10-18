package com.yoga.kalkulasi.Fragment;
import android.support.v4.app.*;
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

public class FragmentEmptyTool extends Fragment
{


    public FragmentEmptyTool()
    {

    }

    public static FragmentEmptyTool newInstance()
    {
        FragmentEmptyTool fragment = new FragmentEmptyTool();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
      


        View view =  inflater.inflate(R.layout.kategori_activity, container, false);

        return view;
    }

}
