/**
 * <b>May not add any accessor/mutator for this class</b>
 */
public class PlayableItem {
    private int lastPlayedTime;
    private int totalPlayTime;
    private String endpoint;
    private String title;
    private String artist;
    private int popularity;
    private int playedCounts; // How many time this song has been played, initially to be 0

    public PlayableItem(int lastTime, int totalPlayTime, String endpoint, String title,
                        String artist, int popularity) {
        this.lastPlayedTime = lastTime;
        this.totalPlayTime = totalPlayTime;
        this.endpoint = endpoint;
        this.title = title;
        this.artist = artist;
        this.popularity = popularity;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getTitle() {
        return this.title;
    }

    public int getPopularity() {
        return this.popularity;
    }

    public void setPopularity(int pop) {
        this.popularity = pop;
    }

    public boolean playable() {
        return this.lastPlayedTime != totalPlayTime;
    }

    public boolean play() {
        if ((this.lastPlayedTime + 1) < this.totalPlayTime) {
            this.lastPlayedTime++;
            if (playable()) {
                return true;
            }
        }
        if (lastPlayedTime == totalPlayTime) {
            this.lastPlayedTime = 0;
            return true;
        }
        return false;
    }

    public boolean equals(PlayableItem another) {
        return getTitle() == another.getTitle() && getArtist() == another.getArtist()
                && this.totalPlayTime == another.totalPlayTime && this.endpoint == another.endpoint;
    }

    public String toString() {
        return this.title + "," + this.endpoint + "," + this.lastPlayedTime + ","
                + this.totalPlayTime + ","
                + this.artist + "," + this.popularity + "," + this.playedCounts;
    }

    public int compareTo(PlayableItem o) {
        return this.playedCounts - o.playedCounts;
    }
}
