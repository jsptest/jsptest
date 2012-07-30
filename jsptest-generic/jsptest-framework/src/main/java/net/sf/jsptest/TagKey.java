package net.sf.jsptest;

/**
 * @author Meinert Schwartau (scwar32)
 * @author Lasse Koskela
 */
public class TagKey {

    private final String prefix;
    private final String name;

    public TagKey(String prefix) {
        this(prefix, "*");
    }

    public TagKey(String prefix, String name) {
        this.prefix = prefix;
        this.name = name;
    }

    public String toString() {
        return prefix + ":" + name;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TagKey other = (TagKey) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (prefix == null) {
            if (other.prefix != null) {
                return false;
            }
        } else if (!prefix.equals(other.prefix)) {
            return false;
        }
        return true;
    }
}
