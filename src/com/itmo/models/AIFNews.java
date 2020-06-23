package com.itmo.models;

import org.w3c.dom.Element;

public class AIFNews extends News {

    public AIFNews(Element xml) {
        super();
        title = xml.getElementsByTagName("title").item(0).getTextContent();
        id = xml.getElementsByTagName("link").item(0).getTextContent();
        resource = "AIF";
    }

}
