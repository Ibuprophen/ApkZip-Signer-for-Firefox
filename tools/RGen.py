#!/usr/bin/python3

#  Copyright (C) 2012 Hai Bison
#
#  See the file LICENSE at the root directory of this project for copying
#  permission.

'''
This tool parses `messages.properties` to generates all strings to their
respective IDs, then writes those to `R.java`.
'''

import os
import os.path
import re
import sys

FILE_SOURCE = os.sep.join([os.path.dirname(os.path.dirname(os.path.abspath(sys.argv[0]))),
                           'code','src','group','pals','desktop','app',
                           'apksigner','i18n','messages_en.properties'])
FILE_R = os.sep.join([os.path.dirname(os.path.dirname(os.path.abspath(sys.argv[0]))),
                      'code','src','group','pals','desktop','app',
                      'apksigner','i18n','R.java'])

# PARSE SOURCE FILE

class Parser:
    '''
    Parses ``FILE_SOURCE`` and generates ``FILE_R``.
    '''

    def __init__(self):
        print(' > Open "{}"...'.format(os.path.basename(FILE_R)))
        with open(FILE_R, 'w') as target:
            # Write header
            self.write_header(target)

            # Parse
            print(' > Open "{}"...'.format(os.path.basename(FILE_SOURCE)))
            with open(FILE_SOURCE, 'r') as source:
                # Should start from 1...
                count = 1
                for line in source:
                    line = line.strip()
                    if line and not line.startswith('#'):
                        target.write(
                            '{:8}public static final int {} = 0x{:08x};\n'\
                            .format('', re.sub(r'(?s)=.*', '', line).strip(), count))
                        count += 1
                        #.if
                    #.for
                #.with

            # Write footer
            self.write_footer(target)

            #.with

        print(' > DONE')

        #.__init__()

    def write_header(self, target):
        '''
        Write the header to ``target``.
        '''

        target.write('\n'.join([
            '/*',
            ' *    Copyright (C) 2012 Hai Bison',
            ' *',
            ' *    See the file LICENSE at the root directory of this project for copying',
            ' *    permission.',
            ' */',
            '',
            'package group.pals.desktop.app.apksigner.i18n;',
            '',
            '/**',
            ' * The resources.',
            ' *',
            ' * @author Hai Bison',
            ' * @since v1.6.5 beta',
            ' */',
            'public class R {',
            '',
            '    /**',
            '     * The string resources.',
            '     * <p>',
            '     * This class is generated automatically, you shouldn\'t rely on the values',
            '     * for your works.',
            '     * </p>',
            '     *',
            '     * @author Hai Bison',
            '     * @since v1.6.5 beta',
            '     */',
            '    public static class string {',
            '',
            '']))

        #.write_header()

    def write_footer(self, target):
        '''
        Write the footer to ``target``.
        '''

        target.write('\n'.join([
            '',
            '    }// string',
            '',
            '}']))

        #.write_footer()

    #.Parser

if __name__ == '__main__':
    Parser()