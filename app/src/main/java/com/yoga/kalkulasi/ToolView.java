package com.yoga.kalkulasi;
import android.support.v7.app.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.*;
import android.os.*;
import android.util.*;
import com.yoga.kalkulasi.Control.*;
import android.view.*;
import android.content.*;
import android.database.*;
import java.util.*;
import android.support.v4.view.*;
import android.support.design.widget.*;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.*;
import com.google.android.gms.ads.*;
public class ToolView extends AppCompatActivity 
{
    toolHelper myDb;
    variabelHelper myVar;
    detailToolHelper myDetailItem;
    Cursor res;
    
    ListView lv;
    
    Toolbar toolbar;
    ViewPager viewPager; 
    TabLayout tabLayout; 
    
    String id_tool;

    Button btnSimpan;
  
    FloatingActionButton fab;
    
    String[] namaVars,aliasVar,formulaVar,satuanVars;
	
	private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
     
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
        fab = (FloatingActionButton) findViewById(R.id.fab);
        
        id_tool = getIntent().getExtras().getString("id_tool");
        
    
        myDb = new toolHelper(this);
        myVar = new variabelHelper(this);
        myDetailItem = new detailToolHelper(this);
		
		mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        
        getSupportActionBar().setTitle(myDb.getNamaTool(id_tool));
        
        viewPager = (ViewPager) findViewById(R.id.vp);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tl);
        tabLayout.setupWithViewPager(viewPager);
        
        fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    final VariabelToolAction dv = new VariabelToolAction(ToolView.this, id_tool,"","tambah");
                    //dv.showDialog(this,"Tambah Variabel");
                    dv.setOnDismissListener(new DialogInterface.OnDismissListener(){
                            @Override
                            public void onDismiss(DialogInterface p1)
                            {
                                if(dv.getFlag()){
                                    setupViewPager(viewPager);
                                    viewPager.setCurrentItem(2);
                                }
                            }
                        });
                }

            });	
       
    }
   
    
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ToolFragmentSatu(id_tool), "HOME");
        adapter.addFrag(new ToolFragmentDua(id_tool), "VARIABEL");
       
        viewPager.setAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
 
    public void hapus()
    {
        Cursor res = myDetailItem.getVariableByTool(id_tool);
        if(res!=null && res.getCount()>0){
            while(res.moveToNext()){
                myVar.deleteData(res.getString(1));
            }
        }
        myDetailItem.deleteDataByTool(id_tool);
        myDb.deleteData(id_tool);
        //setText("Data telah dihapus");
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
                    Intent in = new Intent(ToolView.this, MainActivity.class);
                    in.putExtra("menu", "Tool");
                    startActivity(in);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent in = null;
        switch (item.getItemId())
        {

            case android.R.id.home:
                in = new Intent(ToolView.this,MainActivity.class);
                in.putExtra("menu","Tool");

                startActivity(in);
                return true;
            case R.id.edit:
                DialogTool dv = new DialogTool(ToolView.this, id_tool, "edit");
                //dv.showDialog(this,"Tambah Variabel");
                dv.setOnDismissListener(new DialogInterface.OnDismissListener(){
                        @Override
                        public void onDismiss(DialogInterface p1)
                        {
                            setupViewPager(viewPager);
                            viewPager.setCurrentItem(0);

                        }
                    });
                    return true;
            case R.id.hapus:
                ShowDialog();
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    
}


