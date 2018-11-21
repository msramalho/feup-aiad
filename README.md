# FEUP AIAD: Labyrinth

Labyrinth Project

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


## Configuration File Example
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