package engine.components

interface IComponentLifecycle {
    fun update()
    fun start() {}
    fun destroy() {}
}