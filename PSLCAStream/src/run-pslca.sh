#!/bin/bash
#SBATCH --ntask=1
#SBATCH --qos=qos-7d
#SBTACH --cpus-per-task=8
#SBATCH --partition=large

module load jdk8

#mkdir /tmp/files-$JOBID

#mv * /tmp/files-$JOBID

java main/Main 3 xmark 2

#mv /tmp/files-$JOBID/* ./
