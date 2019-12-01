package com.mo.stratego.model.communication

//todo desc

class DataReceivedEvent(val data: ByteArray?)

class DataWriteEvent()

class OnConnectingEvent()

class OnErrorEvent(val msg: String)

class OnConnectedEvent(val name: String)
