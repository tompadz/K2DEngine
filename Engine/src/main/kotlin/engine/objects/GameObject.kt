package engine.objects

import engine.components.Component
import utils.EngineID

open class GameObject(
    val name: String,
    val transform: Transform = Transform(),
    val width: Int = 0,
    val height: Int = 0
) {

    val id: Long = EngineID.generate()
    private val components = mutableMapOf<Class<*>, Component>()

    fun start() {
        for (component in components.values) {
            component.start()
        }
    }

    fun update() {
        for (component in components.values) {
            component.update()
        }
    }

    fun destroy() {
        for (component in components.values) {
            component.destroy()
        }
    }

    fun <C : Component> getComponentOrNull(componentClass: Class<C>): Component? {
        return components[componentClass]
    }

    fun <C : Component> getComponent(componentClass: Class<C>): Component {
        return components[componentClass] !!
    }

    fun <C : Component> addComponent(component: C) {
        components[component::class.java] = component
        component.gameObject = this
    }

    fun <C : Component> removeComponent(componentClass: Class<C>) {
        components.remove(componentClass)
    }

}