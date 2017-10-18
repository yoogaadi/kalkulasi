package com.yoga.kalkulasi;
import android.database.*;
import android.os.*;
import android.support.v7.app.*;

import android.support.v7.widget.Toolbar;
import android.widget.*;
import com.yoga.kalkulasi.Model.*;
import android.view.*;
import android.content.*;
import android.widget.AutoCompleteTextView.*;
import java.util.*;
import com.yoga.kalkulasi.Control.*;
import com.google.android.gms.ads.*;
import com.yoga.kalkulasi.Tutorial.*;
import java.io.*;

public class ProjectView extends AppCompatActivity 
{
    public Toolbar toolbar;
    TextView textBreadcrumbs,txtDeskripsi;
    projectHelper myDb;
    kalkulasiHelper myKal;
    detailKalkulasiHelper myDetail;
    detailItemHelper myItem;
    detailVariabelHelper myVar;
    detailHargaBarangHelper myHarga;
    barangHelper myBarang;

    NumberControl nc;
    Button btnTambah;

    String id_project,nama,deskripsi = "";
    Cursor res,get;

    String[] nama_kalkulasi,total_kalkulasi = {};
    String[] id_kal = {"1","2"};
    String[] id_detail_kalkulasi = {};
    ListView lv;
    int totalData = 0;

    TextView textTotalBiaya;
    LinearLayout alert;

    MyAdapterKalkulasi adapter;

    String[] arr_alias = {"X","Y","Z","A","B","C","D","E","F","G","H","I","J","K","L","M","N"};

    List<String> price_detail;
    List<String> price_detail_sub;

	private InterstitialAd mInterstitialAd;

	private AdView mAdView;
	
	boolean isFirstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //textBreadcrumbs = (TextView) findViewById(R.id.txtBreadcrumbs);
        txtDeskripsi = (TextView) findViewById(R.id.textDescription);

		mInterstitialAd = new InterstitialAd(this);
		//mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); 
		mInterstitialAd.setAdUnitId("ca-app-pub-9240036468098328/3757928560");
		mInterstitialAd.loadAd(new AdRequest.Builder().build());

        myDb = new projectHelper(this);
        myKal = new kalkulasiHelper(this);
        myDetail = new detailKalkulasiHelper(this);
        myItem = new detailItemHelper(this);
        myVar = new detailVariabelHelper(this);
        myHarga = new detailHargaBarangHelper(this);
        myBarang = new barangHelper(this);

		mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
		
		if(getStatusTutorial()){
			TutorialSembilan ts = new TutorialSembilan(ProjectView.this);
		}
		
	
        nc = new NumberControl();

        alert = (LinearLayout) findViewById(R.id.alert);
        textTotalBiaya = (TextView) findViewById(R.id.textTotalBiaya);

        id_project = getIntent().getExtras().getString("id_project");
        //setText(id_project);


        loadData();
        getSupportActionBar().setTitle(nama);

        txtDeskripsi.setText(deskripsi);

        btnTambah = (Button) findViewById(R.id.btnTambah);
        btnTambah.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    PopupMenu popup = new PopupMenu(ProjectView.this, btnTambah);

                    popup.getMenuInflater().inflate(R.menu.menu_tambah_kalkulasi, popup.getMenu());

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
        getMenuInflater().inflate(R.menu.menu_project, menu);
        return true;
    }

    public void setText(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                ShowDialogExit();
                return true;
            case R.id.btnEdit:
                Intent i = new Intent(ProjectView.this, ProjectAction.class);
                i.putExtra("id_project", id_project);
                i.putExtra("status", "edit");
                startActivity(i);
                return true;

            case R.id.btnHapus:
                ShowDialog();
                return true;

            case R.id.tambahBaru:
                DialogTambahKalkulasi dt = new DialogTambahKalkulasi(ProjectView.this, id_project);

                dt.setOnDismissListener(new DialogInterface.OnDismissListener(){
                        @Override
                        public void onDismiss(DialogInterface p1)
                        {
                            loadData();

                            //viewPager.setCurrentItem(3);
                        }
                    });

                return true;
            case R.id.tambahTemplate:
                DialogTambahKalkulasiProject tb = new DialogTambahKalkulasiProject(ProjectView.this, id_project);
                //dv.showDialog(this,"Tambah Variabel");
                tb.setOnDismissListener(new DialogInterface.OnDismissListener(){
                        @Override
                        public void onDismiss(DialogInterface p1)
                        {
                            loadData();

                            //viewPager.setCurrentItem(3);
                        }
                    });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadData()
    {
        try
        {

            Double total_biaya = 0.0;
            readData();
            getKalkulasiByProject();

            StringBuffer kata = new StringBuffer();

            for (int a=0;a < id_detail_kalkulasi.length;a++)
            {
                price_detail = new ArrayList<String>();

                readDetail(id_detail_kalkulasi[a]);
                getSub(id_kal[a]);
                getVariabel(id_detail_kalkulasi[a]);

                String[] price_det = price_detail.toArray(new String[price_detail.size()]);

                String formula = myDetail.getFormula(id_detail_kalkulasi[a]);


                double hasil = 0.0;
                try
                {

                    for (int i = 0; i < price_detail.size(); i++)
                    {
                        if (!formula.equals(""))
                        {
                            hasil = hitung(changeVar(formula, price_det));                     
                        }
                        else
                        {
                            if (formula.equals(""))
                            {

                            }
                        }    


                    }
                }
                catch (Exception e)
                {

                    setText(e.toString());
                }

                kata.append(nc.getNumberFormat(hasil) + "=");
                total_biaya += hasil;

            }
            total_kalkulasi = kata.toString().split("=");

            textTotalBiaya.setText(nc.getNumberFormat(total_biaya));

            if (totalData > 0)
            {
                alert.setVisibility(LinearLayout.GONE);
                lv = (ListView) findViewById(R.id.listKalkulasi);
                adapter = new MyAdapterKalkulasi(ProjectView.this, nama_kalkulasi, total_kalkulasi);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                        {
                            Intent in = new Intent(ProjectView.this, KalkulasiView.class);
                            in.putExtra("id_kalkulasi", id_kal[i]);
                            //setText(id_kal[i]);
                            in.putExtra("id_project", id_project);
                            in.putExtra("Breadcrumbs", "Project > " + nama_kalkulasi[i]);
                            startActivity(in);
                            //Toast.makeText(getContext(),arr[i]+" id = "+arr_id[i],Toast.LENGTH_SHORT).show();
                        }
                    });
            }
            else
            {
                alert.setVisibility(LinearLayout.VISIBLE);
            }


        }
        catch (Exception e)
        {
            setText(e.toString());
        }

    }

    public void readData()
    {
        res = myDb.getProjectById(id_project);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                nama = res.getString(2);
                deskripsi = res.getString(3);
            }
        }
        else
        {

        }
    }
    public String changeVar(String formula, String[] input)
    {

        for (int i = 0; i < input.length; i++)
        {
            formula = formula.replace(arr_alias[i], input[i]);
        }

        return formula;
    }
    public double hitung(String rumus)
    {

        EvaluateEngine evaluateEngine = new EvaluateEngine();
        Double answer = evaluateEngine.evaluate(rumus);
        return answer;

    }

    public void getKalkulasiByProject()
    {
        res = myDetail.getDetailByProject(id_project);
        StringBuffer nama = new StringBuffer();
        StringBuffer id = new StringBuffer();
        StringBuffer iddetail = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {

            while (res.moveToNext())
            { 

                if (getKalkulasiNoSub(res.getString(1)))
                {
                    totalData++;
                    nama.append(myKal.getNamaKalkulasi(res.getString(1)) + ",");
                    id.append(res.getString(1) + ","); 
                    iddetail.append(res.getString(0) + ",");

                }

            }
            nama_kalkulasi = nama.toString().split(",");
            id_kal = id.toString().split(",");
            id_detail_kalkulasi = iddetail.toString().split(",");


        }


    }


    public boolean getKalkulasiNoSub(String id_kalkulasi)
    {
        boolean flag = false;
        get = myKal.getKalkulasiById(id_kalkulasi);
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                if (get.getString(2).equals(""))
                {
                    flag = true; 
                    return flag;
                }
            }
        }
        return flag;
    }



    public void getSub(String id_kalkulasi_up)
    {
        Cursor sat = myKal.getKalkulasiByParrent(id_kalkulasi_up);

        String[] price_det_sub = {""};

        if (sat != null && sat.getCount() > 0)
        {
            while (sat.moveToNext())
            { 

                String id_detail_kal = myDetail.getIdDetailByProject(sat.getString(0), id_project);
                String formula = myDetail.getFormula(id_detail_kal);
                price_detail_sub = new ArrayList<String>();
                readDetailSub(id_detail_kal);
                getVariabelSub(id_detail_kal);

                price_det_sub = price_detail_sub.toArray(new String[price_detail_sub.size()]);
                Double total = 0.0;
                for (int i = 0; i < price_det_sub.length; i++)
                {
                    if (!formula.equals(""))
                    {
                        total = hitung(changeVar(formula, price_det_sub));

                    }
                    else
                    {
                        String totalBarang = getTotalBarang(id_detail_kal);
                        String totalVariabel = getTotalVariabel(id_detail_kal);
                        total = Double.parseDouble(totalBarang) * Double.parseDouble(totalVariabel);
                    }       
                }

                price_detail.add(String.valueOf(total));

            }
        }
    }

    public void readDetail(String id_detail_kalkulasi)
    {

        Cursor res = myItem.getDetailByDetailKalkulasi(id_detail_kalkulasi);

        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                price_detail.add(myHarga.getPerhitunganBarang(id_detail_kalkulasi, res.getString(1)));
            }
        }


    }

    public void getVariabel(String id_detail_kalkulasi)
    {
        Cursor det = myVar.getDetailById(id_detail_kalkulasi);

        if (det != null && det.getCount() > 0)
        {
            while (det.moveToNext())
            { 

                price_detail.add(det.getString(2));
            }
        }

    }
    public void readDetailSub(String id_detail_kalkulasi)
    {

        Cursor res = myItem.getDetailByDetailKalkulasi(id_detail_kalkulasi);
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                price_detail_sub.add(myHarga.getPerhitunganBarang(id_detail_kalkulasi, res.getString(1)));
            }
        }
    }



    public void getVariabelSub(String id_detail_kalkulasi)
    {
        Cursor det = myVar.getDetailById(id_detail_kalkulasi);

        if (det != null && det.getCount() > 0)
        {
            while (det.moveToNext())
            { 
                price_detail_sub.add(det.getString(2));
            }
        }
    }

    public String getTotalBarang(String id_detail_kal)
    {
        Cursor cr = myItem.getDetailByDetailKalkulasi(id_detail_kal);
        Double total = 0.0;
        if (cr != null && cr.getCount() > 0)
        {

            while (cr.moveToNext())
            {
                Double harga = Double.parseDouble(myHarga.getHargaBarang(cr.getString(1)));
                Double besaran = Double.parseDouble(myBarang.getBesaran(cr.getString(1)));
                Double qty = Double.parseDouble(cr.getString(2));
                total += (harga / besaran) * qty;
            }
        }      
        return String.valueOf(total);

    }


    public String getTotalVariabel(String id_detail_kal)
    {
        Cursor get = myVar.getDetailById(id_detail_kal);
        int total = 1;
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            {

                total = total * Integer.parseInt(get.getString(2));
            }
        }

        return String.valueOf(total);
    }




    public void hapus()
    {
        try
        {
            for (int i = 0; i < id_detail_kalkulasi.length;i++)
            {

                myVar.deleteDataByIdDetail(id_detail_kalkulasi[i]);
                myItem.deleteDataByIdDetailKalkulasi(id_detail_kalkulasi[i]);
            }

            myDetail.deleteDataByProject(id_project);
            myDb.deleteData(id_project);
            setText("Data telah dihapus");
        }
        catch (Exception e)
        {
            setText(e.toString());
        }
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
                    Intent in = new Intent(ProjectView.this, MainActivity.class);
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
    public void ShowDialogExit()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Keluar");
        builder.setMessage("Anda akan keluar dari project?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
					
                    Intent in = new Intent(ProjectView.this, MainActivity.class);
                    startActivity(in);
					if (mInterstitialAd.isLoaded()) {
						mInterstitialAd.show();
					} else {
						
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
