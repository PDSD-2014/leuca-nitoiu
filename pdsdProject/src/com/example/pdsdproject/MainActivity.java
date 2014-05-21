package com.example.pdsdproject;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ListView lv;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		setTitle("Reuters News Feed");
		
		lv = (ListView) findViewById(R.id.lvTopics);
		
		String[] topics = {
				"artsculture",
				"businessNews",
				"companyNews",
				"entertainment",
				"lifestyle",
				"MostRead",
				"oddlyEnoughNews",
				"peopleNews",
				"PoliticsNews",
				"ReutersBusinessTravel",
				"scienceNews",
				"sportsNews",
				"technologyNews",
				"topNews",
				"wealth",
				"worldNews"
		};
		
		TopicAdapter topicAdapter = new TopicAdapter(
        		this,
        		topics );
		
		lv.setAdapter(topicAdapter);
	}
	
	class TopicAdapter extends ArrayAdapter<String> {
		Activity activity;
		
		public TopicAdapter(Activity context, String[] urls) {
	       super(context, R.layout.topic_item, urls);
	       activity = context;
	    }
		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	       // Get the data item for this position
	       final String topic = getItem(position);    
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.topic_item, parent, false);
	       }
	       
	       // Lookup view for data population
	       TextView tvTopic = (TextView) convertView.findViewById(R.id.tvTopic);
	       // Populate the data into the template view using the data object
	       
	       final String urlBase =  "http://www.reuters.com/rssFeed/";
	       
	       //make link and handle click
//	       String linkText = "Click here to read the full article";
	       
	       final String displayText = getDisplayText(topic);
	       
	       SpannableString content = new SpannableString(displayText);
	       content.setSpan(new RelativeSizeSpan(1.7f), 0, content.length(), 0);
	       tvTopic.setText(content);
	       tvTopic.setTypeface(null, Typeface.BOLD);
	       
	       tvTopic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent nextScreen = new Intent(activity, NewsListActivity.class);
					
					nextScreen.putExtra("url", urlBase + topic);
					nextScreen.putExtra("display", displayText);
					
					activity.startActivity(nextScreen);
				}
	       });
	       
	       return convertView;
		}
		
		private String getDisplayText(String topic){
			String displayText = "";
			
			boolean capitalize = true;
			for(int i = 0; i < topic.length(); ++i){
				if(topic.charAt(i) < 'a'){
					if(!capitalize)
						displayText += " ";
					
					capitalize = true;
				}
				
				if(capitalize){
					displayText += topic.substring(i, i + 1).toUpperCase();
					capitalize = false;
				}else{
					displayText += topic.charAt(i);
				}
			}
			
			return displayText;
		}
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
