package com.student.mappic.addmap

/**
 * This are error types user can make in Add Map step 2 and 3.
 */
enum class ErrTypes(val code: Int) {
    UNKNOWN(0),
    /** When user didn't marked point on map. */
    POINT_NOT_MARKED(1),
    /** When user didn't filled GPS data. */
    NOT_FILLED_GPS(2),
    /** When user typed incorrect value in GPS fields. */
    INCORRECT_GPS(3),
    /** When user marked second point same as first or difference is too small. */
    SAME_POINT(4);
}