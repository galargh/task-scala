# Scala Task oratio

## Installation

1. Install either [sbt](http://www.scala-sbt.org/0.13/docs/Setup.html) or [activator](https://www.playframework.com/documentation/2.5.x/Installing)
2. Run with `sbt ~run` or `activator ~run`

## Task

The main route is `/task/:actor`, where `:actor` is a string parameter. if `:actor` is "list", the `ListActor` should be called, if `:actor`
is "user" the `UserActor` should be called. A optional GET parameter called `param` can be passed to perform various actions on this actors.

e.g.

`GET /task/list` will return the list from ListActor as it is

`GET /task/list?param=sum` will return the sum of the list from ListActor
...

More information can be found within the `ListActor` and `UserActor` class.

## Controllers

- HomeController.scala:

  Main entry point, contains the Action for the task.


## Actors

- ListActor.scala:

  An actor containing a list. It expects various parameters to perform actions on this list.

- UserActor.scala:

  An actor containing a list of User case classes. It expects various parameters to perform actions on this list.

