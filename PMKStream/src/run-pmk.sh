#!/bin/bash
#SBATCH --ntask=1
#SBATCH --qos=qos-7d
#SBTACH --cpus-per-task=8
#SBATCH --partition=medium

module load jdk8

expr=$1 #3
base=$2 #0
stacks=1 #2
threads=1 #4
queries=$3 #1

java main/RunMKStream $base $queries $stacks $expr $threads

