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
        # self.item_model = QStringListModel()
        # self.list = ()
        # self.item_model.setStringList(self.list)
        # self.listView.setModel(self.item_model)

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
        # count = self.item_model.rowCount()  # 获取数据存储数据条数
        # select_index = self.m_ListView.currentIndex()  # 获取当前选择的数据项位置
        # if select_index.isValid():
        #     pos = select_index.row()  # 获取当前选择的数据项位置的顺序索引
        # else:
        #     pos = count  # 当前没有选择则插入到最后位置
        # self.item_model.insertRow(pos)   # 执行插入位置元素扩充
        # index = self.item_model.index(pos, 0)  # 获取插入位置的元素项
        # str_item = f'item{pos+1}'  # 设置插入内容
        # self.item_model.setData(index, str_item, Qt.DisplayRole)  # 将内容更新到插入位置

        # 添加bubble部分
        item = QListWidgetItem()  # 创建QListWidgetItem对象
        item.setSizeHint(QSize(300, 50))  # 设置QListWidgetItem大小,50这个高度应可变
        self.listWidget_bubble.addItem(item)  # 添加item
        widget = self.create_item(Msg)  # 调用上面的函数创建widget
        self.listWidget_bubble.setItemWidget(item, widget)  # 为item设置widget
