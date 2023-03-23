from PyQt5.QtCore import QEvent, QObject


class QEditDropHandler(QObject):
    def eventFilter(self, obj, event):
        """
        处理窗体内出现的事件，如果有需要则自行添加if判断语句；
        目前已经实现将拖到控件上文件的路径设置为控件的显示文本；
        """
        if event.type() == QEvent.Type.DragEnter:
            event.accept()
        if event.type() == QEvent.Type.Drop:
            md = event.mimeData()
            if md.hasUrls():
                # 此处md.urls()的返回值为拖入文件的file路径列表，即支持多文件同时拖入；
                # 此处默认读取第一个文件的路径进行处理，可按照个人需求进行相应的修改
                url = md.urls()[0]
                obj.setText("@FilMark@" + url.toLocalFile() + "@FilMarkEnd@")
                return True
        return super().eventFilter(obj, event)
