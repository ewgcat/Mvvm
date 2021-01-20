@file:Suppress("MemberVisibilityCanBePrivate")

package com.lishuaihua.imageloader.network

import com.lishuaihua.imageloader.fetch.HttpFetcher
import okhttp3.Response

/**
 * Exception for an unexpected, non-2xx HTTP response.
 *
 * @see HttpFetcher
 */
class HttpException(val response: Response) : RuntimeException("HTTP ${response.code}: ${response.message}")
