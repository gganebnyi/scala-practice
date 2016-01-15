import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import scala.xml.{Utility, XML}
import org.json4s.Xml.{toJson, toXml}
import org.json4s._
import org.json4s.jackson.JsonMethods._

object ZillowApacheHttpRestClient {
  def main(args: Array[String]) {
    // get the content from Zillow api url
    val content = getRestContent("http://www.zillow.com/webservice/GetUpdatedPropertyDetails.htm?zws-id=X1-ZWz1f47s45rtvv_1sm5d&zpid=", "48749425")

    // convert it to xml
    val xml = XML.loadString(content)

    // print the xml
    (xml \ "response").foreach { updates =>
      println("zpid:" + (updates \ "zpid").text)
      val pageViewCount = (updates \ "pageViewCount").foreach { pageView =>
        println("pageViewCount:" + (updates \ "pageViewCount").text)
        println("\tcurrentMonth:" + (pageView \ "currentMonth").text)
        println("\ttotal:" + (pageView \ "total").text)
      }
      val address = (updates \ "address").foreach { addr =>
        println("address:" + (addr \ "address").text)
        println("\tstreet:" + (addr \ "street").text)
        println("\tzipcode:" + (addr \ "zipcode").text)
        println("\tcity:" + (addr \ "city").text)
        println("\tstate:" + (addr \ "state").text)
        println("\tlatitude:" + (addr \ "latitude").text)
        println("\tlongitude:" + (addr \ "longitude").text)
      }
      val links = (updates \ "links").foreach { link =>
        println("links:" + (link \ "links").text)
        println("\thomeDetails:" + (link \ "homeDetails").text)
        println("\tphotoGallery:" + (link \ "photoGallery").text)
        println("\thomeInfo:" + (link \ "homeInfo").text)
      }
      val images = (updates \ "images").foreach { image =>
        println("images:" + (image \ "images").text)
        println("\tcount:" + (image \ "count").text)
        println("\timage:" + (image \ "image" \ "url").text)
      }
      val editedfacts = (updates \ "editedFacts").foreach { facts =>
        println("editedFacts:" + (facts \ "editedFacts").text)
        println("\tuseCode:" + (facts \ "useCode").text)
        println("\tbedrooms:" + (facts \ "bedrooms").text)
        println("\tbathrooms:" + (facts \ "bathrooms").text)
        println("\tfinishedSqFt:" + (facts \ "finishedSqFt").text)
        println("\tlotSizeSqFt:" + (facts \ "lotSizeSqFt").text)
        println("\tyearBuilt:" + (facts \ "yearBuilt").text)
        println("\tyearUpdated:" + (facts \ "yearUpdated").text)
        println("\tnumFloors:" + (facts \ "numFloors").text)
        println("\tbasement:" + (facts \ "basement").text)
        println("\troof:" + (facts \ "roof").text)
        println("\tview:" + (facts \ "view").text)
        println("\tparkingType:" + (facts \ "parkingType").text)
        println("\theatingSources:" + (facts \ "heatingSources").text)
        println("\theatingSystem:" + (facts \ "heatingSystem").text)
        println("\trooms:" + (facts \ "rooms").text)
      }
      println("neighborhood:" + (updates \ "neighborhood").text)
      println("schoolDistrict:" + (updates\ "schoolDistrict").text)
      println("elementarySchool:" + (updates \ "elementarySchool").text)
      println("middleSchool:" + (updates \ "middleSchool").text)

    }

    val json = toJson(xml)
    println("-------------JSON-----------------")
    println(compact(render(json \\ "images")))


  }

  // resutrn the text content from REST URL
  def getRestContent(url: String, zpid: String): String = {
    val httpClient = new DefaultHttpClient()
    val httpResponse = httpClient.execute(new HttpGet(url+zpid))
    val entity = httpResponse.getEntity
    var content = ""

    if (entity != null) {
      val inputStream = entity.getContent
      content = io.Source.fromInputStream(inputStream).getLines.mkString
      inputStream.close
    }
    httpClient.getConnectionManager.shutdown
    content
  }

}
