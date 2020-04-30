/**
 * DataStruct.java created by nohan on Lenovo Ideapad 520s in aTeam
 *
 * Author: Noah Hansen {nphansen@wisc.edu} Date: @date
 *
 * Course: CS400 Semester: Spring 2020 Lecture: 001
 *
 * IDE: Eclipse IDE for Java Developers Version: 2019-12 (14.4.0) Build id:
 *
 * Device: OS: Windows 10 Version: OS Build:
 *
 * List Collaborators: name, email
 *
 * Other Credits:
 *
 * Known Bugs:
 */
package application;

import java.util.ArrayList;

/**
 * MilkStore - Stores the milk weight data by farm.
 * 
 * Structure: Farm -> year -> month -> day = milk weight
 * 
 * @author Noah Hansen (2020)
 * 
 */
public class MilkStore {
  private ArrayList<Farm> farms;

  public MilkStore() {
    farms = new ArrayList<Farm>();
  }

  /**
   * 
   * Farm - identifies a farm and its yearly data
   * 
   * @author Noah Hansen (2020)
   *
   */
  class Farm {
    private String id = null;
    private ArrayList<Year> years;

    Farm(String id) {
      this.id = id;
    }

    @Override
    public boolean equals(Object farm) {
      if (this.id.equals(((Farm) farm).id))
        return true;
      else
        return false;
    }

    private Year getYear(Year year) {
      if (farms.isEmpty())
        this.years.add(year);

      for (Year curr : this.years) { // see if this farm already exists
        if (curr.equals(year)) {
          return curr;
        }
      }
      this.years.add(year); // new farm, so add to list
      return year;
    }
  }

  /**
   * 
   * Year - contains daily milk weight data for a specified year.
   * 
   * @author Noah Hansen (2020)
   *
   */
  class Year {
    int year;
    // milk weight indexed by month and day
    int[][] milkWeight = new int[12][31];

    Year(int year) {
      this.year = year;
    }

    @Override
    public boolean equals(Object year) {
      if (this.year == ((Year) year).year)
        return true;
      else
        return false;
    }
  }

  /**
   * finds the existing farm in the list. Will add a new farm if it isn't in the list.
   * 
   * @param farm- the farm to find or insert
   * @return the matching farm
   */
  private Farm getFarm(Farm farm) {
    if (farms.isEmpty())
      farms.add(farm);


    for (Farm curr : farms) { // see if this farm already exists
      if (curr.equals(farm)) {
        return curr;
      }
    }
    farms.add(farm); // new farm, so add to list
    return farm;
  }

  /**
   * Inserts the milk weight into the data structure. Duplicates(same day) will be replaced
   */
  public void insert(String id, int year, int month, int day, int weight) {
    Farm farm = new Farm(id);
    farm = getFarm(farm); // sets {var}farm to be the farm to access

    Year toInsert = new Year(year);
    Year found = farm.getYear(toInsert); // the year object with the milk we want

    found.milkWeight[month][day] = weight;
  }


  // TODO for making the reports, a list could be returned of all the columns.

  /**
   * gets the milk weight for the day
   * 
   * @return the weight
   */
  public int get(String id, int year, int month, int day) {
    Farm farm = new Farm(id);
    farm = getFarm(farm); // sets {var}farm to be the farm to access

    Year toInsert = new Year(year);
    Year found = farm.getYear(toInsert); // the year object with the milk we want

    return found.milkWeight[month][day];
  }


  /**
   * Sets the milk weight for the day to 0
   */
  public void remove(String id, int year, int month, int day) {
    Farm farm = new Farm(id);
    farm = getFarm(farm); // sets {var}farm to be the farm to access

    Year toInsert = new Year(year);
    Year found = farm.getYear(toInsert); // the year object with the milk we want

    found.milkWeight[month][day] = 0;
  }

  // How to write this out to a csv....



  // public int getWeight();
  //
  // public void getFarmTotalWeight();
  //
  // public void

}
