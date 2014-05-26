package net.darklordpotter.ml.query.api;

/**
 * 2014-02-07
 *
 * @author Michael Rose
 */
public class FieldPair {
    private final String field;
    private final String value;

    public FieldPair(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return this.field;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof FieldPair)) return false;
        final FieldPair other = (FieldPair) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$field = this.field;
        final Object other$field = other.field;
        if (this$field == null ? other$field != null : !this$field.equals(other$field)) return false;
        final Object this$value = this.value;
        final Object other$value = other.value;
        if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $field = this.field;
        result = result * PRIME + ($field == null ? 0 : $field.hashCode());
        final Object $value = this.value;
        result = result * PRIME + ($value == null ? 0 : $value.hashCode());
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof FieldPair;
    }

    public String toString() {
        return "net.darklordpotter.ml.query.api.FieldPair(field=" + this.field + ", value=" + this.value + ")";
    }
}
