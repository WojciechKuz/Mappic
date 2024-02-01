package pl.umk.mat.mappic.common

/** Universal functional interface for passing data  */
fun interface PassStuff<T> {
    fun pass(stuff: T)
}