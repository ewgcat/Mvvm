package com.lishuaihua.imageloader.request

import com.lishuaihua.imageloader.ImageLoader

/**
 * Exception thrown when an [ImageRequest] with empty/null data is executed by an [ImageLoader].
 */
class NullRequestDataException : RuntimeException("The request's data is null.")
