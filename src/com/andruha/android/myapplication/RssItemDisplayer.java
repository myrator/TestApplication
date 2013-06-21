package com.andruha.android.myapplication;

import java.text.SimpleDateFormat;

import com.andruha.android.myapplication.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RssItemDisplayer extends Activity {
	@SuppressLint("SimpleDateFormat")
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.rss_displayer);

	    RssItem selectedRssItem = ServiceActivity.selectedRssItem;
	    TextView tvTitlerss = (TextView)findViewById(R.id.tvTitlerss);
	    TextView tvDescr = (TextView)findViewById(R.id.tvDescr);
	    TextView tvLink = (TextView)findViewById(R.id.tvLink);
	    TextView tvDate = (TextView)findViewById(R.id.tvDate);
	    
	    String title = selectedRssItem.getTitle();
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd - hh:mm:ss");
	    String date = sdf.format(selectedRssItem.getPubDate());

	    String descr = selectedRssItem.getDescr();
	    String link = selectedRssItem.getLink();

	    tvTitlerss.setText(title);
	    tvDescr.setText(descr);
	    tvLink.setText(link);
	    tvDate.setText(date);
	  }
}
