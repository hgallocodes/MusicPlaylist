import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MusicDatabase {

    private Hashtable<String, ArrayList<PlayableItem>> data;
    private TreeMap<String, ArrayList<PlayableItem>> artists;
    private Recommendation recommender;
    private int size;

    private File text;
    private Scanner scanner;
    private String line;


    public MusicDatabase() {
        data = new Hashtable<>();
        artists = new TreeMap<>();
        recommender = new Recommendation("UserData.csv");
        this.size = 0;


    }

    public boolean addSongs(File inputFile) {
        int counter = 0;

        try {
            scanner = new Scanner(inputFile);
            scanner.useDelimiter(",");

            while (scanner.hasNextLine()) {
                if (counter == 0) {
                    scanner.nextLine().split(",");
                    counter++;
                } else {
                    String[] arrayRep = scanner.nextLine().split(",");
                    addSongs(arrayRep[2], arrayRep[3], Integer.parseInt(arrayRep[4]),
                            Integer.parseInt(arrayRep[5]), arrayRep[7]);
                    counter++;
                }
            }
            scanner.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public void addSongs(String name, String artist, int duration, int popularity,
                         String endpoint) {
        PlayableItem songToAdd = new PlayableItem(0, duration, endpoint, name,
                artist, popularity);

        boolean bool = false;

        if (data.containsKey(name)) {
            for (PlayableItem p : data.get(name)) {
                if (p.equals(songToAdd)) {
                    p.setPopularity(popularity);
                    bool = true;
                }
            }
            if (!bool) {
                data.get(name).add(songToAdd);
                size++;
            }
        } else {
            ArrayList<PlayableItem> newArrayList = new ArrayList<>();
            PlayableItem valueData = songToAdd;
            newArrayList.add(valueData);
            data.put(name, newArrayList);
            size++;
        }

        if (artists.containsKey(artist)) {
            for (PlayableItem p : artists.get(artist)) {
                if (p.equals(songToAdd)) {
                    p.setPopularity(popularity);
                    bool = true;
                }
            }
            if (!bool) {
                artists.get(artist).add(songToAdd);
//                size++;
            }
        } else {
            ArrayList<PlayableItem> newArrayList = new ArrayList<>();
            PlayableItem valueData = songToAdd;
            newArrayList.add(valueData);
            artists.put(artist, newArrayList);
//            size++;
        }
    }

    public ArrayList<PlayableItem> partialSearchBySongName(String name) {
        ArrayList<ArrayList<PlayableItem>> keysArray = new ArrayList<>();
        ArrayList<PlayableItem> arr = new ArrayList<>();

        for (String n : data.keySet()) {
            if (n.toLowerCase().contains(name.toLowerCase())) {
                keysArray.add(data.get(n));
            }
        }

        for (ArrayList<PlayableItem> a: keysArray) {
            arr.addAll(a);
        }
        return arr;
    }

    public ArrayList<PlayableItem> partialSearchByArtistName(String name) {
        ArrayList<ArrayList<PlayableItem>> keysArray = new ArrayList<>();
        ArrayList<PlayableItem> arr = new ArrayList<>();

        for (String n : artists.keySet()) {
            if (n.toLowerCase().contains(name.toLowerCase())) {
                keysArray.add(artists.get(n));
            }
        }

        for (ArrayList<PlayableItem> a: keysArray) {
            arr.addAll(a);
        }

        Collections.sort(arr, Comparator.comparing(PlayableItem::getPopularity));
        Collections.reverse(arr);

        return arr;
    }

    public ArrayList<PlayableItem> searchHighestPopularity(int threshold) {
        ArrayList<ArrayList<PlayableItem>> keysArray = new ArrayList<>();
        ArrayList<PlayableItem> arr = new ArrayList<>();
        ArrayList<PlayableItem> aboveThreshold = new ArrayList<>();

        for (String n : data.keySet()) {
            keysArray.add(data.get(n));
        }

        for (ArrayList<PlayableItem> a: keysArray) {
            arr.addAll(a);
        }

        for (PlayableItem p : arr) {
            if (p.getPopularity() >= threshold) {
                aboveThreshold.add(p);
            }
        }
        Collections.sort(aboveThreshold, Comparator.comparing(PlayableItem::getPopularity));
        Collections.reverse(aboveThreshold);

        return aboveThreshold;
    }

    public ArrayList<PlayableItem> getRecommendedSongs(List<String> fiveArtists) {
        ArrayList<PlayableItem> songs = new ArrayList<>();

        for (String s : recommender.recommendNewArtists(fiveArtists)) {
            songs.addAll(artists.get(s));
        }
        Collections.sort(songs, Comparator.comparing(PlayableItem::getPopularity));
        Collections.reverse(songs);

        ArrayList<PlayableItem> newSongs = new ArrayList<>();
        int i = 0;
        for (PlayableItem p : songs) {
            if (i == 10) {
                break;
            } else {
                newSongs.add(p);
                i++;
            }
        }
        return newSongs;
    }

    public int size() {
        return size;
    }
}
