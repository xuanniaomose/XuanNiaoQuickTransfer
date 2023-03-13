import sys
from PyQt5 import QtCore, QtWidgets, QtGui
from PyQt5.QtWidgets import QApplication, QMainWindow


class Ui_XuanNiaoTR(QMainWindow):
    def __init__(self):
        super().__init__()
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
        self.frame_chart = None
        self.layout_chart = None
        self.scrollArea = None
        self.scrollAreaWidgetContents = None
        self.horizontalLayout = None
        self.browser_chart = None
        self.verticalScrollBar = None
        self.frame_medium = None
        self.layout_medium = None
        self.label_ipv4 = None
        self.lineEdit_ipv4 = None
        self.label_port = None
        self.lineEdit_port = None
        self.checkBox_connect = None
        self.entry_send = None
        self.frame_bottom = None
        self.layout_bottom = None
        self.pushButton_send = None

        self.setupUi(self)

    def setupUi(self, XuanNiaoTR):
        XuanNiaoTR.setObjectName("XuanNiaoTR")
        XuanNiaoTR.resize(420, 600)
        self.widget_central = QtWidgets.QWidget(XuanNiaoTR)
        self.widget_central.setStyleSheet("")
        self.widget_central.setObjectName("widget_central")
        self.verticalLayout_central = QtWidgets.QVBoxLayout(self.widget_central)
        self.verticalLayout_central.setContentsMargins(0, 0, 0, 0)
        self.verticalLayout_central.setSpacing(0)
        self.verticalLayout_central.setObjectName("verticalLayout_central")
        self.frame_title = QtWidgets.QFrame(self.widget_central)
        self.frame_title.setStyleSheet("background-color: rgba(122,211,255, 180)")
        self.frame_title.setObjectName("frame_title")
        self.layout_title = QtWidgets.QHBoxLayout(self.frame_title)
        self.layout_title.setObjectName("layout_title")
        self.label_title = QtWidgets.QLabel(self.frame_title)
        sizePolicy = QtWidgets.QSizePolicy(
            QtWidgets.QSizePolicy.Policy.Preferred, QtWidgets.QSizePolicy.Policy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.label_title.sizePolicy().hasHeightForWidth())
        self.label_title.setSizePolicy(sizePolicy)
        font = QtGui.QFont()
        font.setPointSize(12)
        font.setBold(True)
        self.label_title.setFont(font)
        self.label_title.setStyleSheet("background-color: rgba(255,255,255, 0)")
        self.label_title.setObjectName("label_title")
        self.layout_title.addWidget(self.label_title)
        spacerItem = QtWidgets.QSpacerItem(
            40, 20, QtWidgets.QSizePolicy.Policy.Expanding, QtWidgets.QSizePolicy.Policy.Minimum)
        self.layout_title.addItem(spacerItem)
        self.pushButton_setting = QtWidgets.QPushButton(self.frame_title)
        self.pushButton_setting.setStyleSheet("background-color: rgba(0, 0, 0, 0)")
        self.pushButton_setting.setText("")
        icon = QtGui.QIcon()
        icon.addPixmap(QtGui.QPixmap("image/icon/icon_settings.png"), QtGui.QIcon.Mode.Normal, QtGui.QIcon.State.Off)
        self.pushButton_setting.setIcon(icon)
        self.pushButton_setting.setObjectName("pushButton_setting")
        self.layout_title.addWidget(self.pushButton_setting)
        self.pushButton_minimizeApp = QtWidgets.QPushButton(self.frame_title)
        self.pushButton_minimizeApp.setStyleSheet("background-color: rgba(0, 0, 0, 0)")
        self.pushButton_minimizeApp.setText("")
        icon1 = QtGui.QIcon()
        icon1.addPixmap(QtGui.QPixmap("image/icon/icon_minimize.png"), QtGui.QIcon.Mode.Normal, QtGui.QIcon.State.Off)
        self.pushButton_minimizeApp.setIcon(icon1)
        self.pushButton_minimizeApp.setObjectName("pushButton_minimizeApp")
        self.layout_title.addWidget(self.pushButton_minimizeApp)
        self.pushButton_closeApp = QtWidgets.QPushButton(self.frame_title)
        self.pushButton_closeApp.setStyleSheet("background-color: rgba(0, 0, 0, 0)")
        self.pushButton_closeApp.setText("")
        icon2 = QtGui.QIcon()
        icon2.addPixmap(
            QtGui.QPixmap("image/icon/icon_close.png"), QtGui.QIcon.Mode.Normal, QtGui.QIcon.State.Off)
        self.pushButton_closeApp.setIcon(icon2)
        self.pushButton_closeApp.setObjectName("pushButton_closeApp")
        self.layout_title.addWidget(self.pushButton_closeApp)
        self.verticalLayout_central.addWidget(self.frame_title)
        self.frame_main = QtWidgets.QFrame(self.widget_central)
        sizePolicy = QtWidgets.QSizePolicy(
            QtWidgets.QSizePolicy.Policy.Preferred, QtWidgets.QSizePolicy.Policy.Preferred)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.frame_main.sizePolicy().hasHeightForWidth())
        self.frame_main.setSizePolicy(sizePolicy)
        self.frame_main.setObjectName("frame_main")
        self.layout_main = QtWidgets.QHBoxLayout(self.frame_main)
        self.layout_main.setContentsMargins(0, 0, 0, 0)
        self.layout_main.setSpacing(0)
        self.layout_main.setObjectName("layout_main")
        self.frame_chart = QtWidgets.QFrame(self.frame_main)
        self.frame_chart.setObjectName("frame_chart")
        self.layout_chart = QtWidgets.QVBoxLayout(self.frame_chart)
        self.layout_chart.setContentsMargins(0, 0, 0, 0)
        self.layout_chart.setSpacing(0)
        self.layout_chart.setObjectName("layout_chart")
        self.scrollArea = QtWidgets.QScrollArea(self.frame_chart)
        self.scrollArea.setStyleSheet("border:none;")
        self.scrollArea.setWidgetResizable(True)
        self.scrollArea.setObjectName("scrollArea")
        self.scrollAreaWidgetContents = QtWidgets.QWidget()
        self.scrollAreaWidgetContents.setGeometry(QtCore.QRect(0, 0, 420, 341))
        self.scrollAreaWidgetContents.setStyleSheet("")
        self.scrollAreaWidgetContents.setObjectName("scrollAreaWidgetContents")
        self.horizontalLayout = QtWidgets.QHBoxLayout(self.scrollAreaWidgetContents)
        self.horizontalLayout.setContentsMargins(0, 0, 0, 0)
        self.horizontalLayout.setSpacing(0)
        self.horizontalLayout.setObjectName("horizontalLayout")
        self.browser_chart = QtWidgets.QTextBrowser(self.scrollAreaWidgetContents)
        self.browser_chart.setStyleSheet("")
        self.browser_chart.setObjectName("browser_chart")
        self.horizontalLayout.addWidget(self.browser_chart)
        self.verticalScrollBar = QtWidgets.QScrollBar(self.scrollAreaWidgetContents)
        self.verticalScrollBar.setAutoFillBackground(False)
        self.verticalScrollBar.setStyleSheet(
            "QScrollBar:vertical {\n"
            "    border: none;\n"
            "    background: rgb(240, 248, 255);\n"
            "    width: 12px;\n"
            "    margin: 10px 0 10px 0;\n"
            " }\n"
            "QScrollBar::handle:vertical {    \n"
            "    background: rgb(100,211,255);\n"
            "    min-height: 45px;\n"
            "    border-radius: 5px\n"
            " }\n"
            "QScrollBar::add-line:vertical {\n"
            "    border: none;\n"
            "    background: rgb(193,229,255);\n"
            "    height: 20px;\n"
            "    subcontrol-position: bottom;\n"
            "    subcontrol-origin: margin;\n"
            " }\n"
            "QScrollBar::sub-line:vertical {\n"
            "    border: none;\n"
            "    background: rgb(193,229,255);\n"
            "    height: 20px;\n"
            "    border-top-left-radius: 4px;\n"
            "    border-top-right-radius: 4px;\n"
            "    subcontrol-position: top;\n"
            "    subcontrol-origin: margin;\n"
            " }\n"
            "QScrollBar::up-arrow:vertical, \n"
            "QScrollBar::down-arrow:vertical {\n"
            "     background: none;\n"
            " }\n"
            "QScrollBar::add-page:vertical,\n"
            "QScrollBar::sub-page:vertical {\n"
            "     background: none;\n"
            " }")
        self.verticalScrollBar.setOrientation(QtCore.Qt.Orientation.Vertical)
        self.verticalScrollBar.setObjectName("verticalScrollBar")
        self.horizontalLayout.addWidget(self.verticalScrollBar)
        self.scrollArea.setWidget(self.scrollAreaWidgetContents)
        self.layout_chart.addWidget(self.scrollArea)
        self.frame_medium = QtWidgets.QFrame(self.frame_chart)
        self.frame_medium.setStyleSheet("background-color: rgba(122,211,255, 180)")
        self.frame_medium.setObjectName("frame_medium")
        self.layout_medium = QtWidgets.QHBoxLayout(self.frame_medium)
        self.layout_medium.setContentsMargins(-1, -1, 9, -1)
        self.layout_medium.setObjectName("layout_medium")
        self.label_ipv4 = QtWidgets.QLabel(self.frame_medium)
        sizePolicy = QtWidgets.QSizePolicy(
            QtWidgets.QSizePolicy.Policy.Preferred, QtWidgets.QSizePolicy.Policy.Preferred)
        sizePolicy.setHorizontalStretch(50)
        sizePolicy.setVerticalStretch(20)
        sizePolicy.setHeightForWidth(self.label_ipv4.sizePolicy().hasHeightForWidth())
        self.label_ipv4.setSizePolicy(sizePolicy)
        self.label_ipv4.setStyleSheet("background-color: rgba(255,255,255, 0)")
        self.label_ipv4.setObjectName("label_ipv4")
        self.layout_medium.addWidget(self.label_ipv4)
        self.lineEdit_ipv4 = QtWidgets.QLineEdit(self.frame_medium)
        sizePolicy = QtWidgets.QSizePolicy(
            QtWidgets.QSizePolicy.Policy.Expanding, QtWidgets.QSizePolicy.Policy.Fixed)
        sizePolicy.setHorizontalStretch(110)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.lineEdit_ipv4.sizePolicy().hasHeightForWidth())
        self.lineEdit_ipv4.setSizePolicy(sizePolicy)
        self.lineEdit_ipv4.setMinimumSize(QtCore.QSize(110, 20))
        self.lineEdit_ipv4.setStyleSheet("background-color: rgba(255,255,255, 240)")
        self.lineEdit_ipv4.setInputMask("")
        self.lineEdit_ipv4.setObjectName("lineEdit_ipv4")
        self.layout_medium.addWidget(self.lineEdit_ipv4)
        self.label_port = QtWidgets.QLabel(self.frame_medium)
        sizePolicy = QtWidgets.QSizePolicy(
            QtWidgets.QSizePolicy.Policy.Preferred, QtWidgets.QSizePolicy.Policy.Preferred)
        sizePolicy.setHorizontalStretch(50)
        sizePolicy.setVerticalStretch(20)
        sizePolicy.setHeightForWidth(self.label_port.sizePolicy().hasHeightForWidth())
        self.label_port.setSizePolicy(sizePolicy)
        self.label_port.setStyleSheet("background-color: rgba(255,255,255, 0)")
        self.label_port.setObjectName("label_port")
        self.layout_medium.addWidget(self.label_port)
        self.lineEdit_port = QtWidgets.QLineEdit(self.frame_medium)
        sizePolicy = QtWidgets.QSizePolicy(
            QtWidgets.QSizePolicy.Policy.Expanding, QtWidgets.QSizePolicy.Policy.Fixed)
        sizePolicy.setHorizontalStretch(45)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.lineEdit_port.sizePolicy().hasHeightForWidth())
        self.lineEdit_port.setSizePolicy(sizePolicy)
        self.lineEdit_port.setMinimumSize(QtCore.QSize(45, 20))
        self.lineEdit_port.setStyleSheet("background-color: rgba(255,255,255, 240)")
        self.lineEdit_port.setObjectName("lineEdit_port")
        self.layout_medium.addWidget(self.lineEdit_port)
        spacerItem1 = QtWidgets.QSpacerItem(
            40, 20, QtWidgets.QSizePolicy.Policy.Expanding, QtWidgets.QSizePolicy.Policy.Minimum)
        self.layout_medium.addItem(spacerItem1)
        self.checkBox_connect = QtWidgets.QCheckBox(self.frame_medium)
        self.checkBox_connect.setStyleSheet("background-color: rgba(255,255,255, 0)")
        self.checkBox_connect.setChecked(True)
        self.checkBox_connect.setTristate(True)
        self.checkBox_connect.setObjectName("checkBox_connect")
        self.layout_medium.addWidget(self.checkBox_connect)
        self.layout_chart.addWidget(self.frame_medium)
        self.entry_send = QtWidgets.QLineEdit(self.frame_chart)
        sizePolicy = QtWidgets.QSizePolicy(
            QtWidgets.QSizePolicy.Policy.Expanding, QtWidgets.QSizePolicy.Policy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.entry_send.sizePolicy().hasHeightForWidth())
        self.entry_send.setSizePolicy(sizePolicy)
        self.entry_send.setStyleSheet("border: none;")
        self.entry_send.setObjectName("entry_send")
        self.layout_chart.addWidget(self.entry_send)
        self.frame_bottom = QtWidgets.QFrame(self.frame_chart)
        self.frame_bottom.setStyleSheet("background-color: rgba(255,255,255, 255)")
        self.frame_bottom.setObjectName("frame_bottom")
        self.layout_bottom = QtWidgets.QHBoxLayout(self.frame_bottom)
        self.layout_bottom.setContentsMargins(0, 0, 0, 0)
        self.layout_bottom.setSpacing(0)
        self.layout_bottom.setObjectName("layout_bottom")
        spacerItem2 = QtWidgets.QSpacerItem(
            40, 20, QtWidgets.QSizePolicy.Policy.Expanding, QtWidgets.QSizePolicy.Policy.Minimum)
        self.layout_bottom.addItem(spacerItem2)
        self.pushButton_send = QtWidgets.QPushButton(self.frame_bottom)
        sizePolicy = QtWidgets.QSizePolicy(
            QtWidgets.QSizePolicy.Policy.Fixed, QtWidgets.QSizePolicy.Policy.Expanding)
        sizePolicy.setHorizontalStretch(0)
        sizePolicy.setVerticalStretch(0)
        sizePolicy.setHeightForWidth(self.pushButton_send.sizePolicy().hasHeightForWidth())
        self.pushButton_send.setSizePolicy(sizePolicy)
        self.pushButton_send.setMinimumSize(QtCore.QSize(35, 0))
        self.pushButton_send.setCursor(QtGui.QCursor(QtCore.Qt.CursorShape.ArrowCursor))
        self.pushButton_send.setStyleSheet(
            "QPushButton {    \n"
            "    background-position: left center;\n"
            "    background-repeat: no-repeat;\n"
            "    background-color: rgb(122,211,255);\n"
            "    border: none;\n"
            "    border-top-left-radius: 9px;\n"
            "    border-top-right-radius: 9px;\n"
            "    border-left: 15px solid transparent;\n"
            "    border-right: 15px solid transparent;\n"
            "    text-align: center;\n"
            "    padding-left: 15px;\n"
            "    padding-right: 15px\n"
            "}\n"
            "QPushButton:hover {\n"
            "    background-color: rgb(193,229,255);\n"
            "}\n"
            "QPushButton:pressed {    \n"
            "    background-color: rgb(245,245,255);\n"
            "    color: rgb(255, 255, 255);\n"
            "}")
        self.pushButton_send.setDefault(False)
        self.pushButton_send.setFlat(True)
        self.pushButton_send.setObjectName("pushButton_send")
        self.layout_bottom.addWidget(self.pushButton_send)
        self.layout_chart.addWidget(self.frame_bottom)
        self.layout_chart.setStretch(0, 21)
        self.layout_chart.setStretch(1, 1)
        self.layout_chart.setStretch(2, 9)
        self.layout_chart.setStretch(3, 2)
        self.layout_main.addWidget(self.frame_chart)
        self.layout_main.setStretch(0, 10)
        self.verticalLayout_central.addWidget(self.frame_main)
        self.verticalLayout_central.setStretch(0, 1)
        self.verticalLayout_central.setStretch(1, 24)
        XuanNiaoTR.setCentralWidget(self.widget_central)

        # self.setAcceptDrops(True)  # 支持拖入操作
        self.retranslateUi(XuanNiaoTR)
        QtCore.QMetaObject.connectSlotsByName(XuanNiaoTR)
        print("主界面创建完成")

    def retranslateUi(self, XuanNiaoTR):
        _translate = QtCore.QCoreApplication.translate
        XuanNiaoTR.setWindowTitle(_translate("XuanNiaoTR", "XuanNiaoTR"))
        self.label_title.setText(_translate("XuanNiaoTR", "玄鸟快传"))
        self.label_ipv4.setText(_translate("XuanNiaoTR", "电脑端ipv4:"))
        self.lineEdit_ipv4.setText(_translate("XuanNiaoTR", "192.168.0.0"))
        self.label_port.setText(_translate("XuanNiaoTR", "电脑端口号:"))
        self.lineEdit_port.setInputMask(_translate("XuanNiaoTR", "0000"))
        self.lineEdit_port.setText(_translate("XuanNiaoTR", "9999"))
        self.checkBox_connect.setText(_translate("XuanNiaoTR", "连接状态"))
        self.pushButton_send.setText(_translate("XuanNiaoTR", "发送"))


if __name__ == '__main__':
    app = QApplication(sys.argv)
    win = Ui_XuanNiaoTR()
    win.show()
    app.exit(app.exec())