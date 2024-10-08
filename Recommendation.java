import java.io.FileNotFoundException;
import java.io.File;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Collections;
import java.util.LinkedList;
import java.util.LinkedHashMap;


public class Recommendation {

    Map<Long, HashMap<String, Integer>> userData;
    private Scanner scanner;

    public Recommendation(String filePath) {
        this.userData = new HashMap<>();
        parseCsvFile(filePath);
    }

    private void parseCsvFile(String csvFilePath) {
        int counter = 0;

        try {
            scanner = new Scanner(new File(csvFilePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        scanner.useDelimiter(",");

        while (scanner.hasNextLine()) {
            if (counter == 0) {
                scanner.nextLine().split(",");
                counter++;
            } else {
                String[] arrayRep = scanner.nextLine().split(",");
                Long userID = Long.valueOf(arrayRep[0]);
                String artistName = arrayRep[1];
                Integer minutesSong = Integer.valueOf(arrayRep[3]);

                if (!userData.containsKey(userID)) {
                    HashMap<String, Integer> hashMap = new HashMap<>();
                    hashMap.put(arrayRep[1], Integer.valueOf(arrayRep[3]));
                    userData.put(userID, hashMap);
                } else if (userData.containsKey(userID)) {
                    if (userData.get(userID).containsKey(artistName)) {
                        int addMinutes = userData.get(userID).get(artistName) + minutesSong;
                        userData.get(userID).replace(artistName, addMinutes);
                    } else {
                        userData.get(userID).put(artistName, minutesSong);
                    }
                }
                counter++;
            }
        }
        scanner.close();

        Map<Long, HashMap<String, Integer>> userDataNew = new HashMap<>();
        Long newLong = 1L;
        for (Map<String, Integer> a : userData.values()) {
            List<Entry<String, Integer>> list =
                    new LinkedList<Entry<String, Integer>>(a.entrySet());
            Collections.sort(list, new Comparator<Entry<String, Integer>>() {
                public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
            for (Entry<String, Integer> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }
            if (!userDataNew.containsKey(newLong)) {
                userDataNew.put(newLong, sortedMap);
                newLong++;
            }
        }
        userData = userDataNew;


        Map<Long, HashMap<String, Integer>> lastData = new HashMap<>();
        Long newerLong = 1L;
        for (Map<String, Integer> a : userData.values()) {
            HashMap<String, Integer> sortedMap1 = new LinkedHashMap<String, Integer>();
            int i = 1;
            for (Entry<String, Integer> entrySet : a.entrySet()) {
                if (i <= 5) {
                    sortedMap1.put(entrySet.getKey(), entrySet.getValue());
                    i++;
                } else {
                    break;
                }
            }
            if (!lastData.containsKey(newerLong)) {
                lastData.put(newerLong, sortedMap1);
                newerLong++;
            }
        }
        userData = lastData;
    }


    public String[] recommendNewArtists(List<String> artistList) {
        if (artistList.size() == 0) {
            String[] emptyList = new String[6];
            return emptyList;
        }
        HashMap<Long, Double> topMatch = new HashMap<>();
        Long newLong = 1L;
        for (Map<String, Integer> a : userData.values()) {
            double counter1 = 0;
            Set setArtistList = new HashSet();
            for (String al : artistList) {
                counter1++;
            }
            for (String ks : a.keySet()) {
                counter1++;
            }
            //we have the bottom of the fraction, now we need the top
            double counter = 0.0;
            for (String al : artistList) {
                if (a.keySet().contains(al)) {
                    counter++;
                }
            }
            Double jaccardSimilarity = counter / counter1;
            topMatch.put(newLong, jaccardSimilarity);
            newLong++;
        }
        HashMap<Long, String> topThree = new HashMap<>();
        Map.Entry<Long, Double> maxEntry = null;
        for (Map.Entry<Long, Double> entry : topMatch.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        topThree.put(maxEntry.getKey(), String.valueOf(maxEntry.getValue()));
        topMatch.replace(maxEntry.getKey(), -1.0);
        maxEntry = null;
        for (Map.Entry<Long, Double> entry : topMatch.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        topThree.put(maxEntry.getKey(), String.valueOf(maxEntry.getValue()));
        topMatch.replace(maxEntry.getKey(), -1.0);
        maxEntry = null;
        for (Map.Entry<Long, Double> entry : topMatch.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        topThree.put(maxEntry.getKey(), String.valueOf(maxEntry.getValue()));

        ArrayList<String> names = new ArrayList<>();

        for (Long l : topThree.keySet()) {
            HashMap<String, Integer> artists = userData.get(l);
            names.addAll(artists.keySet());
        }
        ArrayList<String> newNames = new ArrayList<>();
        for (String n : names) {
            if (!artistList.contains(n)) {
                newNames.add(n);
            }
        }
        HashSet<String> namesSet = new HashSet<>(newNames);
        Object[] namesArray = namesSet.toArray();

        String[] finalNames = new String[namesSet.size()];

        for (int i = 0; i < namesSet.size(); i++) {
            finalNames[i] = (String) namesArray[i];
        }
        return finalNames;
    }
}
