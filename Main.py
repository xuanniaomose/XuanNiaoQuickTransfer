import socket
import sys
import threading
import time
from os import path

from PyQt5 import QtWidgets
from PyQt5.QtCore import Qt, pyqtSignal, QSize
from PyQt5.QtWidgets import QApplication, QListWidgetItem, QWidget, QHBoxLayout, QVBoxLayout

import Connect
from Connect import Thread_connecting, Thread_sending, Thread_receiving
import FileMark
from QEditDropHandler import QEditDropHandler
from Server_GUI import Ui_XuanNiaoTR


class Main(Ui_XuanNiaoTR):
    sendTextSignal = pyqtSignal(str)

    def __init__(self):
        super().__init__()
        self.client = None
        self.itemmodel = None
        # self.AddBubbleSignal.connect(self.addBubble)
        self.lineEdit_ipv4.setReadOnly(True)
        # self.checkBox_connect.stateChanged.connect(self.check_connect)
        self.lineEdit_send.setAcceptDrops(True)  # 支持拖入操作
        self.lineEdit_send.setDragEnabled(True)  # 支持拽出操作
        self.lineEdit_send.installEventFilter(QEditDropHandler(self))  # 这句要放Ui里
        print(time.strftime('%H:%M:%S'))
        self.lineEdit_ipv4.editingFinished.connect(self.lineEdit_ipv4.update)
        self.send_text = self.lineEdit_send.text()

    def check_connect(self):
        print("状态改变")
        status = self.checkBox_connect.isChecked()
        if status:
            self.connect()
            print("已连接")




if __name__ == '__main__':
    app = QApplication(sys.argv)
    win = Main()
    win.setWindowFlags(Qt.FramelessWindowHint)
    win.show()
    app.exit(app.exec())
