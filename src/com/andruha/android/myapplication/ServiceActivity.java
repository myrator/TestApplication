package com.andruha.android.myapplication;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.andruha.android.myapplication.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ServiceActivity extends Activity implements OnClickListener{
	ListView lvParse;
	String url;
	ArrayList<RssItem> rssItems = new ArrayList <RssItem>(); 
	ArrayAdapter<RssItem> aa; 
	Button btnRss, btnShow;
	DownloadRssTask dwnld;
	public static RssItem selectedRssItem = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_activity);
		
		lvParse = (ListView) findViewById(R.id.lvParse);
		url = getResources().getString(R.string.rss_url);
		btnRss = (Button) findViewById(R.id.btnRss);
		btnRss.setOnClickListener(this);
		btnShow = (Button) findViewById(R.id.btnShow);
		btnShow.setOnClickListener(this);
	    lvParse.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> av, View view, int index,
	            long arg3) {
	          selectedRssItem = rssItems.get(index);

	          Intent intent = new Intent("com.andruha.android.myapplication.DisplayRss");
	          startActivity(intent);
	        }
	      });
}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.btnRss:
			dwnld = new DownloadRssTask(this);
			dwnld.execute(url);
			break;
		case R.id.btnShow:
			if (dwnld != null){
				try {
					rssItems = dwnld.get();
					ArrayAdapter<RssItem> aa = new ArrayAdapter<RssItem>(this, R.layout.list_item_rss, R.id.tvTitle, rssItems);
					lvParse.setAdapter(aa);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 1, 0, getResources().getString(R.string.pref));
        mi.setIntent(new Intent(this, MyPreferenceActivity.class));
        return super.onCreateOptionsMenu(menu);
      }
	
	
	
	
	class DownloadRssTask extends AsyncTask<String, Integer , ArrayList<RssItem>> {

		private ArrayList<RssItem> rssItems;
		private ProgressDialog pd;
		private Context cntxt;
		private int max;
		
		public DownloadRssTask(Context context){
			cntxt = context;
		}
		
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(cntxt);
			pd.setTitle(getResources().getString(R.string.dwnldRSS));
			pd.setMessage(getResources().getString(R.string.startdwnld));
			pd.setCancelable(true);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setProgress(0);
			pd.show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... progr){
			super.onProgressUpdate(progr);
			int percent;
			
			pd.setMessage(getResources().getString(R.string.procdwnld));			
			pd.setProgress(progr[0]);
			pd.show();
		}
		
		@Override
		protected ArrayList<RssItem> doInBackground(String... params) {
			// TODO Auto-generated method stub
			rssItems = new ArrayList<RssItem>();
			String urlhttp = params[0];
			
			
			try {
				
			      URL url = new URL(urlhttp);
			      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			      if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			        InputStream is = conn.getInputStream();

			        DocumentBuilderFactory dbf = DocumentBuilderFactory
			            .newInstance();
			        DocumentBuilder db = dbf.newDocumentBuilder();

			        Document document = db.parse(is);
			        Element element = document.getDocumentElement();

			        NodeList nodeList = element.getElementsByTagName("item");
			        max = nodeList.getLength();
			        pd.setMax(max);
			        
			        if (nodeList.getLength() > 0) {
			          for (int i = 0; i < nodeList.getLength(); i++) {

			            Element entry = (Element) nodeList.item(i);

			            Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
			            Element _descriptionE = (Element) entry.getElementsByTagName("description").item(0);
			            Element _pubDateE = (Element) entry.getElementsByTagName("pubDate").item(0);
			            Element _linkE = (Element) entry.getElementsByTagName("link").item(0);

			            String _title = _titleE.getFirstChild().getNodeValue();
			            String _description = _descriptionE.getFirstChild().getNodeValue();
			            @SuppressWarnings("deprecation")
						Date _pubDate = new Date(_pubDateE.getFirstChild().getNodeValue());
			            String _link = _linkE.getFirstChild().getNodeValue();

			            RssItem rssItem = new RssItem(_title, _description, _link,_pubDate);

			            rssItems.add(rssItem);
			            
			            this.publishProgress(i);
			          }
			        }

			      }
			    } catch (Exception e) {
			      e.printStackTrace();
			    }
			
			
			return rssItems;
		}
		
		protected void onPostExecute(ArrayList<RssItem> result) {
			super.onPostExecute(result);
			
			pd.setMessage(getResources().getString(R.string.enddwnld));
			pd.setProgress(100);
			pd.show();
			pd.cancel();
			
		}

	}
	
	
}
