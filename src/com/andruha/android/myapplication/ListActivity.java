package com.andruha.android.myapplication;

import com.andruha.android.myapplication.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ListActivity extends Activity {
	ListView lv;
	DB mydb;
	SimpleCursorAdapter scAdapter;
	Cursor cursor;
	Button btnDone, btnRevert;
	EditText etText;
	LinearLayout llView;
	final int DIALOG_ADD = 1;
	final int DIALOG_SAVE = 2;
	final int DIALOG_EDIT = 3;
	final int CONTEXT_MENU_EDIT_ITEM = 1;
	final int CONTEXT_MENU_DELETE_ITEM = 2;
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);
		
		lv = (ListView) findViewById(R.id.listView);
		//lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		
		mydb = new DB(this);
		mydb.open();
		
		cursor = mydb.getAllData();
		
		String[] from = new String[] {DB.COLUMN_IMG, DB.COLUMN_TXT, DB.COLUMN_IMG};
		int[] to = new int[] {R.id.iv, R.id.tvLocation, R.id.cb};
		scAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to);
		
		scAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {			
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				// TODO Auto-generated method stub
				int imgIndex = cursor.getColumnIndex("img");
				int id = view.getId();

				if (imgIndex == columnIndex) {
					int img = cursor.getInt(imgIndex);
					if (id == R.id.iv)
					{
					ImageView iv = (ImageView) view; 
					switch (img) {
					case 0: iv.setImageDrawable(getResources().getDrawable(R.drawable.img1));	
						break;
					case 1: iv.setImageDrawable(getResources().getDrawable(R.drawable.img2));
					}
					return true;
					}
					
					if (id == R.id.cb) {
						CheckBox cb = (CheckBox) view;
						switch (img) {
						case 0: cb.setChecked(false);
							break;
						case 1: cb.setChecked(true);
						}
						cb.setOnClickListener(new View.OnClickListener() {					
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								boolean f = ((CheckBox) v).isChecked();
								LinearLayout ll = (LinearLayout)  v.getParent();
								ImageView imv = (ImageView) ll.findViewById(R.id.iv);
								TextView tv = (TextView) ll.findViewById(R.id.tvLocation);
								String txt = tv.getText().toString();
								if (f) {
									imv.setImageDrawable(getResources().getDrawable(R.drawable.img2));
									mydb.updateRec(txt, 1);
									}
								else { 
									imv.setImageDrawable(getResources().getDrawable(R.drawable.img1));
									mydb.updateRec(txt, 0);
									}
							}
						});
						return true;
					}
				}	
				return false;
			}
		});
		
		
		lv.setAdapter(scAdapter);
	    lv.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
	        	Bundle args = new Bundle();
	        	int idd = (int) id;
	        	args.putInt("id", idd);
	        	showDialog(DIALOG_EDIT, args);
	        }
	      });
	    lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
			    menu.setHeaderTitle(getResources().getString(R.string.list_context_menu));
			    String[] menuItems = getResources().getStringArray(R.array.list_menu_array);
			      menu.add(Menu.NONE, CONTEXT_MENU_EDIT_ITEM , 0, menuItems[0]);
			      menu.add(Menu.NONE, CONTEXT_MENU_DELETE_ITEM, 0, menuItems[1]);
			    }	    	
	    });
	    

}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onContextItemSelected(MenuItem Item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) Item
				.getMenuInfo();
		switch (Item.getItemId()) {
		case CONTEXT_MENU_EDIT_ITEM:
			int idd  =  (int) lv.getItemIdAtPosition(menuInfo.position);
			Bundle args = new Bundle();
			args.putInt("id", idd);
			Log.d("tag", "edit id = " + idd);
			showDialog(DIALOG_EDIT, args);
			return true; 
		case CONTEXT_MENU_DELETE_ITEM:
			idd = (int) lv.getItemIdAtPosition(menuInfo.position);
			mydb.deleteRec(idd);
			Log.d("tag", "delete id = " + idd);
			cursor.requery();
			return true;
		}
		return false;
	}
	
	
	  @SuppressWarnings("deprecation")
	@Override
	  protected Dialog onCreateDialog(int id, Bundle args) {
		if (id == DIALOG_ADD) {
	    AlertDialog.Builder adb = new AlertDialog.Builder(this);
	    adb.setTitle(getResources().getString(R.string.add_item_dialog));
	    // создаем view из dialog.xml
	    llView = (LinearLayout) getLayoutInflater()
	        .inflate(R.layout.dialog, null);
	    adb.setView(llView);
	    etText = (EditText) llView.findViewById(R.id.etText);
	    
	    adb.setNegativeButton(getResources().getString(R.string.revert), new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				etText.setText("");
				dialog.cancel();
			}
	    	});
	    	    
	    adb.setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String txt = etText.getText().toString();
				int img = 0;
				mydb.addRec(txt, img);
				cursor.requery();
			}
		});
	    
	    adb.setOnKeyListener(new Dialog.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				String txt = etText.getText().toString();
				if (keyCode == KeyEvent.KEYCODE_BACK && txt != ""){
					showDialog(DIALOG_SAVE);
					dialog.cancel();
				}
				return false;
			}
	    	
	    });
	    return adb.create();
		}
		
		if (id == DIALOG_SAVE) {
		    AlertDialog.Builder adb = new AlertDialog.Builder(this);
		    adb.setTitle(getResources().getString(R.string.save_item_dialog));
		    adb.setNegativeButton(getResources().getString(R.string.revert), new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					etText.setText("");
					dialog.cancel();
				}
		    	});
		    
		    adb.setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String txt = etText.getText().toString();
					int img = 0;
					mydb.addRec(txt, img);
					cursor.requery();
				}
			});
		    
		    return adb.create();
		}
		
		if (id == DIALOG_EDIT) {
		    AlertDialog.Builder adb = new AlertDialog.Builder(this);
		    adb.setTitle(getResources().getString(R.string.edit_item_dialog));
		    // создаем view из dialog.xml
		    llView = (LinearLayout) getLayoutInflater()
		        .inflate(R.layout.dialog, null);
		    adb.setView(llView);
		    etText = (EditText) llView.findViewById(R.id.etText);
		    
		    adb.setNegativeButton(getResources().getString(R.string.revert), new DialogInterface.OnClickListener() {	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					etText.setText("");
					dialog.cancel();
				}
		    	});
		    final int idd = args.getInt("id");
		    
		    adb.setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String txt = etText.getText().toString();
					int img = 0;
					mydb.updateRec(idd, txt, img);
					cursor.requery();
				}
			});
		    
		    return adb.create();
			}
		
		return super.onCreateDialog(id);
	  }
	
	  @SuppressWarnings("deprecation")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        super.onPrepareDialog(id, dialog);    		    
	  }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	      // TODO Auto-generated method stub
	      menu.add(0, 1, 0, getResources().getString(R.string.add_item_dialog));
	      MenuItem mi = menu.add(0, 2, 0, getResources().getString(R.string.pref));
	      mi.setIntent(new Intent(this, MyPreferenceActivity.class));
	      return super.onCreateOptionsMenu(menu);
	    }
	    
	    
	    @SuppressWarnings("deprecation")
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	      // TODO Auto-generated method stub
	      if (item.getItemId() == 1) { showDialog(DIALOG_ADD);}
	      return super.onOptionsItemSelected(item);
	    }
	  
	  
}
	
	
