enablePlugins(ScalaJSPlugin)
enablePlugins(ScalablyTypedConverterPlugin)

name := "LG"
scalaVersion := "2.13.1"

scalaJSUseMainModuleInitializer := true
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.0.0"

Compile / npmDependencies ++= Seq(
  // Add typescript bindings here if needed
)

scalacOptions ++= Seq(
  "-deprecation"
)

scalaJSStage in Global := FullOptStage
