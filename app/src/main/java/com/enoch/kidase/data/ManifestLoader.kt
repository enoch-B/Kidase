package com.enoch.kidase.data

import android.content.Context
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

object ManifestLoader {

    fun loadManifest(context: Context): List<ClipEntry> {
        val entries = mutableListOf<ClipEntry>()
        try {
            val inputStream = context.assets.open("manifest.json")
            val jsonString = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                entries.add(
                    ClipEntry(
                        id = jsonObject.getString("id"),
                        displayText = jsonObject.getString("displayText"),
                        group = jsonObject.getString("group"),
                        audioFile = jsonObject.getString("audioFile")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return entries
    }

    fun groupClips(clips: List<ClipEntry>): Map<String, Map<Char, List<ClipEntry>>> {
        return clips.groupBy { it.group }
            .mapValues { (_, groupList) ->
                groupList.groupBy { it.displayText.firstOrNull() ?: ' ' }
            }
    }
}
