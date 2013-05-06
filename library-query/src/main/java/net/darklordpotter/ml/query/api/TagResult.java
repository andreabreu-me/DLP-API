package net.darklordpotter.ml.query.api;

/**
 * 2013-02-11
 *
 * @author Michael Rose <elementation@gmail.com>
 */
public class TagResult {
    private String tag;
    private Long count;

    public TagResult() {}

    public TagResult(String tag, Long count) {
        this.tag = tag;
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TagResult{");
        sb.append("tag='").append(tag).append('\'');
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
