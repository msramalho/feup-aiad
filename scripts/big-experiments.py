import sys
import random as rand
import subprocess
import os
import time
import shutil 

########################
#### Config
########################


# Maze Dimensions
# (x,y) or (width, height)
DIMENSIONS = [
    (10,10)
]

# Number of agents of each type
# (Random, Forward, Backtrack, Negotiating, Swarm)
AGENTS = [
    (100, 100, 100, 100, 0)
]

# number of times to run each experiment with different random seeds
NUM_SEEDS = 1

EXECUTIONS_SETS = [
    (DIMENSIONS, AGENTS, NUM_SEEDS)
]

SAVE_DIR = "metrics/big"
ZIP_PATH=f"{SAVE_DIR}.zip"
LOGFILE_PATH=f"{SAVE_DIR}_log_{time.time()}.txt"

STACK_SIZE_MB=100


########################
#### Implementation
########################

if __name__ != "__main__":
    sys.exit(1)

if len(sys.argv) == 1:
    print("Invalid use: specify the file path to the program executable and optionally a 'y' to clean and zip the results, must run from project root")
    sys.exit(1)

# dont change
MAX_SEED = 2**31
FNULL = open(os.devnull, 'w')

jar_path = sys.argv[1]
do_extra_tasks = len(sys.argv) == 3 and sys.argv[2] == "y" # extra arg

log_file = open(LOGFILE_PATH, "w")

def log(msg):
    print(msg)
    log_file.write(msg)
    log_file.write("\n")


def set_execution(dimensions, agents, num_seeds):
    for (x, y) in dimensions:
        for (random, forward, backtrack, negotiating, swarm) in agents:
            # put at the top level to have 'static' seeds that are only generated once
            seeds = list(map(lambda x: rand.randint(1, MAX_SEED), range(0, num_seeds)))
            for seed in seeds:
                savePathStr = f"{SAVE_DIR}/Dx{x}y{y}_R{random}F{forward}B{backtrack}N{negotiating}S{swarm}_SEED{seed}.csv"
                arguments = [
                    "-batchMode=true",
                    f"-seed={seed}", 
                    f"-mazeLength={x}", 
                    f"-mazeHeight={y}", 
                    f"-numForwardAgents={forward}", 
                    f"-numBacktrackAgents={backtrack}", 
                    f"-numRandomAgents={random}", 
                    f"-numNegotiatingAgents={negotiating}", 
                    f"-numSwarmAgents={swarm}", 
                    f"-statisticsPath={savePathStr}"
                ]
            
                log(f">> Running experiment for file {savePathStr}")
                allArgs = ["java", f"-Xss{STACK_SIZE_MB}m", "-jar", jar_path] + arguments
                start = time.time()
                code = subprocess.call(allArgs, stdout=FNULL)
                if code != 0:
                    commandStr = " ".join(allArgs)
                    log("An ERROR occured, failed to execute command: {commandStr}")
                    sys.exit(1)
                end = time.time()
                log("== Took {0:.1f} sec".format(end - start))

# clean folder
if do_extra_tasks:
    log("Removing old files")
    shutil.rmtree(SAVE_DIR, True)
    try:
        os.remove(ZIP_PATH)
    except OSError:
        pass

start_total = time.time()

# experiments execution
for (dimensions, agents, num_seeds) in EXECUTIONS_SETS:
    set_execution(dimensions, agents, num_seeds)

end_total = time.time()
log("All executed successfully, Total execution time {0:.1f} sec, check {1} path for results. ".format(end_total - start_total, SAVE_DIR))

# zip results
if do_extra_tasks:
    log("Zipping files")
    code = subprocess.call(["zip", "-r", ZIP_PATH, SAVE_DIR])
    if code != 0:
        log("Failed to zip results. They still exist")
        sys.exit(1)

