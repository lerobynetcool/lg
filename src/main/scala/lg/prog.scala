package lg

import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.|
import org.scalajs.dom._
// import org.scalajs.dom.raw.{WebGLRenderingContext => rawgl}
import typings.std
import typings.std.{WebGL2RenderingContext => WGL2}

class Program(val gl: WGL2, vshaderSrc: String, fshaderSrc: String) {

	private val program = this.compileProgram(vshaderSrc, fshaderSrc)

	private def use(exec:() => Unit): Unit = {
		gl.useProgram(this.program)
		exec()
		gl.useProgram(null)
	}

	private var uniformType = scala.collection.mutable.HashMap.empty[String, std.GLenum]


	use(() =>{
		(0 to this.gl.getProgramParameter(this.program, WGL2.ACTIVE_UNIFORMS).asInstanceOf[Int]-1).map( i => {
			val info: webgl.ActiveInfo = gl.getActiveUniform(this.program, i).nn
			this.uniformType(info.name) = info.`type`
		})
	})
	
	// @JSExport
	// draw(uniforms: Uniforms = {}, drawingProcedure: () => Unit) {
	// 	this.setUniform("time", getTime())
	// 	this.setUniform("resolution", [this.gl.canvas.width, this.gl.canvas.height])
	// 	this.setUniform("aspect", this.gl.canvas.width / this.gl.canvas.height)
	// 	this.setUniforms(uniforms)
	// 	this.use(() => {
	// 		drawingProcedure()
	// 	})
	// }

	// private def setUniform( varName: String, value: ℝ|Vec2|Vec3|Vec4|Texture ) {
	private def setUniform( varName: String, value: ℤ|ℝ|vec2|vec3|vec4|mat2|mat3|mat4|tex2|tex3 ): Unit = {
		def uniformLocation: webgl.UniformLocation =
			gl.getUniformLocation(program, varName).nn

			// pourquoi def et pas juste une val ? euh on peut aussi si on est sur que c'est jamais null. Si on l'appelle qu'une seule fois ça fait pas de diférence

			val transpose = false
		if( !this.uniformType.contains(varName)) {
			// console.log("ERROR : setUniform : cannot set " + varName + ", it does not exit or is unused")
			return
		}
		println(value)
		this.use(() => {
			(value: Any) match {
				case v: ℤ => 
					this.uniformType(varName) match {
						case WGL2.INT          => gl.uniform1i(uniformLocation, v)
						// case WGL2.UNSIGNED_INT => gl.uniform1ui(this.gl.getUniformLocation(this.program, varName), v)
						// case WGL2.BOOL         => gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value)
						// case WGL2.SAMPLER_2D => this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value)
					}
				case v: ℝ => 
					this.uniformType(varName) match {
						case WGL2.FLOAT        => gl.uniform1f(uniformLocation, v)
						// TODO
						// case WGL2.SAMPLER_3D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value); break
						// case WGL2.SAMPLER_CUBE: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value); break
						// case WGL2.INT_SAMPLER_2D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value); break
						// case WGL2.INT_SAMPLER_3D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value); break
						// case WGL2.INT_SAMPLER_CUBE: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value); break
						// case WGL2.UNSIGNED_INT_SAMPLER_2D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value); break
						// case WGL2.UNSIGNED_INT_SAMPLER_3D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value); break
						// case WGL2.UNSIGNED_INT_SAMPLER_CUBE: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value); break
						// case _ => println(s"ERROR : setUniform : cannot set $varName to $value because it is of type " + this.uniformType(varName))
					}

				case v:vec2 =>
					this.uniformType(varName) match {
						case WGL2.FLOAT_VEC2 => gl.uniform2f(uniformLocation,v.x,v.y)
					}
				case v:vec3 =>
					this.uniformType(varName) match {
						case WGL2.FLOAT_VEC3 => gl.uniform3f(uniformLocation,v.x,v.y,v.z)
					}
				case v:vec4 =>
					this.uniformType(varName) match {
						case WGL2.FLOAT_VEC4 => gl.uniform4f(uniformLocation,v.x,v.y,v.z,v.w)
					}

				case v:mat2 =>
					this.uniformType(varName) match {
						case _ =>
						// case rawgl.FLOAT_MAT2 => gl.uniformMatrix2fv(this.gl.getUniformLocation(this.program, varName), transpose, value)
					}
				case v:mat3 =>
					this.uniformType(varName) match {
						case _ =>
						// case rawgl.FLOAT_MAT3 => gl.uniformMatrix3fv(this.gl.getUniformLocation(this.program, varName), transpose, value)
					}
				case v:mat4 =>
					this.uniformType(varName) match {
						case _ =>
						// case rawgl.FLOAT_MAT4 => gl.uniformMatrix4fv(this.gl.getUniformLocation(this.program, varName), transpose, value)
					}

					// case v:Texture => 
				// switch (this.uniformType[varName]) {
				// 	case this.gl.SAMPLER_2D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value.texUnit); break
				// 	// TODO
				// 	// case this.gl.SAMPLER_3D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value.texUnit); break
				// 	// case this.gl.SAMPLER_CUBE: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value.texUnit); break
				// 	// case this.gl.INT_SAMPLER_2D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value.texUnit); break
				// 	// case this.gl.INT_SAMPLER_3D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value.texUnit); break
				// 	// case this.gl.INT_SAMPLER_CUBE: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value.texUnit); break
				// 	// case this.gl.UNSIGNED_INT_SAMPLER_2D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value.texUnit); break
				// 	// case this.gl.UNSIGNED_INT_SAMPLER_3D: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value.texUnit); break
				// 	// case this.gl.UNSIGNED_INT_SAMPLER_CUBE: this.gl.uniform1i(this.gl.getUniformLocation(this.program, varName), value.texUnit); break
				// 	default: console.log("ERROR : setUniform : cannot set " + varName + " to " + value + " because it is of type " + this.uniformType[varName])
				// }
			}
// 					switch (this.uniformType[varName]) {
						
// 						case this.gl.INT: if (value.length !== 1) { break } this.gl.uniform1iv(this.gl.getUniformLocation(this.program, varName), value); break
// 						case this.gl.INT_VEC2: if (value.length !== 2) { break } this.gl.uniform2iv(this.gl.getUniformLocation(this.program, varName), value); break
// 						case this.gl.INT_VEC3: if (value.length !== 3) { break } this.gl.uniform3iv(this.gl.getUniformLocation(this.program, varName), value); break
// 						case this.gl.INT_VEC4: if (value.length !== 4) { break } this.gl.uniform4iv(this.gl.getUniformLocation(this.program, varName), value); break
						
// 						case this.gl.UNSIGNED_INT: if (value.length !== 1) { break } this.gl.uniform1uiv(this.gl.getUniformLocation(this.program, varName), value); break
// 						case this.gl.UNSIGNED_INT_VEC2: if (value.length !== 2) { break } this.gl.uniform2uiv(this.gl.getUniformLocation(this.program, varName), value); break
// 						case this.gl.UNSIGNED_INT_VEC3: if (value.length !== 3) { break } this.gl.uniform3uiv(this.gl.getUniformLocation(this.program, varName), value); break
// 						case this.gl.UNSIGNED_INT_VEC4: if (value.length !== 4) { break } this.gl.uniform4uiv(this.gl.getUniformLocation(this.program, varName), value); break
						
// 						case this.gl.BOOL: if (value.length !== 1) { break } this.gl.uniform1iv(this.gl.getUniformLocation(this.program, varName), value); break
// 						case this.gl.BOOL_VEC2: if (value.length !== 2) { break } this.gl.uniform2iv(this.gl.getUniformLocation(this.program, varName), value); break
// 						case this.gl.BOOL_VEC3: if (value.length !== 3) { break } this.gl.uniform3iv(this.gl.getUniformLocation(this.program, varName), value); break
// 						case this.gl.BOOL_VEC4: if (value.length !== 4) { break } this.gl.uniform4iv(this.gl.getUniformLocation(this.program, varName), value); break
						
// 						// TODO
// 						// this.gl.FLOAT_MAT2x3
// 						// this.gl.FLOAT_MAT2x4
// 						// this.gl.FLOAT_MAT3x2
// 						// this.gl.FLOAT_MAT3x4
// 						// this.gl.FLOAT_MAT4x2
// 						// this.gl.FLOAT_MAT4x3

// 						// this.gl.SAMPLER_2D_SHADOW
// 						// this.gl.SAMPLER_CUBE_SHADOW

// 						// this.gl.SAMPLER_2D_ARRAY
// 						// this.gl.SAMPLER_2D_ARRAY_SHADOW
// 						// this.gl.INT_SAMPLER_2D_ARRAY
// 						// this.gl.UNSIGNED_INT_SAMPLER_2D_ARRAY
// 						default: console.log("ERROR : setUniform : cannot set " + varName + " to " + value + " because it is of type " + this.uniformType[varName])
// 					}
// 				}
		})
	}

// 	private setUniforms(uniforms: Uniforms) {
// 		for(let name in uniforms) {
// 			this.setUniform(name,uniforms[name][1])
// 		}
// 	}

// /* 	setAttrib(varName: string, data: number[] | number[][], data_dimension: number) {
// 	// 	let attributeLocation = gl.getAttribLocation(this.program, varName)
// 	// 	gl.bindBuffer(gl.ARRAY_BUFFER, gl.createBuffer())
// 	// 	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(data), gl.STATIC_DRAW)
// 	// 	gl.enableVertexAttribArray(attributeLocation)

// 	// 	// TODO : implement indexed buffer
// 	// 	// TODO : use multiple time when possible
// 	// 	// gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, bufferInfo.indices)
// 	// 	// gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(arrays.indices), gl.STATIC_DRAW)

// 	// 	let type = gl.FLOAT
// 	// 	let stride = 0        // TODO : look into this for perf improvement
// 	// 	let offset = 0        // TODO : look into this for perf improvement
// 	// 	gl.vertexAttribPointer(attributeLocation, data_dimension, type, false, stride, offset)
// 	} */

// 	// =========================

	private def compileShader(src: String, shaderType: std.GLenum): webgl.Shader = {
		val shader: webgl.Shader = gl.createShader(shaderType).nn
		val head = """#version 300 es
precision highp float;
uniform float time;
uniform vec2 resolution;
uniform float aspect;
const float pi = 3.141592653589793;
"""
		val toCompile = head + src
		// val toCompile = head + Gatalog.getGLSL() + src // TODO

		gl.shaderSource(shader,toCompile)
		gl.compileShader(shader)
		val success = gl.getBoolean(shader,WGL2.COMPILE_STATUS)
		if(success) return shader

		val toCompileHeader = head + src
		// val toCompileHeader = head + Gatalog.getGLSLHeader() + '\n' + src // TODO

		gl.shaderSource(shader, toCompileHeader)
		gl.compileShader(shader)
		val error = gl.getShaderInfoLog(shader)
		gl.deleteShader(shader)

		var errorMsg = s"ERROR : could not compile shader\n$error\n"
		for((line,i) <- toCompileHeader.split("\n").view.zipWithIndex) errorMsg+=f"${i+1}%03d: $line\n"

		throw new Exception(errorMsg) // TODO
	}

	private def linkProgram(vshader: webgl.Shader, fshader: webgl.Shader): webgl.Program = {
		val program: webgl.Program = gl.createProgram().nn
		gl.attachShader(program, vshader)
		gl.attachShader(program, fshader)
		gl.linkProgram(program)
		gl.detachShader(program, vshader)
		gl.detachShader(program, fshader)
		val success = gl.getBoolean(program, WGL2.LINK_STATUS)
		if (success) return program
		val error = gl.getProgramInfoLog(program)
		gl.deleteProgram(program)
		throw new Exception(s"ERROR : could not link shader\n$error")
	}

	private def compileProgram(vshaderSrc: String, fshaderSrc: String): webgl.Program = {
		val vshader = this.compileShader(vshaderSrc, WGL2.VERTEX_SHADER)
		val fshader = this.compileShader(fshaderSrc, WGL2.FRAGMENT_SHADER)
		val program = this.linkProgram(vshader, fshader)
		this.gl.deleteShader(vshader)
		this.gl.deleteShader(fshader)
		program
	}

}
