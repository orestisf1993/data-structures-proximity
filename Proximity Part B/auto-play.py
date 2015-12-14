#!/usr/bin/env python3
import os
from subprocess import call
import re
RESULTS_FILE = r'Heuristic-old.txt'
CMD = r"java -cp './runnable_lib/*:./bin' gr.auth.ee.dsproject.proximity.board.MainPlatform"
NUMBER_OF_ITERATIONS = 50

def check_output():
    with open(RESULTS_FILE) as f:
        s = f.read()
    return [int(x) for x in re.search(r'(\d{1,4})-(\d{1,4})', s).groups()]

if __name__ == '__main__':
    scoreA = 0
    scoreB = 0
    winsA = 0
    winsB = 0
    for iter_id in range(NUMBER_OF_ITERATIONS):
        call(CMD, shell=True)
        result = check_output()
        if result[0] > result[1]:
            winsA += 1
        elif result[1] > result[0]:
            winsB += 1
        scoreA += result[0]
        scoreB += result[1]
    print(scoreA, scoreB)
    print(winsA, winsB)
