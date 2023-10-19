import engine.components.SpriteRenderer
import engine.io.scene.Scene
import engine.objects.GameObject
import engine.objects.Transform
import org.joml.Vector2f
import org.joml.Vector4f

class MenuScene : Scene() {

//    val texture = Texture(
//        "/Users/jj/Documents/_intelliJ/K2DEngine/src/main/resources/textures/grid.jpg"
//    )

    val size = 200f

    private val vertexArray = floatArrayOf(
        //position              //color             //uv coordinates
        size,   0f,   0f,     1f, 0f, 0f, 1f,     1f, 1f,             //bottom right
        0f,     size, 0f,     0f, 1f, 0f, 1f,     0f, 0f,             //top left
        size,   size, 0f,     0f, 0f, 1f, 1f,     1f, 0f,             //top right
        0f,     0f,   0f,     1f, 1f, 0f, 0f,     0f, 1f,             //bottom left
    )

    private val elementArray = intArrayOf(

        //   x     x
        //      x
        //   x     x

        2,1,0, //top right triangle
        0,1,3 //bottom left triangle
    )

    override fun onCreate() {
       val xOffset = 10
        val yOffset = 10

        val totalWidth = 600f -xOffset * 2
        val totalHeight = 600f -yOffset * 2
        val sizeX = totalWidth / 100f
        val sizeY = totalHeight / 100f

        for(x in 0..100) {
            for (y in 0..100) {
                val xPos = xOffset + (x * sizeX)
                val yPos = yOffset + (y * sizeY)
                val gameObject = GameObject(
                    name = "object $x $y",
                    transform = Transform(
                        position =  Vector2f(xPos, yPos),
                        scale = Vector2f(sizeX, sizeY)
                    )
                ).apply {
                    addComponent(SpriteRenderer(
                        Vector4f(
                            xPos / totalWidth,
                            yPos / totalWidth,
                            1f,1f
                        )
                    ))
                }
                addGameObject(gameObject)
            }
        }

    }


    override fun onUpdate() {
    }
}