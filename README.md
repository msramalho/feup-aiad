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

To run with a configuration file (YAML or JSON), specify the file path as the first argument:
```bash
$ ./gradlew run --args="configurations/agents.yaml"
# or
$ ./gradlew run --args="configurations/defaults.json"
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