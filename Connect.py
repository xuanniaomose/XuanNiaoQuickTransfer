import re
import time
import socket
import FileMark
import threading
from os import path


def __init__(self):
    self.s = None
    self.addr = None
    self.client = None
    self.receive_buffer = str()  # 接收区缓冲
    self.send_buffer = str()  # 发送区缓冲
    self.Server_port = int(9999)


def check_connect(self):
    print("状态改变")
    status = self.checkBox_connect.isChecked()
    if status:
        connect(self)
        print("已连接")


# 自动获取本机host
def get_host_auto():
    # 函数 get_host_auto() 返回当前正在执行 Python 的系统主机名
    host = str(socket.gethostbyname(socket.gethostname()))
    return host


# 建立连接
def connect(self):
    s = socket.socket()
    host = get_host_auto()
    s.bind((host, self.Server_port))
    s.listen(1)
    self.ChartBrowser.append('等待手机接入...\n')
    client, addr = s.accept()
    self.ChartBrowser.append(time.strftime('%H:%M:%S') + ' 连接手机IP为'
                             + str(addr[0]) + '手机端口' + str(addr[1]) + '\n\n')
    try:
        receiving(self)
    except Exception as e:
        print(e)
    return


# 发送数据
def send_data(self):
    if self.s_entry.text() is not None:
        send_text = self.s_entry.text()
        mark = FileMark.check(send_text)
        if mark == 1:
            file_path = str(FileMark.fpath(send_text))
            file_name = str(FileMark.name(file_path))
            file_len = str(path.getsize(file_path))
            file_head = str("@FMark@"+file_name+"@FName@"+file_len+"@FLen@")
            # file_head = file_head.ljust(1008, "0")
            print(file_head+"\n", len(file_head))
            self.client.send(bytes(file_head.encode("utf-8")))
            print("文件信息已发送")
            time.sleep(0.3)
            f = open(file_path, 'rb')
            while 1:
                data = f.read(1024)
                if data is None:
                    print("指定路径没有找到文件"+(path.basename(file_path)))
                    break
                self.client.send(data)
                f.flush()
            f.close()
        else:
            # 特别注意：数据的结尾加上换行符才可让客户端的readline()停止阻塞
            self.client.send(bytes(send_text, 'utf-8'))
            self.ChartBrowser.append(time.strftime('%H:%M:%S') + ' 服务器:\n' + self.send_buffer + '\n\n')
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
                    self.ChartBrowser.append(time.strftime('%H:%M:%S') + ' 手机端:断开连接' + '\n\n')
                # 用正则表达式判定接收内容是“断开”、“消息”还是“文件”
                elif msg_type is not None:
                    print(msg_type.group(0))
                    # 传输类型为文件
                    if msg_type.group(0) == "@FMark@":
                        # server_head_msg = json.loads(self.client.recv(1024))
                        file_name = re.findall(r"@FMark@(.*)@FName@", self.receive_buffer, re.M)[0]
                        self.ChartBrowser.append(time.strftime('%H:%M:%S') + '手机端:\n' + str(file_name))
                        print(file_name)
                        file_path = 'E:/test/'
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
                            print("已接收：", int(recv_len/file_len*100), "%")
                            f.write(data)
                        f.close()
                        self.client.send(bytes("接收到：" + file_name, 'utf8'))

                        # 另一种写法
                        # recv_len = 0
                        # with open(file_path + file_name, 'wb') as f:
                        #     while recv_len < file_len:
                        #         if file_len - recv_len > 1024:
                        #             self.input_stream = self.client.recv(1024)
                        #             recv_len += len(self.input_stream)
                        #         else:
                        #             self.input_stream = self.client.recv(file_len-recv_len)
                        #             recv_len = file_len
                        #         f.write(self.input_stream)
                        #     f.close()
                        #     print("接收到字节数：", recv_len)

                else:
                    self.receive_str = self.receive_buffer
                    self.ChartBrowser.insertPlainText(
                        time.strftime('%H:%M:%S') + ' 客户端:\n' + self.receive_str + '\n')
        except Exception as e:
            print(e)


# connecting(),sending()和receiving()分别开启一个线程
def connecting(self):
    threading.Thread(target=connect(self)).start()
    return


def sending(self):
    threading.Thread(target=send_data(self)).start()
    return


def receiving(self):
    sleep_time = 5
    t_r = threading.Thread(target=receive_data(self), args=(sleep_time,))
    t_r.setDaemon(True)
    t_r.start()
