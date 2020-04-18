addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.0.1")

//resolvers += Resolver.bintrayRepo("oyvindberg", "ScalablyTyped")
//addSbtPlugin("org.scalablytyped" % "sbt-scalablytyped" % "202004090649")

resolvers += Resolver.bintrayRepo("oyvindberg", "converter")
addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta9+27-ac593fad")
