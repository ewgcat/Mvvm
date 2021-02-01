package com.lishuaihua.net.support

import java.util.ArrayList
import java.util.concurrent.ConcurrentHashMap

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


class CookieJarImpl : CookieJar {
    //cookie存储
    private val cookieStore = ConcurrentHashMap<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = cookieStore[url.host]
        return cookies ?: ArrayList()
    }
}
