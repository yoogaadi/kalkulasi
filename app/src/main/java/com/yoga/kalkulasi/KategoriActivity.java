package com.yoga.kalkulasi;
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
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;

public class KategoriActivity extends AppCompatActivity
{

    barangHelper myDb;
    hargaBarangHelper myHarga;
    detailHargaBarangHelper myDetail;
    kategoriHelper myKategori;
    detailItemHelper myItem;

    Toolbar toolbar;

    
    ListView lv;
    Cursor res;
    ArrayAdapter adapter;
    String[] arr, arr_id;
    int totalData = 0;
    String id_user;
    String[] arr_data = {"ce","12"};
    String[] nama_kategori,id_kategori = {};
    String kategori;

    String selectIdKategori = "";

    View view;
    FloatingActionButton fab;




    @Override 
    protected void onCreate(Bundle savedInstanceState)
    { 

        super.onCreate(savedInstanceState); 
        setContentView(R.layout.kategori_activity); 
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        try
        {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        myDb = new barangHelper(this);
        myHarga = new hargaBarangHelper(this);
        myDetail = new detailHargaBarangHelper(this);
        myKategori = new kategoriHelper(this);
        myItem = new detailItemHelper(this);
       
        fab = (FloatingActionButton) findViewById(R.id.fab);

       

            loadData();
            fab.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View p1)
                    {
                        KategoriAction ka = new KategoriAction(KategoriActivity.this, "", "tambah");
                        ka.setOnDismissListener(new DialogInterface.OnDismissListener(){
                                @Override
                                public void onDismiss(DialogInterface p1)
                                {

                                    loadData();
                                }
                            });                  
                    }
                }); 
        }
        catch (Exception e)
        {
            setText(e.toString());
        }

    }

    public void loadData()
    {

        readData();
        if (totalData > 0)
        {
           
            lv = (ListView) findViewById(R.id.listBarang);
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nama_kategori);
            lv.setAdapter(adapter);
            registerForContextMenu(lv);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        // TODO: Implement this method
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select the Action");
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Hapus");

    }


    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo info;
        // TODO: Implement this method
        if (item.getTitle().toString().equals("Edit"))
        {
            info = (AdapterContextMenuInfo) item.getMenuInfo();
            KategoriAction ka = new KategoriAction(KategoriActivity.this, id_kategori[info.position], "edit");
            ka.setOnDismissListener(new DialogInterface.OnDismissListener(){
                    @Override
                    public void onDismiss(DialogInterface p1)
                    {

                        loadData();
                    }
                });

        }
        else if (item.getTitle().toString().equals("Hapus"))
        {
            info = (AdapterContextMenuInfo) item.getMenuInfo();
            //id_barang = arr_id[info.position];
            //id_var = arr_id[info.position];
            ShowDialog();
            setText(String.valueOf(item.getItemId()));
        }

        return super.onContextItemSelected(item);

    }
    public void setText(String teks)
    {
        Toast.makeText(this, teks, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent in = null;
        switch (item.getItemId())
        {

            case android.R.id.home:

                in = new Intent(KategoriActivity.this, MainActivity.class);
                in.putExtra("menu", "Barang");
                startActivity(in);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void readData()
    {

        res = myKategori.getAllData();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer id_buffer = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                stringBuffer.append(res.getString(1) + ",");
                id_buffer.append(res.getString(0) + ",");
            }

        }
        totalData = res.getCount();
        nama_kategori = stringBuffer.toString().split(",");
        id_kategori = id_buffer.toString().split(",");

    }

    public void hapus()
    {

       /* myDb.deleteData(id_barang);   
        myItem.deleteDataByKodeBarang(id_barang);
        myHarga.deleteData(id_barang);
        myDetail.deleteData(id_barang);*/
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
                    loadData();
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
}
