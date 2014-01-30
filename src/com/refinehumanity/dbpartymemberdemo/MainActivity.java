package com.refinehumanity.dbpartymemberdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	
	public static final String BUTTON_CLICK = "BUTTON_CLICK";
	public static final String BUTTON_TAG = "Button was clicked.";
	
	public boolean newGame = false;

	public List<PartyMembers> partyMembersList = new ArrayList<PartyMembers>();
	PartyMembers mainCharacter = new PartyMembers();
	
	/* onCreate sets list adapter using a new PartyAdapter) */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent i = new Intent(this, NewGame.class);
		startActivityForResult(i, 1);
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		
		//Creating PartyAdapter, setting list adapter
		PartyAdapter pAdapter = new PartyAdapter();
		setListAdapter(pAdapter);
		
		//new LoadCursorTask().execute();
		
		//Set up button to add new party member to partyMemberList
		Button button = (Button) findViewById(R.id.get_party_member);
		button.setText("Add new party member");
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				
				PartyMembers p = new PartyMembers();
				partyMembersList.add(p);
				Log.v(BUTTON_CLICK, BUTTON_TAG);
				Log.v(BUTTON_CLICK, p.getName());
				ViewGroup v = (ViewGroup)findViewById(R.id.activity_main);
				
				//This seems to update all views successfully
				v.requestLayout();
			}
		});
		
		//Set up new button to clear partyMemberList
		Button clearButton = (Button)findViewById(R.id.clear_party_members);
		clearButton.setText("Clear party members");
		clearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				
				partyMembersList.clear();
				ViewGroup v = (ViewGroup)findViewById(R.id.activity_main);
				
				//This seems to update all views successfully
				v.requestLayout();
			}
		});
		
		
		//Set up button for adding party members to db
		Button databaseButton = (Button)findViewById(R.id.add_to_database);
		databaseButton.setText("Add party to database");
		databaseButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				
				//Working on putting db stuff here
				
				DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
				SQLiteDatabase partydb = dbhelper.getWritableDatabase();
				
				for (PartyMembers pmembers : partyMembersList) {
					partydb.execSQL(
							"INSERT INTO partymembers (name, sex, health, healthmax, moral, moralmax) VALUES ('" + pmembers.name +"', '" + pmembers.sex +"', " + pmembers.health +", " + pmembers.healthMax +", " + pmembers.moral +", " + pmembers.moralMax +");"
							);
					
					
				}
				
				
				ViewGroup v = (ViewGroup)findViewById(R.id.activity_main);
				
				//This seems to update all views successfully
				v.requestLayout();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == 1) {
			
			if (resultCode == RESULT_OK) {
				Boolean result = data.getBooleanExtra("new", false);
				loadPartyDatabase(result);
			}
			
			if (resultCode == RESULT_CANCELED) {
				return;
			}
		}
	}
	
	//Load party database on Load Game or delete contents of db on New Game
	protected void loadPartyDatabase(Boolean isNewGame) {
		if (isNewGame) {
			DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
			SQLiteDatabase partyDatabase = dbhelper.getWritableDatabase();
			partyDatabase.execSQL("DELETE FROM partymembers");
			partyMembersList.clear();
		}
		else {
			
			//Create databasehelper, sqlite database object, and cursor (pointing to set of all partymembers table data
			DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
			SQLiteDatabase partyDatabase = dbhelper.getWritableDatabase();
			Cursor partyCursor = partyDatabase.rawQuery(
					"SELECT * FROM partymembers", null);
			
			//Moves through all rows of set of partymembers table data
			//Sets PartyMembers attributes
			while (partyCursor.moveToNext()) {
				int i = partyCursor.getPosition();
				
				String n = partyCursor.getString(1);
				String s = partyCursor.getString(2);
				PartyMembers partyMember = new PartyMembers(n, s);
				
				partyMembersList.add(partyMember);
				//partyMembersList.get(i).name = partyCursor.getString(1);
				//partyMembersList.get(i).sex = partyCursor.getString(2);
				partyMembersList.get(i).health = partyCursor.getInt(3);
				partyMembersList.get(i).healthMax = partyCursor.getInt(4);
				partyMembersList.get(i).moral = partyCursor.getInt(5);
				partyMembersList.get(i).moralMax = partyCursor.getInt(6);
				
				Log.v("Sex created", partyMembersList.get(i).name + ".."+partyMembersList.get(i).sex+"..");
			}
		}
	}
	
	
	//PartyAdapter class definition
	class PartyAdapter extends ArrayAdapter<PartyMembers> {
		
		//Constructor
		PartyAdapter(){
			super(MainActivity.this, R.layout.row, R.id.party_member_name_view, partyMembersList);
		}
		
		//getView method
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View row = super.getView(position, convertView, parent);
			
			ImageView icon = (ImageView) row.findViewById(R.id.party_member_icon);
			
			
			
			if (partyMembersList.get(position).sex.contentEquals("male")) {
				icon.setImageResource(R.drawable.ic_blaine);
			}
			else if (partyMembersList.get(position).getSex().contentEquals("female")) {
				icon.setImageResource(R.drawable.ic_cynthia);
			}
			else {icon.setImageResource(R.drawable.ic_launcher);}
			//Could set if conditions for which person it is here
			//Ex if blaine icon.setImageResource(R.drawable.ic_blaine)
			
			//Setting party member's health in view
			TextView healthView = (TextView) row.findViewById(R.id.health);
			healthView.setText("Health: " + partyMembersList.get(position).getHealth());
			
			//Setting party member's moral in view
			TextView moralView = (TextView) row.findViewById(R.id.moral);
			moralView.setText("Morale: " + partyMembersList.get(position).getMoral());
			
			//Setting up action button for adding party memebers to db
			//Probably not right place to have button
			//Button button = (Button) row.findViewById(R.id.action_button);
			//button.setText("Add to database: " + partyMembersList.get(position).getName());
			

			//More stuff to do here for health and moral
			
			return(row);
		}
		
	}
	
	/*public void onClick(View view) {
		switch (view.getId()) {
		case R.id.new_button:
			openNewGameDialog();
			break;
		case R.id.exit_button:
			finish();
			break;
		}
	}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
