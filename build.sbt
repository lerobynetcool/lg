enablePlugins(ScalaJSPlugin)
enablePlugins(ScalablyTypedConverterPlugin)

name := "LG"
scalaVersion := "2.13.1"

scalaJSUseMainModuleInitializer := true
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.0.0"

//libraryDependencies += ScalablyTyped.W.webgl2

Compile / npmDependencies ++= Seq(
  "@types/webgl2" -> "0.0.5"
)

scalacOptions ++= Seq(
  "-deprecation"
)

scalaJSStage in Global := FullOptStage
