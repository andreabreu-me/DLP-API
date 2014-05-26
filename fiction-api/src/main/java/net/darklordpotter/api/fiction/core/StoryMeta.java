//package net.darklordpotter.api.fiction.core;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * 2013-12-23
// *
// * @author Michael Rose
// */
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class StoryMeta {
//    String status;
//    String language;
//    String rated;
//    long ratings;
//    long chapters;
//    long favs;
//    long follows;
//    long reviews;
//    long words;
//    List<Category> categories = Collections.emptyList();
//    List<List<Character>> relationships = Collections.emptyList();
//    List<Character> characters;
//
//    ThreadData threadData;
//
//    public StoryMeta() {
//    }
//
//    public String getStatus() {
//        return this.status;
//    }
//
//    public String getLanguage() {
//        return this.language;
//    }
//
//    public String getRated() {
//        return this.rated;
//    }
//
//    public long getRatings() {
//        return this.ratings;
//    }
//
//    public long getChapters() {
//        return this.chapters;
//    }
//
//    public long getFavs() {
//        return this.favs;
//    }
//
//    public long getFollows() {
//        return this.follows;
//    }
//
//    public long getReviews() {
//        return this.reviews;
//    }
//
//    public long getWords() {
//        return this.words;
//    }
//
//    public List<Category> getCategories() {
//        return this.categories;
//    }
//
//    public List<List<Character>> getRelationships() {
//        return this.relationships;
//    }
//
//    public List<Character> getCharacters() {
//        return this.characters;
//    }
//
//    public ThreadData getThreadData() {
//        return this.threadData;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public void setLanguage(String language) {
//        this.language = language;
//    }
//
//    public void setRated(String rated) {
//        this.rated = rated;
//    }
//
//    public void setRatings(long ratings) {
//        this.ratings = ratings;
//    }
//
//    public void setChapters(long chapters) {
//        this.chapters = chapters;
//    }
//
//    public void setFavs(long favs) {
//        this.favs = favs;
//    }
//
//    public void setFollows(long follows) {
//        this.follows = follows;
//    }
//
//    public void setReviews(long reviews) {
//        this.reviews = reviews;
//    }
//
//    public void setWords(long words) {
//        this.words = words;
//    }
//
//    public void setCategories(List<Category> categories) {
//        this.categories = categories;
//    }
//
//    public void setRelationships(List<List<Character>> relationships) {
//        this.relationships = relationships;
//    }
//
//    public void setCharacters(List<Character> characters) {
//        this.characters = characters;
//    }
//
//    public void setThreadData(ThreadData threadData) {
//        this.threadData = threadData;
//    }
//
//    public boolean equals(Object o) {
//        if (o == this) return true;
//        if (!(o instanceof StoryMeta)) return false;
//        final StoryMeta other = (StoryMeta) o;
//        if (!other.canEqual((Object) this)) return false;
//        final Object this$status = this.status;
//        final Object other$status = other.status;
//        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
//        final Object this$language = this.language;
//        final Object other$language = other.language;
//        if (this$language == null ? other$language != null : !this$language.equals(other$language)) return false;
//        final Object this$rated = this.rated;
//        final Object other$rated = other.rated;
//        if (this$rated == null ? other$rated != null : !this$rated.equals(other$rated)) return false;
//        if (this.ratings != other.ratings) return false;
//        if (this.chapters != other.chapters) return false;
//        if (this.favs != other.favs) return false;
//        if (this.follows != other.follows) return false;
//        if (this.reviews != other.reviews) return false;
//        if (this.words != other.words) return false;
//        final Object this$categories = this.categories;
//        final Object other$categories = other.categories;
//        if (this$categories == null ? other$categories != null : !this$categories.equals(other$categories))
//            return false;
//        final Object this$relationships = this.relationships;
//        final Object other$relationships = other.relationships;
//        if (this$relationships == null ? other$relationships != null : !this$relationships.equals(other$relationships))
//            return false;
//        final Object this$characters = this.characters;
//        final Object other$characters = other.characters;
//        if (this$characters == null ? other$characters != null : !this$characters.equals(other$characters))
//            return false;
//        final Object this$threadData = this.threadData;
//        final Object other$threadData = other.threadData;
//        if (this$threadData == null ? other$threadData != null : !this$threadData.equals(other$threadData))
//            return false;
//        return true;
//    }
//
//    public int hashCode() {
//        final int PRIME = 59;
//        int result = 1;
//        final Object $status = this.status;
//        result = result * PRIME + ($status == null ? 0 : $status.hashCode());
//        final Object $language = this.language;
//        result = result * PRIME + ($language == null ? 0 : $language.hashCode());
//        final Object $rated = this.rated;
//        result = result * PRIME + ($rated == null ? 0 : $rated.hashCode());
//        final long $ratings = this.ratings;
//        result = result * PRIME + (int) ($ratings >>> 32 ^ $ratings);
//        final long $chapters = this.chapters;
//        result = result * PRIME + (int) ($chapters >>> 32 ^ $chapters);
//        final long $favs = this.favs;
//        result = result * PRIME + (int) ($favs >>> 32 ^ $favs);
//        final long $follows = this.follows;
//        result = result * PRIME + (int) ($follows >>> 32 ^ $follows);
//        final long $reviews = this.reviews;
//        result = result * PRIME + (int) ($reviews >>> 32 ^ $reviews);
//        final long $words = this.words;
//        result = result * PRIME + (int) ($words >>> 32 ^ $words);
//        final Object $categories = this.categories;
//        result = result * PRIME + ($categories == null ? 0 : $categories.hashCode());
//        final Object $relationships = this.relationships;
//        result = result * PRIME + ($relationships == null ? 0 : $relationships.hashCode());
//        final Object $characters = this.characters;
//        result = result * PRIME + ($characters == null ? 0 : $characters.hashCode());
//        final Object $threadData = this.threadData;
//        result = result * PRIME + ($threadData == null ? 0 : $threadData.hashCode());
//        return result;
//    }
//
//    public boolean canEqual(Object other) {
//        return other instanceof StoryMeta;
//    }
//
//    public String toString() {
//        return "net.darklordpotter.api.fiction.core.StoryMeta(status=" + this.status + ", language=" + this.language + ", rated=" + this.rated + ", ratings=" + this.ratings + ", chapters=" + this.chapters + ", favs=" + this.favs + ", follows=" + this.follows + ", reviews=" + this.reviews + ", words=" + this.words + ", categories=" + this.categories + ", relationships=" + this.relationships + ", characters=" + this.characters + ", threadData=" + this.threadData + ")";
//    }
//}
