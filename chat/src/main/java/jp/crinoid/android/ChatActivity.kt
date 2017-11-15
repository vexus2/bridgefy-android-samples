package jp.crinoid.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bridgefy.sdk.client.BFEngineProfile
import com.bridgefy.sdk.client.Bridgefy
import jp.crinoid.android.MainActivity.Companion.BROADCAST_CHAT
import jp.crinoid.android.MainActivity.Companion.INTENT_EXTRA_NAME
import jp.crinoid.android.MainActivity.Companion.INTENT_EXTRA_UUID
import jp.crinoid.android.entities.Message
import jp.crinoid.android.entities.Peer
import java.util.*


class ChatActivity : AppCompatActivity() {

    private var conversationName: String? = null
    private var conversationId: String? = null


    @BindView(R.id.txtMessage)
    internal var txtMessage: EditText? = null

    internal var messagesAdapter = MessagesRecyclerViewAdapter(ArrayList())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        ButterKnife.bind(this)

        // recover our Peer object
        conversationName = intent.getStringExtra(INTENT_EXTRA_NAME)
        conversationId = intent.getStringExtra(INTENT_EXTRA_UUID)

        // Configure the Toolbar
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Enable the Up button
        val ab = supportActionBar
        if (ab != null) {
            ab.title = conversationName
            ab.setDisplayHomeAsUpEnabled(true)
        }

        // register the receiver to listen for incoming messages
        LocalBroadcastManager.getInstance(baseContext)
                .registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        val message = Message(intent.getStringExtra(MainActivity.INTENT_EXTRA_MSG))
                        message.deviceName = intent.getStringExtra(MainActivity.INTENT_EXTRA_NAME)
                        message.direction = Message.INCOMING_MESSAGE
                        messagesAdapter.addMessage(message)
                    }
                }, IntentFilter(conversationId))

        // configure the recyclerview
        val messagesRecyclerView = findViewById<View>(R.id.message_list) as RecyclerView
        val mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.reverseLayout = true
        messagesRecyclerView.layoutManager = mLinearLayoutManager
        messagesRecyclerView.adapter = messagesAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


    @OnClick(R.id.btnSend)
    fun onMessageSend(v: View) {
        // get the message and push it to the views
        val messageString = txtMessage!!.text.toString()
        if (messageString.trim { it <= ' ' }.length > 0) {
            // update the views
            txtMessage!!.setText("")
            val message = Message(messageString)
            message.direction = Message.OUTGOING_MESSAGE
            messagesAdapter.addMessage(message)

            // create a HashMap object to send
            val content = HashMap<String, Any>()
            content.put("text", messageString)

            // send message text to device
            if (conversationId == BROADCAST_CHAT) {
                // we put extra information in broadcast packets since they won't be bound to a session
                content.put("device_name", Build.MANUFACTURER + " " + Build.MODEL)
                content.put("device_type", Peer.DeviceType.ANDROID.ordinal)
                Bridgefy.sendBroadcastMessage(
                        Bridgefy.createMessage(content),
                        BFEngineProfile.BFConfigProfileLongReach)
            } else {
                Bridgefy.sendMessage(
                        Bridgefy.createMessage(conversationId, content),
                        BFEngineProfile.BFConfigProfileLongReach)
            }
        }
    }


    /**
     * RECYCLER VIEW CLASSES
     */
    internal inner class MessagesRecyclerViewAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<MessagesRecyclerViewAdapter.MessageViewHolder>() {

        override fun getItemCount(): Int {
            return messages.size
        }

        fun addMessage(message: Message) {
            messages.add(0, message)
            notifyDataSetChanged()
        }

        override fun getItemViewType(position: Int): Int {
            return messages[position].direction
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageViewHolder {
            var messageView: View? = null

            when (viewType) {
                Message.INCOMING_MESSAGE -> messageView = LayoutInflater.from(viewGroup.context).inflate(R.layout.message_row_incoming, viewGroup, false)
                Message.OUTGOING_MESSAGE -> messageView = LayoutInflater.from(viewGroup.context).inflate(R.layout.message_row_outgoing, viewGroup, false)
            }

            return MessageViewHolder(messageView!!)
        }

        override fun onBindViewHolder(messageHolder: MessageViewHolder, position: Int) {
            messageHolder.settingMessage(messages[position])
        }

        internal inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txtMessage: TextView
            lateinit var message: Message

            init {
                txtMessage = view.findViewById<View>(R.id.txtMessage) as TextView
            }

            fun settingMessage(message: Message) {
                this.message = message

                if (message.direction == Message.INCOMING_MESSAGE && conversationId == BROADCAST_CHAT) {
                    this.txtMessage.text = message.deviceName + ":\n" + message.text
                } else {
                    this.txtMessage.text = message.text
                }
            }
        }
    }
}
