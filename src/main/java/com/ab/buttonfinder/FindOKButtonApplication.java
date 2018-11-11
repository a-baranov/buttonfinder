package com.ab.buttonfinder;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class FindOKButtonApplication {

    private static Logger LOGGER = LoggerFactory.getLogger(FindOKButtonApplication.class);

    private static final String CHARSET_NAME = "utf8";
    private static final String CANDIDATE_BUTTON_QUERY = "div[id=\"wrapper\"] div[id=\"page-wrapper\"] div[class*=\"panel-default\"] a[class*=\"btn\"]";
    private static final double SIMILARITY_THRESHOLD = 0.85;

    public static void main(String[] args) {

        try {
            if (args.length < 2) {
                throw new IllegalArgumentException("2 command line arguments are required");
            }

            String input_origin_file_path = args[0];
            String input_other_file_path = args[1];
            String originalElementId = args.length == 3 ? args[2] : "make-everything-ok-button";

            HtmlNavigator htmlNavigator = new HtmlNavigatorImpl(CHARSET_NAME);

            Optional<Element> originalButton = htmlNavigator.findElementById(new File(input_origin_file_path), originalElementId);
            Optional<Map<String, String>> originalAttributes = extractAttributes(originalButton.orElseThrow(() -> new IllegalStateException("Original element not found")));

            SimilarityMeasure similarityMeasure = new SimilarityMeasureImpl();

            Optional<Elements> candidateElements = htmlNavigator.findElementsByQuery(new File(input_other_file_path), CANDIDATE_BUTTON_QUERY);
            candidateElements.ifPresent(elements -> elements.forEach(element ->
                    LOGGER.info("Similarity = {}, element: {}", similarityMeasure.computeSimilartiy(originalAttributes.orElse(Collections.emptyMap()),
                            extractAttributes(element).orElse(Collections.emptyMap())), element)));

            Optional<Map<Double, Element>> scoredElements = candidateElements.map(elements -> elements.stream().collect(
                    toMap(element -> similarityMeasure.computeSimilartiy(originalAttributes.orElse(Collections.emptyMap()),
                            extractAttributes(element).orElse(Collections.emptyMap())), element -> element)));
            if (scoredElements.isPresent()) {
                Optional<Map.Entry<Double, Element>> bestMatch = scoredElements.get().entrySet().stream()
                        .filter(entry -> entry.getKey() > SIMILARITY_THRESHOLD)
                        .max(Comparator.comparing(Map.Entry::getKey));
                bestMatch.map(Map.Entry::getValue).ifPresent(button -> LOGGER.info("Result: {}", button.cssSelector()));
            } else {
                LOGGER.error("No matches found");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to complete task", e);
        }
    }

    private static Optional<Map<String, String>> extractAttributes(Element button) {
        return button != null ? Optional.of(button.attributes().asList().stream().collect(Collectors.toMap(Attribute::getKey, Attribute::getValue))) : Optional.empty();
    }
}
