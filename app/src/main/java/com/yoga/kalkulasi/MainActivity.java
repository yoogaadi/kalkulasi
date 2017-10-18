
package com.yoga.kalkulasi;
import android.support.v7.app.*;
import android.support.design.widget.*;
import android.os.*;
import android.support.v7.widget.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.view.*;
import android.content.res.*;
import android.support.annotation.*;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import java.io.*;
import android.database.*;
import com.yoga.kalkulasi.Model.*;
import android.widget.TextView;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.*;
import com.yoga.kalkulasi.Tutorial.*;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{



    public Toolbar toolbar;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    String FileName = "MyFile";
    String data, id_user, line ="";
    TextView namaUser;
    boolean flag;
    Cursor res;
    String nama = "";

    UserHelper myDb;
    satuanHelper mySatuan;
    kategoriHelper myKategori;

	private AdView mAdView;

	boolean isFirstTime = false;



    String[] nama_satuan = {"km","m","cm","L","ml","cc","kg","g","buah","pcs","sak","m3","m2","hari"};
    String[] kategori = {"Uncategorized","BB","BP","SC","PS","PS","JS"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

		MobileAds.initialize(this,"ca-app-pub-9240036468098328~6530335298");

		mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mySatuan = new satuanHelper(this);
        myKategori = new kategoriHelper(this);



        if (mySatuan.cekBarang() == false)
        {
            for (int i = 0;i < nama_satuan.length;i++)
            {
                mySatuan.insertData(nama_satuan[i]);
            }
        }

        res = myKategori.getAllData();

        if (res.getCount() == 0)
		{
            for (int x =0; x < kategori.length;x++)
			{
                myKategori.insertData(kategori[x]);
            }

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        myDb = new UserHelper(this);

        flag = readFile();

        if (flag == false)
        {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        getNama();

        Fragment frag = null;

        if (getIntent().hasExtra("menu"))
        {
            if (getIntent().getExtras().getString("menu").equals("Kalkulasi"))
            {
                frag = KalkulasiFragmentActivity.newInstance(id_user);
                getSupportActionBar().setTitle("Kalkulasi");
                //frag = HomeFragment.newInstance();
            }
            else if (getIntent().getExtras().getString("menu").equals("Tool"))
            {
                frag = ToolFragmentActivity.newInstance();
                getSupportActionBar().setTitle("Tool");
            }
            else if (getIntent().getExtras().getString("menu").equals("Barang"))
            {
                frag = BarangFragmentActivity.newInstance();
                getSupportActionBar().setTitle("Pricelist");
            }

        }
        else
        {
            getSupportActionBar().setTitle("Project");
			if (getStatusTutorial())
			{
				if (getIntent().hasExtra("tutorial"))
				{
					frag = ProjectFragmentActivity.newInstance(id_user,9);
				}else{
					frag = ProjectFragmentActivity.newInstance(id_user,1);
				}
			}else{
				frag = ProjectFragmentActivity.newInstance(id_user);
			}
			

            
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, frag);
        transaction.commit();

        setupDrawer();

        NavigationView navigationview = (NavigationView) findViewById(R.id.nav_view);
        navigationview.setNavigationItemSelectedListener(this);

    }

    private void setupDrawer()
    {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                                                  R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); 
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        Fragment selectedFragment = null;

        if (id == R.id.tblLogout)
        {

            ShowDialog();
        }
        else
        {
            if (id == R.id.btnHome)
            {
                getSupportActionBar().setTitle("Project");
                selectedFragment = ProjectFragmentActivity.newInstance(id_user);
            }
            else if (id == R.id.btnKalkulasi)
            {
                selectedFragment = KalkulasiFragmentActivity.newInstance(id_user);

                getSupportActionBar().setTitle("Kalkulasi");
            }
            else if (id == R.id.btnTool)
            {
                selectedFragment = ToolFragmentActivity.newInstance();
                getSupportActionBar().setTitle("Tool");
            }
            else if (id == R.id.btnBarang)
            {
                selectedFragment = BarangFragmentActivity.newInstance();
                getSupportActionBar().setTitle("Pricelist");

            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    public void ShowDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Log Out");
        builder.setMessage("Anda yakin untuk log out?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    //Toast.makeText(getApplicationContext(),"OK was Clicked",Toast.LENGTH_SHORT).show();
                    //finish();
                    logout();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            })
            .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    mDrawerLayout.closeDrawers();
                }
            });

        // Create the AlertDialog object and return it
        builder.create().show();
    }
    public void getNama()
    {
        nama = myDb.getNamaUser(id_user);
    }

    public boolean readFile()
    {
        boolean flag = false;
        try
        {
            File file = new File(getCacheDir(), FileName);
            if (file.canRead())
            {
                FileInputStream fin = new FileInputStream(file);
                InputStreamReader inputStream = new InputStreamReader(fin);
                BufferedReader bufferedReader  = new BufferedReader(inputStream);
                StringBuilder stringBuilder = new StringBuilder();
                line = null;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }

                fin.close();
                inputStream.close();
                id_user = stringBuilder.toString();
                flag = true;

            }
            else
            {
                flag = false;

            }
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
        return flag;
    }

    public void logout()
    {
        File file = new File(getCacheDir(), FileName);
        file.delete();
    }
	public boolean getStatusTutorial()
	{

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
        }
		catch (Exception e)
		{

		}

        return flag;
    }

}
