package ds.lab

import scala.io.Source
import collection.JavaConversions._

object WordUtils {
  def parseFile(path: String): java.util.Map[java.lang.String, java.lang.Integer] = {
    val text = Source.fromFile(path)

    val words = text.getLines().mkString(" ").replaceAll("\\p{Punct}|[—…«»„“'\\t\\n\\r]|[0-9]", "")
      .replaceAll(" +", " ")
      .split(" ")
      .map(_.toLowerCase)
      .foldLeft(Map.empty[String, Int]){(map, word) => map + (word -> (map.getOrElse(word, 0) + 1))}

    mapAsJavaMap(words).asInstanceOf[java.util.Map[java.lang.String, java.lang.Integer]]
  }

}
