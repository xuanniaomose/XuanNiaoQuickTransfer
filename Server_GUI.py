from tkinter import *
import socket
import time
import threading
from tkinter import scrolledtext


# 定义一个Server类
class Server:
    def __init__(self):
        self.root = Tk()
        self.root.title('玄鸟快传')
        self.root.geometry('420x520')
        self.receive_buffer = str()  # 接收区缓冲
        self.send_buffer = str()  # 发送区缓冲
        self.Server_ip = self.get_host_auto()
        self.Server_port = int(9999)
        self.receive_str = StringVar(value=self.receive_buffer)
        self.send_str = StringVar(value=self.send_buffer)
        self.ip = StringVar(value=self.Server_ip)
        self.port = IntVar(value=self.Server_port)
        # 框体
        self.frame_bar = Frame(self.root, height=12)
        # 标签
        self.ip_label = Label(self.frame_bar, text='电脑端IP地址')
        self.port_label = Label(self.frame_bar, text='电脑端端口号')
        self.s_label = Label(self.root, text='输入框')
        self.recorde_label = Label(self.root, text='收发记录（请在建立连接后通信）', height=1)
        # 文本框
        self.ip_entry = Entry(self.frame_bar, textvariable=self.ip, width=12)
        self.port_entry = Entry(self.frame_bar, textvariable=self.port, width=6)
        self.s_entry = Entry(self.root, textvariable=self.send_str)
        self.recorde = scrolledtext.ScrolledText(self.root)
        # 按钮
        self.btn0 = Button(self.frame_bar, text='发送', command=lambda: self.sending(), width=7)
        self.btn1 = Button(self.frame_bar, text='建立连接', command=lambda: self.connecting(), width=7)
        # 排列_框体内
        self.ip_label.pack(side=LEFT)
        self.ip_entry.pack(side=LEFT)
        self.port_label.pack(side=LEFT)
        self.port_entry.pack(side=LEFT)
        self.btn0.pack(side=RIGHT)
        self.btn1.pack(side=RIGHT)
        # 排列_整体
        self.recorde_label.pack(fill=X)
        self.recorde.pack(expand=True, fill=BOTH)
        self.frame_bar.pack(fill=X)
        self.s_entry.pack(fill=X, ipady=70)

        self.connecting()
        self.root.after(500, self.update)
        self.root.mainloop()

    # 自动获取本机host
    def get_host_auto(self):
        # 函数 gethostname() 返回当前正在执行 Python 的系统主机名
        host = str(socket.gethostbyname(socket.gethostname()))
        return host

    # 建立连接
    def connect(self):
        self.s = socket.socket()
        host = self.get_host_auto()
        self.s.bind((host, 9999))
        self.s.listen(1)
        self.recorde.insert(INSERT, 'waiting...\n')
        self.client, self.addr = self.s.accept()
        self.recorde.insert(INSERT, time.strftime('%H:%M:%S') + ' 连接手机IP为' + str(self.addr[0]) + '手机端口' + str(
            self.addr[1]) + '\n')
        return

    # 发送数据
    def send_data(self):
        self.send_buffer = self.send_str.get()
        self.client.send(bytes(self.send_buffer, 'utf8'))
        self.recorde.insert(INSERT, time.strftime('%H:%M:%S') + ' 服务器:\n' + self.send_buffer + '\n')
        return

    # 接收数据
    def receive_data(self):
        try:
            self.receive_buffer = str(self.client.recv(1024), 'utf8')
            if not self.receive_buffer:
                return
            self.receive_str.set(self.receive_buffer)
            self.recorde.insert(INSERT, time.strftime('%H:%M:%S') + ' 客户端:\n' + self.receive_buffer + '\n')
        except Exception as e:
            print(e)
        return

    # start(),send_data()和receive_data()分别开启一个线程
    def connecting(self):
        threading.Thread(target=self.connect).start()
        return

    def sending(self):
        threading.Thread(target=self.send_data).start()
        return

    def receiving(self):
        threading.Thread(target=self.receive_data).start()
        return

    # 更新缓存区
    def update(self):
        try:
            self.receiving()
        except Exception as e:
            print(e)
        self.root.after(500, self.update)
        return


if __name__ == '__main__':
    ser = Server()
