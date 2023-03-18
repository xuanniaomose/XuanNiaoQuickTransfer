from PyQt5 import QtWidgets, QtCore
from PyQt5.QtCore import QSize, Qt
from PyQt5.QtGui import QPixmap
from PyQt5.QtWidgets import QListWidgetItem, QHBoxLayout, QVBoxLayout, QLabel, QWidget


class Bubble:
    def __init__(self):
        super().__init__()
        self.listWidget_bubble = None

        # self.frame_bubbleReceive = None
        # self.horizontalLayout_receive = None
        # self.textBrowser_receive = None
        #
        # self.frame_bubbleSend = None
        # self.horizontalLayout_send = None
        # self.textBrowser_send = None
    def create_item(self, Msg):
        # self.itemmodel = QStringListModel()
        # self.list = ()
        # self.itemmodel.setStringList(self.list)
        # self.listView.setModel(self.itemmodel)

        # 读取属性
        Msg_type = Msg['type']
        Msg_content = Msg['content']
        # 总Widget
        wight = QWidget()
        # 总体横向布局
        layout_main = QHBoxLayout()
        if Msg_type == 0:
            # 头像显示
            # map_l = QLabel()
            # map_l.setFixedSize(40, 25)
            # maps = QPixmap(machine_photo).scaled(40, 25)
            # map_l.setPixmap(maps)

            spacerItem = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
            layout_main.addItem(spacerItem)
            # content的纵向布局
            layout_content = QVBoxLayout()
            label_content = QLabel(Msg_content)
            label_content.setTextInteractionFlags(Qt.TextSelectableByMouse)
            label_content.setStyleSheet("background-color: rgb(122,211,255);border-radius: 4px;")
            layout_content.addWidget(label_content)
        else:
            # content的纵向布局
            layout_content = QVBoxLayout()
            label_content = QLabel(Msg_content)
            label_content.setTextInteractionFlags(Qt.TextSelectableByMouse)
            label_content.setStyleSheet("background-color: rgb(122,211,255);border-radius: 4px;")
            layout_content.addWidget(label_content)

            spacerItem = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
            layout_main.addItem(spacerItem)
            # 头像显示
            # map_l = QLabel()
            # map_l.setFixedSize(40, 25)
            # maps = QPixmap(machine_photo).scaled(40, 25)
            # map_l.setPixmap(maps)
        layout_main.addLayout(layout_content)
        wight.setLayout(layout_main)  # 布局给wight
        return wight  # 返回wight

    # def emitAddBubbleSignal(self):
    #     text = str()
    #     # 不把add_content分开的话如何传入add，分开的话如何检测客户端发来了信息
    #     self.AddBubbleSignal.emit(text)

    def addList(self, Msg):
        # count = self.itemmodel.rowCount()  # 获取数据存储数据条数
        # selectindex = self.m_ListView.currentIndex()  # 获取当前选择的数据项位置
        # if selectindex.isValid():
        #     pos = selectindex.row()  # 获取当前选择的数据项位置的顺序索引
        # else:
        #     pos = count  # 当前没有选择则插入到最后位置
        # self.itemmodel.insertRow(pos)   # 执行插入位置元素扩充
        # index = self.itemmodel.index(pos, 0)  # 获取插入位置的元素项
        # stritem = f'item{pos+1}'  # 设置插入内容
        # self.itemmodel.setData(index, stritem, Qt.DisplayRole)  # 将内容更新到插入位置

        item = QListWidgetItem()  # 创建QListWidgetItem对象
        item.setSizeHint(QSize(300, 50))  # 设置QListWidgetItem大小,50这个高度应可变
        widget = self.create_item(Msg)  # 调用上面的函数获取对应
        self.listWidget_bubble.addItem(item)  # 添加item
        self.listWidget_bubble.setItemWidget(item, widget)  # 为item设置widget
        print("触发发送3：" + Msg['content'])

    # def addBubbleR(self, content):
    #     self.frame_bubbleReceive = QtWidgets.QFrame(self.scrollAreaWidgetContents)
    #     self.frame_bubbleReceive.setFrameShape(QtWidgets.QFrame.StyledPanel)
    #     self.frame_bubbleReceive.setFrameShadow(QtWidgets.QFrame.Raised)
    #     self.frame_bubbleReceive.setObjectName("frame_bubbleReceive")
    #     self.horizontalLayout_receive = QtWidgets.QHBoxLayout(self.frame_bubbleReceive)
    #     self.horizontalLayout_receive.setObjectName("horizontalLayout_receive")
    #     self.textBrowser_receive = QtWidgets.QTextBrowser(self.frame_bubbleReceive)
    #     sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Fixed, QtWidgets.QSizePolicy.Fixed)
    #     sizePolicy.setHorizontalStretch(0)
    #     sizePolicy.setVerticalStretch(0)
    #     sizePolicy.setHeightForWidth(self.textBrowser_receive.sizePolicy().hasHeightForWidth())
    #     self.textBrowser_receive.setSizePolicy(sizePolicy)
    #     self.textBrowser_receive.setMinimumSize(QtCore.QSize(200, 30))
    #     self.textBrowser_receive.setObjectName("textBrowser_receive")
    #     self.horizontalLayout_receive.addWidget(self.textBrowser_receive)
    #     spacerItem1 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
    #     self.horizontalLayout_receive.addItem(spacerItem1)
    #     self.verticalLayout_scroll.addWidget(self.frame_bubbleReceive)
    #
    # def addBubbleS(self, content):
    #     self.frame_bubbleSend = QtWidgets.QFrame(self.scrollAreaWidgetContents)
    #     self.frame_bubbleSend.setFrameShape(QtWidgets.QFrame.StyledPanel)
    #     self.frame_bubbleSend.setFrameShadow(QtWidgets.QFrame.Raised)
    #     self.frame_bubbleSend.setObjectName("frame_bubbleSend")
    #     self.horizontalLayout_send = QtWidgets.QHBoxLayout(self.frame_bubbleSend)
    #     self.horizontalLayout_send.setObjectName("horizontalLayout_send")
    #     spacerItem2 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
    #     self.horizontalLayout_send.addItem(spacerItem2)
    #     self.textBrowser_send = QtWidgets.QTextBrowser(self.frame_bubbleSend)
    #     self.textBrowser_send.setObjectName("textBrowser_send")
    #     self.horizontalLayout_send.addWidget(self.textBrowser_send)
    #     self.verticalLayout_scroll.addWidget(self.frame_bubbleSend)
