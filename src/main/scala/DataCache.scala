import util._

import scala.collection.mutable

final class DataCache {
  import DataCache._

  val terrain: mutable.HashMap[String, (Double, TerrainData)] = mutable.HashMap()
  val map: mutable.HashMap[String, (Double, MapData)] = mutable.HashMap()
  val rooms: mutable.HashMap[String, (Double, RoomData)] = mutable.HashMap()
  val focused: mutable.HashMap[String, (Double, FocusedRoomData)] = mutable.HashMap()

}

object DataCache {
  type TerrainData = Unit
  type MapData = Unit
  type RoomData = Unit
  type FocusedRoomData = Unit
}
