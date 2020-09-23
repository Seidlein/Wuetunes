package jpp.wuetunes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import jpp.wuetunes.util.Validate;

public class Playlist {

    private ArrayList<Song> playList;
    private int current;
    private boolean shuffle;
    private Random random;

    public Playlist() {
        playList = new ArrayList<Song>();
        shuffle = false;
        random = new Random();
        current = -1;
    }

    public void add(Song song) {
        if (song == null) {
            throw new IllegalArgumentException();
        }
        playList.add(song);
    }

    public List<Song> getSongs() {
        return playList;
    }

    public void addAt(int index, Song song) {
        if (song == null || index < 0 || index > playList.size()) {
            throw new IllegalArgumentException();
        }
        if(index<=current) current ++;
        playList.add(index, song);
    }

    public void removeAt(int index) {
        if (index < 0 || index > playList.size() - 1) {
            throw new IllegalArgumentException();
        }
        if (index == current) {
            current = -1;
        }
        if(index <current) current --;
        playList.remove(index);
    }

    public void setCurrent(int index) {
        Validate.requireBetween(index, -1, playList.size() - 1);
        current = index;
    }

    public Optional<Integer> getCurrent() {

        if (playList.size() == 0 || current == -1) {
            return Optional.empty();
        } else {
            return Optional.of(current);
        }

    }

    public Optional<Song> getCurrentSong() {

        if (current == -1 || playList.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(playList.get(current));
    }

    public void setRandom(Random random) {
        Validate.requireNonNull(random);
        //shuffle = true;
        this.random = random;
    }

    public Optional<Integer> getRandom() {
        if (playList.size() == 0) {
            return Optional.empty();
        }
        int i = random.nextInt(playList.size());
        return Optional.of(i);
    }

    public void setShuffle(boolean value) {
        shuffle = value;
    }

    public Optional<Integer> next() {
        if (playList.size() == 0) return Optional.empty();
        if (!shuffle) {         //Kein Zufallsmodus
            if (current == -1 || current == playList.size()-1) {
                current = 0;
                return Optional.of(0);
            } else {
                int pos = current;
                current++;
                return Optional.of(current);
            }
        } else {           //Zufallsmodus
            Optional<Integer> i = getRandom();
            current = i.get();
            return i;
        }
    }
    public Optional<Integer> previous() {
        Optional<Integer> opt;
        if (playList.size() == 0) return Optional.empty();
        if (!shuffle) {         //Kein Zufallsmodus
            if (current == -1 || current ==0) {
                current = (playList.size() - 1);
                return Optional.of(playList.size()-1);
            } else {
                int pos = current;
                current --;
                return Optional.of(current);
            }
        } else {           //Zufallsmodus
            Optional<Integer> i = getRandom();
            current = i.get();
            return i;
        }
    }
}
