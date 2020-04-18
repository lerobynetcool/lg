package example.dpcg

//import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.html.Canvas
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js.annotation.JSExport

import typings.std
import typings.std.{WebGL2RenderingContext => WGL2}

import lg._

@JSExportTopLevel("dpcg_main")
object dpcgMain {
	//@JSExport
	def main(args: Array[String]): Unit = {

		document.addEventListener("DOMContentLoaded", (e: Event) => {
			val canvas = document.createElement("canvas").asInstanceOf[Canvas]
			document.body.appendChild(canvas)

			val LG = new LG(canvas)
			val p = LG.program("uniform float f;void main(){gl_Position=vec4(f);}", "void main(){}")

		})
	}
}
