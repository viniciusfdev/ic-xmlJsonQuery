#!/bin/bash
#SBATCH --ntask=1
#SBATCH --qos=qos-7d
#SBTACH --cpus-per-task=8
#SBATCH --partition=large

module load jdk8

expr=$1
base=$2
nTouL=''

if [ "$#" -gt 2 ]; then
	nTouL=$3
	java main/Main $expr $base $nTouL
else
	java main/Main $expr $base
fi
