package com.sushi.fileUploader.listener

import java.io.File

interface ImageCallbackListener {
    fun imageCallback(file: File)
}