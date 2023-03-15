import re
import sys
import time
import socket

from PyQt5.QtCore import Qt
from PyQt5.QtGui import QTextCursor
from PyQt5.QtWidgets import QApplication

import FileMark
import threading
from os import path

from Server_GUI import Ui_XuanNiaoTR
from QEditDropHandler import QEditDropHandler


class Connect(Ui_XuanNiaoTR):
    def __init__(self):
        super().__init__()
        self.s = None
        self.addr = None
        self.client = None
        self.input_stream = None
        self.receive_buffer = str()  # 接收区缓冲
        self.send_buffer = str()  # 发送区缓冲
        self.Server_ipv4 = self.get_host_auto()
        self.Server_port = int(9999)
        self.receive_str = None
        self.send_str = None
        self.ip = None

        self.textBrowser_chart.append(self.receive_str)
        self.lineEdit_ipv4.setText(self.Server_ipv4)
        self.lineEdit_ipv4.setReadOnly(True)
        self.Server_port = int(self.lineEdit_port.text())
        self.checkBox_connect.stateChanged.connect(self.check_connect)
        self.lineEdit_send.setAcceptDrops(True)  # 支持拖入操作
        self.lineEdit_send.setDragEnabled(True)  # 支持拽出操作
        self.lineEdit_send.returnPressed.connect(self.sending)
        self.lineEdit_send.installEventFilter(QEditDropHandler(self))  # 这句要放Ui里
        self.pushButton_send.clicked.connect(self.sending)
        self.lineEdit_ipv4.editingFinished.connect(self.lineEdit_ipv4.update)
        self.connecting()

    def check_connect(self):
        print("状态改变")
        status = self.checkBox_connect.isChecked()
        if status:
            self.connect()
            print("已连接")

    # 自动获取本机host
    def get_host_auto(self):
        # 函数 gethostname() 返回当前正在执行 Python 的系统主机名
        host = str(socket.gethostbyname(socket.gethostname()))
        return host

    # 建立连接
    def connect(self):
        self.s = socket.socket()
        host = self.get_host_auto()
        self.s.bind((host, self.Server_port))
        self.s.listen(1)
        # Cannot queue arguments of type 'QTextCursor'↓
        # Make sure 'QTextCursor' is registered using qRegisterMetaType().↓
        self.textBrowser_chart.append('等待手机接入...\n')
        self.client, self.addr = self.s.accept()
        self.textBrowser_chart.append(time.strftime('%H:%M:%S') + ' 连接手机IP为' +
                                  str(self.addr[0]) + '手机端口' + str(self.addr[1]) + '\n\n')
        try:
            self.receiving()
        except Exception as e:
            print(e)
        return

    # 发送数据
    def send_data(self):
        if self.lineEdit_send.text() is not None:
            send_text = self.lineEdit_send.text()
            mark = FileMark.check(send_text)
            print("触发发送2"+send_text)
            if mark == 1:
                file_path = str(FileMark.fpath(send_text))
                file_name = str(FileMark.name(file_path))
                file_len = str(path.getsize(file_path))
                file_head = str("@FMark@" + file_name + "@FName@" + file_len + "@FLen@")
                print(file_head + "\n", len(file_head))
                self.client.send(bytes(file_head.encode("utf-8")))
                print("文件信息已发送")
                time.sleep(0.3)
                f = open(file_path, 'rb')
                while 1:
                    data = f.read(1024)
                    if data is None:
                        print("指定路径没有找到文件" + (path.basename(file_path)))
                        break
                    self.client.send(data)
                    f.flush()
                f.close()
            else:
                # 特别注意：数据的结尾加上换行符才可让客户端的readline()停止阻塞
                self.client.send(bytes(send_text, 'utf-8'))
                print("触发发送3"+send_text)
                self.textBrowser_chart.append(
                    "<font color='blue'>" + time.strftime('%H:%M:%S') + ' 电脑端:' + "<font>")
                self.textBrowser_chart.append("<font color='blue'>" + send_text + "<font>")
                self.textBrowser_chart.append('\n')
            self.lineEdit_send.clear()
            # 移动textBrowser_chart到最后
            cursor = self.textBrowser_chart.textCursor()
            cursor.movePosition(QTextCursor.End)
            self.textBrowser_chart.setTextCursor(cursor)
        return

    # 接收数据
    def receive_data(self):
        while True:
            try:
                self.input_stream = self.client.recv(1024)
                print(self.input_stream)
                self.receive_buffer = str(self.input_stream, 'utf8')
                msg_type = re.search(r"@\wMark@", self.receive_buffer, re.MULTILINE)
                if self.receive_buffer != "":
                    print(self.receive_buffer)
                    if self.receive_buffer == "@EndMark@\n":
                        self.textBrowser_chart.append(time.strftime('%H:%M:%S') + ' 手机端:断开连接' + '\n\n')
                    # 用正则表达式判定接收内容是“断开”、“消息”还是“文件”
                    elif msg_type is not None:
                        print(msg_type.group(0))
                        # 传输类型为文件
                        if msg_type.group(0) == "@FMark@":
                            file_name = re.findall(r"@FMark@(.*)@FName@", self.receive_buffer, re.M)[0]
                            self.textBrowser_chart.append(time.strftime('%H:%M:%S') + '手机端:\n' + str(file_name))
                            print(file_name)
                            file_path = self.path
                            file_len = int(re.findall(r"@FName@(.*)@FLen@", self.receive_buffer, re.M)[0])
                            print(file_len)

                            f = open(file_path + file_name, "wb")
                            recv_len = 0
                            while recv_len < file_len:
                                if file_len - recv_len > 1024:
                                    length = 1024
                                else:
                                    length = file_len - recv_len
                                data = self.client.recv(length)
                                print(str(data), 'utf-8')
                                data_len = len(data)
                                recv_len += data_len
                                print("已接收：", int(recv_len / file_len * 100), "%")
                                f.write(data)
                            f.close()
                            self.client.send(bytes("接收到：" + file_name, 'utf8'))
                            self.textBrowser_chart.append(
                                "<font color='green'>" + time.strftime('%H:%M:%S') + ' 接收到客户端文件:' + "<font>")
                            self.textBrowser_chart.append(
                                "<font color='green'>" + file_name + str(file_len/1024) + 'kb' + "<font>")
                            self.textBrowser_chart.append('\n')
                    else:
                        self.receive_str = self.receive_buffer
                        self.textBrowser_chart.append(
                            "<font color='green'>" + time.strftime('%H:%M:%S') + ' 客户端:' + "<font>")
                        self.textBrowser_chart.append(
                            "<font color='green'>" + self.receive_str + "<font>")
                        self.textBrowser_chart.append('\n')
                    # 移动textBrowser_chart到最后
                    cursor = self.textBrowser_chart.textCursor()
                    cursor.movePosition(QTextCursor.End)
                    self.textBrowser_chart.setTextCursor(cursor)
            except Exception as e:
                print(e)

    # connecting(),sending()和receiving()分别开启一个线程
    def connecting(self):
        threading.Thread(target=self.connect).start()
        return

    def sending(self):
        threading.Thread(target=self.send_data).start()
        print("触发发送1")
        return

    def receiving(self):
        sleep_time = 5
        t_r = threading.Thread(target=self.receive_data(), args=(sleep_time,))
        t_r.setDaemon(True)
        t_r.start()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    win = Connect()
    win.setWindowFlags(Qt.FramelessWindowHint)
    win.show()
    app.exit(app.exec())
