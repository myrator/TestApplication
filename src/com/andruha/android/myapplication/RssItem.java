package com.andruha.android.myapplication;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RssItem {
	private String title;
	private String link;
	private String descr;
	private Date pubDate;
	
	public RssItem(String mtitle, String mlink, String mdescr, Date mdate){
		title = mtitle;
		link = mlink;
		descr = mdescr;
		pubDate = mdate;
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	  public String toString() {

	    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd - hh:mm:ss");

	    String result = getTitle() + "  ( " + sdf.format(this.getPubDate()) + " )";
	    return result;
	  }
	
	public String getTitle() {
		return title;
	}
	
	public String getLink() {
		return link;
	}
	
	public String getDescr() {
		return descr;
	}
	
	public Date getPubDate() {
		return pubDate;
	}
}
