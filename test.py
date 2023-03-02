import re

receive_buffer = " \n@FileMark@\n test文件.txt这是测试文件\nthis is a test file\n2023.3.1"
msg_type = re.search(r"@\w{4}Mark@", receive_buffer, re.MULTILINE)
print(msg_type.group(0))
