import sys
import time
import Connect
from Server_GUI import Ui_XuanNiaoTR
from QEditDropHandler import QEditDropHandler
from PyQt5.QtGui import QCursor
from PyQt5.QtCore import Qt, pyqtSignal, QSize, pyqtSlot
from PyQt5.QtWidgets import QApplication, QListWidgetItem, QWidget, QHBoxLayout, QFileDialog, QMenu, \
    QAction, QSizePolicy, QLabel


class Main(Ui_XuanNiaoTR):
    sendTextSignal = pyqtSignal(str)

    def __init__(self):
        super().__init__()
        self.client = None
        self.path = None
        self.item_model = None
        self.lineEdit_ipv4.setReadOnly(True)
        self.lineEdit_ipv4.editingFinished.connect(self.lineEdit_ipv4.update)
        self.lineEdit_send.setAcceptDrops(True)  # 支持拖入操作
        self.lineEdit_send.setDragEnabled(True)  # 支持拽出操作
        self.lineEdit_send.installEventFilter(QEditDropHandler(self))  # 拖放处理
        self.send_text = self.lineEdit_send.text()

        self.start_point = None
        self.window_point = None
        self.is_moving = None
        # 声明在page_bubble创建右键菜单
        self.page_bubble.setContextMenuPolicy(Qt.CustomContextMenu)
        self.page_bubble.customContextMenuRequested.connect(self.page_bubble_rightmenu)  # 连接到菜单显示函数
        self.page_text.setContextMenuPolicy(Qt.CustomContextMenu)
        self.page_text.customContextMenuRequested.connect(self.page_text_rightmenu)  # 连接到菜单显示函数

        self.Thread_c = Connect.Thread_connecting()
        self.Thread_c.addBubbleSignal_c.connect(self.addList)  # 将子线程的信号连接到主线程的刷新函数上
        self.Thread_c.changeIpv4Signal.connect(self.changeIpv4)
        self.Thread_s = Connect.Thread_sending()
        self.Thread_s.addBubbleSignal_s.connect(self.addList)
        self.Thread_r = Connect.Thread_receiving()
        self.Thread_r.addBubbleSignal_r.connect(self.addList)
        self.Thread_c.connectOKSignal.connect(self.connectOK)

        self.Thread_c.start()

    def check_connect(self):
        print("状态改变")
        status = self.checkBox_connect.isChecked()
        if status:
            self.connect()
            print("已连接")

    def mousePressEvent(self, e):
        self.start_point = e.globalPos()
        self.window_point = self.frameGeometry().topLeft()

    def mouseMoveEvent(self, e):
        self.is_moving = True
        realPos = e.globalPos() - self.start_point
        self.move(self.window_point + realPos)

    # 使用此函数会导致单击可拖动区域闪退
    # def mouseReleaseEvent(self, e):
    #     if not self.is_moving:
    #         self.close()
    #     self.is_moving = False

    @pyqtSlot()
    def on_pushButton_setting_clicked(self):
        self.getPath()

    def getPath(self):
        get_directory_path = QFileDialog.getExistingDirectory(
            self, "请指定文件存储路径", "D:/")
        self.path = str(get_directory_path)+"/"
        self.Thread_r.get_path(str(get_directory_path)+"/")
        print(self.path)

    @pyqtSlot()
    def on_pushButton_minimizeApp_clicked(self):
        self.showMinimized()

    @pyqtSlot()
    def on_pushButton_closeApp_clicked(self):
        self.close()

    def addList(self, Msg):
        self.add_bubble(Msg)
        self.add_text(Msg)

    def add_bubble(self, Msg):
        print(Msg)
        Msg_type = Msg['type']
        Msg_file = Msg['file']
        # 添加bubble部分
        item = QListWidgetItem()  # 创建QListWidgetItem对象
        item.setSizeHint(QSize(390, 45))  # 设置QListWidgetItem大小,50这个高度应可变
        self.listWidget_bubble.addItem(item)  # 添加item
        # item的Widget
        widget_item = QWidget()
        # item的横向布局
        layout_item = QHBoxLayout(widget_item)
        layout_item.setContentsMargins(6, 5, 6, 5)

        if Msg_type == 0:
            layout_item.setAlignment(Qt.AlignLeft)
            label_content = QLabel(widget_item)
            if Msg_file == 1:
                Msg_file_name = Msg['file_name']
                content = "接收手机端文件:"+Msg_file_name
            else:
                Msg_content = Msg['content']
                content = Msg_content
        else:
            layout_item.setAlignment(Qt.AlignRight)
            label_content = QLabel(widget_item)
            if Msg_file == 1:
                Msg_file_name = Msg['file_name']
                content = "电脑端发送文件:"+Msg_file_name
            else:
                Msg_content = Msg['content']
                content = Msg_content
        label_content.setText(content)
        # label_content.setWordWrap(True)
        label_content.setAlignment(Qt.AlignVCenter)
        label_content.setContentsMargins(6, 5, 6, 5)
        label_content.setMaximumWidth(300)
        sizePolicy = QSizePolicy(QSizePolicy.Minimum, QSizePolicy.Expanding)
        label_content.setSizePolicy(sizePolicy)
        label_content.setTextInteractionFlags(Qt.TextSelectableByMouse)
        label_content.setStyleSheet("background-color: rgb(130,230,255);border-radius: 4px;")
        label_content.setObjectName("label_content")
        # widget_item.setStyleSheet("background-color: rgb(180,250,255);border-radius: 4px;")
        layout_item.addWidget(label_content)
        self.listWidget_bubble.setItemWidget(item, widget_item)  # 为item设置widget

    def add_text(self, Msg):
        # 添加text部分
        Msg_type = Msg['type']
        Msg_file = Msg['file']
        if Msg_type == 0:
            color = "<font color='green'>"
            if Msg_file == 1:
                Msg_file_name = Msg['file_name']
                self.textBrowser_chart.append(
                    color + time.strftime('%H:%M:%S') + ' 接收客户端文件:' + "<font>")
                self.textBrowser_chart.append(
                    color + str(Msg_file_name) + "<font>")
                self.textBrowser_chart.append("\n")
            else:
                Msg_content = Msg['content']
                self.textBrowser_chart.append(
                    color + time.strftime('%H:%M:%S') + ' 客户端:' + "<font>")
                self.textBrowser_chart.append(
                    color + Msg_content + "<font>")
                self.textBrowser_chart.append("\n")
        else:
            color = "<font color='blue'>"
            if Msg_file == 1:
                Msg_file_name = Msg['file_name']
                self.textBrowser_chart.append(
                    color + time.strftime('%H:%M:%S') + ' 电脑端发送文件:' + "<font>")
                self.textBrowser_chart.append(
                    color + str(Msg_file_name) + "<font>")
                self.textBrowser_chart.append("\n")
            else:
                Msg_content = Msg['content']
                self.textBrowser_chart.append(
                    color + time.strftime('%H:%M:%S') + ' 电脑端:' + "<font>")
                self.textBrowser_chart.append(color + Msg_content + "<font>")
                self.textBrowser_chart.append("\n")

    def changeIpv4(self, ipv4):
        self.lineEdit_ipv4.setText(ipv4)

    def connectOK(self):
        self.lineEdit_send.returnPressed.connect(self.sending)
        self.pushButton_send.clicked.connect(self.sending)
        try:
            self.Thread_r.start()
        except Exception as e:
            print(e)

    def sending(self):
        self.Thread_s.getsendtext(self.lineEdit_send.text())
        self.Thread_s.start()
        # text = time.strftime('%H:%M:%S') + ' 电脑端:' + self.lineEdit_send.text()
        # Msg = {"type": 1, "content": text}
        # self.addList(Msg)
        self.lineEdit_send.clear()
        return

    def switch_text(self):
        self.stackedWidget.setCurrentIndex(1)

    def switch_bubble(self):
        self.stackedWidget.setCurrentIndex(0)

    # 创建右键菜单函数
    def page_bubble_rightmenu(self):
        # 菜单对象
        self.page_bubble_menu = QMenu(self)
        # self.actionA = self.contextMenu.addAction(QIcon("images/0.png"),u'|  动作A')
        self.action_text = QAction(u'切换显示', self)
        # self.actionA.setShortcut('Ctrl+S')  # 设置快捷键
        self.page_bubble_menu.addAction(self.action_text)  # 把动作text选项添加到菜单
        self.action_text.triggered.connect(self.switch_text)  # 将动作A触发时连接到槽函数 button
        # 声明当鼠标在page_bubble控件上右击时，在鼠标位置显示右键菜单,exec_,popup两个都可以
        self.page_bubble_menu.popup(QCursor.pos())

    def page_text_rightmenu(self):
        # 菜单对象
        self.page_text_menu = QMenu(self)
        # self.actionA = self.contextMenu.addAction(QIcon("images/0.png"),u'|  动作A')
        self.action_bubble = QAction(u'切换显示', self)
        # self.actionA.setShortcut('Ctrl+S')  # 设置快捷键
        self.page_text_menu.addAction(self.action_bubble)  # 把动作text选项添加到菜单
        self.action_bubble.triggered.connect(self.switch_bubble)  # 将动作A触发时连接到槽函数 button
        # 声明当鼠标在page_bubble控件上右击时，在鼠标位置显示右键菜单,exec_,popup两个都可以
        self.page_text_menu.popup(QCursor.pos())


if __name__ == '__main__':
    app = QApplication(sys.argv)
    win = Main()
    win.setWindowFlags(Qt.FramelessWindowHint)
    win.show()
    app.exit(app.exec())
