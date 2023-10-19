package engine.render

import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20.*
import utils.FileUtils
import utils.Log

class Shader(vertexPath: String, fragmentPath: String) {

    private val TAG = "Shader"

    private val vertexFile: String = FileUtils().loadAsString(vertexPath)
    private val fragmentFile: String = FileUtils().loadAsString(fragmentPath)

    private var programID = 0
    private var vertexID = 0
    private var fragmentID = 0

    var isUsed: Boolean = false
        private set

    fun create() {
        createVertexShader()
        createFragmentShader()
        createProgram()
//        validateProgram()
    }

    fun bind() {
        if (!isUsed) {
            glUseProgram(programID)
            isUsed = true
        }
    }

    fun unbind() {
        glUseProgram(0)
        isUsed = false
    }

    fun destroy() {
        unbind()
        glDeleteProgram(programID)
    }

    /**
     * -----------------------------
     * Matrix
     * -----------------------------
     */

    fun uploadMat4f(varName: String, mat4f: Matrix4f) {
        val location = glGetUniformLocation(programID, varName)
        bind()
        val matBuffer = BufferUtils.createFloatBuffer(16)
        mat4f.get(matBuffer)
        glUniformMatrix4fv(location, false, matBuffer)
    }

    fun uploadMat3f(varName: String, mat3f: Matrix3f) {
        val location = glGetUniformLocation(programID, varName)
        bind()
        val matBuffer = BufferUtils.createFloatBuffer(8)
        mat3f.get(matBuffer)
        glUniformMatrix3fv(location, false, matBuffer)
    }

    /**
     * -----------------------------
     * Vector
     * -----------------------------
     */

    fun uploadVec4f(varName: String, vec4f: Vector4f) {
        val location = glGetUniformLocation(programID, varName)
        bind()
        glUniform4f(location, vec4f.x, vec4f.y, vec4f.z, vec4f.w)
    }

    fun uploadVec3f(varName: String, vec3f: Vector3f) {
        val location = glGetUniformLocation(programID, varName)
        bind()
        glUniform3f(location, vec3f.x, vec3f.y, vec3f.z)
    }

    fun uploadVec2f(varName: String, vec2f: Vector2f) {
        val location = glGetUniformLocation(programID, varName)
        bind()
        glUniform2f(location, vec2f.x, vec2f.y)
    }

    /**
     * -----------------------------
     * Primitive
     * -----------------------------
     */

    fun uploadFloat(varName: String, value: Float) {
        val location = glGetUniformLocation(programID, varName)
        bind()
        glUniform1f(location, value)
    }

    fun uploadInt(varName: String, value: Int) {
        val location = glGetUniformLocation(programID, varName)
        bind()
        glUniform1i(location, value)
    }

    /**
     * -----------------------------
     * Texture
     * -----------------------------
     */

    fun uploadTexture(varName: String, slot: Int) {
        val location = glGetUniformLocation(programID, varName)
        bind()
        glUniform1i(location, slot)
    }

    /**
     * -----------------------------
     * Private
     * -----------------------------
     */

    private fun createVertexShader() {
        vertexID = glCreateShader(GL_VERTEX_SHADER)

        glShaderSource(vertexID, vertexFile)
        glCompileShader(vertexID)

        val isVertexCompile = glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL11.GL_TRUE
        if (!isVertexCompile) {
            Log.e(TAG, "Vertex shader error - ${glGetShaderInfoLog(vertexID)}")
            return
        }
    }

    private fun createFragmentShader() {
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER)

        glShaderSource(fragmentID, fragmentFile)
        glCompileShader(fragmentID)

        val isFragmentCompile = glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL11.GL_TRUE
        if (!isFragmentCompile) {
            Log.e(TAG, "Fragment shader error - ${glGetShaderInfoLog(fragmentID)}")
            return
        }
    }

    private fun createProgram() {
        programID = glCreateProgram()

        glAttachShader(programID, vertexID)
        glAttachShader(programID, fragmentID)
        glLinkProgram(programID)

        val isProgramCompile = glGetProgrami(programID, GL_LINK_STATUS) == GL11.GL_TRUE
        if (!isProgramCompile) {
            Log.e(TAG, "Program linking error - ${glGetProgramInfoLog(programID)}")
            return
        }
    }

    private fun validateProgram() {
        glValidateProgram(programID)
        val isValidateCompile = glGetProgrami(programID, GL_VALIDATE_STATUS) == GL11.GL_TRUE
        if (!isValidateCompile) {
            Log.e(TAG, "Program validate error - ${glGetProgramInfoLog(programID)}")
            return
        }
        glDeleteShader(vertexID)
        glDeleteShader(fragmentID)
    }

}