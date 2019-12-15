package com.sift.situpdenyut.engine

import java.io.Serializable

data class Denyut(
    var value: Int,
    var timestamp: String,
    var namaSession: String
) : Serializable {

    constructor() : this(0, "0", "")

}