#!/bin/bash

for required_command in solc web3j
do
    if ! [ -x "$(command -v $required_command)" ]; then
      echo "Error: ${required_command} is not installed." >&2
      exit 1
    fi
done

usage="$(basename "$0") [-h] [-c <string>] [-p <string>] [-o <string>] -- generate Java-code for Solidity contracts

where:
    -h  show this help
    -c  path to *.sol contract
    -p  package name for your java class
    -o  output directory "


while getopts ':h:c:p:o:' option; do
  case "$option" in
    h) echo "$usage"
       exit
       ;;
    c) contract_path=$OPTARG
       ;;
    p) package_name=$OPTARG
        ;;
    o) output_path=$OPTARG
        ;;
    :) printf "missing argument for -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
   \?) printf "illegal option: -%s\n" "$OPTARG" >&2
       echo "$usage" >&2
       exit 1
       ;;
  esac
done
shift $((OPTIND - 1))

if [ -z "${contract_path}" ] || [ -z "${package_name}" ] || [ -z "${output_path}" ]; then
       echo "$usage" >&2
       exit 1
fi

WORK_DIR=`mktemp -d`

# check if tmp dir was created
if [[ ! "$WORK_DIR" || ! -d "$WORK_DIR" ]]; then
  echo "Could not create temp dir"
  exit 1
fi

# deletes the temp directory
function cleanup {
  rm -rf "$WORK_DIR"
  echo "Deleted temp working directory $WORK_DIR"
}

# register the cleanup function to be called on the EXIT signal
trap cleanup EXIT

solc $contract_path --bin --abi --optimize -o $WORK_DIR
web3j solidity generate -b $WORK_DIR/*.bin -a $WORK_DIR/*.abi -o $output_path -p $package_name
