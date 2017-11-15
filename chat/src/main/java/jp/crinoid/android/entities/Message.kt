package jp.crinoid.android.entities

import com.google.gson.Gson

/**
 * @author dekaru on 5/9/17.
 */

class Message(val text: String) {

    var direction: Int = 0
    var deviceName: String? = null

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {

        val INCOMING_MESSAGE = 0
        val OUTGOING_MESSAGE = 1


        fun create(json: String): Message {
            return Gson().fromJson(json, Message::class.java!!)
        }
    }
}
