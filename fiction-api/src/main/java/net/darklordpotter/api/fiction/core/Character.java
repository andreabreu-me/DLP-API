package net.darklordpotter.api.fiction.core;

/**
 * 2013-12-23
 *
 * @author Michael Rose
 */
public class Character {
    long characterId;
    String name;

    public Character() {
    }

    public long getCharacterId() {
        return this.characterId;
    }

    public String getName() {
        return this.name;
    }

    public void setCharacterId(long characterId) {
        this.characterId = characterId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Character)) return false;
        final Character other = (Character) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.characterId != other.characterId) return false;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $characterId = this.characterId;
        result = result * PRIME + (int) ($characterId >>> 32 ^ $characterId);
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof Character;
    }

    public String toString() {
        return "net.darklordpotter.api.fiction.core.Character(characterId=" + this.characterId + ", name=" + this.name + ")";
    }
}
