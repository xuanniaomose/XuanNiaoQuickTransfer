import re
import time
import socket

import Connect
import FileMark
from os import path
from PyQt5.QtCore import pyqtSignal, QThread


class Thread_connecting(QThread):
    print("连接线程启动" + time.strftime('%H:%M:%S'))
    addBubbleSignal_c = pyqtSignal(dict)
    changeIpv4Signal = pyqtSignal(str)
    connectOKSignal = pyqtSignal(int)

    def __init__(self):
        super().__init__()
        self.s = None
        self.addr = None
        self.client = None
        self.send_buffer = str()  # 发送区缓冲
        self.Server_ipv4 = None
        self.Server_port = int(9999)
        self.receive_str = None
        self.send_str = None
        self.frame_bubble = None
        self.textBrowser_bubble = None

    # 自动获取本机host
    def get_host_auto(self):
        # 函数 gethostname() 返回当前正在执行 Python 的系统主机名
        host = str(socket.gethostbyname(socket.gethostname()))
        self.changeIpv4Signal.emit(host)
        return host

    def run(self):
        self.s = socket.socket()
        host = self.get_host_auto()
        self.s.bind((host, self.Server_port))
        self.s.listen(1)
        Msg = {"type": 1, "file": 0, "file_name": None,
               "content": "等待手机接入..."}
        self.addBubbleSignal_c.emit(Msg)
        self.client, self.addr = self.s.accept()
        Connect.Thread_connecting.client = self.client
        text = '连接手机IP:' + str(self.addr[0]) + ' 手机端口:' + str(self.addr[1])
        Msg = {"type": 1, "file": 0, "file_name": None, "content": text}
        self.addBubbleSignal_c.emit(Msg)
        self.connectOKSignal.emit(1)
        # try:
        #     Connect.Thread_receiving().start()
        # except Exception as e:
        #     print(e)
        return

    def get_client(self):
        print(str(Connect.Thread_connecting.client))
        return Connect.Thread_connecting.client


class Thread_sending(QThread):
    print("发送线程启动"+time.strftime('%H:%M:%S'))
    addBubbleSignal_s = pyqtSignal(dict)
    send_text = None

    def getsendtext(self, send_text):
        self.send_text = str(send_text)

    def __init__(self):
        super().__init__()
        self.client = None
        self.paused = False

    def run(self):
        print("发送" + time.strftime('%H:%M:%S'))
        self.client = Connect.Thread_connecting.get_client(Thread_connecting())
        print(str(self.send_text))
        # if self.lineEdit_send.text() is not None:
        if self.send_text != "":
            mark = FileMark.check(self.send_text)
            if mark == 1:
                file_path = str(FileMark.fpath(self.send_text))
                file_name = str(FileMark.name(file_path))
                file_len = str(path.getsize(file_path))
                file_head = str("@FMark@" + file_name + "@FName@" + file_len + "@FLen@")
                print(file_head + "\n", len(file_head))
                self.client.send(bytes(file_head.encode("utf-8")))
                Msg = {"type": 1, "file": 1, "file_name": file_name, "content": None}
                self.addBubbleSignal_s.emit(Msg)
                print("文件信息已发送")
                time.sleep(1)
                file_len_k = int(file_len) / 1024
                f = open(file_path, 'rb')
                send_len = 0
                while send_len < int(file_len):
                    if int(file_len) - send_len > 1024:
                        length = 1024
                    else:
                        length = int(file_len) - send_len
                    data = f.read(length)
                    if data is None:
                        text = "指定路径没有找到文件：" + path.basename(file_path)
                        Msg = {"type": 1, "file": 1, "file_name": file_name, "content": text}
                        self.addBubbleSignal_s.emit(Msg)
                        break
                    self.client.send(data)
                    f.flush()
                    data_len = len(data)
                    send_len += data_len
                    print("已发送：", int(send_len / int(file_len) * 100), "%")
                f.close()
            else:
                # 特别注意：数据的结尾加上换行符才可让客户端的readline()停止阻塞
                self.client.send(bytes(self.send_text, 'utf-8'))
                Msg = {"type": 1, "file": 0, "file_name": None, "content": self.send_text}
                print("发送字符串")
                self.addBubbleSignal_s.emit(Msg)
        return

    def resume(self):  # 用来恢复/启动run
        with self.state:  # 在该条件下操作
            self.paused = False
            self.state.notify()  # Unblock self if waiting.

    def pause(self):  # 用来暂停run
        with self.state:  # 在该条件下操作
            self.paused = True  # Block self.


class Thread_receiving(QThread):
    print("接收线程启动"+time.strftime('%H:%M:%S'))
    addBubbleSignal_r = pyqtSignal(dict)
    path = 'D:/'

    def __init__(self):
        super().__init__()
        self.input_stream = None
        self.receive_buffer = str()  # 接收区缓冲
        self.receive_str = None

    def get_path(self, path):
        self.path = str(path)

    def run(self):
        while True:
            try:
                self.client = Connect.Thread_connecting.get_client(Thread_connecting())
                self.input_stream = self.client.recv(1024)
                # print(self.input_stream)
                self.receive_buffer = str(self.input_stream, 'utf8')
                msg_type = re.search(r"@\wMark@", self.receive_buffer, re.MULTILINE)
                if self.receive_buffer != "":
                    # print(self.receive_buffer)
                    if self.receive_buffer == "@EndMark@\n":
                        Msg = {"type": 0, "file": 0, "file_name": None, "content": '手机端断开连接'}
                        self.addBubbleSignal_r.emit(Msg)
                    # 用正则表达式判定接收内容是“断开”、“消息”还是“文件”
                    elif msg_type is not None:
                        print(msg_type.group(0))
                        # 传输类型为文件
                        if msg_type.group(0) == "@FMark@":
                            file_name = re.findall(r"@FMark@(.*)@FName@", self.receive_buffer, re.M)[0]
                            self.client.send(bytes("接收到：" + file_name, 'utf8'))
                            Msg = {"type": 0, "file": 1, "file_name": file_name, "content": None}
                            self.addBubbleSignal_r.emit(Msg)

                            print(file_name)
                            file_path = self.path
                            print(file_path)
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
                                # print(str(data), 'utf-8')
                                data_len = len(data)
                                recv_len += data_len
                                # print("已接收：", int(recv_len / file_len * 100), "%")
                                f.write(data)
                            f.close()
                    else:
                        self.receive_str = self.receive_buffer
                        Msg = {"type": 0, "file": 0, "file_name": None, "content": self.receive_str}
                        self.addBubbleSignal_r.emit(Msg)

            except Exception as e:
                print(e)
