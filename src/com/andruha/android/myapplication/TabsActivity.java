package com.andruha.android.myapplication;

import java.util.Locale;

import com.andruha.android.myapplication.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class TabsActivity extends TabActivity {
    private SharedPreferences preferences;
    private Locale locale;
    private String lang;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_activity);
			
		
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        lang = preferences.getString("lang", "en");	
            if (lang.equals("en")) {lang=getResources().getConfiguration().locale.getCountry();}
            locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, null);
		
		
        TabHost tabHost = getTabHost();
        
        TabHost.TabSpec tabSpec;
        
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.tab_list), getResources().getDrawable(R.drawable.list_img));
        tabSpec.setContent(new Intent(this, ListActivity.class));
        tabHost.addTab(tabSpec);
        
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.tab_scaling), getResources().getDrawable(R.drawable.scaling_img));
        tabSpec.setContent(new Intent(this, ScalingActivity.class));
        tabHost.addTab(tabSpec);
        
        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator(getString(R.string.tab_service), getResources().getDrawable(R.drawable.service_img));
        tabSpec.setContent(new Intent(this, ServiceActivity.class));
        tabHost.addTab(tabSpec);
        
        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator(getString(R.string.tab_map), getResources().getDrawable(R.drawable.map_img));
        tabSpec.setContent(new Intent(this, MyMapActivity.class));
        tabHost.addTab(tabSpec);
        
	}
	
	
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        
        locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);     
    }
	
}
