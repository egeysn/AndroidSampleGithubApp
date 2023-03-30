package com.egeysn.githubapp.data.local

import androidx.room.ProvidedTypeConverter
import com.egeysn.githubapp.data.utils.JsonParser

@ProvidedTypeConverter
class DatabaseConverters(private val jsonParser: JsonParser)
