package pl.umk.mat.mappic.db

import pl.umk.mat.mappic.db.entities.DBPoint

/**
 * MPoint, aka MapPoint or MyPoint
 *
 * Each MPoint has its position on screen (x & y px)
 * and its GPS position in real World (x & y gps).
 *
 * This allows app to know where the user is located on map picture.
 */
data class MPoint(
    val xpx: Int,
    val ypx: Int,
    /** xgps is East-West */
    val xgps: Double,
    /** ygps is North-South */
    val ygps: Double,
    /**
     * if point is reference point - this means that it's used to calculate user's position.
     * There should be only 2 reference points per image.
     */
    val reference: Boolean
) {
    companion object {
        fun toMPoint(dbPoint: DBPoint): MPoint {
            return MPoint(
                xpx = dbPoint.xpx!!.toInt(),
                ypx = dbPoint.ypx!!.toInt(),
                ygps = dbPoint.nsgps!!,
                xgps = dbPoint.ewgps!!,
                reference = dbPoint.reference!! == 1
            )
        }
    }
}