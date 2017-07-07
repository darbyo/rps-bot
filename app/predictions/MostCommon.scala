package predictions

object MostCommon {

  def predict(items: List[String]) = {
    if (items.filter(_ == "PAPER").size >= items.size / 2f) {
      "SCISSORS"
    } else if(items.filter(_ == "SCISSORS").size >= items.size / 2f) {
      "ROCK"
    } else if (items.filter(_ == "ROCK").size > 1) {
      "PAPER"
    } else {
      items.reverse.head match {
        case "PAPER" => "SCISSORS"
        case "ROCK" => "PAPER"
        case "SCISSORS" => "ROCK"
      }
    }
  }
}
