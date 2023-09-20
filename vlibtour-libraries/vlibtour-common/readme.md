
Library VlibTour Common
====

This module is a library and proposes classes that are used in other
Maven modules, which are either microservices or applications.

## Position and GPSPosition

The `Position` class defines the node of the graph for the emulation. A
position contains (by delegation) a `GPSPosition` object. It is assumed that
some of the positions are Points Of Interest (POI). Class `Position`
then possesses a reference to an Object that will refer to your
version of the class POI.

NB: class `Position` is the class used in the VLibTour Visit Emulation System,
i.e. in the Maven module `vlibtour-visit-emulation-server`.

## Logging

In the `Log` class, declare your loggers.

## Constants for the illustrative scenario

The `ExampleOfAVisitWithTwoTourists` class contains constants, i.e. public final
attributes that are used in the demonstrator,
i.e. in the Maven modules `vlibtour-tourist-application`, `vlibtour-tour-management-admin-client`, and `vlibtour-visit-emulation-server`.

For instance, the departure position of the tourists is a constant in class `ExampleOfAVisitWithTwoTourists`.

Another example: the positions of the POIs are also constants in class `ExampleOfAVisitWithTwoTourists`.

As a last example: the tour, i.e. the sequence of POIs, for the visit of the demonstration is again a constant in class `ExampleOfAVisitWithTwoTourists`.


