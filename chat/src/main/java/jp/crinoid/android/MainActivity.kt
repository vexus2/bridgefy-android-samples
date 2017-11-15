package jp.crinoid.android

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.bridgefy.sdk.client.Bridgefy
import com.bridgefy.sdk.client.BridgefyClient
import com.bridgefy.sdk.client.Device
import com.bridgefy.sdk.client.Message
import com.bridgefy.sdk.client.MessageListener
import com.bridgefy.sdk.client.RegistrationListener
import com.bridgefy.sdk.client.Session
import com.bridgefy.sdk.client.StateListener
import jp.crinoid.android.entities.Peer

import java.util.ArrayList
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    internal var peersAdapter = PeersRecyclerViewAdapter(ArrayList())

    private val messageListener = object : MessageListener() {
        override fun onMessageReceived(message: Message?) {
            // direct messages carrying a Device name represent device handshakes
            if (message!!.content["device_name"] != null) {
                val peer = Peer(message.senderId,
                        message.content["device_name"] as String)
                peer.isNearby = true
                peer.deviceType = extractType(message)
                peersAdapter.addPeer(peer)

                // any other direct message should be treated as such
            } else {
                val incomingMessage = message.content["text"] as String
                LocalBroadcastManager.getInstance(baseContext).sendBroadcast(
                        Intent(message.senderId)
                                .putExtra(INTENT_EXTRA_MSG, incomingMessage))
            }

            if (isThingsDevice(this@MainActivity)) {
                //if it's an Android Things device, reply automatically
                val content = HashMap<String, Any>()
                content.put("text", "Beep boop. I'm a bot.")
                val replyMessage = Bridgefy.createMessage(message.senderId, content)
                Bridgefy.sendMessage(replyMessage)

            }
        }

        override fun onBroadcastMessageReceived(message: Message?) {
            // we should not expect to have connected previously to the device that originated
            // the incoming broadcast message, so device information is included in this packet
            val incomingMsg = message!!.content["text"] as String
            val deviceName = message.content["device_name"] as String
            val deviceType = extractType(message)

            LocalBroadcastManager.getInstance(baseContext).sendBroadcast(
                    Intent(BROADCAST_CHAT)
                            .putExtra(INTENT_EXTRA_NAME, deviceName)
                            .putExtra(INTENT_EXTRA_TYPE, deviceType)
                            .putExtra(INTENT_EXTRA_MSG, incomingMsg))
        }
    }

    internal var stateListener: StateListener = object : StateListener() {
        override fun onDeviceConnected(device: Device?, session: Session?) {
            // send our information to the Device
            val map = HashMap<String, Any>()
            map.put("device_name", Build.MANUFACTURER + " " + Build.MODEL)
            map.put("device_type", Peer.DeviceType.ANDROID.ordinal)
            device!!.sendMessage(map)
        }

        override fun onDeviceLost(peer: Device?) {
            Log.w(TAG, "onDeviceLost: " + peer!!.userId)
            peersAdapter.removePeer(peer)
        }

        override fun onStartError(message: String?, errorCode: Int) {
            Log.e(TAG, "onStartError: " + message!!)

            if (errorCode == StateListener.INSUFFICIENT_PERMISSIONS) {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure the Toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.title = title

        val recyclerView = findViewById<View>(R.id.peer_list) as RecyclerView
        recyclerView.adapter = peersAdapter



        if (isThingsDevice(this)) {
            //enabling bluetooth automatically
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            bluetoothAdapter.enable()
        }

        Bridgefy.initialize(applicationContext, object : RegistrationListener() {
            override fun onRegistrationSuccessful(bridgefyClient: BridgefyClient?) {
                // Start Bridgefy
                startBridgefy()
            }

            override fun onRegistrationFailed(errorCode: Int, message: String?) {
                Toast.makeText(baseContext, getString(R.string.registration_error),
                        Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing)
            Bridgefy.stop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_broadcast -> {
                startActivity(Intent(baseContext, ChatActivity::class.java)
                        .putExtra(INTENT_EXTRA_NAME, BROADCAST_CHAT)
                        .putExtra(INTENT_EXTRA_UUID, BROADCAST_CHAT))
                return true
            }
        }
        return false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


    /**
     * BRIDGEFY METHODS
     */
    private fun startBridgefy() {
        Bridgefy.start(messageListener, stateListener)
    }

    fun isThingsDevice(context: Context): Boolean {
        val pm = context.packageManager
        return pm.hasSystemFeature("android.hardware.type.embedded")
    }

    private fun extractType(message: Message?): Peer.DeviceType {
        val eventOrdinal: Int
        val eventObj = message!!.content["device_type"]
        if (eventObj is Double) {
            eventOrdinal = eventObj.toInt()
        } else {
            eventOrdinal = eventObj as Int
        }
        return Peer.DeviceType.values()[eventOrdinal]
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // Start Bridgefy
            startBridgefy()

        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Location permissions needed to start peers discovery.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    /**
     * RECYCLER VIEW CLASSES
     */
    internal inner class PeersRecyclerViewAdapter(private val peers: MutableList<Peer>) : RecyclerView.Adapter<PeersRecyclerViewAdapter.PeerViewHolder>() {

        override fun getItemCount(): Int {
            return peers.size
        }

        fun addPeer(peer: Peer) {
            val position = getPeerPosition(peer.uuid)
            if (position > -1) {
                peers[position] = peer
                notifyItemChanged(position)
            } else {
                peers.add(peer)
                notifyItemInserted(peers.size - 1)
            }
        }

        fun removePeer(lostPeer: Device?) {
            val position = getPeerPosition(lostPeer!!.userId)
            if (position > -1) {
                val peer = peers[position]
                peer.isNearby = false
                peers[position] = peer
                notifyItemChanged(position)
            }
        }

        private fun getPeerPosition(peerId: String): Int {
            for (i in peers.indices) {
                if (peers[i].uuid == peerId)
                    return i
            }
            return -1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.peer_row, parent, false)
            return PeerViewHolder(view)
        }

        override fun onBindViewHolder(peerHolder: PeerViewHolder, position: Int) {
            peerHolder.settingPeer(peers[position])
        }

        internal inner class PeerViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

            val mContentView: TextView
            lateinit var peer: Peer

            init {
                mContentView = view.findViewById<View>(R.id.peerName) as TextView
                view.setOnClickListener(this)
            }

            fun settingPeer(peer: Peer) {
                this.peer = peer

                when (peer.deviceType) {
                    Peer.DeviceType.ANDROID -> this.mContentView.text = peer.deviceName + " (android)"

                    Peer.DeviceType.IPHONE -> this.mContentView.text = peer.deviceName + " (iPhone)"
                }

                if (peer.isNearby) {
                    this.mContentView.setTextColor(Color.BLACK)
                } else {
                    this.mContentView.setTextColor(Color.GRAY)
                }
            }

            override fun onClick(v: View) {
                startActivity(Intent(baseContext, ChatActivity::class.java)
                        .putExtra(INTENT_EXTRA_NAME, peer.deviceName)
                        .putExtra(INTENT_EXTRA_UUID, peer.uuid))
            }
        }
    }

    companion object {

        internal val INTENT_EXTRA_NAME = "peerName"
        internal val INTENT_EXTRA_UUID = "peerUuid"
        internal val INTENT_EXTRA_TYPE = "deviceType"
        internal val INTENT_EXTRA_MSG = "message"
        internal val BROADCAST_CHAT = "Broadcast"
    }
}
