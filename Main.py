import sys

from PyQt5.QtCore import Qt
from PyQt5.QtWidgets import QMainWindow, QApplication
from Server_GUI import Ui_XuanNiaoTR
from Connect import Connect


class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.__ui = Connect()
        # self.setWindowFlags(Qt.FramelessWindowHint)
        self.__ui.setupUi(self)
        # self.setWindowTitle('玄鸟快传')  # 设置窗口标题要在窗口ui创建之后


if __name__ == '__main__':
    app = QApplication(sys.argv)
    win = MainWindow()
    win.show()
    app.exit(app.exec())