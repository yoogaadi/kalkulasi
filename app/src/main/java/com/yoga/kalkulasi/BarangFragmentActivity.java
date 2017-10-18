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
import com.yoga.kalkulasi.Control.*;
import com.yoga.kalkulasi.Tutorial.*;
import java.io.*;

public class BarangFragmentActivity extends Fragment implements SearchView.OnQueryTextListener
{

    barangHelper myDb;
    hargaBarangHelper myHarga;
    detailHargaBarangHelper myDetail;
    kategoriHelper myKategori;
    detailItemHelper myItem;

    ListView lv;
    Cursor res;
    ArrayAdapter adapter;
    String[] arr, arr_id;
    int totalData = 0;
    String id_user;

    String[] nama_kategori,id_kategori = {};
    String[] nama_barang,id_barang = {};

    String selectIdKategori = "-";

    View view;
    FloatingActionButton fab;

    String pilih_kategori = "";
    int totalKategori = 0;
    boolean isSelectedItem = false;
    LinearLayout linearEmpty;

    SearchView search;

    Spinner spinKategori;

    String id_brg = "";
	
	boolean isFirstTime = false;

    public BarangFragmentActivity()
    {

    }

    public static BarangFragmentActivity newInstance()
    {
        BarangFragmentActivity fragment = new BarangFragmentActivity();
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
        myDb = new barangHelper(getContext());
        myHarga = new hargaBarangHelper(getContext());
        myDetail = new detailHargaBarangHelper(getContext());
        myKategori = new kategoriHelper(getContext());
        myItem = new detailItemHelper(getContext());

        view =  inflater.inflate(R.layout.barang_activity, container, false);
        spinKategori = (Spinner) view.findViewById(R.id.spinKategori);
        linearEmpty = (LinearLayout) view.findViewById(R.id.linearEmpty);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        loadData();

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, nama_kategori);
        spinKategori.setAdapter(adapter2);
        spinKategori.setOnItemSelectedListener(new OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
                {
                    selectIdKategori = id_kategori[p3];
                    loadData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> p1)
                {
                    // TODO: Implement this method
                }


            });

        search = (SearchView) view.findViewById(R.id.id_search);
        search.setOnQueryTextListener(this);


        fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1)
                {
                    PopupMenu popup = new PopupMenu(getContext(), fab);
                    popup.getMenuInflater().inflate(R.menu.menu_barang, popup.getMenu());
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
		

		if(getStatusTutorial()){
			TutorialDua cp = new TutorialDua(getContext());
		}
		

        return view;
    }

    public void loadData()
    {

        getKategori();
        readData(selectIdKategori);
        lv = (ListView) view.findViewById(R.id.listBarang);
        if (totalData > 0)
        {
            lv.setVisibility(ListView.VISIBLE);
            linearEmpty.setVisibility(LinearLayout.GONE);
            adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, nama_barang);
            lv.setAdapter(adapter);
            registerForContextMenu(lv);

        }
        else
        {
            lv.setVisibility(ListView.GONE);
            linearEmpty.setVisibility(LinearLayout.VISIBLE);
        }


    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent in = null;
        switch (item.getItemId())
        {

            case R.id.kelolaKategori:
                in = new Intent(getContext(),KategoriActivity.class);
                startActivity(in);
                return true;
            case R.id.tambahBarang:
                final DialogBarangAction ba = new DialogBarangAction(getContext(), "", "tambah");
                ba.setOnDismissListener(new DialogInterface.OnDismissListener(){
                        @Override
                        public void onDismiss(DialogInterface p1)
                        {
							
							if(ba.getStatus()){
								loadData();
								
								
								if(getStatusTutorial()){
									Intent in = new Intent(getContext(), MainActivity.class);
									in.putExtra("menu","Kalkulasi");
									getContext().startActivity(in);
									

								}		
							}
							
                          
                        }
                    }); 
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String p1)
    {
        // TODO: Implement this method
        return false;
    }

    @Override
    public boolean onQueryTextChange(String p1)
    {
        // TODO: Implement this method
        String newText = p1;
        adapter.getFilter().filter(newText);
        return false;
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
            
            for(int a = 0; a<nama_barang.length;a++){
                if(adapter.getItem(info.position).equals(nama_barang[a])){
                    id_brg = id_barang[a];
                }
            }
            final DialogBarangAction ba = new DialogBarangAction(getContext(), id_brg, "edit");
            ba.setOnDismissListener(new DialogInterface.OnDismissListener(){
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

            for(int a = 0; a<nama_barang.length;a++){
                if(adapter.getItem(info.position).equals(nama_barang[a])){
                    id_brg = id_barang[a];
                }
            }
            
            ShowDialog();
        }
        return super.onContextItemSelected(item);
    }
    public void setText(Context con, String teks)
    {
        Toast.makeText(con, teks, Toast.LENGTH_SHORT).show();
    }


    public void readData(String id_kategori)
    {
        if (id_kategori.equalsIgnoreCase("-"))
        {
            res = myDb.getAllData();
        }
        else
        {
            res = myDb.getBarangByKategori(id_kategori);
        }

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
        nama_barang = stringBuffer.toString().split(",");
        id_barang = id_buffer.toString().split(",");

    }

    public void getKategori()
    {
        Cursor res = myKategori.getAllData();
        int a = 0;
        StringBuffer nama = new StringBuffer();
        StringBuffer id = new StringBuffer();
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            {
                if (a == 0)
                {
                    nama.append("Tampilkan semua,");
                    id.append("-,");
                }
                nama.append(res.getString(1) + ",");
                id.append(res.getString(0) + ",");
                a++;
            }
        }
        totalKategori = res.getCount();
        id_kategori = id.toString().split(",");
        nama_kategori = nama.toString().split(",");
    }


    public void hapus()
    {
        try
        {
            myDb.updateKategori(pilih_kategori);
            myKategori.deleteData(pilih_kategori);
        }
        catch (Exception e)
        {
            setText(getContext(), e.toString());
        }
        setText(getContext(), "Data telah dihapus");
    }

    public void hapusBarang()
    {

       
        myDb.deleteData(id_brg);   
        myItem.deleteDataByKodeBarang(id_brg);
        myHarga.deleteData(id_brg);
        myDetail.deleteData(id_brg);
        setText(getContext(),"Data telah dihapus");
    }

    public void ShowDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        
        builder.setCancelable(false);
        builder.setTitle("Hapus");
        builder.setMessage("Anda yakin akan menghapus data ini?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    hapusBarang();
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
