package ari;

public interface Searchable {
    boolean matchesKeyword(String keyword);
    String getSearchableInfo();
}
