package com.bridgefy.samples.fileshare.rxbridgefy;

import com.bridgefy.sdk.client.Bridgefy;
import com.bridgefy.sdk.client.Device;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * @author kekoyde on 9/28/17.
 */

public class RxMessage implements RxBridgefy {


    @Override
    public Observable<String> bridgefySendBroadcastMessage(HashMap<String, Object> message) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext(Bridgefy.sendBroadcastMessage(message));
                e.onComplete();
            }
        });
    }

    @Override
    public Observable<String> bridgefySendDirectMessage(final Device device, final HashMap<String, Object> message) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception
            {
                e.onNext(device.sendMessage(message));
                e.onComplete();
            }
        });
    }


}
