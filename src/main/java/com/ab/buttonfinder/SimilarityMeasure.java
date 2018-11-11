package com.ab.buttonfinder;

import java.util.Map;

public interface SimilarityMeasure {

    double computeSimilartiy(Map<String, String> candidateAttributes, Map<String, String> originalAttributes);
}
