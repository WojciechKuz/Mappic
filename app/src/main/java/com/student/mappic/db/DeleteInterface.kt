package com.student.mappic.db

import android.content.Context

fun interface DeleteInterface {
    fun delete(context: Context, mapid: Long)
}

/* A problem story:
    This package used to be named com.student.mappic.DB

    When I renamed it (refactor) to com.student.mappic.db,
    and tried to build project errors like "unresolved reference to <anything that is in this package>"
    occurred.

    To fix it I selected Build > Clean Project,
    also I deleted content of Mappic/app/schema directory (database schema),
    because element in it had old package name.

    Then I just build/rebuild project and everything was fine.
 */