#!/bin/bash

sbatch ./run-pslca.sh 2 icde 0 &
sbatch ./run-pslca.sh 2 icde 1 &
sbatch ./run-pslca.sh 2 icde 2 &
sbatch ./run-pslca.sh 2 icde 3 &
sbatch ./run-pslca.sh 1 icde &
sbatch ./run-pslca.sh 3 icde 2 &
sbatch ./run-pslca.sh 3 icde 4 &
sbatch ./run-pslca.sh 3 icde 6 &
