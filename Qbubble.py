from PyQt5.QtCore import QRect, QPointF, QRectF
from PyQt5.QtGui import QBrush, QLinearGradient, QColor, QPen, QTextOption, QFontMetrics
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QApplication

window_height = 10
begin_width_spacing = 20
begin_height_spacing = 16
icon_width = 40
icon_height = 40
text_width_spacing = 12
text_height_spacing = 12
triangle_width = 6
triangle_height = 10
triangle_height_spacing = 10
text_min_width = 0
min_width = 0
text_max_width = 0
real_width = 0
text_height = 0


class MyWidget(QtWidgets.QWidget):
    def __init__(self, parent=None, data='', type_mess=0):
        super(MyWidget, self).__init__(parent)
        self.setObjectName('myWidget')
        self.user_chat_content = data

    # 重写paintEvent 否则不能使用样式表定义外观
    def paintEvent(self, event):
        # 此函数用于初始化气泡边框的大小，决定于字数大小
        self.init_data()
        global text_min_width, min_width, text_max_width, real_width, text_height, window_height
        # 初始化QPainter对象，一支画笔
        painter = QtGui.QPainter(self)
        painter.setRenderHints(QtGui.QPainter.Antialiasing | QtGui.QPainter.SmoothPixmapTransform)
        # 写入文字
        # font = QtGui.QFont()
        # font.setFamily("实体")
        # font.setPointSize(12)
        # painter.setFont(font)

        # 文本框的背景颜色需与气泡框颜色一致
        self.textEdit.setStyleSheet("background-color: rgb(151, 220, 227);\n"
                                    "border-width:0;\n"
                                    "border-style:outset")
        # 记住必须设定为只可读
        self.textEdit.setReadOnly(True)

        # 画头像
        icon_rect = QRect(begin_width_spacing, begin_height_spacing, icon_width, icon_height)
        painter.setPen(QtCore.Qt.NoPen)
        painter.setBrush(QBrush(QtCore.Qt.gray))
        painter.drawPixmap(icon_rect, QtGui.QPixmap(r"D:\sucai\用户1.png"))
        # 画框架
        bubbleRect = QRect(begin_width_spacing + icon_width, begin_height_spacing,
                           triangle_width + text_width_spacing + text_max_width + text_width_spacing,
                           text_height_spacing + text_height + text_height_spacing)
        # 建立textEdit写入文字
        font_2 = QtGui.QFont()
        font_2.setFamily("宋体")
        font_2.setPointSize(11)
        self.textEdit.setGeometry(QtCore.QRect(bubbleRect.x() + triangle_width + text_width_spacing - 2,
                                               bubbleRect.y() + text_height_spacing - 2, text_max_width + 8,
                                               text_height + 12))
        self.textEdit.setFont(font_2)

        painter.setPen(QtCore.Qt.NoPen)
        painter.setBrush(QBrush(QColor(180, 180, 180)))
        painter.drawRoundedRect(bubbleRect.x() + triangle_width, bubbleRect.y(), bubbleRect.width() - triangle_width,
                                bubbleRect.height(), 18, 18)
        linearGradient = QLinearGradient(QPointF(bubbleRect.x() + triangle_width + 1, bubbleRect.y() + 1),
                                         QPointF(bubbleRect.x() + bubbleRect.width() - 1,
                                                 bubbleRect.y() + bubbleRect.height() - 1))
        linearGradient.setColorAt(0, QColor(151, 220, 227))
        linearGradient.setColorAt(0.25, QColor(151, 220, 227))
        linearGradient.setColorAt(0.5, QColor(151, 220, 227))
        linearGradient.setColorAt(0.75, QColor(151, 220, 227))
        linearGradient.setColorAt(1, QColor(151, 220, 227))

        painter.setBrush(linearGradient)
        painter.drawRoundedRect(bubbleRect.x() + triangle_width + 1, bubbleRect.y() + 1,
                                bubbleRect.width() - triangle_width - 2, bubbleRect.height() - 2, 18, 18)
        painter.setPen(QPen(QColor(244, 164, 96)))
        painter.drawPolygon(QPointF(bubbleRect.x(), bubbleRect.y() + triangle_height_spacing - 4),
                            QPointF(bubbleRect.x() + triangle_width + 1, bubbleRect.y() + triangle_height_spacing),
                            QPointF(bubbleRect.x() + 6 + 1, bubbleRect.y() + 10 + 10))

        painter.setPen(QPen(QColor(180, 180, 180)))
        painter.drawLine(QPointF(bubbleRect.x(), bubbleRect.y() + 10 - 4),
                         QPointF(bubbleRect.x() + 6, bubbleRect.y() + 10))
        painter.drawLine(QPointF(bubbleRect.x(), bubbleRect.y() + 10 - 4),
                         QPointF(bubbleRect.x() + 6, bubbleRect.y() + 10 + 10))
        # 画文字
        penText = QPen()
        penText.setColor(QColor(56, 56, 56))
        painter.setPen(penText)
        option = QTextOption(QtCore.Qt.AlignLeft | QtCore.Qt.AlignVCenter)
        option.setWrapMode(QTextOption.WrapAtWordBoundaryOrAnywhere)
        painter.drawText(
            QRectF(bubbleRect.x() + triangle_width + text_width_spacing, bubbleRect.y() + text_height_spacing,
                   text_max_width, text_height), self.user_chat_content, option)
