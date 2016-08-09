package implicits

import scala.language.implicitConversions

/**
 * This object extends the functionality of a List of Int
 * in order to allow for median and average calculation.
 */
object ListMath {
  implicit class RichListInt(val list: List[Int]) {
    // defaults to 0
    // average of two middle elements if the length is even
    def median(): Double = {
      val sorted = list.sorted
      list.length match {
        case 0 => 0
        case n if n % 2 == 0 => (sorted(n / 2 - 1) + sorted(n / 2)) / 2d
        case n => sorted(n / 2)
      }
    }

    // defaults to 0
    def average(): Double = {
      list.length match {
        case 0 => 0
        case _ => list.sum.toDouble / list.length
      }
    }
  }
}
