package com.lishuaihua.servicemanager

import android.os.*
import androidx.annotation.RequiresApi
import com.lishuaihua.servicemanager.MethodRouter.routerToInstance

class ProcessBinder(private val DESCRIPTOR: String) : Binder() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Throws(RemoteException::class)
    public override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        when (code) {
            INTERFACE_TRANSACTION -> {
                reply!!.writeString(DESCRIPTOR)
                return true
            }
            FIRST_CODE -> {
                data.enforceInterface(DESCRIPTOR)
                val param: Bundle?
                param = if (0 != data.readInt()) {
                    Bundle.CREATOR.createFromParcel(data)
                } else {
                    null
                }
                val result = routerToInstance(param!!)
                reply!!.writeNoException()
                if (result != null) {
                    reply.writeInt(1)
                    result.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE)
                } else {
                    reply.writeInt(0)
                }
                return true
            }
        }
        return super.onTransact(code, data, reply, flags)
    }

    companion object {
        const val FIRST_CODE = FIRST_CALL_TRANSACTION + 0
    }

    init {
        attachInterface(null, DESCRIPTOR)
    }
}