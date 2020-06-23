package com.itmo.models;

public class DuplicateAnalyzeResult {

    private News news;
    private int retryCount;

    private DuplicateAnalyzeResult(News news, int retryCount) {
        this.news = news;
        this.retryCount = retryCount;
    }

    public News getNews() {
        return news;
    }

    public int getRetryCount() {
        return retryCount;
    }
}
