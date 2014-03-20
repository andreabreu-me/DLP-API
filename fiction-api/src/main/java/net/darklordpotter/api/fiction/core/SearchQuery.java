package net.darklordpotter.api.fiction.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.List;

/**
 * 2014-01-30
 *
 *       params =
 title: $("#title").val()
 author: $("#author").val()
 summary: $("#summary").val()
 wordcount_lower: $("#wordcount_lower").val()
 wordcount_upper: $("#wordcount_upper").val()
 rating: $scope.rating
 character_required: $scope.character_required
 character_optional_exclude: $scope.character_optional_exclude
 character_optional: $scope.character_optional
 genre_required: $scope.genre_required
 genre_optional_exclude: $scope.genre_optional_exclude
 genre_optional: $scope.genre_optional
 language: $scope.language
 sort_by: $scope.sort_by
 order_by: $scope.order_by

 *
 * @author Michael Rose <michael@fullcontact.com>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchQuery {
    String title, author, summary;
    Integer wordcountLower;
    Integer wordcountUpper;
    Integer chaptersLower;
    Integer chaptersUpper;

    List<String> rating = Collections.emptyList();
    List<Integer> categoryRequired = Collections.emptyList();
    List<Integer> categoryOptional = Collections.emptyList();

    boolean categoryOptionalExclude;
    List<Integer> characterRequired = Collections.emptyList();
    List<Integer> characterOptional = Collections.emptyList();

//    List<List<Integer>> pairings = Collections.emptyList();

    boolean characterOptionalExclude;
    String language;
    String sortBy, orderBy;

    public SearchQuery() {
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getSummary() {
        return this.summary;
    }

    public Integer getWordcountLower() {
        return this.wordcountLower;
    }

    public Integer getWordcountUpper() {
        return this.wordcountUpper;
    }

    public Integer getChaptersLower() {
        return this.chaptersLower;
    }

    public Integer getChaptersUpper() {
        return this.chaptersUpper;
    }

    public List<String> getRating() {
        return this.rating;
    }

    public List<Integer> getCategoryRequired() {
        return this.categoryRequired;
    }

    public List<Integer> getCategoryOptional() {
        return this.categoryOptional;
    }

    public boolean isCategoryOptionalExclude() {
        return this.categoryOptionalExclude;
    }

    public List<Integer> getCharacterRequired() {
        return this.characterRequired;
    }

    public List<Integer> getCharacterOptional() {
        return this.characterOptional;
    }

    public boolean isCharacterOptionalExclude() {
        return this.characterOptionalExclude;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getSortBy() {
        return this.sortBy;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setWordcountLower(Integer wordcountLower) {
        this.wordcountLower = wordcountLower;
    }

    public void setWordcountUpper(Integer wordcountUpper) {
        this.wordcountUpper = wordcountUpper;
    }

    public void setChaptersLower(Integer chaptersLower) {
        this.chaptersLower = chaptersLower;
    }

    public void setChaptersUpper(Integer chaptersUpper) {
        this.chaptersUpper = chaptersUpper;
    }

    public void setRating(List<String> rating) {
        this.rating = rating;
    }

    public void setCategoryRequired(List<Integer> categoryRequired) {
        this.categoryRequired = categoryRequired;
    }

    public void setCategoryOptional(List<Integer> categoryOptional) {
        this.categoryOptional = categoryOptional;
    }

    public void setCategoryOptionalExclude(boolean categoryOptionalExclude) {
        this.categoryOptionalExclude = categoryOptionalExclude;
    }

    public void setCharacterRequired(List<Integer> characterRequired) {
        this.characterRequired = characterRequired;
    }

    public void setCharacterOptional(List<Integer> characterOptional) {
        this.characterOptional = characterOptional;
    }

    public void setCharacterOptionalExclude(boolean characterOptionalExclude) {
        this.characterOptionalExclude = characterOptionalExclude;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchQuery that = (SearchQuery) o;

        if (categoryOptionalExclude != that.categoryOptionalExclude) return false;
        if (characterOptionalExclude != that.characterOptionalExclude) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (categoryOptional != null ? !categoryOptional.equals(that.categoryOptional) : that.categoryOptional != null)
            return false;
        if (categoryRequired != null ? !categoryRequired.equals(that.categoryRequired) : that.categoryRequired != null)
            return false;
        if (chaptersLower != null ? !chaptersLower.equals(that.chaptersLower) : that.chaptersLower != null)
            return false;
        if (chaptersUpper != null ? !chaptersUpper.equals(that.chaptersUpper) : that.chaptersUpper != null)
            return false;
        if (characterOptional != null ? !characterOptional.equals(that.characterOptional) : that.characterOptional != null)
            return false;
        if (characterRequired != null ? !characterRequired.equals(that.characterRequired) : that.characterRequired != null)
            return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (orderBy != null ? !orderBy.equals(that.orderBy) : that.orderBy != null) return false;
        if (rating != null ? !rating.equals(that.rating) : that.rating != null) return false;
        if (sortBy != null ? !sortBy.equals(that.sortBy) : that.sortBy != null) return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (wordcountLower != null ? !wordcountLower.equals(that.wordcountLower) : that.wordcountLower != null)
            return false;
        if (wordcountUpper != null ? !wordcountUpper.equals(that.wordcountUpper) : that.wordcountUpper != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (wordcountLower != null ? wordcountLower.hashCode() : 0);
        result = 31 * result + (wordcountUpper != null ? wordcountUpper.hashCode() : 0);
        result = 31 * result + (chaptersLower != null ? chaptersLower.hashCode() : 0);
        result = 31 * result + (chaptersUpper != null ? chaptersUpper.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (categoryRequired != null ? categoryRequired.hashCode() : 0);
        result = 31 * result + (categoryOptional != null ? categoryOptional.hashCode() : 0);
        result = 31 * result + (categoryOptionalExclude ? 1 : 0);
        result = 31 * result + (characterRequired != null ? characterRequired.hashCode() : 0);
        result = 31 * result + (characterOptional != null ? characterOptional.hashCode() : 0);
        result = 31 * result + (characterOptionalExclude ? 1 : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (sortBy != null ? sortBy.hashCode() : 0);
        result = 31 * result + (orderBy != null ? orderBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchQuery{");
        sb.append("title='").append(title).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", summary='").append(summary).append('\'');
        sb.append(", wordcountLower=").append(wordcountLower);
        sb.append(", wordcountUpper=").append(wordcountUpper);
        sb.append(", chaptersLower=").append(chaptersLower);
        sb.append(", chaptersUpper=").append(chaptersUpper);
        sb.append(", rating=").append(rating);
        sb.append(", categoryRequired=").append(categoryRequired);
        sb.append(", categoryOptional=").append(categoryOptional);
        sb.append(", categoryOptionalExclude=").append(categoryOptionalExclude);
        sb.append(", characterRequired=").append(characterRequired);
        sb.append(", characterOptional=").append(characterOptional);
        sb.append(", characterOptionalExclude=").append(characterOptionalExclude);
        sb.append(", language='").append(language).append('\'');
        sb.append(", sortBy='").append(sortBy).append('\'');
        sb.append(", orderBy='").append(orderBy).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
