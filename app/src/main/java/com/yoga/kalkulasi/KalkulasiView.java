package com.yoga.kalkulasi;
import android.os.*;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import java.util.*;
import android.view.MenuItem;
import android.content.Intent;
import com.yoga.kalkulasi.Model.*;
import android.database.Cursor;
import android.view.*;
import android.widget.*;
import android.support.design.widget.*;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.AutoCompleteTextView.*;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.yoga.kalkulasi.Tutorial.*;
import java.io.*;

public class KalkulasiView extends AppCompatActivity
{ 
    kalkulasiHelper myDb;
    detailKalkulasiHelper myDetail;
    detailItemHelper myItem;
    detailVariabelHelper myVar;
    
    Toolbar toolbar;
    ViewPager viewPager; 
    TabLayout tabLayout; 
    String id_kalkulasi,nama,id_detail_kalkulasi;
    Cursor res;
    TextView breadcrumbs;
    String kata;
    String id_project = "-";
    
    String id_kalkulasi_up = "";

    FloatingActionButton fab;
	
	private AdView mAdView;
	
	boolean isFirstTime = false;

    @Override 
    protected void onCreate(Bundle savedInstanceState)
    { 

        super.onCreate(savedInstanceState); 
        setContentView(R.layout.kalkulasi_view); 
        
        
        myDb = new kalkulasiHelper(this);
        myDetail = new detailKalkulasiHelper(this);
        myItem = new detailItemHelper(this);
        myVar = new detailVariabelHelper(this);
		
        id_kalkulasi = getIntent().getExtras().getString("id_kalkulasi");

        if (getIntent().hasExtra("id_project"))
        {
            id_project = getIntent().getExtras().getString("id_project");
        }
        
        readData();
        readDetail();
        
        
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        breadcrumbs = (TextView) findViewById(R.id.txtBreadcrumbs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
		
		mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
		

		if(getStatusTutorial()){
			TutorialEmpat te = new TutorialEmpat(KalkulasiView.this);
		}
		
        fab = (FloatingActionButton) findViewById(R.id.fab);
        
        if (getIntent().hasExtra("breadcrumbs"))
        {
            breadcrumbs.setText("Kalkulasi > "+getIntent().getExtras().getString("breadcrumbs"));
        }
        else
        {
            breadcrumbs.setText("Kalkulasi > "+nama);
        }
		
        getSupportActionBar().setTitle(nama);
        viewPager = (ViewPager) findViewById(R.id.vp);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tl);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    PopupMenu popup = new PopupMenu(KalkulasiView.this, fab);
                    if(id_kalkulasi_up.equals("") || id_kalkulasi_up==null){
                        popup.getMenuInflater().inflate(R.menu.menu_kalkulasi, popup.getMenu());
                    }else{
                        popup.getMenuInflater().inflate(R.menu.menu_kalkulasi_no_sub, popup.getMenu());
                    }
                    
                    
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                            public boolean onMenuItemClick(MenuItem item)
                            {
                                onOptionsItemSelected(item);
                                return true;
                            }
                        });

                    popup.show();
                }

            });	
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new KalkulasiFragmentSatu(id_kalkulasi, id_detail_kalkulasi, id_project), "HOME");
        if(id_kalkulasi_up.equals("") || id_kalkulasi_up==null){
            adapter.addFrag(new KalkulasiFragmentDua(id_kalkulasi, id_project), "SUB KALKULASI");
        }
        adapter.addFrag(new KalkulasiFragmentTiga(id_detail_kalkulasi), "VARIABEL");
        adapter.addFrag(new KalkulasiFragmentEmpat(id_detail_kalkulasi), "ITEM");
        viewPager.setAdapter(adapter);
    }
    
    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        @Override
        public int getCount()
        {
            // TODO: Implement this method
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int p1)
        {
            return mFragmentList.get(p1);
        }

        public void addFrag(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            // TODO: Implement this method
            return mFragmentTitleList.get(position);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent in = null;
        switch (item.getItemId())
        {

            case android.R.id.home:
                
                if(id_kalkulasi_up.equals("") || id_kalkulasi_up==null){
                    if(id_project.equals("-")){
                        in = new Intent(KalkulasiView.this, MainActivity.class);
                        in.putExtra("menu", "Kalkulasi");
                    }else{
                        in = new Intent(KalkulasiView.this, ProjectView.class);
                        in.putExtra("id_project", id_project);
                    }
                }else{
                    in = new Intent(KalkulasiView.this, KalkulasiView.class);
                    in.putExtra("id_kalkulasi", id_kalkulasi_up);
                    in.putExtra("id_project", id_project);
                }
                
                startActivity(in);
                return true;
            case R.id.btnSub:
                in = new Intent(KalkulasiView.this, KalkulasiAction.class);
                in.putExtra("status", "tambah");
                in.putExtra("id_project", id_project);
                in.putExtra("id_kalkulasi_up", id_kalkulasi);
                startActivity(in);
                return true;
            case R.id.btnSave:
                setText("simpan");
                return true;
            case R.id.hapus:
                ShowDialog();
                return true;
            case R.id.btnVariabel:
                VariabelAction dv = new VariabelAction(KalkulasiView.this, id_detail_kalkulasi, "", "tambah");
                //dv.showDialog(this,"Tambah Variabel");
                dv.setOnDismissListener(new DialogInterface.OnDismissListener(){
                        @Override
                        public void onDismiss(DialogInterface p1)
                        {
                            setupViewPager(viewPager);
                            viewPager.setCurrentItem(2);
                        }
                    });
                return true;
            case R.id.btnItem:
                TambahBarang tb = new TambahBarang(KalkulasiView.this, new String[]{"1","2"}, id_detail_kalkulasi,id_project);
                //dv.showDialog(this,"Tambah Variabel");
                tb.setOnDismissListener(new DialogInterface.OnDismissListener(){
                        @Override
                        public void onDismiss(DialogInterface p1)
                        {
                            setupViewPager(viewPager);
							

							if(getStatusTutorial()){
								viewPager.setCurrentItem(0);
							}else{
								viewPager.setCurrentItem(3);
							}
                            
                        }
                    });
                    
                
                return true;
            case R.id.edit:
                in = new Intent(KalkulasiView.this, KalkulasiAction.class);
                in.putExtra("status", "edit");
                in.putExtra("id_project", id_project);
                in.putExtra("id_kalkulasi", id_kalkulasi);
                startActivity(in);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void setText(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    
    public void readData()
    {
        res = myDb.getKalkulasiById(id_kalkulasi);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                    nama = res.getString(1);
                    id_kalkulasi_up = res.getString(2);
            }
        }
        else
        {
          
        }
    }

    public void readDetail()
    {
        res = myDetail.getDetailKalkulasi(id_kalkulasi, id_project);
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
               id_detail_kalkulasi = res.getString(0);
               
            }
           
        }
      
    }

    public void hapus()
    {
        if(id_project.equals("-")){
            myDb.deleteData(id_kalkulasi);
        }else{
            if(id_kalkulasi_up.equals("") || id_kalkulasi_up==null){
                
            }else{
				myDb.deleteData(id_kalkulasi);
			}
        }
        myDetail.deleteData(id_detail_kalkulasi);
        myVar.deleteDataByIdDetail(id_detail_kalkulasi);
        myItem.deleteDataByIdDetailKalkulasi(id_detail_kalkulasi);
       
        setText("Data telah dihapus");
    }

    public void ShowDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Hapus");
        builder.setMessage("Anda yakin akan menghapus data ini?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    hapus();
                    if(!id_project.equals("-")){
                        Intent in = new Intent(KalkulasiView.this, ProjectView.class);
                        in.putExtra("id_project", id_project);
                        startActivity(in);
                    }else{
                    Intent in = new Intent(KalkulasiView.this, MainActivity.class);
                    in.putExtra("menu", "Kalkulasi");
                    startActivity(in);
                    }
                }
            })
            .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                    return;

                }
            });

        builder.create().show();
    }
	
	public boolean getStatusTutorial(){

        boolean flag = true;
        try
        {
            File file = new File(getCacheDir() , "Tutorial");
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
