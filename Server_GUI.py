import os
import sys

from PyQt5 import QtCore, QtWidgets, QtGui
from PyQt5.QtCore import Qt
from PyQt5.QtGui import QColor
from PyQt5.QtWidgets import QMainWindow, QApplication


class Ui_XuanNiaoTR(QMainWindow):
    def __init__(self):
        super().__init__()
        self.effect_shadow = None
        self.widget_central = None
        self.verticalLayout_central = None
        self.frame_title = None
        self.layout_title = None
        self.label_title = None
        self.pushButton_setting = None
        self.pushButton_minimizeApp = None
        self.pushButton_closeApp = None
        self.frame_main = None
        self.layout_main = None
        self.scrollArea = None
        self.scrollAreaWidgetContents = None
        self.stackedWidget = None
        self.page_bubble = None
        self.verticalLayout_bubble = None
        self.listWidget_bubble = None
        self.page_text = None
        self.verticalLayout_text = None
        self.textBrowser_chart = None
        self.frame_medium = None
        self.layout_medium = None
        self.label_ipv4 = None
        self.lineEdit_ipv4 = None
        self.label_port = None
        self.lineEdit_port = None
        self.checkBox_connect = None
        self.lineEdit_send = None
        self.frame_bottom = None
        self.horizontalLayout_bottom = None
        self.pushButton_send = None
        self.pushButtonStyle = (
            "QPushButton {background-color: rgba(122,211,255,0);border-radius: 4px;border: none;color:white}\n"
            "QPushButton:hover {background-color: rgb(193,229,255)}\n"
            "QPushButton:pressed {background-color: rgb(245,245,255);color: rgb(255, 255, 255)}")
        self.ScrollbarStyle = (
            "QScrollBar:vertical {border: none;background: rgb(240, 248, 255);width: 12px;margin: 10px 0 10px 0;}\n"
            "QScrollBar::handle:vertical {background: rgb(100,211,255);min-height: 50px;border-radius: 5px}\n"
            "QScrollBar::add-line:vertical {border: none;background: rgb(193,229,255);\n"
            "    height: 10px;subcontrol-position: bottom;subcontrol-origin: margin;}\n"
            "QScrollBar::sub-line:vertical {border: none;background: rgb(193,229,255);\n"
            "    height: 10px;subcontrol-position: top;subcontrol-origin: margin}\n"
            "QScrollBar::up-arrow:vertical, \n"
            "QScrollBar::down-arrow:vertical {background: none;}\n"
            "QScrollBar::add-page:vertical,QScrollBar::sub-page:vertical {background: none;}")
        self.setupUi(self)

    def setupUi(self, XuanNiaoTR):
        XuanNiaoTR.setObjectName("XuanNiaoTR")
        XuanNiaoTR.resize(425, 605)
        XuanNiaoTR.setWindowFlags(Qt.FramelessWindowHint)
        XuanNiaoTR.setAttribute(Qt.WA_TranslucentBackground)
        self.widget_central = QtWidgets.QWidget(XuanNiaoTR)
        self.widget_central.setAutoFillBackground(True)  # 让被包含窗口依然绘制背景
        self.widget_central.setAttribute(Qt.WA_TranslucentBackground)
        self.widget_central.setObjectName("widget_central")
        self.verticalLayout_central = QtWidgets.QHBoxLayout(self.widget_central)
        self.verticalLayout_central.setObjectName("verticalLayout_central")
        self.frame_main = QtWidgets.QFrame(self.widget_central)
        self.effect_shadow = QtWidgets.QGraphicsDropShadowEffect(self)
        self.effect_shadow.setOffset(0, 0)  # 偏移
        self.effect_shadow.setBlurRadius(10)  # 阴影半径
        self.effect_shadow.setColor(QColor("#555555"))  # 阴影颜色
        self.widget_central.setGraphicsEffect(self.effect_shadow)  # 将设置套用到widget窗口中
        self.frame_main = QtWidgets.QFrame(self.widget_central)
        self.frame_main.setStyleSheet("background-color: rgb(255,255,255)")
        self.frame_main.setObjectName("frame_main")
        self.layout_main = QtWidgets.QVBoxLayout(self.frame_main)
        self.layout_main.setContentsMargins(0, 0, 0, 0)
        self.layout_main.setSpacing(0)
        self.layout_main.setObjectName("layout_main")
        self.frame_title = QtWidgets.QFrame(self.frame_main)
        self.frame_title.setStyleSheet("background-color: rgba(122,211,255,200)")
        self.frame_title.setObjectName("frame_title")
        self.frame_title.setFixedHeight(50)
        self.layout_title = QtWidgets.QHBoxLayout(self.frame_title)
        self.layout_title.setContentsMargins(-1, 7, -1, 7)
        self.layout_title.setObjectName("layout_title")
        self.label_title = QtWidgets.QLabel(self.frame_title)
        font = QtGui.QFont()
        font.setPointSize(12)
        font.setBold(True)
        font.setWeight(75)
        self.label_title.setFont(font)
        self.label_title.setStyleSheet("background-color: rgba(255,255,255,0);color:white")
        self.label_title.setObjectName("label_title")
        self.layout_title.addWidget(self.label_title)
        spacerItem = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
        self.layout_title.addItem(spacerItem)
        self.pushButton_setting = QtWidgets.QPushButton(self.frame_title)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Minimum, QtWidgets.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.pushButton_setting.sizePolicy().hasHeightForWidth())
        self.pushButton_setting.setSizePolicy(sizePolicy)
        self.pushButton_setting.setMinimumSize(QtCore.QSize(35, 0))
        self.pushButton_setting.setStyleSheet(self.pushButtonStyle)
        self.pushButton_setting.setText("")
        icon = QtGui.QIcon()
        icon.addPixmap(QtGui.QPixmap(self.get_resource_path("icon/icon_setting.png")))
        self.pushButton_setting.setIcon(icon)
        self.pushButton_setting.setIconSize(QtCore.QSize(20, 20))
        self.pushButton_setting.setObjectName("pushButton_setting")
        self.layout_title.addWidget(self.pushButton_setting)
        self.pushButton_minimizeApp = QtWidgets.QPushButton(self.frame_title)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Minimum, QtWidgets.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.pushButton_minimizeApp.sizePolicy().hasHeightForWidth())
        self.pushButton_minimizeApp.setSizePolicy(sizePolicy)
        self.pushButton_minimizeApp.setMinimumSize(QtCore.QSize(35, 0))
        self.pushButton_minimizeApp.setStyleSheet(self.pushButtonStyle)
        self.pushButton_minimizeApp.setText("")
        icon1 = QtGui.QIcon()
        icon1.addPixmap(QtGui.QPixmap(self.get_resource_path("icon/icon_minimize.png")))
        self.pushButton_minimizeApp.setIcon(icon1)
        self.pushButton_minimizeApp.setIconSize(QtCore.QSize(20, 20))
        self.pushButton_minimizeApp.setObjectName("pushButton_minimizeApp")
        self.layout_title.addWidget(self.pushButton_minimizeApp)
        self.pushButton_closeApp = QtWidgets.QPushButton(self.frame_title)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Minimum, QtWidgets.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.pushButton_closeApp.sizePolicy().hasHeightForWidth())
        self.pushButton_closeApp.setSizePolicy(sizePolicy)
        self.pushButton_closeApp.setMinimumSize(QtCore.QSize(35, 0))
        self.pushButton_closeApp.setStyleSheet(self.pushButtonStyle)
        self.pushButton_closeApp.setText("")
        icon2 = QtGui.QIcon()
        icon2.addPixmap(QtGui.QPixmap(self.get_resource_path("icon/icon_close.png")))
        self.pushButton_closeApp.setIcon(icon2)
        self.pushButton_closeApp.setIconSize(QtCore.QSize(20, 20))
        self.pushButton_closeApp.setObjectName("pushButton_closeApp")
        self.layout_title.addWidget(self.pushButton_closeApp)
        self.layout_main.addWidget(self.frame_title)
        self.stackedWidget = QtWidgets.QStackedWidget(self.frame_main)
        self.stackedWidget.setObjectName("stackedWidget")
        self.page_bubble = QtWidgets.QWidget()
        self.page_bubble.setObjectName("page_bubble")
        self.verticalLayout_bubble = QtWidgets.QVBoxLayout(self.page_bubble)
        self.verticalLayout_bubble.setContentsMargins(0, 0, 0, 0)
        self.verticalLayout_bubble.setObjectName("verticalLayout_bubble")
        self.listWidget_bubble = QtWidgets.QListWidget(self.page_bubble)
        self.listWidget_bubble.setStyleSheet("border:none")
        self.listWidget_bubble.setObjectName("listView_bubble")
        self.listWidget_bubble.verticalScrollBar().setStyleSheet(self.ScrollbarStyle)
        self.verticalLayout_bubble.addWidget(self.listWidget_bubble)
        self.stackedWidget.addWidget(self.page_bubble)
        self.page_text = QtWidgets.QWidget()
        self.page_text.setObjectName("page_text")
        self.verticalLayout_text = QtWidgets.QVBoxLayout(self.page_text)
        self.verticalLayout_text.setContentsMargins(0, 0, 0, 0)
        self.verticalLayout_text.setObjectName("verticalLayout_text")
        self.textBrowser_chart = QtWidgets.QTextBrowser(self.page_text)
        self.textBrowser_chart.setStyleSheet("border: none;")
        self.textBrowser_chart.setContextMenuPolicy(Qt.ActionsContextMenu)
        self.textBrowser_chart.setObjectName("textBrowser_chart")
        self.textBrowser_chart.verticalScrollBar().setStyleSheet(self.ScrollbarStyle)
        self.verticalLayout_text.addWidget(self.textBrowser_chart)
        self.stackedWidget.addWidget(self.page_text)
        self.layout_main.addWidget(self.stackedWidget)
        self.frame_medium = QtWidgets.QFrame(self.frame_main)
        self.frame_medium.setStyleSheet("background-color: rgba(122,211,255,230)")
        self.frame_medium.setObjectName("frame_medium")
        self.layout_medium = QtWidgets.QHBoxLayout(self.frame_medium)
        self.layout_medium.setObjectName("layout_medium")
        self.label_ipv4 = QtWidgets.QLabel(self.frame_medium)
        self.label_ipv4.setStyleSheet("background-color: rgba(255,255,255,0);color:white")
        self.label_ipv4.setObjectName("label_ipv4")
        self.layout_medium.addWidget(self.label_ipv4)
        self.lineEdit_ipv4 = QtWidgets.QLineEdit(self.frame_medium)
        self.lineEdit_ipv4.setStyleSheet("background-color: rgb(255,255,255);border: none;")
        self.lineEdit_ipv4.setObjectName("lineEdit_ipv4")
        self.lineEdit_ipv4.setFixedWidth(100)
        self.layout_medium.addWidget(self.lineEdit_ipv4)
        self.label_port = QtWidgets.QLabel(self.frame_medium)
        self.label_port.setStyleSheet("background-color: rgba(255,255,255,0);color:white")
        self.label_port.setObjectName("label_port")
        self.layout_medium.addWidget(self.label_port)
        self.lineEdit_port = QtWidgets.QLineEdit(self.frame_medium)
        self.lineEdit_port.setStyleSheet("background-color: rgb(255,255,255);border: none;")
        self.lineEdit_port.setObjectName("lineEdit_port")
        self.lineEdit_port.setFixedWidth(50)
        self.layout_medium.addWidget(self.lineEdit_port)
        spacerItem1 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
        self.layout_medium.addItem(spacerItem1)
        self.checkBox_connect = QtWidgets.QCheckBox(self.frame_medium)
        self.checkBox_connect.setStyleSheet("background-color: rgba(255,255,255,0);color:white")
        self.checkBox_connect.setChecked(True)
        self.checkBox_connect.setObjectName("checkBox_connect")
        self.layout_medium.addWidget(self.checkBox_connect)
        self.layout_main.addWidget(self.frame_medium)
        self.lineEdit_send = QtWidgets.QLineEdit(self.frame_main)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.lineEdit_send.sizePolicy().hasHeightForWidth())
        self.lineEdit_send.setSizePolicy(sizePolicy)
        self.lineEdit_send.setStyleSheet("border: none;")
        self.lineEdit_send.setObjectName("lineEdit_send")
        self.layout_main.addWidget(self.lineEdit_send)
        self.frame_bottom = QtWidgets.QFrame(self.frame_main)
        self.frame_bottom.setObjectName("frame_bottom")
        self.horizontalLayout_bottom = QtWidgets.QHBoxLayout(self.frame_bottom)
        self.horizontalLayout_bottom.setContentsMargins(-1, 0, -1, -1)
        self.horizontalLayout_bottom.setObjectName("horizontalLayout_bottom")
        spacerItem2 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
        self.horizontalLayout_bottom.addItem(spacerItem2)
        self.pushButton_send = QtWidgets.QPushButton(self.frame_bottom)
        sizePolicy = QtWidgets.QSizePolicy(QtWidgets.QSizePolicy.Minimum, QtWidgets.QSizePolicy.Minimum)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.pushButton_send.sizePolicy().hasHeightForWidth())
        self.pushButton_send.setSizePolicy(sizePolicy)
        self.pushButton_send.setMinimumSize(QtCore.QSize(75, 20))
        self.pushButton_send.setStyleSheet(
            "QPushButton {background-color: rgb(122,211,255);border-radius: 4px;border: none;color:white}\n"
            "QPushButton:hover {background-color: rgb(193,229,255)}\n"
            "QPushButton:pressed {background-color: rgb(245,245,255);color: rgb(255, 255, 255)}")
        self.pushButton_send.setObjectName("pushButton_send")
        self.horizontalLayout_bottom.addWidget(self.pushButton_send)
        self.layout_main.addWidget(self.frame_bottom)
        self.layout_main.setStretch(0, 5)
        self.layout_main.setStretch(1, 37)
        self.layout_main.setStretch(2, 2)
        self.layout_main.setStretch(3, 12)
        self.layout_main.setStretch(4, 4)
        self.verticalLayout_central.addWidget(self.frame_main)
        XuanNiaoTR.setCentralWidget(self.widget_central)

        self.retranslateUi(XuanNiaoTR)
        self.stackedWidget.setCurrentIndex(0)
        QtCore.QMetaObject.connectSlotsByName(XuanNiaoTR)
        print("主界面创建完成")

    def retranslateUi(self, XuanNiaoTR):
        _translate = QtCore.QCoreApplication.translate
        XuanNiaoTR.setWindowTitle(_translate("XuanNiaoTR", "XuanNiaoTR"))
        self.label_title.setText(_translate("XuanNiaoTR", "玄鸟快传"))
        self.label_ipv4.setText(_translate("XuanNiaoTR", "电脑端ipv4:"))
        self.label_port.setText(_translate("XuanNiaoTR", "电脑端口号:"))
        self.lineEdit_port.setInputMask(_translate("XuanNiaoTR", "0000"))
        self.lineEdit_port.setText(_translate("XuanNiaoTR", "9999"))
        self.checkBox_connect.setText(_translate("XuanNiaoTR", "连接状态"))
        self.pushButton_send.setText(_translate("XuanNiaoTR", "发送"))

    def get_resource_path(self, relative_path):
        if hasattr(sys, '_MEIPASS'):
            return os.path.join(sys._MEIPASS, relative_path)
        return os.path.join(os.path.abspath("."), relative_path)


# if __name__ == '__main__':
#     app = QApplication(sys.argv)
#     win = Ui_XuanNiaoTR()
#     win.show()
#     app.exit(app.exec())
