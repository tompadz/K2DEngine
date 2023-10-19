package engine.components
import engine.objects.GameObject

abstract class Component : IComponentLifecycle {
    lateinit var gameObject: GameObject
}