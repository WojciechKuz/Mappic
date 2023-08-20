package com.student.mappic.addmap

/**
 * This are error types user can make in Add Map step 2 and 3.
 */
enum class ErrTypes {
    /** When user didn't marked point on map. */
    POINT_NOT_MARKED {
        override val msg = "Mark point on map"
    },
    /** When user didn't filled GPS data. */
    NOT_FILLED_GPS {
        override val msg = "Fill Location fields"
    },
    /** When user typed incorrect value in GPS fields. */
    INCORRECT_GPS {
        override val msg = "Incorrect values. Correct example: 20.000 N"
    },
    /** When user marked second point same as first or difference is too small. */
    SAME_POINT {
        override val msg = "Points 1 and 2 are too close or identical"
    };
    abstract val msg: String;
}