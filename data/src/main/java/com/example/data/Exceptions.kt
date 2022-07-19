package com.example.data


open class AppException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class UnknownException() : AppException()
