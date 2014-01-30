package com.refinehumanity.dbpartymemberdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NewGame extends Activity {
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		
		//Setting up new game button
		Button newGameButton = (Button)findViewById(R.id.new_game_button);
		newGameButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent();
				i.putExtra("new", true);
				setResult(RESULT_OK, i);
				finish();
			}
		});
		
		
		Button loadGameButton = (Button)findViewById(R.id.load_game_button);
		loadGameButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent();
				i.putExtra("new", false);
				setResult(RESULT_OK, i);
				finish();
			}
		});		
		
	}
	

}
