package jpp.wuetunes.model.metadata;

import jpp.wuetunes.util.Validate;

import java.util.*;

public class GenreManager {
    ArrayList<Genre> genres;
    public GenreManager(){
        this.genres = new ArrayList<Genre>();
    }
    public GenreManager(Collection<Genre> genres){


        boolean containsDuplicates = false;
        int posg = 0;
        if(genres == null){
            throw new IllegalArgumentException("genremanager");
        }
        for(Genre g: genres){
            if(g == null){
                throw new IllegalArgumentException();
            }
            posg++;
            int posgen= 0;
            for(Genre gen : genres){
                posgen++;
                if(posgen!= posg&&(gen.getName().equals(g.getName())|| gen.getId()== g.getId())){
                    containsDuplicates = true;
                }
            }
        }

        if(containsDuplicates){
            throw new IllegalArgumentException();
        }else {
            this.genres = new ArrayList<Genre>();
            this.genres.addAll(genres);
        }

    }
    public Genre add(Genre genre){
        if(genre == null){
            throw new IllegalArgumentException();
        }
        boolean error = false;
        for(Genre g: genres){
            if (g.getId() == genre.getId() || g.getName().equals(genre.getName())) {
                throw new IllegalArgumentException();

            }
        }
        genres.add(genre);
        return genre;
    }
    public Genre add(String name){
        if(name == null){
            throw new IllegalArgumentException();
        }
        for(Genre g: genres){
            if(g.getName().equals(name)){
                return g;
            }
        }
        //neure ID
        ArrayList<Genre> list = new ArrayList<Genre>();
        list.addAll(genres);
        Collections.sort(list);

        int id = 0;
        int max = list.size();
        int i =0;
        boolean b = false;
        for( ; i< max+1;i++){
            b = false;
            for(Genre g : list){
                if(g.getId()==i){
                    b = true;
                    break; //contains
                }
            }
            if(!b){
                break;
            }
        }
        id = i; // doesnt contain

        Genre genre = new Genre(id,name);
        genres.add(genre);
        return genre;
    }
    public Optional<Genre> getGenreById(int id){
        Validate.requireNonNegative(id);
        Genre genre;
        for(Genre g: genres){
            if(g.getId() == id){
                genre = new Genre(g.getId(),g.getName());
                Optional<Genre> opt = Optional.of(genre);
                return opt;
            }
        }
        Optional<Genre> opt = Optional.empty();
        return opt;
    }
    public Optional<Genre> getGenreByName(String name){
        if(name == null){
            throw new IllegalArgumentException();
        }
        if(name.length()==0){
            throw new IllegalArgumentException();
        }
        Genre genre;
        for(Genre g: genres){
            if(g.getName().equals(name)){
                genre = new Genre(g.getId(),g.getName());
                Optional<Genre> opt = Optional.of(genre);
                return opt;
            }
        }
        Optional<Genre> opt = Optional.empty();
        return opt;
    }

    public List<Genre> getGenres(){

        ArrayList<Genre> list = new ArrayList<Genre>();
        list.addAll(genres);
        Collections.sort(list);
        return list;
    }
}
