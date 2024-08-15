package util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneId

object Datetime {

  def epochOf(mmddyyyy: String): Long = {
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val localDate = LocalDate.parse(mmddyyyy, formatter)
    val epochTime = localDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond
    epochTime
  }

}
