#!/usr/bin/env python3
import os
from subprocess import call
from glob import glob
import re
RESULTS_FILE = r'Heuristic-Heuristic.txt'
CMD_FORMAT = r'java -cp "{java_paths_str}" {main_path}'
MAIN_PATH = r"gr.auth.ee.dsproject.proximity.board.MainPlatform"
NUMBER_OF_ITERATIONS = 50

def check_output():
    with open(RESULTS_FILE) as f:
        s = f.read()
    return [int(x) for x in re.search(r'(\d{1,4})-(\d{1,4})', s).groups()]

def main():
    scoreA = 0
    scoreB = 0
    winsA = 0
    winsB = 0
    cur_dir = os.getcwd()
    runnable_lib = os.path.join(cur_dir, 'runnable_lib', '*')
    java_paths = glob(runnable_lib) + [os.path.join(cur_dir, 'bin')]
    java_paths_str = os.path.pathsep.join(java_paths)
    cmd = CMD_FORMAT.format(java_paths_str=java_paths_str, main_path=MAIN_PATH)
    print(cmd)

    for iter_id in range(NUMBER_OF_ITERATIONS):
        call(cmd, shell=True)
        result = check_output()
        if result[0] > result[1]:
            winsA += 1
        elif result[1] > result[0]:
            winsB += 1
        scoreA += result[0]
        scoreB += result[1]
    print(scoreA, scoreB)
    print(winsA, winsB)

if __name__ == '__main__':
    main()
