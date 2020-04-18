package lg

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom._
//import org.scalajs.dom.document
import org.scalajs.dom.html.Canvas
import typings.std
import typings.std.{WebGL2RenderingContext => WGL2}

object LG {
	def generateWebglContext(canvas: Canvas = document.createElement("canvas").asInstanceOf ) = {
		val gl: WGL2 = canvas.getContext("webgl2").asInstanceOf
		gl
	}
}

@JSExportTopLevel("LG")
class LG( val gl: WGL2 = LG.generateWebglContext() ) {
	def this(canvas: Canvas) = this(LG.generateWebglContext(canvas))
	@JSExport def program(vshaderSrc: String, fshaderSrc: String): Program = new Program(gl, vshaderSrc, fshaderSrc)
	@JSExport def texture(): TexGPU = new TexGPU(this.gl)
}
