# Thrice

*Once is chance; twice is coincidence; thrice is a pattern.*

If you've needed a piece of code three times or more, it may be worth putting it into a library.
Thrice is a collection of Java classes for which its author have found a need on three or more
occasions.


## Contents
1. [Release Notes](#release-notes)
1. [Documentation](#documentation)
1. [Availability](#availability)


## Release Notes

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
