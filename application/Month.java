/**
 * Filename: Month.java
 * 
 * Project: aTeam
 * 
 * date: 4/21/202
 * 
 * Authors: Gunnar Schmitz
 * 
 */
package application;

import javafx.beans.property.SimpleStringProperty;

public class Month {

  private final SimpleStringProperty weight;
  private final SimpleStringProperty percent;
  private final SimpleStringProperty month;

  Month(String weight, String lName, String email) {
    this.weight = new SimpleStringProperty(weight);
    this.percent = new SimpleStringProperty(lName);
    this.month = new SimpleStringProperty(email);
  }

  public String getWeight() {
    return weight.get();
  }

  public void setWeight(String w) {
    weight.set(w);
  }

  public String getPercent() {
    return percent.get();
  }

  public void setPercent(String p) {
    percent.set(p);
  }

  public String getMonth() {
    return month.get();
  }

  public void setMonth(String m) {
    month.set(m);
  }

}
