package com.itmo.services;

import com.itmo.models.DuplicateAnalyzeResult;
import com.itmo.models.News;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TanimotoHandler {

    private List<News> newsLists;

    private static int MinWordLength = 3;
    private static int SubtokenLength = 2;
    private static double ThresholdWord = 0.25;

    private double minSenseValue;


    public TanimotoHandler(List<News> newsLists, double minSenseValue) {
        this.newsLists = newsLists;
        this.minSenseValue = minSenseValue;
        // edited from vscode
    }

    public HashMap<String, DuplicateAnalyzeResult> getRetryCounts() {
        HashMap<String, DuplicateAnalyzeResult> result = new HashMap<>();

        for (News news1: newsLists) {
            for (News news2: newsLists) {
                double fuzzyValue = calculateFuzzyEqualValue(news1.getTitle(), news2.getTitle());
                if (fuzzyValue > minSenseValue && !news1.getId().equals(news2.getId())) {
                    if (!result.containsKey(news1.getId())) {
                        result.put(news1.getId(), new DuplicateAnalyzeResult(news1, 1));
                    }else{
                        result.get(news1.getId()).setRetryCount(result.get(news1.getId()).getRetryCount() + 1);
                    }
                }
            }
        }

        return result;
    }

    private double calculateFuzzyEqualValue(String first, String second) {
        if ((first == null || first.trim().isEmpty()) && (second == null || second.trim().isEmpty())) {
            return 1.0;
        }

        if ((first == null || first.trim().isEmpty()) || (second == null || second.trim().isEmpty())) {
            return 0.0;
        }

        String normalizedFirst = normalizeSentence(first);
        String normalizedSecond = normalizeSentence(second);

        List<String> tokensFirst = getTokens(normalizedFirst);
        List<String> tokensSecond = getTokens(normalizedSecond);

        List<String> fuzzyEqualsTokens = getFuzzyEqualsTokens(tokensFirst, tokensSecond);

        int equalsCount = fuzzyEqualsTokens.size();
        int firstCount = tokensFirst.size();
        int secondCount = tokensSecond.size();

        double resultValue = (1.0 * equalsCount) / (firstCount + secondCount - equalsCount);

        return resultValue;
    }

    private List<String> getTokens(String sentence) {
        List<String> tokens = new ArrayList<String>();
        String[] words = sentence.split(" ");

        for (String word: words) {
            if (word.length() >= MinWordLength) {
                tokens.add(word);
            }
        }

        return tokens;
    }

    private List<String> getFuzzyEqualsTokens(List<String> tokensFirst, List<String> tokensSecond) {
        List<String> equalsTokens = new ArrayList<String>();
        boolean[] usedToken = new boolean[tokensSecond.size()];

        for (int i = 0; i < tokensFirst.size(); i++) {
            for (int j = 0; j < tokensSecond.size(); j++) {
                if (!usedToken[j]) {
                    if (isTokensFuzzyEqual(tokensFirst.get(i), tokensSecond.get(j))) {
                        equalsTokens.add(tokensFirst.get(i));
                        usedToken[j] = true;
                        break;
                    }
                }
            }
        }

        return equalsTokens;
    }

    private boolean isTokensFuzzyEqual(String firstToken, String secondToken) {
        int equalSubtokensCount = 0;
        boolean[] usedTokens = new boolean[secondToken.length() - SubtokenLength + 1];
        for (int i = 0; i < firstToken.length() - SubtokenLength + 1; ++i) {
            String subtokenFirst = firstToken.substring(i, SubtokenLength + i);
            for (int j = 0; j < secondToken.length() - SubtokenLength + 1; ++j) {
                if (!usedTokens[j]) {
                    String subtokenSecond = secondToken.substring(j, SubtokenLength + j);
                    if (subtokenFirst.equals(subtokenSecond)) {
                        equalSubtokensCount++;
                        usedTokens[j] = true;
                        break;
                    }
                }
            }
        }

        int subtokenFirstCount = firstToken.length() - SubtokenLength + 1;
        int subtokenSecondCount = secondToken.length() - SubtokenLength + 1;

        double tanimoto = (1.0 * equalSubtokensCount) / (subtokenFirstCount + subtokenSecondCount - equalSubtokensCount);

        return ThresholdWord <= tanimoto;
    }

    private String normalizeSentence(String initial) {
        return initial.replaceAll("\"", "").replaceAll(":", "").replaceAll("«", "").replaceAll("»", "").toLowerCase();
    }


}
