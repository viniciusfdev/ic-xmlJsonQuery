#!/bin/bash

sbatch ./run-pmk.sh 2 icde 50000 &
sbatch ./run-pmk.sh 3 icde 50000 &
sbatch ./run-pmk.sh 4 icde 50000 &
sbatch ./run-pmk.sh 5 icde 50000 &

sbatch ./run-pmk.sh 6 icde 50000 &
sbatch ./run-pmk.sh 7 icde 50000 &
sbatch ./run-pmk.sh 8 icde 50000 &

sbatch ./run-pmk.sh 7 icde 10000 &
sbatch ./run-pmk.sh 7 icde 20000 &
sbatch ./run-pmk.sh 7 icde 30000 &
sbatch ./run-pmk.sh 7 icde 40000 &

