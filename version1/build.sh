#!/bin/sh
PY_DIR="/home/afeltes/toluca/source/toluca"
CLASSPATH_="/var/www/java/jdom.jar"
cd  $PY_DIR
echo -n "Limpiando..."
find py -name "*.class" | xargs rm -f
echo "listo"
echo -n "Compilando..."
javac -classpath $CLASSPATH_ `find -type f -name *.java`
echo "listo"
echo -n "Creando toluca.jar "
jar cfm toluca.jar toluca-manifest.mf `find py -name *.class` \
	`find py -iname *.gif` `find py -iname *.jpg`
echo "listo"

