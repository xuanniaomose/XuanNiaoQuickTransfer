
# 主要的思路就是创建两个frame（如果有两个以上同理）使用setVisible()函数显示或者隐藏frame 参数是bool值
import sys
from PyQt5.QtWidgets import *
from PyQt5.QtCore import *
from PyQt5.QtGui import *

class logindialog(QDialog):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        self.setWindowTitle('登录界面')
        self.resize(200, 200)
        self.init_index()
        self.init_index1()
        self.pushButton_enter.clicked.connect(self.on_pushButton_enter_clicked)
        self.pushButton_quit.clicked.connect(self.on_pushButton_enter_clicked_1)

    def on_pushButton_enter_clicked(self):
        self.frame1.setVisible(True)
        self.frame.setVisible(False)

    def on_pushButton_enter_clicked_1(self):
        self.frame1.setVisible(False)
        self.frame.setVisible(True)

    def init_index(self):
        self.frame = QFrame(self)
        self.verticalLayout2 = QVBoxLayout(self.frame)
        self.lineEdit_account = QLineEdit()
        self.lineEdit_account.setPlaceholderText("请输入账号")
        self.verticalLayout2.addWidget(self.lineEdit_account)

        self.lineEdit_password = QLineEdit()
        self.lineEdit_password.setPlaceholderText("请输入密码")
        self.verticalLayout2.addWidget(self.lineEdit_password)

        self.pushButton_enter = QPushButton()
        self.pushButton_enter.setText("进入下一个界面")
        self.verticalLayout2.addWidget(self.pushButton_enter)

    def init_index1(self):
        self.frame1 = QFrame(self)
        self.verticalLayout1 = QVBoxLayout(self.frame1)

        self.lineEdit_account = QLineEdit()
        self.lineEdit_account.setPlaceholderText("请输入132")
        self.verticalLayout1.addWidget(self.lineEdit_account)

        self.pushButton_quit = QPushButton()
        self.pushButton_quit.setText("回到主页面")
        self.verticalLayout1.addWidget(self.pushButton_quit)
        self.frame1.setVisible(False)


if __name__ == "__main__":
    app = QApplication(sys.argv)
    dialog = logindialog()
    dialog.show()
    sys.exit(app.exec_())
