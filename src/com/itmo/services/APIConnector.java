package com.itmo.services;

import com.itmo.interfaces.APICallback;
import com.itmo.models.AIFNews;
import com.itmo.models.LentaNews;
import com.itmo.models.News;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APIConnector {

    public APIConnector(){};

    public List<News> requestLentaNews() throws Exception {

        List<News> news = new ArrayList<>();

        HttpsURLConnection httpClient =
                (HttpsURLConnection) new URL("https://api.lenta.ru/lists/latest").openConnection();
        httpClient.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            JSONArray lentaResponse = new JSONObject(response.toString()).getJSONArray("headlines");

            for (int i = 0; i < lentaResponse.length(); i++) {
                news.add(new LentaNews(lentaResponse.getJSONObject(i)));
            }

        }

        return news;
    }

    public List<News> requestAIFNews() throws Exception {
        List<News> news = new ArrayList<>();

        HttpsURLConnection httpClient =
                (HttpsURLConnection) new URL("https://aif.ru/rss/news.php").openConnection();
        httpClient.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            ByteArrayInputStream input = new ByteArrayInputStream(
                    response.toString().getBytes("UTF-8"));
            Document doc = builder.parse(input);

            Element root = doc.getDocumentElement();

            NodeList items = root.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                news.add(new AIFNews((Element) items.item(i)));
            }


        }

        return news;


    }

}
