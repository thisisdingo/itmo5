package com.itmo.interfaces;

import com.itmo.models.News;

import java.util.List;

public interface APICallback {

    void didFetchedNews(List<News> news);

}
