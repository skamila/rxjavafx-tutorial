package com.github.pkrysztofiak.rxjavafxtutorial.examples2;

import io.reactivex.Observable;

public class Example007 {

	public static void main(String[] args) {
		System.out.println("Observable");
		Observable.just("one", "one", "two", "one", "seven", "eight", "eight", "nine")
		.distinctUntilChanged()
		.subscribe(System.out::println);
	}
}