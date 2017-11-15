package jp.crinoid.android.entities

import com.google.gson.Gson

/**
 * @author dekaru on 5/9/17.
 */

class Peer(val uuid: String, val deviceName: String) {
    var isNearby: Boolean = false
    var deviceType: DeviceType? = null

    enum class DeviceType {
        UNDEFINED,
        ANDROID,
        IPHONE
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {


        fun create(json: String): Peer {
            return Gson().fromJson(json, Peer::class.java!!)
        }
    }
}
