# Thrice

*Once is chance; twice is coincidence; thrice is a pattern.*

If you've needed a piece of code three times or more, it may be worth putting it into a library.
Thrice is a collection of Java classes for which its author has found a need on three or more
occasions.


## Contents
1. [Release Notes](#release-notes)
1. [Documentation](#documentation)
1. [Availability](#availability)


## Release Notes

### version 3.5
* Primitive specializations of `Sequence` added.
* `PrimitiveIterators` factory class for `java.util.PrimitiveIterator` implementations added.
* `AlphaCodes` and `Nothing` added to `util` package.
* `Numbers.requireRangeWithinBounds()` added.
* Methods with return values that may be null depending on the context are no longer annotated with
  `javax.annotation.Nullable`. Some tools and IDEs no longer use that annotation in its original
  sense, but as an equivalent to `javax.annotation.CheckForNull`, which may cause false
  `NullPointerException` warnings.

### version 3.4
* `GetByteAtFunction`, `PutByteAtFunction`, and `Utf8` added to `util` package.

### version 3.3

* `PollableFuture` added to `concurrent` package.
* `GetCharAtFunction`, `PutCharAtFunction`, and `TimeSource` added to `util` package.
* `Ascci.isAsciiHexDigit()` added.
* `Strings.bytesToCompactHexString()` added.
* Overloaded `Strings.asString()` without default value argument added.
* `formatInt()` and `formatLong()` added to `Numbers`.
* Overloaded `Numbers.requireNonNegative()` for all primitive number types added.
* `singletonIterator()`, `arrayIterator()`, and `sequenceIterator()` added to `Iterators`.
* `Sequence` implements `Iterable`.
* Build related tools and libraries upgraded to the latest versions.

### version 3.2

* `Numbers.countDigits()` added.
* Build related tools and libraries upgraded to the latest versions.

### version 3.1

* Thrice is packaged as a modular jar file. The classes in the jar file are still targeted for
Java 8.

### version 3.0

* First official release. Previous versions were semi-official and not generally available.


## Documentation

### Building

Thrice must be built with JDK 9 or later (but the classes are targeted for Java 8).

The command

    ./gradlew build

or an equivalent should do the trick.

### Using

Thrice requires Java 8 or later. The library is packaged in a modular jar file that contains the 
module `org.myire.thrice`, which exports all packages in the project and requires no other modules.

The rest is in the JavaDocs.


## Availability

Thrice can be found at Maven Central with the group ID `org.myire` and the artifact ID `thrice`.
