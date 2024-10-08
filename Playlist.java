import java.util.Collections;
import java.util.ArrayList;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.HashSet;

public class Playlist {

    private String name;
    private int playingMode = 0;
    private int playingIndex = 0;
    private ArrayList<PlayableItem> curList;
    private PlayableItem curCopy;
    private PlayableItem cur;
    private Stack<PlayableItem> history;
    private PriorityQueue<PlayableItem> freqListened;
    private ArrayList<PlayableItem> playlist;
    private boolean complete = false;

    public Playlist() {
        this.name = "Default";
        this.curList = new ArrayList<>();
        this.history = new Stack<>();
        this.freqListened = new PriorityQueue<>(Collections.reverseOrder());
        this.playlist = new ArrayList<>();
    }

    public Playlist(String name) {
        this();
        this.setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int size() {
        return playlist.size();
    }

    public String toString() {
        return name + "," + size() + " songs";
    }

    public void addPlayableItem(PlayableItem newItem) {
        if (playingMode == 0) {
            playlist.add(newItem);
            curList = new ArrayList<>(playlist);
        } else if (playingMode == 1) {
            playlist.add(newItem);
            Collections.shuffle(playlist);
            curList = new ArrayList<>(playlist);
        } else if (playingMode == 2) {
            playlist.add(newItem);
            Collections.sort(playlist, PlayableItem::compareTo);
            curList = new ArrayList<>(playlist);
        } else if (playingMode == 3) {
            playlist.add(newItem);
            curList = new ArrayList<>(playlist);
        }
    }

    public void addPlayableItem(ArrayList<PlayableItem> newItem) {
        for (PlayableItem p : newItem) {
            addPlayableItem(p);
        }
    }

    public boolean removePlayableItem(int number) {
        if (number - 1 > curList.size()) {
            return false;
        }
        if (playingIndex == (number - 1)) {
            //if the current song's index that we want to remove is the input one
            playlist.remove(number - 1);
        } else {
            curList.remove(number - 1);
            playlist.remove(number - 1);
            cur = getNextPlayable();
            playingIndex = playlist.indexOf(cur);
        }
        return true;
    }

    public void switchPlayingMode(int newMode) {


    }

    public ArrayList<String> getFiveMostPopular() {
        ArrayList<String> listNames = new ArrayList<>();
        for (int i = 0; i < freqListened.size(); i++) {
            listNames.add(String.valueOf(freqListened.poll()));
        }
        HashSet<String> a = new HashSet<>(listNames);

        ArrayList<String> top5 = new ArrayList<>();

        int counter = 0;
        for (String s : a) {
            if (counter == 5) {
                break;
            } else {
                top5.add(s);
            }
            counter++;
        }
        return top5;
    }

    /**
     * Go to the last playing item
     */
    public void goBack() {
        playingIndex--;
        if (playingIndex < 0) {
            playingIndex++;
            System.out.println("No more step to go back");
        }
    }

    public void play(int seconds) {
        int playedTime = 0;
        boolean start = true;
        cur = getNextPlayable();
        while (seconds > 0) {
            while (seconds > 0) {
                if (cur.play()) {
                    if (start) {
                        System.out.println("Seconds " + playedTime + " : "
                                + cur.getTitle() + " start.");
                    }
                    start = false;
                    seconds--;
                    playedTime++;
                } else {
                    cur.play();
                    seconds--;
                    playedTime++;
                    if (!start) {
                        System.out.println("Seconds " + playedTime + " : "
                                + cur.getTitle() + " complete.");
                    }
                    start = true;
                    break;
                }
            }
            if (curList.size() == playlist.size()) {
                cur = getNextPlayable();
                curList = playlist;
            } else {
                cur = getNextPlayable();
                playingIndex = playlist.indexOf(cur);
            }
        }
    }

    public String showPlaylistStatus() {
        int counter = 0;
        String stringMessage = "";
        for (PlayableItem p : playlist) {
            counter++;
            if (p == cur) {
                stringMessage += counter + ". " + p + " - Currently play" + "\n";
            } else {
                stringMessage += counter + ". " + p + "\n";
            }
        }
        return stringMessage;
    }

    public PlayableItem getNextPlayable() {
        if (playingMode == 0) {
            if (cur == null) {
                cur = curList.get(0);
            } else {
                cur  = curList.get(playingIndex + 1);
            }
        }
        return cur;
    }
}
