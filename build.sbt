name := "Villainous"

version := "0.1"

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
  "com.beachape"  %% "enumeratum"       % "1.7.4",
  "com.beachape"  %% "enumeratum-circe" % "1.7.4",
  "io.circe"      %% "circe-core"       % "0.14.9",
  "io.circe"      %% "circe-generic"    % "0.14.9",
  "io.circe"      %% "circe-parser"     % "0.14.9"
)
