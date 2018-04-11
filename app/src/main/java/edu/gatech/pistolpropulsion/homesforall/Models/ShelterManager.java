package edu.gatech.pistolpropulsion.homesforall.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * manager for shelters
 */
public class ShelterManager {
    private Shelter[] shelterArray;
    private int count;
    //private DatabaseReference mDatabase;

    /**
     * creates shelter manager given number of shelters
     * @param count number of shelters
     */
    public ShelterManager(int count) {
        shelterArray = new Shelter[count];
        this.count = count;
        //mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     *  constructor given 2D string array and number of shelters
     * @param data what was read in from CSV file
     */
    public  ShelterManager(String[][] data) {
        this(data.length);
        for(int i = 0; i < data.length; i++) {
            Shelter newShelter = new Shelter(data[i][0], data[i][1], data[i][2],
                    data[i][3], data[i][4],
                    data[i][5], data[i][6], data[i][7], data[i][8]);
            shelterArray[i] = newShelter;
            //mDatabase.child("shelters").child(newShelter.getKey()).setValue(newShelter);
        }
    }

    /**
     * empty constructor
     */
    public ShelterManager() {
        this(0);
    }

    /**
     * gets an array of all the shelter names
     * @param count number of shelters
     * @return String array of all shelter names
     */
    private String[] makeNamesArray(int count) {
        String [] names = new String[count];
        for(int i = 0; i < count; i++){
            names[i] = shelterArray[i].getName();
        }
        return names;
    }

    /**
     * getter for names array
     * @return String array of all shelter names
     */
    public String[] getNamesArray() {
        return makeNamesArray(count);
    }

    /**
     * gets all the data in this in manager in a Shelter array
     * @return an array of shelters
     */
    public Shelter[] getShelterArray() {
        return this.shelterArray;
    }

    /**
     * given a shelter array, set manager data to that array
     * @param array what to set manager's data to
     */
    public void setShelterArray(Shelter[] array) {
        if (array == null) {
            throw new IllegalArgumentException("array can't be null");
        }
        this.shelterArray = array;
        count = array.length;
    }

    /**
     * searches all shelters given options
     * @param options list of strings representing search options
     * @return all the shelters that correspond to given options
     */
    public Shelter[] search(List<String> options) {
//        for (String items: options){
//            System.out.println(items);
//        }
        ArrayList<Shelter> searchList = new ArrayList<>();
        for (Shelter aShelterArray : shelterArray) {
            for (int j = 0; j < options.size(); j++) {
                if (aShelterArray.canAccommodate(options.get(j))) {
                    if (!searchList.contains(aShelterArray)) {
                        searchList.add(aShelterArray);
                    }
                }
            }
        }
//        for(Shelter item: searchList){
//            System.out.println(item.getName());
//        }
        //System.out.println(searchList.size());
        return searchList.toArray(new Shelter[searchList.size()]);
    }

    /**
     * searches for all shelters if their name contains the srearch string
     * @param search string you're searching for
     * @return an array of shelters containing the search string
     */
    public Shelter[] searchName(String search) {
        if (search == null) {
            throw new IllegalArgumentException(
                    "Cannot search shelters with a null string.");
        } else if (search.equals("")) {
            return Arrays.copyOf(shelterArray, shelterArray.length);
        }
        ArrayList<Shelter> searchList = new ArrayList<>();
        for (Shelter shelter : shelterArray) {
            if (shelter.getName().toLowerCase().contains(search.toLowerCase())
                    && !searchList.contains(shelter)) {
                searchList.add(shelter);
            }
        }
        return searchList.toArray(new Shelter[searchList.size()]);
    }
}
