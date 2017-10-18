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
import android.view.View.*;
import com.yoga.kalkulasi.Tutorial.*;

public class DialogFormula extends Dialog
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
    Button btnSave,btnCancel;
    
    Button btnTambah,btnKurang,btnKali,btnBagi,btnBuka,btnTutup,btnDel,btnClear;
    
    TextView textHasil;
    EditText textFormula;

    NumberControl nc;
    String[] arr_alias = {"X","Y","Z","A","B","C","D","E","F","G","H","I","J","K","L","M","N"};

    String id_kalkulasi,nama,deskripsi ;
    TextView teksDeskripsi,txtTotal;
    ListView lv,ls,lsvar;


    List<String> nama_detail;
    List<String> harga_detail;
    List<String> satuan_detail;
    List<String> price_detail;
    List<String> price_detail_sub;
    List<Button> btnAlias;

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
	


    public DialogFormula(Context context, String id_det_kalkulasi, String id_proj)
    {

        super(context);
       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_formula);
        setTitle("Tambah Barang");

        id_detail_kalkulasi = id_det_kalkulasi;
        id_project = id_proj;

        myDb = new kalkulasiHelper(getContext());
        myDetail = new detailItemHelper(getContext());
        myHarga = new detailHargaBarangHelper(getContext());
        myBarang = new barangHelper(getContext());
        mySatuan = new satuanHelper(getContext());
        myDetKal = new detailKalkulasiHelper(getContext());
        myVar = new variabelHelper(getContext());
        myDetVar = new detailVariabelHelper(getContext());
        nc = new NumberControl();
        
        list = (LinearLayout) findViewById(R.id.detailListItem);

        btnSave = (Button) findViewById(R.id.btnSave);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        textHasil = (TextView) findViewById(R.id.textHasil);
        textFormula = (EditText) findViewById(R.id.textFormula);


        nama_detail = new ArrayList<String>();
        harga_detail = new ArrayList<String>();
        satuan_detail = new ArrayList<String>();
        price_detail = new ArrayList<String>();
        btnAlias = new ArrayList<Button>();

        setButton();
        loadData();

        btnSave.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {

                    try
                    {

                        boolean flag = false;
                        for (int i = 0; i < nama_detail.size(); i++)
                        {
                            if (!textFormula.getText().toString().equals(""))
                            {
                                double hasil = hitung(changeVar(textFormula.getText().toString(), price_det));
                                //textHasil.setText(String.valueOf(hasil));
                                
                                    flag =  myDetKal.updaeFormula(id_detail_kalkulasi, textFormula.getText().toString());
                                    
                               
                                if(flag){
                                    setText("Formula berhasil diubah");
                                }else{
                                    setText("Terjadi kesalahan ketika menyimpan data");
                                }

                            }
                            else
                            {
                                if (textFormula.getText().equals(""))
                                {
                                    setText("Diisi dong");
                                }
                            }       
                        }
                    }
                    catch (Exception e)
                    {
                        setText("Terdapat kesalahan dalam penyimpanan data. Silahkan cek formula kembali");
                    }
                    dismiss();
					
					
                }


            });
        btnCancel.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    dismiss();
                }

            });
        show();
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

    public void loadData()
    {

        getIdKalkulasi();
        readData();

        readDetail(id_detail_kalkulasi);
        getSub(id_kalkulasi);
        getVariabel(id_detail_kalkulasi);

        textFormula.setText(myDetKal.getFormula(id_detail_kalkulasi));

        nama_det = nama_detail.toArray(new String[nama_detail.size()]);
        value_det = harga_detail.toArray(new String[harga_detail.size()]);
        satuan_det = satuan_detail.toArray(new String[satuan_detail.size()]);
        price_det = price_detail.toArray(new String[price_detail.size()]);

        for (int i = 0;i < nama_det.length;i++)
        {
            addLine(i, nama_det[i], value_det[i], satuan_det[i]);
        }

        for (int x=0;x < btnAlias.size();x++)
        {
            final int pos = x;
            btnAlias.get(x).setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View p1)
                    {
                        //setText("sa");
                        textFormula.append(btnAlias.get(pos).getText().toString());
                    }


                });
        }


    }
    public void setText(String teks)
    {
        Toast.makeText(getContext(), teks, Toast.LENGTH_SHORT).show();
    }

    public void addLine(int i, String nama, String harga, String satuan)
    {


        // add edittext
        LinearLayout layout = new LinearLayout(getContext());
        TextView namaVariabel = new TextView(getContext());
        TextView value = new TextView(getContext());
        Button alias = new Button(getContext());
        //TextView satuan = new TextView(getContext());


        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams p4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(p1);

        p2.weight = 3;
        p3.weight = 150;
        p4.width = 50;


        namaVariabel.setLayoutParams(p2);
        namaVariabel.setText(nama + " " + satuan);
        namaVariabel.setId(i);

        value.setLayoutParams(p3);
        value.setText(harga);
        value.setId(200 + i);
        value.setGravity(Gravity.RIGHT);

        alias.setLayoutParams(p4);
        alias.setText(arr_alias[i]);
        alias.setId(300 + i);
        alias.setTextSize(18);
        alias.setGravity(Gravity.CENTER_HORIZONTAL);
        alias.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        alias.setBackgroundColor(getContext().getResources().getColor(R.color.white));

        list.addView(layout);
        layout.addView(alias);
        layout.addView(namaVariabel);
        layout.addView(value);
        //layout.addView(satuan);*/
        btnAlias.add(alias);
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
            //Toast.makeText(getContext(),"ada",Toast.LENGTH_SHORT).show();
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
                            String nama =  potingKata(sat.getString(1));

                            nama_detail.add(nama);
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

        res = myDetail.getDetailByDetailKalkulasi(id_detail_kalkulasi);
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                String nama =  potingKata(myBarang.getNamaBarang(res.getString(1)));
               
                nama_detail.add(nama);
                harga_detail.add(nc.getNumberFormat(Double.parseDouble(myHarga.getPerhitunganBarang(id_detail_kalkulasi, res.getString(1)))));
                price_detail.add(myHarga.getPerhitunganBarang(id_detail_kalkulasi, res.getString(1)));
                satuan_detail.add("(" + res.getString(2) + " " + mySatuan.getNamaSatuan(res.getString(3)) + ")");

            }
        }

    }

    public void readDetailSub(String id_detail_kalkulasi)
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

    public void getVariabel(String id_detail_kalkulasi)
    {
        det = myDetVar.getDetailById(id_detail_kalkulasi);

        if (det != null && det.getCount() > 0)
        {
            while (det.moveToNext())
            { 
                String nama =  potingKata(myVar.getNamaVariabel(det.getString(1)));
                
                nama_detail.add(nama);
                harga_detail.add(det.getString(2) + " " + getSatuanVariabel(det.getString(1)));
                satuan_detail.add("");
                price_detail.add(det.getString(2));
            }
        }
        totalVar = det.getCount();
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
    
    public void setButton(){
        btnTambah = (Button) findViewById(R.id.btnTambah); 
        btnKurang = (Button) findViewById(R.id.btnKurang); 
        btnKali = (Button) findViewById(R.id.btnKali); 
        btnBagi = (Button) findViewById(R.id.btnBagi); 
        btnBuka = (Button) findViewById(R.id.btnBuka); 
        btnTutup = (Button) findViewById(R.id.btnTutup); 
        btnDel = (Button) findViewById(R.id.btnDel);
        btnClear = (Button) findViewById(R.id.btnClear);
        
        btnTambah.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    textFormula.append(btnTambah.getText().toString());
                }            
        });
        btnKurang.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    textFormula.append(btnKurang.getText().toString());
                }            
            });
        btnKali.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    textFormula.append(btnKali.getText().toString());
                }            
            });
        btnBagi.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    textFormula.append(btnBagi.getText().toString());
                }            
            });
        btnBuka.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    textFormula.append(btnBuka.getText().toString());
                }            
            });
        btnTutup.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    textFormula.append(btnTutup.getText().toString());
                }            
            });
        btnDel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    String[] kat = textFormula.getText().toString().split("");
                    StringBuffer kata = new StringBuffer();
                    for(int a = 0; a<kat.length-1;a++){
                        kata.append(kat[a]);
                    }
                    
                    textFormula.setText(kata.toString());
                }            
            });
        btnClear.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    textFormula.setText("");
                }            
            });
    }
    
    public String potingKata(String input){
        String nama = input;
        char[] kata =input.toCharArray();
        StringBuffer sKata = new StringBuffer();
        if(kata.length > 10){
            for(int a = 0; a<10;a++){
                sKata.append(kata[a]);
            }
            sKata.append("...");
            nama = sKata.toString();
        }
        
        return nama;
    }

}
    
