package ch.meineinitiative.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsDTO {
    private List<Result> searchResultListMedia;

    public List<Result> getSearchResultListMedia() {
        return searchResultListMedia;
    }

    public void setSearchResultListMedia(List<Result> searchResultListMedia) {
        this.searchResultListMedia = searchResultListMedia;
    }

    public static class Result {
        private String title;
        private String urn;
        private String imageUrl;
        private String description;
        private String date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrn() {
            return urn;
        }

        public void setUrn(String urn) {
            this.urn = urn;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
