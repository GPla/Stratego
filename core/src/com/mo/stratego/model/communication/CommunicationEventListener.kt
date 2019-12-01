package com.mo.stratego.model.communication

// TODO desc
interface CommunicationEventListener {
    fun onDataReceived(data: ByteArray?)
    fun onStatusChange(status: String)
    fun onError(msg: String)
    fun onConnected(name: String)
    
}