package pl.umk.mat.mappic.db

import android.content.Context
import android.net.Uri
import pl.umk.mat.mappic.db.entities.DBImage
import pl.umk.mat.mappic.db.entities.DBMap
import pl.umk.mat.mappic.db.entities.DBPoint
import pl.umk.mat.mappic.addmap.NewMapViewModel
import java.util.stream.Collectors

interface DBAccess {
    /**
     * Adds everything what new map needs to database:
     * map record, image record and 2 point records
     */
    fun addNewMap(context: Context, viewModel: NewMapViewModel) {
        val db = MapDatabase.getDB(context)

        // add map record, get it's id
        val mapR = DBMap(map_name = viewModel.name)
        db.mapDao().insertAll(mapR)
        val newMapId:  Long = db.mapDao().getMaxId()
        // make sure it's correct map
        if(!db.mapDao().getMap(newMapId)[0].map_name.equals(viewModel.name)) {
            throw Exception("Can't find newly added map in database")
        }

        // add image record, get it's id
        val imageR = DBImage(mapid_fk = newMapId, uri = viewModel.mapImg.toString())
        db.imageDao().insertAll(imageR)
        val newImageId: Long = db.imageDao().getMaxId() // Can this be a source of bugs? Is always newly added record id equal to maxid?
        if(!db.imageDao().getImage(newImageId)[0].uri.equals(viewModel.mapImg.toString())) {
            throw Exception("Can't find newly added image in database")
        }

        // add points
        val pointR1 = DBPoint.toDBPoint(viewModel.p1, newImageId)
        val pointR2 = DBPoint.toDBPoint(viewModel.p2, newImageId)
        db.pointDao().insertAll(pointR1, pointR2)
    }

    /**
     * Fill viewModel with existing data for editing.
     */
    fun getEditMap(context: Context, mapid: Long, viewModel: NewMapViewModel) {
        val db = MapDatabase.getDB(context)

        viewModel.mapid = mapid

        // for now there's one image per map, in future this may change
        val image = db.imageDao().getImagesForMap(mapid)[0]
        viewModel.mapImg = Uri.parse(image.uri)

        val refPoints = db.pointDao().getReferencePointsForImage(image.imgid!!)
        viewModel.p1 = MPoint.toMPoint(refPoints[0])
        viewModel.p2 = MPoint.toMPoint(refPoints[1])
    }

    /**
     * Add edited data to database.
     */
    fun editMap(context: Context, viewModel: NewMapViewModel) {
        val db = MapDatabase.getDB(context)

        // step1: getting and updating objects
        if (viewModel.mapid == null) {
            throw Exception("NewMapViewModel.mapid is null!")
        }
        val oldMap = db.mapDao().getMap(viewModel.mapid!!)[0]
        val newMap = DBMap(
            oldMap.mapid,
            viewModel.name
        )
        val oldImg = db.imageDao().getImagesForMap(viewModel.mapid!!)[0]
        viewModel.mapImg = Uri.parse(oldImg.uri)
        val newImg = DBImage(
            oldImg.imgid,
            oldImg.mapid_fk,
            viewModel.mapImg.toString()
        )
        val oldPoints = db.pointDao().getReferencePointsForImage(oldImg.imgid!!)
        val newPoints = listOf(
            DBPoint.updatePoint(oldPoints[0], viewModel.p1),
            DBPoint.updatePoint(oldPoints[1], viewModel.p2)
        )

        // step2: add updated objects to database
        db.mapDao().updateMaps(newMap)
        db.imageDao().updateImages(newImg)
        db.pointDao().updatePoints(newPoints[0], newPoints[1])
    }

    fun getMapList(context: Context): List<DBMap> {
        val db = MapDatabase.getDB(context)
        return db.mapDao().mapList()
    }
    fun getMapImages(context: Context, mapid: Long): List<DBImage> {
        val db = MapDatabase.getDB(context)
        return db.imageDao().getImagesForMap(mapid)
    }
    fun getMapPoints(context: Context, mapid: Long): List<DBPoint> {
        val db = MapDatabase.getDB(context)
        return db.pointDao().getPointsOnMap(mapid)
    }

    fun getMapName(context: Context, mapid: Long): String? {
        val db = MapDatabase.getDB(context)
        return db.mapDao().getMap(mapid)[0].map_name
    }
    // possibility to add multiple points
    fun addPoint() {}
    fun deletePoint() {} //(do not delete reference points. But ref points can be invisible)
    fun editPoint() {}

    /**
     * Delete everything that is associated with map and map itself.
     */
    fun deleteMap(context: Context, mapid: Long) {
        //delPointsByMapId_1(context, mapid)
        val db = MapDatabase.getDB(context)
        db.pointDao().deletePointsForMapid(mapid)
        db.imageDao().deleteImageForMapid(mapid)
        db.mapDao().deleteById(mapid)
    }
    private fun delPointsByMapId_1(context: Context, mapid: Long) {
        val db = MapDatabase.getDB(context)
        val delPoints: List<DBPoint> = db.pointDao().getPointIdsOnMap(mapid)
        val delPointIds: List<Long> = delPoints.stream().map{ point -> point.pid!! }.collect(Collectors.toList())
        db.pointDao().deleteMultiple(delPointIds)
    }



    fun getStuff(context: Context, mapid: Long) {
        val db = MapDatabase.getDB(context)
    }
}