package com.example.pdsdproject;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class NewsListActivity extends Activity{
	private ListView lv;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.news_activity);
		
		lv = (ListView) findViewById(R.id.lvItems);	
		
		Intent i = getIntent();
		String url = i.getStringExtra("url");
		
		setTitle(i.getStringExtra("display"));
		
		
		ArrayList<NewsItem> newsList = new RSSReader()
			.getNews(url);
	
		NewsAdapter arrayAdapter = new NewsAdapter(
				this, 
				newsList );
	
		lv.setAdapter(arrayAdapter);
		
	}
}
