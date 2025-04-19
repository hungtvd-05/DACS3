package com.app_computer_ecom.dack.viewmodel

import android.net.Uri
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope

class UriListSaver : Saver<List<Uri>, List<String>> {
    override fun restore(value: List<String>): List<Uri> =
        value.mapNotNull { uriString -> Uri.parse(uriString) }

    override fun SaverScope.save(value: List<Uri>): List<String> =
        value.map { it.toString() }
}