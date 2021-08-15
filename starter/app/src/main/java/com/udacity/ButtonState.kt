package com.udacity

/*
Sealed classes and interfaces represent restricted class hierarchies that provide more control over inheritance.
All subclasses of a sealed class are known at compile time.
 */
sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}
/*
sealed class testing{
    object test : testing()
    object test1 : testing()
}*/
