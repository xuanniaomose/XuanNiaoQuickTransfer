import re


def check(send_text):
    regex = re.compile(r"@FilMark@(.*)@FilMarkEnd@", re.M)
    if re.match(regex, send_text):
        mark = 1
    else:
        mark = 0
    print("mark:"+str(mark))
    return mark


def fpath(path):
    path = path.replace("@FilMark@", "")
    path = path.replace("@FilMarkEnd@", "")
    return path


def name(path):
    name = re.search(r"[^/]+(?!.*/)", path).group(0)
    return name
