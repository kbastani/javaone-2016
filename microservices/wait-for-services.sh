#! /bin/bash

done=false

VERBOSE=false

if [ "$1" = "-v" ] ; then
 VERBOSE=true
 shift
fi

host=$1
shift
ports=$*

while [[ "$done" = false ]]; do
	for port in $ports; do
	    URL="http://${host}:${port}/health"
	    if [ $VERBOSE = "true" ] ; then
	      echo $URL
	    fi
		curl -q $URL >& /dev/null
		if [[ "$?" -eq "0" ]]; then
#		    echo $URL succeeded
			done=true
		else
#		    echo $URL failed
			done=false
			break
		fi
	done
	if [[ "$done" = true ]]; then
		echo connected
		break;
    fi
	echo -n .
	sleep 1
done