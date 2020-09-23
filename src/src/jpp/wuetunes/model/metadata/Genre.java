package jpp.wuetunes.model.metadata;

import jpp.wuetunes.util.Validate;

public class Genre implements Comparable<Genre>{
    private int id;
    private String name;
    public Genre(int id, String name){
        Validate.requireNonNegative(id);
        Validate.requireNonNullNotEmpty(name);
        this.id = id;
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genre genre = (Genre) o;

        return id == genre.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String toString(){
        String result = new String(String.valueOf(id)+". "+name);
        return result;
    }

    @Override
    public int compareTo(Genre o) {
        return Integer.compare(id,o.getId());
    }
}
