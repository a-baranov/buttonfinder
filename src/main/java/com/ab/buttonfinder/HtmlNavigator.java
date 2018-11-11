package com.ab.buttonfinder;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Optional;

public interface HtmlNavigator {

    Optional<Elements> findElementsByQuery(File htmlFile, String cssQuery);

    Optional<Element> findElementById(File htmlFile, String targetElementId);
}
