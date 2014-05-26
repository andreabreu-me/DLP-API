package net.darklordpotter.api.fiction.core;

/**
 * 2013-12-23
 *
 * @author Michael Rose
 */
public class Category {
    long categoryId;
    String name;

    public Category() {
    }

    public long getCategoryId() {
        return this.categoryId;
    }

    public String getName() {
        return this.name;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Category)) return false;
        final Category other = (Category) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.categoryId != other.categoryId) return false;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $categoryId = this.categoryId;
        result = result * PRIME + (int) ($categoryId >>> 32 ^ $categoryId);
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof Category;
    }

    public String toString() {
        return "net.darklordpotter.api.fiction.core.Category(categoryId=" + this.categoryId + ", name=" + this.name + ")";
    }
}
