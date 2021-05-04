package com.capstone.catstone_eatmorning;

import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class PostRequestHandler extends AsyncTask<Void,Void,String> {
    String url = "52.231.164.5";
    HashMap<String,String> requestedParams;

    PostRequestHandler(String url,HashMap<String,String> params){
        this.url = url;
        this.requestedParams = params;
    }
    protected  void onPreExecute(){
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(Void... voids) {
        BackGroundWorker backGroundWorker = new BackGroundWorker();
        try {
            String s = backGroundWorker.postRequestHandler(url,requestedParams);
            return s.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected void onPostExecute(String s){
        super.onPostExecute(s);
    }
}
