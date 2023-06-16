package com.example.base.repository.translate

import android.util.Xml
import com.example.base.model.Item
import com.example.base.network.NetworkService
import okhttp3.ResponseBody
import org.xmlpull.v1.XmlPullParser
import java.io.*

class NewsRepositoryImpl(private val networkService: NetworkService) : NewsRepository {
    override suspend fun getNews(): ResponseBody {
        return networkService.getNews()
    }

    override suspend fun convertStreamToString(inputStream: InputStream): String? {
        try {
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                val sb = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line).append('\n')
                }
                return sb.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun parseXmlData(xmlData: String): ArrayList<Item> {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(xmlData))

        var eventType = parser.eventType
        val itemList = ArrayList<Item>()

        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (tagName == "item") {
                        val item = parseItem(parser)
                        itemList.add(item)
                    }
                }
            }
            eventType = parser.next()
        }
        return itemList
    }

    // Hàm hỗ trợ để phân tích một mục (item)
    private fun parseItem(parser: XmlPullParser): Item {
        var eventType = parser.eventType
        var title = ""
        var description = ""
        var slash = ""
        var pubDate = ""
        var guid = ""
        var link = ""
        while (eventType != XmlPullParser.END_TAG || parser.name != "item") {
            val tagName = parser.name

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (tagName) {
                        "title" -> {
                            title = parser.nextText()
                        }
                        "description" -> {
                            description = parser.nextText()
                        }
                        "pubDate" -> {
                            pubDate = parser.nextText()
                        }
                        "link" -> {
                            link = parser.nextText()
                        }
                        "guid" -> {
                            guid = parser.nextText()
                        }
                        "slash:comments" -> {
                            slash = parser.nextText()
                        }
                    }
                }
            }
            eventType = parser.next()
        }
        return Item(
            title = title,
            description = description,
            link = link,
            pubDate = pubDate,
            guid = guid,
            slash = slash
        )
    }
}
