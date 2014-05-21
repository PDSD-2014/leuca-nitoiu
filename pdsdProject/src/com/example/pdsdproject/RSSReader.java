package com.example.pdsdproject;

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class RSSReader {
	Document doc;
	volatile private boolean isLocked;
	
	private synchronized void setDoc(Document doc){
		this.doc = doc;
		notify();
	}
		
	public synchronized ArrayList<NewsItem> getNews(String url){
		
		try {
			isLocked = true;
			
			new RetrieveFeedTask().execute(url);
			
			while(isLocked){
				wait();
			}
			
			return readRSS(doc);
		}
		catch (DocumentException e) {
			Log.e("asdf", e.getMessage());
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<NewsItem> readRSS(Document document) throws DocumentException {

        Element root = document.getRootElement();
        
        ArrayList<NewsItem> newsArray = new ArrayList<>();
        
        Element channel = root.element("channel");

        for ( Iterator i = channel.elementIterator( "item" ); i.hasNext(); ) {
            Element item = (Element) i.next();
            
            NewsItem ni = new NewsItem();
            
            for ( Iterator ii = item.elementIterator(); ii.hasNext(); ) {
                Element info = (Element)ii.next();
                
                if(info.getQualifiedName().equals("title")){
                	ni.title = info.getStringValue();
                }
                else if(info.getQualifiedName().equals("description")){
                	ni.description = info.getStringValue();
                }
                else if(info.getQualifiedName().equals("link")){
                	ni.link = info.getStringValue();
                }
//                else if(info.getQualifiedName().equals("category")){
//                	ni.category = info.getStringValue();
//                }
                else if(info.getQualifiedName().equals("pubDate")){
                	ni.pubDate = info.getStringValue();
                }
            }
            
            newsArray.add(ni);
        }
        return newsArray;
     }
	
	class RetrieveFeedTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			URL url;
			try {
				url = new URL(params[0]);
				
				SAXReader reader = new SAXReader();
		        Document document = reader.read(url);
		        
				isLocked = false;
				setDoc(document);
		        
			} catch (MalformedURLException e) {
				Log.e("asdf", e.getMessage());
			}
			catch (DocumentException e) {
				Log.e("asdf", e.getMessage());
			}
			
			return null;
		}
	}
	
}

class NewsItem{
	String title;
	String description;
	String link;
	String category;
	String pubDate;
	
	public String toString(){
		return title;
	}
}

class NewsAdapter extends ArrayAdapter<NewsItem> {
	Activity activity;
	
	public NewsAdapter(Activity context, ArrayList<NewsItem> newsArray) {
       super(context, R.layout.item_news, newsArray);
       activity = context;
    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       final NewsItem item = getItem(position);    
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news, parent, false);
       }
       // Lookup view for data population
       TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
       TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
       TextView tvLink = (TextView) convertView.findViewById(R.id.tvLink);
       // Populate the data into the template view using the data object
       tvTitle.setText(item.title);
       tvTitle.setTypeface(null, Typeface.BOLD);
       tvDescription.setText(item.description);
       
       //make link and handle click
       String linkText = "Click here to read the full article";
       SpannableString content = new SpannableString(linkText);
       content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
       tvLink.setText(content);
       
       tvLink.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
				
				activity.startActivity(browse);
			}
       });
       
//       //make link
//       String linkText = "Click here to read the full article";
//       SpannableString text = new SpannableString(linkText);
//       Object span = new URLSpan(item.link);
//       text.setSpan(span, 0, linkText.length(), 0);       
//       tvLink.setText(text);
       
       // Return the completed view to render on screen
       return convertView;
	}
}

