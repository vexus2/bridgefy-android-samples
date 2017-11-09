package com.bridgefy.samples.fileshare.rxbridgefy;

import com.bridgefy.sdk.client.Device;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * @author kekoyde on 9/28/17.
 */

public interface RxBridgefy {

    Observable<String> bridgefySendBroadcastMessage(HashMap<String, Object> message);

    Observable<String> bridgefySendDirectMessage(Device device, HashMap<String, Object> message);


}
