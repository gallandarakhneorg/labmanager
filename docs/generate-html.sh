#!/usr/bin/env bash

CDIR=`dirname $0`
ODIR="`pwd`/html"

echo "CDIR: $CDIR"
echo "ODIR: $ODIR"

rm -rf "$ODIR/html"
mkdir -p "$ODIR"

echo "Copying images to ODIR..."
cp "$CDIR/"*.png "$ODIR/"

echo "Generating HTML from Markdown..."
for MD in "$CDIR/"*.md
do
	BN="`basename $MD .md`.html"
	echo "  `basename $MD` -> $BN"
	echo "<html><head><link rel="stylesheet" href="styles.css"></head><body>" > "$ODIR/$BN"
	perl -pe 's/\[(.*?)\]\((.*?)\.md\)/[\1](\2.html)/g' < "$MD" | markdown --html4tags >> "$ODIR/$BN"
	echo "</body></html>" >> "$ODIR/$BN"
done

echo "Generating CSS styles..."
echo "img {max-width:100%;display: block;margin-left:auto;margin-right:auto;border:thick double #32a1ce}" > "$ODIR/styles.css"


