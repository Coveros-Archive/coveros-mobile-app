package com.coveros.coverosmobileapp.website;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLContent{
    private String htmlStuff;
    public URLContent(String saved){ htmlStuff = ""; }

    public static void main(String[] args) throws Exception{
        //String content = URLConnectionReader.getText("https://www.coveros.com");
        //System.out.println(content);
    }

    public static String getText(String link) throws Exception{
        URL url;
        try {
            // get URL content
            url = new URL(link);
            URLConnection conn = url.openConnection();
            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                System.out.println(inputLine);
            }
            br.close();
            return response.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}