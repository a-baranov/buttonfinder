package com.ab.buttonfinder;

import java.util.*;
import java.util.function.ToDoubleBiFunction;

import static java.util.stream.Collectors.toSet;

public class SimilarityMeasureImpl implements SimilarityMeasure {

    @Override
    public double computeSimilartiy(Map<String, String> candidateAttributes, Map<String, String> originalAttributes) {
        double score = 0;
        score += 0.1 * compareAttributes("class", candidateAttributes, originalAttributes, SimilarityMeasureImpl::setOfWordsSimilarity);
        score += 0.1 * compareAttributes("href", candidateAttributes, originalAttributes, SimilarityMeasureImpl::setOfWordsSimilarity);
        score += 0.1 * compareAttributes("title", candidateAttributes, originalAttributes, SimilarityMeasureImpl::setOfWordsSimilarity);
        score += 0.7 * compareAttributes("onclick", candidateAttributes, originalAttributes, (s1, s2) -> s1 != null && s2 != null && s2.toLowerCase().contains("javascript:window.ok") ? 1 : 0);
        return score;
    }

    private static double compareAttributes(String name, Map<String, String> source1, Map<String, String> source2, ToDoubleBiFunction<String, String> similarity) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(source1);
        Objects.requireNonNull(source2);
        Objects.requireNonNull(similarity);
        return similarity.applyAsDouble(source1.get(name), source2.get(name));
    }

    private static double setOfWordsSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0;
        } else {
            Set<String> bow1 = extractSetOfWords(s1);
            Set<String> bow2 = extractSetOfWords(s2);
            double maxSize = Math.max(bow1.size(), bow2.size());
            bow1.retainAll(bow2);
            return bow1.size() / maxSize;
        }
    }

    private static Set<String> extractSetOfWords(String s) {
        return new HashSet<>(Arrays.asList(s.split("\\s|-"))).stream().map(String::toLowerCase).collect(toSet());
    }
}
