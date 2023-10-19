package engine.render

import engine.components.SpriteRenderer
import engine.io.window.Window
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30.*

private const val POS_SIZE = 2
private const val COLOR_SIZE = 4
private const val VERTEX_SIZE = 6
private const val VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.SIZE_BYTES

private const val POS_OFFSET = 0L
private const val COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.SIZE_BYTES

class RenderBatch(private val maxBatchSize: Int) {
    // Vertex

    // pos                    color
    // float, float,          float, float, float, float

    private val spriteRenderer = ArrayList<SpriteRenderer>(maxBatchSize)
    private var vertices = FloatArray(maxBatchSize * 4 * VERTEX_SIZE)

    var hasRoom = true

    private var numSprites = 0
    private var vaoID = 0
    private var vboID = 0

    private var shader = Shader(
        vertexPath = "/Users/jj/Documents/_intelliJ/K2DEngine/src/main/resources/shaders/defaultVertex.glsl",
        fragmentPath = "/Users/jj/Documents/_intelliJ/K2DEngine/src/main/resources/shaders/defaultFragment.glsl"
    ).apply {
        create()
    }

    fun addSpriteRenderer(renderer: SpriteRenderer) {
        val index = numSprites
        spriteRenderer[index] = renderer
        numSprites++
        loadVertexProperties(index)
        hasRoom = numSprites >= maxBatchSize
    }

    fun start() {
        //generate and bind vertex array
        vaoID = glGenVertexArrays()
        glBindVertexArray(vaoID)

        // allocate space for  vertices
        vboID = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        val bufferSize = (vertices.size * Float.SIZE_BYTES).toLong()
        glBufferData(GL_ARRAY_BUFFER, bufferSize, GL_DYNAMIC_DRAW)

        // create and upload indices buffer
        val eboID = glGenBuffers()
        val indices: IntArray = generateIndices()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        // enable thi buffer attr pointer
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET)
        glEnableVertexAttribArray(1)
    }

    fun render() {
        // for new we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID)
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices)

        //use shader
        shader.bind()

        Window.currentScene?.let {
            shader.uploadMat4f("uProjection", it.camera.getProjectionMatrix())
            shader.uploadMat4f("uView", it.camera.getViewMatrix())
        }

        glBindVertexArray(vaoID)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        GL11.glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)

        shader.unbind()
    }

    private fun loadVertexProperties(index: Int) {
        val sprite = spriteRenderer[index]
        var offset = index * 4 * VERTEX_SIZE
        //float float    float float float float    float
        val color = sprite.color

        //add vertices woth apporative perorepties
        var xAdd = 1f
        var yAdd = 1f
        // 1*     *2
        // 3*     *4
        for (i in 0..4) {
            when (i) {
                1 -> yAdd = 0f
                2 -> xAdd = 0f
                3 -> yAdd = 1f
                4 -> xAdd = 1f
            }

            //load pos
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.position.x)
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.position.y)

            //load color
            with(color) {
                vertices[offset + 2] = x
                vertices[offset + 3] = y
                vertices[offset + 4] = z
                vertices[offset + 5] = w
            }

            offset += VERTEX_SIZE
        }
    }

    private fun generateIndices(): IntArray {
        // 6 indices per quad (3 per triangle)
        val elements = IntArray(6 * maxBatchSize)
        for (index in 0..maxBatchSize) {
            val offsetArrayIndex = 6 * index
            val offset = 4 * index
            if (offsetArrayIndex > elements.size - 1) break
            //triangle 1
            println(offsetArrayIndex)
            println(elements.size)
            elements[offsetArrayIndex] = offset + 3
            elements[offsetArrayIndex + 1] = offset + 2
            elements[offsetArrayIndex + 2] = offset + 0
            //triangle 2
            elements[offsetArrayIndex + 3] = offset + 0
            elements[offsetArrayIndex + 4] = offset + 2
            elements[offsetArrayIndex + 5] = offset + 1
        }
        return elements
    }
}


