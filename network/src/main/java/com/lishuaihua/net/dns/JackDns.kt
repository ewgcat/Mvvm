package com.lishuaihua.net.dns

import okhttp3.Dns
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*

class JackDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        val inetAddresseses: MutableList<InetAddress> = ArrayList()
        var hostNameInetAddresses: List<InetAddress>? = null
        try {
            hostNameInetAddresses = Dns.SYSTEM.lookup(hostname)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        if (hostNameInetAddresses != null && hostNameInetAddresses.size > 0) {
            inetAddresseses.addAll(hostNameInetAddresses)
        }
        //从服务器获取后保存在本地，然后本地读取
        try {
            inetAddresseses.add(InetAddress.getByName("113.96.22.215"))
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        return inetAddresseses
    }
}