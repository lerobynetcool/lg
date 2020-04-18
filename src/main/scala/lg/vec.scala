package lg

class vec2(var x: ℝ, var y: ℝ) {
	def + (that:vec2) = new vec2(this.x+that.x,this.y+that.y)
	def - (that:vec2) = new vec2(this.x-that.x,this.y-that.y)
	def * (that:vec2) = new vec2(this.x*that.x,this.y*that.y)
	def / (that:vec2) = new vec2(this.x/that.x,this.y/that.y)
	def ⋅ (that:vec2) = this.x*that.x + this.y*that.y

	def + (that:ℝ) = new vec2(this.x+that,this.y+that)
	def - (that:ℝ) = new vec2(this.x-that,this.y-that)
	def * (that:ℝ) = new vec2(this.x*that,this.y*that)
	def / (that:ℝ) = new vec2(this.x/that,this.y/that)

	override def toString() = "["+x+","+y+"]"
}

class vec3(var x:ℝ, var y:ℝ, var z:ℝ) {
	def + (that:vec3) = new vec3(this.x+that.x,this.y+that.y,this.z+that.z)
	def - (that:vec3) = new vec3(this.x-that.x,this.y-that.y,this.z-that.z)
	def * (that:vec3) = new vec3(this.x*that.x,this.y*that.y,this.z*that.z)
	def / (that:vec3) = new vec3(this.x/that.x,this.y/that.y,this.z/that.z)
	def ⋅ (that:vec3) = this.x*that.x + this.y*that.y + this.z*that.z

	// TODO cross product
	// def ⨯ (that:vec3) = new vec3()

	def + (that:ℝ) = new vec3(this.x+that,this.y+that,this.z+that)
	def - (that:ℝ) = new vec3(this.x-that,this.y-that,this.z-that)
	def * (that:ℝ) = new vec3(this.x*that,this.y*that,this.z*that)
	def / (that:ℝ) = new vec3(this.x/that,this.y/that,this.z/that)

	override def toString() = "["+x+","+y+","+z+"]"
}

class vec4(var x:ℝ, var y:ℝ, var z:ℝ, var w:ℝ) {
	def + (that:vec4) = new vec4(this.x+that.x,this.y+that.y,this.z+that.z,this.w+that.w)
	def - (that:vec4) = new vec4(this.x-that.x,this.y-that.y,this.z-that.z,this.w-that.w)
	def * (that:vec4) = new vec4(this.x*that.x,this.y*that.y,this.z*that.z,this.w*that.w)
	def / (that:vec4) = new vec4(this.x/that.x,this.y/that.y,this.z/that.z,this.w/that.w)
	def ⋅ (that:vec4) = this.x*that.x + this.y*that.y + this.z*that.z + this.w+that.w

	def + (that:ℝ) = new vec4(this.x+that,this.y+that,this.z+that,this.w+that)
	def - (that:ℝ) = new vec4(this.x-that,this.y-that,this.z-that,this.w-that)
	def * (that:ℝ) = new vec4(this.x*that,this.y*that,this.z*that,this.w*that)
	def / (that:ℝ) = new vec4(this.x/that,this.y/that,this.z/that,this.w/that)

	override def toString() = "["+x+","+y+","+z+","+w+"]"
}
