package engine.render

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage
import utils.Log

class Texture(
    private val path: String
) {
    private var textureID = 0

    init {
        createTexture()
        setTextureParams()
        getTextureImg()
    }

    private fun createTexture() {
        textureID = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, textureID)
    }

    private fun setTextureParams() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    }

    private fun getTextureImg() {
        val width = BufferUtils.createIntBuffer(1)
        val height = BufferUtils.createIntBuffer(1)
        val channels = BufferUtils.createIntBuffer(1)
        val image = STBImage.stbi_load(path, width, height, channels, 0)
        if (image != null) {
            val format = if (channels[0] == 4) GL_RGBA else GL_RGB
            glTexImage2D(GL_TEXTURE_2D, 0, format, width[0], height[0], 0, format, GL_UNSIGNED_BYTE, image)
        } else {
            Log.e(this::class.java, "Could load texture from $path")
        }
        STBImage.stbi_image_free(image)
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, textureID)
    }

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}