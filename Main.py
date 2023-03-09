import sys
from PyQt6.QtWidgets import QMainWindow, QApplication
from Server_GUI import Ui_XuanNiaoTR


class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.__ui = Ui_XuanNiaoTR()
        self.__ui.setupUi(self)
        self.setWindowTitle('玄鸟快传')  # 设置窗口标题要在窗口ui创建之后


if __name__ == '__main__':
    app = QApplication(sys.argv)
    win = MainWindow()
    win.show()
    app.exit(app.exec())
