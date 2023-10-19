package engine.render

import engine.components.SpriteRenderer
import engine.objects.GameObject

private const val MAX_BATCH_SIZE = 1000

class Renderer {
    private val batches = mutableListOf<RenderBatch>()

    fun add(gameObject: GameObject) {
        gameObject.getComponentOrNull(SpriteRenderer::class.java)?.let {
            addSpriteRenderer(it as SpriteRenderer)
        }
    }

    fun render() {
        for (batch in batches) {
            batch.render()
        }
    }

    private fun addSpriteRenderer(renderer: SpriteRenderer) {
        var added = false
        for (batch in batches) {
            if (batch.hasRoom) {
                batch.addSpriteRenderer(renderer)
                added = true
                break
            }
        }
        if (!added) {
            val batch = RenderBatch(MAX_BATCH_SIZE)
            batch.start()
            batches.add(batch)
            batch.addSpriteRenderer(renderer)
        }
    }
}