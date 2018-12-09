<h1 align="center">Labyrinth Intelligence & Analysis</h1>
<h3 align="center">Multi Agent System + Data mining on Simulations</h3>


<p align="center"><img src="https://i.imgur.com/QddRdca.png" width="400px"></p>

## Instructions

#### To build:
```bash
$ ./gradlew build
```

#### To run:
```bash
# in project root
$ ./gradlew run
```

This will run with a stack size of 512MiB.

To run with a configuration file (YAML or JSON), specify the file path as the first argument:
```bash
$ gradle run --args="configurations/agents.yaml"
# or
$ gradle run --args="configurations/defaults.json"
```

#### To package

Simply run:
```bash
$ ./gradlew jar
```

This will create a `<name>.jar` file in the `./build/libs` directory.

This is a dependency free `.jar` which means you only need the `.jar` and a java runtime to execute it since all dependencies and resources are already included in the zip.

You can then run 
```bash
$ java -jar <path to jar>/<name>.jar [configuration file]
```

Or with a custom Stack size

```bash
$ java -Xss<MiBAmount>m -jar <path to jar>/<name>.jar [configuration file]
```


## Configuration Example

### Configuration File

YAML:
```yaml
seed: 1
batchMode: true
mazeHeight: 50
mazeLength: 50
numForwardAgents: 1
numBacktrackAgents: 0
numRandomAgents: 0
numNegotiatingAgents: 5
```

### CLI arguments

You can add "any" (if synchronized) arguments to the CLI that you would to the configuration file. To add a seed you can add an argument `-seed=13` for a seed of 13. 
If you supply only one argument without a flag `-` a config file is used. If you supply a flag the arguments are used.

Full Example:

```bash
$ gradle run --args="-batchMode=true -mazeHeight=30 -mazeLength=30 -seed=1 -numForwardAgents=2 -numBacktrackAgents=2 -numRandomAgents=2 -numNegotiatingAgents=2 -numSwarmAgents=0 -slownessRate=10 -statisticsPath="metrics/new.csv""
```


## Scripts 

To run the script with the experiments run for example:

```bash
$ python scripts/big-experiments.py build/libs/aiad-labyrinth-0.1-SNAPSHOT.jar y
```
