package com.itmo;

import com.itmo.models.DuplicateAnalyzeResult;
import com.itmo.models.News;
import com.itmo.services.APIConnector;
import com.itmo.services.TanimotoHandler;

import java.util.ArrayList;
import java.util.List;

class App {

    private APIConnector apiConnector = new APIConnector();

    private List<News> news = new ArrayList<>();
    private List<DuplicateAnalyzeResult> results = null;

    App(){
        parseNews();
        findDuplicates();
    }

    private void parseNews(){
        try {
            System.out.println("Начинаю парсить статьи Lenta.RU");
            List<News> lentaNews = apiConnector.requestLentaNews();
            news.addAll(lentaNews);
            System.out.println("Получено " + lentaNews.size() + " постов");
            System.out.println("Начинаю парсить статьи АИФ");
            List<News> aifNews = apiConnector.requestAIFNews();
            news.addAll(aifNews);
            System.out.println("Получено " + aifNews.size() + " постов");

        }catch(Exception exception) {
            System.out.println("Ошибка при получении данных. Сообщение: " + exception.getLocalizedMessage());
        }
    }

    private void findDuplicates(){
        try {
            TanimotoHandler tanimotoHandler = new TanimotoHandler(this.news);
            this.results = tanimotoHandler.getRetryCounts();
        }catch(Exception exception) {
            System.out.println("Ошибка при анализе данных. Сообщение: " + exception.getLocalizedMessage());
        }

    }

}
