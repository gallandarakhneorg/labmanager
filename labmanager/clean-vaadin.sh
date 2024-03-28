#!/usr/bin/env bash

CDIR=`dirname $0`

echo "Deleting Vaadin files in the project..."
rm -rf "$CDIR/frontend/generated"
rm -rf "$CDIR/src/main/dev-bundle"
rm -rf "$CDIR/node_modules"
rm -f "$CDIR/package-lock.json"
rm -f "$CDIR/package.json"
rm -f "$CDIR/tsconfig.json"
rm -f "$CDIR/"*.ts
rm -f "$CDIR/"derby.log
echo "done"

if test "-$1" = "--a"
then
	echo "Deleting Vaadin files in user home..."
	rm -rf "$HOME/.vaadin"
	echo "done"
fi

