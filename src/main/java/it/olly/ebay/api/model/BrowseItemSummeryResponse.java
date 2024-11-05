package it.olly.ebay.api.model;

import java.util.List;

public record BrowseItemSummeryResponse(String href, Long total, String next, Long limit, Long offset,
        List<ItemSummary> itemSummaries) {
}

record ItemSummary(String itemId, String title, Price price, String itemAffiliateWebUrl, List<Image> thumbnailImages,
        Image image) {
}

record Price(String value, String currency) {
}

record Image(String imageUrl) {
}