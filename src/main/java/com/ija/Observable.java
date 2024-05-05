/**
* @file Observable.java 
* @author Matúš Moravčík (xmorav48)
* @brief Class for registering observers and notifying them about changes in observed object. 
*/

package com.ija;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ija.Enums.ObservableProperty;
import com.ija.Interfaces.Observer;

public abstract class Observable {
  private HashMap<ObservableProperty, List<Observer>> observers = new HashMap<>();
  private List<Observer> defaultObservers = new ArrayList<>();

  public void addObserver(Observer observer, ObservableProperty key) {
    // Create the first observer for a given key
    if (observers.get(key) == null) {
      List<Observer> observerList = new ArrayList<>();
      observerList.add(observer);
      observers.put(key, observerList);
    }
    // Add another observer for a given key 
    else {
      observers.get(key).add(observer);
    }
  }

  public void addObserver(Observer observer) {
    defaultObservers.add(observer);
  }

  public void removeObserver(Observer observer, ObservableProperty key) {
    observers.remove(key, observer);
  }

  public void notifyObservers(ObservableProperty... observableProperties) {
    for (ObservableProperty observableProperty : observableProperties) {
      notifyPropertyObservers(observableProperty);
    }
    
    notifyModelObservers();
  }

  private void notifyModelObservers() {
    for (Observer observer : defaultObservers) {
      observer.update();
    }
  }

  private void notifyPropertyObservers(ObservableProperty observableProperty) {
    List<Observer> observersToNotify = observers.get(observableProperty);
    if (observersToNotify == null) return;
    
    for (Observer observer : observersToNotify) {
      observer.update();
    }
  }
}