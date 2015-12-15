#!/usr/bin/env python3
import os
from subprocess import call
from glob import glob
import re
from argparse import ArgumentParser
import sys
try:
    import progressbar
    def create_progressbar():
        return progressbar.ProgressBar(widgets=[
            ' [', progressbar.Timer(), '] ',
            progressbar.Bar(),
            ' (', progressbar.ETA(), ') ',
        ], fd=sys.stderr, redirect_stderr=True)
except:
    def create_progressbar():
        def custom_count(a):
            total = len(a)
            for idx, element in enumerate(a):
                sys.stderr.write('{count} / {total}'.format(count=idx + 1, total=total) + '\n')
                sys.stderr.flush()
                yield element
        return custom_count
from pprint import pprint
RESULTS_FILE_FORMAT = r'{playerA}-{playerB}.txt'
CMD_FORMAT = r'java -cp "{java_paths_str}" {main_path}'
MAIN_PATH = r'gr.auth.ee.dsproject.proximity.board.MainPlatform'
MESSAGE_FORMAT='''Running for: {n_iters} iterations
using players: {players}
at: {target_dir}
with jar: {jar_lib}
with bin: {bin_path}
'''

def check_output(filename):
    with open(filename) as f:
        s = f.read()
    return [int(x) for x in re.search(r'(\d{1,4})-(\d{1,4})', s).groups()]

def main():
    parser = ArgumentParser()
    parser.add_argument("-p", "--players", action="store", dest="players", default="Heuristic-Heuristic")
    parser.add_argument("-n", "--number-of-iterations", action="store", dest="n_iters", default=20, type=int)
    parser.add_argument("-d", "--dir", action="store", dest="target_dir", default=os.getcwd())
    parser.add_argument("-j", "--jar", action="store", dest="jar_lib", default="runnable_lib")
    parser.add_argument("-b", "--bin", action="store", dest="bin_path", default="bin")
    parser.add_argument("-q", "--quiet", action="store_false", dest="verbose", default=False)
    parser.add_argument("-v", "--verbose", action="store_true", dest="verbose")
    options = parser.parse_args()

    jar_lib = os.path.join(options.target_dir, options.jar_lib, '*')
    java_paths = glob(jar_lib) + [os.path.join(options.target_dir, options.bin_path)]
    java_paths_str = os.path.pathsep.join(java_paths)
    cmd = CMD_FORMAT.format(java_paths_str=java_paths_str, main_path=MAIN_PATH)
    if not options.verbose:
        cmd += ' > ' + os.devnull

    playerA, playerB = options.players.split('-')
    scoreA = 0
    scoreB = 0
    winsA = 0
    winsB = 0
    results_filename = os.path.join(options.target_dir, RESULTS_FILE_FORMAT.format(playerA=playerA, playerB=playerB))

    print(MESSAGE_FORMAT.format(**vars(options)))
    print("command to be executed:\n" + cmd)
    bar = create_progressbar()
    for iter_id in bar(range(options.n_iters)):
        call(cmd, shell=True)
        result = check_output(results_filename)
        if result[0] > result[1]:
            winsA += 1
        elif result[1] > result[0]:
            winsB += 1
        scoreA += result[0]
        scoreB += result[1]
    print(playerA, playerB)
    print(scoreA, scoreB)
    print(winsA, winsB)

if __name__ == '__main__':
    main()
