package com.yoga.kalkulasi;
import android.support.v7.app.*;
import android.os.*;
import android.support.v7.widget.Toolbar;

public class NavigationActivity extends AppCompatActivity
{
	public Toolbar toolbar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		
	}
}
