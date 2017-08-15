

val argonautVersion = "6.2"
val monocleVersion = "1.4.0"
val lwjglVersion = "3.1.2"
val circeVersion = "0.8.0"

lazy val sharedSettings: Seq[Def.Setting[_]] = Seq(

  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.3",
    "org.scalatest" %% "scalatest" % "3.0.3" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.5"
  ),

  libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2",

  libraryDependencies ++= Seq(
    "com.github.julien-truffaut" %% "monocle-core" % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-generic" % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-state" % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-refined" % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-law" % monocleVersion % "test",
    "com.github.kenbot" %% "goggles-dsl" % "1.0"
  ),

  libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.14",
  libraryDependencies += "com.lihaoyi" %% "upickle" % "0.4.4",
  libraryDependencies += "org.scala-graph" %% "graph-core" % "1.11.5",
  libraryDependencies += "org.scala-graph" %% "graph-constrained" % "1.11.0",
  libraryDependencies += "io.reactivex" %% "rxscala" % "0.26.5",

  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion),

  libraryDependencies += scalaOrganization.value % "scala-reflect" % scalaVersion.value % "provided",

  libraryDependencies += "org.scalamacros" % "paradise_2.12.3" % "2.1.1"
)

lazy val lwjglSettings: Seq[Def.Setting[_]] = Seq(
  libraryDependencies ++= Seq(
    "org.lwjgl" % "lwjgl" % lwjglVersion,
    "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
    "org.lwjgl" % "lwjgl-jemalloc" % lwjglVersion,
    "org.lwjgl" % "lwjgl-openal" % lwjglVersion,
    "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,
    "org.lwjgl" % "lwjgl-stb" % lwjglVersion,
    "org.lwjgl" % "lwjgl-vulkan" % lwjglVersion,
    "org.lwjgl" % "lwjgl" % lwjglVersion % "runtime" classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.lwjgl" % "lwjgl-glfw" % lwjglVersion % "runtime" classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.lwjgl" % "lwjgl-jemalloc" % lwjglVersion % "runtime" classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.lwjgl" % "lwjgl-openal" % lwjglVersion % "runtime" classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.lwjgl" % "lwjgl-opengl" % lwjglVersion % "runtime" classifier "natives-windows" classifier "natives-linux" classifier "natives-macos",
    "org.lwjgl" % "lwjgl-stb" % lwjglVersion % "runtime" classifier "natives-windows" classifier "natives-linux" classifier "natives-macos"
  )
)

lazy val root = (project in file("."))
  .aggregate(networking)
  .dependsOn(networking)
  .settings(
    inThisBuild(Seq(
      scalaOrganization := "org.typelevel",
      scalaVersion := "2.12.3-bin-typelevel-4",
      organization := "Screepers",
      version := "1.0",
      resolvers += Resolver.sonatypeRepo("releases"),
      resolvers += Resolver.sonatypeRepo("snapshots"),
      scalacOptions ++= Seq(
        "-Yinduction-heuristics",
        "-Ykind-polymorphism",
        "-Yliteral-types",
        "-Xstrict-patmat-analysis",
        "-Xexperimental"
      ),
      addCompilerPlugin("org.scalamacros" % "paradise_2.12.3" % "2.1.1")
    )),
    name := "Silica"
  )
  .settings(sharedSettings: _*)
  .settings(lwjglSettings: _*)

lazy val networking = (project in file("./Networking"))
  .settings(
    name := "Silica.Networking"
  )
  .settings(sharedSettings: _*)

