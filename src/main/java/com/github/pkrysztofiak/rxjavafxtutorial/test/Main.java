package com.github.pkrysztofiak.rxjavafxtutorial.test;

import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Application;
import io.reactivex.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){

        ObservableList<Human> humans = FXCollections.observableArrayList();

        Human h1 = new Human("Anna", 45);
        Human h2 = new Human("Jon", 86);

        //humans.addAll(h1, h2);

        Observable<Human> ob = JavaFxObservable.additionsOf(humans)
                .flatMap(human -> JavaFxObservable.valuesOf(human.name).map(h -> human));

        ob.subscribe(a -> System.out.println(a.getName()));

        humans.add(h1);
        humans.add(h2);

        h1.setAge(20);
        h2.setName("Jan");
        h2.setName("Janek");


    }

}
