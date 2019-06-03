# RxJavaFx tutorial
RxJavaFx is merely a layer between JavaFx and RxJava technologies.
### Preface
JavaFx brought a new concepts of **Binding**s and **ObservableValue**s. The idea of events triggering other events, and a value notifying another value of its changes seems to be very promising.
But after studying JavaFx deeper one may become discontent. JavaFx does have **Binding** functionality that can synchronize properties and events of different controls, but the ability to express transformations is pretty limited.

Reactive programming brings means for simple and intuitive data processing between synchronized properties. And goes far beyond that. It extends the observer pattern to support sequences of data and/or events and allows you to compose sequences together while abstracting away concerns about things like low-level threading, synchronization, thread-safety, concurrent data structures, and non-blocking I/O.
### RxJava
As mentioned before RxJavaFx is just a bridge between RxJava and JavaFx. To get the idea behind the technology we need to start from Rx.

RxJava has two core types: **Observable** and **Observer**. 
In the simplest definition **Observable** pushes things, a given **Observable\<T>** will push items of type **T** through a series of operators that form other **Observable**s and finally the terminal **Observer** is what consumes the items at the end of the chain. Each pushed T item is known as an emission.

    Observable<Integer> emissionsSource = Observable.just(1, 2, 3, 4, 5);
As you may have noticed the form of the factory is Stream alike.

    Stream<Integer> numbersStream = Stream.of(1, 2, 3, 4, 5);
Where possible I will try to yield Stream counterpart to emphasize that we already know the external mechnics of basic Observable operators.

**Observer** is composed out of three methods:
```java
	new ResourceObserver<T>() {

		@Override
		public void onNext(T t) {
			//is called to pass the emission
		}

		@Override
		public void onError(Throwable e) {
			//is called when exception occured
		}

		@Override
		public void onComplete() {
			//is called when there are no more emissions
		}
	};
```
Lets get one with some examples.

    Observable<Integer> emissionsSource = Observable.just(1, 2, 3, 4, 5);
	emissionsSource.subscribe(number -> System.out.println("number=" + number));
Example001
Produces an output:

    number=1    
    number=2    
    number=3    
    number=4    
    number=5

After subscribing to an Observable it starts to produce an emissions which are handled by the Observer. The Observer we deal with is called finite or cold. In oposition to hot Observables it has predefined number of emissions and in consequence will call Observer onComplete() method when all emissions are passed.
For now just note RxJava has no notion of parallelization, and when you subscribe to a factory like Observable.just(1, 2, 3, 4, 5) , you will always get those emissions serially, in that exact order, and on a single thread.

### Operators
Let us do something a little more useful than just connecting a source
Observable and an Observer . Let's put some operators between them to
actually transform emissions and do work. In RxJava, you can use hundreds of operators to transform emissions and create new Observables with those transformations.
#### map()
 For instance, you can create an Observable\<Integer> off an Observable<String> by using the map() operator, and use it to emit each String's length. The *lettersObservable* Observable pushes each String to the map() operator where it is mapped to its length . That length is then pushed from the map() operator to the Observer where it is printed.
```java
	Observable<String> lettersObservable = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

	lettersObservable
	.map(letter -> letter.length())
	.subscribe(length -> System.out.println("length=" + length));
```
Example002
Produces output:

    length=5    
    length=4    
    length=5    
    length=5    
    length=7

#### doOnNext()
Is a debug operator. Passes emission but does nothing with it.
```java
	Observable<String> numbersObservable = Observable.just("one", "two", "three");
	numbersObservable
	.doOnNext(next -> System.out.println("next=" + next))
	.map(String::length)
	.subscribe(length -> System.out.println("length=" + length));
```
Example003
Produces output:

    next=one
    length=3
    next=two
    length=3
    next=three
    length=5

#### filter()
Another common operator is filter() , which suppresses emissions that fail to
meet a certain criteria, and pushes the ones that do forward.
```java
	Observable.just("one", "two", "three", "four", "five")
	.filter(next -> next.length() > 3)
	.subscribe(next -> System.out.println("next=" + next));
```
Example004
Produces output:

    next=three
    next=four
    next=five
#### distinct()
There are also operators like distinct() , which will suppress emissions that
have previously been emitted to prevent duplicate emissions (based on each
emission's hashcode() / equals() implementation).
```java
	Observable.just("one", "two", "three", "four", "five")
	.map(String::length)
	.distinct()
	.subscribe(System.out::println);
```
Example005
Produces output:

    3
    5
    4
You can also provide a lambda specifying an attribute of each emitted item to distinct on, rather than the item itself.
```java
	Observable.just("one", "two", "three", "four", "five")
	.distinct(String::length)
	.subscribe(System.out::println);
```
Exmaple006
Produces output:

    one
    three
    four
#### distinctUntilChanged()
Will not supress only if current emission is different from the last one.
```java
	Observable.just("one", "one", "two", "one", "seven", "eight", "eight", "nine")
	.distinctUntilChanged()
	.subscribe(System.out::println);
```
Example007
Produces output:

    one
    two
    one
    seven
    eight
    nine
#### take()
The take() operator will cut off at a fixed number of emissions and then unsubscribe from the source. Afterwards, it will call onComplete() downstream to the final Observer.
```java
	Observable.just("one", "two", "three", "four", "five")
	.take(3)
	.subscribe(
			next -> System.out.println("next=" + next), //onNext
			throwable -> throwable.printStackTrace(),	//onError
			() -> System.out.println("completed!")		//onComplete
			);
```
Example008
Produces output:

    next=one
    next=two
    next=three
    completed!
#### takeWhile()
takeWhile() will do something similar to take() , but specifies a lambda condition which, if not satisfied will unsubscribe from the Observer.
```java
	Observable.just("one", "two", "three", "four", "five")
	.takeWhile(number -> number.length() == 3)
	.subscribe(
			next -> System.out.println("next=" + next), //onNext
			throwable -> throwable.printStackTrace(),	//onError
			() -> System.out.println("completed!")		//onComplete
			);
```
Exmaple009
Produces output:

    next=one
    next=two
    completed!
#### takeUntil()
takeUntil() will do something similar to take() , but specifies a lambda condition which, if satisfied will unsubscribe from the Observer after pushing the emission which caused unsubscribing.
```java
	Observable.just("one", "two", "three", "four", "five")
	.takeUntil((String number) -> number.startsWith("f"))
	.subscribe(
			next -> System.out.println("next=" + next), //onNext
			throwable -> throwable.printStackTrace(),	//onError
			() -> System.out.println("completed!")		//onComplete
			);
```
Example010
Produces output:

    next=one
    next=two
    next=three
    next=four
    completed!
#### count()
Some operators will aggregate the emissions in some form, and then push that aggregation as a single emission to the Observer. Obviously, this requires the onComplete() to be called so that the aggregation can be finalized and pushed to the Observer.
The count() actually returns a Single , which is a specialized Observable type that only emits one item. The Single does not have an onNext or onComplete , but rather an onSuccess event which passes the single item. If you ever need to turn a Single back into an Observable (so it works with certain Observable operators), just call its toObservable() method.
```java
	Observable.just("one", "two", "three", "four", "five")
	.count()
	.subscribe(
			result -> System.out.println("onSucces=" + result), //onSuccess
			throwable -> throwable.printStackTrace()			//onError
			);
```
Example011
Produces output:

    onSucces=5
#### toList()
The toList() is similar to the count() , and it also will yield a Single
rather than an Observable . It will collect the emissions until its onComplete()
is called. After that it will push an entire List containing all the emissions to the
Observer .
```java
	Observable.just("one", "two", "three", "four", "five")
	.toList()
	.subscribe(
			list -> System.out.println("onSucces=" + list), //onSuccess
			throwable -> throwable.printStackTrace()		//onError
			);
```
Example012
Produces output:

    onSucces=[one, two, three, four, five]
#### reduce()
