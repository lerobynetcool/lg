package lg

//import org.scalajs.dom
// import org.scalajs.dom.webgl
// import org.scalajs.dom.raw.{WebGLRenderingContext => rawgl}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.typedarray._
import scala.scalajs.js.|
import scala.scalajs.js
import org.scalajs.dom._
//import org.scalajs.dom.raw._
import scala.collection.mutable

import typings.std
import typings.std.{WebGL2RenderingContext => WGL2}
import typings.std.GLsizei

class tex2() {
	
}

class tex3() {
}

object TexGPU {
	private var availableTexUnit = 0
	def allocTexUnit(): Int = {
		val out = availableTexUnit
		this.availableTexUnit = this.availableTexUnit+1
		out
	}
	def freeTexUnit(texUnit: Int): Unit = {
		// TODO
	}
}

class TexGPU(val gl: WGL2) {

	val texUnit: Int = TexGPU.allocTexUnit()


	val tex: webgl.Texture = gl.createTexture().nn

	setBorder("clamp")
	setFilter("nearest")

	protected def use(exec:() => Unit): Unit = {
		val activeTextureBefore = gl.getParameter(WGL2.ACTIVE_TEXTURE).asInstanceOf[Int]
		gl.activeTexture(WGL2.TEXTURE0 + texUnit)
		gl.bindTexture(WGL2.TEXTURE_2D, tex)
		exec()
		gl.activeTexture(activeTextureBefore)
	}

	def uint8array(xs: Short*): Uint8Array =
		new Uint8Array(js.Array(xs: _*))
	
	@JSExport def setContentURL(url: String) = {
		use(() => {
			// temporary content before loading
			gl.texImage2D(WGL2.TEXTURE_2D, 0, WGL2.RGBA, 1, 1, 0, WGL2.RGBA, WGL2.UNSIGNED_BYTE, uint8array(0,0,0,0), 0)
		})
		// TODO : video case
		val data = document.createElement("Image").asInstanceOf[html.Image]
		data.src = url
		//data.addEventListener("load", (e: Event) => setContent(data))
		data.onload = (e: Event) => setContent(data)
		this
	}

	@JSExport def setContent(data: html.Image | html.Canvas | html.Video | ImageData | Float32Array) = {
		use(() => {
			val unpackFlipYbefore = gl.getParameter(WGL2.UNPACK_FLIP_Y_WEBGL).asInstanceOf[Int]
			gl.pixelStorei(WGL2.UNPACK_FLIP_Y_WEBGL, 1)
			// We need to use casts to std here because of https://github.com/ScalablyTyped/Converter/issues/146
			(data: Any) match {
				case data: html.Image  => gl.texImage2D(WGL2.TEXTURE_2D, 0, WGL2.RGBA, WGL2.RGBA, WGL2.UNSIGNED_BYTE, data.asInstanceOf[std.HTMLImageElement])
				case data: html.Canvas => gl.texImage2D(WGL2.TEXTURE_2D, 0, WGL2.RGBA, WGL2.RGBA, WGL2.UNSIGNED_BYTE, data.asInstanceOf[std.HTMLCanvasElement])
				case data: html.Video  => gl.texImage2D(WGL2.TEXTURE_2D, 0, WGL2.RGBA, WGL2.RGBA, WGL2.UNSIGNED_BYTE, data.asInstanceOf[std.HTMLVideoElement])
				case data: ImageData   => gl.texImage2D(WGL2.TEXTURE_2D, 0, WGL2.RGBA, WGL2.RGBA, WGL2.UNSIGNED_BYTE, data.asInstanceOf[std.ImageData])
				// case data: Float32Array      => gl.texImage2D(WGL2.TEXTURE_2D, 0, internalformat, width, height, 0, format, WGL2.FLOAT, data, 0)
				// case data:Float32Array      => gl.texImage3D(WGL2.TEXTURE_3D, 0, internalformat, width, height, depth, 0, format, WGL2.FLOAT, data, 0)
			}
			gl.generateMipmap(WGL2.TEXTURE_2D)
			gl.pixelStorei(WGL2.UNPACK_FLIP_Y_WEBGL, unpackFlipYbefore)
			window.dispatchEvent(new Event("renderplease"))
		})
		this
	}

	@JSExport def setBorder(border: String, direction: String = "xy") = {
		this.use(() => {
			val b = border.asInstanceOf[String] match {
				case "repeat" => WGL2.REPEAT
				case "mirror" => WGL2.MIRRORED_REPEAT
				case "clamp"  => WGL2.CLAMP_TO_EDGE
			}
			direction.asInstanceOf[String] match {
				case "x"  => gl.texParameteri(WGL2.TEXTURE_2D,WGL2.TEXTURE_WRAP_S,b)
				case "y"  => gl.texParameteri(WGL2.TEXTURE_2D,WGL2.TEXTURE_WRAP_T,b)
				case "xy" =>
					gl.texParameteri(WGL2.TEXTURE_2D,WGL2.TEXTURE_WRAP_S,b)
					gl.texParameteri(WGL2.TEXTURE_2D,WGL2.TEXTURE_WRAP_T,b)
			}
		})
		this
	}

	@JSExport def setFilter(filter: String, minmag: String = "both",mipmap: String = "") = {
		this.use(() => {
			val f = filter.asInstanceOf[String] match {
				case "linear" =>
					mipmap.asInstanceOf[String] match {
						case "linear"  => WGL2.LINEAR_MIPMAP_LINEAR
						case "nearest" => WGL2.LINEAR_MIPMAP_NEAREST
						case ""        => WGL2.LINEAR
					}
				case "nearest" =>
					mipmap.asInstanceOf[String] match {
						case "linear"  => WGL2.NEAREST_MIPMAP_LINEAR
						case "nearest" => WGL2.NEAREST_MIPMAP_NEAREST
						case ""        => WGL2.NEAREST
					}
			}
			minmag.asInstanceOf[String] match {
				case "mag"  => gl.texParameteri(WGL2.TEXTURE_2D, WGL2.TEXTURE_MIN_FILTER, f)
				case "min"  => gl.texParameteri(WGL2.TEXTURE_2D, WGL2.TEXTURE_MAG_FILTER, f)
				case "both" =>
					gl.texParameteri(WGL2.TEXTURE_2D, WGL2.TEXTURE_MAG_FILTER, f)
					gl.texParameteri(WGL2.TEXTURE_2D, WGL2.TEXTURE_MIN_FILTER, f)
			}
		})
		this
	}

}

// ---------------------------------------------------------------------------------------------

abstract class RenderLayer(gl: WGL2) extends TexGPU(gl) {
	def resize(width: GLsizei = gl.drawingBufferWidth, height: GLsizei = gl.drawingBufferHeight): this.type
}

abstract class RenderLayerCol(gl: WGL2) extends RenderLayer(gl) {
	def resize(width: GLsizei, height: GLsizei) = {
		use(() => {
			// https://webgl2fundamentals.org/webgl/lessons/webgl-data-textures.html
			// let internalformat
			// let format
			// switch (this.colorChannelCount) {
			// 	case 1: internalformat = this.gl.R32F; format = this.gl.RED; break
			// 	case 2: internalformat = this.gl.RG32F; format = this.gl.RG; break
			// 	case 3: internalformat = this.gl.RGB32F; format = this.gl.RGB; break // TODO : 3 doesn't work ?
			// 	case 4: internalformat = this.gl.RGBA32F; format = this.gl.RGBA; break
			// 	case 'depth': internalformat = this.gl.DEPTH_COMPONENT32F; format = this.gl.DEPTH_COMPONENT; break
			// }
			// gl.texImage2D(WGL2.TEXTURE_2D, 0, internalformat, width, height, 0, format, WGL2.FLOAT, null)
			gl.texImage2D(WGL2.TEXTURE_2D, 0, WGL2.RGBA32F, width, height, 0, WGL2.RGBA, WGL2.FLOAT, null, 0)
		})
		this
	}
	resize()
}

class RenderLayerDepth(gl: WGL2) extends RenderLayer(gl) {
	def resize(width: GLsizei, height: GLsizei) = {
		use(() => {
			gl.texImage2D(WGL2.TEXTURE_2D, 0, WGL2.DEPTH_COMPONENT32F, width, height, 0, WGL2.DEPTH_COMPONENT, WGL2.FLOAT, null, 0)
		})
		this
	}
	resize()
}

abstract class AbstractFrameBuffer(val gl: webgl.RenderingContext) {

// 	protected gl: WebGL2RenderingContext
// 	protected gBuffer: WebGLFramebuffer | null = null

// 	use(exec:() => void) {
// 		let gBufferBefore = this.gl.getParameter(this.gl.FRAMEBUFFER_BINDING)
// 		this.gl.bindFramebuffer(this.gl.FRAMEBUFFER, this.gBuffer)
// 		exec()
// 		this.gl.bindFramebuffer(this.gl.FRAMEBUFFER, gBufferBefore)
// 		this.gl.bindFramebuffer(this.gl.FRAMEBUFFER, null)
// 	}

// 	private clearHelper(r: number, g: number, b: number, a: number, toclear: number) {
// 		this.use(() => {
// 			this.gl.clearColor(r, g, b, a)
// 			this.gl.clear(toclear)
// 		})
// 		return this
// 	}

// 	clear(r = 0, g = 0, b = 0, a = 0) {
// 		return this.clearHelper(r, g, b, a, this.gl.COLOR_BUFFER_BIT | this.gl.DEPTH_BUFFER_BIT)
// 	}

// 	clearDepth(r = 0, g = 0, b = 0, a = 0) {
// 		return this.clearHelper(r, g, b, a, this.gl.DEPTH_BUFFER_BIT)
// 	}

// 	clearColor(r = 0, g = 0, b = 0, a = 0) {
// 		return this.clearHelper(r, g, b, a, this.gl.COLOR_BUFFER_BIT)
// 	}

}

class FrameBufferScreen(gl: webgl.RenderingContext) extends AbstractFrameBuffer(gl) {}

class FrameBuffer(gl: webgl.RenderingContext, var layerCount: Int) extends AbstractFrameBuffer(gl) {
// 	protected texlayers: RenderLayerColor[] = []
// 	protected depth: RenderLayerDepth

// 	constructor(gl: WebGL2RenderingContext, layerCount: number) {
// 		super(gl)
// 		this.gBuffer = gl.createFramebuffer()

// 		this.setDepth(new RenderLayerDepth(gl))
// 		for (let i = 0; i < layerCount; i++) {
// 			this.setLayer(new RenderLayerColor(gl, 4),i)
// 		}
// 	}

// 	getLayer(layerIndex: number) {
// 		return this.texlayers[layerIndex]
// 	}

// 	getDepth() {
// 		return this.depth
// 	}

// 	resize(width?: number, height?: number) {
// 		for( let layer of this.texlayers ) {
// 			layer.resize(width,height)
// 		}
// 		this.depth.resize(width, height)
// 	}

// 	private setLayer(texture: RenderLayerColor, index: number) {
// 		this.use(() => {
// 			this.texlayers[index] = texture
// 			this.gl.framebufferTexture2D(this.gl.FRAMEBUFFER, this.gl.COLOR_ATTACHMENT0 + index, this.gl.TEXTURE_2D, texture.tex, 0)
// 			this.gl.drawBuffers(this.texlayers.map((_, k) => this.gl.COLOR_ATTACHMENT0 + k))
// 		})
// 	}

// 	private setDepth(texture: RenderLayerDepth) {
// 		this.use(() => {
// 			this.depth = texture
// 			this.gl.framebufferTexture2D(this.gl.FRAMEBUFFER, this.gl.DEPTH_ATTACHMENT, this.gl.TEXTURE_2D, texture.tex, 0)
// 		})
// 	}

}
