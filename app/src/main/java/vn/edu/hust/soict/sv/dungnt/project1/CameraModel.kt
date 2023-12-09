package vn.edu.hust.soict.sv.dungnt.project1

import com.google.firebase.firestore.PropertyName

class CameraModel {
    @get:PropertyName("cameraName")
    @set:PropertyName("cameraName")
    var cameraName: String = ""

    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = ""

    constructor()

    constructor(cameraName: String) {
        this.cameraName = cameraName
        this.id = id
    }
}
