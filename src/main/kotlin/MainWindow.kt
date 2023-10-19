import engine.io.window.Window

class MainWindow() : Window() {

    override fun onCreate() {
        changeScene(MenuScene())
    }

    override fun onUpdate() {
       //empty
    }
}