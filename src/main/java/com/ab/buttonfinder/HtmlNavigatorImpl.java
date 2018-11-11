package com.ab.buttonfinder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class HtmlNavigatorImpl implements HtmlNavigator {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlNavigatorImpl.class);

    private String charsetName;

    public HtmlNavigatorImpl(String charsetName) {
        Objects.requireNonNull(charsetName);
        this.charsetName = charsetName;
    }

    @Override
    public Optional<Elements> findElementsByQuery(File htmlFile, String cssQuery) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    charsetName,
                    htmlFile.getAbsolutePath());

            return Optional.ofNullable(doc.select(cssQuery));

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Element> findElementById(File htmlFile, String targetElementId) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    charsetName,
                    htmlFile.getAbsolutePath());

            return Optional.ofNullable(doc.getElementById(targetElementId));

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }
}
