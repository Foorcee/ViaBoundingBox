#!/usr/bin/env bash
versions=(1.13.2 1.14.4 1.15.1)
buildtools="https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"
echo "Downloading BuildTools Sources"
curl -s -o "BuildTools.jar" $buildtools
for version in ${versions[*]}
do
    mkdir -p "$version"
    cp "BuildTools.jar" "$version/"
    cd "$version/"
    echo "Downloading Version $version"
    java -jar "BuildTools.jar" --rev $version
    cd ..
done