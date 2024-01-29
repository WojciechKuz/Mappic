package pl.umk.mat.mappic.common

/**
 * This are error types user can make in Add DBMap [Step2Fragment], [Step3Fragment] and [Step4Fragment].
 */
enum class ErrTypes(val code: Int) {
    UNKNOWN(0),
    /** When user didn't marked point on map. */
    POINT_NOT_MARKED(1),
    /** When user didn't filled GPS data. */
    NOT_FILLED_GPS(2),
    /** When user typed incorrect value in GPS fields. */
    INCORRECT_GPS(3), // There could be more types of this error. incorrect value, value out of bounds, incorrect direction(NEWS)
    /** When user marked second point same as first or difference is too small. (less than 10m) */
    SAME_POINT(4),
    /** This is only for testing purposes */
    TEST_ERR_MESSAGE(5),
    /** MPoint marked outside image */
    POINT_OUT_OF_BOUNDS(6),
    /** DBMap with this name already exists. */
    NAME_EXISTS(7),
    /** If size data of original image were not found */
    NO_SIZE_DATA(8);
}