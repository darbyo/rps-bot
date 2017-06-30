object MostCommon {

  def predict(items: List[String]) = {
    if (items.filter(_ == "ROCK").size == 2) {
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