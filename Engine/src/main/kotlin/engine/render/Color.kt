package engine.render

data class Color(
    val a: Float,
    val r: Float,
    val g: Float,
    val b: Float
) {
    companion object {
        val WHITE = Color(1f, 1f, 1f, 1f)
        val BLACK = Color(1f, 0f, 0f, 0f)
        val GREEN = Color(1f, 0f, 1f, 0f)
        val RED = Color(1f, 1f, 0f, 0f)
        val BLUE = Color(1f, 0f, 0f, 1f)
    }
}
