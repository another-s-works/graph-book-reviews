package com.albertoventurini.graphs.bookreviews.graph;

import com.albertoventurini.graphs.bookreviews.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Queries {

    public static List<Pair<String, Integer>> getAuthorsByNumberOfReviews(final BookReviewsGraph graph) {
        return graph.getNodesByLabel(BookReviewsGraph.NODE_AUTHOR)
                .stream()
                .map(a -> Pair.of((String) a.properties.get("name"), getRatingsByAuthor(a).size()))
                .sorted(Comparator.comparingInt(p -> -p.second))
                .collect(Collectors.toList());
    }

    private static List<Integer> getRatingsByAuthor(final Node authorNode) {
        return authorNode
                .incomingEdges(BookReviewsGraph.EDGE_WRITTEN_BY)
                .map(e -> e.source)
                .flatMap(n -> n.incomingEdges(BookReviewsGraph.EDGE_REVIEWED))
                .map(e -> (int) e.properties.get("rating"))
                .collect(Collectors.toList());
    }

    public static double getAverageRatingsByAuthor(final BookReviewsGraph graph, final String author) {
        return graph.getNode(author).incomingEdges(BookReviewsGraph.EDGE_WRITTEN_BY)
                .map(e -> e.source)
                .flatMap(n -> n.incomingEdges(BookReviewsGraph.EDGE_REVIEWED))
                .mapToInt(e -> (int) e.properties.get("rating"))
                .average()
                .getAsDouble();

    }

}