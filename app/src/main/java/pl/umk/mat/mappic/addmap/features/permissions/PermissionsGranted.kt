package pl.umk.mat.mappic.addmap.features.permissions

/**
 * This interface calls chosen method, after permissions were granted.
 */
fun interface PermissionsGranted {
    fun startAction()
}