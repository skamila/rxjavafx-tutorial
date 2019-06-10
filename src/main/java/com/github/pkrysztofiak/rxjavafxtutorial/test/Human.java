package com.github.pkrysztofiak.rxjavafxtutorial.test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Human {

    StringProperty name = new SimpleStringProperty();
    IntegerProperty age = new SimpleIntegerProperty();

    public Human(String name, int age) {
        this.name.setValue(name);
        this.age.setValue(age);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getAge() {
        return age.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setAge(int age) {
        this.age.set(age);
    }

}
