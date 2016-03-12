#!/bin/sh

HERE=$(cd -P -- "$(dirname -- "$0")" && printf '%s\n' "$(pwd -P)/")

gradle run -Pargs="-d $1 -classpath $2:/home/fs/markus/SDK/android-sdk/platforms/android-19/android.jar $3"
