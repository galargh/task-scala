import org.scalatestplus.play._
import play.api.test._
import play.api.test.Helpers._
import implicits.ListMath._

class ImplicitsSpec extends PlaySpec {
  "List[Int]" should {
    val l1 = List()
    val l2 = List(1)
    val l3 = List(1, 2, 3)
    val l4 = List(4, 5, 6, 7)
    val l5 = List(312, 34, 23, 1312, 3222, 32, 231, 432, 5, 431, 5463, 3465, 645, 6, 54, 6, 5436, 54, 36, 8, 675, 86573, 6, 432, 1)

    "have average operation available" in {
      l1.average mustBe 0d
      l2.average mustBe 1d
      l3.average mustBe 2d
      l4.average mustBe 5.5
      l5.average mustBe 4355.76
    }

    "have median operation available" in {
      l1.median mustBe 0d
      l2.median mustBe 1d
      l3.median mustBe 2d
      l4.median mustBe 5.5
      l5.median mustBe 231d
    }
  }
}
