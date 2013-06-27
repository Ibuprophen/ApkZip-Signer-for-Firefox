#!/usr/bin/python3

#  Copyright (C) 2012 Hai Bison
#
#  See the file LICENSE at the root directory of this project for copying
#  permission.

'''
This tool parses `messages.properties` and generates all strings to their
respective IDs, which can be put into class R.string.
'''

import re

SOURCE_FILE = '../code/src/group/pals/desktop/app/apksigner/i18n/messages.properties'

# PARSE SOURCE FILE

# Should start from 1...
count = 1

with open(SOURCE_FILE, 'r') as f:
    for line in f:
        line = line.strip()
        if not line.startswith('#') and len(line) > 0:
            print('public static final int {} = 0x{:08x};'\
                   .format(re.sub(r'(?s)=.*', '', line).strip(), count))
            count += 1
