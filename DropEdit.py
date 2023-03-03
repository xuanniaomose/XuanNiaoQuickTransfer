from PyQt6 import QtWidgets


class DropEdit(QtWidgets.QLineEdit):   # subclass
    def __init__(self, parent=None):
        super(DropEdit, self).__init__(parent)
        self.setDragEnabled(True)
        self.setAcceptDrops(True)

    def dragEnterEvent(self, event):
        print("dragEnterEvent:")
        if event.mimeData().hasUrls():
            event.accept()   # must accept the dragEnterEvent or else the dropEvent can't occur !!!
        else:
            event.ignore()

    def dropEvent(self, event):

        if event.mimeData().hasUrls():   # if file or link is dropped
            urlcount = len(event.mimeData().urls())  # count number of drops
            url = event.mimeData().urls()[0]   # get first url
            self.setText(url.toString())   # assign first url to edit line
            # event.accept()  # doesnt appear to be needed


class testDialog(QtWidgets.QDialog):
    def __init__(self, parent=None):
        super(testDialog, self).__init__(parent)
