package com.andruha.android.myapplication;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MyPreferenceActivity extends PreferenceActivity {

	
	
	 @SuppressWarnings("deprecation")
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.pref);

}
	 
}
