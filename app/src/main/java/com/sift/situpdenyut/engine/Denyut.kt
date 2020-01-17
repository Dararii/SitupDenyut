package com.sift.situpdenyut.engine

import java.io.Serializable

/**
 * Denyut data model
 */
data class Denyut(
    var value: Int,
    var timestamp: String
) : Serializable {

    constructor() : this(0, "0")

}