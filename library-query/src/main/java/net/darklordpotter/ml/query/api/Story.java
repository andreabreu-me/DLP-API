package net.darklordpotter.ml.query.api;

import java.util.List;

public class Story {
    private String title;
    private String author;
    private Integer chapters;
    private List<String> tags;
    private String summary;
    private String storyText;

    public String title() { return this.title; }

    public String author() { return this.author; }

    public Integer chapters() { return this.chapters; }

    public List<String> tags() { return this.tags; }

    public String summary() { return this.summary; }

    public String storyText() { return this.storyText; }

    public Story title(final String title) {
        this.title = title;
        return this;
    }

    public Story author(final String author) {
        this.author = author;
        return this;
    }

    public Story chapters(final Integer chapters) {
        this.chapters = chapters;
        return this;
    }

    public Story tags(final List<String> tags) {
        this.tags = tags;
        return this;
    }

    public Story summary(final String summary) {
        this.summary = summary;
        return this;
    }

    public Story storyText(final String storyText) {
        this.storyText = storyText;
        return this;
    }


}