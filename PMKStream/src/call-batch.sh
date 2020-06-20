#!/bin/bash

sbatch ./run-pmk.sh 2 icde 50000 &
sbatch ./run-pmk.sh 2 isfdb 50000 &
sbatch ./run-pmk.sh 2 sigmod 50000 &

sbatch ./run-pmk.sh 3 icde 50000 &
sbatch ./run-pmk.sh 3 isfdb 50000 &
sbatch ./run-pmk.sh 3 sigmod 50000 &

sbatch ./run-pmk.sh 4 icde 50000 &
sbatch ./run-pmk.sh 4 isfdb 50000 &
sbatch ./run-pmk.sh 4 sigmod 50000 &

sbatch ./run-pmk.sh 5 icde 50000 &
sbatch ./run-pmk.sh 5 isfdb 50000 &
sbatch ./run-pmk.sh 5 sigmod 50000 &

sbatch ./run-pmk.sh 6 icde 50000 &
sbatch ./run-pmk.sh 6 isfdb 50000 &
sbatch ./run-pmk.sh 6 sigmod 50000 &

sbatch ./run-pmk.sh 8 icde 50000 &
sbatch ./run-pmk.sh 8 isfdb 50000 &
sbatch ./run-pmk.sh 8 sigmod 50000 &

(sbatch ./run-pmk.sh 7 icde 10000 &&
sbatch ./run-pmk.sh 7 icde 20000 &&
sbatch ./run-pmk.sh 7 icde 30000 &&
sbatch ./run-pmk.sh 7 icde 40000 &&
sbatch ./run-pmk.sh 7 icde 50000) &

(sbatch ./run-pmk.sh 7 isfdb 10000 &&
sbatch ./run-pmk.sh 7 isfdb 20000 &&
sbatch ./run-pmk.sh 7 isfdb 30000 &&
sbatch ./run-pmk.sh 7 isfdb 40000 &&
sbatch ./run-pmk.sh 7 isfdb 50000) &

(sbatch ./run-pmk.sh 7 sigmod 10000 &&
sbatch ./run-pmk.sh 7 sigmod 20000 &&
sbatch ./run-pmk.sh 7 sigmod 30000 &&
sbatch ./run-pmk.sh 7 sigmod 40000 &&
sbatch ./run-pmk.sh 7 sigmod 50000) &