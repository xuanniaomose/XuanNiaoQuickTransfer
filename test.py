import re

receive_buffer = " \n@FileMark@\n test文件.txt这是测试文件\nthis is a test file\n2023.3.1"
msg_type = re.findall(r"@(\w*)Mark@", receive_buffer, re.MULTILINE)[0]
print(msg_type)
