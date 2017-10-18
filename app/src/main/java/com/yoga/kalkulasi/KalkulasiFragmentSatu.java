package com.yoga.kalkulasi;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;
import com.yoga.kalkulasi.Model.*;
import android.database.*;
import android.widget.Toast;
import android.widget.*;
import com.yoga.kalkulasi.Control.*;
import java.util.*;
import android.view.LayoutInflater;
import android.util.*;
import android.view.View.*;
import android.content.*;
import com.yoga.kalkulasi.Tutorial.*;
import java.io.*;


public class KalkulasiFragmentSatu extends Fragment
{

    kalkulasiHelper myDb;
    barangHelper myBarang;
    detailHargaBarangHelper myHarga;
    detailItemHelper myDetail;
    satuanHelper mySatuan;
    detailKalkulasiHelper myDetKal;
    variabelHelper myVar;
    detailVariabelHelper myDetVar;
    LinearLayout list;

    NumberControl nc;

    String id_kalkulasi,nama,deskripsi,formula = "" ;
    TextView teksDeskripsi,txtTotal;
    ListView lv,ls,lsvar;

    String[] arr_alias = {"X","Y","Z","A","B","C","D","E","F","G","H","I","J","K","L","M","N"};


    List<String> nama_detail;
    List<String> harga_detail;
    List<String> satuan_detail;
    List<String> price_detail;
    List<String> price_detail_sub;

    LinearLayout layout;

    Button btnOption;


    //String[] total_sub,besaran_var,satuan_var, nama_barang,kode_barang, harga_barang, qty_barang,satuan_item,item_price= {};
    String[] nama_det, value_det,satuan_det,price_det = {""};
    String[] nama_sub = {};
    String[] nama_var = {};
    int totalData,totalSub,totalVar = 0;
    String id_detail_kalkulasi;
    String id_project = "-";
    LayoutInflater layoutInflater;


    Cursor res,bar,det,sat;
    View view;
    String kd_barang;
	
	boolean isFirstTime = false;

    public KalkulasiFragmentSatu()
    {


        //namaVar.add(namaVariabel);

    }
    public KalkulasiFragmentSatu(String id, String id2, String id_proj)
    {

        id_detail_kalkulasi = id2;
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
        View view = inflater.inflate(R.layout.kalkulasi_fragment_1, container, false);
        teksDeskripsi = (TextView) view.findViewById(R.id.teksDeskripsi);
        txtTotal = (TextView) view.findViewById(R.id.txtTotal);

        myDb = new kalkulasiHelper(getContext());
        myDetail = new detailItemHelper(getContext());
        myHarga = new detailHargaBarangHelper(getContext());
        myBarang = new barangHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        myDetKal = new detailKalkulasiHelper(getContext());
        myVar = new variabelHelper(getContext());
        myDetVar = new detailVariabelHelper(getContext());
        nc = new NumberControl();
        list = (LinearLayout)view.findViewById(R.id.detailList);




        btnOption = (Button) view.findViewById(R.id.btnFormula);

        btnOption.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    DialogFormula dv = new DialogFormula(getContext(), id_detail_kalkulasi, id_project);
                    //dv.showDialog(getContext(),"Edit Variabel");
                    dv.setOnDismissListener(new DialogInterface.OnDismissListener(){
                            @Override
                            public void onDismiss(DialogInterface p1)
                            {
                                readFormula();
								
								if(getStatusTutorial()){
									TutorialTuju tj = new TutorialTuju(getContext());
									Intent in = new Intent(getContext(), MainActivity.class);
									//in.putExtra("menu","Project");
									in.putExtra("tutorial","sembilan");
									startActivity(in);
									
								}
                            }
                        });
                }


            });

        nama_detail = new ArrayList<String>();
        harga_detail = new ArrayList<String>();
        satuan_detail = new ArrayList<String>();
        price_detail = new ArrayList<String>();
        //setText(id_kalkulasi);


        lv = (ListView) view.findViewById(R.id.listDetail);
        ls = (ListView) view.findViewById(R.id.listSub);
        lsvar = (ListView) view.findViewById(R.id.listVar);


        loadData();
        readFormula();

        return view;
    }



    public void readFormula()
    {
        formula = myDetKal.getFormula(id_detail_kalkulasi);

        try
        {

            for (int i = 0; i < nama_detail.size(); i++)
            {
                if (!formula.equals(""))
                {
                    double hasil = hitung(changeVar(formula, price_det));
                    txtTotal.setText(nc.getNumberFormat(hasil));
                }
                else
                {
                    if (formula.equals(""))
                    {
                        txtTotal.setText("Rp.0");
                    }
                }       
            }
        }
        catch (Exception e)
        {
            txtTotal.setText("Error, cek formula");
            //setText(e.toString());
        }
    }
    public void loadData()
    {

        getIdKalkulasi();
        readData();
        readDetail(id_detail_kalkulasi);
        getSub(id_kalkulasi);
        getVariabel(id_detail_kalkulasi);


        nama_det = nama_detail.toArray(new String[nama_detail.size()]);
        value_det = harga_detail.toArray(new String[harga_detail.size()]);
        satuan_det = satuan_detail.toArray(new String[satuan_detail.size()]);
        price_det = price_detail.toArray(new String[price_detail.size()]);


        for (int i = 0;i < nama_det.length;i++)
        {
            addLine(i, nama_det[i], value_det[i], satuan_det[i]);
        }



        teksDeskripsi.setText(deskripsi);	
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

    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }

    public void addLine(int i, String nama, String harga, String satuan)
    {


        // add edittext
        layout = new LinearLayout(getContext());

        TextView namaVariabel = new TextView(getContext());
        TextView value = new TextView(getContext());
        //TextView satuan = new TextView(getContext());


        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(p1);

        p2.weight = 1;
        p3.weight = 1;


        namaVariabel.setLayoutParams(p2);
        namaVariabel.setText(nama + " " + satuan);
        namaVariabel.setId(i);

        value.setLayoutParams(p3);
        value.setText(harga);
        value.setId(200 + i);
        value.setGravity(Gravity.RIGHT);


        list.addView(layout);
        layout.addView(namaVariabel);
        layout.addView(value);
    }

    public void getIdKalkulasi()
    {
        res = myDetKal.getDetailById(id_detail_kalkulasi);
        String id= "";
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                id = res.getString(1);
                id_project = res.getString(2);

            }
        }
        id_kalkulasi = id;
    }



    public void readData()
    {
        res = myDb.getKalkulasiById(id_kalkulasi);
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                nama = res.getString(1);
                deskripsi = res.getString(3);
            }
        }
        else
        {

        }
    }



    public void getSub(String id_kalkulasi_up)
    {
        sat = myDb.getKalkulasiByParrent(id_kalkulasi_up);

        String[] price_det_sub = {""};

        if (sat != null && sat.getCount() > 0)
        {
            while (sat.moveToNext())
            { 
                Cursor get = myDetKal.getDetailKalkulasi(sat.getString(0), id_project);

                if (get != null && get.getCount() > 0)
                {
                    while (get.moveToNext())
                    {
                        if (get.getString(2).toString().equals(id_project))
                        {

                            String id_detail_kal = myDetKal.getIdDetailByProject(sat.getString(0), id_project);
                            String formula = myDetKal.getFormula(id_detail_kal);
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

                            satuan_detail.add("");
                            nama_detail.add(sat.getString(1));
                            harga_detail.add(nc.getNumberFormat(total));
                            price_detail.add(String.valueOf(total));
                        }else{
                            continue;
                        }
                    }
                }

            }
        }
    }

    public void readDetail(String id_detail_kalkulasi)
    {
        try
        {
            res = myDetail.getDetailByDetailKalkulasi(id_detail_kalkulasi);

            StringBuffer hargaBarang = new StringBuffer();
            if (res != null && res.getCount() > 0)
            {
                while (res.moveToNext())
                {

                    nama_detail.add(myBarang.getNamaBarang(res.getString(1)));
                    harga_detail.add(nc.getNumberFormat(Double.parseDouble(myHarga.getPerhitunganBarang(id_detail_kalkulasi, res.getString(1)))));
                    price_detail.add(myHarga.getPerhitunganBarang(id_detail_kalkulasi, res.getString(1)));
                    satuan_detail.add("(" + res.getString(2) + " " + mySatuan.getNamaSatuan(res.getString(3)) + ")");

                    /*
                     double hg = Double.parseDouble(myHarga.getHargaBarang(det.getString(1)));
                     double qt = Double.parseDouble(det.getString(2));
                     double besaran = Double.parseDouble(myBarang.getBesaran(det.getString(1)));
                     double total = ((hg / besaran) * qt);
                     harga_detail.add(nc.getNumberFormat(total));*/
                }
            }
        }
        catch (Exception e)
        {
            setText(e.toString());
        }


    }

    public void getVariabel(String id_detail_kalkulasi)
    {
        det = myDetVar.getDetailById(id_detail_kalkulasi);

        if (det != null && det.getCount() > 0)
        {
            while (det.moveToNext())
            { 
                nama_detail.add(myVar.getNamaVariabel(det.getString(1)));
                harga_detail.add(det.getString(2) + " " + getSatuanVariabel(det.getString(1)));
                satuan_detail.add("");
                price_detail.add(det.getString(2));
            }
        }
        totalVar = det.getCount();
    }

    public void readDetailSub(String id_detail_kalkulasi)
    {
        try
        {



            res = myDetail.getDetailByDetailKalkulasi(id_detail_kalkulasi);
            if (res != null && res.getCount() > 0)
            {
                while (res.moveToNext())
                { 
                    price_detail_sub.add(myHarga.getPerhitunganBarang(id_detail_kalkulasi, res.getString(1)));
                }
            }
        }
        catch (Exception e)
        {
            setText(e.toString());
        }
    }



    public void getVariabelSub(String id_detail_kalkulasi)
    {
        det = myDetVar.getDetailById(id_detail_kalkulasi);

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
        try
        {
            Cursor cr = myDetail.getDetailByDetailKalkulasi(id_detail_kal);
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
        catch (Exception e)
        {
            setText(e.toString());
        }

        return "";

    }


    public String getTotalVariabel(String id_detail_kal)
    {
        Cursor get = myDetVar.getDetailById(id_detail_kal);
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


    public String getNamaVariabel(String id_variabel)
    {
        sat = myVar.getVariableById(id_variabel);
        String nama = "";
        if (sat != null && sat.getCount() > 0)
        {

            while (sat.moveToNext())
            { 
                nama = sat.getString(1);
            }

        }
        return nama;
    }

    public String getSatuanVariabel(String id_variabel)
    {
        res = myVar.getVariableById(id_variabel);
        String satuan = "";
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                satuan = mySatuan.getNamaSatuan(res.getString(2));

            }

        }
        return satuan;
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