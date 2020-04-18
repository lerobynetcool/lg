
import scala.scalajs.js.|
import org.scalajs.dom._
import typings.std
import typings.std.{WebGL2RenderingContext => WGL2}

package object lg {

	def vec2(x:Double,y:Double) = new vec2(x,y)
	def vec2(x:Double) = new vec2(x,x)
	def vec2(v:vec2) = new vec2(v.x,v.y)
	def vec2() = new vec2(0,0)

	def vec3(x:Double,y:Double,z:Double) = new vec3(x,y,z)
	def vec3(x:Double) = new vec3(x,x,x)
	def vec3(v:vec3) = new vec3(v.x,v.y,v.z)
	def vec3() = new vec3(0,0,0)

	def vec4(x:Double,y:Double,z:Double,w:Double) = new vec4(x,y,z,w)
	def vec4(x:Double) = new vec4(x,x,x,x)
	def vec4(v:vec4) = new vec4(v.x,v.y,v.z,v.w)
	def vec4() = new vec4(0,0,0,0)

	type ℝ = Double
	type ℤ = Int
	// type ℕ = UInt
	
	// pour rajouter une méthode disponible sur toutes les valeurs d'un certain type,
	// on peut utiliser une implicit class:
	implicit class notNullExtension[A](value: A | Null) {
		def nn: A = {
			assert(value != null)
			value.asInstanceOf[A]
		}
	}

	implicit class glExtension(gl: WGL2) {
		//def getShaderParameter(shader: WebGLShader, pname: typings.std.GLenum): scala.scalajs.js.Any

		/** Get a boolean shader parameter */
		def getBoolean(shader: webgl.Shader, pname: std.GLenum): Boolean =
			gl.getShaderParameter(shader, pname).asInstanceOf
		/** Get a boolean program parameter */
		def getBoolean(program: webgl.Program, pname: std.GLenum): Boolean =
			gl.getProgramParameter(program, pname).asInstanceOf

		/** Get an integer shader parameter */
		def getInt(shader: webgl.Shader, pname: std.GLenum): Int =
			gl.getShaderParameter(shader, pname).asInstanceOf
		/** Get an integer program parameter */
		def getInt(program: webgl.Program, pname: std.GLenum): Int =
			gl.getProgramParameter(program, pname).asInstanceOf
	}
}
